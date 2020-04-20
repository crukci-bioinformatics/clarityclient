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

package com.genologics.ri.process;

import static com.genologics.ri.Namespaces.PROCESS_NAMESPACE;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
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
    private final static QName _ProcessProtocolName_QNAME = new QName("", "protocol-name");
    private final static QName _ProcessTechnician_QNAME = new QName("", "technician");
    private final static QName _ProcessInstrument_QNAME = new QName("", "instrument");
    private final static QName _ProcessType_QNAME = new QName("", "type");
    private final static QName _ProcessInputOutputMap_QNAME = new QName("", "input-output-map");
    private final static QName _ProcessProcessParameter_QNAME = new QName("", "process-parameter");
    private final static QName _ProcessDateRun_QNAME = new QName("", "date-run");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.process
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GenologicsProcess }
     *
     */
    public GenologicsProcess createProcess() {
        return new GenologicsProcess();
    }

    /**
     * Create an instance of {@link InstrumentLink }
     *
     */
    public InstrumentLink createInstrument() {
        return new InstrumentLink();
    }

    /**
     * Create an instance of {@link ProcessTypeLink }
     *
     */
    public ProcessTypeLink createProcessType() {
        return new ProcessTypeLink();
    }

    /**
     * Create an instance of {@link Parameter }
     *
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link GenologicsProcesses }
     *
     */
    public GenologicsProcesses createProcesses() {
        return new GenologicsProcesses();
    }

    /**
     * Create an instance of {@link ArtifactLink }
     *
     */
    public ArtifactLink createArtifact() {
        return new ArtifactLink();
    }

    /**
     * Create an instance of {@link ProcessLink }
     *
     */
    public ProcessLink createProcessLink() {
        return new ProcessLink();
    }

    /**
     * Create an instance of {@link InputOutputMap }
     *
     */
    public InputOutputMap createInputOutputMap() {
        return new InputOutputMap();
    }

    /**
     * Create an instance of {@link ParentProcessLink }
     *
     */
    public ParentProcessLink createParentProcess() {
        return new ParentProcessLink();
    }

    /**
     * Create an instance of {@link Technician }
     *
     */
    public Technician createTechnician() {
        return new Technician();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenologicsProcesses }{@code >}}
     *
     */
    @XmlElementDecl(namespace = PROCESS_NAMESPACE, name = "processes")
    public JAXBElement<GenologicsProcesses> createProcesses(GenologicsProcesses value) {
        return new JAXBElement<GenologicsProcesses>(_Processes_QNAME, GenologicsProcesses.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenologicsProcess }{@code >}}
     *
     */
    @XmlElementDecl(namespace = PROCESS_NAMESPACE, name = "process")
    public JAXBElement<GenologicsProcess> createProcess(GenologicsProcess value) {
        return new JAXBElement<GenologicsProcess>(_Process_QNAME, GenologicsProcess.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "protocol-name", scope = GenologicsProcess.class)
    public JAXBElement<String> createProcessProtocolName(String value) {
        return new JAXBElement<String>(_ProcessProtocolName_QNAME, String.class, GenologicsProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Technician }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "technician", scope = GenologicsProcess.class)
    public JAXBElement<Technician> createProcessTechnician(Technician value) {
        return new JAXBElement<Technician>(_ProcessTechnician_QNAME, Technician.class, GenologicsProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InstrumentLink }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "instrument", scope = GenologicsProcess.class)
    public JAXBElement<InstrumentLink> createProcessInstrument(InstrumentLink value) {
        return new JAXBElement<InstrumentLink>(_ProcessInstrument_QNAME, InstrumentLink.class, GenologicsProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessTypeLink }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "type", scope = GenologicsProcess.class)
    public JAXBElement<ProcessTypeLink> createProcessType(ProcessTypeLink value) {
        return new JAXBElement<ProcessTypeLink>(_ProcessType_QNAME, ProcessTypeLink.class, GenologicsProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InputOutputMap }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "input-output-map", scope = GenologicsProcess.class)
    public JAXBElement<InputOutputMap> createProcessInputOutputMap(InputOutputMap value) {
        return new JAXBElement<InputOutputMap>(_ProcessInputOutputMap_QNAME, InputOutputMap.class, GenologicsProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Parameter }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "process-parameter", scope = GenologicsProcess.class)
    public JAXBElement<Parameter> createProcessProcessParameter(Parameter value) {
        return new JAXBElement<Parameter>(_ProcessProcessParameter_QNAME, Parameter.class, GenologicsProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "date-run", scope = GenologicsProcess.class)
    public JAXBElement<String> createProcessDateRun(String value) {
        return new JAXBElement<String>(_ProcessDateRun_QNAME, String.class, GenologicsProcess.class, value);
    }

}
