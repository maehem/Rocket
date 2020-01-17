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
import com.maehem.rocket.engine.data.type.TerrainInfo;
import javafx.scene.canvas.GraphicsContext;

/**
 * Roads, power lines
 *
 * @author Mark J Koch <rocketcolony-maintainer@maehem.com>
 */
public class NetworkRenderer {
    public static void drawTile(Graphics graphics, GraphicsContext g, Cell cell, Point cellXY) {

        int bid = cell.structure.id;

        // Exclude non-network tiles.
        if (bid < 200 || bid > 299) {
            return;
        }

//        // Backside mountain tunnels get drawn with terrain
//        // to look correct.
//        if (bid == 65 || bid == 66) {
//            return;
//        }

        int altitude = cell.altm.altitude;

        if ((cell.terrain.waterLevel == TerrainInfo.WTR_LVL.SUBMERGED
                || cell.terrain.waterLevel == TerrainInfo.WTR_LVL.SHORE)
                && cell.altm.altitude < graphics.game.data.mapInfo.waterLevel) {
            altitude = graphics.game.data.mapInfo.waterLevel - cell.altm.altitude;

//            // Freeways can be over water, and if they are their
//            // altitude needs to be the water level.
//            if (bid >= 73 && bid <= 74) {
//                altitude = game.data.mapInfo.waterLevel;
//            }
        }

        // Is it a block of rock? Then it sticks out of the ground
        // and we draw the structure on top of that.
        if (cell.terrain.id == 13) {
            altitude++;
        }

        // NetworkRenderer items on top of water/liquid.
//        if ((bid >= 81 && bid <= 92)
//                || (bid >= 106 && bid <= 107)) {
//            // It's a bridge or water road.
//            // Find nearest road.
//            // What direction?
//            //MapTile tile = graphics.getTile(bid);
//            // X axis
//            //Cell[] shores = cell.getShoreCells( tile.isFlipped(data.mapInfo.rotation) );
//
//            //Find the shore in both directions.
//            // The altitudes better match.
//            // Bridges are always over water.
//            altitude = game.data.mapInfo.waterLevel;
//        }

        // NetworkRenderer tiles are kinds of structures.
        StructureTile tile = StructureRenderer.getTile(graphics, cell.structure.id);

        boolean keyTile = false;
        if (cell.xzon.corners == graphics.game.corners[graphics.game.data.mapInfo.getRotation()][graphics.game.data.mapInfo.getRotation()]) {
            keyTile = true;
        }
        if (tile.getLotSize(Graphics.TILE_WIDTH) == 1) {
            keyTile = true;
        }

        if (keyTile && !graphics.debug.hideNetworks) {
            StructureRenderer.drawTile(graphics, g, cell.structure.id, cellXY, altitude * Graphics.LAYER_OFFSET);
        }

        graphics.debug.networkOverlay(g, cell);

    }
    
}
