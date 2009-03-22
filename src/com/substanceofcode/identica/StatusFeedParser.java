/*
 * StatusFeedParser.java
 *
 * Copyright (C) 2005-2008 Tommi Laukkanen
 * http://www.substanceofcode.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.substanceofcode.identica;

import com.substanceofcode.identica.model.Status;
import com.substanceofcode.utils.CustomInputStream;
import com.substanceofcode.utils.ResultParser;
import com.substanceofcode.utils.StringUtil;
import com.substanceofcode.utils.XmlParser;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

/**
 * StatusFeedParser
 * 
 * @author Tommi Laukkanen (tlaukkanen at gmail dot com)
 */
public class StatusFeedParser implements ResultParser {

    private Vector statuses;
    
    /** Creates a new instance of StatusFeedParser */
    public StatusFeedParser() {
        statuses = new Vector();
    }

    /**
     * <status>
<created_at>Wed Aug 29 20:14:27 +0000 2007</created_at>
<id>235455042</id>
<text>
Heheh, I should just go to work for Gawker Media. Now wouldn't THAT be a hoot? (Parent co of Valleywag). One problem: Nick Denton. Boss. ;-)
</text>
−
	<source>
<a href="http://iconfactory.com/software/twitterrific">twitterrific</a>
</source>
−
	<user>
<id>13348</id>
<name>Scobleizer</name>
<screen_name>Scobleizer</screen_name>
<location>Half Moon Bay, California, USA</location>
<description>Tech geek blogger @ http://scobleizer.com</description>
−
	<profile_image_url>
http://assets2.twitter.com/system/user/profile_image/13348/normal/861009_f12647108b.jpg?1182323860
</profile_image_url>
<url>http://scobleshow.com</url>
<protected>false</protected>
</user>
</status>
     */
    
    public Vector getStatuses() {
        return statuses;
    }
    
    public void parse(CustomInputStream is) throws IOException {
        try {
            XmlParser xml = new XmlParser(is);
            boolean doStatus = false;
            String text = "";
            String screenName = "";
            String id = "";
            Date date = null;
            boolean doSender = false;
            boolean doRecipient = false;
            while (xml.parse() != XmlParser.END_DOCUMENT) {
                String elementName = xml.getName();
                
                if (elementName.equals("status")) {
                    if(text.length()>0) {
                        Status status = new Status(screenName, text, date, id );
                        statuses.addElement(status);
                    }
                    text = "";
                    screenName = "";
                    id = "";
                    date = null;
                } else if (elementName.equals("direct_message")) {
                    if(text.length()>0) {
                        Status status = new Status(screenName, text, date, id );
                        statuses.addElement(status);
                    }
                    text = "";
                    screenName = "";
                    id = "";
                    date = null;
                    doSender = false;
                    doRecipient = false;
                } else if (elementName.equals("id") && id.equals("")) {
                    id = xml.getText();
                } else if (elementName.equals("text")) {
                    text += xml.getText();
                } else if(elementName.equals("screen_name")) {
                    if(doSender || (!doSender && !doRecipient)) {
                        screenName = xml.getText();
                    }
                } else if(elementName.equals("sender")) {
                    doSender = true;
                    doRecipient = false;
                } else if(elementName.equals("recipient")) {
                    doSender = false;
                    doRecipient = true;
                } else if(elementName.equals("created_at")) {
                    String dateString = xml.getText();
                    date = parseDate( dateString );                    
                }
                    
            }
            if(text.length()>0) {
                Status status = new Status(screenName, text, date, id);
                statuses.addElement(status);
            }            
        } catch (Exception ex) {
            throw new IOException("Error in StatusFeedParser.parse(): "
                    + ex.getMessage());
        }
    }
    
    /**
     * Parse RSS date format to Date object.
     * Example of RSS date:
     * Sat, 23 Sep 2006 22:25:11 +0000
     */
    public static Date parseDate(String dateString) throws Exception {
        Date pubDate = null;
        try {
            // Split date string to values
            // 0 = week day
            // 1 = day of month
            // 2 = month
            // 3 = year (could be with either 4 or 2 digits)
            // 4 = time
            // 5 = GMT
            int weekDayIndex = 0;
            int dayOfMonthIndex = 2;
            int monthIndex = 1;
            int yearIndex = 5;
            int timeIndex = 3;
            int gmtIndex = 4;
            
            String[] values = StringUtil.split(dateString, " ");
            int columnCount = values.length;
            // Wed Aug 29 20:14:27 +0000 2007
            
            if( columnCount==5 ) {
                // Expected format:
                // 09 Nov 2006 23:18:49 EST
                dayOfMonthIndex = 0;
                monthIndex = 1;
                yearIndex = 2;
                timeIndex = 3;
                gmtIndex = 4;
			} else if( columnCount==7 ) {
                // Expected format:
                // Thu, 19 Jul  2007 00:00:00 N
                yearIndex = 4;
                timeIndex = 5;
                gmtIndex = 6;
            } else if( columnCount<5 || columnCount>6 ) {
                throw new Exception("Invalid date format: " + dateString);
            }
            
            // Day of month
            int dayOfMonth = Integer.parseInt( values[ dayOfMonthIndex ] );
            
            // Month
            String[] months =  {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            String monthString = values[ monthIndex ];
            int month=0;
            for(int monthEnumIndex=0; monthEnumIndex<12; monthEnumIndex++) {
                if( monthString.equals( months[ monthEnumIndex ] )) {
                    month = monthEnumIndex;
                }
            }
            
            // Year
            int year = Integer.parseInt(values[ yearIndex ]);
            if(year<100) {
                year += 2000;
            }
            
            // Time
            String[] timeValues = StringUtil.split(values[ timeIndex ],":");
            int hours = Integer.parseInt( timeValues[0] );
            int minutes = Integer.parseInt( timeValues[1] );
            int seconds = Integer.parseInt( timeValues[2] );
                        
            pubDate = getCal(dayOfMonth, month, year, hours, minutes, seconds);
            
        } catch(Exception ex) {
            // TODO: Add exception handling code
            throw new Exception("Error in parseDate: " + ex.getMessage());
        } catch(Throwable t) {
            // TODO: Add exception handling code
            throw new Exception("Throwable in parseDate: " + t.getMessage());
        }
        return pubDate;
    }    
    
    /** Get calendar date. **/
    public static Date getCal(int dayOfMonth, int month, int year, int hours,
                               int minutes, int seconds) throws Exception {
            // Create calendar object from date values
            Calendar cal = Calendar.getInstance();
            cal.setTimeZone( TimeZone.getTimeZone("GMT+0") );
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.HOUR_OF_DAY, hours);
            cal.set(Calendar.MINUTE, minutes);
            cal.set(Calendar.SECOND, seconds);
                                    
            return cal.getTime();
    }    

}
