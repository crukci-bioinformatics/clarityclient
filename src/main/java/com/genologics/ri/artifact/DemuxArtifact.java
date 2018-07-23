
package com.genologics.ri.artifact;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.Locatable;

/**
 * Details of an individual artifact that is part of a pooled artifact.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "demux-artifact", propOrder = { "samples", "reagentLabels", "demux" })
public class DemuxArtifact implements Locatable, Serializable
{
    private static final long serialVersionUID = 8205564798487086835L;

    @XmlElementWrapper(name = "samples")
    @XmlElement(name = "sample")
    protected List<DemuxArtifactSample> samples;

    @XmlElementWrapper(name = "reagent-labels")
    @XmlElement(name = "reagent-label")
    protected List<ReagentLabel> reagentLabels;

    @XmlElement
    protected DemuxDetails demux;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public DemuxArtifact()
    {
    }

    public DemuxDetails getDemux()
    {
        return demux;
    }

    public void setDemux(DemuxDetails demux)
    {
        this.demux = demux;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public List<DemuxArtifactSample> getSamples()
    {
        if (samples == null)
        {
            samples = new ArrayList<>();
        }
        return samples;
    }

    public List<ReagentLabel> getReagentLabels()
    {
        if (reagentLabels == null)
        {
            reagentLabels = new ArrayList<>();
        }
        return reagentLabels;
    }

}
