package org.example.domain;

/**
 * Represents a Duck entity stored in the "ducks" table.
 * Each Duck extends a User through user_id (primary key).
 * The "tip" field maps to TipRata enum values.
 */
public class Duck extends User {

    private TipRata tip;
    private double viteza;
    private double rezistenta;

    public Duck(Integer id, String username, String email, String password,
                TipRata tip, double viteza, double rezistenta) {
        super(id, username, email, password);
        this.tip = tip;
        this.viteza = viteza;
        this.rezistenta = rezistenta;
    }

  //  public Duck(TipRata tip, double viteza, double rezistenta) {
   //     this(null, tip, viteza, rezistenta);
   // }

    public TipRata getTip() {
        return tip;
    }
    public double getViteza() {
        return viteza;
    }
    public double getRezistenta() {
        return rezistenta;
    }

    @Override
    public String toString() {
        return String.format(
                "Duck{userId=%d, tip=%s, viteza=%.2f, rezistenta=%.2f}",
                id, tip, viteza, rezistenta
        );
    }

}
