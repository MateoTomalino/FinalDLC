/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finaldlc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Buscador {

    private static final int DOCUMENTOS_PAG = 8;
    private static final int MAX_LISTAS = 25;
    private static Buscador self;
    private LinkedList<ItemResultado> resultados;
    
    private Buscador() {
        resultados = new LinkedList<>();
    }
    
    public static Buscador getInstance() {
        if (self == null) {
            self = new Buscador();
        }
        return self;
    }
    
    public int totalPaginas() {
        return resultados.size();
    }
    
    public LinkedList<ItemResultado> obtenerPagina(int numeroPagina) {
        LinkedList<ItemResultado> paginados = new LinkedList<>();
        for (int i = (numeroPagina - 1) * 8; i < resultados.size() && i < (numeroPagina * 8); i++) {
            paginados.add(resultados.get(i));
        }
        return paginados;
    }

    public void obtenerResultados(String consulta) {

        String[] terminos = Util.getInstance().limpiarPalabra(consulta).split(" ");
        LinkedList<Palabra> palabrasBusqueda = new LinkedList<>();
        Palabra aux;

        //Para cada termino ya limpio 
        for (int i = 0; i < terminos.length; i++) {

            if (!Util.getInstance().esPalabraBasura(terminos[i])) //Se verifica que no sea termino basura
            {
                aux = Vocabulario.getInstance().existeTermino(terminos[i]); //Se busca la palabra en el Vocabulario

                if (aux.getNr() != 0) //Si existe la palabra
                {
                    //Se agrega a la lista ordenado de menor a mayor Nr
                    if (palabrasBusqueda.size() == 0) {
                        palabrasBusqueda.add(aux);
                    } else {
                        for (int j = 0; j < palabrasBusqueda.size(); j++) {
                            if (palabrasBusqueda.get(j).getNr() > aux.getNr()) {
                                palabrasBusqueda.add(j, aux);
                                break;
                            }
                        }
                    }
                }
            }
        }

        LinkedList<ListaDocumento> listas = new LinkedList<>();

        //Para cada termino de busqueda ya ordenado, se obtiene su lista de posteo hasta una cantidad MAX_LISTAS
        for (int i = 0; i < palabrasBusqueda.size(); i++) {
            listas.add(Indice.getInstance().obtenerListaDocumento(palabrasBusqueda.get(i).getTermino()).obtenerPrimeros(MAX_LISTAS));
        }

        //String = RutaDocumento y Integer = ranking
        HashMap<String, Integer> preRanking = new HashMap<>();
        Integer valor = 0;

        for (ListaDocumento listaDoc : listas) {

            for (NodoIndice nodo : listaDoc.getLista()) {
                valor = preRanking.get(nodo.getRutaDocumento());
                if (valor == null) {
                    valor = 0;
                }
                valor++;
                preRanking.put(nodo.getRutaDocumento(), valor);
            }

        }
        
        //Ordenar HashMap segun ranking

        ItemResultado temp;
        LinkedList<ItemResultado> ranking = new LinkedList<>();
        for (Map.Entry<String, Integer> entrada : preRanking.entrySet()) {
            temp = new ItemResultado(entrada.getKey(), entrada.getValue());
            if (ranking.size() == 0) {
                ranking.add(temp);
            } else {
                for (int j = 0; j < ranking.size(); j++) {
                    if (ranking.get(j).getOcurrencia() > temp.getOcurrencia()) {
                        ranking.add(j, temp);
                        break;
                    }
                }
            }
        }
        
        resultados = ranking;
    }

}
