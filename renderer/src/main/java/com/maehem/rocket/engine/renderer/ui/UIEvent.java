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

import com.maehem.rocket.engine.data.Cell;


/**
 *
 * @author Mark J Koch <rocketcolony-maintainer@maehem.com>
 */
public class UIEvent {
    public enum Type { CELL_SELECTED, CURSOR_CHANGE }
    
    public Type type ;
    public Cell cell;
    public Object[] objects;
    
    public UIEvent( Type type, Cell cell, Object[] objects) {
        this.type = type;
        this.cell = cell;
        this.objects = objects;
    }
    
    
}
