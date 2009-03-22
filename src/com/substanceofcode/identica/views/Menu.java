/*
 * Menu.java
 * 
 * Copyright (C) 2005-2009 Tommi Laukkanen
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

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * Menu implementation that will render an overlay menu.
 * @author Tommi Laukkanen
 */
public class Menu {

    private String[] labels;
    private int screenWidth;
    private int screenHeight;
    private int top;
    private int height;
    private int selectedIndex;
    private boolean active;

    private static final Font TITLE_FONT = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    private static final Font LABEL_FONT = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    private static final int BACK_COLOR = 0xaaaaaa;
    private static final int SELECTED_COLOR = 0xffffff;
    private static final int FONT_COLOR = 0x000000;
    private static final int BORDER_COLOR = 0x666666;
    
    /** Create new Menu instance 
     * @param labels        
     * @param screenWidth 
     * @param screenHeight 
     */
    public Menu(String[] labels, int screenWidth, int screenHeight) {
        this.labels = labels;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.selectedIndex = 0;
        this.active = false;
        
        int rowHeight = LABEL_FONT.getHeight();
        this.height = (labels.length+1) * rowHeight;
        this.top = screenHeight/2 - height/2;
    }
    
    /** 
     * Draw menu
     * @param g Application graphics.
     */
    public void draw(Graphics g) {
        if(active==false) {
            return;
        }
        /** Draw background and borders */
        g.setColor(BACK_COLOR);
        g.fillRect(10, top-2, screenWidth-20, height+4);
        g.setColor(BORDER_COLOR);
        g.drawRect(10, top-2, screenWidth-20, height+4);
        
        /** Draw menu items */
        g.setColor(FONT_COLOR);
        g.setFont(TITLE_FONT);
        
        g.drawString(
            "Menu", 
            screenWidth/2 - LABEL_FONT.stringWidth("Menu")/2, 
            top + LABEL_FONT.getHeight(),
            Graphics.LEFT|Graphics.BOTTOM);
            
        g.setFont(LABEL_FONT);
        for(int menuIndex=0; menuIndex<labels.length; menuIndex++) {
            if(menuIndex==selectedIndex) {
                g.setColor(SELECTED_COLOR);
                g.fillRect(11, top+(menuIndex+1)*LABEL_FONT.getHeight(), screenWidth-22, LABEL_FONT.getHeight());
                g.setColor(FONT_COLOR);
            }
            String label = labels[ menuIndex ];
            int labelWidth = LABEL_FONT.stringWidth(label);
            g.drawString(labels[menuIndex], screenWidth/2 - labelWidth/2, top + (menuIndex+2)*LABEL_FONT.getHeight(), Graphics.LEFT|Graphics.BOTTOM);
        }
    }
    
    public void selectNext() {
        selectedIndex++;
        if(selectedIndex>labels.length-1) {
            selectedIndex = 0;
        }
    }
    
    public void selectPrevious() {
        selectedIndex--;
        if(selectedIndex<0) {
            selectedIndex = labels.length-1;
        }
    }
    
    public int getSelectedIndex() {
        return selectedIndex;
    }

    boolean isActive() {
        return active;
    }
    
    public void activate() {
        active = true;
        selectedIndex = 0;
    }
    
    public void deactivate() {
        active = false;
    }
    
}
