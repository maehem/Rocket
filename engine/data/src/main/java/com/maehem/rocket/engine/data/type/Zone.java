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
public class Zone {

    public enum TYPE {
        NONE,
        HABITAT_LIGHT, HABITAT_MEDIUM, HABITAT_DENSE,
        AGRICULTURE_LIGHT, AGRICULTURE_MEDIUM, AGRICULTURE_DENSE,
        FABRICATION_LIGHT, FABRICATION_MEDIUM, FABRICATION_DENSE
    }

    public int corners;
//    public String zoneType;
    //public int type = -1;
    public TYPE type = TYPE.NONE;
    
    //public int highlight = 0;   // Used for zone tool boundary preview. Not for load/save.
    public TYPE highlight = TYPE.NONE;   // Used for zone tool boundary preview. Not for load/save.

    public Zone() {
    }

    public void load(DataInputStream dis) throws IOException {
        corners = dis.readByte();
        type = TYPE.values()[dis.readByte()]; // Turn int into TYPE from oridnals
    }

    public void save(DataOutputStream dos) throws IOException {
        dos.writeByte(corners);        
        dos.writeByte(type.ordinal());
    }
}
