package com.meterware.httpunit.site;
/********************************************************************************************************************
 * $Id: SourceForgeSiteTemplate.java 759 2006-04-04 22:58:36Z russgold $
 *
 * Copyright (c) 2005-2006, Russell Gold
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
import com.meterware.website.*;


/**
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 */
public class SourceForgeSiteTemplate extends BasicSiteTemplate {

    public void appendPageHeader( StringBuffer sb, Site site, SiteLocation currentPage ) {
        super.appendPageHeader( sb, site, currentPage );
        if (isLicensePage( currentPage )) sb.append( "<p>" ).append( site.getCopyRight().getNotice() ).append( "</p>" ). append( FragmentTemplate.LINE_BREAK );
    }


    public void appendPageFooter( StringBuffer sb, Site site, SiteLocation currentPage ) {
        if (!isLicensePage( currentPage )) {
            sb.append( "<hr/><div style='position:relative'>" ).append( FragmentTemplate.LINE_BREAK );
            sb.append( "  <div>" ).append( site.getCopyRight().getNotice() ).append( "</div>" ).append( FragmentTemplate.LINE_BREAK );
            ((WebSite) site).appendSourceForgeHostingNotice( sb );
        }
        super.appendPageFooter( sb, site, currentPage );
    }


    private boolean isLicensePage( SiteLocation currentPage ) {
        return ((SourceForgeWebPage) currentPage).isLicense();
    }


}
