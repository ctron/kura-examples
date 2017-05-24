/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package de.dentrassi.kura.examples.camel2;

import static org.eclipse.kura.camel.component.Configuration.asInt;
import static org.eclipse.kura.camel.component.Configuration.asString;
import static org.osgi.framework.FrameworkUtil.getBundle;
import static org.osgi.service.component.annotations.ConfigurationPolicy.REQUIRE;

import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jdbc.JdbcComponent;
import org.eclipse.kura.camel.runner.CamelRunner;
import org.eclipse.kura.camel.runner.RoutesProvider;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.crypto.CryptoService;
import org.eclipse.scada.utils.str.StringReplacer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * A Camel example Kura component
 * <p>
 * <strong>Note:</strong> The component must be marked as
 * {@code immediate = true} and {@code configurationPolicy = REQUIRE}.
 * </p>
 * <p>
 * This example uses the {@link CamelRunner} in order to start up a Camel
 * context in Kura. The runner gets configured and can then be started and
 * stopped. However starting the CamelRunner does not immediately start the
 * CamelContext. First it will wait until all required dependencies are
 * available in OSGi and then it will create and customize the Camel context.
 * For this methods like
 * {@link CamelRunner.Builder#addBeforeStart(org.eclipse.kura.camel.runner.BeforeStart)}
 * can be used. Setting the routes through e.g.
 * {@link CamelRunner#setRoutes(org.eclipse.kura.camel.runner.RoutesProvider))}
 * is possible either before or after starting the runner. If the runner is
 * already started then the new routes will be applied.
 * </p>
 * <p>
 * Bear in mind though that using the method
 * {@link CamelRunner#setRoutes(org.apache.camel.builder.RouteBuilder)} uses the
 * {@link RouteBuilder} class from Camel, which only gets initialized once. So
 * while the CamelRunner will call the
 * {@link RoutesProvider#applyRoutes(org.apache.camel.CamelContext)} method
 * multiple times (when required), the {@link RouteBuilder} class will only call
 * its {@link RouteBuilder#configure()} at the first time. So you will need to
 * pass in a different instance of your {@link RouteBuilder} implementation.
 * </p>
 *
 * @author Jens Reimann
 */
@Designate(ocd = Config.class)
@Component(immediate = true, configurationPolicy = REQUIRE)
public class CamelExampleConfigurableComponent implements ConfigurableComponent {

    private CryptoService cryptoService;

    /**
     * Set the crypto service
     * <p>
     * This is called by OSGi DS once the service is tracked. We cannot use
     * field ibjection as Kura doesn't support OSGi DS 1.3.
     * </p>
     * 
     * @param cryptoService
     *            the instance to use
     */
    @Reference
    public void setCryptoService(final CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    private CamelRunner runner;

    /**
     * Activate the component
     * <p>
     * <b>Note:</b> As Kura doesn't support OSGi DS 1.3, we cannot use
     * the metatype variant of the activation method and have to use
     * the properties map instead.
     * </p>
     * 
     * @param properties
     *            properties from DS
     * @throws Exception
     *             if anything goes wrong
     * 
     */
    @Activate
    public void activate(final Map<String, ?> properties) throws Exception {

        // fetch properties

        final String cloudServicePid = asString(properties, "cloudServicePid");
        final String databaseName = asString(properties, "databaseName");
        int port = asInt(properties, "port", 3306);
        final String host = StringReplacer.replace(asString(properties, "host", "localhost"), System.getenv());
        final String user = asString(properties, "user");
        String password = asString(properties, "password");

        // decode the password

        if (password != null) {
            try {
                password = String.valueOf(this.cryptoService.decryptAes(password.toCharArray()));
            } catch (final Exception e) {
            }
        }

        // create the runner

        this.runner = createRunner(cloudServicePid, databaseName, host, port, user, password);

        // set the routes and start

        if (this.runner != null) {
            // Set the routes provider, will be activated when the context is started
            this.runner.setRoutes(createRoutes());
            this.runner.start();
        }
    }

    private CamelRunner createRunner(final String cloudServicePid, final String databaseName, String host, int port,
            final String user, final String password) {

        final CamelRunner.Builder runner = new CamelRunner.Builder(
                getBundle(CamelExampleConfigurableComponent.class).getBundleContext());

        // JMX is disabled by default

        runner.disableJmx(false);

        // check if cloud service pid is null or empty
        if (cloudServicePid != null && !cloudServicePid.isEmpty()) {
            runner.cloudService("kura.service.pid", cloudServicePid);
        }

        runner.requireComponent("timer");
        runner.requireComponent("jdbc");

        runner.addBeforeStart(ctx -> {
            final JdbcComponent jdbc = new JdbcComponent(ctx);
            final MysqlDataSource dataSource = new MysqlDataSource();

            dataSource.setDatabaseName(databaseName);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            dataSource.setServerName(host);
            dataSource.setPort(port);

            jdbc.setDataSource(dataSource);
            ctx.addComponent("myjdbc", jdbc);
        });

        return runner.build();
    }

    private RouteBuilder createRoutes() {

        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from("timer://timer1?period=1000") //
                        .setBody(constant("select now()")) //
                        .to("myjdbc:component") //
                        .process(new MyProcessor()) //
                        .to("stream:out");
            }
        };
    }

    @Deactivate
    public void deactivate() {
        if (runner != null) {
            this.runner.stop();
            this.runner = null;
        }
    }

    @Modified
    public void modified(final Map<String, ?> properties) throws Exception {
        /*
         * We are lazy here and simply re-configure by restarting
         */
        deactivate();
        activate(properties);
    }
}
