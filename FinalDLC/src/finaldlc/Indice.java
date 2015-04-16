package finaldlc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author dahyana
 */
public class Indice implements Serializable {

    private HashMap<String, ListaDocumento> indice;
    private static String nombre;
    private static final String EXTENSION = ".index";
    private static Indice self;
    
    
    private Indice() {
        File lector = new File(nombre + EXTENSION);
        System.out.println("Comenzo clase Indice.");
        try {
            if (lector.createNewFile()) {
                System.out.println("El indice no existia, se creo uno nuevo.");
            } else {
                System.out.println("Leyendo indice...");
            }
            indice = new HashMap();
            if (lector.length() > 0) {
                FileInputStream fis = new FileInputStream(lector);
                ObjectInputStream ois = new ObjectInputStream(fis);
                System.out.println("Casteando indice...");
                while (fis.available() > 0) {
                    indice = (HashMap<String, ListaDocumento>) ois.readObject();
                    System.out.println("Casting terminado.");
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error en indice: " + ex.toString());
        }
    }
    
    public static void setup(String nombre) {
        Indice.nombre = nombre;
    }
    
    public static Indice getInstance() {
        if (self == null) {
            self = new Indice();
        }
        return self;
    }

    public boolean guardar() {
        boolean exito = false;
        File archivo = new File(nombre + EXTENSION);
        System.out.println("Indice: " + indice.size());
        try {
            FileOutputStream fos = new FileOutputStream(archivo, false); //false = pisa todo el documento, true = agrega al ultimo
            ObjectOutputStream ois = new ObjectOutputStream(fos);
            ois.writeObject(indice);
            exito = true;
        } catch (IOException ex) {
            System.out.println("Error en guardarIndice: " + ex.toString());
        }
        return exito;
    }

    public void agregarNodoAListaDocumento(NodoIndice nodo, String palabraAnalizada) {
        ListaDocumento lista = obtenerListaDocumento(palabraAnalizada);
        
        lista.agregarNodo(nodo);
        indice.put(palabraAnalizada, lista); //Pisa la lista ya modificada
    }

    public HashMap<String, ListaDocumento> getIndice() {
        return indice;
    }

    public void setIndice(HashMap<String, ListaDocumento> indice) {
        this.indice = indice;
    }

    //Obtiene la lista de los documentos que contienen esa palabra
    public ListaDocumento obtenerListaDocumento(String consulta) {
        ListaDocumento lista = indice.get(consulta);
        if (lista == null) {
            lista = new ListaDocumento();
        }
        return lista;
    }

}
