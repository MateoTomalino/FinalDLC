/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finaldlc;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author dahyana
 */
public class ListaDocumento implements Serializable {

    private LinkedList<NodoIndice> lista;

    public ListaDocumento() {
        lista = new LinkedList();
    }

    public LinkedList<NodoIndice> getLista() {
        return lista;
    }

    public void setLista(LinkedList<NodoIndice> lista) {
        this.lista = lista;
    }

    public void agregarNodo(NodoIndice nodo) {
        if (lista.size() == 0) {
            lista.add(nodo);
        } else {
            boolean insertado = false;
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getTf() < nodo.getTf()) {
                    lista.add(i, nodo);
                    insertado = true;
                    break;
                }
            }
            if (!insertado) {
                lista.add(nodo);
            }
        }
    }
    
    public ListaDocumento obtenerPrimeros(int cantidad){
        
        ListaDocumento listaAux = new ListaDocumento();
        
        for (int i = 0; i < lista.size(); i++) {
            
            if(i < cantidad-1){
                
                listaAux.getLista().add(lista.get(i));
            }
        }
        return listaAux;
    }

}
