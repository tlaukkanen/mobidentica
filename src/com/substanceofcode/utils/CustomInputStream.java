/*
 * CustomInputStream.java
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * InputStream wrapper so that we can get the total amount of bytes transferred
 * count.
 * @author Tommi Laukkanen (tlaukkanen at gmail dot com)
 */
public class CustomInputStream {

    private InputStream stream;
    private InputStreamReader reader;
    
    public CustomInputStream(InputStream stream) {
        this.stream = stream;
        try {
            reader = new InputStreamReader(stream, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            reader = new InputStreamReader(stream);
        }
    }
    
    public int read() throws IOException {
        HttpTransferStatus.addReceivedBytes(1);
        return reader.read();
    }
    
    public void close() throws IOException {
        stream.close();
    }
    
}
