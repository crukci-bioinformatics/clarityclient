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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.Linkable;
import com.genologics.ri.protocolconfiguration.Protocol;
import com.genologics.ri.stage.Stage;

/**
 *
 * The detailed representation of a Workflow.
 */
@ClarityEntity(uriSection = "configuration/workflows")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "workflow", propOrder = { "protocols", "stages" })
@XmlRootElement(name = "workflow")
public class Workflow implements Linkable<Workflow>, Serializable
{
    /**
     * Regular expression to extract workflow id from a workflow URI.
     *
     * @since 2.22
     */
    public static final Pattern ID_EXTRACTOR_PATTERN;

    private static final long serialVersionUID = -8884000036888230246L;

    @XmlElementWrapper(name = "protocols")
    @XmlElement(name = "protocol")
    protected List<ProtocolLink> protocols;

    @XmlElementWrapper(name = "stages")
    @XmlElement(name = "stage")
    protected List<StageLink> stages;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "status")
    protected Status status;


    static
    {
        ClarityEntity anno = Workflow.class.getAnnotation(ClarityEntity.class);

        StringBuilder b = new StringBuilder();
        b.append("^.*/").append(anno.uriSection()).append("/(\\d+)$");

        ID_EXTRACTOR_PATTERN = Pattern.compile(b.toString());
    }

    public Workflow()
    {
    }

    public Workflow(URI uri)
    {
        this.uri = uri;
    }

    public Workflow(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
    }

    /**
     * Get the numeric identifier for this workflow from its URI.
     *
     * @return The workflow id, or null if either the URI is not set
     * or it doesn't match the form expected for a workflow URI.
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
                id = Integer.valueOf(m.group(1));
            }
        }
        return id;
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

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public List<ProtocolLink> getProtocols()
    {
        if (protocols == null)
        {
            protocols = new ArrayList<ProtocolLink>();
        }
        return protocols;
    }

    public ProtocolLink addProtocol(Linkable<Protocol> protocol)
    {
        ProtocolLink link = new ProtocolLink(protocol);
        getProtocols().add(link);
        return link;
    }

    public List<StageLink> getStages()
    {
        if (stages == null)
        {
            stages = new ArrayList<StageLink>();
        }
        return stages;
    }

    public StageLink addStage(Linkable<Stage> stage)
    {
        StageLink link = new StageLink(stage);
        getStages().add(link);
        return link;
    }
}
