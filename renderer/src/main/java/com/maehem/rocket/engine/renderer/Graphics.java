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
package com.maehem.rocket.engine.renderer;

import com.maehem.rocket.engine.renderer.tile.StructureTile;
import com.maehem.rocket.engine.renderer.tile.TerrainTile;
import com.maehem.rocket.engine.renderer.debug.Debug;
import com.maehem.rocket.engine.data.Cell;
import com.maehem.rocket.engine.data.Point;
import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.engine.renderer.ui.EventBubble;
import com.maehem.rocket.engine.renderer.ui.UI;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

/**
 *
 * @author Mark J Koch <rocketcolony-maintainer@maehem.com>
 */
public class Graphics {

    private static final Logger LOGGER = Logger.getLogger(Graphics.class.getName());

    public Game game;
    public final Debug debug = new Debug(this);
    public final UI ui;
    public Canvas canvas;

    public final static String TERRAIN_TILES_DIR = "terrain";
    public final static String BUILDING_TILES_DIR = "structures";

    public final TerrainTile terrainTiles[] = new TerrainTile[100];
    public final StructureTile tiles[] = new StructureTile[500];

    public final ArrayList<EventBubble> bubbles = new ArrayList<>();

    private int animationFrame = 0;

    public final static int ANIMATION_FRAME_RATE = 500; // Should be each game tick.

    public final static int MAX_ANIMATION_FRAMES = 512;
    //private int currentFrame = 0;

    public static final int TILE_WIDTH = 128; // 128px wide tiles.
    public final static int LAYER_OFFSET = 48;

    // TODO maybe move this to UI ?
    public int xOffset = 0;
    public int yOffset = 0;

    //public int scale = 1;
    private Cell lookAt;

    public boolean showBuildings = true;
    public boolean showNetworks = true;
    public boolean showTerrain = true;
    public boolean showZones = true;
    public boolean showWater = true;
    public boolean showBubbles = true;
    public boolean showGrid = true;

    public Graphics(Game game) {
        this.game = game;

        System.setProperty("Font", "Verdana");


        debug.enabled = true;
        ui = new UI(game, this);
    }

    public final void init() {
        this.animationFrame = MAX_ANIMATION_FRAMES;
        loadTerrainTiles();
        loadBuildingTiles();

        game.init();
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;

        // Listen to the scroll wheel
        canvas.setOnScroll((t) -> {
            //LOGGER.info("Scroll Delta X: " + t.getDeltaX() + "   Scroll Delta Y: " + t.getDeltaY());
            ui.zoom(t.getDeltaY() / 30.0);
        });

        canvas.setOnMousePressed((t) -> {
            ui.mousePressed(t);
        });

        canvas.setOnMouseReleased((t) -> {
            ui.mouseReleased(t);
        });

        canvas.setOnMouseMoved((t) -> {
            ui.mouseMoved(t);
        });

        canvas.setOnMouseDragged((t) -> {
            ui.mouseDragged(t);
        });

        canvas.setOnMouseClicked((t) -> {
            ui.mouseClicked(t);
        });
    }

    public void render(GraphicsContext gc) {
        // Save the transform since we will be reseting it when we draw
        // any overlay features.
        Affine originalTransform = gc.getTransform();

        clearCanvas(gc);
        debug.begin();

        // Make coordinate 0,0 the center of the view area.
        gc.translate(gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2);
        gc.scale(ui.zoom, ui.zoom); // Scale the view to our zoom setting.

        TerrainRenderer.drawDirtEdge(this, gc);
        drawTiles(gc);
        TerrainRenderer.drawWaterEdge(this, gc);

        ui.draw(gc);

//        debug.drawCrosshairs(g);
        gc.setTransform(originalTransform);
        // Now draw window-relative information.
        //debug.debugOverlay(g2);
        debug.end();
    }

    private void drawTiles(GraphicsContext gc) {
        for (int y = 0; y < game.data.getMapSize(); y++) {
            for (int x = 0; x < game.data.getMapSize(); x++) {
                Point p = game.data.getViewXY(x, y); // Get a X/Y with consideration for viewed rotation
                Cell cell = game.data.map[p.x][p.y];

                // Draw terrain tile
                if (showTerrain) {
                    TerrainRenderer.drawTile(this, gc, cell, p);
                }

                // Draw zone tile
                if (showZones) {
                    ZoneRenderer.drawTile(this, gc, p);
                }
                
                if ( showGrid ) {
//                    int drawX = getDrawX(p.x, p.y);
//                    int drawY = getDrawY(p.x, p.y);
                    TerrainRenderer.drawOutline(this, gc, cell.terrain.id, cell.altm.altitude, p);
                }
                
                // Draw network tile
                if (showNetworks) {
                    NetworkRenderer.drawTile(this, gc, cell, p);
                }
                // Draw building tile
                if (showBuildings) {
                    StructureRenderer.drawTile(this, gc, cell, p);
                }

                if (showBubbles) {
                    bubbles.forEach((b) -> {
                        if (b.getCell().x == p.x && b.getCell().y == p.y) {
                            b.draw(this, gc);
                        }
                    });
                    bubbles.removeIf((e) -> {
                        return e.isDone();
                    });
                }
            }
        }
    }

    // TODO need to update xOffset/yOffset for each rotation.
    // Maybe calculate on every roatation change.
    public void lookAt(Cell cell) {
        this.lookAt = cell;
        ui.setSelectedCell(cell);
        updateOffset();
    }

    public void updateOffset() {
        xOffset = 0;
        yOffset = 0;
        if (null != lookAt) {
            Point mapLocation = lookAt.getMapLocation();
            xOffset
                    = -getDrawX(mapLocation.x, mapLocation.y);
            yOffset
                    = -getDrawY(mapLocation.x, mapLocation.y)
                    + (lookAt.altm.altitude * LAYER_OFFSET);
        }
    }

    /**
     * Update animation state.
     *
     */
    public void animationFrames() {
        animationFrame--;

        if (animationFrame < 0) {
            animationFrame = MAX_ANIMATION_FRAMES;
        }
    }

    public void clearCanvas(GraphicsContext gc) {
        // TODO:  Horizon gradient
        // Clear the canvas
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.AQUA);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    /**
     * Returns the current animation frame for a given tile object
     *
     * @param tile
     * @return current animation frame
     */
    public int getFrame(StructureTile tile) {
        int frameCount = tile.getFrameCount();

        if (frameCount == 1) {
            return 0;
        }

        return (int) (animationFrame - Math.floor(animationFrame / frameCount) * frameCount);
    }

    /**
     * Returns the current animation frame for a given tile object
     *
     * @param tile
     * @return current animation frame
     */
    public int getFrame(TerrainTile tile) {
        int frameCount = tile.getFrameCount();

        if (frameCount == 1) {
            return 0;
        }

        return (int) (animationFrame - Math.floor(animationFrame / frameCount) * frameCount);
    }

    /**
     * Gets the screen pixel center drawing X-axis location of this Cell
     * coordinate.
     *
     * @param cx X grid location
     * @param cy Y grid location
     * @return pixel X location
     */
    public int getDrawX(int cx, int cy) {

        int grid = TILE_WIDTH / 2;
        int gx = cx * grid;
        int gy = cy * grid;

        switch (game.data.mapInfo.getRotation()) {
            default:
            case 0:
                return xOffset /*- game.data.getMapSize() * grid*/ + gx - gy;
            case 1:
                return xOffset + game.data.getMapSize() * grid - gx - gy - grid; //  32 * (-cx+cy)
            case 2:
                return xOffset /*+ game.data.getMapSize() * grid*/ - gx + gy;
            case 3:
                return xOffset - game.data.getMapSize() * grid + gx + gy + grid;
        }
    }

    /**
     * Gets the screen pixel center drawing Y-axis location of this Cell
     * coordinate.
     *
     * @param cx x grid location.
     * @param cy y grid location.
     * @return pixel Y location
     */
    public int getDrawY(int cx, int cy) {
        int grid = TILE_WIDTH / 4;
        int gx = cx * grid;
        int gy = cy * grid;

        switch (game.data.mapInfo.getRotation()) {
            default:
            case 0:
                return yOffset + gx + gy + grid;
            case 1:
                return yOffset + game.data.getMapSize() * grid + gx - gy;
            case 2:
                return yOffset + game.data.getMapSize() * 2 * grid - gx - gy - grid;
            case 3:
                return yOffset + game.data.getMapSize() * grid - gx + gy;
        }
    }

    private void loadTerrainTiles() {
        // Need a tile list.
        LOGGER.log(Level.INFO, "Loading Terrain Tiles...");

        for (int i = 0; i < 100; i++) {
            try {
                terrainTiles[i] = new TerrainTile(TERRAIN_TILES_DIR, i);
            } catch (FileNotFoundException ex) {
                LOGGER.log(Level.FINEST, ex.toString(), ex);
                break;
            }
        }
    }

    private void loadBuildingTiles() {
        // Need a tile list.
        LOGGER.log(Level.INFO, "Loading Building Tiles...");

        for (int i = 0; i < 500; i++) {
            try {
                tiles[i] = new StructureTile(BUILDING_TILES_DIR, i, TILE_WIDTH);
            } catch (FileNotFoundException ex) {
                LOGGER.log(Level.FINEST, ex.toString(), ex);
                break;
            }
        }
    }
    
    public double getTickRate() {
        return 0.066; // 66mS == 15FPS
    }
    
    /**
     * This is the game loop.
     * 
     */
    public void tick() {
            game.tick();  // Game logic state update.

            animationFrames(); // Update animated tiles state.                    
            render(canvas.getGraphicsContext2D());        
    }

}
