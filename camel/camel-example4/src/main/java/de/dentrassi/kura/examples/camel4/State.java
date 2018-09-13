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

public final class State {

    private static final double DEFAULT_SETPOINT_1 = 0.0;
    private static final double DEFAULT_SETPOINT_2 = 0.0;

    private Parameters currentParameters;

    public State() {
        final Parameters initialParameters = new Parameters();
        initialParameters.setSetpoint1(DEFAULT_SETPOINT_1);
        initialParameters.setSetpoint2(DEFAULT_SETPOINT_2);
        this.currentParameters = initialParameters;
    }

    public Parameters getCurrentParameters() {
        return this.currentParameters;
    }

    public Parameters updateCurrentParameters(final Parameters updateRequest) {

        final Parameters newParameters = new Parameters(updateRequest);
        if (newParameters.getSetpoint1() == null) {
            newParameters.setSetpoint1(this.currentParameters.getSetpoint1());
        }
        if (newParameters.getSetpoint2() == null) {
            newParameters.setSetpoint2(this.currentParameters.getSetpoint2());
        }
        this.currentParameters = newParameters;

        return newParameters;
    }

}