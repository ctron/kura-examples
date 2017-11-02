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
package de.dentrassi.kura.examples.camel3;

import static org.eclipse.kura.camel.runner.CamelRunner.createOsgiRegistry;
import static org.osgi.framework.FrameworkUtil.getBundle;
import static org.osgi.service.component.annotations.ConfigurationPolicy.REQUIRE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.io.IOUtils;
import org.eclipse.kura.camel.bean.PayloadFactory;
import org.eclipse.kura.camel.component.Configuration;
import org.eclipse.kura.camel.runner.CamelRunner;
import org.eclipse.kura.camel.runner.RoutesProvider;
import org.eclipse.kura.camel.runner.XmlRoutesProvider;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

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
 * <p>
 * Although this component does not make any use of the configuration service,
 * it is registered as {@link ConfigurableComponent} to be visible in the Web
 * UI. However that would not be necessary, this component would work fine
 * without implementing this interface.
 * </p>
 *
 * @author Jens Reimann
 */
@Designate(ocd = Config.class)
@Component(immediate = true, configurationPolicy = REQUIRE)
public class CamelExampleConfigurableComponent implements ConfigurableComponent {

    private CamelRunner runner;

    private static CamelRunner createRunner(final Map<String, ?> properties) throws IOException {
        final Map<String, Object> services = new HashMap<>();
        services.put("payloadFactory", new PayloadFactory());

        final BundleContext ctx = getBundle(CamelExampleConfigurableComponent.class)
            .getBundleContext();

        final CamelRunner.Builder builder = new CamelRunner.Builder(ctx)

            // JMX is disabled by default

            .disableJmx(false)

            // wait for Camel components: stream, timer

            .requireComponent("stream")
            .requireComponent("timer")

            // register out payload factory

            .registryFactory(createOsgiRegistry(ctx, services));

        // set the cloud service PID

        final String cloudServicePid = Configuration.asString(properties, "cloudServicePid");

        if (cloudServicePid != null && !cloudServicePid.isEmpty()) {
            builder.cloudService("kura.service.pid", cloudServicePid);
        }

        // build the runner

        final CamelRunner runner = builder.build();

        // Set the routes provider, will be activated when the context is started

        runner.setRoutes(new XmlRoutesProvider(
                IOUtils.toString(CamelExampleConfigurableComponent.class.getResource("routes.xml"))));

        return runner;
    }

    @Activate
    public void activate(final Map<String, ?> properties) throws IOException {
        this.runner = createRunner(properties);
        this.runner.start();
    }

    @Deactivate
    public void deactivate() {
        if (this.runner != null) {
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
