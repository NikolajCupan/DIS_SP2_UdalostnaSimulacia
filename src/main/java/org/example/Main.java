package org.example;

import org.example.Simulacia.Jadro.SystemovaUdalost;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Udalosti.UdalostKomparator;
import org.example.Simulacia.System.Udalosti.UdalostPrichodZakaznika;

import java.util.PriorityQueue;

public class Main
{
    public static void main(String[] args)
    {
        PriorityQueue<Udalost> pq = new PriorityQueue<>(new UdalostKomparator());
        pq.add(new SystemovaUdalost(null, 0.0));
        pq.add(new UdalostPrichodZakaznika(null, 0.0, null));

        while (!pq.isEmpty())
        {
            Udalost u = pq.poll();
            System.out.println(u.getClass().getSimpleName());
        }
    }
}
