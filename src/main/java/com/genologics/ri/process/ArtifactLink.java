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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsEntityLink;
import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.Linkable;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.artifact.OutputType;

/**
 *
 * Artifact is a child element of input-output-map and provides a link to an
 * Artifact that was either an input or output of the Process for the
 * input-output-map.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "artifact", propOrder = { "parentProcess" })
public class ArtifactLink implements LimsEntityLink<Artifact>, Serializable
{
    private static final long serialVersionUID = 5173462831311577367L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    @XmlAttribute(name = "output-type")
    protected OutputType outputType;

    @XmlAttribute(name = "output-generation-type")
    protected OutputGenerationType outputGenerationType;

    @XmlAttribute(name = "post-process-uri")
    @XmlSchemaType(name = "anyURI")
    protected URI postProcessUri;

    @XmlElement(name = "parent-process")
    protected ParentProcessLink parentProcess;


    public ArtifactLink()
    {
    }

    public ArtifactLink(URI uri)
    {
        this.uri = uri;
    }

    public ArtifactLink(URI uri, String limsid)
    {
        this.uri = uri;
        this.limsid = limsid;
    }

    public ArtifactLink(Linkable<Artifact> link)
    {
        this.uri = link.getUri();
    }

    public ArtifactLink(LimsEntityLinkable<Artifact> link)
    {
        this.uri = link.getUri();
        this.limsid = link.getLimsid();
    }

    @Override
    public Class<Artifact> getEntityClass()
    {
        return Artifact.class;
    }

    public ParentProcessLink getParentProcess()
    {
        return parentProcess;
    }

    public ParentProcessLink setParentProcess(LimsEntityLinkable<ClarityProcess> parentProcess)
    {
        this.parentProcess = new ParentProcessLink(parentProcess);
        return this.parentProcess;
    }

    public String getLimsid()
    {
        return limsid;
    }

    public void setLimsid(String limsid)
    {
        this.limsid = limsid;
    }

    public OutputType getOutputType()
    {
        return outputType;
    }

    public void setOutputType(OutputType outputType)
    {
        this.outputType = outputType;
    }

    public OutputGenerationType getOutputGenerationType()
    {
        return outputGenerationType;
    }

    public void setOutputGenerationType(OutputGenerationType outputGenerationType)
    {
        this.outputGenerationType = outputGenerationType;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public URI getPostProcessUri()
    {
        return postProcessUri;
    }

    public void setPostProcessUri(URI postProcessUri)
    {
        this.postProcessUri = postProcessUri;
    }

    @Override
    public String toString()
    {
        return limsid;
    }
}
