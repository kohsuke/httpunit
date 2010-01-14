package com.meterware.httpunit.parsing;
/********************************************************************************************************************
* $Id: HTMLParserListenerTest.java 611 2004-03-09 20:32:44Z russgold $
*
* Copyright (c) 2003, Russell Gold
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

import java.net.URL;
import java.io.PrintWriter;

import junit.framework.TestSuite;


/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class HTMLParserListenerTest extends HttpUnitTest {

    public static void main(String[] args) {
        junit.textui.TestRunner.run( suite() );
    }


    public static TestSuite suite() {
        return new TestSuite( HTMLParserListenerTest.class );
    }

    public HTMLParserListenerTest( String name ) {
        super( name );
    }


    public void testBadHTMLPage() throws Exception {
        defineResource( "BadPage.html",
                        "<html>" +
                        "<head><title>A Sample Page</head>\n" +
                        "<body><p><b>Wrong embedded tags</p></b>\n" +
                        "have <a blef=\"other.html?a=1&b=2\">an invalid link</A>\n" +
                        "<IMG SRC=\"/images/arrow.gif\" WIDTH=1 HEIGHT=4>\n" +
                        "<unknownTag>bla</unknownTag>" +
                        "</body></html>\n" );

        final ErrorHandler errorHandler = new ErrorHandler( /* expectProblems */ true );
        try {
            WebConversation wc = new WebConversation();
            HTMLParserFactory.addHTMLParserListener( errorHandler );
            WebRequest request = new GetMethodWebRequest( getHostPath() + "/BadPage.html" );
            wc.getResponse( request );
            assertTrue( "Should have found problems", errorHandler.foundProblems() );
            assertEquals( "Expected URL", request.getURL(), errorHandler.getBadURL() );
        } finally {
            HTMLParserFactory.removeHTMLParserListener( errorHandler );
        }
    }


    public void testGoodHTMLPage() throws Exception {
        final ErrorHandler errorHandler = new ErrorHandler( /* expectProblems */ false );
        try {
            defineResource( "SimplePage.html",
                            "<html>\n" +
                            "<head><title>A Sample Page</title></head>\n" +
                            "<body><p><b>OK embedded tags</b></p>\n" +
                            "have <a href=\"other.html?a=1&amp;b=2\">an OK link</A>\n" +
                            "<IMG SRC=\"/images/arrow.gif\" alt=\"\" WIDTH=1 HEIGHT=4>\n" +
                            "</body></html>\n" );

            WebConversation wc = new WebConversation();
            HTMLParserFactory.addHTMLParserListener( errorHandler );
            WebRequest request = new GetMethodWebRequest( getHostPath() + "/SimplePage.html" );
            wc.getResponse( request );
        } finally {
            HTMLParserFactory.removeHTMLParserListener( errorHandler );
        }
    }


    public void testJTidyPrintWriterParsing() throws Exception {
        URL url = new URL("http://localhost/blank.html");
        PrintWriter p = new JTidyPrintWriter(url);
        p.print("line 1234 column 1234");
        p.print("line 1,234 column 1,234");
        p.print("line 1,234,567 column 1,234,567");
        p.print("line 1,2,34 column 12,34");
        p.print("line 123,,4 column 12,,34");
    }


    static private class ErrorHandler implements HTMLParserListener {

        private boolean _expectProblems;
        private boolean _foundProblems;
        private URL     _badURL;


        public ErrorHandler( boolean expectProblems ) {
            _expectProblems = expectProblems;
        }


        public void warning( URL url, String msg, int line, int column ) {
            _foundProblems = true;
            _badURL = url;
        }

        public void error( URL url, String msg, int line, int column ) {
            assertTrue( msg + " at line " + line + ", column " + column, _expectProblems );
            _foundProblems = true;
            _badURL = url;
        }

        public URL getBadURL() { return _badURL; }

        public boolean foundProblems() { return _foundProblems; }
    }

}
