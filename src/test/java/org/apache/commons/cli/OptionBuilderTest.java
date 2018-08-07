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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class OptionBuilderTest
{
    @Test
    public void testCompleteOption( ) {
        final Option simple = Option.builder("s").longOpt( "simple option")
                                     .hasArg( )
                                     .required( )
                                     .hasArgs( )
                                     .type( Float.class )
                                     .desc( "this is a simple option" )
                                     .build( );

        assertEquals( "s", simple.getOpt() );
        assertEquals( "simple option", simple.getLongOpt() );
        assertEquals( "this is a simple option", simple.getDescription() );
        assertEquals( simple.getType(), Float.class );
        assertTrue( simple.hasArg() );
        assertTrue( simple.isRequired() );
        assertTrue( simple.hasArgs() );
    }

    @Test
    public void testTwoCompleteOptions( ) {
        Option simple = Option.builder("s").longOpt( "simple option")
                                     .hasArg( )
                                     .required( )
                                     .hasArgs( )
                                     .type( Float.class )
                                     .desc( "this is a simple option" )
                                     .build( );

        assertEquals( "s", simple.getOpt() );
        assertEquals( "simple option", simple.getLongOpt() );
        assertEquals( "this is a simple option", simple.getDescription() );
        assertEquals( simple.getType(), Float.class );
        assertTrue( simple.hasArg() );
        assertTrue( simple.isRequired() );
        assertTrue( simple.hasArgs() );

        simple = Option.builder("d").longOpt( "dimple option")
                              .hasArg( )
                              .desc( "this is a dimple option" )
                              .build(  );

        assertEquals( "d", simple.getOpt() );
        assertEquals( "dimple option", simple.getLongOpt() );
        assertEquals( "this is a dimple option", simple.getDescription() );
        assertEquals( String.class, simple.getType() );
        assertTrue( simple.hasArg() );
        assertTrue( !simple.isRequired());
        assertTrue( !simple.hasArgs() );
    }

    @Test
    public void testBaseOptionCharOpt() {
        final Option base = Option.builder("o").desc( "option description")
                                   .build(  );

        assertEquals( "o", base.getOpt() );
        assertEquals( "option description", base.getDescription() );
        assertTrue( !base.hasArg() );
    }

    @Test
    public void testBaseOptionStringOpt() {
        final Option base = Option.builder("o").desc( "option description")
                                   .build(  );

        assertEquals( "o", base.getOpt() );
        assertEquals( "option description", base.getDescription() );
        assertTrue( !base.hasArg() );
    }

    @Test
    public void testSpecialOptChars() throws Exception
    {
        // '?'
        final Option opt1 = Option.builder("?").desc("help options").build();
        assertEquals("?", opt1.getOpt());

        // '@'
        final Option opt2 = Option.builder("@").desc("read from stdin").build();
        assertEquals("@", opt2.getOpt());
        
        // ' '
        try {
            Option.builder(" ").build();
            fail( "IllegalArgumentException not caught" );
        } catch (final IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testOptionArgNumbers()
    {
        final Option opt = Option.builder("o").desc( "option description" )
                                  .numberOfArgs(2 )
                                  .build(  );
        assertEquals( 2, opt.getArgs() );
    }

    @Test
    public void testIllegalOptions() {
        // bad single character option
        try {
            Option.builder("'").desc( "option description" ).build( );
            fail( "IllegalArgumentException not caught" );
        }
        catch( final IllegalArgumentException exp ) {
            // success
        }

        // bad character in option string
        try {
            Option.builder("opt`").build(  );
            fail( "IllegalArgumentException not caught" );
        }
        catch( final IllegalArgumentException exp ) {
            // success
        }

        // valid option 
        try {
            Option.builder( "opt").build( );
            // success
        }
        catch( final IllegalArgumentException exp ) {
            fail( "IllegalArgumentException caught" );
        }
    }

    @Test
    public void testCreateIncompleteOption() {
        try
        {
            Option.builder().hasArg().build();
            fail("Incomplete option should be rejected");
        }
        catch (final IllegalArgumentException e)
        {
            // expected
            
            // implicitly reset the builder
            Option.builder("opt").build(  );
        }
    }

    @Test
    public void testBuilderIsResettedAlways() {
        try
        {
            Option.builder("\"").desc("JUnit").build();
            fail("IllegalArgumentException expected");
        }
        catch (final IllegalArgumentException e)
        {
            // expected
        }
        assertNull("we inherited a description", Option.builder("x").build().getDescription());

        try
        {
            Option.builder().desc("JUnit").build();
            fail("IllegalArgumentException expected");
        }
        catch (final IllegalArgumentException e)
        {
            // expected
        }
        assertNull("we inherited a description", Option.builder("x").build().getDescription());
    }
}
