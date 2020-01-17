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

import com.maehem.rocket.engine.data.type.Slope;
import com.maehem.rocket.engine.renderer.Graphics;
import com.maehem.rocket.tiles.Tiles;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * TerrainTile is read-only game content representing flat and sloped terrain.
 *
 * The variables of this object are also backed by resources like PNG images
 * found in the file-system.
 *
 * @author maehem
 */
public class TerrainTile {

    private static final Logger LOGGER = Logger.getLogger(TerrainTile.class.getName());

    public int id;
    public Image image[];
    
    public static final int HIGH = 13;
    public static final int WATER_BASE = 70;
    public static final int WATER_FALL = 79;

    /**
     * The "base" tile and the four sequential tiles that follow it are part
     * of a rotatable sequence.  i.e  1 == base, 2,3,4 are rest of sequence.
     */
    private final static int[] ROTATABLE_TILE_BASES = {
        1,5,9, 
        71, 75, WATER_FALL
    };

    private final static int[] NON_ROTATE_TILES = {
        0,HIGH, WATER_BASE
    };

    private final static int[] TWO_DIRECTION_TILES = {
    };

    private final static int[] FIVE_DIRECTION_TILES = {
    };

    private final static int[] FLIP_TILES = {};
    
    public final static Color  WATER_EDGE_COLOR = new Color( 0.3, 0.5, 0.9, 0.55 );

    private final static double TW = Graphics.TILE_WIDTH;
    private final static double LO = Graphics.LAYER_OFFSET;
    // Translucent water polys drawn on terrain edge.
    public final static Polygon WATER_POLY_FLAT_LEFT        = new Polygon(new double[]{0,LO,     TW/2,80,  TW/2,128,   0,TW*0.75});    
    public final static Polygon WATER_POLY_SLOPE_A_LEFT     = new Polygon(new double[]{0,LO,     TW/2,80,  TW/2,128             });    
    public final static Polygon WATER_POLY_SLOPE_B_LEFT     = new Polygon(new double[]{0,TW*0.75,0,LO,     TW/2, 80             });    
    public final static Polygon WATER_POLY_FLAT_RIGHT       = new Polygon(new double[]{TW/2,80,  TW,LO,    TW,TW*0.75,  TW/2,128});    
    public final static Polygon WATER_POLY_SLOPE_A_RIGHT    = new Polygon(new double[]{TW,LO,    TW/2,80,  TW,TW*0.75           });    
    public final static Polygon WATER_POLY_SLOPE_B_RIGHT    = new Polygon(new double[]{TW,LO,    TW/2,80,  TW/2,128             });

    public TerrainTile(String tilesDir, int id) throws FileNotFoundException {
        LOGGER.setLevel(Level.CONFIG);
        this.id = id;
        initImage(tilesDir);
    }

    private void initImage(String tilesDir) throws FileNotFoundException {
        final String imageName = String.format("%d", id);
        LOGGER.log(Level.FINER, "    {0}\n", id);

        InputStream dirInputStream = Tiles.getInputStream(tilesDir);

        if (dirInputStream == null) {
            LOGGER.log(Level.SEVERE, "Terrain Tiles Directory [{0}] does not exist!  This will fail.", tilesDir);

            throw new FileNotFoundException("Terrain Tiles Directory [" + tilesDir + "] does not exist in the jar!  This will fail.");
        }
        try {
            dirInputStream.close();
        } catch (IOException ex) {
            //LOGGER.warning(ex.toString());
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
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
                //Exceptions.printStackTrace(ex);
                break;
            }
        }
        if ( !imageList.isEmpty() ) {
            // If we found animated tiles, then use those.  
            image = imageList.toArray(new Image[imageList.size()]);
        } else {
            // Otherwise, use non-animated tile.
            resource = Tiles.getResource(tilesDir + "/" + id + ".png");
            if (resource != null) {
                try {
                    Image b = loadImage(resource.openStream());
                    if ( b != null ) {
                        image = new Image[1];
                        image[0] = b;
                        LOGGER.log(Level.CONFIG, "Found tile image: terrain/{0}.png", id);
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

    private Image loadImage(InputStream is) {
        return new Image(is);
    }

    int getTileId() {
        return id;
    }
    
    int getWidth() {
        return (int) image[0].getWidth();
    }

    public int getFrameCount() {
        return image.length;
    }
    
    public static int getTileIdRotated(int tid, int rotation) {

        if ( Arrays.binarySearch(NON_ROTATE_TILES, tid) >= 0
           ) {
            return tid;
        }

        // At this point, the tile has four rotate states.
        for (int i = 0; i < ROTATABLE_TILE_BASES.length; i++) {
            int base = ROTATABLE_TILE_BASES[i];
            int r = tid - base;
            if (r >= 0 && r < 4) {
                r = (r + rotation) % 4;
                // base is our cluster of four rotations.
                return base + r;
            }
        }

        return tid;  // Not a tile in this set.
    }
    
    public Polygon getOutline(/*int tileWidth, int rotation, boolean waterVisible, TerrainInfo terrain*/) {
        int tw = Graphics.TILE_WIDTH-1;
        
        double lo = Graphics.LAYER_OFFSET;
        
        int tid;
//        if ( waterVisible && terrain.surfaceWater && terrain.slope2  != 0/*terrain.slope.compareTo(Slope.MAP[0]) != 0 */) {
//            tid = HIGH;
//        } else {
            //tid = getTileIdRotated(id, rotation, rotation);
            tid = id;
//        }
        
        Slope s = Slope.MAP[tid];
                
        return new Polygon(
                new double[]{
                    -tw/2, (s.l?-lo:0),
                        0, tid==7?(s.b?-(lo-1):0):-tw/4 +(s.t?-lo:0),
                     tw/2, (s.r?-lo:0),
                        0, tw/4 +(s.b?-lo:0)
                }
        );
        
    }

}
