/*
 * TabBar.java
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
 * TabBar
 * 
 * @author Tommi Laukkanen (tlaukkanen at gmail dot com)
 */
public class TabBar {

    private int activeTabIndex;
    private int selectedTabIndex;
    private String[] menuLabels;
    private int width;
    private int x;
    
    private static final int COLOR_BACKGROUND = 0x444444;
    private static final int COLOR_INACTIVE = 0xaaaaaa;
    private static final int COLOR_ACTIVE = 0xffffff; //Theme.TWITTER_BLUE_COLOR;
    private static final int COLOR_SELECTED = 0xdddddd; //Theme.TWITTER_BLUE_COLOR;
    private static final int COLOR_TEXT = 0x000000;
    private static final int COLOR_INACTIVE_TEXT = 0x444444;
    
    private static final Font labelFont = Font.getFont(
            Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
    
    /** Creates a new instance of TabBar 
     * @param selectedTabIndex  Currently selected tab index
     * @param menuLabels        Menu label texts
     * @param width             Menu width
     */
    public TabBar(int selectedTabIndex, String[] menuLabels, int width) {
        this.selectedTabIndex = selectedTabIndex;
        this.activeTabIndex = selectedTabIndex;
        this.menuLabels = menuLabels;
        this.width = width;
        
        int textWidth = 0;
        for(int textIndex=0; textIndex<menuLabels.length; textIndex++) {
            textWidth += labelFont.stringWidth(menuLabels[textIndex]) + 3;
        }
        
        this.x = width/2 - textWidth/2;
        
    }
    
    public int getHeight() {
        return labelFont.getHeight() + 2;
    }
    
    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }

    public boolean isSelectedActive() {
        return (activeTabIndex==selectedTabIndex);
    }

    public void activateSelectedTab() {
        activeTabIndex = selectedTabIndex;
    }

    public void resetSelectedTab() {
        selectedTabIndex = activeTabIndex;
    }
    
    public void selectNextTab() {
        selectedTabIndex++;
        if(selectedTabIndex>=menuLabels.length) {
            selectedTabIndex = 0;
        }
    }
    
    public void selectPreviousTab() {
        selectedTabIndex--;
        if(selectedTabIndex<0) {
            selectedTabIndex = menuLabels.length-1;
        }
    }

    private int calculateLeftSide() {
        int textWidth = 0;
        for(int textIndex=0; textIndex<menuLabels.length; textIndex++) {
            String label;
            if(selectedTabIndex==textIndex) {
                label = menuLabels[textIndex];
            } else {
                label = menuLabels[textIndex].substring(0,3);
            }
            textWidth += labelFont.stringWidth(label) + 3;
        }
        return width/2 - textWidth/2;
    }
    
    public void draw(Graphics g, int y) {
        int tabCount = menuLabels.length;
        int col = calculateLeftSide();
        
        // Black menu label text
        g.setColor(COLOR_BACKGROUND);
        g.fillRect(0, y, width, labelFont.getHeight()+3);
        g.setFont(labelFont);

        for(int tab=0; tab<tabCount; tab++) {
            String tabLabel;
            int tabHeight;
             
            // Define tab color and height
            if( tab==selectedTabIndex && tab!=activeTabIndex) {
                g.setColor(COLOR_SELECTED);
                tabHeight = labelFont.getHeight()+1;
            } else if(tab==activeTabIndex) {
                g.setColor(COLOR_ACTIVE);
                tabHeight = labelFont.getHeight()+2;
            } else {
                g.setColor(COLOR_INACTIVE);
                tabHeight = labelFont.getHeight()+1;
            }

            // Define tab label
            if( tab==selectedTabIndex ) {
                tabLabel = menuLabels[tab];
            } else {
                tabLabel = menuLabels[tab].substring(0,3);
            }

            int tabWidth = labelFont.stringWidth(tabLabel)+2;
            g.fillRect(col, y+1, tabWidth, tabHeight);
            
            // Black menu label text
            if(selectedTabIndex==tab) {
                g.setColor(COLOR_TEXT);
            } else {
                g.setColor(COLOR_INACTIVE_TEXT);
            }
            g.drawString(tabLabel, col+1, y+labelFont.getHeight()+2, Graphics.LEFT|Graphics.BOTTOM);

            col += tabWidth+1;
        }
    }
    
}
