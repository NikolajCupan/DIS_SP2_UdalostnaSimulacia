package org.example.Simulacia.System.Udalosti;

import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Udalosti.Automat.UdalostKoniecObsluhyAutomat;
import org.example.Simulacia.System.Udalosti.Automat.UdalostZaciatokObsluhyAutomat;
import org.example.Simulacia.System.Udalosti.Okno.UdalostKoniecObsluhyOkno;
import org.example.Simulacia.System.Udalosti.Okno.UdalostZaciatokObsluhyOkno;
import org.example.Simulacia.System.Udalosti.Pokladna.UdalostKoniecObsluhyPokladna;
import org.example.Simulacia.System.Udalosti.Pokladna.UdalostZaciatokObsluhyPokladna;
import org.example.Simulacia.System.Udalosti.Vyzdvihnutie.UdalostKoniecVyzdvihnutia;
import org.example.Simulacia.System.Udalosti.Vyzdvihnutie.UdalostZaciatokVyzdvihnutia;

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
        if (udalost1 instanceof UdalostZaciatokObsluhyAutomat
            && udalost2 instanceof UdalostKoniecObsluhyAutomat)
        {
            throw new RuntimeException("Udalost zaciatok obsluhy automat nemoze mat rovnaky cas vykonania"
                + "ako udalost koniec obsluhy automat!");
        }

        if (udalost1 instanceof UdalostZaciatokObsluhyOkno
            && udalost2 instanceof UdalostKoniecObsluhyOkno)
        {
            if (udalost1.getAgent().getID() == udalost2.getAgent().getID())
            {
                throw new RuntimeException("Udalost zaciatok obsluhy okno nemoze mat rovnaky cas vykonania"
                    + "ako udalost koniec obsluhy okno pre rovnakeho agenta!");
            }
        }

        if (udalost1 instanceof UdalostZaciatokObsluhyPokladna zaciatokPokladna1
            && udalost2 instanceof UdalostZaciatokObsluhyPokladna zaciatokPokladna2)
        {
            if (zaciatokPokladna1.getPokladna() == zaciatokPokladna2.getPokladna())
            {
                throw new RuntimeException("Boli naplanovane 2 udalosti obsluhy pri rovnakej pokladni v rovnaky cas!");
            }
        }

        if (udalost1 instanceof UdalostZaciatokObsluhyPokladna
            && udalost2 instanceof UdalostKoniecObsluhyPokladna)
        {
            if (udalost1.getAgent().getID() == udalost2.getAgent().getID())
            {
                throw new RuntimeException("Udalost zaciatok obsluhy pokladna nemoze mat rovnaky cas vykonania"
                    + "ako udalost koniec obsluhy pokladna pre rovnakeho agenta!");
            }
        }

        if (udalost1 instanceof UdalostZaciatokVyzdvihnutia
            && udalost2 instanceof UdalostKoniecVyzdvihnutia)
        {
            if (udalost1.getAgent().getID() == udalost2.getAgent().getID())
            {
                throw new RuntimeException("Udalost zaciatok vyzdvihnutia nemoze mat rovnaky cas vykonania"
                    + "ako udalost koniec vyzdvihnutia pre rovnakeho agenta!");
            }
        }
    }
}
