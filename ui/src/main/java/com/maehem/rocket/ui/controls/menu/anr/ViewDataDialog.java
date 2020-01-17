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
package com.maehem.rocket.ui.controls.menu.anr;

import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.ui.controls.DialogLayer;
import com.maehem.rocket.ui.controls.DialogPanel;
import com.maehem.rocket.ui.controls.Toolbar;
import com.maehem.rocket.ui.controls.widgets.CompassWidget;
import com.maehem.rocket.ui.controls.widgets.LayerInfoMiniMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 *
 * @author maehem
 */
public class ViewDataDialog extends DialogPanel {
    private static final Logger LOGGER = Logger.getLogger(ViewDataDialog.class.getName());
    
    private static final double SIZE = 600;
    private final Game game;
    private static final ResourceBundle MSG = ResourceBundle.getBundle("MessageBundle");
    
    public ViewDataDialog(Game game, DialogLayer dialogLayer, double x, double y) {        
        super(MSG.getString("MENU_ANR_VIEW"), dialogLayer, x, y);
        this.game = game;
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10));
        hbox.getChildren().addAll(layerTogglePane(), layerViewPane());

        // Add more optons later.
        setContent(hbox);

        getDoneButton().addEventHandler(MouseEvent.MOUSE_CLICKED, (t) -> {
            LOGGER.log(Level.FINER, "{0} {1}", new Object[]{MSG.getString("MENU_ANR_VIEW"),MSG.getString("DIALOG_DONE")});
            // Let's load something.
        });
        
    }
    
    private Pane layerTogglePane() {
        VBox pane = new VBox();
        pane.setBackground(new Background(new BackgroundFill(
                Color.DARKGRAY, 
                new CornerRadii(Toolbar.CORNER_ARC), 
                Insets.EMPTY
        )));
        pane.setPadding(new Insets(Toolbar.CORNER_ARC));
        pane.setEffect(Toolbar.DROP_SHADOW);

        pane.setAlignment(Pos.CENTER);
        Button b = new Button("", Toolbar.createGlyph("/glyphs/zone.png"));
        b.setBackground(new Background(new BackgroundFill(Color.YELLOW, new CornerRadii(6), Insets.EMPTY)));
        
        pane.getChildren().add(b);
        
        return pane;
    }
    
    private Pane layerViewPane() {
        int MAP_SIZE = game.data.getMapSize();
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(SIZE, SIZE);
        pane.setMinWidth(SIZE);
            
        // Mini Map
        LayerInfoMiniMap miniMap = new LayerInfoMiniMap(MAP_SIZE, game);
        miniMap.setScaleX(SIZE/MAP_SIZE*0.707);
        miniMap.setScaleY(SIZE/MAP_SIZE*0.707); 
        AnchorPane.setLeftAnchor(miniMap, (SIZE-MAP_SIZE)/2 /*- miniMap.getScaleX()/2 */ );
        AnchorPane.setTopAnchor( miniMap, (SIZE-MAP_SIZE)/2 /*- miniMap.getScaleY()/2 */ );
        miniMap.refresh();
                        
        // Compass
        CompassWidget cw = new CompassWidget();
        cw.setRotate(45.0 + (90*game.data.mapInfo.getRotation()));
        
        AnchorPane.setLeftAnchor(cw, 40.0);
        AnchorPane.setTopAnchor(cw, 40.0);        
        
        // Legend
        Polygon p = new Polygon(
                0,  50,
                50, 0,
                160,0,
                160,160,
                0,  160                
        );
        
        p.setFill(Color.DIMGREY);
        AnchorPane.setRightAnchor(p, 0.0);
        AnchorPane.setBottomAnchor( p, 0.0);
        
        pane.getChildren().addAll(miniMap,cw, p);
        
        return pane;
    }
}
