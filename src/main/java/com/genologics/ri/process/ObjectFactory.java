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

package com.genologics.ri.process;

import static com.genologics.ri.Namespaces.EMPTY_NAMESPACE;
import static com.genologics.ri.Namespaces.PROCESS_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.process package.
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

    private final static QName _Processes_QNAME = new QName(PROCESS_NAMESPACE, "processes");
    private final static QName _Process_QNAME = new QName(PROCESS_NAMESPACE, "process");
    private final static QName _ProcessProtocolName_QNAME = new QName(EMPTY_NAMESPACE, "protocol-name");
    private final static QName _ProcessTechnician_QNAME = new QName(EMPTY_NAMESPACE, "technician");
    private final static QName _ProcessInstrument_QNAME = new QName(EMPTY_NAMESPACE, "instrument");
    private final static QName _ProcessType_QNAME = new QName(EMPTY_NAMESPACE, "type");
    private final static QName _ProcessInputOutputMap_QNAME = new QName(EMPTY_NAMESPACE, "input-output-map");
    private final static QName _ProcessProcessParameter_QNAME = new QName(EMPTY_NAMESPACE, "process-parameter");
    private final static QName _ProcessDateRun_QNAME = new QName(EMPTY_NAMESPACE, "date-run");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.process
     *
     */
    public ObjectFactory() {
    }

    public ClarityProcess createProcess() {
        return new ClarityProcess();
    }

    public InstrumentLink createInstrument() {
        return new InstrumentLink();
    }

    public ProcessTypeLink createProcessType() {
        return new ProcessTypeLink();
    }

    public Parameter createParameter() {
        return new Parameter();
    }

    public ClarityProcesses createProcesses() {
        return new ClarityProcesses();
    }

    public ArtifactLink createArtifact() {
        return new ArtifactLink();
    }

    public ProcessLink createProcessLink() {
        return new ProcessLink();
    }

    public InputOutputMap createInputOutputMap() {
        return new InputOutputMap();
    }

    public ParentProcessLink createParentProcess() {
        return new ParentProcessLink();
    }

    public Technician createTechnician() {
        return new Technician();
    }

    @XmlElementDecl(namespace = PROCESS_NAMESPACE, name = "processes")
    public JAXBElement<ClarityProcesses> createProcesses(ClarityProcesses value) {
        return new JAXBElement<ClarityProcesses>(_Processes_QNAME, ClarityProcesses.class, null, value);
    }

    @XmlElementDecl(namespace = PROCESS_NAMESPACE, name = "process")
    public JAXBElement<ClarityProcess> createProcess(ClarityProcess value) {
        return new JAXBElement<ClarityProcess>(_Process_QNAME, ClarityProcess.class, null, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "protocol-name", scope = ClarityProcess.class)
    public JAXBElement<String> createProcessProtocolName(String value) {
        return new JAXBElement<String>(_ProcessProtocolName_QNAME, String.class, ClarityProcess.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "technician", scope = ClarityProcess.class)
    public JAXBElement<Technician> createProcessTechnician(Technician value) {
        return new JAXBElement<Technician>(_ProcessTechnician_QNAME, Technician.class, ClarityProcess.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "instrument", scope = ClarityProcess.class)
    public JAXBElement<InstrumentLink> createProcessInstrument(InstrumentLink value) {
        return new JAXBElement<InstrumentLink>(_ProcessInstrument_QNAME, InstrumentLink.class, ClarityProcess.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "type", scope = ClarityProcess.class)
    public JAXBElement<ProcessTypeLink> createProcessType(ProcessTypeLink value) {
        return new JAXBElement<ProcessTypeLink>(_ProcessType_QNAME, ProcessTypeLink.class, ClarityProcess.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "input-output-map", scope = ClarityProcess.class)
    public JAXBElement<InputOutputMap> createProcessInputOutputMap(InputOutputMap value) {
        return new JAXBElement<InputOutputMap>(_ProcessInputOutputMap_QNAME, InputOutputMap.class, ClarityProcess.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "process-parameter", scope = ClarityProcess.class)
    public JAXBElement<Parameter> createProcessProcessParameter(Parameter value) {
        return new JAXBElement<Parameter>(_ProcessProcessParameter_QNAME, Parameter.class, ClarityProcess.class, value);
    }

    @XmlElementDecl(namespace = EMPTY_NAMESPACE, name = "date-run", scope = ClarityProcess.class)
    public JAXBElement<String> createProcessDateRun(String value) {
        return new JAXBElement<String>(_ProcessDateRun_QNAME, String.class, ClarityProcess.class, value);
    }
}
