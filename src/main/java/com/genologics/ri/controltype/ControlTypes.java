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

package com.genologics.ri.controltype;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.Batch;
import com.genologics.ri.ClarityQueryResult;

/**
 *
 * The representation of a list of ControlTypes
 */
@ClarityQueryResult(entityClass = ControlType.class)
@XmlRootElement(name = "control-types")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "control-types")
public class ControlTypes implements Batch<ControlTypeLink>, Serializable
{
    private static final long serialVersionUID = 5668622843069816173L;

    @XmlElement(name = "control-type")
    protected List<ControlTypeLink> controlTypes;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public List<ControlTypeLink> getControlTypes()
    {
        if (controlTypes == null)
        {
            controlTypes = new ArrayList<ControlTypeLink>();
        }
        return controlTypes;
    }

    @Override
    public Iterator<ControlTypeLink> iterator()
    {
        return getControlTypes().iterator();
    }

    @Override
    public List<ControlTypeLink> getList()
    {
        return getControlTypes();
    }

    @Override
    public int getSize()
    {
        return controlTypes == null ? 0 : controlTypes.size();
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

}
