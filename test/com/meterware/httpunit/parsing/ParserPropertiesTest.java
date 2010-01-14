package com.meterware.httpunit.parsing;
/********************************************************************************************************************
 * $Id: ParserPropertiesTest.java 855 2008-03-31 08:54:13Z wolfgang_fahl $
 *
 * Copyright (c) 2002-2008, Russell Gold
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
import junit.textui.TestRunner;
import junit.framework.TestSuite;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;

import com.meterware.httpunit.parsing.HTMLParserFactory;
import com.meterware.httpunit.*;

/**
 * This test checks certain customizable behaviors of the HTML parsers. Not every parser implements every behavior.
 *
 * @author <a href="mailto:bw@xmlizer.biz">Bernhard Wagner</a>
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class ParserPropertiesTest extends HttpUnitTest {

    public static void main( String args[] ) {
        TestRunner.run( suite() );
    }


    /**
     * set up the suite of tests conditionally
     * @return
     */
    public static TestSuite suite() {
        TestSuite ts = new TestSuite();
        HTMLParser parser = HTMLParserFactory.getHTMLParser();
        boolean supportsKeepCase = parser.supportsPreserveTagCase();
        if (supportsKeepCase) 
        	ts.addTest( new ParserPropertiesTest( "testKeepCase" ) );
        boolean supportsForceCase = parser.supportsForceTagCase();
        if (parser instanceof NekoHTMLParser) {
        	ts.addTest( new ParserPropertiesTest( "testLowerCase" ) );
        }	
        if (supportsForceCase) {
        	ts.addTest( new ParserPropertiesTest( "testForceLowerCase" ) );
        	ts.addTest( new ParserPropertiesTest( "testForceUpperCase" ) );
        }	
        return ts;
    }

    public void tearDown() throws Exception {
        super.tearDown();
        HTMLParserFactory.reset();
    }


    public ParserPropertiesTest( String name ) {
        super( name );
    }
    
    /**
     * verify the upper/lower case handling
     * @param wc
     * @param request
     * @param boldNodeContents
     * @param tagName - the tagName to look for
     * @throws IOException
     * @throws SAXException
     */
    private void verifyMatchingBoldNodes( WebConversation wc, WebRequest request, String[] boldNodeContents, String tagName ) throws IOException, SAXException {
      WebResponse simplePage = wc.getResponse( request );
      Document doc = simplePage.getDOM();
      NodeList nlist = doc.getElementsByTagName( tagName );
      assertEquals( "Number of nodes with tag '"+tagName+"':", boldNodeContents.length, nlist.getLength() );
      for (int i = 0; i < nlist.getLength(); i++) {
          assertEquals( "Element " + i, boldNodeContents[i], nlist.item( i ).getFirstChild().getNodeValue() );
      }
    }

    /**
     * verify the upper/lower case handling
     * @param wc
     * @param request
     * @param boldNodeContents
     * @throws IOException
     * @throws SAXException
     */
    private void verifyMatchingBoldNodes( WebConversation wc, WebRequest request, String[] boldNodeContents ) throws IOException, SAXException {
    	verifyMatchingBoldNodes(wc,request,boldNodeContents,"b");
    }
    
    /**
     * shared by all tests
     */
    private WebConversation wc=null;
    private WebRequest request=null;
    
    /**
     * same page for all tests
     * @throws Exception
     */
    public void prepareTestCase() throws Exception {
      defineResource( "SimplePage.html",
          "<HTML><head><title>A Sample Page</title></head>\n" +
          "<body>This has no forms but it does\n" +
          "have <a href=\"/other.html\">an <b>active</b> link</A>\n" +
          " and <a name=here>an <B>anchor</B></a>\n" +
          "</body></HTML>\n" );    	
      wc = new WebConversation();
      request = new GetMethodWebRequest( getHostPath() + "/SimplePage.html" );
    }

    /**
     * test the preserveTagCase configuration feature ofh the HTMLParserFactory
     * @param preserveTagCase
     * @throws Exception
     */
    public void doTestKeepCase(boolean preserveTagCase, String[] expected1, String[] expected2) throws Exception {
    	prepareTestCase();
      verifyMatchingBoldNodes( wc, request, expected1 );
      HTMLParserFactory.setPreserveTagCase( preserveTagCase);
      verifyMatchingBoldNodes( wc, request, expected2 );
    }
    
    /**
     * test the keepcase setting
     * @throws Exception
     */
    public void testKeepCase() throws Exception {
    	doTestKeepCase(true,new String[] { "active", "anchor" },new String[] { "active" });
    }
    
    /**
     * test for patch [ 1211154 ] NekoDOMParser default to lowercase
     * by Dan Allen 
     * @throws Exception
     */
    public void testLowerCase() throws Exception {
     	doTestKeepCase(false,new String[] { "active", "anchor" },new String[] { "active", "anchor" });
    }

    /**
     * test for patch [ 1176688 ] Allow configuration of neko parser properties
     * by james abley
     * @throws Exception
     */
	 public void testForceUpperCase() throws Exception  {
	   	prepareTestCase();
      assertFalse(HTMLParserFactory.getForceUpperCase());
      verifyMatchingBoldNodes(wc, request, new String[] { "active", "anchor" }, "B");
      HTMLParserFactory.setForceUpperCase(true);
      verifyMatchingBoldNodes(wc, request, new String[] { "active" , "anchor"}, "B");
      verifyMatchingBoldNodes(wc, request, new String[0], "b");
   }

   /**
    * test for patch [ 1176688 ] Allow configuration of neko parser properties
    * by james abley
    * 
    */
	  public void testForceLowerCase() throws Exception {
	   	prepareTestCase();
      assertFalse(HTMLParserFactory.getForceLowerCase());
      verifyMatchingBoldNodes(wc, request,new String[] { "active", "anchor" }, "b");
	    HTMLParserFactory.setForceLowerCase(true);
	    verifyMatchingBoldNodes(wc, request, new String[] { "active" , "anchor"}, "b");
	    verifyMatchingBoldNodes(wc, request, new String[0], "B");
	  }

}
