package com.meterware.httpunit.site;
/********************************************************************************************************************
 * $Id: Faq.java 541 2003-05-18 05:20:47Z russgold $
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
public class Faq extends FragmentTemplate {

    private ArrayList _sections = new ArrayList();


    public FragmentTemplate newFragment() {
        return new Faq();
    }


    public String asText() {
        StringBuffer sb = new StringBuffer();
        formatFaqSections( sb, new IndexFormat() );
        sb.append( "<hr/>" ).append( LINE_BREAK );
        formatFaqSections( sb, new BodyFormat() );
        return sb.toString();
    }


    protected String getRootNodeName() {
        return "faqs";
    }


    public Section createSection() {
        Section section = new Section();
        _sections.add( section );
        return section;
    }


    private void formatFaqSections( StringBuffer sb, final FaqSectionFormat format ) {
        int i = 0;
        for (int j = 0; j < _sections.size(); j++) {
            Section section = (Section) _sections.get( j );
            section.append( sb, i, format );
            i += section.getNumItems();
        }
    }


    interface FaqSectionFormat {
        void appendSectionStart( StringBuffer sb, int startIndex, String title );
        void appendSectionEnd( StringBuffer sb );
        void appendEntry( StringBuffer sb, String id, String question, String answer );
        void appendExternalEntry( StringBuffer sb, String url, String question );
    }


    class IndexFormat implements FaqSectionFormat {
        public void appendSectionStart( StringBuffer sb, int startIndex, String title ) {
            sb.append( LINE_BREAK ).append( "<p class='listSubtitle'>" ).append( title ).append( "</p>" );
            sb.append( LINE_BREAK ).append( "<ol start='" ).append( startIndex+1 ).append( "'>" );
        }

        public void appendSectionEnd( StringBuffer sb ) {
            sb.append( LINE_BREAK ).append( "</ol>" );
        }

        public void appendEntry( StringBuffer sb, String id, String question, String answer ) {
            sb.append( LINE_BREAK ).append( "  <li><a href='#" ).append( id ).append( "'>" ).append( question ).append( "</a></li>" );
        }


        public void appendExternalEntry( StringBuffer sb, String url, String question ) {
            sb.append( LINE_BREAK ).append( "  <li><a href='" ).append( url ).append( "'>" ).append( question ).append( "</a></li>" );
        }

    }


    class BodyFormat implements FaqSectionFormat {
        public void appendSectionStart( StringBuffer sb, int startIndex, String title ) {
            sb.append( LINE_BREAK ).append( "<h2>" ).append( title ).append( "</h2>" );
        }


        public void appendSectionEnd( StringBuffer sb ) {
        }


        public void appendEntry( StringBuffer sb, String id, String question, String answer ) {
            sb.append( LINE_BREAK ).append( "<h3><a name='" ).append( id ).append( "'></a>" ).append( question ).append( "</h3>" );
            sb.append( answer );
        }


        public void appendExternalEntry( StringBuffer sb, String url, String question ) {
        }

    }



    public class Section {

        private String _title;
        private ArrayList _entries = new ArrayList();

        public void setTitle( String title ) {
            _title = title;
        }


        public FaqEntry createFaq() {
            FaqEntry entry = new FaqEntry();
            _entries.add( entry );
            return entry;
        }


        public int getNumItems() {
            return _entries.size();
        }


        public void append( StringBuffer sb, int startingIndex, FaqSectionFormat format ) {
            format.appendSectionStart( sb, startingIndex, _title );
            for (int i = 0; i < _entries.size(); i++) {
                FaqEntry faqEntry = (FaqEntry) _entries.get( i );
                if (faqEntry.getUrl() == null) {
                    format.appendEntry( sb, faqEntry.getId(), faqEntry.getQuestion(), faqEntry.getAnswer() );
                } else {
                    format.appendExternalEntry( sb, faqEntry.getUrl(), faqEntry.getQuestion() );
                }
            }
            format.appendSectionEnd( sb );
        }

    }


    public class FaqEntry {

        private String _id;
        private String _url;
        private String _question;
        private String _answer;


        public String getId() {
            return _id;
        }


        public void setId( String id ) {
            _id = id;
        }


        public String getUrl() {
            return _url;
        }


        public void setUrl( String url ) {
            _url = url;
        }


        public String getQuestion() {
            return _question;
        }


        public void setQuestion( String question ) {
            _question = question;
        }


        public String getAnswer() {
            return _answer;
        }


        public void setAnswer( String answer ) {
            _answer = answer;
        }
    }
}

