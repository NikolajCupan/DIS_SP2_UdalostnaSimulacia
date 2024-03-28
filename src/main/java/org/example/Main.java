package org.example;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.System.SimulaciaSystem;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        SimulaciaSystem s = new SimulaciaSystem(10000, Konstanty.KONIEC_SEKUND - Konstanty.ZACIATOK_SEKUND,
        3, 0, false);
        s.simuluj();
    }
}
