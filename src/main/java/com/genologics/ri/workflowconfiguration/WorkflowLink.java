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

package com.genologics.ri.workflowconfiguration;

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
 * Workflow-link is a child element type of workflows and provides a URI linking
 * to the detailed representation of a workflow.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "workflow-link")
public class WorkflowLink implements LimsLink<Workflow>, Serializable
{
    private static final long serialVersionUID = 1335890809720204629L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "status")
    protected Status status;

    public WorkflowLink()
    {
    }

    public WorkflowLink(URI uri)
    {
        this.uri = uri;
    }

    public WorkflowLink(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
    }

    public WorkflowLink(Linkable<Workflow> link)
    {
        uri = link.getUri();
    }

    @Override
    public Class<Workflow> getEntityClass()
    {
        return Workflow.class;
    }

    @Override
    public URI getUri()
    {
        return uri;
    }

    @Override
    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
