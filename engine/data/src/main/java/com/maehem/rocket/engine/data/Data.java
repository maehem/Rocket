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
package com.maehem.rocket.engine.data;

import com.maehem.rocket.engine.data.type.GameInfo;
import com.maehem.rocket.engine.data.sample.MapData;
import com.maehem.rocket.engine.data.sample.Map_0003;
import com.maehem.rocket.engine.data.type.Zone;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * @author maehem
 */
public class Data {

    private static final Logger LOGGER = Logger.getLogger(Data.class.getName());
    private final static int MAX_MAP_SIZE = 128; // @depricated.  Delete me.

    private boolean loaded = false;

    public GameInfo mapInfo;
    public Cell[][] map;

    /**
     * Create a new Data object and initialize it to a default state. Use for a
     * new game.
     */
    public Data() {
        mapInfo = new GameInfo();
        //initMap();
        
        
        //loaded = false;
    }

    public Data(int i) {
        mapInfo = new GameInfo();
        MapData mapData = new Map_0003();
        mapInfo.mapSize = mapData.size;
        this.map = new Cell[mapInfo.mapSize][mapInfo.mapSize];
        for (int y = 0; y < mapInfo.mapSize; y++) {
            for (int x = 0; x < mapInfo.mapSize; x++) {
                map[x][y] = new Cell(this);
                map[x][y].altm.altitude = mapData.altitude[y][x];
                map[x][y].structure.id = mapData.structure[y][x];
                map[x][y].terrain.surfaceWater = mapData.water[y][x] > 0;
            }
        }

        resolveSlopes();

//        for (int y = 0; y < mapInfo.mapSize; y++) {
//            for (int x = 0; x < mapInfo.mapSize; x++) {
//                Cell c = map[x][y];
//                LOGGER.log(Level.WARNING, "GameInfo {0},{1}: {2}  ::  {3}\n", 
//                        new Object[]{x, y, c.terrain.slope.toString(), String.format("%4s", Integer.toBinaryString(c.terrain.slope2)).replace(' ', '0' )});
//            }
//        }
        resolveWater();

        loaded = true;
    }

//    /**
//     * Create a new Data object and load data from a GameStateFile.
//     * Use when loading a previously saved file.
//     * 
//     * @param parent 
//     */
//    public Data(Component parent) {
//        GameStateFile.load(parent, this);
//        //initMap();
//    }
    
    
    public void initMap() {
        this.map = new Cell[mapInfo.mapSize][mapInfo.mapSize];
        for (int y = 0; y < mapInfo.mapSize; y++) {
            for (int x = 0; x < mapInfo.mapSize; x++) {
                map[x][y] = new Cell(this);
                map[x][y].altm.altitude = mapInfo.mapSize / 2;
            }
        }

        // Test code.  Put a small hill near the middle of our map.
        map[mapInfo.mapSize / 2][mapInfo.mapSize / 2].altm.altitude = mapInfo.mapSize / 2 + 1;
        map[mapInfo.mapSize / 2][mapInfo.mapSize / 2 + 1].altm.altitude = mapInfo.mapSize / 2 + 1;
        map[mapInfo.mapSize / 2 + 1][mapInfo.mapSize / 2 + 1].altm.altitude = mapInfo.mapSize / 2 + 1;

        resolveSlopes();
        
        // For testing
    //    map[4][4].structure.id = 200;
    }

    private void resolveSlopes() {
        // For each tile, if the adjacent tile altitude is one less,
        //  then set the co-incendent slope corners to true.
        for (int y = 0; y < mapInfo.mapSize; y++) {
            for (int x = 0; x < mapInfo.mapSize; x++) {
                Cell c = map[x][y];

                Cell b = getMapCell(x, y + 1);
                Cell br = getMapCell(x + 1, y + 1);
                Cell r = getMapCell(x + 1, y);
                Cell tr = getMapCell(x + 1, y - 1);
                Cell t = getMapCell(x, y - 1);
                Cell tl = getMapCell(x - 1, y - 1);
                Cell l = getMapCell(x - 1, y);
                Cell bl = getMapCell(x - 1, y + 1);

                int alt = c.altm.altitude;

                if (b != null && alt - 1 == b.altm.altitude) {
                    b.terrain.slope |= 0b1100;

                    b.terrain.updateID();
                }
                if (br != null && alt - 1 == br.altm.altitude) {
                    br.terrain.slope |= 0b1000;
                    br.terrain.updateID();
                }
                if (r != null && alt - 1 == r.altm.altitude) {
                    r.terrain.slope |= 0b1001;
                    r.terrain.updateID();
                }
                if (tr != null && alt - 1 == tr.altm.altitude) {
                    tr.terrain.slope |= 0b0001;
                    tr.terrain.updateID();
                }
                if (t != null && alt - 1 == t.altm.altitude) {
                    t.terrain.slope |= 0b0011;
                    t.terrain.updateID();
                }
                if (tl != null && alt - 1 == tl.altm.altitude) {
                    tl.terrain.slope |= 0b0010;
                    tl.terrain.updateID();
                }
                if (l != null && alt - 1 == l.altm.altitude) {
                    l.terrain.slope |= 0b0110;
                    l.terrain.updateID();
                }
                if (bl != null && alt - 1 == bl.altm.altitude) {
                    bl.terrain.slope |= 0b0100;
                    bl.terrain.updateID();
                }
            }
        }
    }

    private void resolveWater() {
        for (int y = 0; y < mapInfo.mapSize; y++) {
            for (int x = 0; x < mapInfo.mapSize; x++) {
                map[x][y].balanceCellWaterDepth(mapInfo.waterLevel);
            }
        }
        for (int y = 0; y < mapInfo.mapSize; y++) {
            for (int x = 0; x < mapInfo.mapSize; x++) {
                Cell c = map[x][y];

                Cell b = getMapCell(x, y + 1);
                Cell br = getMapCell(x + 1, y + 1);
                Cell r = getMapCell(x + 1, y);
                Cell tr = getMapCell(x + 1, y - 1);
                Cell t = getMapCell(x, y - 1);
                Cell tl = getMapCell(x - 1, y - 1);
                Cell l = getMapCell(x - 1, y);
                Cell bl = getMapCell(x - 1, y + 1);

                updateShore(c, t, 3);
                updateShore(c, r, 2);
                updateShore(c, b, 1);
                updateShore(c, l, 0);

                updateShoreCorner(c, tl, l, t, 3);
                updateShoreCorner(c, tr, t, r, 2);
                updateShoreCorner(c, br, b, r, 1);
                updateShoreCorner(c, bl, b, l, 0);
            }
        }
    }

    private void updateShore(Cell c, Cell peer, int shoreEdge) {
        if (peer != null) {
            if (peer.terrain.isWet()) {
                c.terrain.shore &= (~(1 << shoreEdge) & 0xff);
            } else {
                c.terrain.shore |= 1 << shoreEdge;
            }
        }
    }

    private void updateShoreCorner(Cell c, Cell cornerPeer, Cell ccwPeer, Cell cwPeer, int shoreEdge) {
        if (cornerPeer != null) {
            if (cornerPeer.terrain.isWet() && ccwPeer.terrain.isWet() && cwPeer.terrain.isWet()) {
                c.terrain.shore &= ~(1 << (4 + shoreEdge) & 0xff);
            } else {
                c.terrain.shore |= 1 << (4 + shoreEdge);
            }
        }
    }

    public int getMapSize() {
        return mapInfo.mapSize;
    }

    public Point getMapLocation(Cell cell) {
        return cell.getMapLocation();
    }

    public Point getViewXY(int x, int y) {
        switch (mapInfo.getRotation()) {
            default:
            case 0:
                return new Point(x, y);
            case 1:
                return new Point(x, mapInfo.mapSize - 1 - y);
            case 2:
                return new Point(mapInfo.mapSize - 1 - x, mapInfo.mapSize - 1 - y);
            case 3:
                return new Point(mapInfo.mapSize - 1 - x, y);
        }
    }

    public Cell getMapCell(int xT, int yT) {
        try {
            return map[xT][yT];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     *           [NW, N, NE]
     *           [W,  C,  W]
     *           [SW, S, SE]
     * 
     * @param tX
     * @param tY
     * @return 
     */
    public Point[] getSurroundingCells(int tX, int tY) {

        Point[] surroundingCells = new Point[9];
        int ix = 0;
        //
        for (int cX = -1; cX <= 1; cX++) {
            for (int cY = -1; cY <= 1; cY++) {
                surroundingCells[ix] = new Point(tX + cX, tY + cY);
                ix++;
            }
        }

        // TODO: Move this to the render sequence.
        //// debug highlight surrounding cells
        for (Point surroundingCell : surroundingCells) {
//            ui.selectionBox(surroundingCell.x, surroundingCell.y, Color.red, 3);
        }

        return surroundingCells;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean state) {
        loaded = state;
    }

    public void rotateCCW() {
        mapInfo.setRotation(mapInfo.getRotation() - 1);
        if (mapInfo.getRotation() < 0) {
            mapInfo.setRotation(3);
        }
    }

    public void rotateCW() {
        mapInfo.setRotation(mapInfo.getRotation() + 1);
        if (mapInfo.getRotation() > 3) {
            mapInfo.setRotation(0);
        }
    }

    public void load(DataInputStream dis) throws IOException {

        // Read GameInfo block
        mapInfo = new GameInfo(dis);

        // Read labels
        // Read each cell
        this.map = new Cell[mapInfo.mapSize][mapInfo.mapSize];
        for (int y = 0; y < mapInfo.mapSize; y++) {
            for (int x = 0; x < mapInfo.mapSize; x++) {
                map[x][y] = new Cell(this);
                map[x][y].load(dis);
            }
        }
        LOGGER.finer("Data Stream Load completed OK.");
        setLoaded(true);
    }

    public void save(DataOutputStream dos) throws IOException {
        // Write GameInfo
        mapInfo.save(dos);
        // Write labels

        // Write Cells           
        for (int y = 0; y < mapInfo.mapSize; y++) {
            for (int x = 0; x < mapInfo.mapSize; x++) {
                map[x][y].save(dos);
            }
        }
    }

    public void useHighlightedZones() {
        for (int x = 0; x < getMapSize(); x++) {
            for (int y = 0; y < getMapSize(); y++) {
                Cell c = map[x][y];

                if (c.xzon.highlight != Zone.TYPE.NONE) {
                    c.xzon.type = c.xzon.highlight;
                    c.xzon.highlight = Zone.TYPE.NONE;
                }
            }
        }
    }

    public void clearHighlight() {
        for (int x = 0; x < getMapSize(); x++) {
            for (int y = 0; y < getMapSize(); y++) {
                map[x][y].xzon.highlight = Zone.TYPE.NONE;
            }
        }
    }

    public int countHighlightedZones() {
        int nCells = 0;
        for (int x = 0; x < getMapSize(); x++) {
            for (int y = 0; y < getMapSize(); y++) {
                Cell c = map[x][y];

                if (c.xzon.highlight != Zone.TYPE.NONE) {
                    nCells++;
                }
            }
        }

        return nCells;
    }
    
    public Point getHighlightCenter() {
        int minX = getMapSize()-1;
        int maxX = 0;
        int minY = getMapSize()-1;
        int maxY = 0;
        
        if ( minX < 0) minX = 0;
        if ( minY < 0) minY = 0;
        
         for (int x = 0; x < getMapSize(); x++) {
            for (int y = 0; y < getMapSize(); y++) {
                Cell c = map[x][y];

                if (c.xzon.highlight != Zone.TYPE.NONE) {
                    if ( x < minX ) minX = x;
                    if ( y < minY ) minY = y;
                    
                    if ( x > maxX ) maxX = x;
                    if ( y > maxY ) maxY = y;
                } 
            }
        }
        
        //LOGGER.log(Level.WARNING, "min: {0},{1}   max:{2},{3}  cmp:{4},{5}", new Object[]{minX, minY, maxX, maxY, minX+(maxX-minX)/2, minY+(maxY-minY)/2});
        return new Point(minX + (maxX-minX)/2, minY + (maxY-minY)/2);
    }
}
