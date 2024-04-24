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

package com.genologics.ri.sample;

import static com.genologics.ri.Namespaces.FILE_NAMESPACE;
import static com.genologics.ri.Namespaces.ROOT_NAMESPACE;
import static com.genologics.ri.Namespaces.UDF_NAMESPACE;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.cruk.clarity.api.jaxb.ShortDateAdapter;

import com.genologics.ri.ExternalId;
import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.Linkable;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.configuration.FieldType;
import com.genologics.ri.controltype.ControlType;
import com.genologics.ri.file.ClarityFile;
import com.genologics.ri.project.Project;
import com.genologics.ri.researcher.Researcher;
import com.genologics.ri.userdefined.UDF;
import com.genologics.ri.userdefined.UDFHolder;
import com.genologics.ri.userdefined.UDT;

/**
 * <p>
 * The base representation of a Sample, defining common elements for both Sample
 * and Sample creation.
 * </p>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "samplebase",
         propOrder = { "name", "dateReceived", "dateCompleted", "project", "controlType", "submitter",
                       "artifact", "bioSource", "type", "fields", "externalIds", "files" })
@XmlSeeAlso({ SampleCreation.class, Sample.class })
public class SampleBase implements UDFHolder, Serializable
{
    private static final long serialVersionUID = 7926341075019764297L;

    protected String name;

    @XmlElement(name = "date-received")
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(ShortDateAdapter.class)
    protected Date dateReceived;

    @XmlElement(name = "date-completed")
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(ShortDateAdapter.class)
    protected Date dateCompleted;

    protected ProjectLink project;

    @XmlElement(name = "control-type")
    protected ControlTypeLink controlType;

    protected Submitter submitter;

    protected ArtifactLink artifact;

    @XmlElement(name = "biosource")
    @Deprecated
    protected BioSource bioSource;

    @XmlElement(name = "type", namespace = UDF_NAMESPACE)
    protected UDT type;

    @XmlElement(name = "field", namespace = UDF_NAMESPACE)
    protected List<UDF> fields;

    @XmlElement(name = "externalid", namespace = ROOT_NAMESPACE)
    protected List<ExternalId> externalIds;

    @XmlElement(name = "file", namespace = FILE_NAMESPACE)
    protected List<ClarityFile> files;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    protected SampleBase()
    {
    }

    protected SampleBase(SampleBase other)
    {
        this.name = other.name;
        this.dateReceived = other.dateReceived;
        this.dateCompleted = other.dateCompleted;
        this.project = other.project;
        this.controlType = other.controlType;
        this.submitter = other.submitter;
        this.artifact = other.artifact;
        this.bioSource = other.bioSource;
        this.type = other.type;
        if (other.fields != null)
        {
            this.fields = new ArrayList<UDF>(other.fields);
        }
        if (other.externalIds != null)
        {
            this.externalIds = new ArrayList<ExternalId>(other.externalIds);
        }
        if (other.files != null)
        {
            this.files = new ArrayList<ClarityFile>(other.files);
        }
    }

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        this.name = value;
    }

    public Date getDateReceived()
    {
        return dateReceived;
    }

    public void setDateReceived(Date value)
    {
        this.dateReceived = value;
    }

    public Date getDateCompleted()
    {
        return dateCompleted;
    }

    public void setDateCompleted(Date value)
    {
        this.dateCompleted = value;
    }

    public ProjectLink getProject()
    {
        return project;
    }

    public void setProject(LimsEntityLinkable<Project> project)
    {
        this.project = new ProjectLink(project);
    }

    public ControlTypeLink getControlType()
    {
        return controlType;
    }

    public void setControlType(Linkable<ControlType> controlType)
    {
        this.controlType = controlType == null ? null : new ControlTypeLink(controlType);
    }

    public boolean isControlSample()
    {
        return controlType != null;
    }

    public Submitter getSubmitter()
    {
        return submitter;
    }

    public Submitter setSubmitter(Linkable<Researcher> link)
    {
        this.submitter = link == null ? null : new Submitter(link);
        return this.submitter;
    }

    public ArtifactLink getArtifact()
    {
        return artifact;
    }

    public void setArtifact(LimsEntityLinkable<Artifact> artifact)
    {
        this.artifact = new ArtifactLink(artifact);
    }

    @Deprecated
    public BioSource getBioSource()
    {
        return bioSource;
    }

    @Deprecated
    public void setBioSource(BioSource bioSource)
    {
        this.bioSource = bioSource;
    }

    public UDT getUserDefinedType()
    {
        return type;
    }

    public UDT setUserDefinedType(UDT value)
    {
        this.type = value;
        return this.type;
    }

    public UDT setUserDefinedType(String type)
    {
        this.type = new UDT(type);
        return this.type;
    }

    @Override
    public List<UDF> getUserDefinedFields()
    {
        if (fields == null)
        {
            fields = new ArrayList<UDF>();
        }
        return fields;
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

    public List<ExternalId> getExternalIds()
    {
        if (externalIds == null)
        {
            externalIds = new ArrayList<ExternalId>();
        }
        return this.externalIds;
    }

    public List<ClarityFile> getFiles()
    {
        if (files == null)
        {
            files = new ArrayList<ClarityFile>();
        }
        return this.files;
    }

    public ClarityFile addFile(ClarityFile f)
    {
        getFiles().add(f);
        return f;
    }

    public String getLimsid()
    {
        return limsid;
    }

    public void setLimsid(String value)
    {
        this.limsid = value;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI value)
    {
        this.uri = value;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(limsid).append(' ').append(name);
        return sb.toString();
    }
}
