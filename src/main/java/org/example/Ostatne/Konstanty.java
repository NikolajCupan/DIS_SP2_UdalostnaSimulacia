package org.example.Ostatne;

public class Konstanty
{
    public static final double EPSILON = 0.000001;
    public static final double ODSADENIE_HRANICNE_HODNOTY = 0.1;

    // Od 9:00 do 17:30 v sekundach
    public static final double ZACIATOK_SEKUND = 9 * 60 * 60;
    public static final double KONIEC_SEKUND = 17.50 * 60 * 60;

    public static final boolean DEBUG_VYPIS_AGENT = true;
    public static final boolean DEBUG_VYPIS_UDALOST = true;
    public static final int DLZKA_PAUZY_MS = 1000;

    // Intervaly spolahlivosti
    public static final double KVANTIL_90_PERCENT = 1.64485362695147;
    public static final double KVANTIL_95_PERCENT = 1.95996398454005;
    public static final double KVANTIL_99_PERCENT = 2.5758293035489;

    // Obsluha
    public static final int KAPACITA_FRONT_OKNO = 3;
}
