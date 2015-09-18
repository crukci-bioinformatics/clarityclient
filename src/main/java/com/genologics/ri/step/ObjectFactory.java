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


package com.genologics.ri.step;

import static com.genologics.ri.Namespaces.STEP_NAMESPACE;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.step package.
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

    private final static QName _Step_QNAME = new QName(STEP_NAMESPACE, "step");
    private final static QName _Actions_QNAME = new QName(STEP_NAMESPACE, "actions");
    private final static QName _Reagents_QNAME = new QName(STEP_NAMESPACE, "reagents");
    private final static QName _StepCreation_QNAME = new QName(STEP_NAMESPACE, "step-creation");
    private final static QName _Placements_QNAME = new QName(STEP_NAMESPACE, "placements");
    private final static QName _Setup_QNAME = new QName(STEP_NAMESPACE, "setup");
    private final static QName _Details_QNAME = new QName(STEP_NAMESPACE, "details");
    private final static QName _Pools_QNAME = new QName(STEP_NAMESPACE, "pools");
    private final static QName _Lots_QNAME = new QName(STEP_NAMESPACE, "lots");
    private final static QName _ProgramStatus_QNAME = new QName(STEP_NAMESPACE, "program-status");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.step
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StepConfiguration }
     *
     */
    public StepConfiguration createStepConfiguration() {
        return new StepConfiguration();
    }

    /**
     * Create an instance of {@link Placements }
     *
     */
    public Placements createPlacements() {
        return new Placements();
    }

    /**
     * Create an instance of {@link EscalationReview }
     *
     */
    public EscalationReview createEscalationReview() {
        return new EscalationReview();
    }

    /**
     * Create an instance of {@link Escalation }
     *
     */
    public Escalation createEscalation() {
        return new Escalation();
    }

    /**
     * Create an instance of {@link Actions }
     *
     */
    public Actions createActions() {
        return new Actions();
    }

    /**
     * Create an instance of {@link NextAction }
     *
     */
    public NextAction createNextAction() {
        return new NextAction();
    }

    /**
     * Create an instance of {@link AutomaticNextStepLink }
     *
     */
    public AutomaticNextStepLink createAutomaticNextStepLink() {
        return new AutomaticNextStepLink();
    }

    /**
     * Create an instance of {@link Input }
     *
     */
    public Input createInput() {
        return new Input();
    }

    /**
     * Create an instance of {@link ProgramStatus }
     *
     */
    public ProgramStatus createProgramStatus() {
        return new ProgramStatus();
    }

    /**
     * Create an instance of {@link StepDetails }
     *
     */
    public StepDetails createDetails() {
        return new StepDetails();
    }

    /**
     * Create an instance of {@link ReagentsLink }
     *
     */
    public ReagentsLink createReagentsLink() {
        return new ReagentsLink();
    }

    /**
     * Create an instance of {@link ActionsLink }
     *
     */
    public ActionsLink createActionsLink() {
        return new ActionsLink();
    }

    /**
     * Create an instance of {@link OutputPlacement }
     *
     */
    public OutputPlacement createOutputPlacement() {
        return new OutputPlacement();
    }

    /**
     * Create an instance of {@link StepCreation }
     *
     */
    public StepCreation createStepCreation() {
        return new StepCreation();
    }

    /**
     * Create an instance of {@link ContainerLink }
     *
     */
    public ContainerLink createContainer() {
        return new ContainerLink();
    }

    /**
     * Create an instance of {@link StepSetup }
     *
     */
    public StepSetup createSetup() {
        return new StepSetup();
    }

    /**
     * Create an instance of {@link ReagentLots }
     *
     */
    public ReagentLots createReagentsLots() {
        return new ReagentLots();
    }

    /**
     * Create an instance of {@link InstrumentLink }
     *
     */
    public InstrumentLink createInstrument() {
        return new InstrumentLink();
    }

    /**
     * Create an instance of {@link PlacementsLink }
     *
     */
    public PlacementsLink createPlacementsLink() {
        return new PlacementsLink();
    }

    /**
     * Create an instance of {@link ArtifactLink }
     *
     */
    public ArtifactLink createArtifact() {
        return new ArtifactLink();
    }

    /**
     * Create an instance of {@link ReagentLotsLink }
     *
     */
    public ReagentLotsLink createReagentLotsLink() {
        return new ReagentLotsLink();
    }

    /**
     * Create an instance of {@link UserLink }
     *
     */
    public UserLink createUser() {
        return new UserLink();
    }

    /**
     * Create an instance of {@link Output }
     *
     */
    public Output createOutput() {
        return new Output();
    }

    /**
     * Create an instance of {@link InputOutputMap }
     *
     */
    public InputOutputMap createInputOutputMap() {
        return new InputOutputMap();
    }

    /**
     * Create an instance of {@link EscalatedArtifact }
     *
     */
    public EscalatedArtifact createEscalatedArtifact() {
        return new EscalatedArtifact();
    }

    /**
     * Create an instance of {@link ReagentLotLink }
     *
     */
    public ReagentLotLink createReagentLotLink() {
        return new ReagentLotLink();
    }

    /**
     * Create an instance of {@link ReagentLabel }
     *
     */
    public ReagentLabel createReagentLabel() {
        return new ReagentLabel();
    }

    /**
     * Create an instance of {@link AvailableProgram }
     *
     */
    public AvailableProgram createAvailableProgram() {
        return new AvailableProgram();
    }

    /**
     * Create an instance of {@link com.genologics.ri.step.PooledInputs }
     *
     */
    public com.genologics.ri.step.PooledInputs createPooledInputs() {
        return new com.genologics.ri.step.PooledInputs();
    }

    /**
     * Create an instance of {@link StepSetupLink }
     *
     */
    public StepSetupLink createSetupLink() {
        return new StepSetupLink();
    }

    /**
     * Create an instance of {@link ProcessStep }
     *
     */
    public ProcessStep createStep() {
        return new ProcessStep();
    }

    /**
     * Create an instance of {@link Reagents }
     *
     */
    public Reagents createReagents() {
        return new Reagents();
    }

    /**
     * Create an instance of {@link Pools }
     *
     */
    public Pools createPools() {
        return new Pools();
    }

    /**
     * Create an instance of {@link CreationInput }
     *
     */
    public CreationInput createCreationInput() {
        return new CreationInput();
    }

    /**
     * Create an instance of {@link ProgramStatusLink }
     *
     */
    public ProgramStatusLink createProgramStatusLink() {
        return new ProgramStatusLink();
    }

    /**
     * Create an instance of {@link StepDetailsLink }
     *
     */
    public StepDetailsLink createDetailsLink() {
        return new StepDetailsLink();
    }

    /**
     * Create an instance of {@link PoolsLink }
     *
     */
    public PoolsLink createPoolsLink() {
        return new PoolsLink();
    }

    /**
     * Create an instance of {@link EscalationRequest }
     *
     */
    public EscalationRequest createEscalationRequest() {
        return new EscalationRequest();
    }

    /**
     * Create an instance of {@link SharedResultFile }
     *
     */
    public SharedResultFile createFile() {
        return new SharedResultFile();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessStep }{@code >}}
     *
     */
    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "step")
    public JAXBElement<ProcessStep> createStep(ProcessStep value) {
        return new JAXBElement<ProcessStep>(_Step_QNAME, ProcessStep.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Actions }{@code >}}
     *
     */
    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "actions")
    public JAXBElement<Actions> createActions(Actions value) {
        return new JAXBElement<Actions>(_Actions_QNAME, Actions.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Reagents }{@code >}}
     *
     */
    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "reagents")
    public JAXBElement<Reagents> createReagents(Reagents value) {
        return new JAXBElement<Reagents>(_Reagents_QNAME, Reagents.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StepCreation }{@code >}}
     *
     */
    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "step-creation")
    public JAXBElement<StepCreation> createStepCreation(StepCreation value) {
        return new JAXBElement<StepCreation>(_StepCreation_QNAME, StepCreation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Placements }{@code >}}
     *
     */
    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "placements")
    public JAXBElement<Placements> createPlacements(Placements value) {
        return new JAXBElement<Placements>(_Placements_QNAME, Placements.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StepSetup }{@code >}}
     *
     */
    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "setup")
    public JAXBElement<StepSetup> createSetup(StepSetup value) {
        return new JAXBElement<StepSetup>(_Setup_QNAME, StepSetup.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StepDetails }{@code >}}
     *
     */
    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "details")
    public JAXBElement<StepDetails> createDetails(StepDetails value) {
        return new JAXBElement<StepDetails>(_Details_QNAME, StepDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Pools }{@code >}}
     *
     */
    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "pools")
    public JAXBElement<Pools> createPools(Pools value) {
        return new JAXBElement<Pools>(_Pools_QNAME, Pools.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReagentLots }{@code >}}
     *
     */
    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "lots")
    public JAXBElement<ReagentLots> createLots(ReagentLots value) {
        return new JAXBElement<ReagentLots>(_Lots_QNAME, ReagentLots.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProgramStatus }{@code >}}
     *
     */
    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "program-status")
    public JAXBElement<ProgramStatus> createProgramStatus(ProgramStatus value) {
        return new JAXBElement<ProgramStatus>(_ProgramStatus_QNAME, ProgramStatus.class, null, value);
    }

}
