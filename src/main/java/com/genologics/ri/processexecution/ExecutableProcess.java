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

package com.genologics.ri.processexecution;

import static com.genologics.ri.Namespaces.UDF_NAMESPACE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.cruk.clarity.api.jaxb.ShortDateAdapter;

import com.genologics.ri.Linkable;
import com.genologics.ri.configuration.FieldType;
import com.genologics.ri.instrument.Instrument;
import com.genologics.ri.processtype.ProcessType;
import com.genologics.ri.researcher.Researcher;
import com.genologics.ri.userdefined.UDF;
import com.genologics.ri.userdefined.UDT;

/**
 *
 * The process element defines the XML needed to run a process.
 */
@XmlRootElement(name = "process")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "process",
         propOrder = { "processType", "dateRun", "technician", "inputOutputMaps",
                       "type", "fields", "instrument", "parameter" })
public class ExecutableProcess implements Serializable
{
    private static final long serialVersionUID = -4582482597481524823L;

    @XmlElement(name = "type")
    protected String processType;

    @XmlElement(name = "date-run")
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(ShortDateAdapter.class)
    protected Date dateRun;

    @XmlElement(name = "technician")
    protected Technician technician;

    @XmlElement(name = "input-output-map")
    protected List<ExecutableInputOutputMap> inputOutputMaps;

    @XmlElement(name = "type", namespace = UDF_NAMESPACE)
    protected UDT type;

    @XmlElement(name = "field", namespace = UDF_NAMESPACE)
    protected List<UDF> fields;

    @XmlElement(name = "instrument")
    protected InstrumentLink instrument;

    @XmlElement(name = "process-parameter")
    protected Parameter parameter;


    public ExecutableProcess()
    {
    }

    public ExecutableProcess(String processType)
    {
        this.processType = processType;
    }

    public ExecutableProcess(String processType, Linkable<Researcher> technician)
    {
        this.processType = processType;
        setTechnician(technician);
    }

    public String getProcessType()
    {
        return processType;
    }

    public void setProcessType(String processType)
    {
        this.processType = processType;
    }

    public void setProcessType(ProcessType processType)
    {
        this.processType = processType.getName();
    }

    public Date getDateRun()
    {
        return dateRun;
    }

    public void setDateRun(Date dateRun)
    {
        this.dateRun = dateRun;
    }

    public Technician getTechnician()
    {
        return technician;
    }

    public void setTechnician(Linkable<Researcher> link)
    {
        this.technician = new Technician(link);
    }

    public List<ExecutableInputOutputMap> getInputOutputMaps()
    {
        if (inputOutputMaps == null)
        {
            inputOutputMaps = new ArrayList<ExecutableInputOutputMap>();
        }
        return inputOutputMaps;
    }

    public ExecutableInputOutputMap addInputOutputMap(ExecutableInputOutputMap ioMap)
    {
        getInputOutputMaps().add(ioMap);
        return ioMap;
    }

    public ExecutableInputOutputMap newInputOutputMap()
    {
        return addInputOutputMap(new ExecutableInputOutputMap());
    }

    public UDT getUserDefinedType()
    {
        return type;
    }

    public UDT setUserDefinedType(UDT type)
    {
        this.type = type;
        return this.type;
    }

    public UDT setUserDefinedType(String type)
    {
        this.type = new UDT(type);
        return this.type;
    }

    public List<UDF> getUserDefinedFields()
    {
        if (fields == null)
        {
            fields = new ArrayList<UDF>();
        }
        return fields;
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

    public InstrumentLink getInstrument()
    {
        return instrument;
    }

    public void setInstrument(Linkable<Instrument> link)
    {
        this.instrument = new InstrumentLink(link);
    }

    public Parameter getParameter()
    {
        return parameter;
    }

    public void setParameter(Parameter parameter)
    {
        this.parameter = parameter;
    }

}
