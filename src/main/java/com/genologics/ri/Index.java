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

package com.genologics.ri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Index is the entry point to a supported version of the API, providing a list
 * of links to the available resources in that version in the system.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "index")
public class Index implements Batch<Link>, Serializable
{
    private static final long serialVersionUID = -458228684534981324L;

    @XmlElement(name = "link")
    protected List<Link> links;

    public List<Link> getLinks()
    {
        if (links == null)
        {
            links = new ArrayList<Link>();
        }
        return this.links;
    }

    @Override
    public Iterator<Link> iterator()
    {
        return getLinks().iterator();
    }

    @Override
    public List<Link> getList()
    {
        return getLinks();
    }

    @Override
    public int getSize()
    {
        return getLinks().size();
    }

}
