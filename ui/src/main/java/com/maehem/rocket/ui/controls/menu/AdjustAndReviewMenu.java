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
import com.maehem.rocket.ui.controls.menu.anr.AlbumViewDialog;
import com.maehem.rocket.ui.controls.menu.anr.BudgetDialog;
import com.maehem.rocket.ui.controls.menu.anr.TaxesDialog;
import com.maehem.rocket.ui.controls.menu.anr.ViewDataDialog;
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
public class AdjustAndReviewMenu extends ToolbarItem {
    private static final Logger LOGGER = Logger.getLogger(AdjustAndReviewMenu.class.getName());
    public static final Color COLOR = Color.LIGHTGREEN;
    
    private static final ResourceBundle MSG = ResourceBundle.getBundle("MessageBundle");
    
    public AdjustAndReviewMenu(Game game, UI ui, DialogLayer dialogLayer) throws IOException {
        super(MSG.getString("MENU_ANR_MAIN"), new Image(Toolbar.class.getResource("/glyphs/anr.png").openStream()), COLOR, dialogLayer);

        addItem(MSG.getString("MENU_ANR_ALBUM"), "/glyphs/album.png", COLOR, albumHandler(game));
        addItem(MSG.getString("MENU_ANR_BUDGET"), "/glyphs/budget.png", COLOR, budgetHandler(game));
        addItem(MSG.getString("MENU_ANR_TAXES"), "/glyphs/taxes.png", COLOR, taxesHandler(game));
        addItem(MSG.getString("MENU_ANR_VIEW"), "/glyphs/view-data.png", COLOR, viewDataHandler(game));

        initFunctionButtons();
    }
    
    private EventHandler albumHandler(Game game) {
        return (EventHandler) (Event t) -> {
            LOGGER.fine(MSG.getString("MENU_ANR_LOGMSG_ALBUM_DIALOG"));
            DialogLayer dialogLayer = getDialogLayer();
            if ( dialogLayer != null ) {
                AlbumViewDialog d = new AlbumViewDialog(game, dialogLayer, 300, -600);
            }
        };
    }
    
    private EventHandler budgetHandler(Game game) {
        return (EventHandler) (Event t) -> {
            LOGGER.fine(MSG.getString("MENU_ANR_LOGMSG_BUDGET_DIALOG"));
            DialogLayer dialogLayer = getDialogLayer();
            if ( dialogLayer != null ) {
                BudgetDialog d = new BudgetDialog(game, dialogLayer, 300, -600);
            }
        };
    }

    private EventHandler taxesHandler(Game game) {
        return (EventHandler) (Event t) -> {
            LOGGER.fine(MSG.getString("MENU_ANR_LOGMSG_TAXES_DIALOG"));
            DialogLayer dialogLayer = getDialogLayer();
            if ( dialogLayer != null ) {
                TaxesDialog d = new TaxesDialog(game, dialogLayer, 300, -600);
            }
        };
    }

    private EventHandler viewDataHandler(Game game) {
        return (EventHandler) (Event t) -> {
            LOGGER.fine(MSG.getString("MENU_ANR_LOGMSG_VIEWDATA_DIALOG"));
            DialogLayer dialogLayer = getDialogLayer();
            if ( dialogLayer != null ) {
                ViewDataDialog d = new ViewDataDialog(game, dialogLayer, 300, -600);
            }
        };
    }
}
