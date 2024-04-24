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

package com.genologics.ri.sample;

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
 * Batch fetch and update holder for samples.
 * @since 2.18
 */
@ClarityBatchRetrieveResult(entityClass = Sample.class, batchCreate = true, batchUpdate = true)
@XmlRootElement(name = "details")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "details", propOrder = { "samples", "sampleCreations" })
public class SampleBatchFetchResult implements BatchUpdate<Sample>, Serializable
{
    private static final long serialVersionUID = 8207729034543316762L;

    @XmlElement(name = "sample", namespace = Namespaces.SAMPLE_NAMESPACE)
    protected List<Sample> samples;

    @XmlElement(name = "samplecreation", namespace = Namespaces.SAMPLE_NAMESPACE)
    protected List<SampleCreation> sampleCreations;

    public List<Sample> getSamples()
    {
        if (samples == null)
        {
            samples = new ArrayList<Sample>();
        }
        return samples;
    }

    public List<SampleCreation> getSampleCreations()
    {
        if (sampleCreations == null)
        {
            sampleCreations = new ArrayList<SampleCreation>();
        }
        return sampleCreations;
    }

    @Override
    public List<Sample> getList()
    {
        return getSamples();
    }

    @Override
    public int getSize()
    {
        return samples == null ? 0 : samples.size();
    }

    @Override
    public Iterator<Sample> iterator()
    {
        return getSamples().iterator();
    }

    @Override
    public void addForUpdate(Collection<Sample> entities)
    {
        getSamples().addAll(entities);
    }

    /**
     * Add samples to this object ready for creation. This actually involves
     * creating {@code SampleCreation} objects in the {@code sampleCreations}
     * list rather than simply putting the original entities into the normal
     * list.
     *
     * @param samples The collection of Sample objects.
     *
     * @throws IllegalStateException if any sample does not have its
     * creation location set or already has a URI.
     *
     * @see Sample#setCreationLocation(com.genologics.ri.Location)
     * @see SampleCreation#SampleCreation(Sample)
     */
    @Override
    public void addForCreate(Collection<Sample> samples)
    {
        for (Sample s : samples)
        {
            if (s.getUri() != null)
            {
                throw new IllegalStateException("Sample '" + s.getName() + "' already has a URI");
            }
            if (s.getCreationLocation() == null)
            {
                throw new IllegalStateException("Sample '" + s.getName() + "' has no creation location set");
            }
        }

        getSampleCreations();
        for (Sample s : samples)
        {
            sampleCreations.add(new SampleCreation(s));
        }
    }
}
