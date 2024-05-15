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
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;
import com.genologics.ri.artifact.Artifact;

/**
 *
 * Provides a URI linking to the output artifact and reagent label.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "output")
public class Output implements LimsLink<Artifact>, Serializable
{
    private static final long serialVersionUID = -5292196293632912589L;

    @XmlElement(name = "reagent-label")
    protected List<ReagentLabel> reagentLabels;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI artifactUri;


    public List<ReagentLabel> getReagentLabels()
    {
        if (reagentLabels == null)
        {
            reagentLabels = new ArrayList<ReagentLabel>();
        }
        return reagentLabels;
    }

    public URI getUri()
    {
        return artifactUri;
    }

    public void setUri(URI artifactUri)
    {
        this.artifactUri = artifactUri;
    }

    public void setArtifact(Linkable<Artifact> artifact)
    {
        artifactUri = artifact.getUri();
    }

    @Override
    public Class<Artifact> getEntityClass()
    {
        return Artifact.class;
    }

}
