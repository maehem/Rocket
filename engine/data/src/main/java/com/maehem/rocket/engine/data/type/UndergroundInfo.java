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

/**
 *
 * @author maehem
 */
public class UndergroundInfo {

    public boolean subway = false;
    public boolean subwayLeftRight = false;
    public boolean subwayStation = false;

    public boolean pipes = false;
    
    //public Slope slope = null;
    public byte slope = 0;

    public UndergroundInfo() {
    }

    public void load(DataInputStream dis) throws IOException {
        subway = dis.readBoolean();
        subwayStation = dis.readBoolean();
        subwayLeftRight = dis.readBoolean();

        pipes = dis.readBoolean();
        
        //slope = Slope.MAP[dis.readByte()];
        slope = dis.readByte();
        
    }

    public void save(DataOutputStream dos) throws IOException {
        dos.writeBoolean(subway);
        dos.writeBoolean(subwayStation);
        dos.writeBoolean(subwayLeftRight);

        dos.writeBoolean(pipes);
        
        //dos.writeByte(Arrays.binarySearch(Slope.MAP, slope));
        dos.writeByte(slope);        
    }
}
