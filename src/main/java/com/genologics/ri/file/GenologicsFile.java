/*
 * CRUK-CI Genologics REST API Java Client.
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

package com.genologics.ri.file;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.GenologicsEntity;
import com.genologics.ri.LimsEntity;
import com.genologics.ri.LimsEntityLink;
import com.genologics.ri.Linkable;

/**
 * The file element contains information about a file in the system.
 *
 * These are rather strange as the link to a file is just a file object with
 * only the URI set. So they are both entities and links.
 */
@GenologicsEntity(uriSection = "files", creatable = true, updateable = true, removable = true)
@XmlRootElement(name = "file")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "file", propOrder = { "attachedTo", "contentLocation", "originalLocation", "published" })
public class GenologicsFile implements LimsEntity<GenologicsFile>, LimsEntityLink<GenologicsFile>, Serializable
{
    private static final long serialVersionUID = 9170097758006050623L;

    @XmlElement(name = "attached-to")
    @XmlSchemaType(name = "anyURI")
    protected URI attachedTo;

    @XmlElement(name = "content-location")
    @XmlSchemaType(name = "anyURI")
    protected URI contentLocation;

    @XmlElement(name = "original-location")
    protected String originalLocation;

    @XmlElement(name = "is-published")
    protected Boolean published;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public GenologicsFile()
    {
    }

    public GenologicsFile(URI uri)
    {
        this.uri = uri;
    }

    public GenologicsFile(URI uri, String limsid)
    {
        this.uri = uri;
        this.limsid = limsid;
    }

    public GenologicsFile(GenologicsFile original)
    {
        this.uri = original.getUri();
        this.limsid = original.getLimsid();
    }

    public URI getAttachedTo()
    {
        return attachedTo;
    }

    public void setAttachedTo(Linkable<?> link)
    {
        this.attachedTo = link == null ? null : link.getUri();
    }

    public URI getContentLocation()
    {
        return contentLocation;
    }

    public void setContentLocation(URI contentLocation)
    {
        this.contentLocation = contentLocation;
    }

    public String getOriginalLocation()
    {
        return originalLocation;
    }

    public void setOriginalLocation(String originalLocation)
    {
        this.originalLocation = originalLocation;
    }

    public boolean isPublished()
    {
        return published == null ? false : published.booleanValue();
    }

    public void setPublished(Boolean published)
    {
        this.published = published;
    }

    public String getLimsid()
    {
        return limsid;
    }

    public void setLimsid(String limsid)
    {
        this.limsid = limsid;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    /**
     * Helper method to clear the detail fields (attachedTo, contentLocation, originalLocation
     * and published). This may be necessary if a new file is created with these values set and
     * that object then set on, say, an artifact that needs to be updated. Internally, the link
     * will have been set on the server.
     */
    public void clearDetailFields()
    {
        attachedTo = null;
        contentLocation = null;
        originalLocation = null;
        published = null;
    }

    @Override
    public Class<GenologicsFile> getEntityClass()
    {
        return GenologicsFile.class;
    }

    @Override
    public String toString()
    {
        return limsid;
    }
}
