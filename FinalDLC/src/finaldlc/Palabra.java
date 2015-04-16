package finaldlc;

import java.io.Serializable;

/**
 *
 * @author dahyana
 */
public class Palabra implements Serializable{
    
    private int maxtf;
    private int nr;
    private String termino;

    public Palabra() {
        nr = 0;
        maxtf = -1;
        termino = "";
    }
    
    public Palabra(int maxtf, int nr, String termino) {
        this.maxtf = maxtf;
        this.nr = nr;
        this.termino = termino;
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }

    
    public int getMaxtf() {
        return maxtf;
    }

    public void setMaxtf(int maxtf) {
        this.maxtf = maxtf;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }
    
    
    
}
