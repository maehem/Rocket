/*
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

*/
package com.maehem.rocket.engine.renderer.tile;

import com.maehem.rocket.tiles.Tiles;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * StructureTile is read-only game content representing a road, pipe, power line,
 * lot, water, building, or other game element that might appear somewhere in the city.
 *
 * The variables of this object are also backed by resources like PNG images
 * found in the file-system.
 *
 * @author maehem
 */
public class StructureTile {

    private static final Logger LOGGER = Logger.getLogger(StructureTile.class.getName());

    public int id;
    public String name;
    public String description;
    public String type;  // TODO: Change to ENUM
    public Image image[];
    public boolean flip_h;
    //boolean flip_alt_tile;

    public char[] slopes;

    private final static int[] ROTATABLE_TILE_BASES = {
//        16
    };

    private final static int[] NON_ROTATE_TILES = {
//        28
    };

    private final static int[] TWO_DIRECTION_TILES = {
//        71, 73
    };

    // Plane, Helicopter, Boat, Rail Car
    // Eight directions:  5 have tiles, 3 are flips
    private final static int[] FIVE_DIRECTION_TILES = {
//        359
    };

    private final static int[] FLIP_TILES = {
//        81, 
    };

    public StructureTile(String tilesDir, int id, int width) throws FileNotFoundException {
        this.id = id;
        initImage(tilesDir, width);
    }

    private void initImage(String tilesDir, int width) throws FileNotFoundException {
        InputStream dirInputStream = Tiles.getInputStream(tilesDir);

        if (dirInputStream == null) {
            LOGGER.log(Level.SEVERE, "Tiles Directory [{0}] does not exist!  This will fail.", tilesDir);

            throw new FileNotFoundException("Tiles Directory [" + tilesDir + "] does not exist in the jar!  This will fail.");
        }
        try {
            dirInputStream.close();
        } catch (IOException ex) {
            LOGGER.warning(ex.toString());
        }

        // Try to get the tileId-1.png file.  If it's there, then get the whole set.
        int index = 0;
        URL resource;
        ArrayList<Image> imageList = new ArrayList<>();
        while ((resource = Tiles.getResource(tilesDir + "/" + id + "-" + index + ".png")) != null) {

            Image b;
            try (InputStream is = resource.openStream()) {
                b = loadImage(is);
                imageList.add(b);
                index++;
            } catch (IOException ex) {
                LOGGER.warning(ex.toString());
                break;
            }
        }
        // If we found animated tiles, then use those.  
        // Otherwise, use non-animated tile.
        if (!imageList.isEmpty()) {
            image = imageList.toArray(new Image[imageList.size()]);
        } else {
            resource = Tiles.getResource(tilesDir + "/" + id + ".png");
            if (resource != null) {
                try {
                    Image b = loadImage(resource.openStream());
                    if (b != null) {
                        image = new Image[]{b};
                        LOGGER.log(Level.CONFIG, "Found tile image: thing/{0}.png", id);
                    }
                } catch (IOException ex) {
                    // We found the resource but it didn't load. Maybe not an image file?
                    // TODO maybe throw this exception.
                    LOGGER.log( Level.WARNING, ex.toString(), ex );
                }
            } else {
                LOGGER.log(Level.FINEST, "Could not find image file [{0}/{1}.png]", new Object[]{tilesDir, id});
            }
        }

    }
    
    public int getLotSize(int minCellPixelWidth) {
        return getWidth() / minCellPixelWidth;
    }


    private Image loadImage(InputStream is) {
        return new Image(is);
    }

    public void draw(GraphicsContext g, int x, int y) {
        g.drawImage(image[0], x, y);
    }

    int getWidth() {
        return (int) image[0].getWidth();
    }

    public static int getTileIdRotated(int tid, int rotation, int mapRotation) {

        if (    tid < 200
 //               || (tid > 99 && tid < 200)
                || Arrays.binarySearch(NON_ROTATE_TILES, tid) >= 0) {
            return tid;
        }

        // TODO: Figure out an algorithm for two direction tiles.
//        if ( tid == 285 || tid == 286 && rotation == 2) {
//            return tid;  // Same for those rotations
//        }
//        if ( tid == 285 && ( rotation == 1 || rotation == 3 )) {
//            return 286;
//        } else if ( tid == 286 && (rotation == 1 || rotation == 3 )) {
//            return 285;
//        }
        for (int i = 0; i < TWO_DIRECTION_TILES.length; i++) {
            int base = TWO_DIRECTION_TILES[i];
            int r = tid - base;
            if ((r >= 0 && r < 2)) {
                r = (r + rotation) % 2;

                return base + r;
            }
        }

        // At this point, the tile has four states.
        for (int i = 0; i < ROTATABLE_TILE_BASES.length; i++) {
            int base = ROTATABLE_TILE_BASES[i];
            int r = tid - base;
            if (r >= 0 && r < 4) {
                r = (r + rotation) % 4;
                // base is our cluster of four rotations.
                return base + r;
            }
        }

        // Bridge special case.  81-85 swap 82-84 swap on rot 2-3?
        if ((tid == 81 || tid == 85) && rotation > 1) {
            return tid ^ 0x4; // Toggle bit 3
        }
        if ((tid == 82 || tid == 84) && rotation > 1) {
            return tid ^ 0x6;  // Toggle bits 2 & 3
        }

        return tid;  // Not a tile in this set.
    }

    public boolean isFlipped(int rotation) {
        return (Arrays.binarySearch(FLIP_TILES, id) >= 0
                && rotation % 2 == 0);
    }

    public int getFrameCount() {
        return image.length;
    }
}
