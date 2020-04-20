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

package com.genologics.ri.processtemplate;

import static com.genologics.ri.Namespaces.PROCESS_TEMPLATE_NAMESPACE;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.processtemplate package.
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

    private final static QName _ProcessTemplates_QNAME = new QName(PROCESS_TEMPLATE_NAMESPACE, "process-templates");
    private final static QName _ProcessTemplate_QNAME = new QName(PROCESS_TEMPLATE_NAMESPACE, "process-template");
    private final static QName _ProcessTemplateTechnician_QNAME = new QName("", "technician");
    private final static QName _ProcessTemplateName_QNAME = new QName("", "name");
    private final static QName _ProcessTemplateIsDefault_QNAME = new QName("", "is-default");
    private final static QName _ProcessTemplateInstrument_QNAME = new QName("", "instrument");
    private final static QName _ProcessTemplateType_QNAME = new QName("", "type");
    private final static QName _ProcessTemplateProcessParameter_QNAME = new QName("", "process-parameter");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.processtemplate
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProcessTemplateLink }
     *
     */
    public ProcessTemplateLink createProcessTemplateLink() {
        return new ProcessTemplateLink();
    }

    /**
     * Create an instance of {@link Parameter }
     *
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link ProcessTemplate }
     *
     */
    public ProcessTemplate createProcessTemplate() {
        return new ProcessTemplate();
    }

    /**
     * Create an instance of {@link ProcessTypeLink }
     *
     */
    public ProcessTypeLink createProcessType() {
        return new ProcessTypeLink();
    }

    /**
     * Create an instance of {@link ProcessTemplates }
     *
     */
    public ProcessTemplates createProcessTemplates() {
        return new ProcessTemplates();
    }

    /**
     * Create an instance of {@link InstrumentLink }
     *
     */
    public InstrumentLink createInstrument() {
        return new InstrumentLink();
    }

    /**
     * Create an instance of {@link Technician }
     *
     */
    public Technician createTechnician() {
        return new Technician();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessTemplates }{@code >}}
     *
     */
    @XmlElementDecl(namespace = PROCESS_TEMPLATE_NAMESPACE, name = "process-templates")
    public JAXBElement<ProcessTemplates> createProcessTemplates(ProcessTemplates value) {
        return new JAXBElement<ProcessTemplates>(_ProcessTemplates_QNAME, ProcessTemplates.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessTemplate }{@code >}}
     *
     */
    @XmlElementDecl(namespace = PROCESS_TEMPLATE_NAMESPACE, name = "process-template")
    public JAXBElement<ProcessTemplate> createProcessTemplate(ProcessTemplate value) {
        return new JAXBElement<ProcessTemplate>(_ProcessTemplate_QNAME, ProcessTemplate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Technician }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "technician", scope = ProcessTemplate.class)
    public JAXBElement<Technician> createProcessTemplateTechnician(Technician value) {
        return new JAXBElement<Technician>(_ProcessTemplateTechnician_QNAME, Technician.class, ProcessTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "name", scope = ProcessTemplate.class)
    public JAXBElement<String> createProcessTemplateName(String value) {
        return new JAXBElement<String>(_ProcessTemplateName_QNAME, String.class, ProcessTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "is-default", scope = ProcessTemplate.class)
    public JAXBElement<Boolean> createProcessTemplateIsDefault(Boolean value) {
        return new JAXBElement<Boolean>(_ProcessTemplateIsDefault_QNAME, Boolean.class, ProcessTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InstrumentLink }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "instrument", scope = ProcessTemplate.class)
    public JAXBElement<InstrumentLink> createProcessTemplateInstrument(InstrumentLink value) {
        return new JAXBElement<InstrumentLink>(_ProcessTemplateInstrument_QNAME, InstrumentLink.class, ProcessTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessTypeLink }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "type", scope = ProcessTemplate.class)
    public JAXBElement<ProcessTypeLink> createProcessTemplateType(ProcessTypeLink value) {
        return new JAXBElement<ProcessTypeLink>(_ProcessTemplateType_QNAME, ProcessTypeLink.class, ProcessTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Parameter }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "process-parameter", scope = ProcessTemplate.class)
    public JAXBElement<Parameter> createProcessTemplateProcessParameter(Parameter value) {
        return new JAXBElement<Parameter>(_ProcessTemplateProcessParameter_QNAME, Parameter.class, ProcessTemplate.class, value);
    }

}
