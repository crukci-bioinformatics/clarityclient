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

package com.genologics.ri.step;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;
import com.genologics.ri.artifact.Artifact;

/**
 *
 * Provides input URI links.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "input")
public class Input implements LimsLink<Artifact>, Serializable
{
    private static final long serialVersionUID = -2525215280378480778L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    /**
     * @since 2.18
     */
    @XmlAttribute
    protected Long replicates;

    public Input()
    {
    }

    public Input(URI uri)
    {
        this.uri = uri;
    }

    public Input(Linkable<Artifact> link)
    {
        uri = link.getUri();
    }

    @Override
    public Class<Artifact> getEntityClass()
    {
        return Artifact.class;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public Long getReplicates()
    {
        return replicates;
    }

    public void setReplicates(Long replicates)
    {
        this.replicates = replicates;
    }

}
