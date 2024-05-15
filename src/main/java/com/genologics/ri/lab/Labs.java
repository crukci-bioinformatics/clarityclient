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

package com.genologics.ri.lab;

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
 * The representation of a list of lab links.
 * <p>
 * The system enforces a maximum number of elements when generating the list of
 * links. When the size of the request result set is larger than the system
 * maximum, the list represents a paged view of the overall results, and the
 * previous-page and next-page elements provide URIs linking to the previous or
 * next page of links in the overall results.
 * </p>
 */
@ClarityQueryResult(entityClass = Lab.class)
@XmlRootElement(name = "labs")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "labs", propOrder = { "labs", "previousPage", "nextPage" })
public class Labs implements PaginatedBatch<LabLink>, Serializable
{
    private static final long serialVersionUID = -2447458193812155143L;

    @XmlElement(name = "lab")
    protected List<LabLink> labs;

    @XmlElement(name = "previous-page")
    protected Page previousPage;

    @XmlElement(name = "next-page")
    protected Page nextPage;

    public List<LabLink> getLabs()
    {
        if (labs == null)
        {
            labs = new ArrayList<LabLink>();
        }
        return labs;
    }

    @Override
    public List<LabLink> getList()
    {
        return getLabs();
    }

    @Override
    public Iterator<LabLink> iterator()
    {
        return getLabs().iterator();
    }

    @Override
    public int getSize()
    {
        return getLabs().size();
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
