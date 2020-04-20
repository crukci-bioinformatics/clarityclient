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

package com.genologics.ri.processexecution;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;
import com.genologics.ri.researcher.Researcher;

/**
 *
 * The technician element provides a URI to the user that is responsible for the
 * Process. Once the Process completes, this user is listed as the technician
 * that ran the Process.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "technician")
public class Technician implements LimsLink<Researcher>, Serializable
{
    private static final long serialVersionUID = 6342603327961394878L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public Technician()
    {
    }

    public Technician(URI uri)
    {
        this.uri = uri;
    }

    public Technician(Linkable<Researcher> link)
    {
        this.uri = link.getUri();
    }

    @Override
    public Class<Researcher> getEntityClass()
    {
        return Researcher.class;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI value)
    {
        this.uri = value;
    }

}
