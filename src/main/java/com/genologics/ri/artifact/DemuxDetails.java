
package com.genologics.ri.artifact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.Linkable;
import com.genologics.ri.step.ProcessStep;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "demux-details", propOrder = { "poolStep", "artifacts" })
public class DemuxDetails implements Serializable
{
    private static final long serialVersionUID = -6725351237693935651L;

    @XmlElement(name = "pool-step")
    protected PoolStep poolStep;

    @XmlElementWrapper(name = "artifacts")
    @XmlElement(name = "artifact")
    protected List<DemuxArtifact> artifacts;

    public DemuxDetails()
    {
    }

    public PoolStep getPoolStep()
    {
        return poolStep;
    }

    public void setPoolStep(PoolStep poolStep)
    {
        this.poolStep = poolStep;
    }

    public void setPoolStep(Linkable<ProcessStep> poolStep)
    {
        this.poolStep = poolStep == null ? null : new PoolStep(poolStep.getUri());
    }

    public List<DemuxArtifact> getArtifacts()
    {
        if (artifacts == null)
        {
            artifacts = new ArrayList<>();
        }
        return artifacts;
    }
}
