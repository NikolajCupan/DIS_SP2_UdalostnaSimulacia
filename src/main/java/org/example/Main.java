package org.example;

import org.example.Testovanie.TestGeneratory;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        TestGeneratory t = new TestGeneratory(123, true);
        t.testSpojityEmpirickyGenerator();
        t.testSpojityTrojuholnikovyGenerator();
        t.testSpojityExponencialnyGenerator();
    }
}
