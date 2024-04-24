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


package com.genologics.ri.step;

import static com.genologics.ri.Namespaces.STEP_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
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

    public StepConfiguration createStepConfiguration() {
        return new StepConfiguration();
    }

    public Placements createPlacements() {
        return new Placements();
    }

    public EscalationReview createEscalationReview() {
        return new EscalationReview();
    }

    public Escalation createEscalation() {
        return new Escalation();
    }

    public Actions createActions() {
        return new Actions();
    }

    public NextAction createNextAction() {
        return new NextAction();
    }

    public AutomaticNextStepLink createAutomaticNextStepLink() {
        return new AutomaticNextStepLink();
    }

    public Input createInput() {
        return new Input();
    }

    public ProgramStatus createProgramStatus() {
        return new ProgramStatus();
    }

    public StepDetails createDetails() {
        return new StepDetails();
    }

    public ReagentsLink createReagentsLink() {
        return new ReagentsLink();
    }

    public ActionsLink createActionsLink() {
        return new ActionsLink();
    }

    public OutputPlacement createOutputPlacement() {
        return new OutputPlacement();
    }

    public StepCreation createStepCreation() {
        return new StepCreation();
    }

    public ContainerLink createContainer() {
        return new ContainerLink();
    }

    public StepSetup createSetup() {
        return new StepSetup();
    }

    public ReagentLots createReagentsLots() {
        return new ReagentLots();
    }

    public InstrumentLink createInstrument() {
        return new InstrumentLink();
    }

    public PlacementsLink createPlacementsLink() {
        return new PlacementsLink();
    }

    public ArtifactLink createArtifact() {
        return new ArtifactLink();
    }

    public ReagentLotsLink createReagentLotsLink() {
        return new ReagentLotsLink();
    }

    public UserLink createUser() {
        return new UserLink();
    }

    public Output createOutput() {
        return new Output();
    }

    public InputOutputMap createInputOutputMap() {
        return new InputOutputMap();
    }

    public EscalatedArtifact createEscalatedArtifact() {
        return new EscalatedArtifact();
    }

    public ReagentLotLink createReagentLotLink() {
        return new ReagentLotLink();
    }

    public ReagentLabel createReagentLabel() {
        return new ReagentLabel();
    }

    public AvailableProgram createAvailableProgram() {
        return new AvailableProgram();
    }

    public com.genologics.ri.step.PooledInputs createPooledInputs() {
        return new com.genologics.ri.step.PooledInputs();
    }

    public StepSetupLink createSetupLink() {
        return new StepSetupLink();
    }

    public ProcessStep createStep() {
        return new ProcessStep();
    }

    public Reagents createReagents() {
        return new Reagents();
    }

    public Pools createPools() {
        return new Pools();
    }

    public CreationInput createCreationInput() {
        return new CreationInput();
    }

    public ProgramStatusLink createProgramStatusLink() {
        return new ProgramStatusLink();
    }

    public StepDetailsLink createDetailsLink() {
        return new StepDetailsLink();
    }

    public PoolsLink createPoolsLink() {
        return new PoolsLink();
    }

    public EscalationRequest createEscalationRequest() {
        return new EscalationRequest();
    }

    public SharedResultFile createFile() {
        return new SharedResultFile();
    }

    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "step")
    public JAXBElement<ProcessStep> createStep(ProcessStep value) {
        return new JAXBElement<ProcessStep>(_Step_QNAME, ProcessStep.class, null, value);
    }

    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "actions")
    public JAXBElement<Actions> createActions(Actions value) {
        return new JAXBElement<Actions>(_Actions_QNAME, Actions.class, null, value);
    }

    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "reagents")
    public JAXBElement<Reagents> createReagents(Reagents value) {
        return new JAXBElement<Reagents>(_Reagents_QNAME, Reagents.class, null, value);
    }

    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "step-creation")
    public JAXBElement<StepCreation> createStepCreation(StepCreation value) {
        return new JAXBElement<StepCreation>(_StepCreation_QNAME, StepCreation.class, null, value);
    }

    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "placements")
    public JAXBElement<Placements> createPlacements(Placements value) {
        return new JAXBElement<Placements>(_Placements_QNAME, Placements.class, null, value);
    }

    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "setup")
    public JAXBElement<StepSetup> createSetup(StepSetup value) {
        return new JAXBElement<StepSetup>(_Setup_QNAME, StepSetup.class, null, value);
    }

    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "details")
    public JAXBElement<StepDetails> createDetails(StepDetails value) {
        return new JAXBElement<StepDetails>(_Details_QNAME, StepDetails.class, null, value);
    }

    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "pools")
    public JAXBElement<Pools> createPools(Pools value) {
        return new JAXBElement<Pools>(_Pools_QNAME, Pools.class, null, value);
    }

    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "lots")
    public JAXBElement<ReagentLots> createLots(ReagentLots value) {
        return new JAXBElement<ReagentLots>(_Lots_QNAME, ReagentLots.class, null, value);
    }

    @XmlElementDecl(namespace = STEP_NAMESPACE, name = "program-status")
    public JAXBElement<ProgramStatus> createProgramStatus(ProgramStatus value) {
        return new JAXBElement<ProgramStatus>(_ProgramStatus_QNAME, ProgramStatus.class, null, value);
    }
}
