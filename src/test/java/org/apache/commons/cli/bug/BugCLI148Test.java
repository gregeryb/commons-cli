/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.cli.bug;

import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

/**
 * http://issues.apache.org/jira/browse/CLI-148
 */
@SuppressWarnings("deprecation") // tests some deprecated classes
public class BugCLI148Test
{    
    private Options options;

    @Before
    public void setUp() throws Exception
    {
        options = new Options();
        options.addOption(Option.builder("t").hasArg().build());
        options.addOption(Option.builder("s").hasArg().build());
    }

    @Test
    public void testWorkaround1() throws Exception
    {
        final CommandLineParser parser = new DefaultParser();
        final String[] args = new String[]{ "-t-something" };

        final CommandLine commandLine = parser.parse(options, args);
        assertEquals("-something", commandLine.getOptionValue('t'));
    }

    @Test
    public void testWorkaround2() throws Exception
    {
        final CommandLineParser parser = new DefaultParser();
        final String[] args = new String[]{ "-t", "\"-something\"" };

        final CommandLine commandLine = parser.parse(options, args);
        assertEquals("-something", commandLine.getOptionValue('t'));
    }
}
