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

import static io.glutamate.lang.Exceptions.wrap;
import static java.util.Collections.singletonMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import com.google.gson.GsonBuilder;

import io.glutamate.util.Collections;

public class CamelApplicationComponentTest extends CamelTestSupport {

    private State state;

    @Produce(uri = "seda:wiresInput1")
    private ProducerTemplate wiresInput1;

    @EndpointInject(uri = "mock:seda:wiresOutput1")
    private MockEndpoint wiresOutput1;

    @EndpointInject(uri = "mock:iec60870-server:0.0.0.0:2404/1-0-0-0-1")
    private MockEndpoint iecP1;

    @EndpointInject(uri = "mock:iec60870-server:0.0.0.0:2404/1-0-0-0-2")
    private MockEndpoint iecP2;

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new Routes(this.state);
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        final JndiRegistry result = super.createRegistry();
        SwaggerUi.fillRegistry(result::bind);
        return result;
    }

    @Override
    protected void doPreSetup() throws Exception {
        this.state = new State();

        this.state.getCurrentParameters().setSetpoint1(1.0);
        this.state.getCurrentParameters().setSetpoint2(2.0);
    }

    @Override
    protected void doPostSetup() throws Exception {
        new ArrayList<>(this.context.getRouteDefinitions())
                .forEach(wrap(route -> route.adviceWith(this.context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        mockEndpoints();
                    }
                })));
    }

    @Test
    public void testGetParameters() {

        final String result = fluentTemplate()
                .to("direct:getParameters")
                .request(String.class);

        assertNotNull(result);

        final Map<?, ?> actual = new GsonBuilder().create().fromJson(result, Map.class);
        assertEquals(Collections.map(map -> {
            map.put("setpoint1", 1.0);
            map.put("setpoint2", 2.0);
        }), actual);
    }

    @Test
    public void testUpdateParametersFromWires() throws InterruptedException {

        final List<MockRecord> records = new LinkedList<>();
        final Map<String, TypedValueMock> properties = singletonMap("P1", new TypedValueMock(5.0));
        records.add(new MockRecord(properties));

        this.wiresInput1.sendBody(records);

        // then

        this.wiresOutput1.setExpectedCount(1);
        this.iecP1.expectedBodiesReceived(5.0);
        this.iecP2.expectedBodiesReceived(2.0);

        // assert mocks

        assertMockEndpointsSatisfied();

        // test if the update changed our state

        assertEquals(Double.valueOf(5.0), this.state.getCurrentParameters().getSetpoint1());
        assertEquals(Double.valueOf(2.0), this.state.getCurrentParameters().getSetpoint2());
    }

}
