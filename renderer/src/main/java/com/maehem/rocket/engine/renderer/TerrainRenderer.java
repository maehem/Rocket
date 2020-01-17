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

import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.engine.renderer.tile.TerrainTile;
import com.maehem.rocket.engine.data.Cell;
import com.maehem.rocket.engine.data.Point;
import com.maehem.rocket.engine.data.type.AltitudeInfo;
import com.maehem.rocket.engine.data.type.TerrainInfo;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Affine;

/**
 *
 * @author Mark J Koch <rocketcolony-maintainer@maehem.com>
 */
public class TerrainRenderer {

    public static void drawTile(Graphics graphics, GraphicsContext gc, Cell cell, Point cellXY) {
        if (cell.terrain.id < 0 || graphics.debug.hideTerrain) {
            return;
        }

        TerrainInfo xter = cell.terrain;
        AltitudeInfo altm = cell.altm;

        int tileId = xter.id;

        int altitude = altm.altitude;

        // Draw dirt tile.
        //drawTile(graphics, gc, tileId, cellXY, altitude * Graphics.LAYER_OFFSET, getTile(graphics, tileId).getOutline(/*Graphics.TILE_WIDTH, graphics.game.data.mapInfo.getRotation(), graphics.showWater, xter*/));
        drawTile(graphics, gc, tileId, cellXY, altitude * Graphics.LAYER_OFFSET, null);

        // Back side road tunnels stick out of the earth and must
        // be rendered here to show up correctly.
//        if (structure.id == XBLD.TUNNEL_LEFT || structure.id == XBLD.TUNNEL_RIGHT) {
        //tileId = structure.id;
        // TODO: Call render BuildingTile for these here.
//        }
        // Overlay water or other features.
        if (graphics.showWater && (xter.surfaceWater || altm.altitude <= graphics.game.data.mapInfo.waterLevel)) {
            int alt;
            if (xter.waterLevel == TerrainInfo.WTR_LVL.SUBMERGED || xter.waterLevel == TerrainInfo.WTR_LVL.SHORE) {
                alt = (graphics.game.data.mapInfo.waterLevel + 1) * Graphics.LAYER_OFFSET;
            } else {
                alt = (altitude + xter.waterDepth) * Graphics.LAYER_OFFSET;
            }
            if (xter.waterLevel == TerrainInfo.WTR_LVL.WATERFALL) {
                drawTile(graphics, gc, TerrainTile.WATER_FALL + TerrainInfo.slopeIndex(cell.terrain.slope) - 1, cellXY, alt, null);
            } else if ((xter.shore & 0b11111111) == 0xFF) {
                // Shore on all eight sides/corners.
                drawTile(graphics, gc, TerrainTile.WATER_BASE, cellXY, alt, null);
            } else if (xter.waterLevel != TerrainInfo.WTR_LVL.DRY) {
                // Shore edges
                if ((xter.shore & 0b00001000) == 0) {
                    drawTile(graphics, gc, TerrainTile.WATER_BASE + 1, cellXY, alt, null);
                }
                if ((xter.shore & 0b00000100) == 0) {
                    drawTile(graphics, gc, TerrainTile.WATER_BASE + 2, cellXY, alt, null);
                }
                if ((xter.shore & 0b00000010) == 0) {
                    drawTile(graphics, gc, TerrainTile.WATER_BASE + 3, cellXY, alt, null);
                }
                if ((xter.shore & 0b00000001) == 0) {
                    drawTile(graphics, gc, TerrainTile.WATER_BASE + 4, cellXY, alt, null);
                }

                // Shore Corners
                if ((xter.shore & 0b10000000) == 0) {
                    drawTile(graphics, gc, TerrainTile.WATER_BASE + 5, cellXY, alt, null);
                }
                if ((xter.shore & 0b01000000) == 0) {
                    drawTile(graphics, gc, TerrainTile.WATER_BASE + 6, cellXY, alt, null);
                }
                if ((xter.shore & 0b00100000) == 0) {
                    drawTile(graphics, gc, TerrainTile.WATER_BASE + 7, cellXY, alt, null);
                }
                if ((xter.shore & 0b00010000) == 0) {
                    drawTile(graphics, gc, TerrainTile.WATER_BASE + 8, cellXY, alt, null);
                }
            }
        }
        // TODO:    Draw fat line if this is a rear edge.
        graphics.debug.terrain(gc, cell);
    }

    /**
     * Draws a tile from the tile map to this graphics context. Converts Cell
     * X/Y to Draw X/Y which also factors in altitude offset.
     *
     * @param gfx Game graphics
     * @param gc JavaFX graphics context
     * @param tileId Tile ID.
     * @param cellXY Cell grid location
     * @param topOffset Altitude 'Z' shift.
     * @param outline Outline shape to draw around this cell.
     */
    public static void drawTile(Graphics gfx, GraphicsContext gc, int tileId, Point cellXY, int topOffset, Polygon outline) {
        int x = gfx.getDrawX(cellXY.x, cellXY.y) - Graphics.TILE_WIDTH / 2;
        int y = gfx.getDrawY(cellXY.x, cellXY.y) - (int) (0.75 * Graphics.TILE_WIDTH) - topOffset;

        TerrainTile tile = getTile(gfx, tileId);
        drawTile(gfx, gc, tile, x, y, outline);
    }

    /**
     * Draw named TerrainTile at this Draw X/Y location.
     *
     * @param gfx
     * @param gc
     * @param tile
     * @param drawX
     * @param drawY
     * @param outline
     */
    private static void drawTile(Graphics gfx, GraphicsContext gc, TerrainTile tile, int drawX, int drawY, Polygon outline) {
        Image img = tile.image[gfx.getFrame(tile)];
        gc.drawImage(img, drawX, drawY);
    }

    public static void drawOutline(Graphics gfx, GraphicsContext gc, int tileId, int altitude, Point p) {
        int x = gfx.getDrawX(p.x, p.y);
        int y = gfx.getDrawY(p.x, p.y) - (altitude*Graphics.LAYER_OFFSET);
        Affine transform = gc.getTransform();
        gc.translate(x, y);
        gc.setLineWidth(1.2);
        gc.setStroke(new Color(0.3,0.1,0.1,0.3));

        Polygon outline = getTile(gfx, tileId).getOutline();
        ObservableList<Double> points = outline.getPoints();
        Iterator<Double> iterator = points.iterator();
        gc.beginPath();
        gc.moveTo(iterator.next(), iterator.next());
        while (iterator.hasNext()) {
            gc.lineTo(iterator.next(), iterator.next());
        }
        gc.closePath();
        gc.stroke();

        gc.setTransform(transform);
    }

    public static void drawTerrainTileIDs(Graphics graphics, GraphicsContext gc) {
        Game game = graphics.game;
        for (int y = 0; y < game.data.getMapSize(); y++) {
            for (int x = 0; x < game.data.getMapSize(); x++) {

                TerrainInfo xter = game.data.map[x][y].terrain;
                AltitudeInfo altm = game.data.map[x][y].altm;

                int tileId = xter.id;
                String waterModifier;

                int topOffset = altm.altitude * Graphics.LAYER_OFFSET;

                switch (xter.waterLevel) {
                    case DRY:
                        waterModifier = "DRY";
                        break;
                    case SHORE:
                        waterModifier = "SHR";
                        break;
                    case SUBMERGED:
                        waterModifier = "SUB";
                        break;
                    case SURFACE:
                        waterModifier = "SRF";
                        break;
                    case WATERFALL:
                        waterModifier = "WFL";
                        break;
                    default:
                        waterModifier = "???";
                }

                // Water edge slope debuging stuff.
//                gc.setFill(new Color(255, 255, 255, 69));
//                Font font = g.getFont();
//                gc.fillText(String.valueOf(tileId) + ":" + waterModifier, graphics.getDrawX(x, y) - 15, graphics.getDrawY(x, y) - topOffset - 6);
//                gc.fillText(String.format("SLP:%4s", Integer.toBinaryString(xter.slope)).replace(" ", "0"),
//                        graphics.getDrawX(x, y) - 15, graphics.getDrawY(x, y) - topOffset - 16);
//                gc.fillText(String.format("SHR:%8s", Integer.toBinaryString(xter.shore)).replace(" ", "0"),
//                        graphics.getDrawX(x, y) - 15, graphics.getDrawY(x, y) - topOffset - 26);
//                g.setFont(g.getFont().deriveFont((g.getFont().getLotSize()*0.66f)));
//                g.drawString( String.format("%2X", terrain.raw), graphics.getDrawX(x, y) - 15, graphics.getDrawY(x, y) - topOffset+4);
//                g.setFont(font); // Put the orginal Font back.
            }
        }
    }

    public static TerrainTile getTile(Graphics graphics, int tileId) {
        return graphics.terrainTiles[TerrainTile.getTileIdRotated(tileId, graphics.game.data.mapInfo.getRotation())];
    }

    public static void drawDirtEdge(Graphics graphics, GraphicsContext gc) {
        if (!graphics.showTerrain || graphics.debug.hideTerrainEdge) {
            return;
        }
        int size = graphics.game.data.mapInfo.mapSize;
        int tX;
        int tY;
        final int ROCK = TerrainTile.HIGH;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (!(y == size - 1 || x == size - 1)) {
                    continue;
                }

                switch (graphics.game.data.mapInfo.getRotation()) {
                    default:
                    case 0:
                        tX = x;
                        tY = y;
                        break;
                    case 1:
                        tX = x;
                        tY = size - 1 - y;
                        break;
                    case 2:
                        tX = size - 1 - x;
                        tY = size - 1 - y;
                        break;
                    case 3:
                        tX = size - 1 - x;
                        tY = y;
                        break;
                }
                int topOffset;

                Cell cell = graphics.game.data.map[tX][tY];

                // draw rock
                for (int j = 0; j < cell.altm.altitude; j++) {
                    topOffset = Graphics.LAYER_OFFSET * j;
                    TerrainRenderer.drawTile(graphics, gc, ROCK, new Point(tX, tY), topOffset, null);
                }
            }
        }
    }

    public static void drawWaterEdge(Graphics graphics, GraphicsContext gc) {
        if (!graphics.showTerrain || graphics.debug.hideTerrainEdge) {
            return;
        }
        int size = graphics.game.data.mapInfo.mapSize;
        int tX;
        int tY;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (!(y == size - 1 || x == size - 1)) {
                    continue;
                }

                switch (graphics.game.data.mapInfo.getRotation()) {
                    default:
                    case 0:
                        tX = x;
                        tY = y;
                        break;
                    case 1:
                        tX = x;
                        tY = size - 1 - y;
                        break;
                    case 2:
                        tX = size - 1 - x;
                        tY = size - 1 - y;
                        break;
                    case 3:
                        tX = size - 1 - x;
                        tY = y;
                        break;
                }
                int topOffset;

                Cell cell = graphics.game.data.map[tX][tY];

                if ((cell.terrain.waterLevel == TerrainInfo.WTR_LVL.SUBMERGED
                        || cell.terrain.waterLevel == TerrainInfo.WTR_LVL.SHORE)
                        && (!graphics.debug.hideWater && graphics.showWater)) {
                    for (int j = cell.altm.altitude; j <= graphics.game.data.mapInfo.waterLevel; j++) {
                        topOffset = Graphics.LAYER_OFFSET * j;
                        int px = graphics.getDrawX(tX, tY) - Graphics.TILE_WIDTH / 2;
                        int py = graphics.getDrawY(tX, tY) - (int) (0.75 * Graphics.TILE_WIDTH) - topOffset;

                        byte slope;
                        if (j == cell.altm.altitude) {
                            slope = cell.terrain.slope;
                        } else {
                            slope = 0;
                        }

                        if (y == size - 1) {
                            switch (graphics.game.data.mapInfo.getRotation()) {
                                case 0:
                                    switch (slope) {
                                        case 0:
                                            drawPoly(gc, TerrainTile.WATER_POLY_FLAT_LEFT, px, py);
                                            break;
                                        case 0b1001:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_A_LEFT, px, py);  // GOOD
                                            break;
                                        case 0b0110:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_B_LEFT, px, py);     // GOOD                                     
                                            break;
                                        default:
                                            break;
                                    }
                                    break;

                                case 1:
                                    switch (slope) {
                                        case 0:
                                        case 0b001:
                                            drawPoly(gc, TerrainTile.WATER_POLY_FLAT_RIGHT, px, py);
                                            break;
                                        case 0b1001:
                                        case 0b1011:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_B_RIGHT, px, py);  // GOOD
                                            break;
                                        case 0b0110:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_A_RIGHT, px, py);    // GOOD                                 
                                            break;
                                        default:
                                            break;
                                    }
                                    break;

                                case 2:
                                    switch (slope) {
                                        case 0:
                                        case 0b0001:
                                            drawPoly(gc, TerrainTile.WATER_POLY_FLAT_LEFT, px, py);
                                            break;
                                        case 0b1001:
                                        case 0b1011:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_B_LEFT, px, py);  // GOOD
                                            break;
                                        case 0b0110:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_A_LEFT, px, py);    // GOOD                                 
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case 3:
                                    switch (slope) {
                                        case 0:
                                            drawPoly(gc, TerrainTile.WATER_POLY_FLAT_RIGHT, px, py);
                                            break;
                                        case 0b1001:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_A_RIGHT, px, py);  // GOOD
                                            break;
                                        case 0b0110:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_B_RIGHT, px, py);    // GOOD                                 
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                default:
                            }
                        }

                        if (x == size - 1) {
                            switch (graphics.game.data.mapInfo.getRotation()) {
                                case 0:
                                    switch (slope) {
                                        case 0:
                                            drawPoly(gc, TerrainTile.WATER_POLY_FLAT_RIGHT, px, py);
                                            break;
                                        case 0b0011:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_A_RIGHT, px, py);  // GOOD
                                            break;
                                        case 0b1100:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_B_RIGHT, px, py);     // GOOD                                     
                                            break;
                                        default:
                                            break;
                                    }
                                    break;

                                case 1:
                                    switch (slope) {
                                        case 0:
                                            drawPoly(gc, TerrainTile.WATER_POLY_FLAT_LEFT, px, py);
                                            break;
                                        case 0b0011:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_A_LEFT, px, py);  // GOOD
                                            break;
                                        case 0b1100:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_B_LEFT, px, py);    // GOOD                                 
                                            break;
                                        default:
                                            break;
                                    }
                                    break;

                                case 2:
                                    switch (slope) {
                                        case 0:
                                        case 0b0100:
                                        case 0b0001:
                                            drawPoly(gc, TerrainTile.WATER_POLY_FLAT_RIGHT, px, py);
                                            break;
                                        case 0b0011:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_B_RIGHT, px, py);  // GOOD
                                            break;
                                        case 0b1100:
                                        case 0b1110:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_A_RIGHT, px, py);    // GOOD                                 
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case 3:
                                    switch (slope) {
                                        case 0:
                                        case 0b0100:
                                            drawPoly(gc, TerrainTile.WATER_POLY_FLAT_LEFT, px, py);
                                            break;
                                        case 0b0011:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_B_LEFT, px, py);  // GOOD
                                            break;
                                        case 0b1100:
                                        case 0b1110:
                                            drawPoly(gc, TerrainTile.WATER_POLY_SLOPE_A_LEFT, px, py);    // GOOD                                 
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                default:
                            }
                        }
                    }
                }
            }
        }
    }

    private static void drawPoly(GraphicsContext gc, Polygon p, int x, int y) {
        Affine transform = gc.getTransform();
        gc.translate(x, y);

        gc.setFill(TerrainTile.WATER_EDGE_COLOR);
        //gc.fill(p);
        gc.setTransform(transform);
    }

}
