package org.rooms.ar.soulstorm.model;

import android.support.annotation.StringRes;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyResources {
    private static final byte BASIC_ENERGY = 100;
    private long energy;
    private long force;
    private Map<String, Integer> items;
    private Date dateTime;

    public MyResources(){
        energy = BASIC_ENERGY;
        items = new HashMap<>();
        dateTime = new Date(System.currentTimeMillis());
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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Map<String, Integer> getItems() {
        return items;
    }


    public void addBuilding(Building item) {
        items.computeIfAbsent(item.name(), building -> items.getOrDefault(building,0)+1);
    }

    public long increase() {
        for (String item : items.keySet()) {
            energy += Building.valueOf(item).getEnergyBoost() * items.get(item);
        }
        return energy;
    }
}
