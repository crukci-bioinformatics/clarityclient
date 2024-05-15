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

@XmlType(name = "action-type")
@XmlEnum
public enum ActionType
{

    @XmlEnumValue("leave")
    LEAVE("leave"),

    @XmlEnumValue("repeat")
    REPEAT("repeat"),

    @XmlEnumValue("remove")
    REMOVE("remove"),

    @XmlEnumValue("review")
    REVIEW("review"),

    @XmlEnumValue("complete")
    COMPLETE("complete"),

    @XmlEnumValue("store")
    STORE("store"),

    @XmlEnumValue("nextstep")
    NEXTSTEP("nextstep"),

    @XmlEnumValue("rework")
    REWORK("rework"),

    @XmlEnumValue("completerepeat")
    COMPLETE_REPEAT("completerepeat"),

    @XmlEnumValue("unknown")
    UNKNOWN("unknown");

    private final String value;

    ActionType(String v)
    {
        value = v;
    }

    public String value()
    {
        return value;
    }

    public static ActionType fromValue(String v)
    {
        for (ActionType c : ActionType.values())
        {
            if (c.value.equals(v))
            {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
