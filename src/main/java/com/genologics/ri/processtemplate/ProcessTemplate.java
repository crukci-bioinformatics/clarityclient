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

package com.genologics.ri.processtemplate;

import static com.genologics.ri.Namespaces.UDF_NAMESPACE;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.Linkable;
import com.genologics.ri.configuration.FieldType;
import com.genologics.ri.instrument.Instrument;
import com.genologics.ri.processtype.ProcessType;
import com.genologics.ri.researcher.Researcher;
import com.genologics.ri.userdefined.UDF;
import com.genologics.ri.userdefined.UDFHolder;
import com.genologics.ri.userdefined.UDT;

/**
 * The detailed representation of a Process Template.
 */
@ClarityEntity(uriSection = "processtemplates")
@XmlRootElement(name = "process-template")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "process-template",
         propOrder = { "name", "processType", "technician", "instrument", "parameter",
                       "type", "fields", "defaultTemplate" })
public class ProcessTemplate implements Linkable<ProcessTemplate>, UDFHolder, Serializable
{
    private static final long serialVersionUID = 3219761173873030207L;

    @XmlElement(name = "name")
    protected String name;

    @XmlElement(name = "type")
    protected ProcessTypeLink processType;

    @XmlElement(name = "technician")
    protected Technician technician;

    @XmlElement(name = "instrument")
    protected InstrumentLink instrument;

    @XmlElement(name = "process-parameter")
    protected Parameter parameter;

    @XmlElement(name = "type", namespace = UDF_NAMESPACE)
    protected UDT type;

    @XmlElement(name = "field", namespace = UDF_NAMESPACE)
    protected List<UDF> fields;

    @XmlElement(name = "is-default")
    protected Boolean defaultTemplate;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public ProcessTemplate()
    {
    }

    public ProcessTemplate(URI uri)
    {
        this.uri = uri;
    }

    public ProcessTemplate(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI value)
    {
        this.uri = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ProcessTypeLink getProcessType()
    {
        return processType;
    }

    public void setProcessType(ProcessTypeLink processType)
    {
        this.processType = processType;
    }

    public void setProcessType(ProcessType processType)
    {
        setProcessType(new ProcessTypeLink(processType));
    }

    public Technician getTechnician()
    {
        return technician;
    }

    public void setTechnician(Technician technician)
    {
        this.technician = technician;
    }

    public void setTechnician(Researcher researcher)
    {
        setTechnician(new Technician(researcher));
    }

    public InstrumentLink getInstrument()
    {
        return instrument;
    }

    public void setInstrument(InstrumentLink instrument)
    {
        this.instrument = instrument;
    }

    public void setInstrument(Instrument instrument)
    {
        setInstrument(new InstrumentLink(instrument));
    }

    public Parameter getParameter()
    {
        return parameter;
    }

    public void setParameter(Parameter parameter)
    {
        this.parameter = parameter;
    }

    public UDT getUserDefinedType()
    {
        return type;
    }

    public UDT setUserDefinedType(UDT value)
    {
        this.type = value;
        return this.type;
    }

    public UDT setUserDefinedType(String type)
    {
        this.type = new UDT(type);
        return this.type;
    }

    @Override
    public List<UDF> getUserDefinedFields()
    {
        if (fields == null)
        {
            fields = new ArrayList<UDF>();
        }
        return fields;
    }

    @Deprecated
    public UDF addUserDefinedField(UDF udf)
    {
        getUserDefinedFields().add(udf);
        return udf;
    }

    @Deprecated
    public UDF addUserDefinedField(String name, FieldType type, String value)
    {
        return addUserDefinedField(new UDF(name, type, value));
    }

    public Boolean isDefault()
    {
        return defaultTemplate;
    }

    public void setDefault(Boolean defaultTemplate)
    {
        this.defaultTemplate = defaultTemplate;
    }

}
