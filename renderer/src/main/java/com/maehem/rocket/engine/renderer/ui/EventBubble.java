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
package com.maehem.rocket.engine.renderer.ui;

import com.maehem.rocket.engine.data.Point;
import com.maehem.rocket.engine.renderer.Graphics;
import javafx.animation.AnimationTimer;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;

/**
 * A UI screen element that posts an animated "bubble" to indicate some important
 * change in the game state.
 * 
 * 
 * @author maehem
 */
public class EventBubble extends StackPane {

    public static final double DEF_W = 100;
    public static final double DEF_H = 24;
    private final Point cell;
    private final Image image;
    private double opacity = 1.0;
    private double opacityAmount = 0.001;
    private boolean done = false;
    
    public EventBubble(Point cell, String text) {
        this.cell = cell;
        this.setLayoutX(-DEF_W/2);
        this.setLayoutY(-100);
        Rectangle r = new Rectangle( 0,0,DEF_W, DEF_H );
        r.setFill(Color.LIGHTGREY);
        r.setStroke(Color.DARKGREY);
        r.setStrokeWidth(3);
        r.setArcHeight(DEF_H/2);
        r.setArcWidth(DEF_H/2);
        getChildren().addAll(r, new Text(text));
       
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        image = this.snapshot(sp, null);
    }
    
    public void start() {
        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long l) {
                setLayoutY(getLayoutY()-0.8);
                opacityAmount += opacityAmount*0.04; // Exponential drop off.
                opacity -= opacityAmount;
                if ( getLayoutY() < -300 || opacity < 0 ) {
                    stop();
                    done = true;
                }
                
            }
        };
        
        // TODO register with UI layer.  
        // Notify UI layer when stop() called and remove from scene.
        
        at.start();
    }
    
    public Point getCell() {
        return cell;
    }

    public void draw(Graphics gfx, GraphicsContext gc) {
        int altitude = gfx.game.data.map[cell.x][cell.y].altm.altitude;

        int x = gfx.getDrawX(cell.x, cell.y);
        int y = gfx.getDrawY(cell.x, cell.y);

        int aY = -(altitude * Graphics.LAYER_OFFSET);
        
        Affine transform = gc.getTransform();
        gc.translate(0, aY);
        
        gc.setGlobalAlpha(opacity);
        gc.drawImage(image, x+getLayoutX(), y+getLayoutY());
        gc.setGlobalAlpha(1.0);
        
        gc.setTransform(transform);
    }
    
    public boolean isDone() {
        return done;
    }
}
