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
package org.codeartisans.spicyplates.multi;

import java.io.File;

import javax.servlet.ServletContext;

import org.codeartisans.spicyplates.AbstractSpicyServlet;
import org.codeartisans.spicyplates.MultiSpicyRepository;
import org.codeartisans.spicyplates.SpicyContext;
import org.codeartisans.spicyplates.SpicyRepository;
import org.codeartisans.spicyplates.eruby.ERubyClasspathSpicyRepository;
import org.codeartisans.spicyplates.eruby.ERubyDirectorySpicyRepository;
import org.codeartisans.spicyplates.eruby.ERubySpicyFactory;
import org.codeartisans.spicyplates.eruby.ERubyWebResourcesSpicyRepository;
import org.codeartisans.spicyplates.stringtemplate.STClasspathSpicyRepository;
import org.codeartisans.spicyplates.stringtemplate.STDirectorySpicyRepository;
import org.codeartisans.spicyplates.stringtemplate.STSpicyFactory;
import org.codeartisans.spicyplates.stringtemplate.STWebResourcesSpicyRepository;
import org.codeartisans.spicyplates.velocity.VelocityClasspathSpicyRepository;
import org.codeartisans.spicyplates.velocity.VelocityDirectorySpicyRepository;
import org.codeartisans.spicyplates.velocity.VelocitySpicyFactory;
import org.codeartisans.spicyplates.velocity.VelocityWebResourcesSpicyRepository;

public class MultiSpicyServlet
        extends AbstractSpicyServlet
{

    private ERubySpicyFactory eRubyFactory = new ERubySpicyFactory();
    private STSpicyFactory stFactory = new STSpicyFactory();
    private VelocitySpicyFactory vmFactory = new VelocitySpicyFactory();

    @Override
    protected SpicyRepository classpathRepository( SpicyContext globalContext, String rootPackage )
    {
        return new MultiSpicyRepository( new ERubyClasspathSpicyRepository( rootPackage, globalContext, eRubyFactory ),
                                         new STClasspathSpicyRepository( rootPackage, globalContext, stFactory ),
                                         new VelocityClasspathSpicyRepository( rootPackage, globalContext, vmFactory ) );
    }

    @Override
    protected SpicyRepository directoryRepository( SpicyContext globalContext, File rootDirectory )
    {
        return new MultiSpicyRepository( new ERubyDirectorySpicyRepository( rootDirectory, globalContext, eRubyFactory ),
                                         new STDirectorySpicyRepository( rootDirectory, globalContext, stFactory ),
                                         new VelocityDirectorySpicyRepository( rootDirectory, globalContext, vmFactory ) );
    }

    @Override
    protected SpicyRepository webResourcesRepository( SpicyContext globalContext, ServletContext servletContext )
    {
        return new MultiSpicyRepository( new ERubyWebResourcesSpicyRepository( servletContext, globalContext, eRubyFactory ),
                                         new STWebResourcesSpicyRepository( servletContext, globalContext, stFactory ),
                                         new VelocityWebResourcesSpicyRepository( servletContext, globalContext, vmFactory ) );
    }

}
