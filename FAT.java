package SIstemaFicheros;

public class FAT {
    public entradaFat[] entradasFAT = new entradaFat[Cluster.cantidadClusters];

    public FAT() {
        entradasFAT[0] = new entradaFat(false,-1,true);
        for (int i = 1; i < entradasFAT.length; i++) {
            entradasFAT[i] = new entradaFat(true,-1,false);
        }
    }

    public int getClusterDisponible()
    {
        for (int i = 0; i < entradasFAT.length; i++) {
            if (entradasFAT[i].isDisponible()) {
                return i;
            }
        }
        return -1;
    }
    
    public void mostrarCOntenidoFAT()
    {
        for (int i = 0; i < entradasFAT.length; i++) 
        {
            // Mostar siguiente si tiene cual y si fin y si dispo y numero de cluster
            int n = entradasFAT[i].getSiguienteCluster() + 1;
            System.out.println("\nContenido FAT de Cluster " + (i+1) + " : " + (entradasFAT[i].getSiguienteCluster() == -1 ? "No tiene siguiente cluster" : " Siguiente Cluster : "+ n) + " " + (entradasFAT[i].isFin() ? " Este cluster es el final para este archivo o directorio " : " ") + ( entradasFAT[i].isDisponible() ? " Esta disponible" : " No esta disponible"));
        }
    }
}
