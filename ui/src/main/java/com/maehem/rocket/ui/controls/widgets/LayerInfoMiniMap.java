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

import com.maehem.rocket.engine.data.Cell;
import com.maehem.rocket.engine.data.type.Zone;
import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.engine.renderer.ZoneRenderer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

/**
 *
 * @author maehem
 */
public class LayerInfoMiniMap extends Canvas {

    private final Game game;

    public LayerInfoMiniMap( double size, Game game ) {
        super(size, size);
        this.game = game;
        setRotate(45.0);
    }
    
    public void refresh() {
        GraphicsContext gc = this.getGraphicsContext2D();
        
        gc.setFill(Color.DARKORANGE);
        gc.fillRect(0,0,this.getWidth(), this.getHeight());
        
        PixelWriter pw = gc.getPixelWriter();
        for ( int x=0; x<game.data.getMapSize(); x++) {
            for ( int y=0; y<game.data.getMapSize(); y++) {
                Cell cell = game.data.map[x][y];
                if ( cell.xzon.type != Zone.TYPE.NONE ) {                    
                    pw.setColor(x, y, ZoneRenderer.getColor(cell.xzon.type));
                } else if ( cell.structure.id >= 100 ) {
                    pw.setColor(x, y, Color.WHITE);
                }
            }
        }
    }
}
