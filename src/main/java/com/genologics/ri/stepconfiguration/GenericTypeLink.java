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

package com.genologics.ri.stepconfiguration;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;

import org.apache.commons.lang3.ClassUtils;


/**
 * Generic-type is a lockable child element that provides a name linking an entity to the step.
 *
 * @since 2.25
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generic-type-link", propOrder = { "value" })
public class GenericTypeLink implements Serializable
{
    private static final long serialVersionUID = -7599295833392440499L;

    @XmlValue
    protected String value;

    @XmlAttribute(name = "locked")
    protected Boolean locked;


    public GenericTypeLink()
    {
    }

    public GenericTypeLink(String value)
    {
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

    public Boolean getLocked()
    {
        return locked;
    }

    public void setLocked(Boolean locked)
    {
        this.locked = locked;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(32);
        sb.append(ClassUtils.getShortClassName(getClass()));
        if (value != null)
        {
            sb.append(':').append(value);
        }
        return sb.toString();
    }
}
