package finaldlc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import procesador.Indexador;

//import org.apache.poi.hwpf.extractor.WordExtractor; //Para leer un .doc
//import org.apache.poi.xwpf.extractor.XWPFWordExtractor; //Para leer un XWPF Document
//import org.apache.poi.xwpf.usermodel.XWPFDocument; //Para instanciar un .docx



/**
 *
 * @author dahyana
 */
public class ParserDocumento extends Thread {

    /*TODO
     * - Comenzar o reanudar documentos [leer utimoDocumento.dat que guarda el indice del ultimo documento analizado] CHECK
     * - Analizar tipos de archivos CHECK
     * - Controlar la memoria:
     *    * Caso de tener poca -> Esperar 10 minutos CHECK
     *    * Si sigue sin memroria -> Guardar ultimo documento parseado y terminar CHECK
     *    * Caso de recuperar memoria -> Seguir CHECK
     * - Si se intenta finalizar y esta leyendo -> Cortar guardar ultimo documento CHECK
     * - Si se intenta finalizar y ya se parseo el texto -> Terminar la creacion de indice, guardar ultimo documento y finalizar CHECK
     */
    private static String DIRECTORIO = "documentos/";
    private int ultimoArchivo;
    private boolean extracionTerminada;
    private Indice indice;
    private Vocabulario vocabulario;
    private static final int LIMITE_MEMORIA = 50;
    private Runtime garbage;
    private File[] archivos;
    private Indexador log;
    //Parser elements
    //.docx
    //.pdf
    private PDFTextStripper pdfStripper;
    private PDDocument pdfDoc;
    //.txt
    private FileInputStream fis;
    private BufferedReader reader;
    //.doc

    /*
     * Lee el archivo ultimoDocumento.dat que contiene el indice/numero del ultimo archivo procesado
     * En el caso de un FileNotFound ponerlo en 0 e iniciar el proceso
     * En el casode un IOException cortar el hilo
     */
    public ParserDocumento(String directorio, File[] archivos, Indexador log) {
        try {
            if (archivos != null) {
                this.archivos = archivos;
            } else if (directorio != null) {
                this.DIRECTORIO = directorio;
            }
            this.log = log;
            garbage = Runtime.getRuntime();
            Indice.setup("indice1");
            indice = Indice.getInstance();
            vocabulario = Vocabulario.getInstance();
            //Nuevo
            File ultimoDocumento = new File("ultimoDocumento.dat");
            if (!ultimoDocumento.exists()) {
                ultimoDocumento.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(ultimoDocumento, "r");
            raf.seek(0);
            ultimoArchivo = raf.readInt();
        } catch (FileNotFoundException ex) {
            System.out.println("Error en ParserDocumento: " + ex.toString());
            log.agregarInfo("Error en ParserDocumento: " + ex.toString());
            ultimoArchivo = 0;
        } catch (IOException ex) {
            System.out.println("Error I/O en ParserDocumento: " + ex.toString());
            log.agregarInfo("Error I/O en ParserDocumento: " + ex.toString());
            ultimoArchivo = 0;
        }
    }

    private boolean sePuedeCancelar() {
        return !extracionTerminada;
    }

    @Override
    public void run() {
        log.agregarInfo("Se inicio el parser");
        long iTime = System.currentTimeMillis();
        try {
            if (archivos == null) {
                File directorio = new File(DIRECTORIO);
                archivos = directorio.listFiles();
            }
            if (ultimoArchivo >= archivos.length) {
                log.agregarInfo("No hay mas documentos para procesar.");
                this.interrupt();
                return;
            }
            String textoLeido;
            HashMap<String, Integer> soporte = new HashMap();
            Integer valor = 0;
            NodoIndice nodo = null;

            for (int i = ultimoArchivo; i < archivos.length; i++) {
                if (!memoriaSuficiente()) {
                    try {
                        log.agregarInfo("Esperando por limpieza de memoria...");
                        garbage.gc();
                        sleep(1000 * 30);
                        if (!memoriaSuficiente()) {
                            log.agregarInfo("No hay memoria suficiente.");
                            guardarEstructuras();
                            this.interrupt();
                            return;
                        }
                    } catch (InterruptedException ex) {
                        System.out.println("Error cuando se queria dormir el hilo: " + ex.toString());
                        guardarEstructuras();
                        return;
                    }
                }
                log.agregarInfo("Parseando " + (i + 1) + "/" + archivos.length);
                textoLeido = procesarExtension(archivos[i]); //Mientras se esta leyendo se puede cancelar el hilo
                extracionTerminada = true;
                textoLeido = Util.getInstance().limpiarPalabra(textoLeido);
                String[] palabrasSeparadas = textoLeido.split(" ");
                soporte.clear();
                valor = 0;

                log.agregarInfo("Procesando " + palabrasSeparadas.length + " palabras");
                for (int j = 0; j < palabrasSeparadas.length; j++) {
                    if (!Util.getInstance().esPalabraBasura(palabrasSeparadas[j])) {
                        valor = soporte.get(palabrasSeparadas[j]);
                        if (valor == null) {
                            valor = 0;
                        }
                        valor++;
                        soporte.put(palabrasSeparadas[j], valor);
                    }
                }
                if (soporte.size() > 1) {
                    log.agregarInfo("Creando " + soporte.size() + " nodos...");
                    for (Map.Entry<String, Integer> entrada : soporte.entrySet()) {
                        nodo = new NodoIndice(archivos[i].getAbsolutePath(), entrada.getValue(), entrada.getKey());
                        indice.agregarNodoAListaDocumento(nodo, entrada.getKey());
                        vocabulario.agregarPalabra(entrada.getKey(), entrada.getValue());
                    }
                } else {
                    log.agregarInfo("Es posible que el documento no se haya podido leer correctamente, ya que solo se detecto 1 nodo.");
                }
                //Despues de procesar pisar ultimoArchivo
                ultimoArchivo = i + 1;
                extracionTerminada = false;
            }
            //guardarEstructuras();
        } catch (Exception ex) {
            log.agregarInfo("Error en run: " + ex.toString());
        } finally {
            guardarEstructuras();
        }
        long fTime = System.currentTimeMillis();
        log.agregarInfo("Se termino el parser");
        log.agregarInfo("Se tardo: " + ((fTime - iTime) / 1000 / 60) + " min.");
    }

    public boolean finalizar() {
        boolean respuesta = sePuedeCancelar();
        if (respuesta) {
            this.stop();
            guardarEstructuras();
        }
        return respuesta;
    }

    private boolean guardarEstructuras() {
        boolean exito = false;
        log.agregarInfo("Guardando indice y vocabulario...");
        try {
            garbage.gc();
            indice.guardar();
            garbage.gc();
            vocabulario.guardar();
            garbage.gc();
            guardarUltimoArchivo();
            exito = true;
        } catch (Exception ex) {
            log.agregarInfo("Error en guardarEstructuras: " + ex.toString());
        }
        return exito;
    }

    private void guardarUltimoArchivo() throws FileNotFoundException, IOException {
        File ultimoDocumento = new File("ultimoDocumento.dat");
        if (!ultimoDocumento.exists()) {
            ultimoDocumento.createNewFile();
        }
        RandomAccessFile raf = new RandomAccessFile(ultimoDocumento, "rw");
        raf.writeInt(ultimoArchivo);
    }

    private String procesarExtension(File documento) {
        String textoLeido = "";
        String extension = documento.getName().substring(documento.getName().length() - 3, documento.getName().length()).toLowerCase();
        switch (extension) {
            case "pdf":
                textoLeido = procesarPDF(documento);
                break;
            case "txt":
                textoLeido = procesarTXT(documento);
                break;
//            case "doc":
//                textoLeido = procesarDOC(documento);
//                break;
            default:
                log.agregarInfo("Extension " + extension + " no soportada.");
        }
        return textoLeido.toLowerCase();
    }

    private String procesarPDF(File documento) {
        String texto = "";
        try {
            if (pdfStripper == null) {
                pdfStripper = new PDFTextStripper();
            }
            pdfDoc = PDDocument.load(documento.getAbsolutePath());
            texto = pdfStripper.getText(pdfDoc);
            pdfDoc.close();
        } catch (IOException ex) {
            log.agregarInfo("Error en procesarPDF: " + ex.toString());
        }
        return texto;
    }

    private String procesarTXT(File documento) {
        StringBuilder builder = new StringBuilder();
        try {
            fis = new FileInputStream(documento.getAbsolutePath());
            reader = new BufferedReader(new InputStreamReader(fis));
            while (reader.ready()) {
                builder.append(reader.readLine());
            }
        } catch (IOException ex) {
            log.agregarInfo("Error en procesarTXT: " + ex.toString());
        }
        return builder.toString();
    }

//    private String procesarDOC(File documento) {
//        String text = "";
//        try {
//             fis = new FileInputStream(documento.getAbsolutePath());
//             WordExtractor wex = new WordExtractor(fis);             
//             text = wex.getText();
//             fis.close();
//        } catch (IOException ex) {
//            log.agregarInfo("Error en procesar DOC: " + ex.toString());
//        }
//        return text;
//    }

   
    private boolean memoriaSuficiente() {
        boolean memoriaSuficiente = true;
        garbage.gc();
        int memoriaLibre = (int) (garbage.freeMemory() / 1024 / 1000);
        int maxMemoria = (int) (garbage.maxMemory() / 1024 / 1000);
        log.agregarInfo("Memoria: " + memoriaLibre + "/" + maxMemoria);
        if ((maxMemoria - memoriaLibre) < LIMITE_MEMORIA) {
            memoriaSuficiente = false;
        }
        return memoriaSuficiente;
    }
}
