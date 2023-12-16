package SIstemaFicheros;

public class Console 
{
    public static void main(String[] args) 
    {
        Console c = new Console();
        
        c.bienvenidaTitle();
        sistemaFicherosFAT.inicializarClusters();
        do {
            c.menu();
        } while (true);
    }

    public void bienvenidaTitle ()
    {
        System.out.println("\nBienvenido Usuario! Al Sistema de Ficheros FAT");
        System.out.println("Versión: 1.0");
        System.out.println("Licencia: MIT");
        System.out.println("Descripción: Sistema de ficheros FAT\n\n\n");
    }
    
    public void clearTerminal()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void esperarUsuario ()
    {
        System.out.println("\n\nPulse enter para continuar");
        System.console().readLine();
    }
    
    public void menu ()
    {
        System.out.println("1. Crear Archivo");
        System.out.println("2. Crear Directorio");
        System.out.println("3. Mostrar Contenido Sistema");
        System.out.println("4. Eliminar Archivo");
        System.out.println("5. Eliminar Directorio");
        System.out.println("6. Mover Archivo");
        System.out.println("7. Mostrar Contenido Directorio");
        System.out.println("8. Salir\n");

        int opcion = 0;
        opcion = Integer.parseInt(System.console().readLine());

        System.out.println("\n\n");
        
        switch (opcion) {
            case 1:
                sistemaFicherosFAT.crearArchivo();
                esperarUsuario();
                break;
            case 2:
                sistemaFicherosFAT.crearDirectorio();
                esperarUsuario();
                break;
            case 3:
                sistemaFicherosFAT.mostrarContenidoSistema();
                esperarUsuario();
                break;
            case 4:
                sistemaFicherosFAT.eliminarArchivo(false, null);
                esperarUsuario();
                break;
            case 5:
                sistemaFicherosFAT.eliminarDirectorio();
                esperarUsuario();
                break;
            case 6:
                sistemaFicherosFAT.moverArchivo();
                esperarUsuario();
                break;
            case 7:
                sistemaFicherosFAT.mostrarContenidoDirectorio();
                esperarUsuario();
                break;
            case 8:
                System.exit(0);
                break;
            default:
                System.out.println("Opción no válida");
                esperarUsuario();
                break;
        }
        
        clearTerminal();
    }
}
