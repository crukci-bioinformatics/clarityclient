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

import static com.genologics.ri.Namespaces.RESEARCHER_NAMESPACE;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.researcher package.
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

    private final static QName _Researcher_QNAME = new QName(RESEARCHER_NAMESPACE, "researcher");
    private final static QName _Researchers_QNAME = new QName(RESEARCHER_NAMESPACE, "researchers");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.researcher
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Researcher }
     *
     */
    public Researcher createResearcher() {
        return new Researcher();
    }

    /**
     * Create an instance of {@link Role }
     *
     */
    public Role createRole() {
        return new Role();
    }

    /**
     * Create an instance of {@link Researchers }
     *
     */
    public Researchers createResearchers() {
        return new Researchers();
    }

    /**
     * Create an instance of {@link LabLink }
     *
     */
    public LabLink createLab() {
        return new LabLink();
    }

    /**
     * Create an instance of {@link Credentials }
     *
     */
    public Credentials createCredentials() {
        return new Credentials();
    }

    /**
     * Create an instance of {@link ResearcherLink }
     *
     */
    public ResearcherLink createResearcherLink() {
        return new ResearcherLink();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Researcher }{@code >}}
     *
     */
    @XmlElementDecl(namespace = RESEARCHER_NAMESPACE, name = "researcher")
    public JAXBElement<Researcher> createResearcher(Researcher value) {
        return new JAXBElement<Researcher>(_Researcher_QNAME, Researcher.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Researchers }{@code >}}
     *
     */
    @XmlElementDecl(namespace = RESEARCHER_NAMESPACE, name = "researchers")
    public JAXBElement<Researchers> createResearchers(Researchers value) {
        return new JAXBElement<Researchers>(_Researchers_QNAME, Researchers.class, null, value);
    }

}
