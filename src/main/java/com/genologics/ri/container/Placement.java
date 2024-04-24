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

package com.genologics.ri.container;

import static com.genologics.ri.Location.WELL_POSITION_SPLITTER;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import com.genologics.ri.LimsEntityLink;
import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.artifact.Artifact;

/**
 *
 * Placement is a child element of container and provides a URI linking to the
 * detailed representation of the artifact that is located in a specific well of
 * the container.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "placement", propOrder = { "wellPosition" })
public class Placement implements LimsEntityLink<Artifact>, Serializable, Comparable<Placement>
{
    private static final long serialVersionUID = -4089394834813900531L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    @XmlElement(name = "value")
    protected String wellPosition;

    public Placement()
    {
    }

    public Placement(URI uri)
    {
        this.uri = uri;
    }

    public Placement(URI uri, String wellPosition)
    {
        this.uri = uri;
        this.wellPosition = wellPosition;
    }

    public Placement(URI uri, String limsid, String wellPosition)
    {
        this.uri = uri;
        this.limsid = limsid;
        this.wellPosition = wellPosition;
    }

    public Placement(LimsEntityLinkable<Artifact> link)
    {
        this.uri = link.getUri();
        this.limsid = link.getLimsid();

        try
        {
            wellPosition = (String)PropertyUtils.getProperty(link, "wellPosition");
        }
        catch (Exception e)
        {
            // Ignore.
        }
    }

    public Placement(LimsEntityLinkable<Artifact> link, String wellPosition)
    {
        this.uri = link.getUri();
        this.limsid = link.getLimsid();
        this.wellPosition = wellPosition;
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

    public String getLimsid()
    {
        return limsid;
    }

    public void setLimsid(String limsid)
    {
        this.limsid = limsid;
    }

    public String getWellPosition()
    {
        return wellPosition;
    }

    public void setWellPosition(String wellPosition)
    {
        this.wellPosition = wellPosition;
    }

    @Override
    public int compareTo(Placement o)
    {
        if (o == null)
        {
            return 1;
        }

        if (StringUtils.isBlank(wellPosition) || StringUtils.isBlank(o.wellPosition))
        {
            return 0;
        }

        String[] myparts = WELL_POSITION_SPLITTER.split(wellPosition);
        String[] itsparts = WELL_POSITION_SPLITTER.split(o.wellPosition);

        int result = 0;
        for (int p = 0; result == 0 && p < Math.min(myparts.length, itsparts.length); p++)
        {
            try
            {
                int p1v = Integer.parseInt(myparts[p]);
                int p2v = Integer.parseInt(itsparts[p]);
                result = p1v - p2v;
            }
            catch (NumberFormatException e)
            {
                result = myparts[p].compareTo(itsparts[p]);
            }
        }
        return result;
    }

    @Override
    public String toString()
    {
        return limsid + " " + wellPosition;
    }
}
