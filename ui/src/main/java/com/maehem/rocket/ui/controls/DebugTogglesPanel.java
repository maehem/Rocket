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
package com.maehem.rocket.ui.controls;

import com.maehem.rocket.engine.data.Data;
import com.maehem.rocket.engine.game.events.GameEvent;
import com.maehem.rocket.engine.game.events.GameListener;
import com.maehem.rocket.engine.renderer.Graphics;
import com.maehem.rocket.engine.renderer.debug.Debug;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 *
 * @author maehem
 */
public class DebugTogglesPanel extends GridPane implements GameListener {

    private final Data data;
    private final Debug debug;
    
    private final ToggleButton showCoords;
    private final ToggleButton ghostStructures;
    private final ToggleButton hideTerrain;
    private final ToggleButton hideZones;
    private final ToggleButton hideWater;
    private final ToggleButton hideTerrainEdge;
    private final ToggleButton hideNetworks;
    private final ToggleButton hideBuildings;

    public DebugTogglesPanel(Graphics gfx) {
        this.data = gfx.game.data;
        this.debug = gfx.debug;
        
        gfx.game.addListener(this);
        setHgap(4);
        setVgap(4);
        //setGridLinesVisible(true);
        
        
        setBorder(new Border(new BorderStroke(Color.GREY.brighter(), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        setPadding(new Insets(4));
        
        showCoords = new ToggleButton("", Toolbar.createGlyph("/glyphs/xy-visible.png"));
        showCoords.setTooltip(new Tooltip("Show Tile Coordinates"));
        showCoords.setSelected(debug.showTileCoordinates);
        showCoords.selectedProperty().addListener((ov, prev, current) -> {
            debug.showTileCoordinates = current;
        });
        add(showCoords, 0, 0);
        
        ghostStructures = new ToggleButton("", Toolbar.createGlyph("/glyphs/ghost-buildings.png"));
        ghostStructures.setTooltip(new Tooltip("Lower Building Opacity"));
        ghostStructures.setSelected(debug.lowerBuildingOpacity);
        ghostStructures.selectedProperty().addListener((ov, prev, current) -> {
            debug.lowerBuildingOpacity = current;
        });
        add(ghostStructures, 0, 1);
        
        hideTerrain = new ToggleButton("", Toolbar.createGlyph("/glyphs/hide-terrain.png"));
        hideTerrain.setTooltip(new Tooltip("Hide Terrain"));
        hideTerrain.setSelected(debug.hideTerrain);
        hideTerrain.selectedProperty().addListener((ov, prev, current) -> {
            debug.hideTerrain = current;
        });
        add(hideTerrain, 2, 0);
        
        hideZones = new ToggleButton("", Toolbar.createGlyph("/glyphs/hide-zones.png"));
        hideZones.setTooltip(new Tooltip("Hide Zones"));
        hideZones.setSelected(debug.hideZones);
        hideZones.selectedProperty().addListener((ov, prev, current) -> {
            debug.hideZones = current;
        });
        add(hideZones, 2, 1);
        
        hideWater = new ToggleButton("", Toolbar.createGlyph("/glyphs/hide-water.png"));
        hideWater.setTooltip(new Tooltip("Hide Water"));
        hideWater.setSelected(debug.hideWater);
        hideWater.selectedProperty().addListener((ov, prev, current) -> {
            debug.hideWater = current;
        });
        add(hideWater, 2, 2);
        
        hideNetworks = new ToggleButton("", Toolbar.createGlyph("/glyphs/hide-networks.png"));
        hideNetworks.setTooltip(new Tooltip("Hide Networks"));
        hideNetworks.setSelected(debug.hideNetworks);
        hideNetworks.selectedProperty().addListener((ov, prev, current) -> {
            debug.hideNetworks = current;
        });
        add(hideNetworks, 2, 3);
        
        hideTerrainEdge = new ToggleButton("", Toolbar.createGlyph("/glyphs/hide-terrain-edge.png"));
        hideTerrainEdge.setTooltip(new Tooltip("Hide Terrain Edge"));
        hideTerrainEdge.setSelected(debug.hideTerrainEdge);
        hideTerrainEdge.selectedProperty().addListener((ov, prev, current) -> {
            debug.hideTerrainEdge = current;
        });
        add(hideTerrainEdge, 3, 0);

        hideBuildings = new ToggleButton("", Toolbar.createGlyph("/glyphs/hide-buildings.png"));
        hideBuildings.setTooltip(new Tooltip("Hide Buildings"));
        hideBuildings.setSelected(debug.hideBuildings);
        hideBuildings.selectedProperty().addListener((ov, prev, current) -> {
            debug.hideBuildings = current;
        });
        add(hideBuildings, 3, 1);
    }

    @Override
    public void gameEvent(GameEvent e) {
        switch( e.type ) {
            case DATA_LOADED:
                // Not used yet.
                break;
        }
    }
    
}
