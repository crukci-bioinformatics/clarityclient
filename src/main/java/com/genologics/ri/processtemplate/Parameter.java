/*
 * CRUK-CI Genologics REST API Java Client.
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

package com.genologics.ri.processtemplate;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * The parameter is a child element of process-template that integrates the
 * Process this Process Template represents, with the External Program
 * Integration plug-in (EPP). When a user runs the Process built from this
 * Process Template, the system automatically issues a command configured in the
 * process parameter this element represents.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "parameter")
public class Parameter implements Serializable
{
    private static final long serialVersionUID = -8795139448974204288L;

    @XmlAttribute(name = "name")
    protected String name;

    public Parameter()
    {
    }

    public Parameter(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        this.name = value;
    }

}
