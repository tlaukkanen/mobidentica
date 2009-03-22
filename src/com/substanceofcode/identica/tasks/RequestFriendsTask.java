/*
 * RequestFriendsTask.java
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

package com.substanceofcode.identica.tasks;

import com.substanceofcode.tasks.AbstractTask;
import com.substanceofcode.identica.IdenticaApi;
import com.substanceofcode.identica.IdenticaController;
import java.util.Vector;

/**
 *
 * @author Tommi Laukkanen
 */
public class RequestFriendsTask extends AbstractTask {

    IdenticaController controller;
    IdenticaApi api;

    public RequestFriendsTask(IdenticaController controller, IdenticaApi api) {
        this.controller = controller;
        this.api = api;
    }

    public void doTask() {
        String state = "";
        try {
            state = "requesting friends";
            Vector friends = api.requestFriends();
            state = "showing friends";
            controller.showFriends( friends );
        } catch(Exception ex) {
            controller.showError("Error while " + state + ": " + ex.getMessage());
        }
    }

}
