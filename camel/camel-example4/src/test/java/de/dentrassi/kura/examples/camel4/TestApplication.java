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

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class TestApplication {
    public static void main(final String[] args) throws Exception {
        final Main main = new Main();
        SwaggerUi.fillRegistry(main::bind);
        main.addRouteBuilder(new Routes(new State()));
        main.addRouteBuilder(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("seda:wiresOutput1")
                        .log("Update for Wires: ${body}");
            }
        });
        main.run(args);
    }
}
