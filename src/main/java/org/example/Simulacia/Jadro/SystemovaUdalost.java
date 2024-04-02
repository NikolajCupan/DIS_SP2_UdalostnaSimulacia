package org.example.Simulacia.Jadro;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.System.Agenti.Zakaznik.Agent;

public class SystemovaUdalost extends Udalost
{
    private static final Agent DUMMY_AGENT = new Agent(-1, null);

    public SystemovaUdalost(SimulacneJadro simulacneJadro, double casVykonania)
    {
        super(simulacneJadro, casVykonania, SystemovaUdalost.DUMMY_AGENT, Konstanty.PRIORITA_SYSTEMOVA_UDALOST);
    }

    @Override
    public void vykonajUdalost()
    {
        SimulacneJadro simulacneJadro = this.getSimulacneJadro();

        try
        {
            simulacneJadro.aktualizujSimulacnyCasGUI();
            Thread.sleep(Konstanty.DLZKA_PAUZY_SYSTEMOVA_UDALOST_MS);
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Pri uspavani simulacie nastala chyba!");
        }

        double rychlost = simulacneJadro.getRychlost();
        if (rychlost >= Konstanty.MAX_RYCHLOST)
        {
            // Real time, neplanuj systemove eventy
        }
        else
        {
            double casVykonania = this.getCasVykonania() + rychlost;

            SystemovaUdalost systemovaUdalost = new SystemovaUdalost(simulacneJadro, casVykonania);
            simulacneJadro.naplanujUdalost(systemovaUdalost);
        }
    }
}
