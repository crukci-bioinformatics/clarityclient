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

package com.genologics.ri.artifact;

import static com.genologics.ri.Namespaces.FILE_NAMESPACE;
import static com.genologics.ri.Namespaces.UDF_NAMESPACE;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.LimsEntity;
import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.Linkable;
import com.genologics.ri.Location;
import com.genologics.ri.artifactgroup.ArtifactGroup;
import com.genologics.ri.configuration.FieldType;
import com.genologics.ri.controltype.ControlType;
import com.genologics.ri.file.ClarityFile;
import com.genologics.ri.process.ClarityProcess;
import com.genologics.ri.sample.Sample;
import com.genologics.ri.userdefined.UDF;
import com.genologics.ri.userdefined.UDFHolder;

/**
 *
 * <p>
 * The detailed representation of an Artifact.
 * </p>
 * <p>
 * An Artifact represents the inputs to or outputs from a process. An Artifact
 * is classified by its type (Analyte, ResultFile, etc).
 * </p>
 * <p>
 * Artifacts do not support HTTP POST requests.
 * </p>
 */
@ClarityEntity(uriSection = "artifacts", updateable = true, stateful = true)
@XmlRootElement(name = "artifact")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "artifact",
         propOrder = { "name", "type", "outputType", "parentProcess", "qcFlag", "location",
                       "workingFlag", "samples", "reagentLabels", "controlType", "fields", "file",
                       "artifactGroups", "workflowStages", "demux" })
public class Artifact implements LimsEntity<Artifact>, UDFHolder, Serializable
{
    private static final long serialVersionUID = 4667019853212119178L;

    protected String name;

    protected String type;

    @XmlElement(name = "output-type")
    protected OutputType outputType;

    @XmlElement(name = "parent-process")
    protected ParentProcessLink parentProcess;

    @XmlElement(name = "qc-flag")
    protected QCFlag qcFlag;

    @XmlElement(name = "location")
    protected Location location;

    @XmlElement(name = "working-flag")
    protected Boolean workingFlag;

    @XmlElement(name = "sample")
    protected List<SampleLink> samples;

    @XmlElement(name = "reagent-label")
    protected List<ReagentLabel> reagentLabels;

    @XmlElement(name = "control-type")
    protected ControlTypeLink controlType;

    @XmlElement(name = "field", namespace = UDF_NAMESPACE)
    protected List<UDF> fields;

    @XmlElement(name = "file", namespace = FILE_NAMESPACE)
    protected ClarityFile file;

    @XmlElement(name = "artifact-group")
    protected List<ArtifactGroupLink> artifactGroups;

    /**
     * @since 2.20
     */
    @XmlElementWrapper(name = "workflow-stages")
    @XmlElement(name = "workflow-stage")
    protected List<WorkflowStage> workflowStages;

    /**
     * @since 2.26
     */
    @XmlElement(name = "demux")
    protected DemuxLink demux;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        this.name = value;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String value)
    {
        this.type = value;
    }

    public OutputType getOutputType()
    {
        return outputType;
    }

    public void setOutputType(OutputType value)
    {
        this.outputType = value;
    }

    public ParentProcessLink getParentProcess()
    {
        return parentProcess;
    }

    public ParentProcessLink setParentProcess(LimsEntityLinkable<ClarityProcess> link)
    {
        parentProcess = new ParentProcessLink(link);
        return parentProcess;
    }

    public QCFlag getQCFlag()
    {
        return qcFlag;
    }

    public void setQCFlag(QCFlag value)
    {
        this.qcFlag = value;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location value)
    {
        this.location = value;
    }

    public Boolean isWorkingFlag()
    {
        return workingFlag;
    }

    public void setWorkingFlag(Boolean value)
    {
        this.workingFlag = value;
    }

    public List<SampleLink> getSamples()
    {
        if (samples == null)
        {
            samples = new ArrayList<SampleLink>();
        }
        return samples;
    }

    public void setSamples(Collection<? extends LimsEntityLinkable<Sample>> links)
    {
        getSamples().clear();
        for (LimsEntityLinkable<Sample> link : links)
        {
            samples.add(new SampleLink(link));
        }
    }

    public List<ReagentLabel> getReagentLabels()
    {
        if (reagentLabels == null)
        {
            reagentLabels = new ArrayList<ReagentLabel>();
        }
        return reagentLabels;
    }

    public ReagentLabel addReagentLabel(ReagentLabel label)
    {
        getReagentLabels().add(label);
        return label;
    }

    public ReagentLabel addReagentLabel(String labelName)
    {
        return addReagentLabel(new ReagentLabel(labelName));
    }

    public ControlTypeLink getControlType()
    {
        return controlType;
    }

    public void setControlType(Linkable<ControlType> link)
    {
        this.controlType = link == null ? null : new ControlTypeLink(link);
    }

    @Override
    public List<UDF> getUserDefinedFields()
    {
        if (fields == null)
        {
            fields = new ArrayList<>();
        }
        return this.fields;
    }

    @Deprecated
    public UDF getUserDefinedField(String name)
    {
        return UDF.getUDF(fields, name);
    }

    @Deprecated
    public UDF addUserDefinedField(UDF udf)
    {
        getUserDefinedFields().add(udf);
        return udf;
    }

    @Deprecated
    public UDF addUserDefinedField(String name, FieldType type, String value)
    {
        return addUserDefinedField(new UDF(name, type, value));
    }

    /**
     * File provides a URI linking to the detailed representation of the File
     * associated with the Artifact.
     *
     * @return The file link, if present.
     */
    public ClarityFile getFile()
    {
        return file;
    }

    public void setFile(ClarityFile value)
    {
        this.file = value == null ? null : new ClarityFile(value);
    }

    public List<ArtifactGroupLink> getArtifactGroups()
    {
        if (artifactGroups == null)
        {
            artifactGroups = new ArrayList<ArtifactGroupLink>();
        }
        return artifactGroups;
    }

    public void setArtifactGroups(Collection<? extends Linkable<ArtifactGroup>> links)
    {
        getArtifactGroups().clear();
        for (Linkable<ArtifactGroup> link : links)
        {
            artifactGroups.add(new ArtifactGroupLink(link));
        }
    }

    public ArtifactGroupLink addArtifactGroup(Linkable<ArtifactGroup> link)
    {
        ArtifactGroupLink l = new ArtifactGroupLink(link);
        getArtifactGroups().add(l);
        return l;
    }

    public List<WorkflowStage> getWorkflowStages()
    {
        if (workflowStages == null)
        {
            workflowStages = new ArrayList<WorkflowStage>();
        }
        return workflowStages;
    }

    public void setWorkflowStages(Collection<WorkflowStage> workflowStages)
    {
        getWorkflowStages().clear();
        getWorkflowStages().addAll(workflowStages);
    }

    public WorkflowStage addWorkflowStage(WorkflowStage stage)
    {
        if (stage != null)
        {
            getWorkflowStages().add(stage);
        }
        return stage;
    }

    public DemuxLink getDemux()
    {
        return demux;
    }

    public void setDemux(Linkable<Demux> demux)
    {
        this.demux = demux == null ? null : new DemuxLink(demux.getUri());
    }

    @Override
    public String getLimsid()
    {
        return limsid;
    }

    @Override
    public void setLimsid(String value)
    {
        this.limsid = value;
    }

    @Override
    public URI getUri()
    {
        return uri;
    }

    @Override
    public void setUri(URI value)
    {
        this.uri = value;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(limsid).append(' ').append(type);
        return sb.toString();
    }
}
