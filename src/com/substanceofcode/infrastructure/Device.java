/*
 * Device.java
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

package com.substanceofcode.infrastructure;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen at gmail dot com)
 */
public class Device {

    public static boolean isNokia() {
        try {
            Class.forName("com.nokia.mid.ui.DeviceControl");
            return true;
        } catch (Exception ex) {
            return false;
        }        
    }
    
    public static boolean isSonyEricsson() {
        try {
            Class.forName("com.nokia.mid.ui.DeviceControl");
            return true;
        } catch (Exception ex) {
            return false;
        }        
    }

    public static boolean isSiemens() {
        try {
            /* if this class is found, the phone is a siemens phone */
            Class.forName("com.siemens.mp.game.Light");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
        
    public static boolean isSamsung() {
        try {
            /* if this class is found, the phone is a samsung phone */
            Class.forName("com.samsung.util.LCDLight");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String getVendorName() {
        if(isNokia()) {
            return "Nokia";
        } else if(isSonyEricsson()) {
            return "Sony Ericsson";
        } else if(isSiemens()) {
            return "Siemens";
        } else if(isSamsung()) {
            return "Samsung";
        } else {
            return "Unknown";
        }
    }
    
    public static String getPhoneType() {
        try {
            return System.getProperty("microedition.platform");
        } catch(Exception ex) {
            return "Unknown";
        }
    }
    
}
