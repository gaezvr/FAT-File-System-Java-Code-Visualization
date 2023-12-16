package SIstemaFicheros;

import java.util.ArrayList;

public abstract class sistemaFicherosFAT {
    private static directorioRaiz directorioRaiz;
    private static FAT FAT;
    private static Cluster[] clusters = new Cluster[Cluster.cantidadClusters];

    public static void inicializarClusters() {
        directorioRaiz = new directorioRaiz() {
        };
        FAT = new FAT();
        for (int i = 0; i < clusters.length; i++) {
            clusters[i] = new Cluster();
        }

    }

    public static void crearArchivo() {
        Archivo archivo = new Archivo(pedirNombre(), pedirContenido(), pedirTamano());
        Directorio Directorio = new Directorio();
        Directorio.setNombre(pedirDirectorio());

        // Comprobar si hay espacio en el sistema de archivos
        int size = 0;
        for (entradaFat o : FAT.entradasFAT) {
            size += (o.isDisponible()) ? 1 : 0;
        }
        if (size < archivo.getTamanoEnClusters()) {
            System.out.println("No hay espacio en el sistema de archivos o no estan disponibles los clusters");
            return;
        }

        Directorio = buscarDirectorio(Directorio.getNombre());
        if (Directorio == null) {
            System.out.println("El directorio no existe");
            return;
        }
        for (int i = 0; i < Directorio.entradasDirectorio.size(); i++) {
            if (Directorio.entradasDirectorio.get(i).getNombre().equals(archivo.getNombre())
                    && Directorio.entradasDirectorio.get(i).isEsDirectorio() == false) {
                System.out.println("El archivo ya existe");
                return;
            }
        }

        // Añadir entrada al directorio / directorio raiz
        Directorio.entradasDirectorio
                .add(new entradaDirectorio(archivo.getNombre(), false, FAT.getClusterDisponible()));

        // Tema de metadatos y todo eso
        int tam = archivo.getTamanoEnClusters();
        for (int i = FAT.getClusterDisponible(); i < Cluster.cantidadClusters; i++) {
            if (FAT.entradasFAT[i].isDisponible() && tam > 0) {
                clusters[i].setDato(archivo);
                FAT.entradasFAT[i].setDisponible(false);
                if (tam != 1) {
                    FAT.entradasFAT[i].setSiguienteCluster(i + 1);
                } else {
                    FAT.entradasFAT[i].setFin(true);
                }
                tam--;
            }
        }
    }

    public static void crearDirectorio() {
        // Comprobar si hay espacio en el sistema de archivos
        int size = 0;
        for (entradaFat o : FAT.entradasFAT) {
            size += (o.isDisponible()) ? 1 : 0;
        }
        if (size == 0) {
            System.out.println("No hay espacio en el sistema de archivos o no estan disponibles los clusters");
            return;
        }

        Directorio Nuevodirectorio = new Directorio();
        Nuevodirectorio.setNombre(pedirDirectorio());
        if (Nuevodirectorio.getNombre().equals(SIstemaFicheros.directorioRaiz.getNombreRaiz())) {
            System.out.println("El directorio ya existe");
            return;
        }

        System.out.println("El directorio se creara en el directorio que pongas a continuacion");
        Directorio directorioObjetivo = new Directorio();
        directorioObjetivo.setNombre(pedirDirectorio());

        directorioObjetivo = buscarDirectorio(directorioObjetivo.getNombre());
        if (directorioObjetivo == null) {
            System.out.println("El directorio no existe");
            return;
        }

        for (int i = 0; i < directorioObjetivo.entradasDirectorio.size(); i++) {
            if (directorioObjetivo.entradasDirectorio.get(i).getNombre().equals(Nuevodirectorio.getNombre())
                    && directorioObjetivo.entradasDirectorio.get(i).isEsDirectorio() == true) {
                System.out.println("El directorio ya existe");
                return;
            }
        }

        // Añadir entrada al directorio / directorio raiz
        directorioObjetivo.entradasDirectorio
                .add(new entradaDirectorio(Nuevodirectorio.getNombre(), true, FAT.getClusterDisponible()));

        // Tema de metadatos y todo eso
        int n = FAT.getClusterDisponible();

        clusters[n].setDato(Nuevodirectorio);
        FAT.entradasFAT[n].setDisponible(false);
        FAT.entradasFAT[n].setFin(true);
    }

    /**
     * Muestra el contenido del sistema de ficheros
     * ten en cuenta que esto mostarara ABSOLUTAMENTE todo lo que hay en el sistema
     * de ficheros y los metadatos restantes
     * de los clusters
     */
    public static void mostrarContenidoSistema() {
        // Mostrar contenido del directorio raiz
        System.out.println("Cluster 1 : contiene a directorio C:");
        mostrarContenidoDirectorio(directorioRaiz);
        int i = 0;
        ArrayList<String> nombres = new ArrayList<String>();

        for (Cluster c : clusters) {
            i++;
            if (c != null && i != 1) {
                if (c.getDato() instanceof Archivo) {
                    System.out.println(
                            "Cluster " + i + " : contiene una parte del archivo "
                                    + ((Archivo) c.getDato()).getNombre() + "\n");

                } else if (c.getDato() instanceof Directorio) {
                    System.out.println(
                            "Cluster " + i + ":  contiene al directorio " + ((Directorio) c.getDato()).getNombre()
                                    + "\n");
                    /*
                     * Al meter en cada clusters en cluster.dato una instancia del archivo /
                     * directorio (ya que no se puede poner 1/N 2/N ...)
                     * NO queremos mostrar el nombre repetido, solo una vez (por eso el arraylist de
                     * nombres)
                     */
                    if (!nombres.contains(((Directorio) c.getDato()).getNombre())) {
                        mostrarContenidoDirectorio((Directorio) c.getDato());
                        nombres.add(((Directorio) c.getDato()).getNombre());
                    }

                } else {
                    System.out.println("Cluster " + i + " vacio");
                }
            }
        }

        FAT.mostrarCOntenidoFAT();
    }

    public static void eliminarArchivo(boolean usarEnElimDirectorio, Object archviObject) {
        // He creado una lista de int que va a ser el numero de cada cluster que ocupa
        // el archivo
        ArrayList<Integer> n = !usarEnElimDirectorio ? buscarArchivo(pedirNombre())
                : buscarArchivo(((Archivo) archviObject).getNombre());

        if (n == null) {
            System.out.println("El archivo no existe");
            return;
        }

        Archivo a = (Archivo) clusters[n.get(0)].getDato();

        // Tema metadatos
        while (!n.isEmpty()) {
            FAT.entradasFAT[n.get(0)].setDisponible(true);
            n.remove(0);
        }

        // Tema directorio raiz / entradas directorio
        borrarEnDirectorio(a);
    }

    public static void eliminarDirectorio() {
        Directorio dir = buscarDirectorio(pedirDirectorio());
        Directorio dirCopia = dir;

        if (dir == null || dir.getNombre().equals(SIstemaFicheros.directorioRaiz.getNombreRaiz())) {
            System.out.println("El directorio no existe o no se puede borrar el directorio raiz");
            return;
        }

        /*
         * Me creo un arraylist de objetos que va a contener los directorios que se van
         * a borrar ( por ejemplo si el directorio
         * que quiero borrar tiene mas directorios dentro se van a ir añadiendo a este
         * arraylist y se van a ir borrando de uno en uno
         * hasta que el arraylist este vacio) ademas tambien se borraran los respectivos
         * archivos de cierto directorio
         */
        ArrayList<Object> borrar = new ArrayList<Object>();
        borrar.add(dir);

        while (!borrar.isEmpty()) {
            dir = ((Directorio) borrar.get(0));
            while (!dir.entradasDirectorio.isEmpty()) {
                entradaDirectorio e = dir.entradasDirectorio.get(0);
                if (!e.isEsDirectorio()) {
                    eliminarArchivo(true, buscarArchivo(e.getNombre(), e.getClusterInicio()));
                } else {
                    borrar.add(buscarDirectorio(e.getNombre()));
                    dir.entradasDirectorio.remove(e);
                    FAT.entradasFAT[buscarDirectorio(buscarDirectorio(e.getNombre()))].setDisponible(true);
                }
            }

            borrar.remove(0);
        }

        // Borrar el directorio principal en sí que queria borrar el usuario
        FAT.entradasFAT[buscarDirectorio(dirCopia)].setDisponible(true);
        borrarEnDirectorio(dirCopia);
    }

    public static void moverArchivo() {
        System.out.println("El directorio nuevo : ");
        Directorio dirNuevo = buscarDirectorio(pedirDirectorio());
        System.out.println("El directorio viejo : ");
        Directorio dirViejo = buscarDirectorio(pedirDirectorio());

        if (dirNuevo == null || dirViejo == null) {
            System.out.println("El directorio no existe");
            return;
        }

        // Simplemente mover la entrada de un directorio a otro
        String nombreArchivo = pedirNombre();
        for (int i = 0; i < dirViejo.entradasDirectorio.size(); i++) {
            if (dirViejo.entradasDirectorio.get(i).getNombre().equals(nombreArchivo)
                    && dirViejo.entradasDirectorio.get(i).isEsDirectorio() == false) {
                dirNuevo.entradasDirectorio.add(dirViejo.entradasDirectorio.get(i));
                dirViejo.entradasDirectorio.remove(i);
                return;
            }
        }

        System.out.println("El archivo no existe");
    }

    public static void mostrarContenidoDirectorio() // Para pedirlo por pantalla (sobrecarga funciones)
    {
        Directorio dir = new Directorio();
        dir = buscarDirectorio(pedirDirectorio());

        if (dir == null || FAT.entradasFAT[buscarDirectorio(dir)].isDisponible()) {
            System.out.println("El directorio no existe o fue borrado");
            return;
        }

        // Mostrar las entradas del directorio
        mostrarContenidoDirectorio(dir);
    }

    public static void mostrarContenidoDirectorio(Directorio Directorio) // Para usdar desde el codigo (sobrecarga
                                                                         // funciones)
    {
        System.out.println("\n Estamos en las entradas Directorio " + Directorio.getNombre()
                + " y contiene los siguientes archivos o directorios : \n{ \n");

        for (int i = 0; i < Directorio.entradasDirectorio.size(); i++) {
            if (Directorio.entradasDirectorio.get(i) != null)
            {
                            if (Directorio.entradasDirectorio.get(i).isEsDirectorio() == false) {
                System.out.println("Archivo : " + Directorio.entradasDirectorio.get(i).getNombre() + "\n");
            } else {
                System.out.println("Directorio : " + Directorio.entradasDirectorio.get(i).getNombre() + "\n");
            }
            }
        }

        System.out.println("}\n");
    }

    /*
     * Funciones auxiliares
     * 
     * Funcion que te devuelve el directorio que buscas segun el nombre
     */
    private static Directorio buscarDirectorio(String nombre) {
        if (nombre.equals(SIstemaFicheros.directorioRaiz.getNombreRaiz())) {
            return directorioRaiz;
        }

        // Los directorio estan almacenados dentro de Cluster.dato (al igual que los
        // archivos) habria que recorrer los clusters y buscar el directorio en si
        for (Cluster cluster : clusters) {
            if (cluster.getDato() != null) {
                // Si el cluster contieene un directorio y getNombre equals nombre
                if (cluster.getDato() instanceof Directorio
                        && ((Directorio) cluster.getDato()).getNombre().equals(nombre)) {
                    return (Directorio) cluster.getDato();
                }
            }
        }
        return null;
    }

    /*
     * Funcion que te devuelve el numero de cluster en que contiene el directorio
     * que buscas segun el nombre
     */
    public static int buscarDirectorio(Directorio d) {
        int i = -1;
        for (Cluster cluster : clusters) {
            ++i;
            if (cluster.getDato() != null) {
                // Si el cluster contieene un directorio y nombre equals nombre
                if (cluster.getDato() instanceof Directorio
                        && ((Directorio) cluster.getDato()).getNombre().equals(d.getNombre())) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Funcion para borrar cierta entrada del directorio de algun directorio o
     * archivo
     * 
     * @param a ARCHIVO o DIRECTORIO
     */
    private static void borrarEnDirectorio(Object a) {
        if (a instanceof Archivo) {

            for (int i = 0; i < directorioRaiz.entradasDirectorio.size(); i++) {
                if (directorioRaiz.entradasDirectorio.get(i).getNombre().equals(((Archivo) a).getNombre())
                        && directorioRaiz.entradasDirectorio.get(i).isEsDirectorio() == false) {
                    directorioRaiz.entradasDirectorio.remove(i);
                    return;
                }
            }

            for (Cluster cluster : clusters) {
                if (cluster.getDato() != null) {
                    if (cluster.getDato() instanceof Directorio) {
                        for (int i = 0; i < ((Directorio) cluster.getDato()).entradasDirectorio.size(); i++) {
                            if (((Directorio) cluster.getDato()).entradasDirectorio.get(i).getNombre()
                                    .equals(((Archivo) a).getNombre())
                                    && ((Directorio) cluster.getDato()).entradasDirectorio.get(i)
                                            .isEsDirectorio() == false) {
                                ((Directorio) cluster.getDato()).entradasDirectorio.remove(i);
                                return;
                            }
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < directorioRaiz.entradasDirectorio.size(); i++) {
                if (directorioRaiz.entradasDirectorio.get(i).getNombre().equals(((Directorio) a).getNombre())
                        && directorioRaiz.entradasDirectorio.get(i).isEsDirectorio() == true) {
                    directorioRaiz.entradasDirectorio.remove(i);
                    return;
                }
            }

            for (Cluster cluster : clusters) {
                if (cluster.getDato() != null) {
                    if (cluster.getDato() instanceof Directorio) {
                        for (int i = 0; i < ((Directorio) cluster.getDato()).entradasDirectorio.size(); i++) {
                            if (((Directorio) cluster.getDato()).entradasDirectorio.get(i).getNombre()
                                    .equals(((Directorio) a).getNombre())
                                    && ((Directorio) cluster.getDato()).entradasDirectorio.get(i)
                                            .isEsDirectorio() == true) {
                                ((Directorio) cluster.getDato()).entradasDirectorio.remove(i);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    /*
     * Funcion para buscar un archivo en los clusters segun el nombre
     * te devuelve un arraylist de int que contiene los numeros de los clusters que
     * contiene el archivo
     */
    private static ArrayList<Integer> buscarArchivo(String nombre) {
        ArrayList<Integer> n = new ArrayList<Integer>();

        for (int i = 0; i < Cluster.cantidadClusters; i++) {
            if (clusters[i].getDato() != null) {
                // Si el cluster contieene un archivo y nombre equals nombre
                if (clusters[i].getDato() instanceof Archivo
                        && ((Archivo) clusters[i].getDato()).getNombre().equals(nombre)) {
                    n.add(i);
                    i = (FAT.entradasFAT[i].getSiguienteCluster() != -1) ? FAT.entradasFAT[i].getSiguienteCluster() - 1
                            : 20;
                }
            }
        }
        if (!n.isEmpty())
            return n;
        return null;
    }

    /*
     * Funcion para buscar un archivo en los clusters segun el nombre y cluster de
     * inicio
     * te devuelve el archivo en si
     */
    private static Archivo buscarArchivo(String nombre, int clusterInicio) {
        if (clusters[clusterInicio].getDato() instanceof Archivo
                && ((Archivo) clusters[clusterInicio].getDato()).getNombre().equals(nombre)) {
            return (Archivo) clusters[clusterInicio].getDato();
        }
        return null;
    }

    private static String pedirContenido() {
        String contenido = "";
        do {
            System.out.println("Introduzca el contenido del archivo\n");
            contenido = System.console().readLine();
            System.out.println("\n");
        } while (contenido.length() == 0);

        return contenido;
    }

    private static String pedirNombre() {
        String nombre = "";
        do {
            System.out.println("Introduzca el nombre del archivo\n");
            nombre = System.console().readLine();
            System.out.println("\n");
        } while (nombre.length() == 0);
        return nombre;
    }

    private static int pedirTamano() {
        int tamano = 0;
        do {
            System.out.println("Introduzca el tamaño del archivo\n");
            tamano = Integer.parseInt(System.console().readLine());
            System.out.println("\n");
        } while (tamano <= 0);
        return tamano;
    }

    private static String pedirDirectorio() {
        String directorio = "";
        do {
            System.out.println("Introduzca el nombre del directorio\n");
            directorio = System.console().readLine();
            System.out.println("\n");
        } while (directorio.length() == 0);
        return directorio;
    }
}
