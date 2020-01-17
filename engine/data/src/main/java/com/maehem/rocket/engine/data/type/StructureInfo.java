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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *   0 -  99 -- Surface debris, rocks, minerals, liquid
 * 100 - 199 -- Roads, power lines (Network)
 * 200 - 299 -- Habitat Structures
 * 300 - 499 -- Resource Generating Structures
 * 500 - 699 -- Government Infrastructure Structures
 * 700 - 799 -- Special Structures
 * 800 - 999 -- Monuments
 * 
 * @author maehem
 */
public class StructureInfo {

    public int id=0;
    
    public static final int TUNNEL_LEFT = 165;
    public static final int TUNNEL_RIGHT = 166;
    
    public static final int ROCKS_0 = 0;
    public static final int ROCKS_1 = 1;
    public static final int ROCKS_2 = 2;
    public static final int ROCKS_3 = 3;
    public static final int ROCKS_4 = 4;
    public static final int ROCKS_5 = 5;
    public static final int ROCKS_6 = 6;
    public static final int ROCKS_7 = 7;
    public static final int ROCKS_8 = 8;
    public static final int ROCKS_9 = 9;
    
    public static final int MINERALS_0 = 10;
    public static final int MINERALS_1 = 11;
    public static final int MINERALS_2 = 12;
    public static final int MINERALS_3 = 13;
    public static final int MINERALS_4 = 14;
    public static final int MINERALS_5 = 15;
    public static final int MINERALS_6 = 16;
    public static final int MINERALS_7 = 17;
    public static final int MINERALS_8 = 18;
    public static final int MINERALS_9 = 19;
    
    public static final int DEBRIS_START = 0;
    public static final int DEBRIS_END   = 99;
    
    public static final int ROADS_START = 100;
    public static final int ROADS_END   = 149;
    
    public static final int POWERLINES_START = 150;
    public static final int POWERLINES_END   = 199;
    
    public static final int HABITAT_LIGHT_START = 200;
    public static final int HABITAT_LIGHT_END   = 232;
    
    public static final int HABITAT_MEDIUM_START = 233;
    public static final int HABITAT_MEDIUM_END   = 265;
    
    public static final int HABITAT_DENSE_START = 266;
    public static final int HABITAT_DENSE_END   = 299;
    
    public static final int HABITAT_START = HABITAT_LIGHT_START;
    public static final int HABITAT_END   = HABITAT_DENSE_END;
    
    public static final int AGRICULTURE_LIGHT_START = 300;
    public static final int AGRICULTURE_LIGHT_END   = 332;
    
    public static final int AGRICULTURE_MEDIUM_START = 333;
    public static final int AGRICULTURE_MEDIUM_END   = 365;
    
    public static final int AGRICULTURE_DENSE_START = 366;
    public static final int AGRICULTURE_DENSE_END   = 399;
    
    public static final int AGRICULTURE_START = AGRICULTURE_LIGHT_START;
    public static final int AGRICULTURE_END   = AGRICULTURE_DENSE_END;
    
    public static final int FAB_LIGHT_START = 400;
    public static final int FAB_LIGHT_END   = 432;
    
    public static final int FAB_MEDIUM_START = 433;
    public static final int FAB_MEDIUM_END   = 465;
    
    public static final int FAB_DENSE_START = 466;
    public static final int FAB_DENSE_END   = 499;
    
    

    // For bridges and powerlines over water, so that we
    // don't have to compute this every time we draw.
    //public int altitudeCache = -1;
    
    public double tileProbablity[] = {
        0.99,
        0.4,
        0.3,
        0.2,
        0.1,
        0.08,
        0.07,
        0.055,
        
        0.05,
        0.048,
        0.046,
        0.043,
        0.038,
        0.033,
        0.030,
        0.028
            
    };

    public StructureInfo() {
        double random = Math.random();
        for( int i=tileProbablity.length-1; i >= 0; i-- ) {
            if ( tileProbablity[i] > random ) {
                id = i;
                return;
            }
        }
    }

    public boolean hasRoad() {
        return  (id >= 29 && id <= 43) ||
                (id >= 63 && id <= 70) ||
                (id >= 73 && id <= 89);
    }

    public void load(DataInputStream dis) throws IOException {
        id = dis.readShort();
        //altitudeCache = dis.readByte();
    }

    public void save(DataOutputStream dos) throws IOException {
        dos.writeShort(id);
        //dos.writeByte(altitudeCache);
    }
}

/*   NOTES:

Non-buildings:

00: Clear terrain (empty)
01-04: Rubble
05: Radioactive waste
06-0C: Trees (density increases as code increases)
0D: Small park (set XZON as for a 1x1 building)
0E-1C: Power lines (various directions, slopes)
   The difference X between the code and 0E, the first code, tells 
   what direction(s) and slope the power line takes.
   X (in hex)  Direction
   0           Left-right [for definition of directions, see note with ALTM]
   1           Top-bottom
   2           Top-bottom; slopes upwards towards top
   3           Left-right; slopes upwards towards right 
   4           Top-bottom; slopes upwards towards bottom
   5           Left-right; slopes upwards towards left
   6           From bottom side to right side
   7           Bottom to left
   8           Left to top
   9           Top to right
   A           T junction between top, right and bottom
   B           T between left, bottom and right
   C           T between top, left and bottom
   D           T between top, left and right
   E           Intersection connecting top, left, bottom, and right
1D-2B: Roads (various directions, slopes; same coding as for 0E-1C)
2C-3A: Rails (various directions, slopes; same coding as for 0E-1C)
3B-3E: More sloping rails.  These are used as preparation before ascending.
The 2C-3A rail codes are used on the actual sloping square.  This is why
rails don't look right when ascending a 1:1 grade.
  3B: Top-bottom; slopes upwards towards top
  3C: Left-right; slopes upwards towards right
  3D: Top-bottom; slopes upwards towards bottom
  3E: Left-right; slopes upwards towards left
3F-42: Tunnel entrances
  3F: Tunnel to the top
  40: Tunnel to the right
  41: Tunnel to the bottom
  42: Tunnel to the left
43-44: Crossovers (roads/power lines)
  43: Road left-right, power top-bottom
  44: Road top-bottom, power left-right
45-46: Crossovers (roads/rails)
  45: Road left-right, rails top-bottom
  46: Road top-bottom, rails left-right
47-48: Crossovers (rails/power lines)
  47: Rails left-right, power lines top-bottom
  48: Rails top-bottom, power lines left-right
49-4A: Highways (set XZON as for a 1x1 building)
  49: Highway left-right
  4A: Highway top-bottom
4B-4C: Crossovers (roads/highways; set XZON as for a 1x1 building)
  4B: Highway left-right, road top-bottom
  4C: Highway top-bottom, road left-right
4D-4E: Crossovers (rails/highways; set XZON as for a 1x1 building)
  4D: Highway left-right, rails top-bottom
  4E: Highway top-bottom, rails left-right
4F-50: Crossovers (highways/power lines; set XZON as for a 1x1 building)
  4F: Highway left-right, power lines top-bottom
  50: Highway top-bottom, power lines left-right
51-55: Suspension bridge pieces
56-59: Other road bridge pieces
5A-5B: Rail bridge pieces
5C: Elevated power lines
5D-60: Highway entrances (on-ramps)
  5D: Highway at top, road at left OR highway at right, road at bottom
  5E: H right, R top OR H top, R right
  5F: R right, H bottom OR H left, R top
  60: R left, H bottom OR H left, R bottom
61-69: Highways (various directions, slopes; 2x2 tiles; XZON should be set
                 as for a 2x2 building)
  61: Highway top-bottom, slopes up to the top
  62: Highway left-right, slopes up to the right
  63: Highway top-bottom, slopes up to the bottom
  64: Highway left-right, slopes up to the left
  65: Highway joining the bottom to the right
  66: Highway joining the bottom to the left
  67: Highway joining the left to the top
  68: Highway joining the top to the right
  69: Cloverleaf intersection connecting top, left, bottom and right
6A-6B: Highway bridges (2x2 tiles; set XZON as for a 2x2 building.)  This 
       is a reinforced bridge.  Use 49/4A for the `Hiway' bridge.
6C-6F: Sub/rail connections (set XZON as for a 1x1 building)
  6C: Sub/rail connection, rail at bottom
  6D: Sub/rail connection, rail at left
  6E: Sub/rail connection, rail at top
  6F: Sub/rail connection, rail at right

Buildings:

  Residential, 1x1:
    70-73: Lower-class homes
    74-77: Middle-class homes
    78-7B: Luxury homes
  Commercial, 1x1:
    7C: Gas station
    7D: Bed & breakfast inn
    7E: Convenience store
    7F: Gas station
    80: Small office building
    81: Office building
    82: Warehouse
    83: Cassidy's Toy Store
  Industrial, 1x1:
    84: Warehouse
    85: Chemical storage
    86: Warehouse
    87: Industrial substation
  Miscellaneous, 1x1:
    88-89: Construction
    8A-8B: Abandoned building
  Residential, 2x2:
    8C: Cheap apartments
    8D-8E: Apartments
    8F-90: Nice apartments
    91-93: Condominium
  Commercial, 2x2:
    94: Shopping center
    95: Grocery store
    96: Office building
    97: Resort hotel
    98: Office building
    99: Office / Retail
    9A-9D: Office building
  Industrial, 2x2:
    9E: Warehouse
    9F: Chemical processing
    A0-A5: Factory
  Miscellaneous, 2x2:
    A6-A9: Construction
    AA-AD: Abandoned building
  Residential, 3x3:
    AE-AF: Large apartment building
    B0-B1: Condominium
  Commercial, 3x3:
    B2: Office park
    B3: Office tower
    B4: Mini-mall
    B5: Theater square
    B6: Drive-in theater
    B7-B8: Office tower
    B9: Parking lot
    BA: Historic office building
    BB: Corporate headquarters
  Industrial, 3x3:
    BC: Chemical processing
    BD: Large factory
    BE: Industrial thingamajig
    BF: Factory
    C0: Large warehouse
    C1: Warehouse
  Miscellaneous, 3x3:
    C2-C3: Construction
    C4-C5: Abandoned building
  Power plants:
    C6-C7: Hydroelectric power (1x1)
    C8: Wind power (1x1)
    C9: Natural gas power plant (4x4)
    CA: Oil power plant (4x4)
    CB: Nuclear power plant (4x4)
    CC: Solar power plant (4x4)
    CD: Microwave power receiver (4x4)
    CE: Fusion power plant (4x4)
    CF: Coal power plant (4x4)
  City services:
    D0: City hall
    D1: Hospital
    D2: Police station
    D3: Fire station
    D4: Museum
    D5: Park (big)
    D6: School
    D7: Stadium
    D8: Prison
    D9: College
    DA: Zoo
    DB: Statue
  Seaports, airports, transportation, military bases, and more city services:
    DC: Water pump
    DD-DE: Runway
    DF: Pier
    E0: Crane
    E1-E2: Control tower
    E3: Warehouse (for seaport)
    E4-E5: Building (for airport)
    E6: Tarmac
    E7: F-15b
    E8: Hangar
    E9: Subway station
    EA: Radar
    EB: Water tower
    EC: Bus station
    ED: Rail station
    EE-EF: Parking lot
    F0: Loading bay
    F1: Top secret
    F2: Cargo yard
    F3: man (aka the mayor's house)
    F4: Water treatment plant
    F5: Library
    F6: Hangar
    F7: Church
    F8: Marina
    F9: Missile silo
    FA: Desalination plant
  Arcologies:
    FB: Plymouth arcology
    FC: Forest arcology
    FD: Darco arcology
    FE: Launch arcology
  Braun Llama-dome:
    FF: Braun Llama-dome

*/
