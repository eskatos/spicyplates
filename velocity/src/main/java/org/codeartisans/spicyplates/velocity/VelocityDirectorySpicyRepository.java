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
package org.codeartisans.spicyplates.velocity;

import java.io.File;

import org.codeartisans.spicyplates.AbstractDirectorySpicyRepository;
import org.codeartisans.spicyplates.SpicyContext;
import org.codeartisans.spicyplates.SpicyContexts;

public class VelocityDirectorySpicyRepository
        extends AbstractDirectorySpicyRepository
{

    public VelocityDirectorySpicyRepository( File rootDir, SpicyContext globalContext, VelocitySpicyFactory factory )
    {
        super( rootDir, globalContext, factory );
    }

    public VelocityDirectorySpicyRepository( File rootDir )
    {
        super( rootDir, SpicyContexts.EMPTY_CONTEXT, new VelocitySpicyFactory() );
    }

    @Override
    protected boolean acceptTemplateName( String name )
    {
        return name.endsWith( ".vm" );
    }

}
