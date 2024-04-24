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

package com.genologics.ri.researcher;

import static com.genologics.ri.Namespaces.ROOT_NAMESPACE;
import static com.genologics.ri.Namespaces.UDF_NAMESPACE;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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
import com.genologics.ri.ExternalId;
import com.genologics.ri.LimsEntity;
import com.genologics.ri.Link;
import com.genologics.ri.Linkable;
import com.genologics.ri.configuration.FieldType;
import com.genologics.ri.lab.Lab;
import com.genologics.ri.userdefined.UDF;
import com.genologics.ri.userdefined.UDFHolder;
import com.genologics.ri.userdefined.UDT;

/**
 * The detailed representation of a researcher.
 */
@ClarityEntity(uriSection = "researchers", creatable = true, updateable = true, removable = true)
@XmlRootElement(name = "researcher")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "researcher",
         propOrder = { "firstName", "lastName", "phone", "fax", "email", "lab", "type", "fields",
                       "externalIds", "credentials", "initials" })
public class Researcher implements LimsEntity<Researcher>, UDFHolder, Serializable
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

    /**
     * Convenience method for returning the full name of the researcher.
     *
     * @return The full name.
     *
     * @since 2.31.2
     *
     * @see #makeFullName(String, String)
     */
    public String getFullName()
    {
        return makeFullName(firstName, lastName);
    }

    /**
     * Convenience method for making the full name of the researcher.
     * Present as a static to allow reuse by some of the researcher link classes.
     *
     * @param firstName The researcher first name.
     * @param lastName The researcher last name.
     *
     * @return First name &lt;space&gt; last name. If both names are null,
     * returns null.
     *
     * @since 2.31.2
     */
    public static String makeFullName(String firstName, String lastName)
    {
        if (firstName == null && lastName == null)
        {
            return null;
        }

        StringBuilder name = new StringBuilder(32);
        if (isNotEmpty(firstName))
        {
            name.append(firstName);
        }
        if (isNoneEmpty(firstName, lastName))
        {
            name.append(' ');
        }
        if (isNotEmpty(lastName))
        {
            name.append(lastName);
        }

        return name.toString();
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
