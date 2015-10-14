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

package com.genologics.ri.workflowconfiguration;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class WorkflowExtractionTest
{

    @Test
    public void testWorkflowGetId() throws URISyntaxException
    {
        Workflow workflow = new Workflow();

        assertNull("Got id without URI.", workflow.getId());

        workflow.setUri(new URI("http://limsdev.cri.camres.org:8080/api/v2/configuration/protocols/17"));

        assertNull("Got id with incorrect URI.", workflow.getId());

        workflow.setUri(new URI("http://limsdev.cri.camres.org:8080/api/v2/configuration/workflows/7"));

        assertEquals("Workflow id not extracted properly", Integer.valueOf(7), workflow.getId());
    }
}
