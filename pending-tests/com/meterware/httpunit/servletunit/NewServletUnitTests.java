package com.meterware.httpunit.servletunit;
/********************************************************************************************************************
 * $Id: NewServletUnitTests.java 975 2008-05-16 19:44:48Z russgold $
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
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.StatelessTest;

import junit.framework.TestSuite;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Tests for features pending addition to ServletUnit.
 */
public class NewServletUnitTests extends HttpUnitTest {

    public static void main( String args[] ) {
        junit.textui.TestRunner.run( suite() );
    }


    public static TestSuite suite() {
        return new TestSuite( NewServletUnitTests.class );
    }


    public NewServletUnitTests( String name ) {
        super( name );
    }


    /**
     * test bug report [ 1534234 ] HttpServletResponse.isCommitted() always false? (+patch)
     * by Olaf Klischat?
     * @throws Exception on unexpected error
     */
    public void testIsCommitted() throws Exception {
        ServletRunner sr = new ServletRunner();

        WebRequest request = new GetMethodWebRequest(
                "http://localhost/servlet/" + CheckIsCommittedServlet.class.getName() );
        WebResponse response = sr.getResponse( request );
        assertTrue( "The response should be committed", CheckIsCommittedServlet.isCommitted );
    }


    /**
     * helper Servlet for bug report 1534234
     */
    public static class CheckIsCommittedServlet extends HttpServlet {

        public static boolean isCommitted;


        protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
            resp.setContentType( "text/html" );

            PrintWriter pw = resp.getWriter();
            pw.println( "anything" );
            pw.flush();
            pw.close();
            isCommitted = resp.isCommitted();
        }
    }

}
