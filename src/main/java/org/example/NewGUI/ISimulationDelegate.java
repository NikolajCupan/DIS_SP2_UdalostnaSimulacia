package org.example.NewGUI;

import org.example.Simulacia.Jadro.SimulacneJadro;

public interface ISimulationDelegate
{
    void aktualizujSa(SimulacneJadro simulacneJadro, boolean celkoveStatistiky, boolean priebezneStatistiky);
    void aktualizujSimulacnyCas(SimulacneJadro simulacneJadro);
}
