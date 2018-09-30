package org.rooms.ar.soulstorm.model;

public class Resourses {
    private long energy;
    private long force;

    public Resourses(){}

    public Resourses(long energy, long force) {
        this.energy = energy;
        this.force = force;
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
}
