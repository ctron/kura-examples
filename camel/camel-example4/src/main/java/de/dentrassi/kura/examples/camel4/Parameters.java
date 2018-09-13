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

import com.google.common.base.MoreObjects;

public class Parameters {
    private Double setpoint1;
    private Double setpoint2;

    public Parameters(final Parameters other) {
        this.setpoint1 = other.setpoint1;
        this.setpoint2 = other.setpoint2;
    }

    public Parameters(final Double setpoint1, final Double setpoint2) {
        this.setpoint1 = setpoint1;
        this.setpoint2 = setpoint2;
    }

    public Parameters() {
    }

    public Double getSetpoint1() {
        return setpoint1;
    }

    public void setSetpoint1(final Double setpoint1) {
        this.setpoint1 = setpoint1;
    }

    public Double getSetpoint2() {
        return setpoint2;
    }

    public void setSetpoint2(final Double setpoint2) {
        this.setpoint2 = setpoint2;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("setpoint1", this.setpoint1)
                .add("setpoint2", this.setpoint2)
                .toString();
    }

}
