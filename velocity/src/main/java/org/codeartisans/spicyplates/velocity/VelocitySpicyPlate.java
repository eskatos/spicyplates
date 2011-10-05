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

import java.io.Writer;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.codeartisans.spicyplates.SpicyContext;
import org.codeartisans.spicyplates.SpicyPlate;
import org.codeartisans.spicyplates.SpicyPlatesFailure;

public class VelocitySpicyPlate
        implements SpicyPlate
{

    private final SpicyContext globalContext;
    private final String template;
    private final VelocityEngine engine;

    VelocitySpicyPlate( SpicyContext globalContext, String template, VelocityEngine engine )
    {
        this.globalContext = globalContext;
        this.template = template;
        this.engine = engine;
    }

    @Override
    public void render( SpicyContext context, Writer output )
    {
        try {

            VelocityContext velocityContext = new VelocityContext( globalContext );
            for ( Map.Entry<String, Object> eachLocalAttribute : context.entrySet() ) {
                velocityContext.put( eachLocalAttribute.getKey(), eachLocalAttribute.getValue() );
            }
            engine.evaluate( velocityContext, output, "VelocitySpicyPlate", template );

        } catch ( ParseErrorException ex ) {
            throw new SpicyPlatesFailure( "Unable to parse Velocity template", ex );
        } catch ( ResourceNotFoundException ex ) {
            throw new SpicyPlatesFailure( "Unable to parse Velocity template", ex );
        } catch ( MethodInvocationException ex ) {
            throw new SpicyPlatesFailure( "Unable to render Velocity template", ex );
        }
    }

}
