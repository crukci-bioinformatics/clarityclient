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


package com.genologics.ri.stepconfiguration;

import static com.genologics.ri.Namespaces.STEP_CONFIGURATION_NAMESPACE;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.stepconfiguration package.
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

    private final static QName _Step_QNAME = new QName(STEP_CONFIGURATION_NAMESPACE, "step");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.stepconfiguration
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ControlTypeLink }
     *
     */
    public ControlTypeLink createControlTypeLink() {
        return new ControlTypeLink();
    }

    /**
     * Create an instance of {@link StepProperty }
     *
     */
    public StepProperty createStepProperty() {
        return new StepProperty();
    }

    /**
     * Create an instance of {@link EppTrigger }
     *
     */
    public EppTrigger createEppTrigger() {
        return new EppTrigger();
    }

    /**
     * Create an instance of {@link LockableSetting }
     *
     */
    public LockableSetting createLockableSetting() {
        return new LockableSetting();
    }

    /**
     * Create an instance of {@link ProtocolStep }
     *
     */
    public ProtocolStep createProtocolStep() {
        return new ProtocolStep();
    }

    /**
     * Create an instance of {@link ProcessTypeLink }
     *
     */
    public ProcessTypeLink createProcessTypeLink() {
        return new ProcessTypeLink();
    }

    /**
     * Create an instance of {@link ReagentKitLink }
     *
     */
    public ReagentKitLink createReagentKitLink() {
        return new ReagentKitLink();
    }

    /**
     * Create an instance of {@link IceBucketField }
     *
     */
    public IceBucketField createIceBucketField() {
        return new IceBucketField();
    }

    /**
     * Create an instance of {@link SharedResultFile }
     *
     */
    public SharedResultFile createSharedResultFile() {
        return new SharedResultFile();
    }

    /**
     * Create an instance of {@link StepSetup }
     *
     */
    public StepSetup createStepSetup() {
        return new StepSetup();
    }

    /**
     * Create an instance of {@link NextStep }
     *
     */
    public NextStep createNextStep() {
        return new NextStep();
    }

    /**
     * Create an instance of {@link GenericTypeLink }
     *
     */
    public GenericTypeLink createGenericTypeLink() {
        return new GenericTypeLink();
    }

    /**
     * Create an instance of {@link Field }
     *
     */
    public Field createField() {
        return new Field();
    }

    /**
     * Create an instance of {@link QueueField }
     *
     */
    public QueueField createQueueField() {
        return new QueueField();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProtocolStep }{@code >}}
     *
     */
    @XmlElementDecl(namespace = STEP_CONFIGURATION_NAMESPACE, name = "step")
    public JAXBElement<ProtocolStep> createStep(ProtocolStep value) {
        return new JAXBElement<ProtocolStep>(_Step_QNAME, ProtocolStep.class, null, value);
    }

}
