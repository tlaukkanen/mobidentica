/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.identica;

import com.substanceofcode.identica.model.Status;
import com.substanceofcode.identica.model.User;
import com.substanceofcode.utils.CustomInputStream;
import com.substanceofcode.utils.Log;
import com.substanceofcode.utils.ResultParser;
import com.substanceofcode.utils.XmlParser;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author tommi
 */
public class UsersParser implements ResultParser {

    Vector users;

    public Vector getUsers() {
        return users;
    }

    /**
     * Users parsing.
     * Sample XML
     * <users>
     * <user>
     *   <id>14550388</id>
     *   <name>jeffsutherland</name>
     *   <screen_name>jeffsutherland</screen_name>
     *   <location>Boston</location>
     *   <description>Scrum trainer</description>
     *   <profile_image_url>
     *     http://s3.amazonaws.com/twitter_production/profile_images/64612158/jefflean_normal.jpg
     *   </profile_image_url>
     *   <url>http://jeffsutherland.com/scrum</url>
     *   <protected>false</protected>
     *   <followers_count>276</followers_count>
     *   <status>
     *     <created_at>Sat Jan 10 16:20:13 +0000 2009</created_at>
     *     <id>1109218902</id>
     *     <text>"Study hard and win big!" Musashi http://ad.vu/35qj</text>
     *     <source><a href="http://www.adjix.com">Adjix</a></source>
     *     <truncated>false</truncated>
     *     <in_reply_to_status_id/>
     *     <in_reply_to_user_id/>
     *     <favorited>false</favorited>
     *     <in_reply_to_screen_name/>
     *  </status>
     * </user>
     * </users>
     * @param is
     */
    public void parse(CustomInputStream is) throws IOException {
        try {
            XmlParser xml = new XmlParser(is);
            users = new Vector();
            while (xml.parse() != XmlParser.END_DOCUMENT) {
                String elementName = xml.getName();
                if (elementName.equals("user")) {
                    String userXml = xml.getInnerXml();
                    User user = parseUser( userXml );
                    if(user!=null) {
                        users.addElement( user );
                    }
                }
            }
        } catch (IOException ex) {
            throw new IOException("IOException in UserParser.parse(): " + ex.getMessage());
        }
    }

    private Status parseStatus(String screenName, String statusXml) 
            throws IOException {
        String state = "";
        try {
            XmlParser xml = new XmlParser(statusXml);
            String text = "";
            Date date = null;
            String id = "";
            while (xml.parse() != XmlParser.END_DOCUMENT) {
                String elementName = xml.getName();
                if(elementName.equals("text")) {
                    text = xml.getText();
                } else if(elementName.equals("id")) {
                    id = xml.getText();
                } else if(elementName.equals("created_at")) {
                    String dateValue = xml.getText();
                    date = StatusFeedParser.parseDate( dateValue );
                }
            }
            Status status = new Status(screenName, text, date, id);
            return status;
        } catch (Exception ex) {
            throw new IOException("Err in parseStatus: " + ex.getMessage());
        }
    }

    private User parseUser(String userXml) throws IOException {
        if(userXml==null) {
            return null;
        }
        String state = "";
        try {
            //System.out.println("USERXML: " + userXml);
            XmlParser xml = new XmlParser(userXml);
            String id = "";
            String screenName = "";
            String location = "";
            Status status = null;
            state = "starting parsing ";
            while (xml.parse() != XmlParser.END_DOCUMENT) {
                String elementName = xml.getName();
                if(elementName.equals("id")) {
                    state = "getting id";
                    id = xml.getText();
                } else if(elementName.equals("screen_name")) {
                    state = "getting screen_name";
                    screenName = xml.getText();
                } else if(elementName.equals("location")) {
                    state = "gettin location";
                    location = xml.getText();
                } else if(elementName.equals("status")) {
                    state = "getting status";
                    String statusXml = xml.getOuterXml();
                    status = parseStatus(screenName, statusXml);
                }
            }
            state = "creating new user instance";
            User user = new User(id, screenName, location, status);
            return user;
        } catch (IOException ex) {
            String sample = userXml;
            throw new IOException("Err while " + state + " in UsersParser.parseUser: " + ex.getMessage()
                    + "\nXML: " + sample);
        }
    }

}
