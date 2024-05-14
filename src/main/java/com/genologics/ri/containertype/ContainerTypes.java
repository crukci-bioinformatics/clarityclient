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

package com.genologics.ri.containertype;

import static com.genologics.ri.Namespaces.CONTAINER_TYPE_NAMESPACE;

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
 * The representation for a list of container type links.
 * <p>
 * The system enforces a maximum number of elements when generating the list of
 * links. When the size of the request result set is larger than the system
 * maximum, the list represents a paged view of the overall results, and the
 * previous-page and next-page elements provide URIs linking to the previous or
 * next page of links in the overall results.
 * </p>
 */
@ClarityQueryResult(entityClass = ContainerType.class)
@XmlRootElement(name = "container-types")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = CONTAINER_TYPE_NAMESPACE, name = "container-types",
         propOrder = { "containerTypes", "previousPage", "nextPage" })
public class ContainerTypes implements PaginatedBatch<ContainerTypeLink>, Serializable
{
    private static final long serialVersionUID = 8642031910932251369L;

    @XmlElement(name = "container-type")
    protected List<ContainerTypeLink> containerTypes;

    @XmlElement(name = "previous-page")
    protected Page previousPage;

    @XmlElement(name = "next-page")
    protected Page nextPage;

    public List<ContainerTypeLink> getContainerTypes()
    {
        if (containerTypes == null)
        {
            containerTypes = new ArrayList<ContainerTypeLink>();
        }
        return containerTypes;
    }

    @Override
    public List<ContainerTypeLink> getList()
    {
        return getContainerTypes();
    }

    @Override
    public Iterator<ContainerTypeLink> iterator()
    {
        return getContainerTypes().iterator();
    }

    @Override
    public int getSize()
    {
        return getContainerTypes().size();
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
