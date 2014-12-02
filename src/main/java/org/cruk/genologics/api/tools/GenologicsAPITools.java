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

package org.cruk.genologics.api.tools;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.TreeBidiMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cruk.genologics.api.GenologicsAPI;
import org.springframework.beans.factory.annotation.Required;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.genologics.ri.LimsEntityLink;
import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.LimsLink;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.container.Container;
import com.genologics.ri.process.ArtifactLink;
import com.genologics.ri.process.GenologicsProcess;
import com.genologics.ri.process.InputOutputMap;
import com.genologics.ri.reagenttype.ReagentType;
import com.genologics.ri.sample.Sample;
import com.genologics.ri.userdefined.UDF;


/**
 * A set of helpful utilities for general functionality around the API.
 *
 * <p>These utilities may be of use to others, but their primary
 * purpose is to support operations done here at CRUK-CI.
 * </p>
 */
public class GenologicsAPITools
{
    /**
     * Container type name for flow cells.
     */
    public static final String FLOW_CELL_CONTAINER_TYPE;

    /**
     * Container type name for rapid run (Hiseq 2500) flow cells.
     */
    public static final String RAPID_RUN_FLOW_CELL_CONTAINER_TYPE;

    /**
     * Container type name for Miseq reagent cartridges.
     * @deprecated Use {@code MISEQ_REAGENT_CARTRIDGE_CONTAINER_TYPE} instead.
     */
    @Deprecated
    public static final String REAGENT_CARTRIDGE_CONTAINER_TYPE;

    /**
     * Container type name for Miseq reagent cartridges.
     */
    public static final String MISEQ_REAGENT_CARTRIDGE_CONTAINER_TYPE;

    /**
     * Container type name for Nextseq reagent cartridges.
     */
    public static final String NEXTSEQ_REAGENT_CARTRIDGE_CONTAINER_TYPE;

    /**
     * Name of the GAIIx/Hiseq sequencing process.
     */
    public static final String GA_HISEQ_RUN_PROCESS_NAME;

    /**
     * Name of the Miseq sequencing process.
     */
    public static final String MISEQ_RUN_PROCESS_NAME;

    /**
     * Name of the Nextseq sequencing process.
     */
    public static final String NEXTSEQ_RUN_PROCESS_NAME;

    /**
     * Name of the sequencing process for runs imported from the old LIMS.
     */
    public static final String HISTORIC_RUN_PROCESS_NAME;

    /**
     * Names of the sequencing processes (GAIIx/Hiseq, Miseq, Nextseq & Historic).
     */
    public static final Set<String> SEQUENCING_PROCESS_NAMES;

    /**
     * The "No Index" reagent label.
     */
    public static final String NO_INDEX_LABEL;

    /**
     * The "Unspecified Index" reagent label.
     */
    public static final String OTHER_INDEX_LABEL;

    /**
     * The "In-Line" reagent label.
     */
    public static final String INLINE_INDEX_LABEL;

    /**
     * Name of the field for SLX identifiers on samples and artifacts.
     */
    public static final String SLX_ID_FIELD;

    /**
     * Pattern for finding SLX id artifact names.
     */
    public static final Pattern SLX_ID_PATTERN;

    /**
     * Name of the field on sequencing processes for the run id.
     */
    public static final String RUN_ID_FIELD;

    /**
     * Name of the field on sequencing processes for the flow cell id.
     */
    public static final String FLOW_CELL_ID_FIELD;

    /**
     * Name of the field on sequencing processes for the reagent cartridge id.
     */
    public static final String REAGENT_CARTRIDGE_ID_FIELD;

    /**
     * The flow cell UDF as a search term.
     */
    private static final String FLOW_CELL_ID_UDF;

    /**
     * The names of processes that perform pooling.
     */
    protected static final Set<String> POOLING_PROCESS_NAMES;

    /**
     * The names of processes that perform barcoding.
     */
    protected static final Set<String> BARCODING_PROCESS_NAMES;

    /**
     * The names of barcode indexes that are not actual multiplex bar codes.
     */
    public static final Set<String> SINGLEPLEX_BARCODE_NAMES;

    /**
     * Synchronization lock for {@code getBarcodes}.
     */
    private static Object barcodeMapLock = new Object();

    /**
     * Time to use the barcode cache maps until they need to be re-read.
     * This is set to six hours.
     */
    private static final long BARCODE_MAP_MAXIMIUM_CACHE_TIME = 6 * 60 * 60 * 1000;

    /**
     * Logger.
     */
    protected Log logger = LogFactory.getLog(GenologicsAPITools.class);

    /**
     * The Clarity API.
     */
    protected GenologicsAPI api;

    /**
     * The barcode cache file.
     */
    protected File barcodeCacheFile;

    /**
     * Map of reagent type name to its sequence.
     * This is the cached result of a call to {@link #getBarcodes()}.
     */
    protected Map<String, String> simpleBarcodeMap;

    /**
     * Time of the last time the barcodes were read into {@code simpleBarcodeMap}.
     * If this gets too long, read again to make sure changes are picked up.
     */
    private long lastBarcodeMapReadTime;

    /**
     * Map of reagent category name to map of barcode name to sequence within that category.
     * This is the cached result of a call to {@link #getBarcodesByReagentCategory()}.
     */
    protected Map<String, BidiMap<String, String>> barcodeByCategoryMap;

    /**
     * Thread local map of artifacts retrieved so far for getReagentLabelsForSamplesInPool.
     * @see #getReagentLabelsForSamplesInPoolRecursive(Artifact, Map)
     */
    private ThreadLocal<Map<String, Sample>> knownSamples = new ThreadLocal<Map<String, Sample>>();

    /**
     * Thread local map of artifacts retrieved so far for getReagentLabelsForSamplesInPool.
     * @see #getReagentLabelsForSamplesInPoolRecursive(Artifact, Map)
     */
    private ThreadLocal<Map<String, Artifact>> knownArtifacts = new ThreadLocal<Map<String, Artifact>>();

    /**
     * Thread local map of processes retrieved so far for getReagentLabelsForSamplesInPool.
     * @see #getReagentLabelsForSamplesInPoolRecursive(Artifact, Map)
     */
    private ThreadLocal<Map<String, GenologicsProcess>> knownProcesses = new ThreadLocal<Map<String, GenologicsProcess>>();


    /**
     * Static initialiser - complete fields from resource file.
     */
    static
    {
        final String bundleName = "org/cruk/genologics/api/genologics-settings";

        ResourceBundle settings;
        try
        {
            settings = ResourceBundle.getBundle(bundleName);
        }
        catch (MissingResourceException e)
        {
            Log logger = LogFactory.getLog(GenologicsAPITools.class);
            logger.fatal("Cannot locate the resource bundle " + bundleName + " on the class path.");
            throw e;
        }

        try
        {
            FLOW_CELL_CONTAINER_TYPE = settings.getString("containertype.flowcell");
            RAPID_RUN_FLOW_CELL_CONTAINER_TYPE = settings.getString("containertype.rapidrunflowcell");
            MISEQ_REAGENT_CARTRIDGE_CONTAINER_TYPE = settings.getString("containertype.miseqcartridge");
            NEXTSEQ_REAGENT_CARTRIDGE_CONTAINER_TYPE = settings.getString("containertype.nextseqcartridge");
            GA_HISEQ_RUN_PROCESS_NAME = settings.getString("process.sequencing.hiseq");
            MISEQ_RUN_PROCESS_NAME = settings.getString("process.sequencing.miseq");
            NEXTSEQ_RUN_PROCESS_NAME = settings.getString("process.sequencing.nextseq");
            HISTORIC_RUN_PROCESS_NAME = settings.getString("process.sequencing.historic");
            NO_INDEX_LABEL = settings.getString("barcode.noindex");
            OTHER_INDEX_LABEL = settings.getString("barcode.otherindex");
            INLINE_INDEX_LABEL = settings.getString("barcode.inline");
            SLX_ID_FIELD = settings.getString("field.slxidentifer");
            SLX_ID_PATTERN = Pattern.compile(settings.getString("slxidentifier.pattern"));
            RUN_ID_FIELD = settings.getString("field.runid");
            FLOW_CELL_ID_FIELD = settings.getString("field.flowcellid");
            REAGENT_CARTRIDGE_ID_FIELD = settings.getString("field.reagentcartridgeid");
            FLOW_CELL_ID_UDF = "udf." + FLOW_CELL_ID_FIELD;

            REAGENT_CARTRIDGE_CONTAINER_TYPE = MISEQ_REAGENT_CARTRIDGE_CONTAINER_TYPE;

            Pattern splitter = Pattern.compile("\\s*,\\s*");

            String[] poolingProcesses = splitter.split(settings.getString("process.poolingprocesses"));
            POOLING_PROCESS_NAMES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(poolingProcesses)));

            String[] barcodingProcesses = splitter.split(settings.getString("process.barcodingprocesses"));
            BARCODING_PROCESS_NAMES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(barcodingProcesses)));

            SEQUENCING_PROCESS_NAMES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(GA_HISEQ_RUN_PROCESS_NAME, MISEQ_RUN_PROCESS_NAME,
                                                                                                     NEXTSEQ_RUN_PROCESS_NAME, HISTORIC_RUN_PROCESS_NAME)));

            SINGLEPLEX_BARCODE_NAMES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(NO_INDEX_LABEL, OTHER_INDEX_LABEL, INLINE_INDEX_LABEL)));
        }
        catch (MissingResourceException e)
        {
            Log logger = LogFactory.getLog(GenologicsAPITools.class);
            logger.fatal("Cannot locate the setting '" + e.getKey() + "' in the resource bundle " + bundleName);
            throw e;
        }
    }

    /**
     * Constructor.
     */
    public GenologicsAPITools()
    {
        // Some uses of this class will run within the JBoss application server.
        // In that environment, use its temporary directory.
        File tmpDir = new File(System.getProperty("jboss.server.temp.dir", System.getProperty("java.io.tmpdir", "/tmp")));
        barcodeCacheFile = new File(tmpDir, "barcodes.txt");
    }

    /**
     * Set the {@code GenologicsAPI} to use.
     *
     * @param api The GenologicsAPI instance.
     */
    @Required
    public void setGenologicsAPI(GenologicsAPI api)
    {
        this.api = api;
    }

    /**
     * Get the file where barcode category, name and sequence are cached.
     *
     * @return The barcode cache file.
     *
     * @see #getBarcodesByReagentCategory(boolean)
     */
    public File getBarcodeCacheFile()
    {
        return barcodeCacheFile;
    }

    /**
     * Set the file where barcode category, name and sequence are cached.
     *
     * @param barcodeCacheFile The barcode cache file.
     */
    public void setBarcodeCacheFile(File barcodeCacheFile)
    {
        this.barcodeCacheFile = barcodeCacheFile;
    }

    /**
     * Checks the API is set, and throws an IllegalStateException if not.
     *
     * @throws IllegalStateException if the API is not set.
     */
    private void checkReady()
    {
        if (api == null)
        {
            throw new IllegalStateException("api has not been set.");
        }
    }

    /**
     * Find all the sequencing processes that have a finish date that used the flow cell with
     * the giving id.
     *
     * @param flowcellId The id of the flow cell.
     * @param completeOnly If {@code true}, only return processes with a finish date.
     *
     * @return A list of complete sequencing processes (all types) that used the flow cell.
     */
    public List<GenologicsProcess> findSequencingProcessesForFlowcell(String flowcellId, boolean completeOnly)
    {
        Map<String, Object> search = new HashMap<String, Object>();
        search.put(FLOW_CELL_ID_UDF, flowcellId);

        // GAIIx / Hiseq
        search.put("type", GA_HISEQ_RUN_PROCESS_NAME);

        List<LimsLink<GenologicsProcess>> links = api.find(search, GenologicsProcess.class);

        // Miseq
        search.put("type", MISEQ_RUN_PROCESS_NAME);

        links.addAll(api.find(search, GenologicsProcess.class));

        // Nextseq
        search.put("type", NEXTSEQ_RUN_PROCESS_NAME);

        links.addAll(api.find(search, GenologicsProcess.class));

        // Historical runs.
        search.put("type", HISTORIC_RUN_PROCESS_NAME);

        links.addAll(api.find(search, GenologicsProcess.class));

        List<GenologicsProcess> processes = api.loadAll(links);

        if (completeOnly)
        {
            // Filter out those without a finish date.

            Iterator<GenologicsProcess> iter = processes.iterator();
            while (iter.hasNext())
            {
                GenologicsProcess p = iter.next();

                UDF finishedField = UDF.getUDF(p.getUserDefinedFields(), "Finish Date");
                if (finishedField == null || StringUtils.isBlank(finishedField.getValue()))
                {
                    iter.remove();
                }
            }
        }

        return processes;
    }

    /**
     * Find a flow cell by name (id). This involves a simple look for the container with the
     * name, but in the case of Miseq this can involve looking for a sequencing process with
     * the "Flow Cell ID" field set to the given value, then returning the container used by
     * the process (which will be the reagent cartridge).
     *
     * @param name The id of the flow cell.
     *
     * @return The appropriate container used for sequencing with the given id.
     */
    public Container findFlowcellByName(String name)
    {
        GenologicsProcess process;
        UDF cartridgeField;

        Map<String, Object> search = new HashMap<String, Object>();
        search.put("name", name);
        search.put("type", new String[] { FLOW_CELL_CONTAINER_TYPE, RAPID_RUN_FLOW_CELL_CONTAINER_TYPE } );

        List<LimsLink<Container>> containers = api.find(search, Container.class);
        switch (containers.size())
        {
            case 0:
                break;

            case 1:
                return api.load(containers.get(0));

            default:
                logger.warn("There are " + containers.size() + " flow cells with the name '" + name + "'.");
                return api.load(containers.get(0));
        }

        // If fallen through, nothing found. Search for a MiSeq flow cell instead.
        search.clear();
        search.put(FLOW_CELL_ID_UDF, name);
        search.put("type", MISEQ_RUN_PROCESS_NAME);

        List<LimsLink<GenologicsProcess>> miseqProcesses = api.find(search, GenologicsProcess.class);
        switch (miseqProcesses.size())
        {
            case 0:
                // May be in the historical Miseq runs.

                search.put("type", HISTORIC_RUN_PROCESS_NAME);
                search.put("udf.Device Type", "MiSeq");
                miseqProcesses = api.find(search, GenologicsProcess.class);

                switch (miseqProcesses.size())
                {
                    case 0:
                    case 1:
                        break;

                    default:
                        logger.warn("There are " + containers.size() + " historical Miseq runs using the flow cell with the name '" + name + "'.");
                        break;
                }
                break;

            case 1:
                break;

            default:
                logger.warn("There are " + containers.size() + " Miseq runs using the flow cell with the name '" + name + "'.");
                break;
        }

        if (miseqProcesses.size() > 0)
        {
            process = api.load(miseqProcesses.get(0));

            cartridgeField = UDF.getUDF(process.getUserDefinedFields(), REAGENT_CARTRIDGE_ID_FIELD, true,
                                        "No " + REAGENT_CARTRIDGE_ID_FIELD + " field on MiSeq Run " + process.getLimsid());

            search.clear();
            search.put("name", cartridgeField.getValue());
            search.put("type", MISEQ_REAGENT_CARTRIDGE_CONTAINER_TYPE);

            containers = api.find(search, Container.class);
            switch (containers.size())
            {
                case 0:
                    return null;

                case 1:
                    return api.load(containers.get(0));

                default:
                    logger.warn("There are " + containers.size() + " MiSeq reagent cartridges with the name '" + cartridgeField.getValue() + "'.");
                    return api.load(containers.get(0));
            }
        }

        // If fallen through, nothing found. Search for a NextSeq flow cell instead.
        search.clear();
        search.put(FLOW_CELL_ID_UDF, name);
        search.put("type", NEXTSEQ_RUN_PROCESS_NAME);

        List<LimsLink<GenologicsProcess>> nextseqProcesses = api.find(search, GenologicsProcess.class);
        switch (nextseqProcesses.size())
        {
            case 0:
                return null;

            case 1:
                break;

            default:
                logger.warn("There are " + containers.size() + " Nextseq runs using the flow cell with the name '" + name + "'.");
                break;
        }

        process = api.load(nextseqProcesses.get(0));

        cartridgeField = UDF.getUDF(process.getUserDefinedFields(), REAGENT_CARTRIDGE_ID_FIELD, true,
                                    "No " + REAGENT_CARTRIDGE_ID_FIELD + " field on NextSeq Run " + process.getLimsid());

        search.clear();
        search.put("name", cartridgeField.getValue());
        search.put("type", NEXTSEQ_REAGENT_CARTRIDGE_CONTAINER_TYPE);

        containers = api.find(search, Container.class);
        switch (containers.size())
        {
            case 0:
                return null;

            case 1:
                return api.load(containers.get(0));

            default:
                logger.warn("There are " + containers.size() + " NextSeq reagent cartridges with the name '" + cartridgeField.getValue() + "'.");
                return api.load(containers.get(0));
        }
    }

    /**
     * Get a map of reagent label name to sequence. This result is held by this tools object
     * for reuse and, by default, cached to a file "barcodes.txt" in the temporary directory,
     * as fetching from the API can be an expensive operation.
     *
     * @return A map of reagent label name to sequence for all reagent types.
     */
    public Map<String, String> getBarcodes()
    {
        return getBarcodes(true);
    }

    /**
     * Get a map of reagent label name to sequence. This result is held by this tools object
     * for reuse and, optionally, cached to a file "barcodes.txt" in the temporary directory,
     * as fetching from the API can be an expensive operation.
     *
     * @param useCache Whether to read and write to the cache file "barcodes.txt".
     *
     * @return A map of reagent label name to sequence for all reagent types.
     */
    public Map<String, String> getBarcodes(boolean useCache)
    {
        getBarcodesByReagentCategory(useCache);
        return simpleBarcodeMap;
    }

    /**
     * Get a map of reagent category names to a map of the label name to sequence for barcodes
     * of that category. This result is held by this tools object
     * for reuse and, by default, cached to a file "barcodes.txt" in the temporary directory,
     * as fetching from the API can be an expensive operation.
     *
     * @return A map of reagent category to map of label name to sequence for that reagent type.
     */
    public Map<String, BidiMap<String, String>> getBarcodesByReagentCategory()
    {
        return getBarcodesByReagentCategory(true);
    }

    /**
     * Get a map of reagent category names to a map of the label name to sequence for barcodes
     * of that category. This result is held by this tools object
     * for reuse and, optionally, cached to a file "barcodes.txt" in the temporary directory,
     * as fetching from the API can be an expensive operation.
     *
     * @param useCache Whether to read and write to the cache file "barcodes.txt".
     *
     * @return A map of reagent category to map of label name to sequence for that reagent type.
     */
    public Map<String, BidiMap<String, String>> getBarcodesByReagentCategory(boolean useCache)
    {
        checkReady();

        synchronized (barcodeMapLock)
        {
            if (simpleBarcodeMap == null || lastBarcodeMapReadTime + BARCODE_MAP_MAXIMIUM_CACHE_TIME < System.currentTimeMillis())
            {
                simpleBarcodeMap = new HashMap<String, String>();
                barcodeByCategoryMap = new HashMap<String, BidiMap<String, String>>();

                int numberOfLabelsRead = 0;

                if (useCache && barcodeCacheFile.exists())
                {
                    logger.debug("Loading previously created barcode map from " + barcodeCacheFile.getAbsolutePath());

                    try
                    {
                        LineNumberReader reader = new LineNumberReader(new FileReader(barcodeCacheFile));
                        CSVReader csvReader = new CSVReader(reader);
                        try
                        {
                            String[] parts;
                            while ((parts = csvReader.readNext()) != null)
                            {
                                if (parts.length < 2)
                                {
                                    logger.debug("Have unexpected number of fields on line " + reader.getLineNumber() +
                                                 " of " + barcodeCacheFile.getAbsolutePath());
                                    simpleBarcodeMap.clear();
                                    barcodeByCategoryMap.clear();
                                    break;
                                }

                                String category = parts[0].trim();
                                String name = parts[1].trim();
                                String sequence = parts.length < 3 ? "" : parts[2].trim();

                                simpleBarcodeMap.put(name, sequence);

                                BidiMap<String, String> categoryMap = barcodeByCategoryMap.get(category);
                                if (categoryMap == null)
                                {
                                    categoryMap = new TreeBidiMap<String, String>();
                                    barcodeByCategoryMap.put(category, categoryMap);
                                }

                                assert !categoryMap.containsKey(name) : "Already have a barcode with the name '" + name + "' in " + category;
                                assert !categoryMap.containsValue(sequence) : "Already have a barcode with the sequence " + sequence + " in " + category;

                                categoryMap.put(name, sequence);

                                ++numberOfLabelsRead;
                            }
                        }
                        finally
                        {
                            csvReader.close();
                        }
                    }
                    catch (IOException e)
                    {
                        // Make sure we fetch from the API, as reading from file had problems.
                        simpleBarcodeMap.clear();
                        barcodeByCategoryMap.clear();
                    }
                }

                List<LimsLink<ReagentType>> reagentTypeList = api.listAll(ReagentType.class);

                if (simpleBarcodeMap.isEmpty() || reagentTypeList.size() != numberOfLabelsRead)
                {
                    logger.warn("Fetching all reagent labels. This can take some time.");

                    simpleBarcodeMap.clear();
                    barcodeByCategoryMap.clear();

                    List<ReagentType> allTypes = api.loadAll(reagentTypeList);

                    for (ReagentType type : allTypes)
                    {
                        String category = type.getReagentCategory();
                        String name = type.getName();
                        String sequence = type.getSpecialType().getAttributes().get(0).getValue();

                        simpleBarcodeMap.put(name, sequence);

                        BidiMap<String, String> categoryMap = barcodeByCategoryMap.get(category);
                        if (categoryMap == null)
                        {
                            categoryMap = new TreeBidiMap<String, String>();
                            barcodeByCategoryMap.put(category, categoryMap);
                        }

                        assert !categoryMap.containsKey(name) : "Already have a barcode with the name '" + name + "' in " + category;
                        assert !categoryMap.containsValue(sequence) : "Already have a barcode with the sequence " + sequence + " in " + category;

                        categoryMap.put(name, sequence);
                    }

                    if (useCache)
                    {
                        try
                        {
                            CSVWriter writer = new CSVWriter(new FileWriter(barcodeCacheFile));
                            try
                            {
                                String[] line = new String[3];
                                for (Map.Entry<String, BidiMap<String, String>> entry1 : barcodeByCategoryMap.entrySet())
                                {
                                    for (Map.Entry<String, String> entry2 : entry1.getValue().entrySet())
                                    {
                                        line[0] = entry1.getKey();
                                        line[1] = entry2.getKey();
                                        line[2] = entry2.getValue();
                                        writer.writeNext(line);
                                    }
                                }
                            }
                            finally
                            {
                                writer.close();
                                barcodeCacheFile.setReadable(true, false);
                                barcodeCacheFile.setWritable(true, false);
                            }
                        }
                        catch (IOException e)
                        {
                            // Ignore
                            barcodeCacheFile.delete();
                        }
                    }
                }

                lastBarcodeMapReadTime = System.currentTimeMillis();
            }
        }

        return barcodeByCategoryMap;
    }

    /**
     * Work out whether the lanes (artifacts) from a sequencing run have been flagged
     * as passing QC.
     * <p>A QC value of {@code PASSED} indicates a pass; all other values
     * are interpreted as a fail.</p>
     *
     * @param sequencingProcess The sequencing process to examine.
     *
     * @return A map of lane artifact LIMS ids to boolean pass or fail flags.
     */
    public Map<String, Boolean> getQCPasses(GenologicsProcess sequencingProcess)
    {
        if (sequencingProcess == null)
        {
            throw new IllegalArgumentException("sequencingProcess cannot be null");
        }

        Map<String, LimsLink<Artifact>> links = new HashMap<String, LimsLink<Artifact>>();
        for (InputOutputMap iomap : sequencingProcess.getInputOutputMaps())
        {
            links.put(iomap.getInput().getLimsid(), new com.genologics.ri.process.ArtifactLink(iomap.getInput().getPostProcessUri()));
        }

        List<Artifact> inputArtifactsInState = api.loadAll(links.values());
        Map<String, Artifact> artifacts = new HashMap<String, Artifact>();
        for (Artifact a : inputArtifactsInState)
        {
            artifacts.put(a.getLimsid(), a);
        }

        Map<String, Boolean> qcState = new HashMap<String, Boolean>();
        for (InputOutputMap iomap : sequencingProcess.getInputOutputMaps())
        {
            if (!qcState.containsKey(iomap.getInput().getLimsid()))
            {
                Artifact a = artifacts.get(iomap.getInput().getLimsid());
                assert a != null : "Don't have artifact " + iomap.getInput().getLimsid();

                Boolean passed;
                switch (a.getQCFlag())
                {
                    case FAILED:
                        passed = Boolean.FALSE;
                        break;

                    default:
                        passed = Boolean.TRUE;
                        break;
                }

                qcState.put(a.getLimsid(), passed);
            }
        }

        return qcState;
    }

    /**
     * Get the sample to reagent label mapping for all samples in the given artifact.
     * This recursively traverses back up the history of the artifact (via parent process
     * and earlier artifacts) until a pooling process is found, at which point we can
     * reliably discover which label was applied to each sample from the input artifact to
     * the pooling.
     *
     * <p>This is an expensive operation and while this class tries to minimise reloading
     * artifacts and processes, using the optional caching facility around the API is
     * strongly recommended.</p>
     *
     * @param artifact The artifact to start from.
     *
     * @return A map of sample LIMS id to reagent label name for all samples in the artifact.
     */
    public Map<String, String>
    getReagentLabelsForSamplesInPool(LimsEntityLinkable<Artifact> artifact)
    {
        if (artifact == null)
        {
            throw new IllegalArgumentException("artifact cannot be null");
        }

        checkReady();

        Map<String, String> sampleReagentMap = new HashMap<String, String>();

        try
        {
            Artifact realArtifact = getArtifact(artifact);
            getReagentLabelsForSamplesInPoolRecursive(realArtifact, sampleReagentMap);
        }
        finally
        {
            cleanup();
        }

        return sampleReagentMap;
    }

    /**
     * Adaptation of {@code getReagentLabelsForSamplesInPool} for a collection of starting
     * artifacts. Running en masse can help with local reloads, but again the use of the caching
     * facility is strongly recommended.
     *
     * @param artifacts A collection of either the artifacts themselves or links to the artifacts.
     *
     * @return A map of starting artifact id to another map of sample LIMS id to reagent label name
     * for all samples in that artifact.
     *
     * @see #getReagentLabelsForSamplesInPool(LimsEntityLinkable)
     */
    public Map<String, Map<String, String>>
    getReagentLabelsForSamplesInPools(Collection<? extends LimsEntityLinkable<Artifact>> artifacts)
    {
        if (artifacts == null)
        {
            throw new IllegalArgumentException("artifacts cannot be null");
        }

        checkReady();

        try
        {
            Map<String, Map<String, String>> collectiveMap = new HashMap<String, Map<String, String>>();

            for (LimsEntityLinkable<Artifact> linkable : artifacts)
            {
                if (!collectiveMap.containsKey(linkable.getLimsid()))
                {
                    Map<String, String> sampleReagentMap = new HashMap<String, String>();

                    Artifact artifact = getArtifact(linkable);

                    getReagentLabelsForSamplesInPoolRecursive(artifact, sampleReagentMap);

                    collectiveMap.put(artifact.getLimsid(), sampleReagentMap);
                }
            }

            return collectiveMap;
        }
        finally
        {
            cleanup();
        }
    }

    /**
     * Clean up after recursive searches by clearing out the local maps.
     */
    private void cleanup()
    {
        Map<String, Sample> smap = knownSamples.get();
        if (smap != null)
        {
            smap.clear();
            knownSamples.set(null);
        }

        Map<String, Artifact> amap = knownArtifacts.get();
        if (amap != null)
        {
            amap.clear();
            knownArtifacts.set(null);
        }

        Map<String, GenologicsProcess> pmap = knownProcesses.get();
        if (pmap != null)
        {
            pmap.clear();
            knownProcesses.set(null);
        }
    }

    /**
     * Get a Sample from the local cache map, or load it if it does not exist.
     *
     * @param linkable (Normally) the link to the Sample to fetch. If this happens
     * to be the actual sample, it is recorded in the map but not fetched again.
     *
     * @return The Sample.
     */
    private Sample getSample(LimsEntityLinkable<Sample> linkable)
    {
        Map<String, Sample> map = knownSamples.get();
        if (map == null)
        {
            map = new HashMap<String, Sample>();
            knownSamples.set(map);
        }

        Sample s = map.get(linkable.getLimsid());
        if (s == null)
        {
            if (linkable instanceof Sample)
            {
                s = (Sample)linkable;
            }
            else
            {
                s = api.retrieve(linkable.getUri(), Sample.class);
            }

            if (s == null)
            {
                throw new NullPointerException("No Sample " + linkable.getUri());
            }

            map.put(s.getLimsid(), s);
        }
        return s;
    }

    /**
     * Put a collection of samples into the thread local sample map.
     *
     * @param samples The samples to add.
     */
    @SuppressWarnings("unused")
    private void recordSamples(Collection<Sample> samples)
    {
        Map<String, Sample> map = knownSamples.get();
        if (map == null)
        {
            map = new HashMap<String, Sample>();
            knownSamples.set(map);
        }

        for (Sample s : samples)
        {
            map.put(s.getLimsid(), s);
        }
    }

    /**
     * Get an Artifact from the local cache map, or load it if it does not exist.
     *
     * @param linkable (Normally) the link to the Artifact to fetch. If this happens
     * to be the actual artifact, it is recorded in the map but not fetched again.
     *
     * @return The Artifact.
     */
    private Artifact getArtifact(LimsEntityLinkable<Artifact> linkable)
    {
        Map<String, Artifact> map = knownArtifacts.get();
        if (map == null)
        {
            map = new HashMap<String, Artifact>();
            knownArtifacts.set(map);
        }

        Artifact a = map.get(linkable.getLimsid());
        if (a == null)
        {
            if (linkable instanceof Artifact)
            {
                a = (Artifact)linkable;
            }
            else
            {
                a = api.retrieve(linkable.getUri(), Artifact.class);
            }

            if (a == null)
            {
                throw new NullPointerException("No Artifact " + linkable.getUri());
            }

            map.put(a.getLimsid(), a);
        }
        return a;
    }

    /**
     * Put a collection of artifacts into the thread local artifact map.
     *
     * @param artifacts The artifacts to add.
     */
    private void recordArtifacts(Collection<Artifact> artifacts)
    {
        Map<String, Artifact> map = knownArtifacts.get();
        if (map == null)
        {
            map = new HashMap<String, Artifact>();
            knownArtifacts.set(map);
        }

        for (Artifact a : artifacts)
        {
            map.put(a.getLimsid(), a);
        }
    }

    /**
     * Get a GenologicsProcess from the local cache map, or load it if it does not exist.
     *
     * @param linkable (Normally) the link to the GenologicsProcess to fetch. If this happens
     * to be the actual process, it is recorded in the map but not fetched again.
     *
     * @return The GenologicsProcess.
     */
    private GenologicsProcess getProcess(LimsEntityLinkable<GenologicsProcess> linkable)
    {
        Map<String, GenologicsProcess> map = knownProcesses.get();
        if (map == null)
        {
            map = new HashMap<String, GenologicsProcess>();
            knownProcesses.set(map);
        }

        GenologicsProcess p = map.get(linkable.getLimsid());
        if (p == null)
        {
            if (linkable instanceof GenologicsProcess)
            {
                p = (GenologicsProcess)linkable;
            }
            else
            {
                p = api.retrieve(linkable.getUri(), GenologicsProcess.class);
            }

            if (p == null)
            {
                throw new NullPointerException("No GenologicsProcess " + linkable.getUri());
            }

            map.put(p.getLimsid(), p);
        }
        return p;
    }

    /**
     * Recursively traverse from the given Artifact up its process and prior artifact history
     * until we can reliably obtain the reagent label for all the samples in the artifact.
     *
     * @param artifact The artifact to search from.
     * @param sampleReagentMap A map of sample LIMS id to reagent label name for all samples
     * in the artifact that is built up as this method runs.
     */
    private void getReagentLabelsForSamplesInPoolRecursive(
            Artifact artifact, Map<String, String> sampleReagentMap)
    {
        String artifactId = artifact.getLimsid();

        logger.debug("Looking at artifact " + artifactId + " " + artifact.getName());

        // If there is no parent, we only have the information on this artifact.
        // This can happen when there is a historical run on non-multiplexed artifacts.

        if (artifact.getParentProcess() == null)
        {
            if (artifact.getSamples().size() != 1)
            {
                throw new AssertionError("Submission artifact is expected to have exactly one sample.");
            }

            logger.debug("Top of the tree with " + artifactId + ". Using the reagent label from submission.");

            // Here, there should be zero or one reagent labels. If there are more, something has gone wrong.

            String label;
            switch (artifact.getReagentLabels().size())
            {
                case 0:
                    label = NO_INDEX_LABEL;
                    break;

                case 1:
                    label = artifact.getReagentLabels().get(0).getName();
                    break;

                default:
                    throw new AssertionError("Submission artifact is expected to have zero or one reagent labels.");
            }

            // Return a map with the single sample to reagent label mapping.

            sampleReagentMap.put(artifact.getSamples().get(0).getLimsid(), label);
            return;
        }

        // Otherwise, we have a parent process. See if that process is one of the barcoding processes.
        // If it is, the artifact passed in is the output from that process and so the artifact created
        // the reagent label attached. This gives the appropriate label for its sole sample in the starting pool.

        GenologicsProcess process = getProcess(artifact.getParentProcess());

        logger.debug("Loaded process " + process.getLimsid() + " " + process.getProcessType().getName());

        if (BARCODING_PROCESS_NAMES.contains(process.getProcessType().getName()))
        {
            logger.debug(process.getLimsid() + " is one of the barcoding processes.");

            if (artifact.getSamples().size() != 1)
            {
                throw new AssertionError("Input artifact to barcode application step is expected to have exactly one sample.");
            }
            if (artifact.getReagentLabels().size() != 1)
            {
                throw new AssertionError("Input artifact to barcode application step is expected to have exactly one reagent label.");
            }

            sampleReagentMap.put(artifact.getSamples().get(0).getLimsid(),
                                 artifact.getReagentLabels().get(0).getName());
        }
        else
        {
            // Not a barcoding process. So, look at the process's input output maps and where
            // the output artifact of the parent process matches the id of the artifact
            // passed in to the method, look at the history of the corresponding input artifact.

            for (InputOutputMap iomap : process.getInputOutputMaps())
            {
                if (artifactId.equals(iomap.getOutput().getLimsid()))
                {
                    Artifact inputArtifact = getArtifact(iomap.getInput());
                    getReagentLabelsForSamplesInPoolRecursive(inputArtifact, sampleReagentMap);
                }
            }
        }
    }

    /**
     * Find the correct identifier for the given artifact. The history of the artifact
     * is traversed until we find the first artifact with a name that matches the given pattern.
     *
     * <p>This is an expensive operation and while this class tries to minimise reloading
     * artifacts, samples and processes, using the optional caching facility around the API is
     * strongly recommended.</p>
     *
     * @param matchPattern The pattern to use to locate a suitable identifier.
     * @param sampleFieldName An optional field name to use to get a value from the original
     * sample if there is no match in the artifact history.
     * @param artifact The starting artifact.
     *
     * @return The identifier for the artifact. It is possible to get a null back if there
     * is absolutely nothing in the history.
     */
    public String getIdentifierForArtifact(Pattern matchPattern, String sampleFieldName,
                                           LimsEntityLinkable<Artifact> artifact)
    {
        if (matchPattern == null)
        {
            throw new IllegalArgumentException("matchPattern cannot be null");
        }

        checkReady();

        String identifier = null;
        try
        {
            if (artifact != null)
            {
                Artifact realArtifact = getArtifact(artifact);
                identifier = getIdentifierForArtifactRecursive(matchPattern, sampleFieldName, realArtifact);
            }
        }
        finally
        {
            cleanup();
        }

        return identifier;
    }

    /**
     * Adaptation of {@code getIdentifierForArtifact} for a collection of starting
     * artifacts. Running en masse can help with local reloads, but again the use of the caching
     * facility is strongly recommended.
     *
     * @param matchPattern The pattern to use to locate a suitable identifier.
     * @param sampleFieldName An optional field name to use to get a value from the original
     * sample if there is no match in the artifact history.
     * @param artifacts A collection of either the artifacts themselves or links to the artifacts.
     *
     * @return A map of starting artifact id to the identifier found for that artifact. Some of the
     * values in the map may be null if no matching identifier could be found.
     *
     * @see #getIdentifierForArtifact(Pattern, String, LimsEntityLinkable)
     */
    public Map<String, String> getIdentifiersForArtifacts(
            Pattern matchPattern, String sampleFieldName,
            Collection<? extends LimsEntityLinkable<Artifact>> artifacts)
    {
        if (matchPattern == null)
        {
            throw new IllegalArgumentException("matchPattern cannot be null");
        }

        checkReady();

        Map<String, String> identifierMap = new HashMap<String, String>();

        if (artifacts != null)
        {
            try
            {
                for (LimsEntityLinkable<Artifact> linkable : artifacts)
                {
                    if (!identifierMap.containsKey(linkable.getLimsid()))
                    {
                        Artifact artifact = getArtifact(linkable);

                        String identifier = getIdentifierForArtifactRecursive(matchPattern, sampleFieldName, artifact);

                        identifierMap.put(artifact.getLimsid(), identifier);
                    }
                }
            }
            finally
            {
                cleanup();
            }
        }

        return identifierMap;
    }

    /**
     * Recursively traverse from the given Artifact up its process and prior artifact history
     * until we can reliably obtain an identifier for the artifact. This will be the first artifact
     * that has a name that exactly matches the given pattern. If we get to the original
     * artifact and no id is found, we look at the sample and, if the {@code sampleFieldName}
     * is supplied, return the value of that field if there is one.
     *
     * @param matchPattern The pattern to use to locate a suitable identifier.
     * @param sampleFieldName An optional field name to use to get a value from the original
     * sample if there is no match in the artifact history.
     * @param artifact The artifact to search from.
     *
     * @return The identifier for the artifact. It is possible to get a null back if there
     * is absolutely nothing in the history.
     *
     * @see #getFormativeArtifactRecursive(Pattern, boolean, Artifact)
     */
    private String getIdentifierForArtifactRecursive(final Pattern matchPattern, final String sampleFieldName, Artifact artifact)
    {
        Artifact foundArtifact = getFormativeArtifact(matchPattern, true, artifact);

        if (matchPattern.matcher(foundArtifact.getName()).matches())
        {
            return foundArtifact.getName();
        }

        if (foundArtifact.getParentProcess() == null)
        {
            // No parent. See if there is a field on the sample we can use (if supplied).

            if (StringUtils.isNotEmpty(sampleFieldName))
            {
                if (foundArtifact.getSamples().size() != 1)
                {
                    throw new AssertionError("Submission artifact is expected to have exactly one sample.");
                }

                Sample sample = getSample(foundArtifact.getSamples().iterator().next());
                return UDF.getUDFValue(sample, sampleFieldName);
            }
        }

        return null;
    }

    /**
     * Get all the processes created from the given artifact. This is recursive, so will include
     * immediate child processes and artifacts that are the outputs of those processes and so forth.
     *
     * <p>This is an expensive operation and while this class tries to minimise reloading
     * artifacts and processes, using the optional caching facility around the API is
     * strongly recommended.</p>
     *
     * @param artifact The starting artifact.
     *
     * @return A list of processes run on the artifact or its later analytes.
     */
    public List<GenologicsProcess> getProcessesUsingArtifact(LimsEntityLinkable<Artifact> artifact)
    {
        checkReady();

        List<GenologicsProcess> processes = new ArrayList<GenologicsProcess>();
        try
        {
            if (artifact != null)
            {
                Map<String, GenologicsProcess> artifactProcesses = new HashMap<String, GenologicsProcess>();

                Artifact a = getArtifact(artifact);
                findProcessesUsingArtifactRecursive(a, artifactProcesses);

                processes.addAll(artifactProcesses.values());
            }
        }
        finally
        {
            cleanup();
        }

        return processes;
    }

    /**
     * Adaptation of {@code getProcessesUsingArtifact} to fetch the child processes of many
     * artifacts. Running en masse can help with local reloads, but again the use of the caching
     * facility is strongly recommended.
     *
     * @param artifacts The starting artifacts.
     *
     * @return A map of starting artifact LIMS id to a list of processes run on that artifact.
     *
     * @see #getProcessesUsingArtifact(LimsEntityLinkable)
     */
    public Map<String, List<GenologicsProcess>>
    getProcessesUsingArtifacts(Collection<? extends LimsEntityLinkable<Artifact>> artifacts)
    {
        checkReady();

        Map<String, List<GenologicsProcess>> processMap = new HashMap<String, List<GenologicsProcess>>();
        try
        {
            if (artifacts != null)
            {
                Map<String, GenologicsProcess> artifactProcesses = new HashMap<String, GenologicsProcess>();
                for (LimsEntityLinkable<Artifact> artifact : artifacts)
                {
                    if (!processMap.containsKey(artifact.getLimsid()))
                    {
                        Artifact a = getArtifact(artifact);
                        findProcessesUsingArtifactRecursive(a, artifactProcesses);
                        processMap.put(a.getLimsid(), new ArrayList<GenologicsProcess>(artifactProcesses.values()));
                    }
                }
            }
        }
        finally
        {
            cleanup();
        }

        return processMap;
    }

    /**
     * Recursively traverse from the given Artifact down the genealogy looking for processes that
     * used the artifact as one of its input artifacts. Looks for all child processes so looks again for
     * processes from the outputs of the processes initially found.
     *
     * <p>Found processes accumulate in the {@code artifactProcesses} map.</p>
     *
     * @param current The artifact to search from.
     * @param artifactProcesses The map to accumulate found processes in.
     */
    private void findProcessesUsingArtifactRecursive(Artifact current, Map<String, GenologicsProcess> artifactProcesses)
    {
        final String currentLimsId = current.getLimsid();

        logger.debug("Looking at artifact " + currentLimsId + " " + current.getName());

        Map<String, Object> searchTerms = new HashMap<String, Object>();
        searchTerms.put("inputartifactlimsid", current.getLimsid());

        List<LimsLink<GenologicsProcess>> processes = api.find(searchTerms, GenologicsProcess.class);

        Map<String, ArtifactLink> outputs = new HashMap<String, ArtifactLink>();
        List<LimsLink<Artifact>> missingArtifacts = new ArrayList<LimsLink<Artifact>>(100);

        for (LimsLink<GenologicsProcess> plink : processes)
        {
            LimsEntityLink<GenologicsProcess> elink = (LimsEntityLink<GenologicsProcess>)plink;

            if (!artifactProcesses.containsKey(elink.getLimsid()))
            {
                GenologicsProcess p = getProcess(elink);

                artifactProcesses.put(p.getLimsid(), p);

                logger.debug("Handling process " + p.getLimsid() + " " + p.getProcessType().getName());

                outputs.clear();
                for (InputOutputMap iomap : p.getInputOutputMaps())
                {
                    ArtifactLink output = iomap.getOutput();
                    if (output != null && currentLimsId.equals(iomap.getInput().getLimsid()))
                    {
                        assert output.getOutputType() != null
                               : "Artifact " + output.getUri() + " has no output type set.";

                        switch (output.getOutputType())
                        {
                            case ANALYTE:
                            case SAMPLE:
                                outputs.put(output.getLimsid(), output);
                                break;
                        }
                    }
                }

                // This little chunk means we only fetch artifacts not already loaded.
                missingArtifacts.clear();
                for (ArtifactLink alink : outputs.values())
                {
                    if (knownArtifacts.get() == null || !knownArtifacts.get().containsKey(alink.getLimsid()))
                    {
                        missingArtifacts.add(alink);
                    }
                }
                recordArtifacts(api.loadAll(missingArtifacts));

                // At this point, knownArtifacts must contain all the artifacts needed.
                // So the getArtifact call will always fetch from the map.
                for (ArtifactLink alink : outputs.values())
                {
                    findProcessesUsingArtifactRecursive(getArtifact(alink), artifactProcesses);
                }
            }
        }
    }

    /**
     * Check whether the given artifact is derived at some point
     * in its history from a parent artifact.
     *
     * @param parentArtifactLimsId The LIMS id of the parent artifact.
     * @param artifact The artifact to check.
     *
     * @return {@code true} if {@code artifact} is derived from the
     * artifact given by {@code parentArtifactLimsId}, {@code false} if not.
     */
    public boolean isDerivedFrom(String parentArtifactLimsId, LimsEntityLinkable<Artifact> artifact)
    {
        if (StringUtils.isEmpty(parentArtifactLimsId))
        {
            throw new IllegalArgumentException("parentArtifactLimsId cannot be empty");
        }
        if (artifact == null || artifact.getLimsid() == null)
        {
            throw new IllegalArgumentException("artifact cannot be null, and must have a LIMS id set");
        }

        checkReady();

        boolean derived;
        try
        {
            Artifact a = getArtifact(artifact);

            derived = recursiveArtifactMatch(parentArtifactLimsId, a.getLimsid(), a);
        }
        finally
        {
            cleanup();
        }

        return derived;
    }

    /**
     * Filter from the given collection of artifacts those that were derived from the
     * artifact whose LIMS id is given.
     *
     * @param parentArtifactLimsId The LIMS id of the parent artifact.
     * @param artifacts The artifacts possibly derived from the parent artifact.
     *
     * @return A collection of artifacts from {@code artifacts} that were derived
     * from the given parent.
     */
    public Collection<Artifact>
    filterArtifactsDerivedFrom(final String parentArtifactLimsId,
                               Collection<? extends LimsEntityLinkable<Artifact>> artifacts)
    {
        if (StringUtils.isEmpty(parentArtifactLimsId))
        {
            throw new IllegalArgumentException("parentArtifactLimsId cannot be empty");
        }

        checkReady();

        List<Artifact> derivedArtifacts = new ArrayList<Artifact>(artifacts.size());

        try
        {
            Set<String> handledArtifacts = new HashSet<String>();

            for (LimsEntityLinkable<Artifact> link : artifacts)
            {
                if (link != null && !handledArtifacts.contains(link.getLimsid()))
                {
                    Artifact sourceArtifact = getArtifact(link);

                    if (recursiveArtifactMatch(parentArtifactLimsId, sourceArtifact.getLimsid(), sourceArtifact))
                    {
                        derivedArtifacts.add(sourceArtifact);
                    }

                    handledArtifacts.add(sourceArtifact.getLimsid());
                }
            }
        }
        finally
        {
            cleanup();
        }

        return derivedArtifacts;
    }

    /**
     * Filter from the given collection of artifacts those that were derived from the
     * artifacts whose LIMS ids are given.
     *
     * @param parentArtifactLimsIds The set of LIMS ids for parent artifacts.
     * @param artifacts The artifacts possibly derived from the parent artifact.
     *
     * @return A map of parent artifact LIMS id (from {@code parentArtifactLimsIds})
     * to collection of artifacts from {@code artifacts} that were derived
     * from each parent.
     */
    public Map<String, Collection<Artifact>>
    filterArtifactsDerivedFrom(Collection<String> parentArtifactLimsIds,
                               Collection<? extends LimsEntityLinkable<Artifact>> artifacts)
    {
        if (parentArtifactLimsIds == null)
        {
            throw new IllegalArgumentException("parentArtifactLimsIds cannot be null");
        }
        for (String parentArtifactLimsId : parentArtifactLimsIds)
        {
            if (StringUtils.isEmpty(parentArtifactLimsId))
            {
                throw new IllegalArgumentException("parentArtifactLimsIds cannot contain null or empty values");
            }
        }

        checkReady();

        if (!(parentArtifactLimsIds instanceof Set))
        {
            parentArtifactLimsIds = new HashSet<String>(parentArtifactLimsIds);
        }

        Map<String, Collection<Artifact>> derivedArtifactMap = new HashMap<String, Collection<Artifact>>();

        try
        {
            Set<String> handledArtifacts = new HashSet<String>();

            for (String parentArtifactLimsId : parentArtifactLimsIds)
            {
                handledArtifacts.clear();
                List<Artifact> derivedArtifacts = new ArrayList<Artifact>(artifacts.size());

                for (LimsEntityLinkable<Artifact> link : artifacts)
                {
                    if (link != null && !handledArtifacts.contains(link.getLimsid()))
                    {
                        Artifact sourceArtifact = getArtifact(link);

                        if (recursiveArtifactMatch(parentArtifactLimsId, sourceArtifact.getLimsid(), sourceArtifact))
                        {
                            derivedArtifacts.add(sourceArtifact);
                        }

                        handledArtifacts.add(sourceArtifact.getLimsid());
                    }
                }

                derivedArtifactMap.put(parentArtifactLimsId, derivedArtifacts);
            }
        }
        finally
        {
            cleanup();
        }

        return derivedArtifactMap;
    }

    /**
     * Helper to the {@code filterArtifactsDerivedFrom} method, this checks whether
     * the given artifact is the same artifact as the parent (indicating a match) or if not
     * searches earlier in the process tree until either there is a match or there is no
     * parent process.
     *
     * @param parentArtifactLimsId The LIMS id of the expected parent artifact.
     * @param sourceArtifactLimsId The LIMS id of the latest artifact being checked.
     * @param a The current Artifact object being tested.
     *
     * @return {@code true} if {@code a}'s LIMS id matches {@code parentArtifactLimsId}
     * or if an earlier artifact from which {@code a} is derived does,
     * {@code false} if not.
     */
    private boolean recursiveArtifactMatch(final String parentArtifactLimsId, final String sourceArtifactLimsId, Artifact a)
    {
        if (a != null)
        {
            if (parentArtifactLimsId.equals(a.getLimsid()))
            {
                return true;
            }
            else if (a.getParentProcess() != null)
            {
                GenologicsProcess p = getProcess(a.getParentProcess());

                for (InputOutputMap iomap : p.getInputOutputMaps())
                {
                    if (a.getLimsid().equals(iomap.getOutput().getLimsid()))
                    {
                        if (recursiveArtifactMatch(parentArtifactLimsId, sourceArtifactLimsId, getArtifact(iomap.getInput())))
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    /**
     * Find the artifact that the given artifact was derived from whose name matches the
     * given pattern. Optionally, if there is no match, return the original artifact created at
     * sample submission.
     *
     * <p>This is an expensive operation and while this class tries to minimise reloading
     * artifacts, samples and processes, using the optional caching facility around the API is
     * strongly recommended.</p>
     *
     * @param nameMatchPattern The pattern to use to locate a formative artifact by name.
     * @param includeSampleArtifact Whether to return the original sample artifact if no later
     * one was found by name.
     * @param artifact The starting artifact.
     *
     * @return The formative artifact from which {@code artifact} was derived.
     * It is possible to get a null back if there is absolutely nothing in the history and
     * {@code includeSampleArtifact} is {@code false}.
     */
    public Artifact getFormativeArtifact(Pattern nameMatchPattern, boolean includeSampleArtifact,
                                         LimsEntityLinkable<Artifact> artifact)
    {
        if (nameMatchPattern == null)
        {
            throw new IllegalArgumentException("nameMatchPattern cannot be null");
        }

        checkReady();

        Artifact formative = null;
        try
        {
            if (artifact != null)
            {
                Artifact realArtifact = getArtifact(artifact);
                formative = getFormativeArtifactRecursive(nameMatchPattern, includeSampleArtifact, realArtifact);
            }
        }
        finally
        {
            cleanup();
        }

        return formative;
    }

    /**
     * Adaptation of {@code getFormativeArtifact} for a collection of starting
     * artifacts. Running en masse can help with local reloads, but again the use of the caching
     * facility is strongly recommended.
     *
     * @param nameMatchPattern The pattern to use to locate a formative artifact by name.
     * @param includeSampleArtifact Whether to return the original sample artifact if no later
     * one was found by name.
     * @param artifacts A collection of either the artifacts themselves or links to the artifacts.
     *
     * @return A map of starting artifact id to the formative artifact found for that artifact. Some of the
     * values in the map may be null if there is nothing in the history for a given artifact and
     * {@code includeSampleArtifact} is {@code false}.
     *
     * @see #getFormativeArtifact(Pattern, boolean, LimsEntityLinkable)
     */
    public Map<String, Artifact> getFormativeArtifacts(
            Pattern nameMatchPattern, boolean includeSampleArtifact,
            Collection<? extends LimsEntityLinkable<Artifact>> artifacts)
    {
        if (nameMatchPattern == null)
        {
            throw new IllegalArgumentException("nameMatchPattern cannot be null");
        }

        checkReady();

        Map<String, Artifact> artifactMap = new HashMap<String, Artifact>();

        if (artifacts != null)
        {
            try
            {
                for (LimsEntityLinkable<Artifact> linkable : artifacts)
                {
                    if (!artifactMap.containsKey(linkable.getLimsid()))
                    {
                        Artifact artifact = getArtifact(linkable);

                        Artifact formative = getFormativeArtifactRecursive(nameMatchPattern, includeSampleArtifact, artifact);

                        artifactMap.put(artifact.getLimsid(), formative);
                    }
                }
            }
            finally
            {
                cleanup();
            }
        }

        return artifactMap;
    }

    /**
     * Recursively traverse from the given Artifact up its process and prior artifact history
     * until we can reliably obtain formative artifact for the given artifact. This will be the first artifact
     * that has a name that exactly matches the given pattern. If we get to the original
     * artifact and no id is found, then that original artifact is returned if {@code includeSampleArtifact}
     * is true, or {@code null} if {@code includeSampleArtifact} is false.
     *
     * @param matchPattern The pattern to use to locate a formative artifact by name.
     * @param includeSampleArtifact Whether to return the original sample artifact if no later
     * one was found by name.
     * @param artifact The artifact to search from.
     *
     * @return The formative artifact for {@code artifact}. It is possible to get a null back if there
     * is no artifact matching the name pattern in the history and {@code includeSampleArtifact} is false.
     */
    private Artifact getFormativeArtifactRecursive(final Pattern matchPattern, final boolean includeSampleArtifact, Artifact artifact)
    {
        Artifact formative = null;

        if (matchPattern.matcher(artifact.getName()).matches())
        {
            formative = artifact;
        }
        else
        {
            if (artifact.getParentProcess() == null)
            {
                // No parent. This must be the sample's initial artifact, created at submission.
                formative = includeSampleArtifact ? artifact : null;
            }
            else
            {
                GenologicsProcess parentProcess = getProcess(artifact.getParentProcess());

                for (InputOutputMap iomap : parentProcess.getInputOutputMaps())
                {
                    if (iomap.getOutput().getLimsid().equals(artifact.getLimsid()))
                    {
                        formative = getFormativeArtifactRecursive(matchPattern, includeSampleArtifact, getArtifact(iomap.getInput()));
                        if (formative != null)
                        {
                            break;
                        }
                    }
                }
            }
        }

        return formative;
    }

}
