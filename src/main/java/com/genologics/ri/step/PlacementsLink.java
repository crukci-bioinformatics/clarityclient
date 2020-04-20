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

package com.genologics.ri.step;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;

/**
 *
 * Identifies the resource that represents the output placements for the step.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "placements-link")
public class PlacementsLink implements LimsLink<Placements>, Serializable
{
    private static final long serialVersionUID = 2017533453426861528L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public PlacementsLink()
    {
    }

    public PlacementsLink(URI uri)
    {
        this.uri = uri;
    }

    public PlacementsLink(Linkable<Placements> placements)
    {
        uri = placements.getUri();
    }

    @Override
    public Class<Placements> getEntityClass()
    {
        return Placements.class;
    }

    @Override
    public URI getUri()
    {
        return uri;
    }

    @Override
    public void setUri(URI uri)
    {
        this.uri = uri;
    }

}
