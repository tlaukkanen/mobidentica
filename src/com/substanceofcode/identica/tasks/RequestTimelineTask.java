/*
 * RequestFriendsTimelineTask.java
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

package com.substanceofcode.identica.tasks;

import com.substanceofcode.tasks.AbstractTask;
import com.substanceofcode.identica.IdenticaApi;
import com.substanceofcode.identica.IdenticaController;
import java.util.Vector;

/**
 * RequestFriendsTimelineTask
 * 
 * @author Tommi Laukkanen (tlaukkanen at gmail dot com)
 */
public class RequestTimelineTask extends AbstractTask {

    private IdenticaController controller;
    private IdenticaApi api;
    private int feedType;
    public final static int FEED_FRIENDS = 0;
    public final static int FEED_RESPONSES = 1;
    public final static int FEED_ARCHIVE = 2;
    public final static int FEED_PUBLIC = 3;
    public final static int FEED_DIRECT = 4;
    
    /** 
     * Creates a new instance of RequestFriendsTimelineTask.
     * @param controller 
     * @param api
     * @param feedType 
     */
    public RequestTimelineTask(
            IdenticaController controller,
            IdenticaApi api,
            int feedType) {
        this.controller = controller;
        this.api = api;
        this.feedType = feedType;
    }

    public void doTask() {
        if(feedType==FEED_FRIENDS) {
            Vector friendsTimeline = api.requestFriendsTimeline();
            controller.setRecentTimeline( friendsTimeline );
            controller.showTimeline( friendsTimeline );
        } else if(feedType==FEED_ARCHIVE) {
            Vector archiveTimeline = api.requestUserTimeline();
            controller.setUserTimeline( archiveTimeline );
            controller.showTimeline( archiveTimeline );
        } else if(feedType==FEED_RESPONSES) {
            Vector responsesTimeline = api.requestResponsesTimeline();
            controller.setResponsesTimeline( responsesTimeline );
            controller.showTimeline( responsesTimeline );
        } else if(feedType==FEED_PUBLIC) {
            Vector publicTimeline = api.requestPublicTimeline();
            controller.setPublicTimeline( publicTimeline );
            controller.showTimeline( publicTimeline );
        } else if(feedType==FEED_DIRECT) {
            Vector directTimeline = api.requestDirectTimeline();
            controller.setDirectTimeline(directTimeline);
            controller.showTimeline( directTimeline );
        }
        
    }

}
