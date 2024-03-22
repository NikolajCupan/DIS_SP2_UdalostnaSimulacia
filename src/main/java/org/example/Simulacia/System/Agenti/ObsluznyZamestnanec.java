package org.example.Simulacia.System.Agenti;

public class ObsluznyZamestnanec
{
    private boolean obsadeny;

    public ObsluznyZamestnanec()
    {
        this.obsadeny = false;
    }

    public boolean getObsadeny()
    {
        return this.obsadeny;
    }

    public void setObsadeny(boolean obsadeny)
    {
        this.obsadeny = obsadeny;
    }
}
