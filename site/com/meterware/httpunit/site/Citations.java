package com.meterware.httpunit.site;
/********************************************************************************************************************
 * $Id: Citations.java 541 2003-05-18 05:20:47Z russgold $
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

import com.meterware.website.FragmentTemplate;

import java.util.ArrayList;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class Citations extends FragmentTemplate {

    private ArrayList _sections = new ArrayList();

    public FragmentTemplate newFragment() {
        return new Citations();
    }


    public String asText() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < _sections.size(); i++) {
            Section section = (Section) _sections.get( i );
            section.appendTo( sb );
        }
        return sb.toString();
    }


    protected String getRootNodeName() {
        return "citations";
    }


    public Section createSection() {
        Section section = new Section();
        _sections.add( section );
        return section;
    }


    public class Section {
        private ArrayList _citations = new ArrayList();
        private String _title;


        public void setTitle( String title ) {
            _title = title;
        }


        public Citation createCitation() {
            Citation citation = new Citation();
            _citations.add( citation );
            return citation;
        }


        public void appendTo( StringBuffer sb ) {
            sb.append( "<h2>" ).append( _title ).append( "</h2>" ).append( LINE_BREAK );
            sb.append( "  <ul>" ).append( LINE_BREAK );
            for (int i = 0; i < _citations.size(); i++) {
                Citation citation = (Citation) _citations.get( i );
                citation.appendTo( sb );
            }
            sb.append( "  </ul>" ).append( LINE_BREAK );
        }

    }


    public class Citation {

        private String _url;
        private String _name;
        private String _text;

        public void setUrl( String url ) {
            _url = url;
        }


        public void setName( String name ) {
            _name = name;
        }


        public void setText( String text ) {
            _text = text;
        }


        public void appendTo( StringBuffer sb ) {
            sb.append( "    <li><a href='" ).append( _url ).append( "'>" ).append( _name ).append( "</a><br/>").append( LINE_BREAK );
            sb.append( "    " ).append( _text ).append( LINE_BREAK );
        }

    }
}
