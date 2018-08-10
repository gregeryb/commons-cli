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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ValueTest
{
    private CommandLine _cl = null;
    private final Options opts = new Options();

    @Before
    public void setUp() throws Exception
    {
        opts.addOption("a", false, "toggle -a");
        opts.addOption("b", true, "set -b");
        opts.addOption("c", "c", false, "toggle -c");
        opts.addOption("d", "d", true, "set -d");

        opts.addOption(Option.builder("e").hasArg().optionalArg(true).build());
        opts.addOption(Option.builder().hasArg().optionalArg(true).longOpt("fish").build());
        opts.addOption(Option.builder().optionalArg(true).hasArgs().longOpt("gravy").build());
        opts.addOption(Option.builder().optionalArg(true).numberOfArgs(2).longOpt("hide").build());
        opts.addOption(Option.builder("i").optionalArg(true).numberOfArgs(2).build( ));
        opts.addOption(Option.builder("j").optionalArg(true).hasArgs().build( ));

        final String[] args = new String[] { "-a",
            "-b", "foo",
            "--c",
            "--d", "bar" 
        };

        final DefaultParser parser = new DefaultParser();
        _cl = parser.parse(opts,args);
    }

    @Test
    public void testShortNoArg()
    {
        assertTrue( _cl.hasOption("a") );
        assertNull( _cl.getOptionValue("a") );
    }
    
    @Test
    public void testShortNoArgWithOption()
    {
        assertTrue( _cl.hasOption(opts.getOption("a")) );
        assertNull( _cl.getOptionValue(opts.getOption("a")) );
    }

    @Test
    public void testShortWithArg()
    {
        assertTrue( _cl.hasOption("b") );
        assertNotNull( _cl.getOptionValue("b") );
        assertEquals( _cl.getOptionValue("b"), "foo");
    }
    
    @Test
    public void testShortWithArgWithOption()
    {
        assertTrue( _cl.hasOption(opts.getOption("b")) );
        assertNotNull( _cl.getOptionValue(opts.getOption("b")) );
        assertEquals( _cl.getOptionValue(opts.getOption("b")), "foo");
    }

    @Test
    public void testLongNoArg()
    {
        assertTrue( _cl.hasOption("c") );
        assertNull( _cl.getOptionValue("c") );
    }
    
    @Test
    public void testLongNoArgWithOption()
    {
        assertTrue( _cl.hasOption(opts.getOption("c")) );
        assertNull( _cl.getOptionValue(opts.getOption("c")) );
    }

    @Test
    public void testLongWithArg()
    {
        assertTrue( _cl.hasOption("d") );
        assertNotNull( _cl.getOptionValue("d") );
        assertEquals( _cl.getOptionValue("d"), "bar");
    }
    
    @Test
    public void testLongWithArgWithOption()
    {
        assertTrue( _cl.hasOption(opts.getOption("d")) );
        assertNotNull( _cl.getOptionValue(opts.getOption("d")) );
        assertEquals( _cl.getOptionValue(opts.getOption("d")), "bar");
    }

    @Test
    public void testShortOptionalArgNoValue() throws Exception
    {
        final String[] args = new String[] { "-e" };

        final DefaultParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption("e") );
        assertNull( cmd.getOptionValue("e") );
    }
    
    @Test
    public void testShortOptionalArgNoValueWithOption() throws Exception
    {
        final String[] args = new String[] { "-e" };

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption(opts.getOption("e")) );
        assertNull( cmd.getOptionValue(opts.getOption("e")) );
    }

    @Test
    public void testShortOptionalArgValue() throws Exception
    {
        final String[] args = new String[] { "-e", "everything" };

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption("e") );
        assertEquals( "everything", cmd.getOptionValue("e") );
    }
    
    @Test
    public void testShortOptionalArgValueWithOption() throws Exception
    {
        final String[] args = new String[] { "-e", "everything" };

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption(opts.getOption("e")) );
        assertEquals( "everything", cmd.getOptionValue(opts.getOption("e")) );
    }

    @Test
    public void testLongOptionalNoValue() throws Exception
    {
        final String[] args = new String[] { "--fish" };

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption("fish") );
        assertNull( cmd.getOptionValue("fish") );
    }
    
    @Test
    public void testLongOptionalNoValueWithOption() throws Exception
    {
        final String[] args = new String[] { "--fish" };

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption(opts.getOption("fish")) );
        assertNull( cmd.getOptionValue(opts.getOption("fish")) );
    }

    @Test
    public void testLongOptionalArgValue() throws Exception
    {
        final String[] args = new String[] { "--fish", "face" };

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption("fish") );
        assertEquals( "face", cmd.getOptionValue("fish") );
    }
    
    @Test
    public void testLongOptionalArgValueWithOption() throws Exception
    {
        final String[] args = new String[] { "--fish", "face" };

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption(opts.getOption("fish")) );
        assertEquals( "face", cmd.getOptionValue(opts.getOption("fish")) );
    }

    @Test
    public void testShortOptionalArgValues() throws Exception
    {
        final String[] args = new String[] { "-j", "ink", "idea" };

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption("j") );
        assertEquals( "ink", cmd.getOptionValue("j") );
        assertEquals("ink", cmd.getOptionValues("j").get(0));
        assertEquals("idea", cmd.getOptionValues("j").get(1));
        assertEquals( cmd.getArgs().length, 0 );
    }
    
    @Test
    public void testShortOptionalArgValuesWithOption() throws Exception
    {
        final String[] args = new String[] { "-j", "ink", "idea" };

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption(opts.getOption("j")) );
        assertEquals( "ink", cmd.getOptionValue(opts.getOption("j")) );
        assertEquals("ink", cmd.getOptionValues(opts.getOption("j")).get(0));
        assertEquals("idea", cmd.getOptionValues(opts.getOption("j")).get(1));
        assertEquals( cmd.getArgs().length, 0 );
    }

    @Test
    public void testLongOptionalArgValues() throws Exception
    {
        final String[] args = new String[] { "--gravy", "gold", "garden" };

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption("gravy") );
        assertEquals( "gold", cmd.getOptionValue("gravy") );
        assertEquals("gold", cmd.getOptionValues("gravy").get(0));
        assertEquals("garden", cmd.getOptionValues("gravy").get(1));
        assertEquals( cmd.getArgs().length, 0 );
    }
    
    @Test
    public void testLongOptionalArgValuesWithOption() throws Exception
    {
        final String[] args = new String[] { "--gravy", "gold", "garden" };

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption(opts.getOption("gravy")) );
        assertEquals( "gold", cmd.getOptionValue(opts.getOption("gravy")) );
        assertEquals("gold", cmd.getOptionValues(opts.getOption("gravy")).get(0));
        assertEquals("garden", cmd.getOptionValues(opts.getOption("gravy")).get(1));
        assertEquals( cmd.getArgs().length, 0 );
    }

    @Test
    public void testShortOptionalNArgValues() throws Exception
    {
        final String[] args = new String[] { "-i", "ink", "idea", "isotope", "ice" };

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption("i") );
        assertEquals( "ink", cmd.getOptionValue("i") );
        assertEquals("ink", cmd.getOptionValues("i").get(0));
        assertEquals("idea", cmd.getOptionValues("i").get(1));
        assertEquals( cmd.getArgs().length, 2 );
        assertEquals( "isotope", cmd.getArgs()[0] );
        assertEquals( "ice", cmd.getArgs()[1] );
    }
    
    @Test
    public void testShortOptionalNArgValuesWithOption() throws Exception
    {
        final String[] args = new String[] { "-i", "ink", "idea", "isotope", "ice" };

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption("i") );
        assertEquals( "ink", cmd.getOptionValue(opts.getOption("i")) );
        assertEquals("ink", cmd.getOptionValues(opts.getOption("i")).get(0));
        assertEquals("idea", cmd.getOptionValues(opts.getOption("i")).get(1));
        assertEquals( cmd.getArgs().length, 2 );
        assertEquals( "isotope", cmd.getArgs()[0] );
        assertEquals( "ice", cmd.getArgs()[1] );
    }

    @Test
    public void testLongOptionalNArgValues() throws Exception
    {
        final String[] args = new String[] { 
            "--hide", "house", "hair", "head"
        };

        final CommandLineParser parser = new DefaultParser();

        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption("hide") );
        assertEquals( "house", cmd.getOptionValue("hide") );
        assertEquals("house", cmd.getOptionValues("hide").get(0));
        assertEquals("hair", cmd.getOptionValues("hide").get(1));
        assertEquals( cmd.getArgs().length, 1 );
        assertEquals( "head", cmd.getArgs()[0] );
    }
    
    @Test
    public void testLongOptionalNArgValuesWithOption() throws Exception
    {
        final String[] args = new String[] { 
            "--hide", "house", "hair", "head"
        };

        final CommandLineParser parser = new DefaultParser();

        final CommandLine cmd = parser.parse(opts,args);
        assertTrue( cmd.hasOption(opts.getOption("hide")) );
        assertEquals( "house", cmd.getOptionValue(opts.getOption("hide")) );
        assertEquals("house", cmd.getOptionValues(opts.getOption("hide")).get(0));
        assertEquals("hair", cmd.getOptionValues(opts.getOption("hide")).get(1));
        assertEquals( cmd.getArgs().length, 1 );
        assertEquals( "head", cmd.getArgs()[0] );
    }
}
