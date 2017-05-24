/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc
 *******************************************************************************/
package de.dentrassi.kura.examples.camel2;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(MyProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Processing Meassage: {}", exchange.getIn().getBody(String.class));
        exchange.getIn().setBody(exchange.getIn().getBody(String.class));
    }

}
