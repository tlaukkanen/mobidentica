/*
 * WaitCanvas.java
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

import com.substanceofcode.tasks.AbstractTask;
import com.substanceofcode.identica.IdenticaController;
import com.substanceofcode.utils.ImageUtil;
import com.substanceofcode.utils.Log;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Tommi
 */
public class WaitCanvas extends Canvas implements Runnable {
    
    private IdenticaController controller;
    private String waitText = "Please wait...";
    private Displayable nextScreen;
    private AbstractTask task;
    private Thread thread;
    private Font statusFont;
    private Image loadingImage;
    private int loadingImageIndex;
    private TalkBalloon talkBalloon;
    
    private final Font titleFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
     
        
    public void setWaitText(String text) {
        waitText = text;
    }
    
    /** Creates a new instance of WaitCanvas 
     * @param controller    Application controller.
     * @param task          Task to be executed.
     */
    public WaitCanvas(IdenticaController controller, AbstractTask task) {
        this.setFullScreenMode(true);
        this.controller = controller;
        this.waitText = "Please wait...";
        this.task = task;
        this.talkBalloon = new TalkBalloon(getWidth(), getHeight());
        this.loadingImage = ImageUtil.loadImage("/images/loading.png");
        loadingImageIndex = 0;
        statusFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        thread = new Thread(this);
        thread.start();           
    }
    
    protected void paint(Graphics g) {
        // Get dimensions
        int height = getHeight();
        int width = getWidth();
        
        // Clear the background to white
        g.setColor( Theme.BACKGROUND_COLOR );
        g.fillRect( 0, 0, width, height );
        
        /** Draw title */
        //drawTitleBar(g);
        
        // Write title
        // int titleX = width/2;
        int titleY = height/4;

        talkBalloon.draw(g, waitText, "Mobidentica", titleY);
        
        //g.setColor(0x000000);
        //g.setFont(statusFont);
        //g.drawString("Please wait...", getWidth()/2, titleY-6, Graphics.TOP|Graphics.HCENTER); 
        
        //g.setClip(getWidth()/2-8, titleY+titleFont.getHeight()+14,16,16);
        //g.drawImage(loadingImage, getWidth()/2-8 - loadingImageIndex*16, titleY+titleFont.getHeight()+14, Graphics.TOP|Graphics.LEFT);
    }
    
    private void drawTitleBar(Graphics g) {
        g.setColor( Theme.BACKGROUND_COLOR );
        g.fillRect( 0, 0, getWidth(), titleFont.getHeight()+2);
        g.setFont( titleFont );
        g.setColor( 0x000000 );
        g.drawString("mobidentica",getWidth()/2,titleFont.getHeight(),Graphics.HCENTER|Graphics.BOTTOM);
    }    

    public void run() {
        task.execute(); 
        while(controller.getCurrentDisplay() == this) {            
            try {
                Thread.sleep(100);
                loadingImageIndex++;
                if(loadingImageIndex>7) {
                    loadingImageIndex = 0;
                }
                this.repaint();
                Thread.yield();
            }catch(Exception ex) {
                Log.error("WaitCanvas.run: " + ex.getMessage());
            }
        }
    }
    
}
