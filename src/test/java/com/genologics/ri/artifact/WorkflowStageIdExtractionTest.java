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

package com.genologics.ri.artifact;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class WorkflowStageIdExtractionTest
{
    @Test
    public void testStageGetId() throws URISyntaxException
    {
        WorkflowStage stage = new WorkflowStage();

        assertNull("Got id without URI.", stage.getId());
        assertNull("Got workflow id without URI.", stage.getWorkflowId());

        stage.setUri(new URI("http://limsdev.cri.camres.org:8080/api/v2/configuration/protocols/17/steps/49"));

        assertNull("Got id with incorrect URI.", stage.getId());
        assertNull("Got workflow id with incorrect URI.", stage.getWorkflowId());

        stage.setUri(new URI("http://limsdev.cri.camres.org:8080/api/v2/configuration/workflows/7/stages/86"));

        assertEquals("Stage id not extracted properly", Integer.valueOf(86), stage.getId());
        assertEquals("Workflow id not extracted properly", Integer.valueOf(7), stage.getWorkflowId());
    }
}
