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

package com.genologics.ri.file;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.BatchUpdate;
import com.genologics.ri.ClarityBatchRetrieveResult;
import com.genologics.ri.Namespaces;

/**
 * The representation of a batch of file resources.
 * @since 2.18
 */
@ClarityBatchRetrieveResult(entityClass = ClarityFile.class, batchUpdate = true)
@XmlRootElement(name = "details")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "details")
public class ClarityFileBatchFetchResult implements BatchUpdate<ClarityFile>, Serializable
{
    private static final long serialVersionUID = -536166874357106044L;

    @XmlElement(name = "file", namespace = Namespaces.FILE_NAMESPACE)
    protected List<ClarityFile> files;

    public List<ClarityFile> getFiles()
    {
        if (files == null)
        {
            files = new ArrayList<ClarityFile>();
        }
        return files;
    }

    @Override
    public List<ClarityFile> getList()
    {
        return getFiles();
    }

    @Override
    public int getSize()
    {
        return files == null ? 0 : files.size();
    }

    @Override
    public Iterator<ClarityFile> iterator()
    {
        return getFiles().iterator();
    }

    @Override
    public void addForCreate(Collection<ClarityFile> entities)
    {
        getFiles().addAll(entities);
    }

    @Override
    public void addForUpdate(Collection<ClarityFile> entities)
    {
        // Nothing to worry about with state parameters for files.
        getFiles().addAll(entities);
    }

}
