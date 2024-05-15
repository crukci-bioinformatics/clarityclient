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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The representation of a list of resource links.
 */
@XmlRootElement(name = "links")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "links")
public class Links implements Batch<Link>, Serializable
{
    private static final long serialVersionUID = 8838137119064380847L;

    @XmlElement(name = "link")
    protected List<Link> linkList;

    public Links()
    {
    }

    public Links(int capacity)
    {
        linkList = new ArrayList<Link>(capacity);
    }

    public Links(Collection<Link> links)
    {
        linkList = new ArrayList<Link>(links);
    }

    public Links(Links links)
    {
        this(links.getLinks());
    }

    public List<Link> getLinks()
    {
        if (linkList == null)
        {
            linkList = new ArrayList<Link>(128);
        }
        return this.linkList;
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

    public void addAll(Links links)
    {
        addAll(links.getLinks());
    }

    public void addAll(Collection<Link> links)
    {
        getLinks().addAll(links);
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
