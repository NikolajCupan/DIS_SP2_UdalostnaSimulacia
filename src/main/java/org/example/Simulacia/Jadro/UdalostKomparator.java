package org.example.Simulacia.Jadro;

import java.util.Comparator;

public class UdalostKomparator implements Comparator<Udalost>
{
    @Override
    public int compare(Udalost udalost1, Udalost udalost2)
    {
        if (Double.compare(udalost1.getCasVykonania(), udalost2.getCasVykonania()) == 0)
        {
            // Udalosti maju rovnaky cas vykonania, porovnanie na zaklade ich priorit
            return Integer.compare(udalost1.getPriorita(), udalost2.getPriorita());
        }
        else
        {
            return Double.compare(udalost1.getCasVykonania(), udalost2.getCasVykonania());
        }
    }
}
