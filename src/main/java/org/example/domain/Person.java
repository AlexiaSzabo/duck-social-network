package org.example.domain;

import java.time.LocalDate;

/**
 * Represents a Person entity stored in the "persons" database table.
 * A Person extends a User through the same ID.
 */
public class Person extends User {

    private String nume;
    private String prenume;
    private String ocupatie;
    private double nivelEmpatie;
    private LocalDate dataNasterii;

    // FULL CONSTRUCTOR with User data + Person data
    public Person(Integer id, String username, String email, String password,
                  String nume, String prenume, String ocupatie,
                  double nivelEmpatie, LocalDate dataNasterii) {

        super(id, username, email, password); // 🔥 user fields

        this.nume = nume;
        this.prenume = prenume;
        this.ocupatie = ocupatie;
        this.nivelEmpatie = nivelEmpatie;
        this.dataNasterii = dataNasterii;
    }

    // Constructor used when inserting a new person
    // (User must be inserted first!)
    public Person(Integer id, String nume, String prenume, String ocupatie,
                  double nivelEmpatie, LocalDate dataNasterii) {

        super(id, null, null, null); // username/email loaded later

        this.nume = nume;
        this.prenume = prenume;
        this.ocupatie = ocupatie;
        this.nivelEmpatie = nivelEmpatie;
        this.dataNasterii = dataNasterii;
    }

    // GETTERS
    public String getNume() { return nume; }
    public String getPrenume() { return prenume; }
    public String getOcupatie() { return ocupatie; }
    public double getNivelEmpatie() { return nivelEmpatie; }
    public LocalDate getDataNasterii() { return dataNasterii; }

    // SETTERS
    public void setNume(String nume) { this.nume = nume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }
    public void setOcupatie(String ocupatie) { this.ocupatie = ocupatie; }
    public void setNivelEmpatie(double nivelEmpatie) { this.nivelEmpatie = nivelEmpatie; }
    public void setDataNasterii(LocalDate dataNasterii) { this.dataNasterii = dataNasterii; }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", ocupatie='" + ocupatie + '\'' +
                ", empatie=" + nivelEmpatie +
                ", dataNasterii=" + dataNasterii +
                '}';
    }
}
