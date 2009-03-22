/*
 * HttpUtil.java
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

package com.substanceofcode.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 *
 * @author Tommi Laukkanen
 */
public class HttpUtil extends HttpAbstractUtil {

    /** Total bytes transfered */
    private static long totalBytes = 0;
    
    /** Creates a new instance of HttpUtil */
    public HttpUtil() {
    }

    public static String doPost(String url) throws IOException, Exception {
        return HttpUtil.doPost(url, null);
    }

    public static String doGet(String url) throws IOException, Exception {
        return doRequest(url, null, HttpConnection.GET);
    }

    public static String doGet(String url, ResultParser parser) throws IOException, Exception {
        return doRequest(url, parser, HttpConnection.GET);
    }

    public static String doPost(String url, ResultParser parser) throws IOException, Exception {
        return doRequest(url, parser, HttpConnection.POST);
    }

    public static String doRequest(String url, ResultParser parser, String requestMethod) throws IOException, Exception {
        HttpConnection hc = null;
        DataInputStream dis = null;
        String response = "";
        try {
            /**
             * Open an HttpConnection with the Web server
             * The default request method is GET
             */
            hc = (HttpConnection) Connector.open( url );
            hc.setRequestMethod(requestMethod);
            /** Some web servers requires these properties */
            //hc.setRequestProperty("User-Agent",
            //        "Profile/MIDP-1.0 Configuration/CLDC-1.0");
            hc.setRequestProperty("Content-Length", "0");
            hc.setRequestProperty("Connection", "close");

            Log.add("Posting to URL: ");
            Log.add(url);

            // Cookie: name=SID; domain=.google.com; path=/; expires=1600000000; content=
            if (cookie != null && cookie.length() > 0) {
                hc.setRequestProperty("Cookie", cookie);
            }

            if (username.length() > 0) {
                /**
                 * Add authentication header in HTTP request. Basic authentication
                 * should be formatted like this:
                 *     Authorization: Basic QWRtaW46Zm9vYmFy
                 */
                String userPass;
                Base64 b64 = new Base64();
                userPass = username + ":" + password;
                userPass = b64.encode(userPass.getBytes());
                hc.setRequestProperty("Authorization", "Basic " + userPass);
            }


            /**
             * Get a DataInputStream from the HttpConnection
             * and forward it to XML parser
             */
            InputStream his = hc.openInputStream();
            CustomInputStream is = new CustomInputStream(his);

            /** Check for the cookie */
            String sessionCookie = hc.getHeaderField("Set-cookie");
            if (sessionCookie != null) {
                int semicolon = sessionCookie.indexOf(';');
                cookie = sessionCookie.substring(0, semicolon);
                Log.debug("Using cookie: " + cookie);
            } else {
                Log.debug("No cookie found");
            }

            if (parser == null) {
                // Prepare buffer for input data
                StringBuffer inputBuffer = new StringBuffer();

                // Read all data to buffer
                int inputCharacter;
                try {
                    while ((inputCharacter = is.read()) != -1) {
                        inputBuffer.append((char) inputCharacter);
                    }
                } catch (IOException ex) {
                    Log.error("Error while reading response: " + ex.getMessage());
                }

                // Split buffer string by each new line
                response = inputBuffer.toString();
                totalBytes += response.length();
            } else {
                parser.parse(is);
            }
            if(his!=null) {
                his.close();
            }
            if(is!=null) {
                is.close();
            }
            // DEBUG_END
        } catch (Exception e) {
            throw new Exception("Error while posting: " + e.toString());
        } finally {
            if (hc != null) {
                hc.close();
            }
            if (dis != null) {
                dis.close();
            }
        }
        return response;
    }
}