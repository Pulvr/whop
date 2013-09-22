package bib.local.valueobjects;

/**
 * Massenware ist von ware abgeleitet und hat das extra attribut packungsgr��e
 * @author Florian
 *
 */
public class MassengutWare extends Ware {
    private static final long serialVersionUID = 1L;
    
    private int packungsGroesse = 0;

    public MassengutWare(String bezeichnung, int nr, int bstd, float preis, int packungsGroesse) {
        super(bezeichnung, nr, bstd, preis);
        this.packungsGroesse = packungsGroesse;
    }

    public int getPackungsGroesse() {
        return packungsGroesse;
    }

    public void setPackungsGroesse(int packungsGroesse) {
        this.packungsGroesse = packungsGroesse;
    }
    
    /**
     * Standard-Methode von Object �berschrieben.
     * Methode wird immer automatisch aufgerufen, wenn ein Waren-Objekt als String
     * benutzt wird (z.B. in println(Ware);)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String ausgabe = super.toString(false);
        return ausgabe + " / Packungsgr��e: " + packungsGroesse + "\n";
    }
    
    @Override
    /**
     * Methode die �berpr�ft ob die bestelltemenge (gui oder cui) der packungsgr��e entspricht
     */
    public boolean checkBestellmengeGueltig(int menge) {
        return (menge % packungsGroesse == 0);
    }
    
    public float getRechnungsPreis(){
        return getPreis() * getPackungsGroesse();
    }
}
