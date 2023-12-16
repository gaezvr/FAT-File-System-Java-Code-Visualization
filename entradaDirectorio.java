package SIstemaFicheros;

public class entradaDirectorio {
    private String nombre;
    private boolean esDirectorio;
    private int clusterInicio;

    public entradaDirectorio(String nombre, boolean esDirectorio, int clusterInicio) {
        this.nombre = nombre;
        this.esDirectorio = esDirectorio;
        this.clusterInicio = clusterInicio;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isEsDirectorio() {
        return esDirectorio;
    }

    public int getClusterInicio() {
        return clusterInicio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEsDirectorio(boolean esDirectorio) {
        this.esDirectorio = esDirectorio;
    }

    public void setClusterInicio(int clusterInicio) {
        this.clusterInicio = clusterInicio;
    }
}
