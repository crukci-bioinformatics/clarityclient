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

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;

/**
 *
 * The role element defines a researcher's level of access in the system. You
 * can repeat the element to provide a researcher with access to more than one
 * area of the system. When submitting a PUT request to update a researcher's
 * credentials (any child elements within the credentials element), your XML
 * must include the current roles for the researcher. If you do not include all
 * of the current roles, the system will remove the current data and the
 * researcher will no longer have access to the system.
 * When adding a new role you must provide at least one of: URI, name, or roleName.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "role")
public class Role implements LimsLink<com.genologics.ri.role.Role>, Serializable
{
    public static final String SYSTEM_ADMIN_ROLE = "System Administrator";
    public static final String CLARITY_ROLE = "Researcher";
    public static final String ADMIN_ROLE = "Facility Administrator";
    public static final String LABLINK_ROLE = "Collaborator";

    private static final long serialVersionUID = -4641418115009008515L;

    /**
     * @deprecated Deprecated from version 2.19.
     */
    @XmlAttribute(name = "roleName")
    @Deprecated
    protected String roleName;

    /**
     * @since 2.19
     */
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * @since 2.19
     */
    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public Role()
    {
    }

    public Role(URI uri)
    {
        this.uri = uri;
    }

    public Role(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
    }

    public Role(Linkable<com.genologics.ri.role.Role> link)
    {
        this.uri = link.getUri();
    }

    public Role(com.genologics.ri.role.Role otherRole)
    {
        this.uri = otherRole.getUri();
        this.name = otherRole.getName();
    }

    /**
     * The name of the security role assigned to the researcher.
     * Only used by built-in roles.
     * Acceptable  values are: "systemadministrator", "administrator", "labtech",
     * and "webclient".
     *
     * @return The role name.
     * @deprecated Deprecated from version 2.19.
     */
    @Deprecated
    public String getRoleName()
    {
        return roleName;
    }

    /**
     * Set the role name.
     * @param value The role name.
     * @deprecated Deprecated from version 2.19.
     */
    @Deprecated
    public void setRoleName(String value)
    {
        this.roleName = value;
    }

    /**
     * The user-facing name of the security role assigned to the researcher. Must be unique.
     *
     * @return The role name.
     *
     * @since 2.19
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the role name.
     * @param name The role name.
     * @since 2.19
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the URI to the researcher.
     * @return The URI.
     * @since 2.19
     */
    @Override
    public URI getUri()
    {
        return uri;
    }

    /**
     * Set the URI to the researcher.
     * @param uri The URI.
     * @since 2.19
     */
    @Override
    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    @Override
    public Class<com.genologics.ri.role.Role> getEntityClass()
    {
        return com.genologics.ri.role.Role.class;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
