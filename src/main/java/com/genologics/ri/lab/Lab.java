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

package com.genologics.ri.lab;

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

import com.genologics.ri.Address;
import com.genologics.ri.ExternalId;
import com.genologics.ri.GenologicsEntity;
import com.genologics.ri.Linkable;
import com.genologics.ri.configuration.FieldType;
import com.genologics.ri.userdefined.UDF;
import com.genologics.ri.userdefined.UDT;

/**
 * The detailed representation of a lab.
 */
@GenologicsEntity(uriSection = "labs", creatable = true, updateable = true)
@XmlRootElement(name = "lab")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lab",
         propOrder = { "name", "billingAddress", "shippingAddress", "type", "fields", "externalIds", "website" })
public class Lab implements Linkable<Lab>, Serializable
{
    private static final long serialVersionUID = 8559819171292932068L;

    @XmlElement(name = "name")
    protected String name;

    @XmlElement(name = "billing-address")
    protected Address billingAddress;

    @XmlElement(name = "shipping-address")
    protected Address shippingAddress;

    @XmlElement(name = "type", namespace = UDF_NAMESPACE)
    protected UDT type;

    @XmlElement(name = "field", namespace = UDF_NAMESPACE)
    protected List<UDF> fields;

    @XmlElement(name = "externalid", namespace = ROOT_NAMESPACE)
    protected List<ExternalId> externalIds;

    protected String website;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public Lab()
    {
    }

    public Lab(URI uri)
    {
        this.uri = uri;
    }

    public Lab(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        this.name = value;
    }

    public Address getBillingAddress()
    {
        return billingAddress;
    }

    public void setBillingAddress(Address value)
    {
        this.billingAddress = value;
    }

    public Address getShippingAddress()
    {
        return shippingAddress;
    }

    public void setShippingAddress(Address value)
    {
        this.shippingAddress = value;
    }

    /**
     *
     * The User-Defined Type that is associated with the lab. <br/>
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
     * A User-Defined Field that is associated with the lab. This element is
     * repeated for each UDF associated with the lab. <br/>
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
     * An identifier that allows an external system to retrieve information
     * about the lab <br/>
     * Always returns with GET: No <br/>
     * Creatable with POST: Yes <br/>
     * Required for POST: No <br/>
     * Updatable with PUT: Yes <br/>
     * Required for PUT: No
     */
    public List<ExternalId> getExternalid()
    {
        if (externalIds == null)
        {
            externalIds = new ArrayList<ExternalId>();
        }
        return this.externalIds;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String value)
    {
        this.website = value;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI value)
    {
        this.uri = value;
    }

}
