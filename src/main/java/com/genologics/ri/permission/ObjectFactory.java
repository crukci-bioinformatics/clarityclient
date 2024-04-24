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

package com.genologics.ri.permission;

import static com.genologics.ri.Namespaces.PERMISSION_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.permission package.
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

    private final static QName _Permission_QNAME = new QName(PERMISSION_NAMESPACE, "permission");
    private final static QName _Permissions_QNAME = new QName(PERMISSION_NAMESPACE, "permissions");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.permission
     *
     */
    public ObjectFactory() {
    }

    public Permissions createPermissions() {
        return new Permissions();
    }

    public PermissionLink createPermissionLink() {
        return new PermissionLink();
    }

    public Permission createPermission() {
        return new Permission();
    }

    @XmlElementDecl(namespace = PERMISSION_NAMESPACE, name = "permission")
    public JAXBElement<Permission> createPermission(Permission value) {
        return new JAXBElement<Permission>(_Permission_QNAME, Permission.class, null, value);
    }

    @XmlElementDecl(namespace = PERMISSION_NAMESPACE, name = "permissions")
    public JAXBElement<Permissions> createPermissions(Permissions value) {
        return new JAXBElement<Permissions>(_Permissions_QNAME, Permissions.class, null, value);
    }

}
