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

package com.genologics.ri.processtemplate;

import static com.genologics.ri.Namespaces.EMPTY_NAMESPACE;
import static com.genologics.ri.Namespaces.PROCESS_TEMPLATE_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
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
    private final static QName _ProcessTemplateTechnician_QNAME = new QName(EMPTY_NAMESPACE, "technician");
    private final static QName _ProcessTemplateName_QNAME = new QName(EMPTY_NAMESPACE, "name");
    private final static QName _ProcessTemplateIsDefault_QNAME = new QName(EMPTY_NAMESPACE, "is-default");
    private final static QName _ProcessTemplateInstrument_QNAME = new QName(EMPTY_NAMESPACE, "instrument");
    private final static QName _ProcessTemplateType_QNAME = new QName(EMPTY_NAMESPACE, "type");
    private final static QName _ProcessTemplateProcessParameter_QNAME = new QName(EMPTY_NAMESPACE, "process-parameter");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.processtemplate
     *
     */
    public ObjectFactory() {
    }

    public ProcessTemplateLink createProcessTemplateLink() {
        return new ProcessTemplateLink();
    }

    public Parameter createParameter() {
        return new Parameter();
    }

    public ProcessTemplate createProcessTemplate() {
        return new ProcessTemplate();
    }

    public ProcessTypeLink createProcessType() {
        return new ProcessTypeLink();
    }

    public ProcessTemplates createProcessTemplates() {
        return new ProcessTemplates();
    }

    public InstrumentLink createInstrument() {
        return new InstrumentLink();
    }

    public Technician createTechnician() {
        return new Technician();
    }

    @XmlElementDecl(namespace = PROCESS_TEMPLATE_NAMESPACE, name = "process-templates")
    public JAXBElement<ProcessTemplates> createProcessTemplates(ProcessTemplates value) {
        return new JAXBElement<ProcessTemplates>(_ProcessTemplates_QNAME, ProcessTemplates.class, null, value);
    }

    @XmlElementDecl(namespace = PROCESS_TEMPLATE_NAMESPACE, name = "process-template")
    public JAXBElement<ProcessTemplate> createProcessTemplate(ProcessTemplate value) {
        return new JAXBElement<ProcessTemplate>(_ProcessTemplate_QNAME, ProcessTemplate.class, null, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "technician", scope = ProcessTemplate.class)
    public JAXBElement<Technician> createProcessTemplateTechnician(Technician value) {
        return new JAXBElement<Technician>(_ProcessTemplateTechnician_QNAME, Technician.class, ProcessTemplate.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "name", scope = ProcessTemplate.class)
    public JAXBElement<String> createProcessTemplateName(String value) {
        return new JAXBElement<String>(_ProcessTemplateName_QNAME, String.class, ProcessTemplate.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "is-default", scope = ProcessTemplate.class)
    public JAXBElement<Boolean> createProcessTemplateIsDefault(Boolean value) {
        return new JAXBElement<Boolean>(_ProcessTemplateIsDefault_QNAME, Boolean.class, ProcessTemplate.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "instrument", scope = ProcessTemplate.class)
    public JAXBElement<InstrumentLink> createProcessTemplateInstrument(InstrumentLink value) {
        return new JAXBElement<InstrumentLink>(_ProcessTemplateInstrument_QNAME, InstrumentLink.class, ProcessTemplate.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "type", scope = ProcessTemplate.class)
    public JAXBElement<ProcessTypeLink> createProcessTemplateType(ProcessTypeLink value) {
        return new JAXBElement<ProcessTypeLink>(_ProcessTemplateType_QNAME, ProcessTypeLink.class, ProcessTemplate.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "process-parameter", scope = ProcessTemplate.class)
    public JAXBElement<Parameter> createProcessTemplateProcessParameter(Parameter value) {
        return new JAXBElement<Parameter>(_ProcessTemplateProcessParameter_QNAME, Parameter.class, ProcessTemplate.class, value);
    }
}
