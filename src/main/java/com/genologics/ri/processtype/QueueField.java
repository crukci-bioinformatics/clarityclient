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

package com.genologics.ri.processtype;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * A field has a name and an attach to value,
 * these fields are used to keep track of all the UDF
 * values for samples in queue and work view as well as
 * those on the actual step
 *
 * @since 2.25
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queuefield")
public class QueueField extends Field
{
    private static final long serialVersionUID = 2849413901300410668L;

    @XmlAttribute(name = "detail")
    protected Boolean detail;

    public QueueField()
    {
    }

    public Boolean getDetail()
    {
        return detail;
    }

    public void setDetail(Boolean detail)
    {
        this.detail = detail;
    }
}
