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

package com.genologics.ri.researcher;

import static com.genologics.ri.Namespaces.ROOT_NAMESPACE;
import static com.genologics.ri.Namespaces.UDF_NAMESPACE;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
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
import com.genologics.ri.Link;
import com.genologics.ri.Linkable;
import com.genologics.ri.configuration.FieldType;
import com.genologics.ri.lab.Lab;
import com.genologics.ri.userdefined.UDF;
import com.genologics.ri.userdefined.UDT;

/**
 * The detailed representation of a researcher.
 */
@GenologicsEntity(uriSection = "researchers", creatable = true, updateable = true, removable = true)
@XmlRootElement(name = "researcher")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "researcher",
         propOrder = { "firstName", "lastName", "phone", "fax", "email", "lab", "type", "fields",
                       "externalIds", "credentials", "initials" })
public class Researcher implements LimsEntity<Researcher>, Serializable
{
    private static final long serialVersionUID = 2552745292977587999L;

    @XmlElement(name = "first-name")
    protected String firstName;

    @XmlElement(name = "last-name")
    protected String lastName;

    protected String phone;

    protected String fax;

    protected String email;

    protected LabLink lab;

    @XmlElement(name = "type", namespace = UDF_NAMESPACE)
    protected UDT type;

    @XmlElement(name = "field", namespace = UDF_NAMESPACE)
    protected List<UDF> fields;

    @XmlElement(name = "externalid", namespace = ROOT_NAMESPACE)
    protected List<ExternalId> externalIds;

    protected Credentials credentials;

    protected String initials;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public Researcher()
    {
    }

    public Researcher(URI uri)
    {
        this.uri = uri;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String value)
    {
        this.firstName = value;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String value)
    {
        this.lastName = value;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String value)
    {
        this.phone = value;
    }

    public String getFax()
    {
        return fax;
    }

    public void setFax(String value)
    {
        this.fax = value;
    }

    /**
     * The researcher's e-mail address.
     *
     * <br/>Always returns with GET: Yes
     * <br/>Creatable with POST: Yes
     * <br/>Required for POST: Yes
     * <br/>Updatable with PUT: Yes
     * <br/>Required for PUT: Yes
     *
     * @return The researcher's e-mail address.
     */
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String value)
    {
        this.email = value;
    }

    public LabLink getLab()
    {
        return lab;
    }

    public void setLab(Linkable<Lab> link)
    {
        this.lab = new LabLink(link);
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

    /**
     *
     * A User-Defined Field that is associated with the researcher. This element
     * is repeated for each UDF associated with the researcher. <br/>
     * Always returns with GET: No <br/>
     * Creatable with POST: Yes <br/>
     * Required for POST: No, unless the UDF has been configured as required. <br/>
     * Updatable with PUT: Yes <br/>
     * Required for PUT: No, unless the UDF has been configured as required. If
     * a current UDF is not provided, existing values are deleted. Gets the
     * value of the field property.
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

    /**
     *
     * An identifier that allows an external system to retrieve information
     * about the researcher. <br/>
     * Always returns with GET: No <br/>
     * Creatable with POST: Yes <br/>
     * Required for POST: No <br/>
     * Updatable with PUT: Yes <br/>
     * Required for PUT: No Gets the value of the externalid property.
     */
    public List<ExternalId> getExternalIds()
    {
        if (externalIds == null)
        {
            externalIds = new ArrayList<ExternalId>();
        }
        return externalIds;
    }

    public Credentials getCredentials()
    {
        return credentials;
    }

    public void setCredentials(Credentials value)
    {
        this.credentials = value;
    }

    public String getInitials()
    {
        return initials;
    }

    public void setInitials(String value)
    {
        this.initials = value;
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
    public String getLimsid()
    {
        return Link.limsIdFromUri(uri);
    }

    @Override
    public void setLimsid(String id)
    {
        // Does nothing.
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(50);
        sb.append(getLimsid()).append(' ').append(firstName).append(' ').append(lastName);
        return sb.toString();
    }
}
