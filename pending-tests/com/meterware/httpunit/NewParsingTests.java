package com.meterware.httpunit;
/********************************************************************************************************************
* $Id: NewParsingTests.java 977 2008-05-16 20:59:33Z wolfgang_fahl $
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
import com.meterware.httpunit.HttpUnitTest;
import com.meterware.httpunit.WebClient;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;

import junit.framework.Test;
import junit.framework.TestSuite;

public class NewParsingTests extends HttpUnitTest {

    private WebClient _wc;


    public static void main(String args[]) {
        junit.textui.TestRunner.run( suite() );
    }


    public static Test suite() {
        return new TestSuite( NewParsingTests.class );
    }


    public NewParsingTests( String name ) {
        super( name );
    }


    public void setUp() throws Exception {
        super.setUp();
        _wc = new WebConversation();
    }



    /**
     * test a link that has a line break included
     * @throws Exception on an unexpected exception - requires nekohtml 1.9.6 to pass
     */
    public void testLinkUrlAcrossLineBreaks() throws Exception {
        WebConversation wc = new WebConversation();
        defineWebPage( "Initial", "<a id='midbreak' href='http://loc\nalhost/somewhere'</a>" +
                                  "<a id='endbreak' href='http://localhost/somewhere\n'</a>" );

        WebResponse response = wc.getResponse( getHostPath() + "/Initial.html" );
        String endbreak=response.getLinkWithID( "endbreak" ).getRequest().getURL().toExternalForm() ;
        assertEquals( "URL with break at end", endbreak,"http://localhost/somewhere");
        //System.err.println("endbreak='"+endbreak+"'");
        String midbreak=response.getLinkWithID( "midbreak" ).getRequest().getURL().toExternalForm() ;
        //System.err.println("midbreak='"+midbreak+"'");
        assertEquals( "URL across linebreak", midbreak,"http://loc\nalhost/somewhere");
    }


    /**
     * test for bug report [ 1393144 ] URL args in form action are sent for GET forms
     * by Nathan Jakubiak
     * @throws Exception on an uncaught error
     */
    public void testParamReplacement() throws Exception {
        String expected = "/cgi-bin/bar?foo=a";
        String nogood = "/cgi-bin/bar?arg=replaced&foo=a";
        defineResource( nogood, "not good" );
        defineResource( expected, "excellent" );
        String html =
                "<FORM NAME=Bethsheba METHOD=GET ACTION=/cgi-bin/bar?arg=replaced>" +
                        "<INPUT TYPE=TEXT NAME=foo>" +
                        "<INPUT TYPE=SUBMIT>" +
                        "</FORM>" +
                        "<br>" +
                        "<!--JavaScript submit:" +
                        "<a	href=\"javascript:document.Bethsheba.submit()\">go</a>" +
                        "-->";
        defineWebPage( "test", html );
        WebResponse resp = _wc.getResponse( getHostPath() + "/test.html" );
        WebForm form = resp.getFormWithName( "Bethsheba" );
        form.setParameter( "foo", "a" );
        resp = form.submit();
        String foundURL = resp.getURL().toString();
        assertTrue( foundURL.equals( expected ) );
    }


    /**
     * test bug report [ 1376739 ] iframe tag not recognized if Javascript code contains '<'
     * by Nathan Jakubiak
     * @throws Exception on an uncaught error
     */
    public void testIFrameBug() throws Exception {
        String html = "\"<SCRIPT LANGUAGE=\"JavaScript\">\n" +
                "var b = 0 < 1;\n" +
                "</SCRIPT>\n" +
                "<iframe name=\"iframe_after_lessthan_in_javascript\"\n" +
                "src=\"c.html\"></iframe>";
        defineWebPage( "iframe", html );
        WebResponse response = _wc.getFrameContents( "iframe_after_lessthan_in_javascript" );
        assertNotNull( "Iframe was not recognized", response );
    }
    

    /**
     * test for bug report [ 1156972 ] isWebLink doesn't recognize all anchor tags
     * by fregienj
     * @throws Exception on anuncaught error
     */
    public void testFindNonHrefLinks() throws Exception {
        defineResource( "NonHref.html", "<html><head><title>NonHref Links</title></head><body>\n" +
                "<a onclick='javascript:followlink()'>I am a clickable link after all</a>\n" +
                "</body></html>" );
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse( getHostPath() + "/NonHref.html" );
        WebLink[] links = response.getLinks();
        assertNotNull( links );
        assertEquals( "number of non-href anchor tags", 1, links.length );
    }

}
