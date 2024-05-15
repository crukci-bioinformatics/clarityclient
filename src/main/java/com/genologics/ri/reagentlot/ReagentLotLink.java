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

package com.genologics.ri.reagentlot;

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
 * Reagent-lot-link is a child element type of Reagent Lots and provides a URI
 * linking to the detailed representation of a Reagent Lot.
 *
 * @since 2.18
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reagent-lot-link", propOrder = { "name" })
public class ReagentLotLink implements LimsEntityLink<ReagentLot>, Serializable
{
    private static final long serialVersionUID = -5668438563329514777L;

    /**
     * The name of the Reagent Lot.
     * @since 2.31
     */
    @XmlElement
    protected String name;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public ReagentLotLink()
    {
    }

    public ReagentLotLink(URI uri)
    {
        this.uri = uri;
    }

    public ReagentLotLink(URI uri, String limsid)
    {
        this.uri = uri;
        this.limsid = limsid;
    }

    public ReagentLotLink(LimsEntityLinkable<ReagentLot> link)
    {
        uri = link.getUri();
        limsid = link.getLimsid();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String getLimsid()
    {
        return limsid;
    }

    public void setLimsid(String limsid)
    {
        this.limsid = limsid;
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

    @Override
    public Class<ReagentLot> getEntityClass()
    {
        return ReagentLot.class;
    }

    @Override
    public String toString()
    {
        return limsid;
    }
}
