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

package com.genologics.ri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The representation of a list of resource links.
 */
@XmlRootElement(name = "links")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "links")
public class Links implements Batch<Link>, Serializable
{
    private static final long serialVersionUID = -9069517524812753843L;

    @XmlElement(name = "link")
    protected List<Link> links;

    public Links()
    {
    }

    public Links(int capacity)
    {
        this.links = new ArrayList<Link>(capacity);
    }

    public Links(Collection<Link> links)
    {
        this.links = new ArrayList<Link>(links);
    }

    public List<Link> getLinks()
    {
        if (links == null)
        {
            links = new ArrayList<Link>(128);
        }
        return this.links;
    }

    public Link add(Link link)
    {
        getLinks().add(link);
        return link;
    }

    public Link add(Linkable<?> link)
    {
        Link l = new Link(link);
        getLinks().add(l);
        return l;
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
