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
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsEntityLink;
import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.artifact.Artifact;

/**
 *
 * The file element describes a shared result file output that will be displayed
 * in the step-setup view.
 *
 * @since 2.18
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "file")
public class SharedResultFile implements LimsEntityLink<Artifact>, Serializable
{
    private static final long serialVersionUID = -1481534271853089634L;

    @XmlElement
    protected String message;

    @XmlAttribute(name = "shared-result-file-index")
    protected String sharedResultFileIndex;

    @XmlAttribute(name = "artifact-uri")
    @XmlSchemaType(name = "anyURI")
    protected URI artifactUri;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    @XmlAttribute(name = "name")
    protected String name;

    public SharedResultFile()
    {
    }

    public SharedResultFile(URI artifactUri)
    {
        this.artifactUri = artifactUri;
    }

    public SharedResultFile(URI artifactUri, String limsid)
    {
        this.artifactUri = artifactUri;
        this.limsid = limsid;
    }

    public SharedResultFile(LimsEntityLinkable<Artifact> link)
    {
        this.artifactUri = link.getUri();
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getSharedResultFileIndex()
    {
        return sharedResultFileIndex;
    }

    public void setSharedResultFileIndex(String sharedResultFileIndex)
    {
        this.sharedResultFileIndex = sharedResultFileIndex;
    }

    public URI getUri()
    {
        return artifactUri;
    }

    public void setUri(URI artifactUri)
    {
        this.artifactUri = artifactUri;
    }

    public String getLimsid()
    {
        return limsid;
    }

    public void setLimsid(String limsid)
    {
        this.limsid = limsid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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
