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

package com.genologics.ri.processtype;

import static com.genologics.ri.Namespaces.PROCESS_TYPE_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.processtype package.
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

    private final static QName _ProcessTypes_QNAME = new QName(PROCESS_TYPE_NAMESPACE, "process-types");
    private final static QName _ProcessType_QNAME = new QName(PROCESS_TYPE_NAMESPACE, "process-type");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.processtype
     *
     */
    public ObjectFactory() {
    }

    public SharedResultFile createSharedResultFile() {
        return new SharedResultFile();
    }

    public StepProperty createStepProperty() {
        return new StepProperty();
    }

    public Parameter createParameter() {
        return new Parameter();
    }

    public ProcessType createProcessType() {
        return new ProcessType();
    }

    public ControlTypeLink createControlTypeLink() {
        return new ControlTypeLink();
    }

    public ContainerTypeLink createContainerTypeLink() {
        return new ContainerTypeLink();
    }

    public IceBucketField createIceBucketField() {
        return new IceBucketField();
    }

    public ProcessTypes createProcessTypes() {
        return new ProcessTypes();
    }

    public EppTrigger createEppTrigger() {
        return new EppTrigger();
    }

    public StepSetup createStepSetup() {
        return new StepSetup();
    }

    public Field createField() {
        return new Field();
    }

    public TypeDefinition createTypeDefinition() {
        return new TypeDefinition();
    }

    public ProcessTypeLink createProcessTypeLink() {
        return new ProcessTypeLink();
    }

    public QueueField createQueueField() {
        return new QueueField();
    }

    public ProcessOutput createProcessOutput() {
        return new ProcessOutput();
    }

    public ReagentKitLink createReagentKitLink() {
        return new ReagentKitLink();
    }

    public ProcessInput createProcessInput() {
        return new ProcessInput();
    }

    public ProcessTypeAttribute createProcessTypeAttribute() {
        return new ProcessTypeAttribute();
    }

    @XmlElementDecl(namespace = PROCESS_TYPE_NAMESPACE, name = "process-types")
    public JAXBElement<ProcessTypes> createProcessTypes(ProcessTypes value) {
        return new JAXBElement<ProcessTypes>(_ProcessTypes_QNAME, ProcessTypes.class, null, value);
    }

    @XmlElementDecl(namespace = PROCESS_TYPE_NAMESPACE, name = "process-type")
    public JAXBElement<ProcessType> createProcessType(ProcessType value) {
        return new JAXBElement<ProcessType>(_ProcessType_QNAME, ProcessType.class, null, value);
    }
}
