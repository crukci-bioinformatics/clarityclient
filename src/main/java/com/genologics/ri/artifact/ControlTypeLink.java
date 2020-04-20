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

package com.genologics.ri.artifact;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;
import com.genologics.ri.controltype.ControlType;

/**
 *
 * Provide a URI linking to the control type if this artifact is a control
 * sample.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "control-type")
public class ControlTypeLink implements LimsLink<ControlType>, Serializable
{
    private static final long serialVersionUID = -4203696627970925073L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public ControlTypeLink()
    {
    }

    public ControlTypeLink(URI uri)
    {
        this.uri = uri;
    }

    public ControlTypeLink(Linkable<ControlType> link)
    {
        this.uri = link.getUri();
    }

    @Override
    public URI getUri()
    {
        return uri;
    }

    @Override
    public void setUri(URI value)
    {
        this.uri = value;
    }

    @Override
    public Class<ControlType> getEntityClass()
    {
        return ControlType.class;
    }

}
