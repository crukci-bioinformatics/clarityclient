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

package com.genologics.ri.processexecution;

import static com.genologics.ri.Namespaces.PROCESS_EXECUTION_NAMESPACE;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(namespace = PROCESS_EXECUTION_NAMESPACE, name = "qc-flag")
@XmlEnum
public enum QCFlag
{

    UNKNOWN, PASSED, FAILED, CONTINUE;

    public String value()
    {
        return name();
    }

    public static QCFlag fromValue(String v)
    {
        return valueOf(v);
    }

}
