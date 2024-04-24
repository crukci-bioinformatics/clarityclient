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

package com.genologics.ri;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import jakarta.xml.bind.annotation.XmlSchema;

/**
 * Static list of all the namespaces in the Genologics API.
 */
public final class Namespaces
{
    public static final String ROOT_NAMESPACE = "http://genologics.com/ri";
    public static final String ARTIFACT_NAMESPACE = "http://genologics.com/ri/artifact";
    public static final String ARTIFACT_GROUP_NAMESPACE = "http://genologics.com/ri/artifactgroup";
    public static final String AUTOMATION_NAMESPACE = "http://genologics.com/ri/automation";
    public static final String CONFIGURATION_NAMESPACE = "http://genologics.com/ri/configuration";
    public static final String CONTAINER_NAMESPACE = "http://genologics.com/ri/container";
    public static final String CONTAINER_TYPE_NAMESPACE = "http://genologics.com/ri/containertype";
    public static final String CONTROL_TYPE_NAMESPACE = "http://genologics.com/ri/controltype";
    public static final String EXCEPTION_NAMESPACE = "http://genologics.com/ri/exception";
    public static final String FILE_NAMESPACE = "http://genologics.com/ri/file";
    public static final String INSTRUMENT_NAMESPACE = "http://genologics.com/ri/instrument";
    public static final String INSTRUMENT_TYPE_NAMESPACE = "http://genologics.com/ri/instrumenttype";
    public static final String LAB_NAMESPACE = "http://genologics.com/ri/lab";
    public static final String PERMISSION_NAMESPACE = "http://genologics.com/ri/permission";
    public static final String PROCESS_NAMESPACE = "http://genologics.com/ri/process";
    public static final String PROCESS_EXECUTION_NAMESPACE = "http://genologics.com/ri/processexecution";
    public static final String PROCESS_TEMPLATE_NAMESPACE = "http://genologics.com/ri/processtemplate";
    public static final String PROCESS_TYPE_NAMESPACE = "http://genologics.com/ri/processtype";
    public static final String PROJECT_NAMESPACE = "http://genologics.com/ri/project";
    public static final String PROPERTY_NAMESPACE = "http://genologics.com/ri/property";
    public static final String PROTOCOL_CONFIGURATION_NAMESPACE = "http://genologics.com/ri/protocolconfiguration";
    public static final String QUEUE_NAMESPACE = "http://genologics.com/ri/queue";
    public static final String REAGENT_KIT_NAMESPACE = "http://genologics.com/ri/reagentkit";
    public static final String REAGENT_LOT_NAMESPACE = "http://genologics.com/ri/reagentlot";
    public static final String REAGENT_TYPE_NAMESPACE = "http://genologics.com/ri/reagenttype";
    public static final String ROLE_NAMESPACE = "http://genologics.com/ri/role";
    public static final String ROUTING_NAMESPACE = "http://genologics.com/ri/routing";
    public static final String RESEARCHER_NAMESPACE = "http://genologics.com/ri/researcher";
    public static final String SAMPLE_NAMESPACE = "http://genologics.com/ri/sample";
    public static final String STAGE_NAMESPACE = "http://genologics.com/ri/stage";
    public static final String STEP_NAMESPACE = "http://genologics.com/ri/step";
    public static final String STEP_CONFIGURATION_NAMESPACE = "http://genologics.com/ri/stepconfiguration";
    public static final String UDF_NAMESPACE = "http://genologics.com/ri/userdefined";
    public static final String VERSION_NAMESPACE = "http://genologics.com/ri/version";
    public static final String WORKFLOW_CONFIGURATION_NAMESPACE = "http://genologics.com/ri/workflowconfiguration";

    /**
     * The empty namespace. Used for some inner elements.
     */
    public static final String EMPTY_NAMESPACE = EMPTY;

    /**
     * Extract package level information for the named package.
     * No longer used, but kept for reference.
     *
     * @param packageName The name of the package to interrogate.
     *
     * @return The namespace attribute on the package's XmlSchema annotation.
     */
    @SuppressWarnings("unused")
    private static String findNamespace(String packageName)
    {
        Class<?> objectFactoryClass;
        try
        {
            objectFactoryClass = Class.forName(packageName + ".ObjectFactory");
        }
        catch (ClassNotFoundException e)
        {
            throw new AssertionError("No ObjectFactory class in package " + packageName);
        }

        Package p = objectFactoryClass.getPackage(); // Package.getPackage(packageName);
        if (p == null)
        {
            throw new NullPointerException("There is no package '" + packageName + "'.");
        }
        XmlSchema a = p.getAnnotation(XmlSchema.class);
        if (a == null)
        {
            throw new NullPointerException("There is no XmlSchema annotation on the package '" + packageName + "'.");
        }
        String ns = a.namespace();
        if (ns == null)
        {
            throw new NullPointerException("There is no namespace set on the package '" + packageName + "'.");
        }
        return ns;
    }
}
