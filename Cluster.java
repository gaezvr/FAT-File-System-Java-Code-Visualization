package SIstemaFicheros;

public class Cluster {
    public static final int cantidadClusters = 20;
    private Object dato; // Sera el contenido del cluster es decir el archivo o directorio TAL CUAL

    public Cluster()
    {
        dato = null;
    }

    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }
}
