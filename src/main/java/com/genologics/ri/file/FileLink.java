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
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.LimsEntityLinkable;

/**
 * The file-link type provides a URI that links to information about a file in
 * the system.
 * <p>
 * Elements of this type are used for lists of files, or by resources that have
 * attached files to identify and link to further information about the file.
 * </p>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "file-link")
public class FileLink implements LimsLink<ClarityFile>, Serializable
{
    private static final long serialVersionUID = -2151598768590902010L;

    @XmlAttribute(name = "uri")
    protected URI uri;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    public FileLink()
    {
    }

    public FileLink(URI uri)
    {
        this.uri = uri;
    }

    public FileLink(URI uri, String limsid)
    {
        this.uri = uri;
        this.limsid = limsid;
    }

    public FileLink(LimsEntityLinkable<ClarityFile> link)
    {
        this.uri = link.getUri();
        this.limsid = link.getLimsid();
    }

    @Override
    public Class<ClarityFile> getEntityClass()
    {
        return ClarityFile.class;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public String getLimsid()
    {
        return limsid;
    }

    public void setLimsid(String limsid)
    {
        this.limsid = limsid;
    }

    @Override
    public String toString()
    {
        return limsid;
    }
}
