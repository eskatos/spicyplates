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

import java.util.ArrayList;
import java.util.List;

import org.codeartisans.spicyplates.SpicyPlate;
import org.codeartisans.spicyplates.SpicyRepository;

public class MultiSpicyRepository
        implements SpicyRepository
{

    private final List<SpicyRepository> delegates = new ArrayList<SpicyRepository>();

    @SuppressWarnings( "ManualArrayToCollectionCopy" ) // Protective copy
    public MultiSpicyRepository( SpicyRepository... delegates )
    {
        for ( SpicyRepository eachRepository : delegates ) {
            this.delegates.add( eachRepository );
        }
    }

    public MultiSpicyRepository( List<SpicyRepository> delegates )
    {
        this.delegates.addAll( delegates );
    }

    @Override
    public SpicyPlate get( String name )
    {
        SpicyPlate template = null;
        loop:
        for ( SpicyRepository eachDelegate : delegates ) {
            template = eachDelegate.get( name );
            if ( template != null ) {
                break loop;
            }
        }
        return template;
    }

}
