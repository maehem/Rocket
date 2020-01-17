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
package com.maehem.rocket.engine.game;

import com.maehem.rocket.engine.data.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * @author maehem
 */
public class GameStateFile {

    private static final Logger LOGGER = Logger.getLogger(GameStateFile.class.getName());

    public static final String MAGIC_COOKIE = "ROCKETCOLONY";
    
//    static {
//        LOGGER.setLevel(Level.FINE);
//    }

    private GameStateFile() {
    }

    public static void load(DataInputStream dis, Data data) throws IOException {
        if (!isOurSaveFileType(dis)) {
            LOGGER.severe("File is not a valid RocketColony Save File\n");
            return;
        }

        try (dis) {
            // TODO: Read file version
            data.load(dis);
        } catch ( EOFException ex ) {
            LOGGER.severe("LOAD FAILED! Game save file seems to be truncated.");
        }
    }

    public static void save(FileOutputStream fos, Data data) throws IOException {
            DataOutputStream dos = new DataOutputStream(fos);
            // Write File Magic Number
            writeMagicCookie(dos);
            // TODO: Write File Version
            
            data.save(dos);
    }

    public static final boolean isOurSaveFileType(DataInputStream dis) {
        try {
            if ( dis.available() < MAGIC_COOKIE.length() ) return false;
            
            if (    dis.readByte() != 'R'
                    || // 'R'
                    dis.readByte() != 'O'
                    || // 'O'
                    dis.readByte() != 'C'
                    || // 'C'
                    dis.readByte() != 'K'
                    || // 'K'
                    dis.readByte() != 'E'
                    || // 'E'
                    dis.readByte() != 'T'
                    || // 'T'
                    dis.readByte() != 'C'
                    || // 'C'
                    dis.readByte() != 'O'
                    || // 'O'
                    dis.readByte() != 'L'
                    || // 'L'
                    dis.readByte() != 'O'
                    || // 'O'
                    dis.readByte() != 'N'
                    || // 'N'
                    dis.readByte() != 'Y') { // 'Y'
                LOGGER.severe("Magic Number does not match!\n");
                return false;
            }
            LOGGER.finer("Magic Cookie Read OK.");
            return true;
        } catch (IOException ex) {
            LOGGER.warning(ex.toString());
            return false;
        }
    }

    private static void writeMagicCookie(DataOutputStream dos) throws IOException {
        dos.writeBytes(MAGIC_COOKIE);
    }
}
