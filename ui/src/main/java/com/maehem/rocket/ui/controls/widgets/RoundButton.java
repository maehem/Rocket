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

import javafx.scene.effect.Light.Distant;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author maehem
 */
public class RoundButton extends StackPane {

    public RoundButton(Image image, double diameter, double locX, double locY) {
        Circle outerCircle = new Circle(diameter / 2);
        setLayoutX(locX);
        setLayoutY(locY);

        Distant light = new Distant();
        light.setAzimuth(-135.0f);

        Lighting l = new Lighting();
        l.setLight(light);
        l.setSpecularExponent(2.0);
        l.setSurfaceScale(5.0f);

        ImageView iv = new ImageView(image);
        iv.setPreserveRatio(true);
        iv.setFitWidth(diameter * 2 / 3);

        outerCircle.setEffect(l);
        outerCircle.setFill(Color.LIGHTGREY);
        getChildren().addAll(outerCircle, iv);
    }

}
