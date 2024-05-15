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

package com.genologics.ri.researcher;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsEntityLink;
import com.genologics.ri.Link;
import com.genologics.ri.Linkable;

/**
 * Researcher-link is a child element type of researchers and provides a URI
 * linking to the detailed representation of a researcher.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "researcher-link", propOrder = { "firstName", "lastName" })
public class ResearcherLink implements LimsEntityLink<Researcher>, Serializable
{
    private static final long serialVersionUID = 5396333049575412661L;

    @XmlElement(name = "first-name")
    protected String firstName;

    @XmlElement(name = "last-name")
    protected String lastName;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public ResearcherLink()
    {
    }

    public ResearcherLink(URI uri)
    {
        this.uri = uri;
    }

    public ResearcherLink(Linkable<Researcher> link)
    {
        this.uri = link.getUri();
        /*
        try
        {
            this.firstName = (String)PropertyUtils.getProperty(link, "firstName");
            this.lastName = (String)PropertyUtils.getProperty(link, "lastName");
        }
        catch (Exception e)
        {
            // Ignore.
        }
        */
    }

    public Class<Researcher> getEntityClass()
    {
        return Researcher.class;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Convenience method for returning the full name of the researcher.
     *
     * @return The full name.
     *
     * @since 2.31.2
     *
     * @see Researcher#makeFullName(String, String)
     */
    public String getFullName()
    {
        return Researcher.makeFullName(firstName, lastName);
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
    public String getLimsid()
    {
        return Link.limsIdFromUri(uri);
    }

    @Override
    public void setLimsid(String id)
    {
        // Does nothing.
    }

    @Override
    public String toString()
    {
        return firstName + " " + lastName;
    }
}
