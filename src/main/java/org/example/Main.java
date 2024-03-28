package org.example;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.System.SimulaciaSystem;

public class Main
{
    public static void main(String[] args)
    {
        SimulaciaSystem s = new SimulaciaSystem(10_000, Konstanty.OTVARACIA_DOBA_SEKUND,
        3, 0, false);
        s.simuluj();
    }
}
