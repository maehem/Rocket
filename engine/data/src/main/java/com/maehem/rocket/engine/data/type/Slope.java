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
package com.maehem.rocket.engine.data.type;

// TODO: Move this to interior of XTER class.
/**
 *
 * @author maehem
 */
public class Slope implements Comparable<Slope> {

    public boolean t;
    public boolean r;
    public boolean b;
    public boolean l;

    public static final Slope[] MAP = new Slope[]{
                                                ////    T R B L           B L R T
        new Slope(false, false, false, false), //0x0: [0,0,0,0], //0x0: [0,0,0,0], 0

        new Slope(true, false, false,   true), //0x1: [0,0,1,1], //0x1: [1,1,0,0], 1
        new Slope(true,  true, false,  false), //0x2: [1,0,0,1], //0x2: [0,1,0,1], 2
        new Slope(false,   true, true, false), //0x3: [1,1,0,0], //0x3: [0,0,1,1], 3
        new Slope(false,  false, true,  true), //0x4: [0,1,1,0], //0x4: [1,0,1,0], 4

        new Slope(true, true, false, true), //0x5: [1,0,1,1], //0x5: [1,1,0,1], 5
        new Slope(true, true, true, false), //0x6: [1,1,0,1], //0x6: [0,1,1,1], 6
        new Slope(false, true, true, true), //0x7: [1,1,1,0], //0x7: [1,0,1,1], 7
        new Slope(true, false, true, true), //0x8: [0,1,1,1], //0x8: [1,1,1,0], 8

        new Slope(true, false, false, false), //0x9: [0,0,0,1], //0x9: [0,1,0,0], 9
        new Slope(false, true, false, false), //0xA: [1,0,0,0], //0xA: [0,0,0,1], 10
        new Slope(false, false, true, false), //0xB: [0,1,0,0], //0xB: [0,0,1,0], 11
        new Slope(false, false, false, true), //0xC: [0,0,1,0], //0xC: [1,0,0,0], 12

//        new Slope(false, false, true,   true), //0x1: [0,0,1,1], //0x1: [1,1,0,0], 1
//        new Slope(false,  true, false,  true), //0x2: [1,0,0,1], //0x2: [0,1,0,1], 2
//        new Slope(true,   true, false, false), //0x3: [1,1,0,0], //0x3: [0,0,1,1], 3
//        new Slope(true,  false, true,  false), //0x4: [0,1,1,0], //0x4: [1,0,1,0], 4
//
//        new Slope(false, true, true, true), //0x5: [1,0,1,1], //0x5: [1,1,0,1], 5
//        new Slope(true, true, false, true), //0x6: [1,1,0,1], //0x6: [0,1,1,1], 6
//        new Slope(true, true, true, false), //0x7: [1,1,1,0], //0x7: [1,0,1,1], 7
//        new Slope(true, false, true, true), //0x8: [0,1,1,1], //0x8: [1,1,1,0], 8
//
//        new Slope(false, false, false, true), //0x9: [0,0,0,1], //0x9: [0,1,0,0], 9
//        new Slope(false, true, false, false), //0xA: [1,0,0,0], //0xA: [0,0,0,1], 10
//        new Slope(true, false, false, false), //0xB: [0,1,0,0], //0xB: [0,0,1,0], 11
//        new Slope(false, false, true, false), //0xC: [0,0,1,0], //0xC: [1,0,0,0], 12
//
        new Slope(true, true, true, true), //0xD: [1,1,1,1]  //0xD: [1,1,1,1]  13
    };
    
    public Slope(boolean t, boolean r, boolean b, boolean l) {
        this.t = t;
        this.r = r;
        this.b = b;
        this.l = l;
    }

    @Override
    public int compareTo(Slope o) {
        if (this.t == o.t && this.r == o.r && this.b == o.b && this.l == o.l ) {
            return 0;
        }
        return 1;
    }

    @Override
    public String toString() {
        return     "T:" + String.valueOf(t) 
                + " R:" + String.valueOf(r)
                + " B:" + String.valueOf(b)
                + " L:" + String.valueOf(l);
    }   
    
}
