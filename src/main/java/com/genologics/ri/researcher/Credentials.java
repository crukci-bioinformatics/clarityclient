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
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * The credentials element contains information about a researcher's access to
 * the client software interface. It is used only when a researcher is given
 * access to the system. It is not used when a researcher's information is being
 * stored for contact purposes only.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "credentials", propOrder = { "username", "password", "accountLocked", "roles" })
public class Credentials implements Serializable
{
    private static final long serialVersionUID = -2712902822454057343L;

    protected String username;

    protected String password;

    @XmlElement(name = "account-locked")
    protected Boolean accountLocked;

    @XmlElement(name = "role")
    protected List<Role> roles;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String value)
    {
        this.username = value;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String value)
    {
        this.password = value;
    }

    public Boolean isAccountLocked()
    {
        return accountLocked;
    }

    public void setAccountLocked(Boolean value)
    {
        this.accountLocked = value;
    }

    /**
     * The type of system access assigned to the researcher's user account. <br/>
     * Always returns with GET: Yes <br/>
     * Creatable with POST: Yes <br/>
     * Required for POST: Yes <br/>
     * Updatable with PUT: Yes <br/>
     * Required for PUT: No, but existing roles must be included or the system
     * will remove the roles not present
     */
    public List<Role> getRoles()
    {
        if (roles == null)
        {
            roles = new ArrayList<Role>();
        }
        return this.roles;
    }

    public Role addRole(Role role)
    {
        getRoles().add(role);
        return role;
    }

    public Role addRole(String roleName)
    {
        Role role = new Role(roleName);
        getRoles().add(role);
        return role;
    }
}
