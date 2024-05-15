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

package com.genologics.ri.container;

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

import com.genologics.ri.BatchUpdate;
import com.genologics.ri.ClarityBatchRetrieveResult;
import com.genologics.ri.Namespaces;

/**
 *
 * The representation of a list of resource details.
 */
@ClarityBatchRetrieveResult(entityClass = Container.class, batchCreate = true, batchUpdate = true)
@XmlRootElement(name = "details")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "details", propOrder = { "containers" })
public class ContainerBatchFetchResult implements BatchUpdate<Container>, Serializable
{
    private static final long serialVersionUID = 3942410984112211790L;

    @XmlElement(name = "container", namespace = Namespaces.CONTAINER_NAMESPACE)
    protected List<Container> containers;

    public ContainerBatchFetchResult()
    {
    }

    public ContainerBatchFetchResult(Collection<Container> containers)
    {
        this.containers = new ArrayList<Container>(containers);
    }

    public List<Container> getContainers()
    {
        if (containers == null)
        {
            containers = new ArrayList<Container>();
        }
        return this.containers;
    }

    @Override
    public Iterator<Container> iterator()
    {
        return getContainers().iterator();
    }

    @Override
    public List<Container> getList()
    {
        return getContainers();
    }

    @Override
    public int getSize()
    {
        return getContainers().size();
    }

    @Override
    public void addForCreate(Collection<Container> entities)
    {
        getContainers().addAll(entities);
    }

    @Override
    public void addForUpdate(Collection<Container> entities)
    {
        // Nothing to worry about with state parameters for containers.
        getContainers().addAll(entities);
    }

}
