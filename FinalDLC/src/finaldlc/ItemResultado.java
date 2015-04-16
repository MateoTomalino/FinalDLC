/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package finaldlc;

import java.util.LinkedList;

/**
 *
 * @author dahyana
 */
public class ItemResultado {
    
    private String rutaDocumento;
    private int ocurrencia;
    
    public ItemResultado(String rd, int ocu) {
        rutaDocumento = rd;
        ocurrencia = ocu;
    }

    /**
     * @return the rutaDocumento
     */
    public String getRutaDocumento() {
        return rutaDocumento;
    }

    /**
     * @param rutaDocumento the rutaDocumento to set
     */
    public void setRutaDocumento(String rutaDocumento) {
        this.rutaDocumento = rutaDocumento;
    }

    /**
     * @return the ocurrencia
     */
    public int getOcurrencia() {
        return ocurrencia;
    }

    /**
     * @param ocurrencia the ocurrencia to set
     */
    public void setOcurrencia(int ocurrencia) {
        this.ocurrencia = ocurrencia;
    }
    
    
    
    
}
