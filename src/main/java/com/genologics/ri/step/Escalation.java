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
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.Linkable;
import com.genologics.ri.artifact.Artifact;

/**
 *
 * Escalation details.
 *
 * @since 2.18
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "escalation", propOrder = { "request", "review", "escalatedArtifacts" })
public class Escalation implements Serializable
{
    private static final long serialVersionUID = -7363821101050828859L;

    @XmlElement
    protected EscalationRequest request;

    @XmlElement
    protected EscalationReview review;

    @XmlElementWrapper(name = "escalated-artifacts")
    @XmlElement(name = "escalated-artifact")
    protected List<EscalatedArtifact> escalatedArtifacts;


    public EscalationRequest getRequest()
    {
        return request;
    }

    public void setRequest(EscalationRequest request)
    {
        this.request = request;
    }

    public EscalationReview getReview()
    {
        return review;
    }

    public void setReview(EscalationReview review)
    {
        this.review = review;
    }

    public List<EscalatedArtifact> getEscalatedArtifacts()
    {
        if (escalatedArtifacts == null)
        {
            escalatedArtifacts = new ArrayList<EscalatedArtifact>();
        }
        return escalatedArtifacts;
    }

    public EscalatedArtifact addEscalatedArtifact(EscalatedArtifact a)
    {
        getEscalatedArtifacts().add(a);
        return a;
    }

    public EscalatedArtifact addEscalatedArtifact(Linkable<Artifact> link)
    {
        EscalatedArtifact a = new EscalatedArtifact(link);
        getEscalatedArtifacts().add(a);
        return a;
    }

}
