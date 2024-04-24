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

package com.genologics.ri.stage;

import java.io.Serializable;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.Linkable;
import com.genologics.ri.protocolconfiguration.Protocol;
import com.genologics.ri.stepconfiguration.ProtocolStep;
import com.genologics.ri.workflowconfiguration.Workflow;

/**
 *
 * The detailed representation of a stage.
 */
@ClarityEntity(uriSection = "stages", primaryEntity = Workflow.class)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stage", propOrder = { "workflow", "protocol", "step" })
@XmlRootElement(name = "stage")
public class Stage implements Linkable<Stage>, Serializable
{
    /**
     * Regular expression to extract workflow id and workflow stage id from a
     * workflow stage URI.
     *
     * @since 2.22
     */
    public static final Pattern ID_EXTRACTOR_PATTERN;

    private static final long serialVersionUID = -7190421220331989537L;

    @XmlElement(name = "workflow")
    protected WorkflowLink workflow;

    @XmlElement(name = "protocol")
    protected ProtocolLink protocol;

    @XmlElement(name = "step")
    protected ProtocolStepLink step;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "index")
    protected Integer index;


    static
    {
        ClarityEntity innerAnno = Stage.class.getAnnotation(ClarityEntity.class);
        ClarityEntity outerAnno = innerAnno.primaryEntity().getAnnotation(ClarityEntity.class);

        StringBuilder b = new StringBuilder();
        b.append("^.*/").append(outerAnno.uriSection()).append("/(\\d+)/");
        b.append(innerAnno.uriSection()).append("/(\\d+)$");

        ID_EXTRACTOR_PATTERN = Pattern.compile(b.toString());
    }

    public Stage()
    {
    }

    public Stage(URI uri)
    {
        this.uri = uri;
    }

    public Stage(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
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

    public WorkflowLink getWorkflow()
    {
        return workflow;
    }

    public void setWorkflow(Linkable<Workflow> workflow)
    {
        this.workflow = new WorkflowLink(workflow);
    }

    public ProtocolLink getProtocol()
    {
        return protocol;
    }

    public void setProtocol(Linkable<Protocol> protocol)
    {
        this.protocol = new ProtocolLink(protocol);
    }

    public ProtocolStepLink getStep()
    {
        return step;
    }

    public void setStep(Linkable<ProtocolStep> step)
    {
        this.step = new ProtocolStepLink(step);
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

    public Integer getIndex()
    {
        return index;
    }

    public void setIndex(Integer index)
    {
        this.index = index;
    }

}
