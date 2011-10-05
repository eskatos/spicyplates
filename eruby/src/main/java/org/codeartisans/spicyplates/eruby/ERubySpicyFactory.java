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

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.codeartisans.java.toolbox.Strings;
import org.codeartisans.java.toolbox.exceptions.NullArgumentException;
import org.codeartisans.spicyplates.AbstractSpicyFactory;
import org.codeartisans.spicyplates.SpicyContext;
import org.codeartisans.spicyplates.SpicyPlate;
import org.codeartisans.spicyplates.SpicyPlatesFailure;

public class ERubySpicyFactory
        extends AbstractSpicyFactory
{

    private final ScriptEngine ruby;

    public ERubySpicyFactory()
    {
        super();
        // see https://github.com/jruby/jruby/wiki/RedBridge
        System.setProperty( "org.jruby.embed.localcontext.scope", "concurrent" );
        System.setProperty( "org.jruby.embed.compilemode", "jit" );
        ScriptEngineManager engineManager = new ScriptEngineManager( this.getClass().getClassLoader() );
        ruby = engineManager.getEngineByName( "jruby" );
    }

    @Override
    public SpicyPlate spicyPlate( SpicyContext globalContext, String template )
    {
        NullArgumentException.ensureNotNull( "Global context", globalContext );
        NullArgumentException.ensureNotEmpty( "Template", template );
        StringWriter errorWriter = new StringWriter();
        try {

            ScriptContext scriptContext = new SimpleScriptContext();
            scriptContext.setErrorWriter( errorWriter );
            scriptContext.setAttribute( "__spicy_plates_raw_template", template, ScriptContext.ENGINE_SCOPE );
            Object erb = ruby.eval( new InputStreamReader( getClass().getResourceAsStream( "template_reader.rb" ) ), scriptContext );
            return new ERubySpicyPlate( ruby, globalContext, erb );

        } catch ( ScriptException ex ) {
            throw new SpicyPlatesFailure( "Unable to create ERB object from template content", ex ).andLogError( "Ruby error output was:\n" + Strings.indent( errorWriter.toString(), 1, "  ", "| " ) );
        }
    }

}
