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

import static org.osgi.service.metatype.annotations.AttributeType.PASSWORD;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Meta type information for {@link CamelExampleConfigurableComponent}
 * <p>
 * <strong>Note: </strong> The id must be the full qualified name of the assigned component. This is a requirement by
 * Kura.
 * </p>
 */
@ObjectClassDefinition(id = "de.dentrassi.kura.examples.camel2.CamelExampleConfigurableComponent", name = "Camel Example #2", description = "This is the Camel example #2 component")
@interface Config {

    @AttributeDefinition(name = "Cloud Service PID", required = false)
    public String cloudServicePid();

    @AttributeDefinition(name = "Database name")
    public String databaseName() default "camel";

    @AttributeDefinition(name = "Host", description = "The name or IP address of the database host. This value may use environment variables by using the syntax \"${VAR}\"")
    public String host() default "localhost";

    @AttributeDefinition(name = "Port")
    public int port() default 3306;

    @AttributeDefinition(name = "User name")
    public String user() default "camel";

    @AttributeDefinition(name = "Password", type = PASSWORD)
    public String password() default "password";
}
