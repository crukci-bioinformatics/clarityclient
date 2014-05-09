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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * The role element defines a researcher's level of access in the system. You
 * can repeat the element to provide a researcher with access to more than one
 * area of the system. When submitting a PUT request to update a researcher's
 * credentials (any child elements within the credentials element), your XML
 * must include the current roles for the researcher. If you do not include all
 * of the current roles, the system will remove the current data and the
 * researcher will no longer have access to the system.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "role")
public class Role implements Serializable
{
    public static final String SYSTEM_ADMIN_ROLE = "systemadministrator";
    public static final String CLARITY_ROLE = "labtech";
    public static final String ADMIN_ROLE = "administrator";
    public static final String LABLINK_ROLE = "webclient";

    private static final long serialVersionUID = 8672841750737751034L;

    @XmlAttribute(name = "roleName")
    protected String roleName;

    public Role()
    {
    }

    public Role(String roleName)
    {
        this.roleName = roleName;
    }

    /**
     * The name of the security role assigned to the researcher. Acceptable
     * values are: "systemadministrator", "administrator", "labtech", and
     * "webclient". <br/>
     * Always returns with GET: Yes <br/>
     * Creatable with POST: Yes <br/>
     * Required for POST: Yes <br/>
     * Updatable with PUT: Yes <br/>
     * Required for PUT: Yes
     */
    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String value)
    {
        this.roleName = value;
    }

}
