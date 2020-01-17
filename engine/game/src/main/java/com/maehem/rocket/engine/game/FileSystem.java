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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO: Tested on: OSX -- YES Windows -- NO Linux -- NO
 *
 * @author maehem
 */
public class FileSystem {

    private static final Logger LOGGER = Logger.getLogger(FileSystem.class.getName());

    private static final String GAME_DIR = System.getProperty("user.home")
            + File.separator + "Documents"
            + File.separator + "RocketColony";
    private static final String SNAPSHOT_DIR = GAME_DIR + File.separator + "Snapshots";
    private static final String SAVE_DIR = GAME_DIR + File.separator + "Saves";

    public FileSystem() {
        LOGGER.log(Level.CONFIG, "Game directory is: {0}", GAME_DIR);
    }

    public File getGameDir() {
        return new File(GAME_DIR);
    }

    public File getAlbumDir() {
        return new File(SNAPSHOT_DIR);
    }

    public File getSaveDir() {
        return new File(SAVE_DIR);
    }

    public void initGameFilesDirectories() {
        File rocketFiles = getGameDir();
        if (!rocketFiles.exists()) {
            try {
                if (rocketFiles.mkdirs()) {
                    LOGGER.log(Level.CONFIG, "Created game files directory at: {0}", rocketFiles.getAbsolutePath());
                } else {
                    LOGGER.log(Level.SEVERE, "Unable to create game files directory at: {0}", rocketFiles.getAbsolutePath());
                }
            } catch (SecurityException e) {
                LOGGER.severe("Could not create game files directory due to Java security settings exception.");
                throw e;
            }
        } else {
            LOGGER.log(Level.CONFIG, "Found existing game files directory at: {0}", rocketFiles.getAbsolutePath());
        }

        File saveFiles = getSaveDir();
        if (!saveFiles.exists()) {
            try {
                if (saveFiles.mkdirs()) {
                    LOGGER.log(Level.CONFIG, "Created game save directory at: {0}", saveFiles.getAbsolutePath());
                } else {
                    LOGGER.log(Level.SEVERE, "Unable to create game save directory at: {0}", saveFiles.getAbsolutePath());
                }
            } catch (SecurityException e) {
                LOGGER.severe("Could not create game save directory due to Java security settings exception.");
                throw e;
            }
        } else {
            LOGGER.log(Level.CONFIG, "Found existing game save directory at: {0}", saveFiles.getAbsolutePath());
        }

        File albumFiles = getAlbumDir();
        if (!albumFiles.exists()) {
            try {
                if (albumFiles.mkdirs()) {
                    LOGGER.log(Level.CONFIG, "Created game snapshots directory at: {0}", albumFiles.getAbsolutePath());
                } else {
                    LOGGER.log(Level.SEVERE, "Unable to create game snapshots directory at: {0}", albumFiles.getAbsolutePath());
                }
            } catch (SecurityException e) {
                LOGGER.severe("Could not create game snapshots directory due to Java security settings exception.");
                throw e;
            }
        } else {
            LOGGER.log(Level.CONFIG, "Found existing game snapshots directory at: {0}", albumFiles.getAbsolutePath());
        }
    }

    public FileFilter getGameSaveFilter() {
        return (File file) -> {
            DataInputStream dis = null;
            try {
                // TODO Read magic number of head of file.
                dis = new DataInputStream(new FileInputStream(file));
                return GameStateFile.isOurSaveFileType(dis);
            } catch (FileNotFoundException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            } finally {
                try {
                    dis.close();
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }

            return false;
        };
    }

    public DataInputStream getInputStreamFor(String fileName) {
        DataInputStream dis = null;
        try {
            // TODO Read magic number of head of file.
            dis = new DataInputStream(new FileInputStream(new File(getSaveDir() + File.separator + fileName)));
            return dis;
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return dis;
    }
    
    public FileOutputStream getOutputStreamFor(String fileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(getSaveDir() + File.separator + fileName);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fos;
    }
}
