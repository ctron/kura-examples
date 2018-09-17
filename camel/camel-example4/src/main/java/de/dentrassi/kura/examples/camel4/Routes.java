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

import java.util.Objects;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestParamType;

final class Routes extends RouteBuilder {

    private final State state;

    public Routes(final State state) {
        Objects.requireNonNull(state);
        this.state = state;
    }

    @Override
    public void configure() throws Exception {

        SwaggerUi.configureRoutes(this);

        restConfiguration()
                .component("jetty")
                .contextPath("/").port(8090)
                .apiContextPath("/api")
                .apiProperty("api.title", "Kura Camel Example #4")
                .apiProperty("api.version", "1.0.0");

        rest("/parameters/current")
                .description("Example API for local interfacing")

                .produces("application/json")
                .consumes("application/json")

                .get()
                .description("Get the current parameters")
                .outType(Parameters.class)
                .to("direct:getParameters")

                .put()
                .type(Parameters.class)
                .description("Set new parameters")
                .param().name("body").type(RestParamType.body)
                .description(
                        "The new parameters to set. This may contain null values, which are then filled with the currently active parameters.")
                .endParam()
                .outType(Parameters.class)
                .responseMessage().code(200).message("Applied new parameters").endResponseMessage()
                .description("The newly apply parameters, merged with the current parameters.")
                .to("direct:updateParameterAsJson");

        from("direct:getParameters")
                .routeId("getParameters")
                .setBody(method(this.state, "getCurrentParameters"))
                .marshal().json(JsonLibrary.Gson);

        from("direct:updateParameterAsJson")
                .routeId("updateParameterAsJson")
                .unmarshal().json(JsonLibrary.Gson, Parameters.class)
                .to("direct:updateParameters");

        from("direct:updateParameters")
                .routeId("updateParameters")
                .bean(this.state, "updateCurrentParameters")
                .multicast()
                .to("direct:update.wires", "direct:update.iec.p1", "direct:update.iec.p2").end();

        from("direct:update.wires")
                .routeId("update.wires")
                .transform().groovy("['P1': request.body.setpoint1, 'P2': request.body.setpoint2]")
                .to("seda:wiresOutput1?waitForTaskToComplete=Never");

        from("direct:update.iec.p1")
                .routeId("update.iec.p1")
                .transform().simple("body.setpoint1").to("iec60870-server:0.0.0.0:2404/1-0-0-0-1");

        from("direct:update.iec.p2")
                .routeId("update.iec.p2")
                .transform().simple("body.setpoint2").to("iec60870-server:0.0.0.0:2404/1-0-0-0-2");

        from("seda:wiresInput1")
                .routeId("wiresInput1")
                .transform().groovy("resource:classpath:de/dentrassi/kura/examples/camel4/toParameters.groovy")
                .to("direct:updateParameters");

        from("iec60870-server:0.0.0.0:2404/1-0-0-0-1")
                .convertBodyTo(Double.class)
                .process(exchange -> new Parameters(exchange.getIn().getBody(Double.class), null))
                .to("direct:updateParameters");

        from("iec60870-server:0.0.0.0:2404/1-0-0-0-2")
                .convertBodyTo(Double.class)
                .process(exchange -> new Parameters(null, exchange.getIn().getBody(Double.class)))
                .to("direct:updateParameters");
    }
}