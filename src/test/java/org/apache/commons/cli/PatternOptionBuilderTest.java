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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.junit.Test;

/** 
 * Test case for the PatternOptionBuilder class.
 */
public class PatternOptionBuilderTest
{
    @Test
    public void testSimplePattern() throws Exception
    {
        final Options options = PatternOptionBuilder.parsePattern("a:b@cde>f+n%t/m*z#");
        final String[] args = new String[] {"-c", "-a", "foo", "-b", "java.util.Vector", "-e", "build.xml", "-f", "java.util.Calendar", "-n", "4.5", "-t", "http://commons.apache.org", "-z", "Thu Jun 06 17:48:57 EDT 2002", "-m", "test*"};

        final CommandLineParser parser = new DefaultParser();
        final CommandLine line = parser.parse(options, args);

        assertEquals("flag a", "foo", line.getOptionValue("a"));
        assertEquals("string flag a", "foo", line.getParsedOptionValue("a"));
        assertEquals("object flag b", new Vector<Object>(), line.getParsedOptionValue("b"));
        assertTrue("boolean true flag c", line.hasOption("c"));
        assertFalse("boolean false flag d", line.hasOption("d"));
        assertEquals("file flag e", new File("build.xml"), line.getParsedOptionValue("e"));
        assertEquals("class flag f", Calendar.class, line.getParsedOptionValue("f"));
        assertEquals("number flag n", new Double(4.5), line.getParsedOptionValue("n"));
        assertEquals("url flag t", new URL("http://commons.apache.org"), line.getParsedOptionValue("t"));

        // tests the char methods of CommandLine that delegate to the String methods
        assertEquals("flag a", "foo", line.getOptionValue('a'));
        assertEquals("string flag a", "foo", line.getParsedOptionValue('a'));
        assertEquals("object flag b", new Vector<Object>(), line.getParsedOptionValue('b'));
        assertTrue("boolean true flag c", line.hasOption('c'));
        assertFalse("boolean false flag d", line.hasOption('d'));
        assertEquals("file flag e", new File("build.xml"), line.getParsedOptionValue('e'));
        assertEquals("class flag f", Calendar.class, line.getParsedOptionValue('f'));
        assertEquals("number flag n", new Double(4.5), line.getParsedOptionValue('n'));
        assertEquals("url flag t", new URL("http://commons.apache.org"), line.getParsedOptionValue('t'));

        // FILES NOT SUPPORTED YET
        try {
            assertEquals("files flag m", new File[0], line.getParsedOptionValue('m'));
            fail("Multiple files are not supported yet, should have failed");
        } catch(final UnsupportedOperationException uoe) {
            // expected
        }

        // DATES NOT SUPPORTED YET
        try {
            assertEquals("date flag z", new Date(1023400137276L), line.getParsedOptionValue('z'));
            fail("Date is not supported yet, should have failed");
        } catch(final UnsupportedOperationException uoe) {
            // expected
        }
    }

    @Test
    public void testEmptyPattern() throws Exception
    {
        final Options options = PatternOptionBuilder.parsePattern("");
        assertTrue(options.getOptions().isEmpty());
    }

    @Test
    public void testUntypedPattern() throws Exception
    {
        final Options options = PatternOptionBuilder.parsePattern("abc");
        final CommandLineParser parser = new DefaultParser();
        final CommandLine line = parser.parse(options, new String[] { "-abc" });

        assertTrue(line.hasOption('a'));
        assertNull("value a", line.getParsedOptionValue('a'));
 
        assertTrue(line.hasOption('b'));
        assertNull("value b", line.getParsedOptionValue('b'));

        assertTrue(line.hasOption('c'));
        assertNull("value c", line.getParsedOptionValue('c'));
    }

    @Test
    public void testNumberPattern() throws Exception
    {
        final Options options = PatternOptionBuilder.parsePattern("n%d%x%");
        final CommandLineParser parser = new DefaultParser();
        final CommandLine line = parser.parse(options, new String[] { "-n", "1", "-d", "2.1", "-x", "3,5" });

        assertEquals("n object class", Long.class, line.getParsedOptionValue("n").getClass());
        assertEquals("n value", new Long(1), line.getParsedOptionValue("n"));

        assertEquals("d object class", Double.class, line.getParsedOptionValue("d").getClass());
        assertEquals("d value", new Double(2.1), line.getParsedOptionValue("d"));

        
        try{
          assertNull("x object", line.getParsedOptionValue("x"));
          fail("failed to throw ParseException");
        }
        catch (ParseException ex)
        {
         // success
        }
    }

    @Test
    public void testClassPattern() throws Exception
    {
        final Options options = PatternOptionBuilder.parsePattern("c+d+");
        final CommandLineParser parser = new DefaultParser();
        final CommandLine line = parser.parse(options, new String[] { "-c", "java.util.Calendar", "-d", "System.DateTime" });

        assertEquals("c value", Calendar.class, line.getParsedOptionValue("c"));

        try{
          assertNull("d value", line.getParsedOptionValue("d"));
          fail("failed to throw ParseException");
        }
        catch (ParseException ex)
        {
         // success
        }    
    }

    @Test
    public void testObjectPattern() throws Exception
    {
        final Options options = PatternOptionBuilder.parsePattern("o@i@n@");
        final CommandLineParser parser = new DefaultParser();
        final CommandLine line = parser.parse(options, new String[] { "-o", "java.lang.String", "-i", "java.util.Calendar", "-n", "System.DateTime" });

        assertEquals("o value", "", line.getParsedOptionValue("o"));
        try{
          assertNull("i value", line.getParsedOptionValue("i"));
          fail("failed to throw ParseException");
        }
        catch (ParseException ex)
        {
         // success
        }
        
        try{
          assertNull("n value", line.getParsedOptionValue("n"));
          fail("failed to throw ParseException");
        }
        catch (ParseException ex)
        {
         // success
        }
    }

    @Test
    public void testURLPattern() throws Exception
    {
        final Options options = PatternOptionBuilder.parsePattern("u/v/");
        final CommandLineParser parser = new DefaultParser();
        final CommandLine line = parser.parse(options, new String[] { "-u", "http://commons.apache.org", "-v", "foo://commons.apache.org" });

        assertEquals("u value", new URL("http://commons.apache.org"), line.getParsedOptionValue("u"));
        try{
          line.getParsedOptionValue("v");
          fail("failed to throw ParseException");
        }
        catch (ParseException ex)
        {
         // success
        }
    }

    @Test
    public void testExistingFilePattern() throws Exception
    {
        final Options options = PatternOptionBuilder.parsePattern("g<");
        final CommandLineParser parser = new DefaultParser();
        final CommandLine line = parser.parse(options, new String[] { "-g", "src/test/resources/existing-readable.file" });

        final Object parsedReadableFileStream = line.getParsedOptionValue("g");

        assertNotNull("option g not parsed", parsedReadableFileStream);
        assertTrue("option g not FileInputStream", parsedReadableFileStream instanceof FileInputStream);
    }

    @Test
    public void testExistingFilePatternFileNotExist() throws Exception {
        final Options options = PatternOptionBuilder.parsePattern("f<");
        final CommandLineParser parser = new DefaultParser();
        final CommandLine line = parser.parse(options, new String[] { "-f", "non-existing.file" });


       try{
         assertNull("option f parsed", line.getParsedOptionValue("f"));
          fail("failed to throw ParseException");
        }
        catch (ParseException ex)
        {
         // success
        }
    }

    @Test
    public void testRequiredOption() throws Exception
    {
        final Options options = PatternOptionBuilder.parsePattern("!n%m%");
        final CommandLineParser parser = new DefaultParser();

        try
        {
            parser.parse(options, new String[]{""});
            fail("MissingOptionException wasn't thrown");
        }
        catch (final MissingOptionException e)
        {
            assertEquals(1, e.getMissingOptions().size());
            assertTrue(e.getMissingOptions().contains("n"));
        }
    }
}
