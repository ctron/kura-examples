/*******************************************************************************
 * Copyright (c) 2018 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/

package de.dentrassi.kura.examples.camel4;

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.camel.CamelContext;
import org.apache.camel.core.osgi.OsgiDefaultCamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component(enabled = true, immediate = true)
public class CamelApplicationComponent {

    private DefaultCamelContext context;
    private final State state;
    private ServiceRegistration<CamelContext> registration;

    public CamelApplicationComponent() {
        this.state = new State();
    }

    @Activate
    public void start(final BundleContext context) throws Exception {
        this.context = new OsgiDefaultCamelContext(context, SwaggerUi.createRegistry());
        this.context.addRoutes(new Routes(this.state));
        this.context.start();

        final Dictionary<String, Object> properties = new Hashtable<>();
        properties.put("camel.context.id", "camel.example.4");
        this.registration = context.registerService(CamelContext.class, this.context, properties);
    }

    @Deactivate
    public void stop() throws Exception {
        if (this.registration != null) {
            this.registration.unregister();
            this.registration = null;
        }

        if (this.context != null) {
            this.context.stop();
            this.context = null;
        }
    }
}
