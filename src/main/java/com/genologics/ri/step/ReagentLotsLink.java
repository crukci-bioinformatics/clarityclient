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

package com.genologics.ri.step;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;

/**
 *
 * Identifies the resource that represents the reagent lots for the step.
 *
 * @since 2.18
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reagent-lots-link")
public class ReagentLotsLink implements LimsLink<ReagentLots>, Serializable
{
    private static final long serialVersionUID = -2260570956329971986L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public ReagentLotsLink()
    {
    }

    public ReagentLotsLink(URI uri)
    {
        this.uri = uri;
    }

    public ReagentLotsLink(Linkable<ReagentLots> link)
    {
        this.uri = link.getUri();
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
    public Class<ReagentLots> getEntityClass()
    {
        return ReagentLots.class;
    }

}
