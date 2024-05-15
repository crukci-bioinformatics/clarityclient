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

package com.genologics.ri.container;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * An enumeration of the values possible in the
 * {@link Container#state} attribute. This enumeration
 * isn't actually an enumeration in the XML schemas, but it has
 * a finite set of possible values and it's easier to work with
 * an enumeration in Java code than just a string.
 *
 * @since 2.31.6
 */
@XmlType(name = "container-state")
@XmlEnum
public enum ContainerState
{
    /**
     * The container state when the container is empty.
     */
    @XmlEnumValue("Empty")
    EMPTY("Empty", 1),

    /**
     * The container state when the container has something in it.
     */
    @XmlEnumValue("Populated")
    POPULATED("Populated", 2),

    /**
     * The container state when the container is depleted.
     */
    @XmlEnumValue("Depleted")
    DEPLETED("Depleted", 3),

    /**
     * The container state when the container has been discarded.
     */
    @XmlEnumValue("Discarded")
    DISCARDED("Discarded", 4),

    /**
     * The container state when the container only container reagents.
     */
    @XmlEnumValue("Reagent Only")
    REAGENT_ONLY("Reagent Only", 5),

    /**
     * The container state when the container is newly created.
     */
    @XmlEnumValue("New")
    NEW("New", 6),

    /**
     * The container state when the container's contents have been hybridized.
     */
    @XmlEnumValue("Hybridized")
    HYBRIDIZED("Hybridized", 7),

    /**
     * The container state when the container's contents have been scanned.
     */
    @XmlEnumValue("Scanned")
    SCANNED("Scanned", 8);


    /**
     * The display name of the state.
     */
    public final String displayName;

    /**
     * The numeric state id for the container state, as stored in the database.
     */
    public final int stateId;

    /**
     * Private constructor.
     *
     * @param display The display name.
     */
    private ContainerState(String display, int state)
    {
        displayName = display;
        stateId = state;
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
     * Get the container state that has the given display name.
     *
     * @param str The display name, as returned from Clarity calls.
     *
     * @return The matching process state, or null if {@code str} is null.
     *
     * @throws IllegalArgumentException if {@code str} does not match the
     * display name of a state.
     */
    public static ContainerState fromValue(String str)
    {
        if (str == null)
        {
            return null;
        }

        for (ContainerState state : ContainerState.values())
        {
            if (state.displayName.equals(str))
            {
                return state;
            }
        }

        throw new IllegalArgumentException('"' + str + "\" is not a recognised Clarity container state.");
    }
}
