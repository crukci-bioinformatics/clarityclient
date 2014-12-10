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

import com.genologics.ri.ExternalId;
import com.genologics.ri.GenologicsEntity;
import com.genologics.ri.LimsEntity;
import com.genologics.ri.Linkable;
import com.genologics.ri.configuration.FieldType;
import com.genologics.ri.file.GenologicsFile;
import com.genologics.ri.researcher.Researcher;
import com.genologics.ri.userdefined.UDF;
import com.genologics.ri.userdefined.UDT;

/**
 * The detailed representation of a Project.
 */
@GenologicsEntity(uriSection = "projects", creatable = true, updateable = true)
@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "project",
         propOrder = { "name", "openDate", "closeDate", "invoiceDate", "researcher", "type",
                       "fields", "externalIds", "files" })
public class Project implements LimsEntity<Project>, Serializable
{
    private static final long serialVersionUID = -2194491709522434295L;

    @XmlElement(name = "name")
    protected String name;

    @XmlElement(name = "open-date")
    @XmlSchemaType(name = "date")
    protected Date openDate;

    @XmlElement(name = "close-date")
    @XmlSchemaType(name = "date")
    protected Date closeDate;

    @XmlElement(name = "invoice-date")
    @XmlSchemaType(name = "date")
    protected Date invoiceDate;

    protected ResearcherLink researcher;

    @XmlElement(name = "type", namespace = UDF_NAMESPACE)
    protected UDT type;

    @XmlElement(name = "field", namespace = UDF_NAMESPACE)
    protected List<UDF> fields;

    @XmlElement(name = "externalid", namespace = ROOT_NAMESPACE)
    protected List<ExternalId> externalIds;

    @XmlElement(name = "file", namespace = FILE_NAMESPACE)
    protected List<GenologicsFile> files;

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

    /**
     * The UDT of the Project. <br/>
     * Always returns with GET: No <br/>
     * Creatable with POST: Yes <br/>
     * Required for POST: No, unless the UDT has been configured as required. <br/>
     * Updatable with PUT: Yes <br/>
     * Required for PUT: No, unless the UDT has been configured as required. If
     * a current UDT is not provided, existing values are deleted.
     */
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

    /**
     * The UDFs of the Project. <br/>
     * Always returns with GET: No <br/>
     * Creatable with POST: Yes <br/>
     * Required for POST: No, unless the UDF has been configured as required. <br/>
     * Updatable with PUT: Yes <br/>
     * Required for PUT: No, unless the UDF has been configured as required. If
     * a current UDF is not provided, existing values are deleted.
     */
    public List<UDF> getUserDefinedFields()
    {
        if (fields == null)
        {
            fields = new ArrayList<UDF>();
        }
        return this.fields;
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

    /**
     *
     * Each external id is an identifier that allows looking up related
     * information about the Project from an external system. <br/>
     * Always returns with GET: No <br/>
     * Creatable with POST: Yes <br/>
     * Required for POST: No <br/>
     * Updatable with PUT: Yes <br/>
     * Required for PUT: No
     */
    public List<ExternalId> getExternalIds()
    {
        if (externalIds == null)
        {
            externalIds = new ArrayList<ExternalId>();
        }
        return this.externalIds;
    }

    /**
     *
     * Each File provides a URI linking to the detailed representation of a File
     * associated with the Project. <br/>
     * Always returns with GET: No <br/>
     * Creatable with POST: Yes <br/>
     * Required for POST: No <br/>
     * Updatable with PUT: Yes <br/>
     * Required for PUT: No
     */
    public List<GenologicsFile> getFiles()
    {
        if (files == null)
        {
            files = new ArrayList<GenologicsFile>();
        }
        return this.files;
    }

    public GenologicsFile addFile(GenologicsFile f)
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
        StringBuilder sb = new StringBuilder();
        sb.append(limsid).append(' ').append(name);
        return sb.toString();
    }
}
