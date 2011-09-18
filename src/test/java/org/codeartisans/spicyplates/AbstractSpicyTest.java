/*
 * Copyright (c) 2011, Paul Merlin. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.codeartisans.spicyplates;

import java.io.StringWriter;
import java.util.Collections;

import org.codeartisans.spicyplates.TestData.Implementation;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractSpicyTest
{

    private Implementation impl;
    private SpicyFactory factory;

    @Before
    public void gatherTestsParameters()
    {
        impl = getImplementation();
        factory = getSpicyPlateFactory();
    }

    protected abstract Implementation getImplementation();

    protected abstract SpicyFactory getSpicyPlateFactory();

    @Test
    public void testContexts()
    {
        factory.withDefaultGlobalContext( SpicyContexts.EMPTY_CONTEXT );
        SpicyPlate template = factory.spicyPlate( TestData.contextsTestTemplate( impl ) );

        SpicyRepository repository = new InMemorySpicyPlateRepository( Collections.singletonMap( "under-test", template ) );
        template = repository.get( "under-test" );

        SpicyContext ctx = SpicyContexts.singletonContext( "foo", "bar" );

        // Using local context
        StringWriter output = new StringWriter();
        template.render( ctx, output );
        assertEquals( "Template output is wrong", "Hello from template : bar", output.toString().trim() );

        // Now using global context
        factory.spicyPlate( ctx, TestData.contextsTestTemplate( impl ) );
        output = new StringWriter();
        template.render( SpicyContexts.EMPTY_CONTEXT, output );
        assertEquals( "Template output is wrong", "Hello from template : bar", output.toString().trim() );

        // Overriding global context in local context
        output = new StringWriter();
        SimpleSpicyContext override = new SimpleSpicyContext();
        override.put( "foo", "foo" );
        template.render( override, output );
        assertEquals( "Template output is wrong", "Hello from template : foo", output.toString().trim() );
    }

    @Test
    public void testLoops()
    {
        SpicyPlate spicyPlate = factory.spicyPlate( TestData.loopsTestTemplate( impl ) );
        StringWriter output = new StringWriter();
        spicyPlate.render( SpicyContexts.singletonContext( "list", new String[]{ "foo", "bar" } ), output );
        String out = output.toString();
        assertTrue( "Output content is wrong", out.contains( "<li>foo</li>" ) );
        assertTrue( "Output content is wrong", out.contains( "<li>bar</li>" ) );
    }

    @Test
    public void testBeans()
    {
        SpicyPlate spicyPlate = factory.spicyPlate( TestData.beansTestTemplate( impl ) );
        StringWriter output = new StringWriter();
        spicyPlate.render( SpicyContexts.singletonContext( "bean", new TestBean( "foo", "bar" ) ), output );
        String out = output.toString();
        assertTrue( "Output content is wrong", out.contains( "<li>foo</li>" ) );
        assertTrue( "Output content is wrong", out.contains( "<li>bar</li>" ) );
    }

    public static class TestBean
    {

        private final String left;
        private final String right;

        public TestBean( String left, String right )
        {
            this.left = left;
            this.right = right;
        }

        public String getLeft()
        {
            return left;
        }

        public String getRight()
        {
            return right;
        }

    }

}
