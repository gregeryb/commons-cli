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

package org.apache.commons.cli;

import java.util.Arrays;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ValuesTest
{
    private CommandLine cmd;

    @Before
    public void setUp() throws Exception
    {
        final Options options = new Options();

        options.addOption("a", false, "toggle -a");
        options.addOption("b", true, "set -b");
        options.addOption("c", "c", false, "toggle -c");
        options.addOption("d", "d", true, "set -d");

        options.addOption(Option.builder("e").longOpt("e").hasArgs().desc("set -e ").build());
        options.addOption("f", "f", false, "jk");
        options.addOption(Option.builder("g").longOpt("g").numberOfArgs(2).desc("set -g").build());
        options.addOption(Option.builder("h").longOpt("h").hasArg().desc("set -h").build());
        options.addOption(Option.builder("i").longOpt("i").desc("set -i").build());
        options.addOption(Option.builder("j").longOpt("j").hasArgs().desc("set -j").valueSeparator('=').build());
        options.addOption(Option.builder("k").longOpt("k").hasArgs().desc("set -k").valueSeparator('=').build());
        options.addOption(Option.builder("m").longOpt("m").hasArgs().desc("set -m").valueSeparator().build());

        final String[] args = new String[] { "-a",
                                       "-b", "foo",
                                       "--c",
                                       "--d", "bar",
                                       "-e", "one", "two",
                                       "-f",
                                       "arg1", "arg2",
                                       "-g", "val1", "val2" , "arg3",
                                       "-h", "val1", "-i",
                                       "-h", "val2",
                                       "-jkey=value",
                                       "-j", "key=value",
                                       "-kkey1=value1", 
                                       "-kkey2=value2",
                                       "-mkey=value"};

        final CommandLineParser parser = new DefaultParser();

        cmd = parser.parse(options,args);
    }

    @Test
    public void testShortArgs()
    {
        assertTrue("Option a is not set", cmd.hasOption("a"));
        assertTrue("Option c is not set", cmd.hasOption("c"));

        assertEquals(0,cmd.getOptionValues("a").size());
        assertEquals(0,cmd.getOptionValues("c").size());
    }

    @Test
    public void testShortArgsWithValue()
    {
        assertTrue("Option b is not set", cmd.hasOption("b"));
        assertTrue(cmd.getOptionValue("b").equals("foo"));
        assertEquals(1, cmd.getOptionValues("b").size());

        assertTrue("Option d is not set", cmd.hasOption("d"));
        assertTrue(cmd.getOptionValue("d").equals("bar"));
        assertEquals(1, cmd.getOptionValues("d").size());
    }

    @Test
    public void testMultipleArgValues()
    {
        assertTrue("Option e is not set", cmd.hasOption("e"));
        assertTrue(Arrays.asList("one", "two" ).equals( cmd.getOptionValues("e")));
    }

    @Test
    public void testTwoArgValues()
    {
        assertTrue("Option g is not set", cmd.hasOption("g"));
        assertTrue(Arrays.asList( "val1", "val2" ).equals( cmd.getOptionValues("g")));
    }

    @Test
    public void testComplexValues()
    {
        assertTrue("Option i is not set", cmd.hasOption("i"));
        assertTrue("Option h is not set", cmd.hasOption("h"));
        assertTrue(Arrays.asList( "val1", "val2" ).equals(cmd.getOptionValues("h")));
    }

    @Test
    public void testExtraArgs()
    {
        assertArrayEquals("Extra args", new String[] { "arg1", "arg2", "arg3" }, cmd.getArgs());
    }

    @Test
    public void testCharSeparator()
    {
        // tests the char methods of CommandLine that delegate to the String methods
        assertTrue("Option j is not set", cmd.hasOption("j"));
        assertTrue("Option j is not set", cmd.hasOption('j'));
        assertTrue(Arrays.asList("key", "value", "key", "value" ).equals(cmd.getOptionValues("j")));
        assertTrue(Arrays.asList("key", "value", "key", "value" ).equals(cmd.getOptionValues("j")));

        assertTrue("Option k is not set", cmd.hasOption("k"));
        assertTrue("Option k is not set", cmd.hasOption('k'));
        assertTrue(Arrays.asList("key1", "value1", "key2", "value2" ).equals(cmd.getOptionValues("k")));
        assertTrue(Arrays.asList("key1", "value1", "key2", "value2" ).equals(cmd.getOptionValues("k")));

        assertTrue("Option m is not set", cmd.hasOption("m"));
        assertTrue("Option m is not set", cmd.hasOption('m'));
        assertTrue(Arrays.asList("key", "value").equals(cmd.getOptionValues("m")));
        assertTrue(Arrays.asList("key", "value").equals(cmd.getOptionValues("m")));
    }

    /**
     * jkeyes - commented out this test as the new architecture
     * breaks this type of functionality.  I have left the test 
     * here in case I get a brainwave on how to resolve this.
     */
    /*
    public void testGetValue()
    {
        // the 'm' option
        assertTrue( _option.getValues().length == 2 );
        assertEquals( _option.getValue(), "key" );
        assertEquals( _option.getValue( 0 ), "key" );
        assertEquals( _option.getValue( 1 ), "value" );

        try {
            assertEquals( _option.getValue( 2 ), "key" );
            fail( "IndexOutOfBounds not caught" );
        }
        catch( IndexOutOfBoundsException exp ) {
            
        }

        try {
            assertEquals( _option.getValue( -1 ), "key" );
            fail( "IndexOutOfBounds not caught" );
        }
        catch( IndexOutOfBoundsException exp ) {

        }
    }
    */
}
