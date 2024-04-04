package org.example.Ostatne;

public class Identifikator
{
    private long curID;

    public Identifikator()
    {
        this.curID = 0;
    }

    public long getID()
    {
        return this.curID++;
    }
}
