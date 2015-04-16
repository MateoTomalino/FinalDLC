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
public class Vocabulario implements Serializable {

    private HashMap<String, Palabra> vocabulario;
    private static final String NOMBRE_VOCABULARIO = "vocabulario";
    private static final String EXTENSION = ".dic";
    private static Vocabulario self;

    private Vocabulario() {
        File lector = new File(NOMBRE_VOCABULARIO + EXTENSION);
        System.out.println("Comenza clase Vocabulario.");
        try {
            if (lector.createNewFile()) {
                System.out.println("El vocabulario no existía, se creo uno nuevo.");
            } else {
                System.out.println("Leyendo Vocabulario...");
            }
            vocabulario = new HashMap();
            if (lector.length() > 0) {
                FileInputStream fis = new FileInputStream(lector);
                ObjectInputStream ois = new ObjectInputStream(fis);
                System.out.println("Casteando...");
                while (fis.available() > 0) {
                    vocabulario = (HashMap<String, Palabra>) ois.readObject();
                    System.out.println("Casting terminado.");
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error en vocabulario: " + ex.toString());
        }
    }

    public static Vocabulario getInstance() {
        if (self == null) {
            self = new Vocabulario();
        }
        return self;
    }

    public boolean guardar() {
        boolean exito = false;
        File archivo = new File(NOMBRE_VOCABULARIO + EXTENSION);
        System.out.println("Vocabulario: " + vocabulario.size());
        try {
            FileOutputStream fos = new FileOutputStream(archivo, false); //false = pisa todo el documento, true = agrega al ultimo
            ObjectOutputStream ois = new ObjectOutputStream(fos);
            ois.writeObject(vocabulario);
            exito = true;
        } catch (IOException ex) {
            System.out.println("Error en guardarVocabulario: " + ex.toString());
        }
        return exito;
    }

    public Palabra existeTermino(String termino) {
        Palabra palabra = vocabulario.get(termino);
        if (palabra == null) {
            palabra = new Palabra();  
            palabra.setTermino(termino);
        }
        return palabra;
    }

    public void agregarPalabra(String termino, int tf) {
        Palabra palabra = existeTermino(termino);

        //seteo de maxTf y nr
        if (tf > palabra.getMaxtf()) {
            palabra.setMaxtf(tf);
        }
        palabra.setNr(palabra.getNr() + 1);
        
        vocabulario.put(termino, palabra);
    }

    public HashMap<String, Palabra> getVocabulario() {
        return vocabulario;
    }

    public void setVocabulario(HashMap<String, Palabra> vocabulario) {
        this.vocabulario = vocabulario;
    }
}
