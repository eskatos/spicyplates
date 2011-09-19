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
package org.codeartisans.spicyplates.eruby;

import java.io.File;

import org.codeartisans.spicyplates.AbstractDirectorySpicyRepository;
import org.codeartisans.spicyplates.SpicyContext;
import org.codeartisans.spicyplates.SpicyFactory;

public class ERubyDirectorySpicyRepository
        extends AbstractDirectorySpicyRepository
{

    public ERubyDirectorySpicyRepository( File basedir )
    {
        super( basedir, null, new ERubySpicyFactory() );
    }

    public ERubyDirectorySpicyRepository( File basedir, SpicyContext globalContext, SpicyFactory factory )
    {
        super( basedir, globalContext, factory );
    }

    @Override
    protected boolean acceptTemplateName( String name )
    {
        return name.endsWith( ".erb" );
    }

}
