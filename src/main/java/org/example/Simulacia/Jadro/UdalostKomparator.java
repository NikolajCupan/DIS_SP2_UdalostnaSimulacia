package org.example.Simulacia.Jadro;

import java.util.Comparator;

public class UdalostKomparator implements Comparator<Udalost>
{
    @Override
    public int compare(Udalost udalost1, Udalost udalost2)
    {
        return Double.compare(udalost1.getCasVykonania(), udalost2.getCasVykonania());
    }
}
