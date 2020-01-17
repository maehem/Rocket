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
import com.maehem.rocket.engine.data.type.GameInfo.SPEED;
import com.maehem.rocket.engine.game.events.GameEvent;
import com.maehem.rocket.engine.game.events.GameListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mark J Koch <rocketcolony-maintainer@maehem.com>
 */
public class Game {

    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    public ArrayList<GameListener> listeners = new ArrayList<>();

    public static final String CURRENCY_SYMBOL = "$";

    public static final long DAY_LENGTH = 4; // Ticks
    public static final long MONTH_LENGTH = 28 * DAY_LENGTH; // Ticks
    public static final long YEAR_LENGTH = 12 * MONTH_LENGTH; // Ticks

    private boolean initialized = false;
    
    private boolean running = false;  // Running==true or Paused==false

    //  corners: ['1000','0100','0010','0001'],
    //public final int corners[] = {0b1000, 0b0100, 0b0010, 0b0001};
    public final int corners[][] = {
        {4, 8, 1, 2},
        {2, 4, 8, 1},
        {1, 2, 4, 8},
        {8, 1, 2, 4}
    };

    public Data data = null;
    private FileSystem userGameDir;

    private int subTicks = 0;

    public Game() {
        userGameDir = new FileSystem();
        
        userGameDir.initGameFilesDirectories();        
    }

    public void init() {
        Data newData = new Data();
        newData.initMap();
        newData.setLoaded(true);
        setData(newData);        
    }

    public final void setData(Data data) {
        this.data = data;

        if (data.isLoaded()) {
            LOGGER.log(Level.INFO, "New city loaded: {0}", data.mapInfo.getName());
            doNotify(GameEvent.TYPE.DATA_LOADED);
            setRunning(running);

            initialized = true;
        }
    }

    /**
     * Game state is computed here.
     */
    public void tick() {
        if (!initialized || !running) {
            return;
        }

        subTicks++;
        if (subTicks > 511) {
            subTicks = 0;
        }

        switch (data.mapInfo.speed) {
            case SLOW:
                if (subTicks % 36  == 0) {
                    doTick();
                }
                break;
            case MEDIUM:
                if (subTicks % 22 == 0) {
                    doTick();
                }
                break;
            case FAST:
                if (subTicks % 9 == 0) {
                    doTick();
                }
                break;
            default:
            case PLAID:
                doTick();
                break;

        }
        // What speed are we running at?

        //data.mapInfo.setTicksElapsed(data.mapInfo.getTicksElapsed() + 1);
        // Update game state
    }

    private void doTick() {
        data.mapInfo.setTicksElapsed(data.mapInfo.getTicksElapsed() + 1);
        doNotify(GameEvent.TYPE.TICK);
    }

    public void addListener(GameListener l) {
        listeners.add(l);
    }

    public void removeListener(GameListener l) {
        listeners.remove(l);
    }

    public void doNotify(GameEvent.TYPE type) {
        listeners.forEach((l) -> {
            l.gameEvent(new GameEvent(this, type));
        });
    }

    public String getDate() {
        long ticks = data.mapInfo.getTicksElapsed();

        long year = ticks / YEAR_LENGTH;
        long month = (ticks % YEAR_LENGTH) / MONTH_LENGTH + 1;
        long day = ((ticks % YEAR_LENGTH) % MONTH_LENGTH) / DAY_LENGTH + 1;
        year += data.mapInfo.yearFounded;

        return year + "/" + month + "/" + day;
    }

    public void setGameSpeed(SPEED spd) {
        data.mapInfo.speed = spd;
        doNotify(GameEvent.TYPE.SPEED);
        switch ( spd ) {
            case SLOW:
                LOGGER.info("Game speed set to SLOW.");
                break;
            case MEDIUM:
                LOGGER.info("Game speed set to MEDIUM.");
                break;
            case FAST:
                LOGGER.info("Game speed set to FAST.");
                break;
            case PLAID:
                LOGGER.info("Game speed set to PLAID.");
                break;
        }
        // TODO:  Does anyome need to know about speed changes?
    }
    
    public void setRunning(boolean running) {
        this.running = running;
        if ( running ) {
            doNotify(GameEvent.TYPE.RUNNING);
            LOGGER.info("Game Running");
        } else {
            doNotify(GameEvent.TYPE.PAUSED);
            LOGGER.info("Game Paused");
        }

    }
    
    public boolean isRunning() {
        return running;
    }

}
