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
package com.maehem.rocket.engine.data.type;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * @author maehem
 */
public class TerrainInfo {
    private static final Logger LOGGER = Logger.getLogger(TerrainInfo.class.getName());

    public int id = -1;

    public byte slope = 0;

    public int shore = 0;  // 0-16  Bits 0-3 represent shore for t,r,b,l (3:0)
    public boolean surfaceWater;  // Flooded???
    public int waterDepth = 0;
    public WTR_LVL waterLevel;
    public int waterLevelHex; // TODO:  get rid of this, waterLevel is the same info.

    public enum WTR_LVL {
        DRY, SUBMERGED, SHORE, SURFACE, SURFACE2, WATERFALL
    };

    public static final byte[] SLOPE_MAP = {
        //TRLB
        0b0000,
        0b1001,
        0b1100,
        0b0110,
        0b0011,
        0b1101,
        0b1110,
        0b0111,
        0b1011,
        0b1000,
        0b0100,
        0b0010,
        0b0001,
        0b1111
    };

    public TerrainInfo() {
        id = 0;
        waterLevel = WTR_LVL.DRY;
    }

    public void updateID() {
        id = slopeIndex(slope);
    }

    /**
     * Shore value with consideration for rotation.
     *
     * @param rotation
     * @return rotated shore value
     */
    public int getShore(int rotation) {
        switch (rotation) {
            default:
            case 0:
                return shore;
            case 1:
                return (shore >> 1 | (shore << 3)) & 0xF;
            case 2:
                return (shore >> 2 | (shore << 2)) & 0xF;
            case 3:
                return (shore >> 3 | (shore << 1)) & 0xF;
        }
    }

    public boolean isWet() {
        return surfaceWater || waterLevel == WTR_LVL.SUBMERGED || waterLevel == WTR_LVL.SHORE;
    }
    
    public void load(DataInputStream dis) throws IOException {
        id = dis.readInt();
        //slope = Slope.MAP[dis.readByte()];
        slope = dis.readByte();
        surfaceWater = dis.readBoolean();
        //dis.readUTF();
        waterLevel = WTR_LVL.values()[dis.readByte()];
        //LOGGER.log(Level.INFO, "Water Level Ordinal: {0}", waterLevel.ordinal());
        //dis.readUTF();
    }

    public void save(DataOutputStream dos) throws IOException {
        dos.writeInt(id);
        //dos.writeByte(Arrays.binarySearch(Slope.MAP, slope));
        dos.writeByte(slope);
        dos.writeBoolean(surfaceWater);
        //dos.writeInt(Arrays.binarySearch(WTR_LVL.values(), waterLevel));
        //dos.writeUTF("WTRLVLA");
        dos.writeByte(waterLevel.ordinal());
        //dos.writeUTF("WTRLVLB");
    }

    public static int slopeIndex( byte key) {
        int returnvalue = -1;
        for (int i = 0; i < SLOPE_MAP.length; ++i) {
            if (key == SLOPE_MAP[i]) {
                returnvalue = i;
                break;
            }
        }
        return returnvalue;
    }
}
