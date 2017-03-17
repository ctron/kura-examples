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

import static org.osgi.service.component.annotations.ConfigurationPolicy.REQUIRE;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.kura.configuration.ConfigurableComponent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

/**
 * A simple example Kura component
 * <p>
 * <strong>Note:</strong> The component must be marked as
 * {@code immediate = true} and {@code configurationPolicy = REQUIRE}.
 * </p>
 * 
 * @author Jens Reimann
 */
@Designate(ocd = Config.class)
@Component(immediate = true, configurationPolicy = REQUIRE)
public class ExampleConfigurableComponent implements ConfigurableComponent {

	@Activate
	public void activate(Map<String, Object> properties) {
		dumpProperties("activate", properties);
	}

	@Modified
	public void modified(Map<String, Object> properties) {
		dumpProperties("modified", properties);
	}

	@Deactivate
	public void deactivate() {
		System.out.println("deactivate");
	}

	private void dumpProperties(String operation, final Map<String, Object> properties) {
		final Map<String, Object> sortedMap = new TreeMap<>(properties);

		System.out.format("=========== %s ===========%n", operation);
		for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
			System.out.format("\t'%s' = '%s'%n", entry.getKey(), entry.getValue());
		}
		System.out.format("=========== %s ===========%n", operation);
	}
}
