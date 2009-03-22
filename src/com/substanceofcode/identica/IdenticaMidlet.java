/*
 * IdenticaMidlet.java
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

import javax.microedition.midlet.MIDlet;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen at gmail dot com)
 */
public class IdenticaMidlet extends MIDlet {

    private IdenticaController controller;
    
    /** Default constructor for midlet */
    public IdenticaMidlet() {
        try{
            controller = IdenticaController.getInstance(this);
            controller.showSplash();            
        }catch(Exception any){
            any.printStackTrace();
        }
    }
    
    public void startApp() {
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
        try{
        }catch(Exception ex) {
            // Do nothing as we are already exiting
        }
        notifyDestroyed();
    }

}
