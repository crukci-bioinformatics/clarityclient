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

package com.genologics.ri.workflowconfiguration;

import static com.genologics.ri.Namespaces.WORKFLOW_CONFIGURATION_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.workflowconfiguration package.
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

    private final static QName _Workflows_QNAME = new QName(WORKFLOW_CONFIGURATION_NAMESPACE, "workflows");
    private final static QName _Workflow_QNAME = new QName(WORKFLOW_CONFIGURATION_NAMESPACE, "workflow");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.workflowconfiguration
     *
     */
    public ObjectFactory() {
    }

    public Workflow createWorkflow() {
        return new Workflow();
    }

    public ProtocolLink createProtocolLink() {
        return new ProtocolLink();
    }

    public WorkflowLink createWorkflowLink() {
        return new WorkflowLink();
    }

    public StageLink createStageLink() {
        return new StageLink();
    }

    public Workflows createWorkflows() {
        return new Workflows();
    }

    @XmlElementDecl(namespace = WORKFLOW_CONFIGURATION_NAMESPACE, name = "workflows")
    public JAXBElement<Workflows> createWorkflows(Workflows value) {
        return new JAXBElement<Workflows>(_Workflows_QNAME, Workflows.class, null, value);
    }

    @XmlElementDecl(namespace = WORKFLOW_CONFIGURATION_NAMESPACE, name = "workflow")
    public JAXBElement<Workflow> createWorkflow(Workflow value) {
        return new JAXBElement<Workflow>(_Workflow_QNAME, Workflow.class, null, value);
    }
}
