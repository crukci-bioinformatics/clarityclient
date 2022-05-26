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

package com.genologics.ri.project;

import static com.genologics.ri.Namespaces.FILE_NAMESPACE;
import static com.genologics.ri.Namespaces.ROOT_NAMESPACE;
import static com.genologics.ri.Namespaces.UDF_NAMESPACE;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.cruk.clarity.api.jaxb.ShortDateAdapter;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.ExternalId;
import com.genologics.ri.LimsEntity;
import com.genologics.ri.Linkable;
import com.genologics.ri.configuration.FieldType;
import com.genologics.ri.file.ClarityFile;
import com.genologics.ri.researcher.Researcher;
import com.genologics.ri.userdefined.UDF;
import com.genologics.ri.userdefined.UDFHolder;
import com.genologics.ri.userdefined.UDT;

/**
 * The detailed representation of a Project.
 */
@ClarityEntity(uriSection = "projects", creatable = true, updateable = true)
@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "project",
         propOrder = { "name", "openDate", "closeDate", "invoiceDate", "researcher", "type",
                       "fields", "externalIds", "files" })
public class Project implements LimsEntity<Project>, UDFHolder, Serializable
{
    private static final long serialVersionUID = -2194491709522434295L;

    @XmlElement(name = "name")
    protected String name;

    @XmlElement(name = "open-date")
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(ShortDateAdapter.class)
    protected Date openDate;

    @XmlElement(name = "close-date")
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(ShortDateAdapter.class)
    protected Date closeDate;

    @XmlElement(name = "invoice-date")
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(ShortDateAdapter.class)
    protected Date invoiceDate;

    protected ResearcherLink researcher;

    @XmlElement(name = "type", namespace = UDF_NAMESPACE)
    protected UDT type;

    @XmlElement(name = "field", namespace = UDF_NAMESPACE)
    protected List<UDF> fields;

    @XmlElement(name = "externalid", namespace = ROOT_NAMESPACE)
    protected List<ExternalId> externalIds;

    @XmlElement(name = "file", namespace = FILE_NAMESPACE)
    protected List<ClarityFile> files;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;



    public Project()
    {
    }

    public Project(URI uri)
    {
        this.uri = uri;
    }

    public Project(URI uri, String limsid)
    {
        this.uri = uri;
        this.limsid = limsid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        this.name = value;
    }

    public Date getOpenDate()
    {
        return openDate;
    }

    public void setOpenDate(Date openDate)
    {
        this.openDate = openDate;
    }

    public Date getCloseDate()
    {
        return closeDate;
    }

    public void setCloseDate(Date closeDate)
    {
        this.closeDate = closeDate;
    }

    public Date getInvoiceDate()
    {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate)
    {
        this.invoiceDate = invoiceDate;
    }

    public ResearcherLink getResearcher()
    {
        return researcher;
    }

    public void setResearcher(Linkable<Researcher> link)
    {
        this.researcher = new ResearcherLink(link);
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
        return this.fields;
    }

    @Deprecated
    public UDF getUserDefinedField(String name)
    {
        return UDF.getUDF(fields, name);
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

    public List<ExternalId> getExternalIds()
    {
        if (externalIds == null)
        {
            externalIds = new ArrayList<ExternalId>();
        }
        return this.externalIds;
    }

    public List<ClarityFile> getFiles()
    {
        if (files == null)
        {
            files = new ArrayList<ClarityFile>();
        }
        return this.files;
    }

    public ClarityFile addFile(ClarityFile f)
    {
        getFiles().add(f);
        return f;
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

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(30);
        sb.append(limsid).append(' ').append(name);
        return sb.toString();
    }
}
