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

import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.engine.renderer.ui.UI;
import com.maehem.rocket.ui.controls.DialogLayer;
import com.maehem.rocket.ui.controls.Toolbar;
import com.maehem.rocket.ui.controls.menu.loadsavesettings.GameLoadDialog;
import com.maehem.rocket.ui.controls.menu.loadsavesettings.GameNewDialog;
import com.maehem.rocket.ui.controls.menu.loadsavesettings.GameSaveAsDialog;
import com.maehem.rocket.ui.controls.menu.loadsavesettings.GameSaveDialog;
import com.maehem.rocket.ui.controls.menu.loadsavesettings.GameSettingsDialog;
import com.maehem.rocket.ui.controls.widgets.ToolbarItem;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author maehem
 */
public class LoadSaveSettingsMenu extends ToolbarItem {

    private static final Logger LOGGER = Logger.getLogger(LoadSaveSettingsMenu.class.getName());
    public static final Color COLOR = Color.LIGHTGREY;

    private static final ResourceBundle MSG = ResourceBundle.getBundle("MessageBundle");

    public LoadSaveSettingsMenu(Game game, UI ui, DialogLayer dialogLayer) throws IOException {
        super(MSG.getString("MENU_LSS_MAIN"), new Image(Toolbar.class.getResource("/glyphs/lss.png").openStream()), COLOR, dialogLayer);

        addItem(MSG.getString("MENU_LSS_SAVE"), "/glyphs/save.png", COLOR, saveHandler(game));
        addItem(MSG.getString("MENU_LSS_SAVEAS"), "/glyphs/save-as.png", COLOR, saveAsHandler(game));
        addItem(MSG.getString("MENU_LSS_LOAD"), "/glyphs/load.png", COLOR, loadHandler(game));
        addItem(MSG.getString("MENU_LSS_NEW"), "/glyphs/new-colony.png", COLOR, (EventHandler) newGameHandler(game));
        addItem(MSG.getString("MENU_LSS_SETTINGS"), "/glyphs/settings.png", COLOR, settingsHandler(game));

        initFunctionButtons();
    }

    private EventHandler loadHandler(Game game) {
        return (EventHandler) (Event t) -> {
            DialogLayer dialogLayer = getDialogLayer();
            if (dialogLayer != null) {
                GameLoadDialog d = new GameLoadDialog(game, dialogLayer, 300, -600);
            }
        };
    }

    private EventHandler saveHandler(Game game) {
        return (EventHandler) (Event t) -> {
            DialogLayer dialogLayer = getDialogLayer();
            if (dialogLayer != null) {
                if (game.data.mapInfo.getFileSaveName() != null) {
                    GameSaveDialog d = new GameSaveDialog(game, dialogLayer, 300, -600);
                } else {
                    GameSaveAsDialog d = new GameSaveAsDialog(game, dialogLayer, 300, -600);
                }
            }
        };
    }

    private EventHandler saveAsHandler(Game game) {
        return (EventHandler) (Event t) -> {
            DialogLayer dialogLayer = getDialogLayer();
            if (dialogLayer != null) {
                GameSaveAsDialog d = new GameSaveAsDialog(game, dialogLayer, 300, -600);
            }
        };
    }

    private EventHandler newGameHandler(Game game) {
        return (EventHandler) (Event t) -> {
            DialogLayer dialogLayer = getDialogLayer();
            if (dialogLayer != null) {
                GameNewDialog d = new GameNewDialog(game, dialogLayer, 0, -900);
            }
        };
    }

    private EventHandler settingsHandler(Game game) {
        return (EventHandler) (Event t) -> {
            DialogLayer dialogLayer = getDialogLayer();
            if (dialogLayer != null) {
                GameSettingsDialog d = new GameSettingsDialog(game, dialogLayer, 300, -600);
            }
        };
    }
}
