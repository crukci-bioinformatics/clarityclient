/*
 * CRUK-CI Clarity REST API Java Client.
 * Copyright (C) 2013 Cancer Research UK Cambridge Institute.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.genologics.ri.step;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * An enumeration of the values possible in the
 * {@link ProcessStep#currentState} attribute. This enumeration
 * isn't actually an enumeration in the XML schemas, but it has
 * a finite set of possible values and it's easier to work with
 * an enumeration in Java code than just a string.
 */
@XmlType(name = "process-state")
@XmlEnum
public enum ProcessState
{
    /**
     * The step state when the step is in the sample placement stage.
     */
    @XmlEnumValue("Started")
    STARTED("Started"),

    /**
     * The step state when the step is in the sample placement stage.
     */
    @XmlEnumValue("Placement")
    PLACEMENT("Placement"),

    /**
     * The step state when the step is in the stage where barcodes are attached
     * to the samples.
     */
    @XmlEnumValue("Add Reagent")
    ADD_REAGENT("Add Reagent"),

    /**
     * The step state when the step is in the pooling stage.
     */
    @XmlEnumValue("Pooling")
    POOLING("Pooling"),

    /**
     * The step state when the step is in the record details stage.
     */
    @XmlEnumValue("Record Details")
    RECORD_DETAILS("Record Details"),

    /**
     * The step state when the step is in the next steps stage.
     */
    @XmlEnumValue("Assign Next Steps")
    NEXT_STEPS("Assign Next Steps"),

    /**
     * The step state when the step is complete.
     */
    @XmlEnumValue("Completed")
    COMPLETED("Completed");


    /**
     * The display name of the state.
     */
    public final String displayName;

    /**
     * Private constructor.
     *
     * @param display The display name.
     */
    private ProcessState(String display)
    {
        displayName = display;
    }

    @Override
    public String toString()
    {
        return displayName;
    }

    public String value()
    {
        return displayName;
    }

    /**
     * Get the process state that has the given display name.
     *
     * @param str The display name, as returned from Clarity calls.
     *
     * @return The matching process state, or null if {@code str} is null.
     *
     * @throws IllegalArgumentException if {@code str} does not match the
     * display name of a state.
     */
    public static ProcessState fromValue(String str)
    {
        if (str == null)
        {
            return null;
        }

        for (ProcessState state : ProcessState.values())
        {
            if (state.displayName.equals(str))
            {
                return state;
            }
        }

        throw new IllegalArgumentException('"' + str + "\" is not a recognised Clarity process state.");
    }
}
