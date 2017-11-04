
package com.pastew.plague;

import com.badlogic.gdx.graphics.Color;

public class Column extends BaseGameEntity{
    
    
    Column(float x, float y) {
        super();
        position = new Vector2D(x,y);     
        color = Color.GREEN;
        size = 50; 
    }
    
}
