package org.example;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.System.SimulaciaSystem;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        SimulaciaSystem s = new SimulaciaSystem(1, Konstanty.OTVRACIA_DOBA_SEKUND, 3, 0, true);
        s.simuluj();
    }
}
