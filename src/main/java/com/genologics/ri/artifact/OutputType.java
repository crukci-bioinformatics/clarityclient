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

package com.genologics.ri.artifact;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum OutputType
{
    @XmlEnumValue("Sample")
    SAMPLE("Sample"),

    @XmlEnumValue("Analyte")
    ANALYTE("Analyte"),

    @XmlEnumValue("ResultFile")
    RESULT_FILE("ResultFile"),

    @XmlEnumValue("SharedResultFile")
    SHARED_RESULT_FILE("SharedResultFile"),

    @XmlEnumValue("SearchResultFile")
    SEARCH_RESULT_FILE("SearchResultFile"),

    @XmlEnumValue("SpotList")
    SPOT_LIST("SpotList");

    private final String value;

    OutputType(String v)
    {
        value = v;
    }

    public String value()
    {
        return value;
    }

    public static OutputType fromValue(String v)
    {
        if (v == null)
        {
            throw new NullPointerException("Output type cannot be null");
        }

        for (OutputType c : OutputType.values())
        {
            if (c.value.equals(v))
            {
                return c;
            }
        }
        throw new IllegalArgumentException("'" + v + "' is not a known output type.");
    }

}
