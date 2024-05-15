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

package com.genologics.ri.project;

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

/**
 *
 * Project-link is a child element type of projects and provides a URI linking
 * to the detailed representation of a project.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "project-link")
public class ProjectLink implements LimsEntityLink<Project>, Serializable
{
    private static final long serialVersionUID = 5015998464762805841L;

    @XmlElement(name = "name")
    protected String name;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public ProjectLink()
    {
    }

    public ProjectLink(URI uri)
    {
        this.uri = uri;
    }

    public ProjectLink(URI uri, String limsid)
    {
        this.uri = uri;
        this.limsid = limsid;
    }

    public ProjectLink(URI uri, String limsid, String name)
    {
        this.uri = uri;
        this.limsid = limsid;
        this.name = name;
    }

    public ProjectLink(LimsEntityLinkable<Project> link)
    {
        uri = link.getUri();
        limsid = link.getLimsid();
        /*
        try
        {
            this.name = (String)PropertyUtils.getProperty(link, "name");
        }
        catch (Exception e)
        {
            // Ignore.
        }
        */
    }

    @Override
    public Class<Project> getEntityClass()
    {
        return Project.class;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    @Override
    public String toString()
    {
        return limsid;
    }
}
