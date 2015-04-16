package finaldlc;

/**
 *
 * @author Jessica
 */
public class Util {

    private static Util self;

    private Util() {
    }

    public static Util getInstance() {
       if (self==null){
           self = new Util();
       }
        return self;
    }
    
    public String limpiarPalabra(String temp) {
        temp = eliminarAcentos(temp);
        temp = eliminarCaracteresEspeciales(temp);
        return temp;
    }

    private String eliminarAcentos(String cadena) {
        cadena = cadena.replace('á', 'a');
        cadena = cadena.replace('é', 'e');
        cadena = cadena.replace('í', 'i');
        cadena = cadena.replace('ó', 'o');
        cadena = cadena.replace('ú', 'u');
        return cadena;
    }

    public boolean esPalabraBasura(String termino) {
        String[] terminosBasura = "a,ante,bajo,con,de,desde,durante,en,entre,excepto,hacia,hasta,mediante,para,por,salvo,según,sin,sobre,tras,que,quien,como,cuando,donde,porque,cual,si,no,el,lo,la,las,los,ello,ella,le,se,su,mi,tu,en,y,o".split(",");
        boolean esTerminoBasura = false;
        for (int i = 0; i < terminosBasura.length; i++) {
            if (termino.compareToIgnoreCase(terminosBasura[i]) == 0) {
                esTerminoBasura = true;
                break;
            }
        }
        return esTerminoBasura;
    }

    private String eliminarCaracteresEspeciales(String cadena) {
        //char caracter = ' ';
        cadena.replaceAll("[^a-zA-Z\\s]", "");
//        for (int i = 0; i < cadena.length(); i++) {
//            caracter = cadena.charAt(i);
//            if (!(caracter >= 97 && caracter <= 122)) {
//                cadena = cadena.replace(caracter, ' '); //Reemplaza caracter que no no este entre [az]
//            }
//        }
        return cadena;
    }
}
