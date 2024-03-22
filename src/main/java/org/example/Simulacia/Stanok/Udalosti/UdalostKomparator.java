package org.example.Simulacia.Stanok.Udalosti;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.Udalost;

import java.util.Comparator;

public class UdalostKomparator implements Comparator<Udalost>
{
    @Override
    public int compare(Udalost udalost1, Udalost udalost2)
    {
        if (Math.abs(udalost1.getCasVykonania() - udalost2.getCasVykonania()) < Konstanty.EPSILON)
        {
            return 0;
        }
        else if (udalost1.getCasVykonania() < udalost2.getCasVykonania())
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }
}
