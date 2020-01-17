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

import com.maehem.rocket.ui.controls.InfoBar2;
import static com.maehem.rocket.ui.controls.InfoBar2.STUB_WIDTH;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 *
 * @author maehem
 */
public class ModalGlyph extends StackPane {
    private static final Logger LOGGER = Logger.getLogger(ModalGlyph.class.getName());
    
    private final ImageView top;
    private final ImageView bottom;

    public ModalGlyph( String imgPathBottom, String imgPathTop ) {
        bottom = createGlyph(imgPathBottom);
        bottom.setOpacity(0.7);
        top = createGlyph(imgPathTop);
        
        getChildren().addAll( bottom,top );
        setValue(0.0);
    }
    
    private ImageView createGlyph(String path) {
        try {
            URL glyphResource = InfoBar2.class.getResource(path);
            ImageView glyphImage = new ImageView(new Image(glyphResource.openStream()));
            glyphImage.setPreserveRatio(true);
            glyphImage.setFitWidth(STUB_WIDTH);
            return glyphImage;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return new ImageView("Error");
        }
    }
    
    public final void setValue( double val ) {
        top.setOpacity(val);
    }
    
    public final double getValue() {
        return top.getOpacity();
    }    
}
