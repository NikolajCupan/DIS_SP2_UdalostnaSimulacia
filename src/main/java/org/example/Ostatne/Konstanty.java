package org.example.Ostatne;

public class Konstanty
{
    public static final double EPSILON = 0.000001;
    public static final double ODSADENIE_HRANICNE_HODNOTY = 0.1;

    // Otvaracia doba od 9:00 do 17:00
    private static final double ZACIATOK_SEKUND = 9 * 60 * 60;
    private static final double KONIEC_SEKUND = 17 * 60 * 60;
    public static final double OTVARACIA_DOBA_SEKUND = Konstanty.KONIEC_SEKUND - Konstanty.ZACIATOK_SEKUND;


    // Debug
    public static final boolean DEBUG_VYPIS_AGENT = false;
    public static final boolean DEBUG_VYPIS_UDALOST = false;
    public static final boolean STATISTIKY_ZOZNAM_DAT = true;


    public static final int DLZKA_PAUZY_MS = 1000;
    public static final int DLZKA_PAUZY_SYSTEMOVA_UDALOST_MS = 1000;

    public static final int DEFAULT_RYCHLOST = 100;
    public static final int MAX_RYCHLOST = 1000;


    // Intervaly spolahlivosti
    public static final double KVANTIL_90_PERCENT = 1.64485362695147;
    public static final double KVANTIL_95_PERCENT = 1.95996398454005;
    public static final double KVANTIL_99_PERCENT = 2.5758293035489;


    // Obsluha okno
    public static final int KAPACITA_FRONT_OKNO = 9;


    // Priority udalosti,
    // mensia hodnota priority => vacsia priorita
    public static final int PRIORITA_SYSTEMOVA_UDALOST         = 0;
    public static final int PRIORITA_PRICHOD_ZAKAZNIKA         = Integer.MAX_VALUE;
    public static final int PRIORITA_ZACIATOK_OBSLUHY_AUTOMAT  = Integer.MAX_VALUE;
    public static final int PRIORITA_KONIEC_OBSLUHY_AUTOMAT    = Integer.MAX_VALUE;
    public static final int PRIORITA_ZACIATOK_OBSLUHY_OKNO     = Integer.MAX_VALUE;
    public static final int PRIORITA_KONIEC_OBSLUHY_OKNO       = Integer.MAX_VALUE;
    public static final int PRIORITA_ZACIATOK_OBSLUHY_POKLADNA = Integer.MAX_VALUE;
    public static final int PRIORITA_KONIEC_OBSLUHY_POKLADNA   = Integer.MAX_VALUE;
    public static final int PRIORITA_ZACIATOK_VYZDVIHNUTIA     = Integer.MAX_VALUE;
    public static final int PRIORITA_KONIEC_VYZDVIHNUTIA       = Integer.MAX_VALUE;
}
