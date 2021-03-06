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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

import org.codeartisans.java.toolbox.io.IO;
import org.codeartisans.spicyplates.eruby.ERubyClasspathSpicyRepository;
import org.codeartisans.spicyplates.eruby.ERubyDirectorySpicyRepository;
import org.codeartisans.spicyplates.stringtemplate.STClasspathSpicyRepository;
import org.codeartisans.spicyplates.stringtemplate.STDirectorySpicyRepository;
import org.codeartisans.spicyplates.velocity.VelocityClasspathSpicyRepository;
import org.codeartisans.spicyplates.velocity.VelocityDirectorySpicyRepository;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Use test resources from the target directory and output results alongside each found template.
 */
// FIXME This test is Locale dependent!
public class SpicyRepositoryTest
{

    private static final File ROOT_FILE = new File( "target/test-classes/org/codeartisans/spicyplates/multi" );
    private static final String ROOT_PACKAGE = "org.codeartisans.spicyplates.multi";

    @Test
    public void testDirectory()
            throws IOException
    {
        String templateName = "index.erb";
        SpicyRepository repository = new ERubyDirectorySpicyRepository( ROOT_FILE );
        String result = runTestOnTemplate( repository, templateName );
        IO.copy( new StringReader( result ), new FileWriter( new File( ROOT_FILE, templateName + ".output" ) ) );
    }

    @Test
    public void testClasspath()
    {
        SpicyRepository repository = new ERubyClasspathSpicyRepository( ROOT_PACKAGE );
        runTestOnTemplate( repository, "index.erb" );
    }

    @Test
    public void multiTest()
    {
        SpicyRepository eRubyRepository = new ERubyClasspathSpicyRepository( ROOT_PACKAGE );
        runTestOnTemplate( eRubyRepository, "index.erb" );
        eRubyRepository = new ERubyDirectorySpicyRepository( ROOT_FILE );
        runTestOnTemplate( eRubyRepository, "index.erb" );

        SpicyRepository stRepository = new STDirectorySpicyRepository( ROOT_FILE );
        runTestOnTemplate( stRepository, "index.st" );
        stRepository = new STClasspathSpicyRepository( ROOT_PACKAGE );
        runTestOnTemplate( stRepository, "index.st" );

        SpicyRepository vmRepository = new VelocityDirectorySpicyRepository( ROOT_FILE );
        runTestOnTemplate( vmRepository, "index.vm" );
        vmRepository = new VelocityClasspathSpicyRepository( ROOT_PACKAGE );
        runTestOnTemplate( vmRepository, "index.vm" );

        SpicyRepository multiRepository = new MultiSpicyRepository( eRubyRepository, stRepository, vmRepository );
        runTestOnTemplate( multiRepository, "index.erb" );
        runTestOnTemplate( multiRepository, "index.st" );
        runTestOnTemplate( multiRepository, "index.vm" );
    }

    private String runTestOnTemplate( SpicyRepository repository, String templateName )
    {
        SpicyPlate index = repository.get( templateName );
        assertNotNull( "Template " + templateName + " was null", index );
        StringWriter output = new StringWriter();
        Date date = new Date();
        index.render( SpicyContexts.singletonContext( "date", date ), output );
        String result = output.toString();
        assertTrue( "Output content is wrong", result.contains( "SpicyPlates" ) );
        assertTrue( "Output content is wrong", result.contains( date.toString() ) );
        return result;
    }

}
