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

import de.dentrassi.kura.examples.camel4.Parameters

def p1 = request.body.collectMany { it.properties.findAll { it.key == 'P1'}.collect{it.value.value} }[0]
def p2 = request.body.collectMany { it.properties.findAll { it.key == 'P2'}.collect{it.value.value} }[0]

return new Parameters(p1, p2)
