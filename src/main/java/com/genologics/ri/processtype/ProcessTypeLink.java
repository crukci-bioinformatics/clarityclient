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

package com.genologics.ri.processtype;

import static com.genologics.ri.Namespaces.PROCESS_TYPE_NAMESPACE;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;

/**
 *
 * Process-type-link is a child element type of process types and provides a URI
 * linking to the detailed representation of a process type.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = PROCESS_TYPE_NAMESPACE, name = "process-type-link")
public class ProcessTypeLink implements LimsLink<ProcessType>, Serializable
{
    private static final long serialVersionUID = 5914216290949476076L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "name")
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
