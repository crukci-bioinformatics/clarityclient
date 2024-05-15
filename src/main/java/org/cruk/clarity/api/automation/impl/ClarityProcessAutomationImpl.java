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

package org.cruk.clarity.api.automation.impl;

import org.cruk.clarity.api.ClarityAPI;
import org.cruk.clarity.api.ClarityException;
import org.cruk.clarity.api.automation.ClarityAutomationException;
import org.cruk.clarity.api.automation.ClarityProcessAutomation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

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
@Component("clarityProcessAutomation")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ClarityProcessAutomationImpl implements ClarityProcessAutomation
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
    public ClarityProcessAutomationImpl()
    {
    }

    /**
     * Constructor.
     *
     * @param api The Clarity API.
     */
    public ClarityProcessAutomationImpl(ClarityAPI api)
    {
        setClarityAPI(api);
    }

    /**
     * Set the API.
     *
     * @param api The Clarity API.
     */
    @Autowired
    @Qualifier("clarityAPI")
    public void setClarityAPI(ClarityAPI api)
    {
        this.api = api;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWait()
    {
        return wait;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWait(int wait)
    {
        if (wait < MIN_WAIT)
        {
            throw new IllegalArgumentException("wait can't be negative, and less than " + MIN_WAIT + "ms will thrash the server.");
        }
        this.wait = wait;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWaitAttempts()
    {
        return waitAttempts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWaitAttempts(int waitAttempts)
    {
        if (waitAttempts < 1)
        {
            throw new IllegalArgumentException("Need at least one attempt when waiting for a state change.");
        }
        this.waitAttempts = waitAttempts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessStep beginProcessStep(StepCreation stepCreation)
    {
        return api.beginProcessStep(stepCreation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public ProgramStatus startProgram(AvailableProgram program)
    {
        return api.startProgram(program);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("incomplete-switch")
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
