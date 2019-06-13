package com.example.prajwal.smarthelmet1;

/**
 * Created by Prajwal on 26-09-2017.
 */

public class Helmets {
    private String name;
    private String mac;
    private boolean type;
    private  static final boolean ADD_HELMETS = true;
    private  static final boolean MY_HELMETS = false;


    public Helmets(String name, String mac,boolean type)
    {
        this.name=name;
        this.mac=mac;
        this.type=type;
    }



    public String getName() {
        return name;
    }

    public String getMAC() {
        return mac;
    }

    public boolean getType() {
        return type;
    }

    public void setName(String name){ this.name = name;}

    public void setMAC (String mac){ this.mac = mac;}

    public void setType(boolean type){ this.type = type;}

}
