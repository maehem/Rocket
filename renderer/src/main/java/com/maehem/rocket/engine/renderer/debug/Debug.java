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
package com.maehem.rocket.engine.renderer.debug;

import com.maehem.rocket.engine.renderer.tile.StructureTile;
import com.maehem.rocket.engine.renderer.tile.TerrainTile;
import com.maehem.rocket.engine.data.Cell;
import com.maehem.rocket.engine.data.Point;
import com.maehem.rocket.engine.data.type.AltitudeInfo;
import com.maehem.rocket.engine.data.type.StructureInfo;
import com.maehem.rocket.engine.data.type.TerrainInfo;
import com.maehem.rocket.engine.data.type.Zone;
import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.engine.renderer.Graphics;
import com.maehem.rocket.engine.renderer.StructureRenderer;
import com.maehem.rocket.engine.renderer.TerrainRenderer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author Mark J Koch <rocketcolony-maintainer@maehem.com>
 */
public class Debug {

    public boolean enabled = false;
    public boolean hideTerrain = false; //
    public boolean hideZones = false;   //
    public boolean hideNetworks = false;
    public boolean hideBuildings = false;
    public boolean hideWater = false;    //
    public boolean hideTerrainEdge = false;
    public boolean hideAnimatedTiles = false;

    public boolean showTileCoordinates = true; //
    public boolean showHeightMap = false;   // ???
    //public boolean showClipBounds = false;  // ???  delete me
    public boolean showBuildingCorners = false;
    public boolean showZoneOverlay = false;
    public boolean showNetworkOverlay = false;
    public boolean showTileCount = false;  // ???
    public boolean lowerBuildingOpacity = false; //
    public boolean highlightSelectedCellSurroundings = false;
    public boolean showSelectedTileInfo = true;
    public boolean showOverlayInfo = true;
    public boolean showStatsPanel = false;  // Another pop tab

    //public int clipOffset = 0;   // ???   delete me
    //public int tileCount = 0;    // ??? delete me

    public long beginTime = System.nanoTime();
    public long previousTime = System.nanoTime();
    public int frameTime = 0;
    public int frames = 0;
    public int frameCount = 0;
    public int fps = 0;
    private int msPerFrame = 0;

    private final Graphics gfx;

    public Debug(Graphics gfx) {
        this.gfx = gfx;
    }

    public void main(GraphicsContext g) {
        if (!enabled) {
            return;
        }

        debugOverlay(g);
        showTileInfo(g);
        showFrameStats(g);
    }

    public void begin() {
        beginTime = System.nanoTime();
        //tileCount = 0;
    }

    public long end() {
        long time = System.nanoTime();
        frames++;

        if (time > previousTime + 1000000) {
            msPerFrame = Math.round(time - beginTime)/1000;

            fps = Math.round((frames * 1000000000) / (time - previousTime));
            previousTime = time;

            frameCount += frames;
            frames = 0;
        }

        return time;
    }

    public void showFrameStats(GraphicsContext g) {
        int width = 260;
        int height = 30;

        int x = (int) (g.getCanvas().getWidth() - width);
        int y = (int) (g.getCanvas().getHeight() - height);

        g.fillRect(0, 0, width, height);

        g.setFont(new Font("Verdana", (int)(height*0.5)));
        g.setFill(new Color(255, 255, 255, 126));


        g.fillText(msPerFrame / 1000 + " m/s per frame (" + fps + " FPS)",
                x + 20, y + height/3);

    }

    public void terrain(GraphicsContext gc, Cell cell) {
        if (!enabled) {
            return;
        }

        heightMap(gc, cell);
        cellCoordinates(gc, cell);
    }

    public void building(GraphicsContext g, Cell cell) {
        if (!enabled) {
            return;
        }

        buildingCorners(g, cell);
        networkOverlay(g, cell);
    }

    public void buildingCorners(GraphicsContext g, Cell cell) {
        if (!showBuildingCorners) {
            return;
        }

        if (cell.structure.id == 0) {
            return;
        }

        StructureTile tile = StructureRenderer.getTile(gfx, cell.structure.id);

        if (tile.getLotSize(Graphics.TILE_WIDTH) == 1) {
            return;
        }

        Color line = new Color(0, 0, 255, 128);
        Color fill = new Color(0, 0, 255, 26);
        Color textStyle = new Color(255, 255, 255, 196);

// TODO   Get Corners working
        Point loc = cell.parent.getMapLocation(cell);
    }

    public void networkOverlay(GraphicsContext g, Cell cell) {
        if (!showNetworkOverlay) {
            return;
        }

        StructureTile tile = StructureRenderer.getTile(gfx, cell.structure.id);

        Color line;
        Color fill;
        if (null == tile.name.substring(0, 4)) {
            return;
        } else {
            switch (tile.name.substring(0, 4)) {
                case "road":
                    line = new Color(1, 1, 1, .9);
                    fill = new Color(1, 1, 1, .3);
                    break;
                case "rail":
                    line = new Color(.4, .25, .15, .9);
                    fill = new Color(.4, .25, .15, .25);
                    break;
                case "powe":
                    line = new Color(1, 0, 0, 0.9);
                    fill = new Color(1, 0, 0, 0.25);
                    break;
                default:
                    return;
            }
        }

        //TODO
        g.setFont(new Font("Verdana", 8));
        g.setFill(new Color(255, 255, 255, 64));
        Point loc = cell.parent.getMapLocation(cell);
        //graphics.ui.selectionBox(loc.x, loc.y, line, 2, fill);
    }

    public void zoneOverlay(GraphicsContext g, Cell cell) {
        if (!showZoneOverlay) {
            return;
        }

        if (cell.xzon.type == Zone.TYPE.NONE) {
            return;
        }

        Color color;
    }

    public void drawCrosshairs(GraphicsContext g) {
        if (!enabled) {
            return;
        }
        int xOffset = gfx.xOffset;
        int yOffset = gfx.yOffset;

        // Draw crosshairs
        g.setLineWidth(1.0);
        g.setStroke(Color.RED);
        g.strokeLine(-20, 0, 20, 0);
        g.strokeLine(0, -20, 0, 20);

                // Draw crosshairs
        g.setLineWidth(1.0);
        g.setStroke(Color.BLUE);
        g.strokeLine(xOffset-20, yOffset, xOffset+20, yOffset);
        g.strokeLine(xOffset, yOffset-20, xOffset, yOffset+20);
    }

    public void heightMap(GraphicsContext g, Cell cell) {
        if (!showHeightMap) {
            return;
        }

        if (cell.terrain.id < 0 || cell.terrain.id > TerrainTile.HIGH) {
            return;
        }

        int tileId = cell.terrain.id;
        int topOffset = 0;

        if (tileId == 0) {
            topOffset = 0 - Graphics.LAYER_OFFSET/2;
        } else {
            topOffset = 0 - (Graphics.LAYER_OFFSET / 3);
        }

//        game.gfx.drawTile(g, tileId, cell, topOffset, true);
    }

    public void cellCoordinates(GraphicsContext g, Cell cell) {
        if (!this.showTileCoordinates) {
            return;
        }

        Point p = cell.getMapLocation();

        g.setFont(new Font("Verdana", 10));
        g.setFill(new Color(1, 1, 1, 0.5));
        g.fillText(p.x + ", " + p.y,
                gfx.getDrawX(p.x, p.y) - 16, gfx.getDrawY(p.x, p.y) + 3 - Graphics.LAYER_OFFSET*cell.altm.altitude
        );

    }

    public void drawDebugLayer(GraphicsContext g, Cell cell) {
        if (!showTileCount) {
            return;
        }

    }

    public void debugOverlay(GraphicsContext g) {
        if (!showOverlayInfo) {
            return;
        }

        float fontSize = 20.0f;
        int width = 320;
        int height = (int) (fontSize*6);
        int lineInc = (int) fontSize;

        int x = 20;
        int y = (int) gfx.canvas.getHeight();

        g.setFill(new Color( 0,0,0,60));
        g.fillRect(x-5, y-height, width, y-5);

        g.setLineWidth(1.0);
        g.setStroke(Color.DARKGREY);
        g.strokeRect(x-5, y-height, width, y-5);

        y -= lineInc;
        Font origFont = g.getFont();
        g.setFont(new Font(g.getFont().getFamily(), fontSize));
        g.setFill(new Color(255, 255, 255, 180));


        g.fillText("cursor x: " + gfx.ui.cursorX + ", y: " + gfx.ui.cursorY, x, y);
        y -= lineInc;
        g.fillText("map rotation: " + gfx.game.data.mapInfo.getRotation(), x, y);
        y -= lineInc;
        g.fillText("zoom: " + Math.round(gfx.ui.zoom*10)/10.0f, x, y);
        y -= lineInc;
        if (gfx.ui.selectedCell != null) {
            g.fillText("selected tile"
                    +  " x: " + gfx.ui.selectedCell.getMapLocation().x
                    + ", y: " + gfx.ui.selectedCell.getMapLocation().y,
                    x, y);
            y -= lineInc;
        }
        if (gfx.ui.isCursorOnMap()) {
            Cell cell = gfx.game.data.getMapCell(gfx.ui.cursorTileX, gfx.ui.cursorTileY);
            g.fillText("tile x: " + cell.getMapLocation().x + ", y: " + cell.getMapLocation().y + ", z: " + cell.altm.altitude, x, y);
            y -= lineInc;
        }

        g.setFont(origFont); // Put the font settings back.
    }

    public void showTileInfo(GraphicsContext g) {
        if (!showSelectedTileInfo) {
            return;
        }

        Cell cell = gfx.game.data.getMapCell(gfx.ui.cursorTileX, gfx.ui.cursorTileY);

        // TODO   No cells are ever null!
        if (cell == null) {
            return;
        }

        StringBuilder textData = new StringBuilder();

        // todo: this should be moved to a function for drawing text?
        textData.append("Cell Position:");
        textData.append("Current X: ").append(cell.getMapLocation().x)
                .append(", Y: ").append(cell.getMapLocation().y)
                .append(", Z: ").append(cell.altm.altitude);

        // TerrainRenderer has complex structure/behaviour
        if (cell.terrain.id >= 0) {
            TerrainTile tile = TerrainRenderer.getTile(gfx, cell.terrain.id);

            textData.append("Terrain: " + " (R" + gfx.game.data.mapInfo.getRotation() + ": " + tile.id + ")");
            textData.append("  Slopes: " + cell.terrain.slope);
            textData.append("  Frames: " + tile.image.length);
            textData.append("  Water Level: " + cell.terrain.waterLevel);
            textData.append("");
        }

        if (cell.structure.id > 0) {
            StructureTile tile = StructureRenderer.getTile(gfx, cell.structure.id);

            textData.append("Building: " + cell.structure.id + " (R" + gfx.game.data.mapInfo.getRotation() + ": " + tile.id + ")");
            textData.append("  Name: " + tile.description);
            textData.append("  Lot Size: ").append(tile.getLotSize(Graphics.TILE_WIDTH)).append("x").append(tile.getLotSize(Graphics.TILE_WIDTH));
            textData.append("  Frames: " + tile.image.length);
            textData.append("  Transforms:");
            textData.append("    Tile Flip: " + tile.flip_h);
            textData.append("    Cell Rotate: " + cell.xbit.rotate);
            textData.append("");
        }

        int height = 20 + (textData.length() * 15);
        int width = 220;

        g.fillRect(0, 0, width, height);
        int lineX = gfx.ui.cursorX + 20;
        int lineY = gfx.ui.cursorY + 25;

        g.setFont(new Font("Verdana", 10));
        g.setFill(new Color(255, 255, 255, 230));

        g.fillText(textData.toString(), lineX, lineY);
    }

    private void drawStructureIDs(GraphicsContext gc) {
        Game game = gfx.game;
        for (int y = 0; y < game.data.getMapSize(); y++) {
            for (int x = 0; x < game.data.getMapSize(); x++) {

                StructureInfo xbld = game.data.map[x][y].structure;
                TerrainInfo xter = game.data.map[x][y].terrain;
                AltitudeInfo altm = game.data.map[x][y].altm;

                int tileId = xbld.id;
                int altitude = altm.altitude;

                if (xter.id == 13) {
                    altitude++;
                }

                int rotatedTid = StructureTile.getTileIdRotated(tileId, game.data.mapInfo.getRotation(), game.data.mapInfo.getRotation());
                int topOffset = altitude * Graphics.LAYER_OFFSET;

                gc.setFill(new Color(255, 255, 255, 69));
                Font font = gc.getFont();
                gc.setFont(new Font(6.0));
                gc.fillText(String.valueOf(tileId),
                        gfx.getDrawX(x, y) - 15,
                        gfx.getDrawY(x, y) - topOffset);
                gc.fillText("[" + String.valueOf(rotatedTid) + "]",
                        gfx.getDrawX(x, y) - 15,
                        gfx.getDrawY(x, y) - topOffset + 10);
                gc.setFont(font); // Put the orginal Font back.
            }
        }
    }

}
