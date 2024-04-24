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

package com.genologics.ri.processtype;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "variability-type")
@XmlEnum
public enum VariabilityType
{

    @XmlEnumValue("Fixed")
    FIXED("Fixed"),

    @XmlEnumValue("Variable")
    VARIABLE("Variable"),

    @XmlEnumValue("VariableByInput")
    VARIABLE_BY_INPUT("VariableByInput");

    private final String value;

    VariabilityType(String v)
    {
        value = v;
    }

    public String value()
    {
        return value;
    }

    public static VariabilityType fromValue(String v)
    {
        for (VariabilityType c : VariabilityType.values())
        {
            if (c.value.equals(v))
            {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
