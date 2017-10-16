package com.pastew.plague;

public class BaseGameEntity {
    //every entity has a unique identifying number
    public int ID;
    //this is the next valid ID. Each time a BaseGameEntity is instantiated
    //this value is updated
    static int nextValidID;

    //this is called within the constructor to make sure the ID is set
    //correctly. It verifies that the value passed to the method is greater
    //or equal to the next valid ID, before setting the ID and incrementing
    //the next valid ID
    public void setID(int val) {
        this.ID = val;
    }

    public BaseGameEntity(int id) {
        setID(id);
    }

    public BaseGameEntity() {
    }

    //all entities must implement an update function
    public void Update() {
    }

    int ID() {
        return ID;
    }
}