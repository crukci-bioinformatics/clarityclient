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

package org.cruk.clarity.api.automation;

import com.genologics.ri.step.ProcessStep;

/**
 * Enumeration of the process states Clarity returns.
 *
 * @see ProcessStep#getCurrentState()
 *
 * @since 2.31.2
 */
public enum ProcessState
{
    /**
     * The step state when the step is in the sample placement stage.
     */
    PLACEMENT("Placement"),

    /**
     * The step state when the step is in the stage where barcodes are attached
     * to the samples.
     */
    BARCODING("Add Reagent"),

    /**
     * The step state when the step is in the pooling stage.
     */
    POOLING("Pooling"),

    /**
     * The step state when the step is in the record details stage.
     */
    RECDETAILS("Record Details"),

    /**
     * The step state when the step is in the next steps stage.
     */
    NEXTSTEPS("Assign Next Steps"),

    /**
     * The step state when the step is complete.
     */
    COMPLETE("Completed");


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

    /**
     * Get the display name of the state.
     *
     * @return The display name.
     */
    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public String toString()
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
    public static ProcessState of(String str)
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

    /**
     * Get the current state of the given process step.
     *
     * @param step The process step object.
     *
     * @return The process step's current state.
     */
    public static ProcessState currentState(ProcessStep step)
    {
        return of(step.getCurrentState());
    }
}
