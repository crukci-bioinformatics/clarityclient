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

package com.genologics.ri.queue;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.genologics.ri.Page;
import com.genologics.ri.PaginatedBatch;

/**
 *
 * The representation of a queue.
 * <p>
 * It represents and is used to list all of the elements contained within a
 * queue. A queue is a representation of samples that are ready to run through a
 * Protocol Step.
 * </p>
 * <p>
 * Each artifact listed in the queue will contain elements to show when it was
 * queued and its container location.
 * </p>
 *
 * @since 2.19
 */
@ClarityEntity(uriSection = "queues", cacheable = false)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queue", propOrder = { "artifacts", "previousPage", "nextPage" })
@XmlRootElement(name = "queue")
public class Queue implements PaginatedBatch<ArtifactLink>, Linkable<Queue>, Serializable
{
    private static final long serialVersionUID = 4372664462524847744L;

    @XmlElementWrapper(name = "artifacts")
    @XmlElement(name = "artifact")
    protected List<ArtifactLink> artifacts;

    @XmlElement(name = "previous-page")
    protected Page previousPage;

    @XmlElement(name = "next-page")
    protected Page nextPage;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "protocol-step-uri")
    @XmlSchemaType(name = "anyURI")
    protected URI protocolStepUri;

    @XmlAttribute(name = "name")
    protected String name;

    public Queue()
    {
    }

    public Queue(URI uri)
    {
        this.uri = uri;
    }

    public Queue(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
    }

    @Override
    public Page getPreviousPage()
    {
        return previousPage;
    }

    @Override
    public void setPreviousPage(Page previousPage)
    {
        this.previousPage = previousPage;
    }

    @Override
    public Page getNextPage()
    {
        return nextPage;
    }

    @Override
    public void setNextPage(Page nextPage)
    {
        this.nextPage = nextPage;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public URI getProtocolStepUri()
    {
        return protocolStepUri;
    }

    public void setProtocolStepUri(URI protocolStepUri)
    {
        this.protocolStepUri = protocolStepUri;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<ArtifactLink> getArtifacts()
    {
        if (artifacts == null)
        {
            artifacts = new ArrayList<ArtifactLink>();
        }
        return artifacts;
    }

    @Override
    public List<ArtifactLink> getList()
    {
        return getArtifacts();
    }

    @Override
    public int getSize()
    {
        return artifacts == null ? 0 : artifacts.size();
    }

    @Override
    public Iterator<ArtifactLink> iterator()
    {
        return getArtifacts().iterator();
    }

}
