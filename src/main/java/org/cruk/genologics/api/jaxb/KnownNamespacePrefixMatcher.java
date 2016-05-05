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

package org.cruk.genologics.api.jaxb;

import static com.genologics.ri.Namespaces.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import com.sun.xml.bind.v2.WellKnownNamespace;

/**
 * Implementation of the internal Sun JAXB {@code NamespacePrefixMapper} to make
 * sure the XML documents created from JAXB have user friendly name spaces in their
 * headers.
 */
public class KnownNamespacePrefixMatcher extends NamespacePrefixMapper
{
    /**
     * Map of namespace declarations to reader friendly short tags.
     */
    private static final Map<String, String> PREFIX_MAP;
    static
    {
        Map<String, String> map = new HashMap<String, String>();

        map.put(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "xsi");
        map.put(XMLConstants.W3C_XML_SCHEMA_NS_URI, "xsd");
        map.put(WellKnownNamespace.XML_MIME_URI, "xmime");

        map.put(ROOT_NAMESPACE, "ri");
        map.put(ARTIFACT_GROUP_NAMESPACE, "artgr");
        map.put(ARTIFACT_NAMESPACE, "art");
        map.put(CONFIGURATION_NAMESPACE, "cnf");
        map.put(CONTAINER_TYPE_NAMESPACE, "ctp");
        map.put(CONTAINER_NAMESPACE, "con");
        map.put(EXCEPTION_NAMESPACE, "exc");
        map.put(FILE_NAMESPACE, "file");
        map.put(INSTRUMENT_NAMESPACE, "inst");
        map.put(INSTRUMENT_TYPE_NAMESPACE, "itp");
        map.put(LAB_NAMESPACE, "lab");
        map.put(PERMISSION_NAMESPACE, "perm");
        map.put(PROCESS_EXECUTION_NAMESPACE, "prx");
        map.put(PROCESS_TEMPLATE_NAMESPACE, "ptm");
        map.put(PROCESS_TYPE_NAMESPACE, "ptp");
        map.put(PROCESS_NAMESPACE, "prc");
        map.put(PROJECT_NAMESPACE, "prj");
        map.put(PROPERTY_NAMESPACE, "prop");
        map.put(PROTOCOL_CONFIGURATION_NAMESPACE, "protcnf");
        map.put(QUEUE_NAMESPACE, "que");
        map.put(REAGENT_KIT_NAMESPACE, "kit");
        map.put(REAGENT_LOT_NAMESPACE, "lot");
        map.put(REAGENT_TYPE_NAMESPACE, "rtp");
        map.put(RESEARCHER_NAMESPACE, "res");
        map.put(ROLE_NAMESPACE, "role");
        map.put(ROUTING_NAMESPACE, "rt");
        map.put(SAMPLE_NAMESPACE, "smp");
        map.put(STAGE_NAMESPACE, "stg");
        map.put(STEP_NAMESPACE, "stp");
        map.put(STEP_CONFIGURATION_NAMESPACE, "protstepcnf");
        map.put(UDF_NAMESPACE, "udf");
        map.put(VERSION_NAMESPACE, "ver");
        map.put(WORKFLOW_CONFIGURATION_NAMESPACE, "wkfcnf");

        PREFIX_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * A local, modifiable map of namespace to prefix strings.
     */
    private Map<String, String> prefixMap;

    /**
     * Default constructor.
     */
    public KnownNamespacePrefixMatcher()
    {
        prefixMap = PREFIX_MAP;
    }

    /**
     * Add a prefix for to the standard set for this instance of the
     * namespace matcher.
     *
     * @param namespace The namespace to add (or replace).
     * @param prefix The short prefix string.
     */
    public void addPrefix(String namespace, String prefix)
    {
        // If the map is the default one, a copy needs to be made first.
        if (prefixMap == PREFIX_MAP)
        {
            prefixMap = new HashMap<String, String>(PREFIX_MAP);
        }
        prefixMap.put(namespace, prefix);
    }

    /**
     * Returns a preferred prefix for the given name space URI.
     * This overridden method checks to see if the given name space URI is one that
     * is known to this class. If it is, the "nice" tag is returned. If not, the
     * suggestion is returned.
     *
     * @param namespaceUri The name space URI for which the prefix needs to be found.
     * @param suggestion The suggested tag for the name space URI.
     * @param requirePrefix Whether a non-empty name space is required (i.e. not the
     * default name space).
     *
     * @return The "nice" tag for the name space URI, or the suggested one if the
     * URI is not recognised.
     */
    @Override
    public String getPreferredPrefix(String namespaceUri,
                                     String suggestion,
                                     boolean requirePrefix)
    {
        String ns = prefixMap.get(namespaceUri);
        return ns == null ? suggestion : ns;
    }
}
