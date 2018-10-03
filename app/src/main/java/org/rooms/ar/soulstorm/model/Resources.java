package org.rooms.ar.soulstorm.model;

import java.util.HashMap;
import java.util.Map;

public class Resources {
    private long energy;
    private long force;
    private Map<Building, Integer> mMap;

    public Resources(){}

    public Resources(long energy, long force) {
        this.energy = energy;
        this.force = force;
        mMap = new HashMap<>();
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
        mMap.computeIfAbsent(item, building -> mMap.get(building)+1);
    }
}
