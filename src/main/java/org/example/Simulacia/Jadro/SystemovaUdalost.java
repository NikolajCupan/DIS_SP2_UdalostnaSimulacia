package org.example.Simulacia.Jadro;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.System.Agenti.Agent;

public class SystemovaUdalost extends Udalost
{
    private static final Agent DUMMY_AGENT = new Agent(-1, null);

    public SystemovaUdalost(SimulacneJadro simulacneJadro, double casVykonania)
    {
        super(simulacneJadro, casVykonania, SystemovaUdalost.DUMMY_AGENT);
    }

    @Override
    public void vykonajUdalost()
    {
        try
        {
            Thread.sleep(Konstanty.DLZKA_PAUZY_SYSTEMOVA_UDALOST_MS);
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Pri uspavani simulacie nastala chyba!");
        }

        SimulacneJadro simulacneJadro = this.getSimulacneJadro();

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
