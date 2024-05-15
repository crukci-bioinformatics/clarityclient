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
 * List of step transitions including the URI allowing access to the specific
 * next step
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "next-step")
public class NextStep implements LimsLink<ProtocolStep>, Serializable
{
    private static final long serialVersionUID = -955680094983735241L;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "sequence")
    protected Integer sequence;

    @XmlAttribute(name = "next-step-uri")
    @XmlSchemaType(name = "anyURI")
    protected URI nextStepUri;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getSequence()
    {
        return sequence;
    }

    public void setSequence(Integer sequence)
    {
        this.sequence = sequence;
    }

    public URI getNextStepUri()
    {
        return nextStepUri;
    }

    public URI getUri()
    {
        return nextStepUri;
    }

    public void setNextStepUri(URI nextStepUri)
    {
        this.nextStepUri = nextStepUri;
    }

    public void setNextStep(Linkable<ProtocolStep> step)
    {
        this.nextStepUri = step.getUri();
    }

    public void setUri(URI nextStepUri)
    {
        this.nextStepUri = nextStepUri;
    }

    @Override
    public Class<ProtocolStep> getEntityClass()
    {
        return ProtocolStep.class;
    }
}
