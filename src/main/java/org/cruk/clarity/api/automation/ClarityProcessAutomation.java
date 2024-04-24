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

package org.cruk.clarity.api.automation;

import org.cruk.clarity.api.ClarityAPI;
import org.cruk.clarity.api.ClarityException;

import com.genologics.ri.protocolconfiguration.Protocol;
import com.genologics.ri.stage.Stage;
import com.genologics.ri.step.AvailableProgram;
import com.genologics.ri.step.ProcessState;
import com.genologics.ri.step.ProcessStep;
import com.genologics.ri.step.ProgramStatus;
import com.genologics.ri.step.StepCreation;
import com.genologics.ri.stepconfiguration.ProtocolStep;
import com.genologics.ri.workflowconfiguration.Workflow;

/**
 * Helper for moving Clarity processes through their stages
 * using the "click through" newer methods rather than the API's
 * {@code ExecutableProcess} end point.
 *
 * @since 2.31.4
 */
public interface ClarityProcessAutomation
{
    /**
     * Get the time to wait between polls.
     *
     * @return The wait time, in milliseconds.
     */
    int getWait();

    /**
     * Set the wait time between polls.
     *
     * @param wait The wait time, in milliseconds.
     *
     * @throws IllegalArgumentException if the wait time is too short (minimum 500ms).
     */
    void setWait(int wait);

    /**
     * Get the number of times to ask for a state change before stopping.
     *
     * @return The number of polls.
     */
    int getWaitAttempts();

    /**
     * Set the number of times to ask for a state change before stopping.
     *
     * @param waitAttempts The number of polls.
     *
     * @throws IllegalArgumentException if there is not at least one poll attempt.
     */
    void setWaitAttempts(int waitAttempts);

    /**
     * Start a protocol step as if running through Clarity. This is a call through to
     * the same method on the Java client API, provided here to allow the whole process
     * to be run through this utility.
     *
     * @param stepCreation The step creation object. See Genologics' API documentation.
     *
     * @return The {@code ProcessStep} object for the step started.
     *
     * @throws IllegalArgumentException if {@code stepCreation} is null.
     *
     * @see ClarityAPI#beginProcessStep(StepCreation)
     *
     * @see <a href="https://d10e8rzir0haj8.cloudfront.net/6.0/rest.version.steps.html#POST">Clarity API documentation</a>
     *
     * @see <a href="https://genologics.zendesk.com/entries/68573603-Starting-a-Protocol-Step-via-the-API">Starting
     * a Protocol Step via the API</a>
     */
    ProcessStep beginProcessStep(StepCreation stepCreation);

    /**
     * Move a process step on to the next stage. Tries this a number of times,
     * testing and waiting for that change to complete.
     *
     * @param step The step to advance.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     * for the state to change.
     *
     * @throws ClarityException if there is a problem from Clarity.
     *
     * @see <a href="https://d10e8rzir0haj8.cloudfront.net/6.0/rest.version.steps.limsid.advance.html">Clarity API documentation</a>
     *
     * @see <a href="https://d10e8rzir0haj8.cloudfront.net/6.0/rest.version.steps.limsid.actions.html">Actions documentation</a>
     *
     * @see <a href="https://genologics.zendesk.com/entries/69596247-Advancing-Completing-a-Protocol-Step-via-the-API">Advancing
     * and Completing a Step via the API</a>
     */
    void advanceStep(ProcessStep step) throws InterruptedException;

    /**
     * Wait until a process has reached a certain state. Tries this a number of times,
     * testing and waiting for the state to reach that asked for.
     *
     * @param step The step to advance.
     * @param desiredState The state we're waiting for.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     * for the state to change.
     *
     * @throws ClarityException if there is a problem from Clarity.
     *
     * @throws ClarityAutomationException if the process didn't reach the required
     * state before the time and number of polling attempts ran out.
     */
    void waitUntilProcessInState(ProcessStep step, ProcessState desiredState) throws InterruptedException;

    /**
     * Start execution of an EPP as part of a process step moving through Clarity.
     * This is a call through to the same method on the Java client API, provide
     * here to allow the whole program to be run through this utility.
     *
     * @param program The program, as listed in the {@code ProcessStep} object.
     *
     * @return An updated program status structure.
     *
     * @see <a href="https://d10e8rzir0haj8.cloudfront.net/6.0/rest.version.steps.limsid.trigger.programid.html">Clarity API Documentation</a>
     */
    ProgramStatus startProgram(AvailableProgram program);

    /**
     * Wait for an EPP to finish running. Tries this a number of times,
     * testing and waiting for the program to finish.
     *
     * @param program The program that is being run.
     * @param status The program status object. This is updated in place.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     * for the program to end.
     *
     * @throws ClarityException if there is a problem from Clarity.
     *
     * @throws ClarityAutomationException if the EPP has failed.
     */
    void waitUntilProgramCompletes(AvailableProgram program, ProgramStatus status) throws InterruptedException;

    /**
     * Find a work flow by name.
     *
     * @param name The name of the work flow.
     *
     * @return The Workflow object.
     *
     * @throws ClarityAutomationException if there is no such work flow.
     */
    Workflow findWorkflow(String name);

    /**
     * Find a stage in a work flow by name.
     *
     * @param workflow The workflow object.
     * @param name The name of the stage.
     *
     * @return The Stage object.
     *
     * @throws ClarityAutomationException if there is no such stage in the work flow.
     */
    Stage findStage(Workflow workflow, String name);

    /**
     * Find a protocol by name.
     *
     * @param name The name of the protocol.
     *
     * @return The Protocol object.
     *
     * @throws ClarityAutomationException if there is no such protocol.
     */
    Protocol findProtocol(String name);

    /**
     * Find a step in a protocol by name.
     *
     * @param protocol The protocol object.
     * @param name The name of the step.
     *
     * @return The ProtocolStep object.
     *
     * @throws ClarityAutomationException if there is no such step in the protocol.
     */
    ProtocolStep findStep(Protocol protocol, String name);
}
