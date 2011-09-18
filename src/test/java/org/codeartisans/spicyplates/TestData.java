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

public final class TestData
{

    public static enum Implementation
    {

        ERB, ST
    }

    static String contextsTestTemplate( Implementation impl )
    {
        switch ( impl ) {
            case ERB:
                return "Hello from template : <%= $foo %>";
            case ST:
                return "Hello from template : $foo$";
            default:
                throw new InternalError();
        }
    }

    static String loopsTestTemplate( Implementation impl )
    {
        switch ( impl ) {
            case ERB:
                return "<h1>Using loops</h1>\n"
                        + "<ul>\n"
                        + "<% $list.each { |line| %>"
                        + "  <li><%= line %></li>\n"
                        + "<% } %>"
                        + "</ul>";
            case ST:
                return "<h1>Using loops</h1>\n"
                        + "<ul>\n"
                        + "$list:{ line |"
                        + "  <li>$line$</li>\n"
                        + "}$"
                        + "</ul>";
            default:
                throw new InternalError();
        }
    }

    static String beansTestTemplate( Implementation impl )
    {
        switch ( impl ) {
            case ERB:
                return "<h1>Using beans</h1>\n"
                        + "<ul>\n"
                        + "  <li><%= $bean.getLeft %></li>\n"
                        + "  <li><%= $bean.left %></li>\n"
                        + "  <li><%= $bean.right %></li>\n"
                        + "</ul>";
            case ST:
                return "<h1>Using beans</h1>\n"
                        + "<ul>\n"
                        + "  <li>$bean.getLeft$</li>\n"
                        + "  <li>$bean.left$</li>\n"
                        + "  <li>$bean.right$</li>\n"
                        + "</ul>";
            default:
                throw new InternalError();
        }
    }

    private TestData()
    {
    }

}
