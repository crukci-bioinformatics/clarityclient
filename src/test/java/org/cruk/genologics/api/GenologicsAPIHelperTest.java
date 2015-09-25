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

package org.cruk.genologics.api;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genologics.ri.sample.Sample;

public class GenologicsAPIHelperTest
{
    ConfigurableApplicationContext context;
    GenologicsAPI api;

    public GenologicsAPIHelperTest() throws MalformedURLException
    {
        context = new ClassPathXmlApplicationContext("/org/cruk/genologics/api/genologics-client-context.xml");

        api = context.getBean("genologicsAPI", GenologicsAPI.class);
    }

    @Override
    protected void finalize()
    {
        context.close();
    }

    @Test
    public void testToEntityMap()
    {
        List<Sample> samples = new ArrayList<Sample>();
        for (int num = 1; num <= 4; num++)
        {
            Sample s  = new Sample();
            s.setLimsid(Integer.toString(num));
            samples.add(s);
        }

        Map<String, Sample> sampleMap;

        // Test with a normal collection.

        sampleMap = api.toEntityMap(samples);

        assertEquals("Wrong number of samples in map", 4, sampleMap.size());

        for (int num = 1; num <= 4; num++)
        {
            assertSame("Sample with id " + num + " does not match",
                       samples.get(num - 1),
                       sampleMap.get(Integer.toString(num)));
        }

        // Repeat, but set a duplicate id. The last in the collection should win.

        samples.get(1).setLimsid("4");

        sampleMap = api.toEntityMap(samples);

        assertEquals("Wrong number of samples in map", 3, sampleMap.size());

        assertNull("There should be no sample 2", sampleMap.get("2"));

        assertSame("Sample 4 should be the last in the list", samples.get(3), sampleMap.get("4"));

        // Test with a null lims id.

        samples.get(1).setLimsid(null);

        try
        {
            api.toEntityMap(samples);
            fail("Performed toEntityMap with a sample with a null lims id.");
        }
        catch (IllegalArgumentException e)
        {
            // Correct.
        }

        // Test with a null collection.

        sampleMap = api.toEntityMap((List<Sample>)null);

        assertNotNull("Sample map is null from a null collection", sampleMap);
        assertEquals("Wrong number of samples in map", 0, sampleMap.size());
    }
}
