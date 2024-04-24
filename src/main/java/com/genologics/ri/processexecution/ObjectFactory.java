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

package com.genologics.ri.processexecution;

import static com.genologics.ri.Namespaces.EMPTY_NAMESPACE;
import static com.genologics.ri.Namespaces.PROCESS_EXECUTION_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.processexecution package.
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

    private final static QName _Process_QNAME = new QName(PROCESS_EXECUTION_NAMESPACE, "process");
    private final static QName _ProcessTechnician_QNAME = new QName(EMPTY_NAMESPACE, "technician");
    private final static QName _ProcessInstrument_QNAME = new QName(EMPTY_NAMESPACE, "instrument");
    private final static QName _ProcessType_QNAME = new QName(EMPTY_NAMESPACE, "type");
    private final static QName _ProcessInputOutputMap_QNAME = new QName(EMPTY_NAMESPACE, "input-output-map");
    private final static QName _ProcessProcessParameter_QNAME = new QName(EMPTY_NAMESPACE, "process-parameter");
    private final static QName _ProcessDateRun_QNAME = new QName(EMPTY_NAMESPACE, "date-run");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.processexecution
     *
     */
    public ObjectFactory() {
    }

    public ExecutableProcess createProcess() {
        return new ExecutableProcess();
    }

    public Output createOutput() {
        return new Output();
    }

    public Input createInput() {
        return new Input();
    }

    public Technician createTechnician() {
        return new Technician();
    }

    public Parameter createParameter() {
        return new Parameter();
    }

    public ExecutableInputOutputMap createInputOutputMap() {
        return new ExecutableInputOutputMap();
    }

    public InstrumentLink createInstrument() {
        return new InstrumentLink();
    }

    @XmlElementDecl(namespace = PROCESS_EXECUTION_NAMESPACE, name = "process")
    public JAXBElement<ExecutableProcess> createProcess(ExecutableProcess value) {
        return new JAXBElement<ExecutableProcess>(_Process_QNAME, ExecutableProcess.class, null, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "technician", scope = ExecutableProcess.class)
    public JAXBElement<Technician> createProcessTechnician(Technician value) {
        return new JAXBElement<Technician>(_ProcessTechnician_QNAME, Technician.class, ExecutableProcess.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "instrument", scope = ExecutableProcess.class)
    public JAXBElement<InstrumentLink> createProcessInstrument(InstrumentLink value) {
        return new JAXBElement<InstrumentLink>(_ProcessInstrument_QNAME, InstrumentLink.class, ExecutableProcess.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "type", scope = ExecutableProcess.class)
    public JAXBElement<String> createProcessType(String value) {
        return new JAXBElement<String>(_ProcessType_QNAME, String.class, ExecutableProcess.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "input-output-map", scope = ExecutableProcess.class)
    public JAXBElement<ExecutableInputOutputMap> createProcessInputOutputMap(ExecutableInputOutputMap value) {
        return new JAXBElement<ExecutableInputOutputMap>(_ProcessInputOutputMap_QNAME, ExecutableInputOutputMap.class, ExecutableProcess.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "process-parameter", scope = ExecutableProcess.class)
    public JAXBElement<Parameter> createProcessProcessParameter(Parameter value) {
        return new JAXBElement<Parameter>(_ProcessProcessParameter_QNAME, Parameter.class, ExecutableProcess.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "date-run", scope = ExecutableProcess.class)
    public JAXBElement<String> createProcessDateRun(String value) {
        return new JAXBElement<String>(_ProcessDateRun_QNAME, String.class, ExecutableProcess.class, value);
    }
}
