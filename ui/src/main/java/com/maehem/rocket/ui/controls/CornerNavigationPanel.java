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

import com.maehem.rocket.engine.renderer.Graphics;
import com.maehem.rocket.engine.renderer.ui.UI;
import com.maehem.rocket.ui.controls.widgets.InfoMap;
import com.maehem.rocket.ui.controls.widgets.RoundButton;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light.Distant;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 *
 * @author maehem
 */
public class CornerNavigationPanel extends Group {
    private final static Logger LOGGER = Logger.getLogger(CornerNavigationPanel.class.getName());
    
    private final static double ZOOM_AMOUNT = 5.0;
    
    // coordinates of the points of polygon 
    private final static double points[] = {
        -10, 0,
        100, 0,
        200, 100,
        200, 210,
        -10, 210
    };

    // create a polygon 
    private final static Polygon polygon = new Polygon(points);
    private final static Polygon polygonDs = new Polygon(points);
    private final Graphics gfx;
    private final DialogLayer dialogLayer;

    public CornerNavigationPanel(Graphics gfx, DialogLayer dl) {
        this.gfx = gfx;
        this.dialogLayer = dl;
        
        Distant light = new Distant();
        light.setAzimuth(-135.0f);

        Lighting l = new Lighting();
        l.setLight(light);
        l.setSurfaceScale(5.0f);

        polygon.setEffect(l);
                
        DropShadow ds = new DropShadow();
        ds.setSpread(0.01);
        ds.setRadius(30.0);
        ds.setOffsetX(-4);
        ds.setOffsetY(4);
        ds.setColor(Color.BLACK);
        polygonDs.setEffect(ds);

        polygon.setFill(Color.LIGHTGRAY);
        getChildren().addAll(polygonDs, polygon);
        
        InfoMap map = new InfoMap();
        map.setLayoutX(20);
        map.setLayoutY(20);
        getChildren().add(map);
        
        initButtons();
    }

    private void initButtons() {
        URL zoomOutGlyphResource = CornerNavigationPanel.class.getResource("/glyphs/zoom-out.png");
        RoundButton zoomOutButton;
        try {
            zoomOutButton = new RoundButton(new Image(zoomOutGlyphResource.openStream()),
                    28, 
                    20, 126 );
            zoomOutButton.setOnMouseClicked((t) -> {
                gfx.ui.zoom(-ZOOM_AMOUNT);
            });
            getChildren().add(zoomOutButton);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
        URL zoomInGlyphResource = CornerNavigationPanel.class.getResource("/glyphs/zoom-in.png");
        RoundButton zoomInButton;
        try {
            zoomInButton = new RoundButton(new Image(zoomInGlyphResource.openStream()),
                    28, 
                    155, 126 );
            zoomInButton.setOnMouseClicked((t) -> {
                gfx.ui.zoom(ZOOM_AMOUNT);
            });
            getChildren().add(zoomInButton);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
        URL cwButtonResource = CornerNavigationPanel.class.getResource("/glyphs/rotate-cw.png");
        RoundButton rotateCWButton;
        try {
            rotateCWButton = new RoundButton(new Image(cwButtonResource.openStream()),
                    28, 
                    124, 160 );
            rotateCWButton.setOnMouseClicked((t) -> {
                gfx.ui.rotate(UI.MAP_ROTATE.CW);
            });
            getChildren().add(rotateCWButton);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
        URL ccwButtonResource = CornerNavigationPanel.class.getResource("/glyphs/rotate-ccw.png");
        RoundButton rotateCCWButton;
        try {
            rotateCCWButton = new RoundButton(new Image(ccwButtonResource.openStream()),
                    28,
                    52, 160 );
            rotateCCWButton.setOnMouseClicked((t) -> {
                gfx.ui.rotate(UI.MAP_ROTATE.CCW);
            });
            getChildren().add(rotateCCWButton);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
        URL layerToggleButtonResource = CornerNavigationPanel.class.getResource("/glyphs/layers-toggle.png");
        RoundButton layerToggleButton;
        try {
            layerToggleButton = new RoundButton(new Image(layerToggleButtonResource.openStream()),
                    36,
                    10, 40 );
            layerToggleButton.setOnMouseClicked((t) -> {
                DialogPanel d = new LayerTogglesDialog(gfx, dialogLayer, 300, -2*gfx.canvas.getHeight()/3);
            });
            getChildren().add(layerToggleButton);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }        

        URL gridToggleButtonResource = CornerNavigationPanel.class.getResource("/glyphs/grid-toggle.png");
        RoundButton gridToggleButton;
        try {
            gridToggleButton = new RoundButton(new Image(gridToggleButtonResource.openStream()),
                    36,
                    40, 10 );
            gridToggleButton.setOnMouseClicked((t) -> {
                gfx.showGrid = !gfx.showGrid;
            });
            getChildren().add(gridToggleButton);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }        
    }
    
    public double getSizeY() {
        return 200;
    }

    public double getSizeX() {
        return 200;
    }

}
