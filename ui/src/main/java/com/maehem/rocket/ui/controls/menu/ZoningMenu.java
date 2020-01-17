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
package com.maehem.rocket.ui.controls.menu;

import com.maehem.rocket.engine.data.type.Zone;
import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.engine.renderer.ui.UI;
import com.maehem.rocket.ui.controls.DialogLayer;
import com.maehem.rocket.ui.controls.Toolbar;
import com.maehem.rocket.ui.controls.widgets.ToolbarItem;
import com.maehem.rocket.ui.controls.widgets.ToolbarButton;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author maehem
 */
public class ZoningMenu extends ToolbarItem {
    private static final Logger LOGGER = Logger.getLogger(ZoningMenu.class.getName());
    public static final Color COLOR = Color.YELLOW;
    private final UI ui;
    
    private static final ResourceBundle MSG = ResourceBundle.getBundle("MessageBundle");

    public ZoningMenu(Game game, UI ui, DialogLayer dialogLayer) throws IOException {
        super(MSG.getString("MENU_ZONING_MAIN"), new Image(Toolbar.class.getResource("/glyphs/zone.png").openStream()), COLOR, dialogLayer);
        this.ui = ui;

        initHabMenu(game);
        initAgMenu(game);
        initFabMenu(game);
        
        initFunctionButtons();
    }
    
    private void initHabMenu(Game game) {
        
        ToolbarButton light = makeButton( MSG.getString("MENU_ZONING_HAB_LITE"), "/glyphs/habitat-light.png", COLOR);
        light.setOnMouseClicked((t) -> {
            LOGGER.info(MSG.getString("MENU_ZONING_LOGMSG_SELECT_HAB_LITE"));
            select(false);            
            ui.requestZoneTool(Zone.TYPE.HABITAT_LIGHT);
        });
        ToolbarButton medium = makeButton( MSG.getString("MENU_ZONING_HAB_MED"), "/glyphs/habitat-medium.png", COLOR);
        medium.setOnMouseClicked((t) -> {
            LOGGER.info(MSG.getString("MENU_ZONING_LOGMSG_SELECT_HAB_MED"));
            select(false);
            ui.requestZoneTool(Zone.TYPE.HABITAT_MEDIUM);
        });
        ToolbarButton dense = makeButton( MSG.getString("MENU_ZONING_HAB_DENS"), "/glyphs/habitat-dense.png", COLOR);
        dense.setOnMouseClicked((t) -> {
            LOGGER.info(MSG.getString("MENU_ZONING_LOGMSG_SELECT_HAB_DENS"));
            select(false);
            ui.requestZoneTool(Zone.TYPE.HABITAT_DENSE);
        });
        
        addItem(MSG.getString("MENU_ZONING_HAB_MAIN"), "/glyphs/habitat.png", COLOR, new ToolbarButton[]{light, medium, dense});
    }
       
    private void initAgMenu(Game game) {
        
        ToolbarButton light = makeButton( MSG.getString("MENU_ZONING_AG_LITE"), "/glyphs/agriculture-light.png", COLOR);
        light.setOnMouseClicked((t) -> {
            LOGGER.info(MSG.getString("MENU_ZONING_LOGMSG_SELECT_AG_LITE"));
            select(false);            
            ui.requestZoneTool(Zone.TYPE.AGRICULTURE_LIGHT);
        });
        ToolbarButton medium = makeButton( MSG.getString("MENU_ZONING_AG_MED"), "/glyphs/agriculture-medium.png", COLOR);
        medium.setOnMouseClicked((t) -> {
            LOGGER.info(MSG.getString("MENU_ZONING_LOGMSG_SELECT_AG_MED"));
            select(false);
            ui.requestZoneTool(Zone.TYPE.AGRICULTURE_MEDIUM);
        });
        ToolbarButton dense = makeButton( MSG.getString("MENU_ZONING_AG_DENS"), "/glyphs/agriculture-dense.png", COLOR);
        dense.setOnMouseClicked((t) -> {
            LOGGER.info(MSG.getString("MENU_ZONING_LOGMSG_SELECT_AG_DENS"));
            select(false);
            ui.requestZoneTool(Zone.TYPE.AGRICULTURE_DENSE);
        });
        
        addItem(MSG.getString("MENU_ZONING_AG_MAIN"), "/glyphs/agriculture.png", COLOR, new ToolbarButton[]{light, medium, dense});
    }
    
    private void initFabMenu(Game game) {
        
        ToolbarButton light = makeButton( MSG.getString("MENU_ZONING_FAB_LITE"), "/glyphs/fabrication-light.png", COLOR);
        light.setOnMouseClicked((t) -> {
            LOGGER.info(MSG.getString("MENU_ZONING_LOGMSG_SELECT_FAB_LITE"));
            select(false);            
            ui.requestZoneTool(Zone.TYPE.FABRICATION_LIGHT);
        });
        ToolbarButton medium = makeButton( MSG.getString("MENU_ZONING_FAB_MED"), "/glyphs/fabrication-medium.png", COLOR);
        medium.setOnMouseClicked((t) -> {
            LOGGER.info(MSG.getString("MENU_ZONING_LOGMSG_SELECT_FAB_MED"));
            select(false);
            ui.requestZoneTool(Zone.TYPE.FABRICATION_MEDIUM);
        });
        ToolbarButton dense = makeButton( MSG.getString("MENU_ZONING_FAB_DENS"), "/glyphs/fabrication-dense.png", COLOR);
        dense.setOnMouseClicked((t) -> {
            LOGGER.info(MSG.getString("MENU_ZONING_LOGMSG_SELECT_FAB_DENS"));
            select(false);
            ui.requestZoneTool(Zone.TYPE.FABRICATION_DENSE);
        });
        
        addItem(MSG.getString("MENU_ZONING_FAB_MAIN"), "/glyphs/fabrication.png", COLOR, new ToolbarButton[]{light, medium, dense});
    }
       
}
