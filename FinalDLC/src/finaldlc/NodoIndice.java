/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package finaldlc;

import java.io.Serializable;

/**
 *
 * @author dahyana
 */
public class NodoIndice implements Serializable {
    
    private String rutaDocumento;
    private int tf;
    private String termino;
    
    public NodoIndice() {
    }
    
    public NodoIndice(String rutaDocumento, int tf, String termino) {
        this.rutaDocumento = rutaDocumento;
        this.tf = tf;
        this.termino = termino;
        
              
    }

    public String getRutaDocumento() {
        return rutaDocumento;
    }

    public void setRutaDocumento(String rutaDocumento) {
        this.rutaDocumento = rutaDocumento;
    }

    public int getTf() {
        return tf;
    }

    public void setTf(int tf) {
        this.tf = tf;
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }

    public String toString() {
        return "Doc.: " + rutaDocumento + "\ntF: " + tf + "\n";
    }
}
