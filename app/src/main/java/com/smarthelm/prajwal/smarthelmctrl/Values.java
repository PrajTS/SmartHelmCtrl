package com.smarthelm.prajwal.smarthelmctrl;

/**
 * Created by Prajwal on 30-09-2017.
 */

public class Values {

    public boolean accident;
    public boolean status;

    public Values()
    {}

    public Values (boolean accident, boolean status)
    {
        this.status=status;
        this.accident=accident;
    }

    public void setStatus(boolean status)
    {
        this.status=status;
    }

    public void setAccident(boolean accident)
    {
        this.accident=accident;
    }
    public boolean getStatus()
    {
        return status;
    }
    public boolean getAccident()
    {
        return accident;
    }
}
