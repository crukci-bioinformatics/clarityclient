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

package com.genologics.ri.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityQueryResult;
import com.genologics.ri.Page;
import com.genologics.ri.PaginatedBatch;

/**
 *
 * The representation of a list of user defined type configuration links.
 * <p>
 * The system enforces a maximum number of elements when generating the list of
 * links. When the size of the request result set is larger than the system
 * maximum, the list represents a paged view of the overall results, and the
 * previous-page and next-page elements provide URIs linking to the previous or
 * next page of links in the overall results.
 * </p>
 */
@ClarityQueryResult(entityClass = Type.class)
@XmlRootElement(name = "udts")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "udts", propOrder = { "udtConfigLinks", "previousPage", "nextPage" })
public class Udts implements PaginatedBatch<UdtConfigLink>, Serializable
{
    private static final long serialVersionUID = -3512684674322879203L;

    @XmlElement(name = "udtconfig")
    protected List<UdtConfigLink> udtConfigLinks;

    @XmlElement(name = "previous-page")
    protected Page previousPage;

    @XmlElement(name = "next-page")
    protected Page nextPage;

    public List<UdtConfigLink> getLinks()
    {
        if (udtConfigLinks == null)
        {
            udtConfigLinks = new ArrayList<UdtConfigLink>();
        }
        return this.udtConfigLinks;
    }

    @Override
    public List<UdtConfigLink> getList()
    {
        return getLinks();
    }

    @Override
    public Iterator<UdtConfigLink> iterator()
    {
        return getLinks().iterator();
    }

    @Override
    public int getSize()
    {
        return getLinks().size();
    }

    @Override
    public Page getPreviousPage()
    {
        return previousPage;
    }

    @Override
    public void setPreviousPage(Page previousPage)
    {
        this.previousPage = previousPage;
    }

    @Override
    public Page getNextPage()
    {
        return nextPage;
    }

    @Override
    public void setNextPage(Page nextPage)
    {
        this.nextPage = nextPage;
    }

}
