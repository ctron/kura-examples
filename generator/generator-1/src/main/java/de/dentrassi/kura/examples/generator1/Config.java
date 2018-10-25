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
package de.dentrassi.kura.examples.generator1;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Meta type information for {@link GeneratorComponent}
 */
@ObjectClassDefinition(id = "de.dentrassi.kura.examples.generator1.GeneratorComponent", name = "Generator #1", description = "This is a sawtooth generator")
@interface Config {

    @AttributeDefinition(name = "Enabled", description = "Whether the component is enabled or not")
    boolean enabled() default true;

    @AttributeDefinition(name = "Minimum", description = "The minimum value")
    double minValue() default 0.0;

    @AttributeDefinition(name = "Maximum Value", description = "The maximum value")
    double maxValue() default 5000.0;

    @AttributeDefinition(name = "Period", description = "The period in milliseconds")
    long period() default 60_000;

    @AttributeDefinition(name = "Publish rate", description = "The publish rate in milliseconds")
    long publishRate() default 1000;

}
