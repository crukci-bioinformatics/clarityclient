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

package com.genologics.ri.processexecution;

import static com.genologics.ri.Namespaces.PROCESS_EXECUTION_NAMESPACE;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
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
    private final static QName _ProcessTechnician_QNAME = new QName("", "technician");
    private final static QName _ProcessInstrument_QNAME = new QName("", "instrument");
    private final static QName _ProcessType_QNAME = new QName("", "type");
    private final static QName _ProcessInputOutputMap_QNAME = new QName("", "input-output-map");
    private final static QName _ProcessProcessParameter_QNAME = new QName("", "process-parameter");
    private final static QName _ProcessDateRun_QNAME = new QName("", "date-run");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.processexecution
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ExecutableProcess }
     *
     */
    public ExecutableProcess createProcess() {
        return new ExecutableProcess();
    }

    /**
     * Create an instance of {@link Output }
     *
     */
    public Output createOutput() {
        return new Output();
    }

    /**
     * Create an instance of {@link Input }
     *
     */
    public Input createInput() {
        return new Input();
    }

    /**
     * Create an instance of {@link Technician }
     *
     */
    public Technician createTechnician() {
        return new Technician();
    }

    /**
     * Create an instance of {@link Parameter }
     *
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link ExecutableInputOutputMap }
     *
     */
    public ExecutableInputOutputMap createInputOutputMap() {
        return new ExecutableInputOutputMap();
    }

    /**
     * Create an instance of {@link InstrumentLink }
     *
     */
    public InstrumentLink createInstrument() {
        return new InstrumentLink();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecutableProcess }{@code >}}
     *
     */
    @XmlElementDecl(namespace = PROCESS_EXECUTION_NAMESPACE, name = "process")
    public JAXBElement<ExecutableProcess> createProcess(ExecutableProcess value) {
        return new JAXBElement<ExecutableProcess>(_Process_QNAME, ExecutableProcess.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Technician }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "technician", scope = ExecutableProcess.class)
    public JAXBElement<Technician> createProcessTechnician(Technician value) {
        return new JAXBElement<Technician>(_ProcessTechnician_QNAME, Technician.class, ExecutableProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InstrumentLink }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "instrument", scope = ExecutableProcess.class)
    public JAXBElement<InstrumentLink> createProcessInstrument(InstrumentLink value) {
        return new JAXBElement<InstrumentLink>(_ProcessInstrument_QNAME, InstrumentLink.class, ExecutableProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "type", scope = ExecutableProcess.class)
    public JAXBElement<String> createProcessType(String value) {
        return new JAXBElement<String>(_ProcessType_QNAME, String.class, ExecutableProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecutableInputOutputMap }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "input-output-map", scope = ExecutableProcess.class)
    public JAXBElement<ExecutableInputOutputMap> createProcessInputOutputMap(ExecutableInputOutputMap value) {
        return new JAXBElement<ExecutableInputOutputMap>(_ProcessInputOutputMap_QNAME, ExecutableInputOutputMap.class, ExecutableProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Parameter }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "process-parameter", scope = ExecutableProcess.class)
    public JAXBElement<Parameter> createProcessProcessParameter(Parameter value) {
        return new JAXBElement<Parameter>(_ProcessProcessParameter_QNAME, Parameter.class, ExecutableProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "date-run", scope = ExecutableProcess.class)
    public JAXBElement<String> createProcessDateRun(String value) {
        return new JAXBElement<String>(_ProcessDateRun_QNAME, String.class, ExecutableProcess.class, value);
    }

}
