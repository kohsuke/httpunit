package com.meterware.httpunit.site;
/********************************************************************************************************************
 * $Id: Developers.java 564 2003-06-20 03:21:11Z russgold $
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
public class Developers extends FragmentTemplate {

    private ArrayList _groups = new ArrayList();

    public FragmentTemplate newFragment() {
        return new Developers();
    }


    public String asText() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < _groups.size(); i++) {
            Group group = (Group) _groups.get( i );
            group.appendTo( sb );
        }
        return sb.toString();
    }


    protected String getRootNodeName() {
        return "developers";
    }


    public Group createGroup() {
        Group group = new Group();
        _groups.add( group );
        return group;
    }


    public class Group {

        private ArrayList _developers = new ArrayList();
        private ArrayList _summaries = new ArrayList();
        private String _type;
        private static final int MAX_SUMMARY_COLUMNS = 6;


        public void setType( String type ) {
            _type = type;
        }


        public Developer createDeveloper() {
            Developer developer = new Developer();
            _developers.add( developer );
            return developer;
        }


        void appendTo( StringBuffer sb ) {
            sb.append( "<h2>" ).append( _type ).append( "</h2>" ).append( LINE_BREAK );
            sb.append( "<dl class='developers'>" ).append( LINE_BREAK );
            for (int i = 0; i < _developers.size(); i++) {
                Developer developer = (Developer) _developers.get( i );
                if (developer.isNameOnly()) {
                    _summaries.add( developer );
                } else {
                    developer.appendTo( sb );
                }
            }
            sb.append( "</dl>" ).append( LINE_BREAK );
            if (_summaries.size() != 0) appendSummary( sb );
        }


        private void appendSummary( StringBuffer sb ) {
            sb.append( "<table>" ).append( LINE_BREAK );
            int numColumns = Math.min( _summaries.size(), MAX_SUMMARY_COLUMNS );
            int numRows = (_summaries.size() + numColumns - 1) / numColumns;

            for (int j = 0; j < numRows; j++) {
                sb.append( "<tr>" );
                for (int k = 0; k < numColumns; k++) {
                    int i = j + k*numRows;
                    if (i >= _summaries.size()) continue;
                    sb.append( "<td class='summaries'>");
                    Developer developer = (Developer) _summaries.get(i);
                    developer.appendNameTo( sb );
                    sb.append( "</td>" );
                }
                sb.append( "</tr>" ).append( LINE_BREAK );
            }
            sb.append( "</table>" ).append( LINE_BREAK );
        }
    }


    public class Developer {

        private String _name;
        private String _username;
        private String _email;
        private String _text;

        public void setName( String name ) {
            _name = name;
        }


        public void setUsername( String username ) {
            _username = username;
        }


        public void setEmail( String email ) {
            _email = email;
        }


        public void setText( String text ) {
            _text = text;
        }


        boolean isNameOnly() {
            return _text == null;
        }


        void appendTo( StringBuffer sb ) {
            sb.append( "<dt>" );
            appendNameTo( sb );
            sb.append( "</dt>" ).append( LINE_BREAK );
            if (_text != null) sb.append( "<dd>" ).append( _text ).append( "</dd>" ).append( LINE_BREAK );
        }


        void appendNameTo( StringBuffer sb ) {
            if (_username == null /* && _email == null */) {
                sb.append( _name );
            } else {
                sb.append( "<a" );
                if (_username != null) sb.append( " name='" ).append( _username ).append( "'" );
                if (_email != null) sb.append( " href='mailto:" ).append( _email ).append( "'" );
                sb.append( ">" ).append( _name ).append( "</a>" );
            }
        }
    }
}
