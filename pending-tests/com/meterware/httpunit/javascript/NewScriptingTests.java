package com.meterware.httpunit.javascript;
/********************************************************************************************************************
 * $Id: NewScriptingTests.java 977 2008-05-16 20:59:33Z wolfgang_fahl $
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

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.javascript.AbstractJavaScriptTest;

import junit.textui.TestRunner;
import junit.framework.TestSuite;

/**
 * Tests designed to track promised new features.
 */
public class NewScriptingTests extends AbstractJavaScriptTest {


    public static void main( String args[] ) {
        TestRunner.run( suite() );
    }


    public static TestSuite suite() {
        return new TestSuite( NewScriptingTests.class );
    }


    public NewScriptingTests( String name ) {
        super( name );
    }


    /**
     * bug report [ 1286018 ] EcmaError in seemingly valid function
     * by Stephane Mikaty
     *
     * @throws Exception on uncaught error
     */
    public void testArgumentsProperty() throws Exception {
        String html = "<html><head><script language='JavaScript'>               " +
                " function dumpargs() {                                         " +
                "    var args=dumpargs.arguments;                               " +
                "    var argdump=args.length;                                   " +
                "    alert( args.length + ' arguments')                         " +
                "    for (i=0; i<args.length; i+=1) {                           " +
                "      alert( i + ': ' + args[i]')                              " +
                "    }                                                          " +
                "  }                                                            " +
                "</script>                                                      " +
                "<body onload=\"dumpargs('a','b')\">                            " +
                "</body></html>                                                 ";

        defineResource( "OnCommand.html", html );
        WebConversation wc = new WebConversation();
        wc.getResponse( getHostPath() + "/OnCommand.html" );
        assertEquals( "alert message 1", "2 arguments", wc.popNextAlert() );
        assertEquals( "alert message 2", "0: a", wc.popNextAlert() );
        assertEquals( "alert message 3", "1: b", wc.popNextAlert() );
    }


    /**
     * test for bug report [ 1216567 ] Exception for large javascripts
     * by Grzegorz Lukasik
     * and bug report [ 1572117 ] ClassFormatError
     * by Walter Meier
     *
     * @author Wolfgang Fahl 2008-04-05
     */
    public void testLargeJavaScript() throws Exception {
        // create at least 64 KByte worth of Java script of the form
        // var1000=1000;
        // let's check the length;
        //          1
        // 12345678901234
        // var1000=1000+1;
        // so that is 14 chars avg per line - we'll do that many times for good measure ...
        // you might want to adjust the numbers to your environment
        // we do tests with 1000,10000,100000,1000000 lines in a logarithmic manner
        // we do the optimization levels according to rhino docs:
        //
        //    -2: with continuation
        //    -1: interpret
        // 0: compile to Java bytecode, don't optimize
        // 1..9: compile to Java bytecode, optimize
        //
        // set quicktest to false to get the full extent of the test
        // the quick version only runs 1000 and 1000 lines for the levels -2 to 1
        boolean quicktest = true;
        boolean showProgress = false;
        int linesToTest[] = {1000, 10000, 100000, 1000000};
        int numTests = linesToTest.length;
        int minOptLevel = -2;
        int maxOptLevel = 9;
        // when to expect a memory error
        int expectMemoryExceededForLinesOver = 100000;
        if (quicktest) {
            numTests = 2;
            minOptLevel = -1;
            maxOptLevel = 1;
            showProgress = false;
        } else {
            showProgress = true;
        }
        for (int optimizationLevel = minOptLevel; optimizationLevel <= maxOptLevel; optimizationLevel++) {
            HttpUnitOptions.setJavaScriptOptimizationLevel( optimizationLevel );
            // allow for different number of lines
            for (int i = 1; i < numTests; i++) {
                int fromj = 1;
                int toj = linesToTest[i] + 1;
                int lines = toj - fromj;
                String testDesc = "test " + i + " for " + lines + " Lines (" + fromj + "-" + toj + ") at optlevel " + optimizationLevel;
                if (showProgress)
                    System.out.println( testDesc );
                int midj = (fromj + toj) / 2;
                WebConversation wc = null;
                StringBuffer prepareScript = new StringBuffer();
                try {
                    // prepare code lines like
                    // var1000=1000+1;
                    // with the var<j>=<j>+1;\n pattern ...
                    // should be fun for the optimizer to remove all that unused code :-)
                    // we'll only use one variable later ...
                    for (int j = fromj; j < toj; j++) {
                        prepareScript.append( "var" );
                        prepareScript.append( j );
                        prepareScript.append( "=" );
                        prepareScript.append( j );
                        prepareScript.append( "+1;\n" );
                    }
                    prepareScript.append( "alert(var" + midj + ");" );
                    // off we go ... see what happens ...
                    wc = this.doTestJavaScript( prepareScript.toString() );
                } catch (RuntimeException re) {
                    // currently we get:
                    // alert(var25500);}' failed: java.lang.IllegalArgumentException: out of range index
                    // for 50000 lines and opt level 0
                    if ((optimizationLevel >= 0) && (lines >= 50000)) {
                        this.warnDisabled( "testLargeJavaScript","C",2,
                                "fails with runtime Exception for " + lines + " lines at optimizationLevel " + optimizationLevel + " the default is level -1 so we only warn" );
                    } else {
                        throw re;
                    }
                } catch (java.lang.OutOfMemoryError ome) {
                    if (lines >= expectMemoryExceededForLinesOver) {
                        this.warnDisabled( "testLargeJavaScript","C",2,
                                "fails with out of memory error for " + lines + " lines at optimizationLevel " + optimizationLevel + " we expect this for more than " + expectMemoryExceededForLinesOver + " lines" );
                        break;
                    } else {
                        throw ome;
                    }
                } catch (java.lang.ClassFormatError cfe) {
                    // java.lang.ClassFormatError: Invalid method Code length 223990 in class file org/mozilla/javascript/gen/c1
                    if (optimizationLevel >= 0)
                        this.warnDisabled( "testLargeJavaScript","C",2,
                                "fails with class format error for " + lines + " lines at optimizationLevel " + optimizationLevel + " the default is level -1 so we only warn" );
                    else
                        throw cfe;
                } // try

                if (wc != null) {
                    String expected = "" + (midj + 1);
                    expected = expected.trim();
                    // if we get here the big javascript was actually executed
                    // e.g. interpreted and compiled and the alert message at it's end
                    // was fired to show the content of a variable in the far middle of the script
                    assertEquals( testDesc, expected, wc.popNextAlert() );
                }
            } // for
            HttpUnitOptions.reset();
        }    // for optimizationLevel
    }

}
