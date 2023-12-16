package SIstemaFicheros;

public class directorioRaiz extends Directorio {
    
    public directorioRaiz()
    {
        super();
        nombre = getNombreRaiz();
    }

    public final static String getNombreRaiz()
    {
        return "C:";
    }
}
