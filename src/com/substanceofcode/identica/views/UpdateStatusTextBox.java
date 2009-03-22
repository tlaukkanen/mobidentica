/*
 * UpdateStatusTextBox.java
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

package com.substanceofcode.identica.views;

import com.substanceofcode.identica.IdenticaController;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author Tommi Laukkanen
 */
public class UpdateStatusTextBox extends TextBox implements CommandListener {

    private IdenticaController controller;
    private Command okCommand;
    private Command cancelCommand;
    
    public UpdateStatusTextBox(IdenticaController controller, String prefix) {
        super("Status", prefix, 140, TextField.ANY);
        this.controller = controller;
        
        okCommand = new Command("OK", Command.OK, 1);
        this.addCommand(okCommand);
        
        cancelCommand = new Command("Cancel", Command.CANCEL, 2);
        this.addCommand(cancelCommand);
        
        this.setCommandListener(this);
    }
    
    /** 
     * Handle commands.
     * @param cmd   Activated command.
     * @param disp  Display.
     */
    public void commandAction(Command cmd, Displayable disp) {
        if(cmd==okCommand) {
            controller.updateStatus(this.getString());
        } else {
            controller.showRecentTimeline();
        }
    }

}
