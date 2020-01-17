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

import com.maehem.rocket.engine.data.type.AltitudeInfo;
import com.maehem.rocket.engine.data.type.State;
import com.maehem.rocket.engine.data.type.StructureInfo;
import com.maehem.rocket.engine.data.type.TerrainInfo;
import com.maehem.rocket.engine.data.type.Notation;
import com.maehem.rocket.engine.data.type.UndergroundInfo;
import com.maehem.rocket.engine.data.type.Zone;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author maehem
 */
public class Cell {

    public Data parent;

    public TerrainInfo terrain = new TerrainInfo();
    public AltitudeInfo altm = new AltitudeInfo();
    public State xbit = new State();
    public Zone xzon = new Zone();
    public UndergroundInfo xund = new UndergroundInfo();
    public StructureInfo structure = new StructureInfo();
    public Notation xtxt = new Notation();

    public Cell(Data parent) {
        this.parent = parent;
    }

    public Point getMapLocation() {
        for (int y = 0; y < parent.mapInfo.mapSize; y++) {
            for (int x = 0; x < parent.mapInfo.mapSize; x++) {
                if (this.equals(parent.map[x][y])) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    /**
     *
     * @param direction 0 = y-- : 1 = x++ : 2 = y++ : 3 = x--
     *
     * @return @Cell if road goes that way, else null
     */
    public Cell getConnectingRoad(int direction) {
        int xDir = 0;
        int yDir = 0;
        switch (direction) {
            case 0:
                yDir = -1;
                break;
            case 1:
                xDir = 1;
                break;
            case 2:
                yDir = 1;
                break;
            case 3:
                xDir = -1;
                break;
        }
        Point mapLocation = parent.getMapLocation(this);

        try {
            if (parent.map[mapLocation.x + xDir][mapLocation.y + yDir].hasRoad()) {
                return parent.map[mapLocation.x + xDir][mapLocation.y + yDir];
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            // Off the map so nothing here.
            return null;
        }

        return null;
    }

    public boolean hasRoad() {
        return structure.hasRoad();
    }

    public void balanceCellWaterDepth(int waterLevel) {
        if (altm.altitude < waterLevel) {
            terrain.waterLevel = TerrainInfo.WTR_LVL.SUBMERGED;
        } else if (altm.altitude == waterLevel) {
            terrain.waterLevel = TerrainInfo.WTR_LVL.SHORE;
        } else if (terrain.surfaceWater) {

            // We have water but are above sea level.
            if (terrain.slope > 0) {
                if (TerrainInfo.slopeIndex(terrain.slope) < 5) {
                    terrain.waterLevel = TerrainInfo.WTR_LVL.WATERFALL;
                } else {
                    terrain.waterLevel = TerrainInfo.WTR_LVL.DRY;
                    terrain.surfaceWater = false;
                }

            } else {
                terrain.waterLevel = TerrainInfo.WTR_LVL.SURFACE;
            }
        }
    }

    public void load(DataInputStream dis) throws IOException {
        terrain.load(dis);
        altm.load(dis);
        xbit.load(dis);
        xzon.load(dis);
        xund.load(dis);
        structure.load(dis);
        xtxt.load(dis);
    }

    public void save(DataOutputStream dos) throws IOException {
        terrain.save(dos);
        altm.save(dos);
        xbit.save(dos);
        xzon.save(dos);
        xund.save(dos);
        structure.save(dos);
        xtxt.save(dos);
    }

}
