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

package com.genologics.ri.file;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.LimsEntity;
import com.genologics.ri.LimsEntityLink;
import com.genologics.ri.Linkable;

/**
 * The file element contains information about a file in the system.
 *
 * These are rather strange as the link to a file is just a file object with
 * only the URI set. So they are both entities and links.
 */
@ClarityEntity(uriSection = "files", creatable = true, updateable = true, removable = true)
@XmlRootElement(name = "file")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "file", propOrder = { "attachedTo", "contentLocation", "originalLocation", "originalName", "published" })
public class ClarityFile implements LimsEntity<ClarityFile>, LimsEntityLink<ClarityFile>, Serializable
{
    private static final long serialVersionUID = -6683975101172687186L;

    @XmlElement(name = "attached-to")
    @XmlSchemaType(name = "anyURI")
    protected URI attachedTo;

    @XmlElement(name = "content-location")
    @XmlSchemaType(name = "anyURI")
    protected URI contentLocation;

    @XmlElement(name = "original-location")
    protected String originalLocation;

    /**
     * @since 2.26
     */
    @XmlElement(name = "original-name")
    protected String originalName;

    @XmlElement(name = "is-published")
    protected Boolean published;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public ClarityFile()
    {
    }

    public ClarityFile(URI uri)
    {
        this.uri = uri;
    }

    public ClarityFile(URI uri, String limsid)
    {
        this.uri = uri;
        this.limsid = limsid;
    }

    public ClarityFile(ClarityFile original)
    {
        this.uri = original.getUri();
        this.limsid = original.getLimsid();
    }

    /**
     * This element contains a URI that identifies and links to further information about the resource that
     * the file is attached to, such as a project, sample, process, or file-based artifact.
     *
     * @return The URI of the entity the file is attached to.
     */
    public URI getAttachedTo()
    {
        return attachedTo;
    }

    public void setAttachedTo(Linkable<?> link)
    {
        this.attachedTo = link == null ? null : link.getUri();
    }

    /**
     * This element contains a URI that identifies and links to the network location of the file,
     * which can be used to retrieve the file and process its contents.
     *
     * @return The file's location.
     */
    public URI getContentLocation()
    {
        return contentLocation;
    }

    public void setContentLocation(URI contentLocation)
    {
        this.contentLocation = contentLocation;
    }

    /**
     * This element provides the original name and location of the file before it was imported into the system.
     * Note: If the file was uploaded from the Clarity web interface, the original-location element will not contain
     * the full file path due to browser security limitations. Only the original file name will be available.
     *
     * @return The original file location.
     */
    public String getOriginalLocation()
    {
        return originalLocation;
    }

    public void setOriginalLocation(String originalLocation)
    {
        this.originalLocation = originalLocation;
    }

    /**
     * This element provides the original name of the file before it was imported into the system.
     * This is calculated from the original-location.
     *
     * @return The original file name.
     *
     * @since 2.26
     */
    public String getOriginalName()
    {
        return originalName;
    }

    public void setOriginalName(String originalName)
    {
        this.originalName = originalName;
    }

    /**
     * This element specifies whether the file is displayed in LabLink.
     *
     * @return true to be visible in Lablink, false to be invisible.
     */
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
    public Class<ClarityFile> getEntityClass()
    {
        return ClarityFile.class;
    }

    @Override
    public String toString()
    {
        return limsid;
    }
}
