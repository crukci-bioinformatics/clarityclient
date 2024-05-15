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

package com.genologics.ri.reagentkit;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityQueryResult;
import com.genologics.ri.Page;
import com.genologics.ri.PaginatedBatch;

/**
 *
 * The representation for a list of reagent type links.
 * @since 2.18
 */
@ClarityQueryResult(entityClass = ReagentKit.class)
@XmlRootElement(name = "reagent-kits")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reagent-kits", propOrder = { "reagentKits", "nextPage", "previousPage" })
public class ReagentKits implements PaginatedBatch<ReagentKitLink>, Serializable
{
    private static final long serialVersionUID = 719305311109268363L;

    @XmlElement(name = "reagent-kit")
    protected List<ReagentKitLink> reagentKits;

    @XmlElement(name = "next-page")
    protected Page nextPage;

    @XmlElement(name = "previous-page")
    protected Page previousPage;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public List<ReagentKitLink> getReagentKits()
    {
        if (reagentKits == null)
        {
            reagentKits = new ArrayList<ReagentKitLink>();
        }
        return reagentKits;
    }

    @Override
    public List<ReagentKitLink> getList()
    {
        return getReagentKits();
    }

    @Override
    public int getSize()
    {
        return reagentKits == null ? 0 : reagentKits.size();
    }

    @Override
    public Iterator<ReagentKitLink> iterator()
    {
        return getReagentKits().iterator();
    }

    public Page getNextPage()
    {
        return nextPage;
    }

    public void setNextPage(Page nextPage)
    {
        this.nextPage = nextPage;
    }

    public Page getPreviousPage()
    {
        return previousPage;
    }

    public void setPreviousPage(Page previousPage)
    {
        this.previousPage = previousPage;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }
}
