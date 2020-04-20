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

package com.genologics.ri.step;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.GenologicsEntity;
import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.Link;
import com.genologics.ri.Linkable;
import com.genologics.ri.Locatable;
import com.genologics.ri.Namespaces;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.configuration.FieldType;
import com.genologics.ri.instrument.Instrument;
import com.genologics.ri.userdefined.UDF;

/**
 * @since 2.18
 */
@GenologicsEntity(uriSection = "details", processStepComponent = true, updateable = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "details", propOrder = { "step", "configuration", "inputOutputMaps", "fields", "preset", "instrument" })
@XmlRootElement(name = "details")
public class StepDetails implements Locatable, Serializable
{
    private static final long serialVersionUID = 5865493881321340964L;

    @XmlElement
    protected Link step;

    @XmlElement
    protected StepConfiguration configuration;

    @XmlElementWrapper(name = "input-output-maps")
    @XmlElement(name = "input-output-map")
    protected List<InputOutputMap> inputOutputMaps;

    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field", namespace = Namespaces.UDF_NAMESPACE)
    protected List<UDF> fields;

    @XmlElement
    protected String preset;

    @XmlElement
    protected InstrumentLink instrument;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public StepDetails()
    {
    }

    public StepDetails(URI uri)
    {
        this.uri = uri;
    }

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

    public List<InputOutputMap> getInputOutputMaps()
    {
        if (inputOutputMaps == null)
        {
            inputOutputMaps = new ArrayList<InputOutputMap>();
        }
        return inputOutputMaps;
    }

    public InputOutputMap addInputOutputMap(InputOutputMap iomap)
    {
        getInputOutputMaps().add(iomap);
        return iomap;
    }

    public InputOutputMap addInputOutputMap(LimsEntityLinkable<Artifact> input, LimsEntityLinkable<Artifact> output)
    {
        InputOutputMap iomap = new InputOutputMap(input, output);
        getInputOutputMaps().add(iomap);
        return iomap;
    }

    /**
     * The user-defined fields of this Step.
     */
    public List<UDF> getUserDefinedFields()
    {
        if (fields == null)
        {
            fields = new ArrayList<UDF>();
        }
        return fields;
    }

    public UDF getUserDefinedField(String name)
    {
        return UDF.getUDF(fields, name);
    }

    public UDF addUserDefinedField(UDF udf)
    {
        getUserDefinedFields().add(udf);
        return udf;
    }

    public UDF addUserDefinedField(String name, FieldType type, String value)
    {
        return addUserDefinedField(new UDF(name, type, value));
    }

    public String getPreset()
    {
        return preset;
    }

    public void setPreset(String preset)
    {
        this.preset = preset;
    }

    public InstrumentLink getInstrument()
    {
        return instrument;
    }

    public void setInstrument(Linkable<Instrument> link)
    {
        this.instrument = new InstrumentLink(link);
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
