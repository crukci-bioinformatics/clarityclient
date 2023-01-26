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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Link;
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
 * @since 2.31.2
 */
public class ClarityProcessAutomation
{
    /**
     * Minimum time between polls of Clarity to see if the process has moved state (milliseconds).
     */
    private static final int MIN_WAIT = 500;

    /**
     * Logger.
     */
    protected Logger logger = LoggerFactory.getLogger(ClarityProcessAutomation.class);

    /**
     * Clarity API.
     */
    protected ClarityAPI api;

    /**
     * Wait time between polls, in milliseconds.
     */
    private int wait = 4000;

    /**
     * Maximum number of times to poll waiting for a state change before giving up.
     */
    private int waitAttempts = 15;


    /**
     * Constructor.
     */
    public ClarityProcessAutomation()
    {
    }

    /**
     * Constructor.
     *
     * @param api The Clarity API.
     */
    public ClarityProcessAutomation(ClarityAPI api)
    {
        setClarityAPI(api);
    }

    /**
     * Set the API.
     *
     * @param api The Clarity API.
     */
    @Autowired
    public void setClarityAPI(ClarityAPI api)
    {
        this.api = api;
    }

    /**
     * Get the time to wait between polls.
     *
     * @return The wait time, in milliseconds.
     */
    public int getWait()
    {
        return wait;
    }

    /**
     * Set the wait time between polls.
     *
     * @param wait The wait time, in milliseconds.
     *
     * @throws IllegalArgumentException if the wait time is too short (minimum 500ms).
     */
    public void setWait(int wait)
    {
        if (wait < MIN_WAIT)
        {
            throw new IllegalArgumentException("wait can't be negative, and less than " + MIN_WAIT + "ms will thrash the server.");
        }
        this.wait = wait;
    }

    /**
     * Get the number of times to ask for a state change before stopping.
     *
     * @return The number of polls.
     */
    public int getWaitAttempts()
    {
        return waitAttempts;
    }

    /**
     * Set the number of times to ask for a state change before stopping.
     *
     * @param waitAttempts The number of polls.
     *
     * @throws IllegalArgumentException if there is not at least one poll attempt.
     */
    public void setWaitAttempts(int waitAttempts)
    {
        if (waitAttempts < 1)
        {
            throw new IllegalArgumentException("Need at least one attempt when waiting for a state change.");
        }
        this.waitAttempts = waitAttempts;
    }

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
    public ProcessStep beginProcessStep(StepCreation stepCreation)
    {
        return api.beginProcessStep(stepCreation);
    }

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
    public void advanceStep(ProcessStep step) throws InterruptedException
    {
        for (int i = 0; i < waitAttempts; i++)
        {
            try
            {
                // Try to step. If waiting for scripts, this will raise an exception.
                api.advanceProcessStep(step);

                // If no exception, we're done.
                return;
            }
            catch (ClarityException e)
            {
                if (e.getHttpStatus() == HttpStatus.BAD_REQUEST && e.getMessage().startsWith("Cannot advance a step"))
                {
                    Thread.sleep(wait);

                    // Need to reload as the state may have changed.
                    api.reload(step);

                    logger.debug("Step not yet ready. Current state is {}.", step.getCurrentState());
                }
                else
                {
                    throw e;
                }
            }
        }

        int totalTime = wait * waitAttempts / 1000;
        throw new ClarityAutomationException("Process " + step.getLimsid() +
                " did not finish within the allowed " + totalTime + " seconds.");
    }

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
    public void waitUntilProcessInState(ProcessStep step, ProcessState desiredState) throws InterruptedException
    {
        for (int i = 0; i < waitAttempts; i++)
        {
            Thread.sleep(wait);

            api.reload(step);

            if (step.getCurrentState() == desiredState)
            {
                return;
            }

            logger.debug("Step not yet ready. Current state is {}.", step.getCurrentState());
        }

        int totalTime = wait * waitAttempts / 1000;
        throw new ClarityAutomationException("Process " + step.getLimsid() +
                " did not reach the state '" + desiredState + "' within the allowed " + totalTime + " seconds.");
    }

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
    public ProgramStatus startProgram(AvailableProgram program)
    {
        return api.startProgram(program);
    }

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
    public void waitUntilProgramCompletes(AvailableProgram program, ProgramStatus status) throws InterruptedException
    {
        for (int i = 0; i < waitAttempts; i++)
        {
            Thread.sleep(wait);

            api.currentStatus(status);

            switch (status.getStatus())
            {
                case OK:
                case WARNING:
                    return;

                case ERROR:
                    throw new ClarityAutomationException(program.getName() + " has failed: " + status.getMessage());
            }

            logger.debug("Program not yet complete. Current status is {}.", status.getStatus());
        }

        int totalTime = wait * waitAttempts / 1000;
        throw new ClarityAutomationException("Program " + program.getName() + " for process " +
                Link.limsIdFromUri(status.getStep().getUri()) + " did not complete within the allowed " + totalTime + " seconds.");
    }

    /**
     * Find a work flow by name.
     *
     * @param name The name of the work flow.
     *
     * @return The Workflow object.
     *
     * @throws ClarityAutomationException if there is no such work flow.
     */
    public Workflow findWorkflow(String name)
    {
        for (LimsLink<Workflow> link : api.listAll(Workflow.class))
        {
            com.genologics.ri.workflowconfiguration.WorkflowLink wfl = (com.genologics.ri.workflowconfiguration.WorkflowLink)link;
            if (name.equals(wfl.getName()))
            {
                return api.load(wfl);
            }
        }
        throw new ClarityAutomationException("There is no workflow " + name);
    }

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
    public Stage findStage(Workflow workflow, String name)
    {
        for (com.genologics.ri.workflowconfiguration.StageLink s : workflow.getStages())
        {
            if (name.equals(s.getName()))
            {
                return api.load(s);
            }
        }
        throw new ClarityAutomationException("There is no stage/protocol step " + name + " in workflow " + workflow.getName());
    }

    /**
     * Find a protocol by name.
     *
     * @param name The name of the protocol.
     *
     * @return The Protocol object.
     *
     * @throws ClarityAutomationException if there is no such protocol.
     */
    public Protocol findProtocol(String name)
    {
        for (LimsLink<Protocol> link : api.listAll(Protocol.class))
        {
            com.genologics.ri.protocolconfiguration.ProtocolLink pfl = (com.genologics.ri.protocolconfiguration.ProtocolLink)link;
            if (name.equals(pfl.getName()))
            {
                return api.load(pfl);
            }
        }
        throw new ClarityAutomationException("There is no protocol " + name);
    }

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
    public ProtocolStep findStep(Protocol protocol, String name)
    {
        for (ProtocolStep s : protocol.getSteps())
        {
            if (name.equals(s.getName()))
            {
                return s;
            }
        }
        throw new ClarityAutomationException("There is no step " + name + " in protocol " + protocol.getName());
    }
}
