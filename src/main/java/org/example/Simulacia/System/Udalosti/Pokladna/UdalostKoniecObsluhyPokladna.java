package org.example.Simulacia.System.Udalosti.Pokladna;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.Agenti.Pokladna;
import org.example.Simulacia.System.SimulaciaSystem;
import org.example.Simulacia.System.Udalosti.Vyzdvihnutie.UdalostZaciatokVyzdvihnutia;

public class UdalostKoniecObsluhyPokladna extends Udalost
{
    private final Pokladna pokladna;

    public UdalostKoniecObsluhyPokladna(SimulacneJadro simulacneJadro, double casVykonania, Agent agent, Pokladna pokladna)
    {
        super(simulacneJadro, casVykonania, agent);

        this.pokladna = pokladna;
    }

    private void vypis()
    {
        if (Konstanty.DEBUG_VYPIS_UDALOST)
        {
            System.out.print("[UDALOST ");
            System.out.format("%6s", this.getAgent().getID());
            System.out.print("]   ");
            System.out.format("%-35s", "Koniec obsluhy pokladna");
            System.out.println(this.getCasVykonania());
        }
    }

    @Override
    public void vykonajUdalost()
    {
        this.vypis();
        SimulaciaSystem simulacia = (SimulaciaSystem)this.getSimulacneJadro();
        Agent vykonavajuciAgent = this.getAgent();


        // Kontrola stavu simulacie
        if (!this.pokladna.getObsadena())
        {
            throw new RuntimeException("Pokladna obsahuje agenta, hoci ma nastavene, ze nikoho neobsahuje!");
        }


        // Zmena stavu simulacie
        this.pokladna.setObsadena(false);


        // Nastavenia atributov agenta, ktory udalost vykonava
        vykonavajuciAgent.setCasKoniecObsluhyPokladna(this.getCasVykonania());


        // Pokus o naplanovanie vyzdvihnutia tovaru
        if (vykonavajuciAgent.getOdlozenyTovar())
        {
            if (vykonavajuciAgent.getOdlozenyTovarOkno() == null)
            {
                throw new RuntimeException("Okno, pri ktorom agent nechal tovar je null!");
            }

            // Agent ma odlozeny tovar pri okne, naplanuj jeho vyzdvihnutie
            UdalostZaciatokVyzdvihnutia zaciatokVyzdvihnutia =
                new UdalostZaciatokVyzdvihnutia(simulacia, this.getCasVykonania(), vykonavajuciAgent);
            simulacia.naplanujUdalost(zaciatokVyzdvihnutia);
        }


        // Pokus o naplanovanie zaciatku obsluhy dalsieho agenta pri pokladni
        if (this.pokladna.getPocetFront() == 0)
        {
            // Front pred pokladnou je prazdny, nie je mozne naplanovat obsluhu dalsieho agenta
        }
        else
        {
            Agent odobratyAgent = this.pokladna.odoberZFrontu();

            UdalostZaciatokObsluhyPokladna zaciatokObsluhyPokladna =
                new UdalostZaciatokObsluhyPokladna(simulacia, this.getCasVykonania(), odobratyAgent, this.pokladna);
            simulacia.naplanujUdalost(zaciatokObsluhyPokladna);
        }


        // Statistiky
        // Pridaj iba za predpokladu, ze agent nema odlozeny tovar
        if (!vykonavajuciAgent.getOdlozenyTovar())
        {
            simulacia.getStatistikaCasSystem().pridajHodnotu(
            vykonavajuciAgent.getCasKoniecObsluhyPokladna() - vykonavajuciAgent.getCasPrichodSystem());
        }
    }
}
