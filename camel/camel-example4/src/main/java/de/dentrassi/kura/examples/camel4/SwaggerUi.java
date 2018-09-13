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

import static io.glutamate.lang.ClassLoaders.runWithClassLoader;

import java.util.function.BiConsumer;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.spi.Registry;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

public final class SwaggerUi {

    private SwaggerUi() {
    }

    public static void fillRegistry(final BiConsumer<String, ? super Object> binder) {

        runWithClassLoader(SwaggerUi.class, () -> {

            final ResourceHandler staticHandler = new ResourceHandler();
            staticHandler.setBaseResource(Resource.newClassPathResource("web/swagger-ui"));

            binder.accept("staticHandler", staticHandler);

        });
    }

    public static Registry createRegistry() {

        final SimpleRegistry result = new SimpleRegistry();
        fillRegistry(result::put);
        return result;

    }

    public static void configureRoutes(final RouteBuilder routes) {

        routes
                .from("jetty:http://0.0.0.0:8090/?handlers=#staticHandler")
                .log(LoggingLevel.TRACE, "Static web request");

    }

}
