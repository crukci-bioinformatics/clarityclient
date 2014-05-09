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

package com.genologics.ri.reagentlot;

import static com.genologics.ri.Namespaces.REAGENT_LOT_NAMESPACE;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.reagentlot package.
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

    private final static QName _ReagentLots_QNAME = new QName(REAGENT_LOT_NAMESPACE, "reagent-lots");
    private final static QName _ReagentLot_QNAME = new QName(REAGENT_LOT_NAMESPACE, "reagent-lot");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.reagentlot
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReagentLotLink }
     *
     */
    public ReagentLotLink createReagentLotLink() {
        return new ReagentLotLink();
    }

    /**
     * Create an instance of {@link ReagentLot }
     *
     */
    public ReagentLot createReagentLot() {
        return new ReagentLot();
    }

    /**
     * Create an instance of {@link ResearcherLink }
     *
     */
    public ResearcherLink createResearcher() {
        return new ResearcherLink();
    }

    /**
     * Create an instance of {@link ReagentLots }
     *
     */
    public ReagentLots createReagentLots() {
        return new ReagentLots();
    }

    /**
     * Create an instance of {@link ReagentKitLink }
     *
     */
    public ReagentKitLink createReagentKitLink() {
        return new ReagentKitLink();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReagentLots }{@code >}}
     *
     */
    @XmlElementDecl(namespace = REAGENT_LOT_NAMESPACE, name = "reagent-lots")
    public JAXBElement<ReagentLots> createReagentLots(ReagentLots value) {
        return new JAXBElement<ReagentLots>(_ReagentLots_QNAME, ReagentLots.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReagentLot }{@code >}}
     *
     */
    @XmlElementDecl(namespace = REAGENT_LOT_NAMESPACE, name = "reagent-lot")
    public JAXBElement<ReagentLot> createReagentLot(ReagentLot value) {
        return new JAXBElement<ReagentLot>(_ReagentLot_QNAME, ReagentLot.class, null, value);
    }

}
