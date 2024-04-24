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

package com.genologics.ri.role;

import static com.genologics.ri.Namespaces.ROLE_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.role package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 * @since 2.19
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Roles_QNAME = new QName(ROLE_NAMESPACE, "roles");
    private final static QName _Role_QNAME = new QName(ROLE_NAMESPACE, "role");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.role
     *
     */
    public ObjectFactory() {
    }

    public Role createRole() {
        return new Role();
    }

    public ResearcherLink createResearcherLink() {
        return new ResearcherLink();
    }

    public RoleLink createRoleLink() {
        return new RoleLink();
    }

    public PermissionLink createPermissionLink() {
        return new PermissionLink();
    }

    public Roles createRoles() {
        return new Roles();
    }

    @XmlElementDecl(namespace = ROLE_NAMESPACE, name = "roles")
    public JAXBElement<Roles> createRoles(Roles value) {
        return new JAXBElement<Roles>(_Roles_QNAME, Roles.class, null, value);
    }

    @XmlElementDecl(namespace = ROLE_NAMESPACE, name = "role")
    public JAXBElement<Role> createRole(Role value) {
        return new JAXBElement<Role>(_Role_QNAME, Role.class, null, value);
    }
}
