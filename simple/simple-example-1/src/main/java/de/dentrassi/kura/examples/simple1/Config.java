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
package de.dentrassi.kura.examples.simple1;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Meta type information for {@link ExampleConfigurableComponent}
 * <p>
 * <strong>Note: </strong> The id must be the full qualified name of the assigned component.
 * </p>
 */
@ObjectClassDefinition(id="de.dentrassi.kura.examples.simple1.ExampleConfigurableComponent", name="Simple Example #1", description="This is the simple example #1 component")
@interface Config {
	@AttributeDefinition(name = "Some string", description = "This is just some string value")
	String someString() default "Hello World";

	@AttributeDefinition(name = "Enabled", description = "Whether the component is enabled or not")
	boolean enabled() default true;
}
