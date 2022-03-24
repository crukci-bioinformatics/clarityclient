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

package com.genologics.ri.process;

import static com.genologics.ri.Namespaces.FILE_NAMESPACE;
import static com.genologics.ri.Namespaces.UDF_NAMESPACE;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.cruk.genologics.api.jaxb.ShortDateAdapter;

import com.genologics.ri.GenologicsEntity;
import com.genologics.ri.LimsEntity;
import com.genologics.ri.Linkable;
import com.genologics.ri.configuration.FieldType;
import com.genologics.ri.file.GenologicsFile;
import com.genologics.ri.instrument.Instrument;
import com.genologics.ri.processtype.ProcessType;
import com.genologics.ri.researcher.Researcher;
import com.genologics.ri.userdefined.UDF;
import com.genologics.ri.userdefined.UDT;

/**
 *
 * The detailed representation of a process.
 */
@GenologicsEntity(uriSection = "processes", cacheable = true, creatable = true, updateable = true)
@XmlRootElement(name = "process")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "process",
         propOrder = { "processType", "dateRun", "technician", "inputOutputMaps", "type", "fields",
                       "files", "protocolName", "instrument", "parameter" })
public class GenologicsProcess implements LimsEntity<GenologicsProcess>, Serializable
{
    private static final long serialVersionUID = 6767285324829996005L;

    @XmlElement(name = "type")
    protected ProcessTypeLink processType;

    @XmlElement(name = "date-run")
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(ShortDateAdapter.class)
    protected Date dateRun;

    @XmlElement(name = "technician")
    protected Technician technician;

    @XmlElement(name = "input-output-map")
    protected List<InputOutputMap> inputOutputMaps;

    @XmlElement(name = "type", namespace = UDF_NAMESPACE)
    protected UDT type;

    @XmlElement(name = "field", namespace = UDF_NAMESPACE)
    protected List<UDF> fields;

    @XmlElement(name = "file", namespace = FILE_NAMESPACE)
    protected List<GenologicsFile> files;

    @XmlElement(name = "protocol-name")
    protected String protocolName;

    @XmlElement(name = "instrument")
    protected InstrumentLink instrument;

    @XmlElement(name = "process-parameter")
    protected Parameter parameter;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public GenologicsProcess()
    {
    }

    public GenologicsProcess(URI uri)
    {
        this.uri = uri;
    }

    public GenologicsProcess(URI uri, String limsid)
    {
        this.limsid = limsid;
        this.uri = uri;
    }

    public String getLimsid()
    {
        return limsid;
    }

    public void setLimsid(String value)
    {
        this.limsid = value;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI value)
    {
        this.uri = value;
    }

    public ProcessTypeLink getProcessType()
    {
        return processType;
    }

    public void setProcessType(Linkable<ProcessType> link)
    {
        this.processType = new ProcessTypeLink(link);
    }

    public void setProcessType(ProcessType processType)
    {
        setProcessType(new ProcessTypeLink(processType));
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

    public List<InputOutputMap> getInputOutputMaps()
    {
        if (inputOutputMaps == null)
        {
            inputOutputMaps = new ArrayList<InputOutputMap>();
        }
        return inputOutputMaps;
    }

    public InputOutputMap addInputOutputMap(InputOutputMap ioMap)
    {
        getInputOutputMaps().add(ioMap);
        return ioMap;
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

    public List<GenologicsFile> getFiles()
    {
        if (files == null)
        {
            files = new ArrayList<GenologicsFile>();
        }
        return files;
    }

    public GenologicsFile addFile(GenologicsFile f)
    {
        getFiles().add(f);
        return f;
    }

    public String getProtocolName()
    {
        return protocolName;
    }

    public void setProtocolName(String protocolName)
    {
        this.protocolName = protocolName;
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

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(limsid);
        if (processType != null)
        {
            sb.append(' ').append(processType.getName());
        }
        return sb.toString();
    }

}
