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

package com.genologics.ri;

import java.io.Serializable;
import java.net.URI;
import java.util.regex.Pattern;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.genologics.ri.container.Container;

/**
 * <p>
 * Location provides a URI linking to the detailed representation of a Container
 * along with the well location within that Container.
 * </p>
 * <p>
 * Location is used by representations to describe their location within a
 * Container. For example Artifact and reagent use location to describe which
 * Container they are located in.
 * </p>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "location", propOrder = { "container", "wellPosition" })
public class Location implements LimsEntityLink<Container>, Serializable, Comparable<Location>
{
    public static final Pattern WELL_POSITION_SPLITTER = Pattern.compile(":");

    private static final long serialVersionUID = -2461753783129402323L;

    @XmlElement(name = "container")
    private ContainerLink container;

    @XmlElement(name = "value")
    private String wellPosition;

    private transient Integer hc;

    public Location()
    {
    }

    public Location(LimsEntityLinkable<Container> container, String position)
    {
        this.container = new ContainerLink(container);
        this.wellPosition = position;
    }

    public Location(Linkable<Container> container, String position)
    {
        this.container = new ContainerLink(container);
        this.wellPosition = position;
    }

    public ContainerLink getContainer()
    {
        return container;
    }

    public ContainerLink setContainer(LimsEntityLinkable<Container> link)
    {
        container = new ContainerLink(link);
        hc = null;
        return container;
    }

    public String getWellPosition()
    {
        return wellPosition;
    }

    public void setWellPosition(String wellPosition)
    {
        this.wellPosition = wellPosition;
        hc = null;
    }

    @Override
    public int hashCode()
    {
        if (hc == null)
        {
            HashCodeBuilder b = new HashCodeBuilder();
            b.append(container);
            b.append(wellPosition);
            hc = b.toHashCode();
        }
        return hc.intValue();
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean equal = obj == this;
        if (!equal && obj != null)
        {
            if (getClass().equals(obj.getClass()))
            {
                Location other = (Location)obj;

                EqualsBuilder b = new EqualsBuilder();
                b.append(container, other.container);
                b.append(wellPosition, other.wellPosition);
                equal = b.isEquals();
            }
        }
        return equal;
    }

    @Override
    public int compareTo(Location o)
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
        String[] itsparts = WELL_POSITION_SPLITTER.split(wellPosition);

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
        return container + " " + wellPosition;
    }

    // Convenience methods to implement LimsEntityLink.
    // Just pass through to the container link.

    @Override
    public String getLimsid()
    {
        return container == null ? null : container.getLimsid();
    }

    @Override
    public void setLimsid(String id)
    {
        if (container != null)
        {
            container.setLimsid(id);
        }
    }

    @Override
    public URI getUri()
    {
        return container == null ? null : container.getUri();
    }

    @Override
    public void setUri(URI uri)
    {
        if (container != null)
        {
            container.setUri(uri);
        }
    }

    @Override
    public Class<Container> getEntityClass()
    {
        return Container.class;
    }
}
