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

package org.cruk.genologics.api.unittests;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({
    "classpath:/org/cruk/genologics/api/genologics-cache-context.xml",
    "classpath:/unittest-cache-context.xml"
})
public class ClarityClientCacheTestConfiguration extends ClarityClientTestConfiguration
{
    public ClarityClientCacheTestConfiguration()
    {
    }
}
