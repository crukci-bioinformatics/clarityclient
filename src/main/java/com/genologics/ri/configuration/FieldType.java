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

package com.genologics.ri.configuration;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * UDF field types.
 */
@XmlType(name = "field-type")
@XmlEnum
public enum FieldType
{

    @XmlEnumValue("String")
    STRING("String"),

    @XmlEnumValue("Text")
    TEXT("Text"),

    @XmlEnumValue("Boolean")
    BOOLEAN("Boolean"),

    @XmlEnumValue("Numeric")
    NUMERIC("Numeric"),

    @XmlEnumValue("Date")
    DATE("Date"),

    @XmlEnumValue("URI")
    URI("URI");


    private final String value;

    FieldType(String v)
    {
        value = v;
    }

    public String value()
    {
        return value;
    }

    public static FieldType fromValue(String v)
    {
        for (FieldType c : FieldType.values())
        {
            if (c.value.equals(v))
            {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
