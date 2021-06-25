package com.example.finalapplication.speedmeter;

import android.location.Location;

public class Clocation extends Location {

    private boolean bUseMeterunits=false;

    public Clocation(Location location) {

        this(location,true);
    }
    public  Clocation(Location location,boolean bUseMeterunits){
        super(location);
        this.bUseMeterunits=bUseMeterunits;
    }

    public  boolean getUseMeterUnits(){
        return this.bUseMeterunits;
    }

    public  void setbUseMeterunits(boolean bUseMeterunits){
        this.bUseMeterunits=bUseMeterunits;
    }


    @Override
    public float distanceTo(Location dest) {
       float nDistance=super.distanceTo(dest);
       if(!this.getUseMeterUnits()){
           //Convert meter to feet
           nDistance=nDistance*3.28083989501312f;
       }
       return nDistance;
    }

    @Override
    public double getAltitude() {
        double nAltitude=super.getAltitude();
        if(!this.getUseMeterUnits()){
            //Convert meter to feet
            nAltitude=nAltitude*3.28083989501312d;
        }
        return nAltitude;
    }


    @Override
    public float getSpeed() {
        float nspeed=super.getSpeed()*3.6f;
        if(!this.getUseMeterUnits()){
            //Convert meter/second to miles/hour
            nspeed=super.getSpeed() * 2.23693629f;
        }
        return nspeed;
    }

    @Override
    public float getAccuracy() {
        float nAccuracy=super.getAccuracy();
        if(!this.getUseMeterUnits()){
            //Convert meter to feet
            nAccuracy=nAccuracy*3.28083989501312f;
            //these small f or d represent data type float or double
        }
        return nAccuracy;
    }
}
