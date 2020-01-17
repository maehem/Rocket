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
import com.maehem.rocket.engine.data.Cell;
import com.maehem.rocket.engine.data.Point;
import com.maehem.rocket.engine.data.type.AltitudeInfo;
import com.maehem.rocket.engine.data.type.StructureInfo;
import com.maehem.rocket.engine.data.type.TerrainInfo;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * @author Mark J Koch <rocketcolony-maintainer@maehem.com>
 */
public class StructureRenderer {

    public static StructureTile getTile(Graphics graphics, int tileId) {
        return graphics.tiles[StructureTile.getTileIdRotated(tileId, graphics.game.data.mapInfo.getRotation(), graphics.game.data.mapInfo.getRotation())];
    }

    /**
     * Draws a tile from the tile map to this graphics context.
     *
     * @param gfx Game Graphics
     * @param gc JavaFX graphics context
     * @param tileId Tile ID.
     * @param cellXY Cell XY grid location
     * @param topOffset Altitude 'Z' shift.
     */
    public static void drawTile(Graphics gfx, GraphicsContext gc, int tileId, Point cellXY, int topOffset) {
        StructureTile tile = getTile(gfx, tileId);

        Image img = tile.image[gfx.getFrame(tile)];

        int offsetX = -Graphics.TILE_WIDTH / 2;
        int x = gfx.getDrawX(cellXY.x, cellXY.y);
        int y = (int) (gfx.getDrawY(cellXY.x, cellXY.y) - (img.getHeight() - (Graphics.TILE_WIDTH / 4)) - topOffset);

        if (tile.isFlipped(gfx.game.data.mapInfo.getRotation())) {
            // Draws a scaled image with a negative width, mirroring the image.
            gc.drawImage(img, x - offsetX, y, -img.getWidth(), img.getHeight());
        } else {
            gc.drawImage(img, x + offsetX, y);
        }
    }

    /**
     *
     * @param gfx  Game graphics
     * @param gc   JavaFX graphics context
     * @param cellXY Cell grid location
     * @param cell Map cell
     */
    public static void drawTile(Graphics gfx, GraphicsContext gc, Cell cell, Point cellXY) {

        StructureInfo bldg = cell.structure;
        TerrainInfo terrain = cell.terrain;
        AltitudeInfo alt = cell.altm;

//                // Don't draw transportation or electrical tiles.
//                // Draw those on the 'network' layer.
//                if (/* structure.id == 0 || */ (structure.id > 13 && structure.id < 112)) {
//                    return;
//                }
        // Don't draw terrain garbage if there's water or a slope.
        if (bldg.id < 100 && (cell.terrain.isWet() || terrain.slope > 0) ) {
            return;
        }

        StructureTile tile = StructureRenderer.getTile(gfx, bldg.id);

        int altitude = alt.altitude;
        int buildingID = bldg.id;

//        // Boat docks
//        if (structure.id == 223) {
//            altitude = game.data.mapInfo.waterLevel;
//        }

        boolean keyTile
                = tile.getLotSize(Graphics.TILE_WIDTH) == 1
                || cell.xzon.corners == gfx.game.corners[gfx.game.data.mapInfo.getRotation()][gfx.game.data.mapInfo.getRotation()];

//                if (!keyTile && tile.getLotSize() == 1) {
//                    keyTile = true;
//                    LOGGER.warning("Got here.");
//                }
        int topOffset = altitude * Graphics.LAYER_OFFSET;

        if (gfx.debug.lowerBuildingOpacity) {
            gc.setGlobalAlpha(0.4f);
        }

        if (keyTile && !gfx.debug.hideBuildings) {
            
            // TODO: Develop Layered tile groups.  Mostly for ground effect
            // build up like piles of rocks or debris.
            
            // Junk pile layers the four junk tiles.
//            if (structure.id >= 2 && structure.id <= 4) {
//                for (int i = 1; i <= structure.id; i++) {
//                    graphics.drawTile(g, i, p.x, p.y, topOffset);
//                }
//            } else {
            // Not a layered tile.
            StructureRenderer.drawTile(gfx, gc, buildingID, cellXY, /*.x, p.y,*/ topOffset);
//            }

        }

        // Put the alpha back if changed.
        if (gfx.debug.lowerBuildingOpacity) {
            gc.setGlobalAlpha(1.0);
        }

        gfx.debug.building(gc, cell);
    }

}
