/*
 * CRUK-CI Clarity REST API Java Client.
 * Copyright (C) 2015 Cancer Research UK Cambridge Institute.
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.stage.Stage;

/**
 * Provides links to workflow stages, stage names and their respective statuses.
 *
 * @since 2.20
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "workflow-stage")
public class WorkflowStage implements LimsLink<Stage>, Serializable
{
    /**
     * Regular expression to extract workflow id and workflow stage id from a
     * workflow stage URI.
     *
     * @since 2.22
     */
    public static final Pattern ID_EXTRACTOR_PATTERN = Stage.ID_EXTRACTOR_PATTERN;

    private static final long serialVersionUID = 8029803456211993608L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "status")
    protected Status status;


    public WorkflowStage()
    {
    }

    public WorkflowStage(URI uri)
    {
        setUri(uri);
    }

    public WorkflowStage(URI uri, String name)
    {
        setUri(uri);
        setName(name);
    }

    public WorkflowStage(URI uri, String name, Status status)
    {
        setUri(uri);
        setName(name);
        setStatus(status);
    }

    /**
     * Get the numeric identifier for this workflow stage from its URI.
     *
     * @return The workflow stage id, or null if either the URI is not set
     * or it doesn't match the form expected for a workflow stage URI.
     *
     * @since 2.22
     */
    public Integer getId()
    {
        Integer id = null;
        if (uri != null)
        {
            Matcher m = ID_EXTRACTOR_PATTERN.matcher(uri.toString());
            if (m.matches())
            {
                id = Integer.valueOf(m.group(2));
            }
        }
        return id;
    }

    /**
     * Get the numeric identifier for this stage's workflow from its URI.
     *
     * @return The workflow id, or null if either the URI is not set
     * or it doesn't match the form expected for a workflow stage URI.
     *
     * @since 2.22
     */
    public Integer getWorkflowId()
    {
        Integer id = null;
        if (uri != null)
        {
            Matcher m = ID_EXTRACTOR_PATTERN.matcher(uri.toString());
            if (m.matches())
            {
                id = Integer.valueOf(m.group(1));
            }
        }
        return id;
    }

    public URI getUri()
    {
        return uri;
    }

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
    public Class<Stage> getEntityClass()
    {
        return Stage.class;
    }

    @Override
    public String toString()
    {
        return name + " " + status + " " + uri;
    }
}
