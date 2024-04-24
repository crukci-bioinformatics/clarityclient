/*
 * CRUK-CI Clarity REST API Java Client.
 * Copyright (C) 2018 Cancer Research UK Cambridge Institute.
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

package com.genologics.ri.automation;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityQueryResult;
import com.genologics.ri.Page;
import com.genologics.ri.PaginatedBatch;

/**
 * @since 2.26
 */
@ClarityQueryResult(entityClass = Automation.class)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "automations", propOrder = { "automations", "previousPage", "nextPage" })
@XmlRootElement(name = "automations")
public class Automations implements PaginatedBatch<AutomationLink>, Serializable
{
    private static final long serialVersionUID = -3048084395718929498L;

    @XmlElement(name = "automation")
    protected List<AutomationLink> automations;

    @XmlElement(name = "previous-page")
    protected Page previousPage;

    @XmlElement(name = "next-page")
    protected Page nextPage;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public Automations()
    {
    }

    @Override
    public List<AutomationLink> getList()
    {
        return getAutomations();
    }

    @Override
    public int getSize()
    {
        return automations == null ? 0 : automations.size();
    }

    @Override
    public Iterator<AutomationLink> iterator()
    {
        return getAutomations().iterator();
    }

    public List<AutomationLink> getAutomations()
    {
        if (automations == null)
        {
            automations = new ArrayList<>();
        }
        return automations;
    }

    public Page getPreviousPage()
    {
        return previousPage;
    }

    public void setPreviousPage(Page value)
    {
        this.previousPage = value;
    }

    public Page getNextPage()
    {
        return nextPage;
    }

    public void setNextPage(Page value)
    {
        this.nextPage = value;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI value)
    {
        this.uri = value;
    }
}
