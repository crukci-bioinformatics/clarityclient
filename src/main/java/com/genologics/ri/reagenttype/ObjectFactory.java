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

package com.genologics.ri.reagenttype;

import static com.genologics.ri.Namespaces.REAGENT_TYPE_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.reagenttype package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ReagentType_QNAME = new QName(REAGENT_TYPE_NAMESPACE, "reagent-type");
    private final static QName _ReagentTypes_QNAME = new QName(REAGENT_TYPE_NAMESPACE, "reagent-types");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.reagenttype
     *
     */
    public ObjectFactory() {
    }

    public ReagentTypes createReagentTypes() {
        return new ReagentTypes();
    }

    public SpecialType createSpecialType() {
        return new SpecialType();
    }

    public Attribute createAttribute() {
        return new Attribute();
    }

    public ReagentTypeLink createReagentTypeLink() {
        return new ReagentTypeLink();
    }

    public ReagentType createReagentType() {
        return new ReagentType();
    }

    @XmlElementDecl(namespace = REAGENT_TYPE_NAMESPACE, name = "reagent-type")
    public JAXBElement<ReagentType> createReagentType(ReagentType value) {
        return new JAXBElement<ReagentType>(_ReagentType_QNAME, ReagentType.class, null, value);
    }

    @XmlElementDecl(namespace = REAGENT_TYPE_NAMESPACE, name = "reagent-types")
    public JAXBElement<ReagentTypes> createReagentTypes(ReagentTypes value) {
        return new JAXBElement<ReagentTypes>(_ReagentTypes_QNAME, ReagentTypes.class, null, value);
    }
}
