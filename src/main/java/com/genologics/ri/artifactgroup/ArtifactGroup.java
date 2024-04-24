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

package com.genologics.ri.artifactgroup;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.Locatable;

/**
 *
 * ArtifactGroups are created by the Lab Scientist interface for each workflow.
 * The Operations Interface assigns samples to workflows by placing them into an
 * artifactGroup. Seeing a sample in an artifactGroup in the API indicates that
 * the sample has been assigned to a workflow of the same name.
 */
@ClarityEntity(uriSection = "artifactgroups")
@XmlRootElement(name = "artifactgroup")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "artifactgroup", propOrder = { "name", "artifacts" })
public class ArtifactGroup implements Locatable, Serializable
{
    private static final long serialVersionUID = 2205878179459911556L;

    protected String name;

    protected Artifacts artifacts;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        this.name = value;
    }

    public Artifacts getArtifacts()
    {
        return artifacts;
    }

    public void setArtifacts(Artifacts value)
    {
        this.artifacts = value;
    }

    @Override
    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI value)
    {
        this.uri = value;
    }

}
