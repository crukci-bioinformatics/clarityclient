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

import static com.genologics.ri.Namespaces.QUEUE_NAMESPACE;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.cruk.clarity.api.jaxb.LongTimestampAdapter;

import com.genologics.ri.LimsEntityLink;
import com.genologics.ri.Location;
import com.genologics.ri.artifact.Artifact;


/**
 * @since 2.19
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = QUEUE_NAMESPACE, name = "artifact-link", propOrder = { "queueTime", "location" })
public class ArtifactLink implements LimsEntityLink<Artifact>, Serializable
{
    private static final long serialVersionUID = -531351200403814712L;

    @XmlElement(name = "queue-time")
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(LongTimestampAdapter.class)
    protected Date queueTime;

    protected Location location;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "limsid")
    protected String limsid;

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

    public Date getQueueTime()
    {
        return queueTime;
    }

    public void setQueueTime(Date queueTime)
    {
        this.queueTime = queueTime;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
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

    @Override
    public String getLimsid()
    {
        return limsid;
    }

    public void setLimsid(String limsid)
    {
        this.limsid = limsid;
    }

    @Override
    public Class<Artifact> getEntityClass()
    {
        return Artifact.class;
    }

    @Override
    public String toString()
    {
        return limsid;
    }
}
