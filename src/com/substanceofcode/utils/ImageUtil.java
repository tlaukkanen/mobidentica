/*
 * ImageUtil.java
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

import javax.microedition.lcdui.Image;

/**
 * Image utility for loading images from resources.
 *
 * @author Tommi Laukkanen
 */
public class ImageUtil {
    
    /** Creates a new instance of ImageUtil */
    private ImageUtil() {
    }
    
    /** Load an image 
     * @param   filename 
     * @return 
     */
    public static Image loadImage(String filename) {
        Image image = null;
        try {
            image = Image.createImage(filename);
        } catch(Exception e) {
            System.err.println("Error while loading image: " + filename);
            System.out.println("Description: " + e.toString());
            // Use null
        }
        return image;
    }
    
    /** Scale image */
    public static Image scale(Image src, int width, int height) {
        
        int scanline = src.getWidth();
        int srcw = src.getWidth();
        int srch = src.getHeight();
        int buf[] = new int[srcw * srch];
        src.getRGB(buf, 0, scanline, 0, 0, srcw, srch);
        int buf2[] = new int[width*height];
        for (int y=0;y<height;y++) {
            int c1 = y*width;
            int c2 = (y*srch/height)*scanline;
            for (int x=0;x<width;x++) {
                buf2[c1 + x] = buf[c2 + x*srcw/width];
            }
        }
        Image img = Image.createRGBImage(buf2, width, height, true);
        return img;
    }
    
}
