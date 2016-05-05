/*
 * CRUK-CI Genologics REST API Java Client.
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.Batch;
import com.genologics.ri.GenologicsQueryResult;

/**
 * @since 2.24
 */
@GenologicsQueryResult(entityClass = InstrumentType.class)
@XmlRootElement(name = "instrument-types")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instrument-types")
public class InstrumentTypes implements Batch<InstrumentTypeLink>, Serializable
{
    private static final long serialVersionUID = 7737316665060724718L;

    @XmlElement(name = "instrument-type")
    protected List<InstrumentTypeLink> instrumentTypes;

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
}
