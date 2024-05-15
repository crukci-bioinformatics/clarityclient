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

package com.genologics.ri.sample;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;
import com.genologics.ri.researcher.Researcher;

/**
 * Submitter is a child element of Sample and provides a URI linking to the
 * detailed representation of the submitter for the Sample.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submitter", propOrder = { "firstName", "lastName" })
public class Submitter implements LimsLink<Researcher>, Serializable
{
    private static final long serialVersionUID = 1302200229944706575L;

    @XmlElement(name = "first-name")
    protected String firstName;

    @XmlElement(name = "last-name")
    protected String lastName;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public Submitter()
    {
    }

    public Submitter(URI uri)
    {
        this.uri = uri;
    }

    public Submitter(Linkable<Researcher> link)
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

    @Override
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
        return firstName + " " + lastName;
    }
}
