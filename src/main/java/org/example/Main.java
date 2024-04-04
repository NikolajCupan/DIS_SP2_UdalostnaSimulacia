package org.example;

import org.example.Simulacia.Statistiky.SpojitaStatistika;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        SpojitaStatistika s = new SpojitaStatistika();
        s.pridajHodnotu(0, 10);
        s.pridajHodnotu(5, 5);
        System.out.println(s.getPriemer(10, 5));
    }
}
