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
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.cruk.clarity.api.jaxb.LongTimestampAdapter;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.LimsEntity;
import com.genologics.ri.Linkable;

/**
 *
 * The detailed representation of a step.
 * <p>
 * Since API version 2.27, the step can be deleted to cancel a process.
 * Requires the current status of step to be "in progress".
 * </p>
 */
@ClarityEntity(uriSection = "steps", removable = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "step",
         propOrder = { "dateStarted", "dateCompleted",
                       "configuration", "actions", "reagents", "pools", "placements", "reagentLots",
                       "programStatus", "setup", "details", "availablePrograms", "automaticNextStep" })
@XmlRootElement(name = "step")
public class ProcessStep implements LimsEntity<ProcessStep>, Serializable
{
    private static final long serialVersionUID = 2856981733241902872L;

    /**
     * @since 2.22
     */
    @XmlElement(name = "date-started")
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(LongTimestampAdapter.class)
    protected Date dateStarted;

    /**
     * @since 2.22
     */
    @XmlElement(name = "date-completed")
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(LongTimestampAdapter.class)
    protected Date dateCompleted;

    @XmlElement(name = "configuration")
    protected StepConfiguration configuration;

    @XmlElement(name = "actions")
    protected ActionsLink actions;

    @XmlElement(name = "reagents")
    protected ReagentsLink reagents;

    @XmlElement(name = "pools")
    protected PoolsLink pools;

    @XmlElement(name = "placements")
    protected PlacementsLink placements;

    /**
     * @since 2.18
     */
    @XmlElement(name = "reagent-lots")
    protected ReagentLotsLink reagentLots;

    @XmlElement(name = "program-status")
    protected ProgramStatusLink programStatus;

    /**
     * @since 2.18
     */
    @XmlElement(name = "setup")
    protected StepSetupLink setup;

    /**
     * @since 2.18
     */
    @XmlElement(name = "details")
    protected StepDetailsLink details;

    /**
     * @since 2.18
     */
    @XmlElementWrapper(name = "available-programs")
    @XmlElement(name = "available-program")
    protected List<AvailableProgram> availablePrograms;

    /**
     * @since 2.22
     */
    @XmlElement(name = "automatic-next-step")
    protected AutomaticNextStepLink automaticNextStep;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    /**
     * Now an enumeration since release 2.31.3 of the client.
     *
     * @since 2.18
     */
    @XmlAttribute(name = "current-state")
    protected ProcessState currentState;


    public ProcessStep()
    {
    }

    public ProcessStep(URI uri)
    {
        this.uri = uri;
    }

    public ProcessStep(URI uri, String limsid)
    {
        this.uri = uri;
        this.limsid = limsid;
    }

    public Date getDateStarted()
    {
        return dateStarted;
    }

    public void setDateStarted(Date dateStarted)
    {
        this.dateStarted = dateStarted;
    }

    public Date getDateCompleted()
    {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted)
    {
        this.dateCompleted = dateCompleted;
    }

    public StepConfiguration getConfiguration()
    {
        return configuration;
    }

    public void setConfiguration(StepConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public ActionsLink getActions()
    {
        return actions;
    }

    public void setActions(ActionsLink actions)
    {
        this.actions = actions;
    }

    public ReagentsLink getReagents()
    {
        return reagents;
    }

    public void setReagents(Linkable<Reagents> reagents)
    {
        this.reagents = new ReagentsLink(reagents);
    }

    public PoolsLink getPools()
    {
        return pools;
    }

    public void setPools(Linkable<Pools> pools)
    {
        this.pools = new PoolsLink(pools);
    }

    public PlacementsLink getPlacements()
    {
        return placements;
    }

    public void setPlacements(Linkable<Placements> placements)
    {
        this.placements = new PlacementsLink(placements);
    }

    public ProgramStatusLink getProgramStatus()
    {
        return programStatus;
    }

    public void setProgramStatus(Linkable<ProgramStatus> programStatus)
    {
        this.programStatus = new ProgramStatusLink(programStatus);
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public String getLimsid()
    {
        return limsid;
    }

    public void setLimsid(String limsid)
    {
        this.limsid = limsid;
    }

    public ReagentLotsLink getReagentLots()
    {
        return reagentLots;
    }

    public void setReagentLots(Linkable<ReagentLots> link)
    {
        this.reagentLots = new ReagentLotsLink(link);
    }

    public StepSetupLink getSetup()
    {
        return setup;
    }

    public void setSetup(Linkable<StepSetup> link)
    {
        this.setup = new StepSetupLink(link);
    }

    public StepDetailsLink getStepDetails()
    {
        return details;
    }

    public void setDetails(Linkable<StepDetails> link)
    {
        this.details = new StepDetailsLink(uri);
    }

    public List<AvailableProgram> getAvailablePrograms()
    {
        if (availablePrograms == null)
        {
            availablePrograms = new ArrayList<AvailableProgram>();
        }
        return availablePrograms;
    }

    public AvailableProgram addAvailableProgram(AvailableProgram ap)
    {
        getAvailablePrograms().add(ap);
        return ap;
    }

    public ProcessState getCurrentState()
    {
        return currentState;
    }

    public void setCurrentState(ProcessState currentState)
    {
        this.currentState = currentState;
    }

    public AutomaticNextStepLink getAutomaticNextStep()
    {
        return automaticNextStep;
    }

    public void setAutomaticNextStep(Linkable<ProcessStep> step)
    {
        this.automaticNextStep = new AutomaticNextStepLink(step);
    }
}
