/*
 * TwitterController.java
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
import com.substanceofcode.identica.model.User;
import com.substanceofcode.identica.tasks.RequestFriendsTask;
import com.substanceofcode.identica.tasks.RequestTimelineTask;
import com.substanceofcode.identica.tasks.UpdateStatusTask;
import com.substanceofcode.identica.views.AboutCanvas;
import com.substanceofcode.identica.views.LoginForm;
import com.substanceofcode.identica.views.SplashCanvas;
import com.substanceofcode.identica.views.TimelineCanvas;
import com.substanceofcode.identica.views.UpdateStatusTextBox;
import com.substanceofcode.identica.views.WaitCanvas;
import com.substanceofcode.utils.Log;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStoreException;

/**
 * TwitterController controls the application flow.
 * 
 * @author Tommi Laukkanen (tlaukkanen at gmail dot com)
 */
public class IdenticaController {

    IdenticaMidlet midlet;
    Display display;
    IdenticaApi api;
    Settings settings;
    TimelineCanvas timeline;

    Vector publicTimeline;
    Vector recentTimeline;
    Vector archiveTimeline;
    Vector responsesTimeline;
    Vector directTimeline;
    Vector friendsStatuses;

    static IdenticaController instance;

    /**
     * Get controller instance.
     * @return
     */
    public static IdenticaController getInstance() {
        return instance;
    }

    /**
     * Get new instance of controller.
     * @param midlet
     * @return
     */
    public static IdenticaController getInstance(IdenticaMidlet midlet) {
        if(instance==null) {
            instance = new IdenticaController(midlet);
        }
        return instance;
    }

    /** 
     * Creates a new instance of TwitterController
     * @param midlet Application midlet.
     */
    private IdenticaController(IdenticaMidlet midlet) {
        try {
            this.midlet = midlet;
            this.display = Display.getDisplay(midlet);
            this.api = new IdenticaApi();
            this.timeline = new TimelineCanvas(this);
            settings = Settings.getInstance(midlet);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

    public void about() {
        AboutCanvas canvas = new AboutCanvas(this);
        display.setCurrent(canvas);
    }

    public void addStatus(Status status) {
        if(recentTimeline!=null) {
            recentTimeline.insertElementAt(status, 0);
        }
        if(archiveTimeline!=null) {
            archiveTimeline.insertElementAt(status, 0);
        }
    }

    public void clearTimelines() {
        setRecentTimeline(null);
        setPublicTimeline(null);
        setResponsesTimeline(null);
        setUserTimeline(null);
        setDirectTimeline(null);
        setFriendsStatuses(null);
    }

    public MIDlet getMIDlet() {
        return midlet;
    }
    
    public Settings getSettings() {
        return settings;
    }

    public void exit() {
        try {
            midlet.destroyApp(true);
            midlet.notifyDestroyed();
        } catch(Exception ex) {
            Log.error("Exit: " + ex.getMessage());
        }
    }

    public Displayable getCurrentDisplay() {
        return display.getCurrent();
    }

    /** 
     * Login to twitter.
     * @param username Username for Twitter
     * @param password Password for Twitter
     */
    public void login(String username, String password, String serviceUrl) {
        api.setUsername(username);
        api.setPassword(password);
        api.setUrl(serviceUrl);
        showRecentTimeline();
    }

    public void setPublicTimeline(Vector publicTimeline) {
        this.publicTimeline = publicTimeline;
    }

    public void setResponsesTimeline(Vector responsesTimeline) {
        this.responsesTimeline = responsesTimeline;
    }

    public void setServiceUrl(String url) {
        api.setUrl( url );
    }

    public String getServiceUrl() {
        return api.getUrl();
    }

    public void setUserTimeline(Vector archiveTimeline) {
        this.archiveTimeline = archiveTimeline;
    }

    public void setDirectTimeline(Vector directTimeline) {
        this.directTimeline = directTimeline;
    }

    public void setFriendsStatuses(Vector friendStatuses) {
        this.friendsStatuses = friendStatuses;
    }

    public void showDirectMessages() {
        if(directTimeline==null) {
            RequestTimelineTask task = new RequestTimelineTask(
                this, api, RequestTimelineTask.FEED_DIRECT);
            WaitCanvas wait = new WaitCanvas(this, task);
            display.setCurrent(wait);
        } else {
            timeline.setTimeline(directTimeline);
            display.setCurrent(timeline);
        }
    }

    public void showError(String string) {
        Alert alert = new Alert("Error");
        alert.setString(string);
        alert.setTimeout(Alert.FOREVER);
        display.setCurrent(alert, timeline);
    }

    /** Show friends */
    public void showFriends() {
        if(friendsStatuses==null) {
            RequestFriendsTask task = new RequestFriendsTask(this, api);
            WaitCanvas wait = new WaitCanvas(this, task);
            wait.setWaitText("Loading friends");
            display.setCurrent(wait);
        } else {
            timeline.setTimeline(friendsStatuses);
            display.setCurrent(timeline);
        }
    }

    /** Show friends */
    public void showFriends(Vector friends) {
        String state = "";
        int nullUserCount = 0; // Only for debugging purposes
        try {
            if(friends==null) {
                showError("Friends vector is null");
                return;
            }
            state = "initializing vector";
            friendsStatuses = new Vector();
            state = "creating enumeration";
            Enumeration friendEnum = friends.elements();
            state = "starting the loop friends";
            while(friendEnum.hasMoreElements()) {
                state = "getting user from element";
                User user = (User) friendEnum.nextElement();
                if(user==null) {
                    // why?
                    nullUserCount++;
                }
                state = "getting user's last status";
                if(user.getLastStatus()!=null) {
                    state = "adding last status to vector";
                    friendsStatuses.addElement(user.getLastStatus());
                }
            }
            state = "setting friends timeline";
            timeline.setTimeline(friendsStatuses);
            state = "showing timeline";
            display.setCurrent(timeline);
        } catch(Exception ex) {
            this.showError("Error while " + state + ": " + ex.getMessage()
                    + "\nNull users: " + nullUserCount
                    + "\nFriends: " + friends.capacity());
        }
    }

    public void showPublicTimeline() {
        if(publicTimeline==null) {
            RequestTimelineTask task = new RequestTimelineTask(
                this, api, RequestTimelineTask.FEED_PUBLIC);
            WaitCanvas wait = new WaitCanvas(this, task);
            display.setCurrent(wait);
        } else {
            timeline.setTimeline(publicTimeline);
            display.setCurrent(timeline);
        }
    }

    public void showResponsesTimeline() {
        if(responsesTimeline==null) {
            RequestTimelineTask task = new RequestTimelineTask(
                this, api, RequestTimelineTask.FEED_RESPONSES);
            WaitCanvas wait = new WaitCanvas(this, task);
            wait.setWaitText("Loading responses...");
            display.setCurrent(wait);
        } else {
            timeline.setTimeline(responsesTimeline);
            display.setCurrent(timeline);
        }        
    }

    /** Show status updating view. */
    public void showStatusView(String prefix) {
        UpdateStatusTextBox statusView = new UpdateStatusTextBox(this, prefix);
        display.setCurrent(statusView);
    }

    /** 
     * Update Twitter status.
     * @param status    New status
     */
    public void updateStatus(String status) {
        UpdateStatusTask task = new UpdateStatusTask( this, api, status );
        WaitCanvas wait = new WaitCanvas(this, task);
        wait.setWaitText("Updating status...");
        display.setCurrent(wait);
    }
    
    public void useArchiveTimeline() {
        timeline.setTimeline(archiveTimeline);
    }
    
    public void useResponsesTimeline() {
        timeline.setTimeline(responsesTimeline);
    }
    
    public void showArchiveTimeline() {
        if(archiveTimeline==null) {
            RequestTimelineTask task = new RequestTimelineTask(
                this, api, RequestTimelineTask.FEED_ARCHIVE);
            WaitCanvas wait = new WaitCanvas(this, task);
            wait.setWaitText("Loading...");
            display.setCurrent(wait);
        } else {
            timeline.setTimeline(archiveTimeline);
            display.setCurrent(timeline);
        }
    }

    /** 
     * Set friends time line entries.
     * @param friendsTimeline 
     */
    public void setRecentTimeline(Vector friendsTimeline) {
        this.recentTimeline = friendsTimeline;
    }
    
    /** Show login form */
    public void showLoginForm() {
        LoginForm loginForm = new LoginForm( this );
        display.setCurrent( loginForm );
    }

    public void showRecentTimeline() {
        if( recentTimeline==null) {
            RequestTimelineTask task = new RequestTimelineTask(
                this, api, RequestTimelineTask.FEED_FRIENDS);
            WaitCanvas wait = new WaitCanvas(this, task);
            wait.setWaitText("Loading your timeline...");
            display.setCurrent(wait);
        } else {
            timeline.setTimeline( recentTimeline );
            display.setCurrent( timeline );
        }
    }    
    
    public void showTimeline(Vector timelineFeed ) {        
        timeline.setTimeline( timelineFeed );
        display.setCurrent( timeline );
    }

    /** Show splash screen */
    void showSplash() {
        SplashCanvas splash = new SplashCanvas(this);
        display.setCurrent(splash);
    }

}
