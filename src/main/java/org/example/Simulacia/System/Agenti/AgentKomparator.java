package org.example.Simulacia.System.Agenti;

import java.util.Comparator;

public class AgentKomparator implements Comparator<Agent>
{
    @Override
    public int compare(Agent agent1, Agent agent2)
    {
        return Long.compare(agent2.getID(), agent1.getID());
    }
}
