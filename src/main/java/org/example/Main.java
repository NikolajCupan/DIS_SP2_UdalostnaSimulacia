package org.example;

import org.example.Simulacia.System.Agenti.Agent;

import java.util.LinkedList;
import java.util.Queue;

public class Main
{
    public static void main(String[] args)
    {
        Queue<Agent> q = new LinkedList<>();

        Agent a1 = new Agent(1, null);
        Agent a2 = new Agent(2, null);
        Agent a3 = new Agent(3, null);
        Agent a4 = new Agent(4, null);
        Agent a5 = new Agent(5, null);
        q.add(a1);
        q.add(a5);
        q.add(a3);
        q.add(a2);
        q.add(a4);

        boolean r1 = q.remove(a3);
        Agent p2 = q.poll();

        int x = 100;
    }
}
