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
package com.maehem.rocket.engine.logging;

import java.util.ArrayList;
import java.util.logging.LogRecord;

/**
 *
 * @author maehem
 */
public class LoggingMessageList extends ArrayList<LogRecord> {
    
    ArrayList<LogListener> listeners = new ArrayList<>();
    
    @Override
    public boolean add(LogRecord e) {
        boolean add = super.add(e);
        listeners.forEach((t) -> {
            t.messageAdded(e);
        });
        
        return add;
    }

    public void addListener( LogListener l) {
        listeners.add(l);
    }
    
    public void removeListener( LogListener l) {
        listeners.remove(l);
    }
}
