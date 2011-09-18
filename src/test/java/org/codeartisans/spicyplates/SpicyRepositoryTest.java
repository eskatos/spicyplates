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

import org.codeartisans.spicyplates.erb.ERBClasspathSpicyRepository;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

import org.codeartisans.java.toolbox.io.IO;
import org.codeartisans.spicyplates.erb.ERBDirectorySpicyRepository;
import org.codeartisans.spicyplates.multi.MultiSpicyRepository;
import org.codeartisans.spicyplates.stringtemplate.STClasspathSpicyRepository;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Use test resources from the target directory and output results alongside each found template.
 */
public class SpicyRepositoryTest
{

    private static final File ROOT_FILE = new File( "target/test-classes/org/codeartisans/spicyplates/multi" );
    private static final String ROOT_PACKAGE = "org.codeartisans.spicyplates.multi";

    // FIXME This test is Locale dependent!
    @Test
    public void testDirectory()
            throws IOException
    {
        String templateName = "index.erb";
        SpicyRepository repository = new ERBDirectorySpicyRepository( ROOT_FILE );
        String result = runTestOnTemplate( repository, templateName );
        IO.copy( new StringReader( result ), new FileWriter( new File( ROOT_FILE, templateName + ".output" ) ) );
    }

    @Test
    public void testClasspath()
    {
        SpicyRepository repository = new ERBClasspathSpicyRepository( ROOT_PACKAGE );
        runTestOnTemplate( repository, "index.erb" );
    }

    @Test
    public void multiTest()
    {
        SpicyRepository erbRepository = new ERBDirectorySpicyRepository( ROOT_FILE );
        runTestOnTemplate( erbRepository, "index.erb" );

        SpicyRepository stRepository = new STClasspathSpicyRepository( ROOT_PACKAGE );
        runTestOnTemplate( stRepository, "index.st" );

        SpicyRepository multiRepository = new MultiSpicyRepository( erbRepository, stRepository );
        runTestOnTemplate( multiRepository, "index.erb" );
        runTestOnTemplate( multiRepository, "index.st" );
    }

    private String runTestOnTemplate( SpicyRepository repository, String templateName )
    {
        SpicyPlate index = repository.get( templateName );
        assertNotNull( "Unable to load " + templateName + " template", index );
        StringWriter output = new StringWriter();
        Date date = new Date();
        index.render( SpicyContexts.singletonContext( "date", date ), output );
        String result = output.toString();
        assertTrue( "Output content is wrong", result.contains( "SpicyPlates" ) );
        assertTrue( "Output content is wrong", result.contains( date.toString() ) );
        return result;
    }

}
