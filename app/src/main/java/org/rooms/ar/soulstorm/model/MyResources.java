package org.rooms.ar.soulstorm.model;

import java.util.HashMap;
import java.util.Map;

public class MyResources {
    private static final int BASIC_ENERGY = 1100;
    private float energy;
    private long force;
    private Map<String, Integer> items = new HashMap<>();
    private long dateTime;

    public MyResources(){ }

    public MyResources(boolean defaultEnergy){
        energy = defaultEnergy? BASIC_ENERGY : 0;
        dateTime = System.currentTimeMillis();
    }

    private MyResources(float energy, long force, Map<String, Integer> items, long dateTime) {
        this.energy = energy;
        this.force = force;
        this.items = items;
        this.dateTime = dateTime;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public long getForce() {
        return force;
    }

    public void setForce(long force) {
        this.force = force;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public Map<String, Integer> getItems() {
        return items;
    }


    public void addBuilding(Building item) {
        items.computeIfAbsent(item.name(), building -> items.getOrDefault(building,0)+1);
    }

    public void removeBuilding(Building item) {
        items.computeIfAbsent(item.name(), building -> items.getOrDefault(building,0)-1);
    }

    public MyResources increase() {
        for (String item : items.keySet()) {
            energy += Building.valueOf(item).getEnergyBoost() * items.get(item);
            force += Building.valueOf(item).getBattleBoost() * items.get(item);
        }
        energy += 0.1;
        return new MyResources(Math.round(energy*10)/10f, force, items, dateTime);
    }

}
