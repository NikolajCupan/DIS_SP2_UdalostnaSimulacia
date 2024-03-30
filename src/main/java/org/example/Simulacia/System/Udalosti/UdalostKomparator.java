package org.example.Simulacia.System.Udalosti;

import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Udalosti.Automat.UdalostKoniecObsluhyAutomat;
import org.example.Simulacia.System.Udalosti.Automat.UdalostZaciatokObsluhyAutomat;

import java.util.Comparator;

public class UdalostKomparator implements Comparator<Udalost>
{
    @Override
    public int compare(Udalost udalost1, Udalost udalost2)
    {
        if (Double.compare(udalost1.getCasVykonania(), udalost2.getCasVykonania()) == 0)
        {
            // Udalosti maju rovnaky cas vykonania, porovnanie na zaklade ich priorit
            this.validujStav(udalost1, udalost2);
            return Integer.compare(udalost1.getPriorita(), udalost2.getPriorita());
        }
        else
        {
            return Double.compare(udalost1.getCasVykonania(), udalost2.getCasVykonania());
        }
    }

    private void validujStav(Udalost udalost1, Udalost udalost2)
    {
        if (udalost1 instanceof UdalostZaciatokObsluhyAutomat && udalost2 instanceof UdalostKoniecObsluhyAutomat)
        {
            throw new RuntimeException("Udalost zaciatok obsluhy automat nemoze mat rovnaky cas vykonania ako udalost koniec obsluhy automat!");
        }
    }
}
