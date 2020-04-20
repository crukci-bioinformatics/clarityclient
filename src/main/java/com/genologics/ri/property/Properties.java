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

package com.genologics.ri.property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.GenologicsQueryResult;
import com.genologics.ri.Page;
import com.genologics.ri.PaginatedBatch;
import com.genologics.ri.protocolconfiguration.Protocol;

/**
 *
 * The representation for a list of property links.
 * <p>
 * The system enforces a maximum number of elements when generating the list of
 * links. When the size of the request result set is larger than the system
 * maximum, the list represents a paged view of the overall results, and the
 * previous-page and next-page elements provide URIs linking to the previous or
 * next page of links in the overall results.
 * </p>
 */
@GenologicsQueryResult(entityClass = Protocol.class)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "properties", propOrder = { "properties", "previousPage", "nextPage" })
public class Properties implements PaginatedBatch<PropertyLink>, Serializable
{
    private static final long serialVersionUID = -4193984606951017687L;

    @XmlElement(name = "property")
    protected List<PropertyLink> properties;

    @XmlElement(name = "previous-page")
    protected Page previousPage;

    @XmlElement(name = "next-page")
    protected Page nextPage;

    public List<PropertyLink> getProperties()
    {
        if (properties == null)
        {
            properties = new ArrayList<PropertyLink>();
        }
        return properties;
    }

    @Override
    public Iterator<PropertyLink> iterator()
    {
        return getProperties().iterator();
    }

    @Override
    public List<PropertyLink> getList()
    {
        return getProperties();
    }

    @Override
    public int getSize()
    {
        return properties == null ? 0 : properties.size();
    }

    public Page getPreviousPage()
    {
        return previousPage;
    }

    public void setPreviousPage(Page previousPage)
    {
        this.previousPage = previousPage;
    }

    public Page getNextPage()
    {
        return nextPage;
    }

    public void setNextPage(Page nextPage)
    {
        this.nextPage = nextPage;
    }

}
