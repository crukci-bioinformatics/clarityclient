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

package org.cruk.genologics.api.cache;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.springframework.stereotype.Service;

import com.genologics.ri.LimsEntity;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.artifact.Demux;
import com.genologics.ri.automation.Automation;
import com.genologics.ri.container.Container;
import com.genologics.ri.containertype.ContainerType;
import com.genologics.ri.controltype.ControlType;
import com.genologics.ri.file.GenologicsFile;
import com.genologics.ri.instrument.Instrument;
import com.genologics.ri.lab.Lab;
import com.genologics.ri.process.GenologicsProcess;
import com.genologics.ri.processtype.ProcessType;
import com.genologics.ri.project.Project;
import com.genologics.ri.protocolconfiguration.Protocol;
import com.genologics.ri.reagenttype.ReagentType;
import com.genologics.ri.researcher.Researcher;
import com.genologics.ri.sample.Sample;
import com.genologics.ri.stage.Stage;
import com.genologics.ri.stepconfiguration.ProtocolStep;
import com.genologics.ri.workflowconfiguration.Workflow;

/**
 * A concrete implementation of {@code AbstractCacheManagerFactory} suitable
 * for use in most stand alone programs. One might want to create a different
 * configuration in another class for use in a server.
 *
 * @since 2.31
 */
@Service
public class DefaultCacheManagerFactory extends AbstractCacheManagerFactory
{
    /**
     * Constructor.
     */
    public DefaultCacheManagerFactory()
    {
    }

    /**
     * Configure the {@link CacheManager} with suitable caches for the different
     * entities in the Clarity system, then build and initialise it.
     *
     * @return A configured and initialised {@code CacheManager}.
     */
    @Override
    public CacheManager getObject()
    {
        CacheManagerBuilder<CacheManager> b = null;

        b = buildCache(b, LimsEntity.class, 500, 600);
        b = buildCache(b, Artifact.class, 2000, 300);
        b = buildCache(b, Demux.class, 1000, 300);
        b = buildCache(b, Automation.class, 100, 1800);
        b = buildCache(b, Sample.class, 1000, 300);
        b = buildCache(b, Project.class, 100, 600);
        b = buildCache(b, Container.class, 500, 300);
        b = buildCache(b, ContainerType.class, 50, 1800);
        b = buildCache(b, ControlType.class, 50, 1800);
        b = buildCache(b, GenologicsProcess.class, 200, 300);
        b = buildCache(b, GenologicsFile.class, 2000, 300);
        b = buildCache(b, ProcessType.class, 200, 1800);
        b = buildCache(b, Instrument.class, 50, 1800);
        b = buildCache(b, Lab.class, 500, 1800);
        b = buildCache(b, Researcher.class, 100, 1800);
        b = buildCache(b, ReagentType.class, 5000, 1800);
        b = buildCache(b, Workflow.class, 100, 1800);
        b = buildCache(b, Stage.class, 300, 1800);
        b = buildCache(b, Protocol.class, 100, 1800);
        b = buildCache(b, ProtocolStep.class, 300, 1800);

        return b.build(true);
    }
}
