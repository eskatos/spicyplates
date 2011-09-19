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

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.codeartisans.java.toolbox.StringUtils;
import org.codeartisans.spicyplates.SpicyContext;
import org.codeartisans.spicyplates.SpicyPlate;
import org.codeartisans.spicyplates.SpicyPlatesFailure;

class ERubySpicyPlate
        implements SpicyPlate
{

    private final ScriptEngine ruby;
    private final SpicyContext globalContext;
    private final Object erb;

    ERubySpicyPlate( ScriptEngine ruby, SpicyContext globalContext, Object erb )
    {
        this.ruby = ruby;
        this.globalContext = globalContext;
        this.erb = erb;
    }

    @Override
    public void render( SpicyContext context, Writer output )
    {
        StringWriter errorWriter = new StringWriter();
        try {

            ScriptContext scriptContext = new SimpleScriptContext();

            scriptContext.setWriter( output );
            scriptContext.setErrorWriter( errorWriter );

            for ( Map.Entry<String, Object> eachGlobalAttribute : globalContext.entrySet() ) {
                // We use ScriptContext.ENGINE_SCOPE on purpose here, it seems JRuby do not support ScriptContext.GLOBAL_SCOPE.
                // It seems http://jira.codehaus.org/browse/JRUBY-4944 strikes again in the used version.
                // Next loop will override existing entries anyway.
                scriptContext.setAttribute( eachGlobalAttribute.getKey(), eachGlobalAttribute.getValue(), ScriptContext.ENGINE_SCOPE );
            }
            for ( Map.Entry<String, Object> eachLocalAttribute : context.entrySet() ) {
                scriptContext.setAttribute( eachLocalAttribute.getKey(), eachLocalAttribute.getValue(), ScriptContext.ENGINE_SCOPE );
            }

            scriptContext.setAttribute( "__spicy_plates_erb", erb, ScriptContext.ENGINE_SCOPE );
            ruby.eval( new InputStreamReader( getClass().getResourceAsStream( "template_renderer.rb" ) ), scriptContext );

        } catch ( ScriptException ex ) {
            throw new SpicyPlatesFailure( "Unable to render ERB template", ex ).andLogError( "Ruby error output was:\n" + StringUtils.indent( errorWriter.toString(), 1, "  ", "| " ) );
        }

    }

}
