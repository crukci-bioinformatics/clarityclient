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

package com.genologics.ri.step;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.Link;
import com.genologics.ri.Linkable;
import com.genologics.ri.reagentlot.ReagentLot;

/**
 * The list representation of a step's reagent lots.
 *
 * @since 2.18
 */
@ClarityEntity(uriSection = "steps", uriSubsection = "reagentlots", updateable = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reagents-lots", propOrder = { "step", "configuration", "reagentLots" })
@XmlRootElement(name = "lots")
public class ReagentLots implements Linkable<ReagentLots>, Serializable
{
    private static final long serialVersionUID = 27145430586603574L;

    @XmlElement
    protected Link step;

    @XmlElement
    protected StepConfiguration configuration;

    @XmlElementWrapper(name = "reagent-lots")
    @XmlElement(name = "reagent-lot")
    protected List<ReagentLotLink> reagentLots;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public Link getStep()
    {
        return step;
    }

    public void setStep(Link step)
    {
        this.step = step;
    }

    public StepConfiguration getConfiguration()
    {
        return configuration;
    }

    public void setConfiguration(StepConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public List<ReagentLotLink> getReagentLots()
    {
        if (reagentLots == null)
        {
            reagentLots = new ArrayList<ReagentLotLink>();
        }
        return reagentLots;
    }

    public ReagentLotLink addReagentLot(ReagentLotLink link)
    {
        getReagentLots().add(link);
        return link;
    }

    public ReagentLotLink addReagentLot(LimsEntityLinkable<ReagentLot> lot)
    {
        ReagentLotLink link = new ReagentLotLink(lot);
        getReagentLots().add(link);
        return link;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }


}
