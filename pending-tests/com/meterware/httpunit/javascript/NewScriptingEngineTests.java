package com.meterware.httpunit.javascript;
/********************************************************************************************************************
 * $Id: NewScriptingEngineTests.java 977 2008-05-16 20:59:33Z wolfgang_fahl $
 *
 * Copyright (c) 2007-2008, Russell Gold
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 *******************************************************************************************************************/
import com.meterware.httpunit.*;
import com.meterware.httpunit.javascript.AbstractJavaScriptTest;

import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * New features promised for scripting, but only implemented for new scripting engine.
 */
public class NewScriptingEngineTests extends AbstractJavaScriptTest {

    public static void main( String args[] ) {
        TestRunner.run( suite() );
    }


    public static TestSuite suite() {
        return new TestSuite( NewScriptingEngineTests.class );
    }


    public NewScriptingEngineTests( String name ) {
        super( name );
    }


    /**
     * test jsFunction_createElement() - supplied by Mark Childerson
     * also for bug report [ 1430378 ] createElement not found in JavaScript by Saliya Jinadasa
     * @since 2008-03-26
     * @throws Exception on uncaught problem
     */
    public void testCreateElement() throws Exception {
        defineResource( "OnCommand.html",
                "<html><head><title>Amazing!</title></head>" +
                        "<body onLoad='var elem=document.createElement(\"input\");elem.id=\"hellothere\";alert(elem.id);'></body>" );
        WebConversation wc = new WebConversation();
        boolean oldDebug = HttpUnitUtils.setEXCEPTION_DEBUG( false );
        try {
            wc.getResponse( getHostPath() + "/OnCommand.html" );
            // 	used to throw:
            // 	com.meterware.httpunit.ScriptException: Event 'var elem=document.createElement("input");elem.id="hellothere";alert(elem.id);' failed: org.mozilla.javascript.EcmaError: TypeError: Cannot find function createElement.
            assertEquals( "Alert message", "hellothere", wc.popNextAlert() );
        } finally {
            HttpUnitUtils.setEXCEPTION_DEBUG( oldDebug );
        }
    }

    /**
     * test for cloneNode feature (asked for by Mark Childeson on 2008-04-01)
     * @throws Exception on any uncaught problem
     */
    public void testCloneNode() throws Exception {
        doTestJavaScript(
                "dolly1=document.getElementById('Dolly');\n" +
                        "dolly2=dolly1.cloneNode(true);\n" +
                        "dolly1.firstChild.nodeValue += dolly2.firstChild.nodeValue;\n" +
                        "alert(dolly1.firsthChild.nodeValue);\n",
                "<div id='Dolly'>Dolly </div>" );
    }


    /**
     * test for bug report [ 1396877 ] Javascript:properties parentNode,firstChild, .. returns null
     * by gklopp 2006-01-04 15:15
     * @throws Exception on any uncaught problem
     */
    public void testDOM() throws Exception {
        defineResource( "testSelect.html", "<html><head><script type='text/javascript'>\n" +
                "<!--\n" +
                "function testDOM() {\n" +
                "  var sel = document.getElementById('the_select');\n" +
                "  var p = sel.parentNode;\n" +
                "  var child = p.firstChild;\n" +
                "  alert('Parent : ' + p.nodeName);\n" +
                "  alert('First child : ' + child.nodeName);\n" +
                "}\n" +
                "-->\n" +
                "</script></head>" +
                "<body>" +
                "<form name='the_form'>" +
                "   <table>" +
                "    <tr>" +
                "      <td>Selection :</td>" +
                "       <td>" +
                "          <select name='the_select'>" +
                "              <option value='option1Value'>option1</option>" +
                "          </select>" +
                "       </td>" +
                "     </tr>" +
                "   </table>" +
                "</form>" +
                "<script type='text/javascript'>testDOM();</script>" +
                "</body></html>" );
        WebConversation wc = new WebConversation();
        wc.getResponse( getHostPath() + "/testSelect.html" );
        assertEquals( "Message 1", "TD", wc.popNextAlert().toUpperCase() );
        assertEquals( "Message 2", "SELECT", wc.popNextAlert().toUpperCase() );
    }

}
