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

package com.genologics.ri.artifact;

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

@ClarityBatchRetrieveResult(entityClass = Artifact.class, batchUpdate = true)
@XmlRootElement(name = "details")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "details")
public class ArtifactBatchFetchResult implements BatchUpdate<Artifact>, Serializable
{
    private static final long serialVersionUID = -1280235468943933621L;

    @XmlElement(name = "artifact", namespace = Namespaces.ARTIFACT_NAMESPACE)
    protected List<Artifact> artifacts;

    public ArtifactBatchFetchResult()
    {
    }

    public ArtifactBatchFetchResult(Collection<Artifact> artifacts)
    {
        this.artifacts = new ArrayList<Artifact>(artifacts);
    }

    public List<Artifact> getArtifacts()
    {
        if (artifacts == null)
        {
            artifacts = new ArrayList<Artifact>();
        }
        return artifacts;
    }

    @Override
    public Iterator<Artifact> iterator()
    {
        return getArtifacts().iterator();
    }

    @Override
    public List<Artifact> getList()
    {
        return getArtifacts();
    }

    @Override
    public int getSize()
    {
        return getArtifacts().size();
    }

    @Override
    public void addForCreate(Collection<Artifact> entities)
    {
        getArtifacts().addAll(entities);
    }

    @Override
    public void addForUpdate(Collection<Artifact> entities)
    {
        // Ignore the state part of the query and just let it pass.
        getArtifacts().addAll(entities);

        /*
        // Need to remove the "state=" part of the artifact URIs.
        getArtifacts();
        for (Artifact a : entities)
        {
            URI uri = URIAdapter.removeStateParameter(a.getUri());
            a.setUri(uri);
            artifacts.add(a);
        }
        */
    }
}
