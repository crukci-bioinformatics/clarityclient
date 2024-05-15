/*
 * CRUK-CI Clarity REST API Java Client.
 * Copyright (C) 2016 Cancer Research UK Cambridge Institute.
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

package com.genologics.ri.instrumenttype;

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
 * @since 2.24
 */
@ClarityQueryResult(entityClass = InstrumentType.class)
@XmlRootElement(name = "instrument-types")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instrument-types", propOrder = { "instrumentTypes", "previousPage", "nextPage" })
public class InstrumentTypes implements PaginatedBatch<InstrumentTypeLink>, Serializable
{
    private static final long serialVersionUID = 1268811869773163450L;

    @XmlElement(name = "instrument-type")
    protected List<InstrumentTypeLink> instrumentTypes;

    /**
     * @since 2.31
     */
    @XmlElement(name = "previous-page")
    protected Page previousPage;

    /**
     * @since 2.31
     */
    @XmlElement(name = "next-page")
    protected Page nextPage;


    public InstrumentTypes()
    {
    }

    @Override
    public Iterator<InstrumentTypeLink> iterator()
    {
        return getList().iterator();
    }

    @Override
    public List<InstrumentTypeLink> getList()
    {
        if (instrumentTypes == null)
        {
            instrumentTypes = new ArrayList<InstrumentTypeLink>();
        }
        return instrumentTypes;
    }

    @Override
    public int getSize()
    {
        return instrumentTypes == null ? 0 : instrumentTypes.size();
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
