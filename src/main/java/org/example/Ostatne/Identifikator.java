package org.example.Ostatne;

public class Identifikator
{
    private static long curID = 0;

    public static synchronized long getID()
    {
        return Identifikator.curID++;
    }

    public static synchronized void resetID()
    {
        Identifikator.curID = 0;
    }
}
