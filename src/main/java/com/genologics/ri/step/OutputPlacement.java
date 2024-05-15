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

import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;
import com.genologics.ri.Location;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.container.Container;

/**
 *
 * Provides a URI linking to the output artifact and container placement.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "output-placement")
public class OutputPlacement implements LimsLink<Artifact>, Serializable
{
    private static final long serialVersionUID = 5187719712136408829L;

    protected Location location;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public OutputPlacement()
    {
    }

    public OutputPlacement(Linkable<Artifact> artifact)
    {
        setArtifact(artifact);
    }

    public OutputPlacement(Linkable<Artifact> artifact, LimsEntityLinkable<Container> container, String wellPosition)
    {
        setArtifact(artifact);
        setLocation(container, wellPosition);
    }

    public OutputPlacement(Linkable<Artifact> artifact, Linkable<Container> container, String wellPosition)
    {
        setArtifact(artifact);
        setLocation(container, wellPosition);
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public void setLocation(LimsEntityLinkable<Container> container, String wellPosition)
    {
        this.location = new Location(container, wellPosition);
    }

    public void setLocation(Linkable<Container> container, String wellPosition)
    {
        this.location = new Location(container, wellPosition);
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI artifactUri)
    {
        this.uri = artifactUri;
    }

    public void setArtifact(Linkable<Artifact> artifact)
    {
        uri = artifact.getUri();
    }

    @Override
    public Class<Artifact> getEntityClass()
    {
        return Artifact.class;
    }

    @Override
    public String toString()
    {
        return location == null ? null : location.toString();
    }
}
