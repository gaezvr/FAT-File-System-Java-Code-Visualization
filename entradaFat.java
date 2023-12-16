package SIstemaFicheros;

public class entradaFat {
    private boolean disponible;
    private int siguienteCluster;
    private boolean fin;

    public entradaFat(boolean disponible, int siguienteCluster, boolean fin) {
        this.disponible = disponible;
        this.siguienteCluster = siguienteCluster;
        this.fin = fin;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public int getSiguienteCluster() {
        return siguienteCluster;
    }

    public boolean isFin() {
        return fin;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public void setSiguienteCluster(int siguienteCluster) {
        this.siguienteCluster = siguienteCluster;
    }

    public void setFin(boolean fin) {
        this.fin = fin;
    }
}
