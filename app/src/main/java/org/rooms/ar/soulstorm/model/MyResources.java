package org.rooms.ar.soulstorm.model;

import java.util.HashMap;
import java.util.Map;

public class MyResources {
    private static final byte BASIC_ENERGY = 100;
    private long energy;
    private long force;
    private Map<String, Integer> items;

    public MyResources(){
        energy = BASIC_ENERGY;
    }

    public MyResources(long energy, long force) {
        this.energy = energy;
        this.force = force;
        items = new HashMap<>();
    }

    public long getEnergy() {
        return energy;
    }

    public void setEnergy(long energy) {
        this.energy = energy;
    }

    public long getForce() {
        return force;
    }

    public void setForce(long force) {
        this.force = force;
    }

    public void addBuilding(Building item) {
        items.computeIfAbsent(item.name(), building -> items.getOrDefault(building,0)+1);
    }
}
