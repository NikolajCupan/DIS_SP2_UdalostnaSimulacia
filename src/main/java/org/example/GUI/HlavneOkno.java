package org.example.GUI;

import javax.swing.*;

public class HlavneOkno extends JFrame
{
    private JPanel panel;

    public HlavneOkno()
    {
        setTitle("Aplikácia - Nikolaj Cupan");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1550, 750);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(this.panel);
    }

    public void createUIComponents()
    {
    }
}
