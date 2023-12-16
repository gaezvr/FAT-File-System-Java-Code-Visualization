package SIstemaFicheros;

public class Archivo extends Cluster{
    private String contenido;
    private String nombre;
    private int tamanoEnClusters;

    public Archivo(String nombre, String contenido, int tamanoEnClusters) {
        this.nombre = nombre;
        this.contenido = contenido;
        this.tamanoEnClusters = tamanoEnClusters;
    }

    public String getContenido() {
        return contenido;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTamanoEnClusters() {
        return tamanoEnClusters;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTamanoEnClusters(int tamanoEnClusters) {
        this.tamanoEnClusters = tamanoEnClusters;
    }
}
