package SIstemaFicheros;

import java.util.ArrayList;

public class Directorio {
    public ArrayList<entradaDirectorio> entradasDirectorio;
    protected String nombre;

    public Directorio()
    {
        entradasDirectorio = new ArrayList<entradaDirectorio>();
    }

    public String getNombre() 
    {
        return nombre;
    }

    public void setNombre(String nombre) 
    {
        this.nombre = nombre;
    }
}
