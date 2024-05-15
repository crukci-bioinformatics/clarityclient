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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;


/**
 * A field has a name and an attach to value,
 * these fields are used to keep track of all the UDF
 * values for samples in ice bucket view.
 *
 * @since 2.25
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "icebucketfield")
public class IceBucketField extends Field
{
    private static final long serialVersionUID = 8915614372544937283L;

    @XmlAttribute(name = "detail")
    protected Boolean detail;

    public IceBucketField()
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
