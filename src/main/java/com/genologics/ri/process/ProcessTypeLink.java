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

package com.genologics.ri.process;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;
import com.genologics.ri.processtype.ProcessType;

/**
 *
 * Process-type is a child element of process that identifies and provides a URI
 * linking to the detailed representation of the process type that the process
 * is associated with.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "process-type")
public class ProcessTypeLink implements LimsLink<ProcessType>, Serializable
{
    private static final long serialVersionUID = 1852325206868769788L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlValue
    protected String name;

    public ProcessTypeLink()
    {
    }

    public ProcessTypeLink(URI uri)
    {
        this.uri = uri;
    }

    public ProcessTypeLink(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
    }

    public ProcessTypeLink(Linkable<ProcessType> link)
    {
        this.uri = link.getUri();
        /*
        try
        {
            this.name = (String)PropertyUtils.getProperty(link, "name");
        }
        catch (Exception e)
        {
            // Ignore.
        }
        */
    }

    @Override
    public Class<ProcessType> getEntityClass()
    {
        return ProcessType.class;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
