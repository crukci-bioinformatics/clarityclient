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

package com.genologics.ri.containertype;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;

/**
 *
 * Calibrant-well is a child element of container type and identifies a well
 * location that is reserved for calibrants in containers of the container type.
 *
 * @deprecated This type is no longer supported.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "calibrant-well")
@Deprecated
public class CalibrantWell implements Serializable
{
    private static final long serialVersionUID = 218110772739169852L;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlValue
    protected String value;


    public CalibrantWell()
    {
    }

    public CalibrantWell(String name)
    {
        this.name = name;
    }

    public CalibrantWell(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
