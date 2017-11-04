package com.pastew.plague;

public class Column extends BaseGameEntity {

    Column(float x, float y) {
        super();
        position = new Vector2D(x, y);
        color = GameColors.columnColor;
        size = 50;
    }

}
