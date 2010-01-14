package com.meterware.httpunit.site;
/********************************************************************************************************************
 * $Id: News.java 541 2003-05-18 05:20:47Z russgold $
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
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class News extends FragmentTemplate  {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat( "d MMM yyyy");

    private ArrayList _items = new ArrayList();

    public FragmentTemplate newFragment() {
        return new News();
    }


    public String asText() {
        StringBuffer sb = new StringBuffer( "<h2>News</h2>" );
        sb.append( LINE_BREAK ).append( "<table>" );
        for (int i = 0; i < _items.size(); i++) {
            Item item = (Item) _items.get( i );
            sb.append( item.asText( i % 2 ) );
        }
        sb.append( LINE_BREAK ).append( "</table>" );
        return sb.toString();
    }


    protected String getRootNodeName() {
        return "news";
    }


    public Item createItem() {
        Item item = new Item();
        _items.add( item );
        return item;
    }


    public class Item {

        private Date _date;
        private String _text;
        private String _url;

        public String asText( int styleIndex ) {
            StringBuffer sb = new StringBuffer( LINE_BREAK );
            sb.append( "<tr><td class='news' align='right'>" );
            sb.append( DATE_FORMAT.format( _date ) ).append( "</td><td class='news'>" );
            if (_url != null) sb.append( "<a href='" ).append( _url ).append( "'>" );
            sb.append( _text );
            if (_url != null) sb.append( "</a>" );
            sb.append( "</td></tr>" );
            return sb.toString();
        }


        public void setDate( Date date ) {
            _date = date;
        }


        public void setText( String text ) {
            _text = text;
        }


        public void setUrl( String url ) {
            _url = url;
        }
    }
}
