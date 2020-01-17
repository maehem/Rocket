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
package com.maehem.rocket.ui.controls.widgets;

import com.maehem.rocket.ui.controls.Toolbar;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;

/**
 *
 * @author maehem
 */
public class CompassWidget extends StackPane {
    private static final double SIZE = 100;
    
    double [] spike = {
            0,0,
            0.2, -0.2,
            0, -1.0
    };
    
    public CompassWidget() {
        setPrefSize(SIZE, SIZE);
        setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(SIZE/2), Insets.EMPTY)));
        //setRotate(45);
        Text n = cText("N");
        n.setTranslateY(-SIZE/3);
        Text s = cText("S");
        s.setTranslateY(SIZE/3);
        Text w = cText("W");
        w.setTranslateX(-SIZE/3);
        Text e = cText("E");
        e.setTranslateX(SIZE/3);
        
        // Compass spikes.  
        Polygon p1 = spike(SIZE/4,  0, false);
        Polygon p2 = spike(SIZE/4, 90, false);
        Polygon p3 = spike(SIZE/4,180, false);
        Polygon p4 = spike(SIZE/4,270, false);
        
        Polygon p1b = spike(SIZE/4,  0, true);
        Polygon p2b = spike(SIZE/4, 90, true);
        Polygon p3b = spike(SIZE/4,180, true);
        Polygon p4b = spike(SIZE/4,270, true);
        
        Group compass = new Group(p1,p1b,p2,p2b,p3,p3b,p4,p4b);
        
        getChildren().addAll(n,e,s,w,compass);
        
        setEffect(Toolbar.DROP_SHADOW);
    }
    
    private Text cText(String dirText) {
        Text t = new Text(dirText);
        t.setFont(new Font(SIZE/7));
        return t;
    }
    
    private Polygon spike( double size, double angle, boolean mirror ) {
        Polygon p = new Polygon(spike);
        p.setScaleX(size);
        p.setScaleY(size);
        p.setStrokeWidth(0.01);
        p.setStroke(Color.DARKGREY);
        if ( mirror ) {
            p.setFill(Color.ORANGE);
            if ( angle == 90.0 || angle == 270.0 ) {
                p.getTransforms().add(new Scale(1, -1));
            } else {
                p.getTransforms().add(new Scale(-1, 1));
            }
        } else {
            p.setFill(Color.DARKGRAY);
        }
        
        p.getTransforms().add(new Rotate(angle));
        
//        AnchorPane.setLeftAnchor(p, 50.0);
//        AnchorPane.setTopAnchor(p, 50.0);
        
        return p;
    }
}
