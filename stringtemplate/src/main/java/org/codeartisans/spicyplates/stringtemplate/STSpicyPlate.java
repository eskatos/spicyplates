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
package org.codeartisans.spicyplates.stringtemplate;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.codeartisans.spicyplates.SpicyContext;
import org.codeartisans.spicyplates.SpicyPlate;
import org.codeartisans.spicyplates.SpicyPlatesFailure;
import org.stringtemplate.v4.ST;

class STSpicyPlate
        implements SpicyPlate
{

    private final SpicyContext globalContext;
    private final ST st;

    STSpicyPlate( SpicyContext globalContext, ST st )
    {
        this.globalContext = globalContext;
        this.st = st;
    }

    @Override
    public void render( SpicyContext context, Writer output )
    {
        try {
            for ( Map.Entry<String, Object> eachLocalAttribute : globalContext.entrySet() ) {
                st.add( eachLocalAttribute.getKey(), eachLocalAttribute.getValue() );
            }
            for ( Map.Entry<String, Object> eachLocalAttribute : context.entrySet() ) {
                String key = eachLocalAttribute.getKey();
                if ( st.getAttribute( key ) != null ) {
                    st.remove( key );
                }
                st.add( key, eachLocalAttribute.getValue() );
            }
            output.write( st.render() );
        } catch ( IOException ex ) {
            throw new SpicyPlatesFailure( "Unable to render StringTemplate template", ex );
        }
    }

}
