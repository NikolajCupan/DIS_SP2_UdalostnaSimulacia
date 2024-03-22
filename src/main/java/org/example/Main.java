package org.example;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.System.SimulaciaSystem;

public class Main
{
    public static void main(String[] args)
    {
        SimulaciaSystem s = new SimulaciaSystem(1, Konstanty.KONIEC_SEKUND - Konstanty.ZACIATOK_SEKUND,
        3, 3, true);
        s.simuluj();
    }
}
