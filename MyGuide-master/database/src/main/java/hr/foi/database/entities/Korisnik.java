package hr.foi.database.entities;

/**
 * Created by Mateo on 7.12.2017..
 */

public class Korisnik {
    public Integer id_korisnik;
    public String ime;
    public String prezime;
    public String email;
    public String korisniko_ime;
    public String img_path;
    public String img_name;
    public Integer id_tip_korisnika;

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    public  Korisnik(){

    }


    public Korisnik(Integer id_korisnik, String ime, String prezime, String email, String korisniko_ime,String img_path, String img_name, Integer id_tip_korisnika) {
        this.id_korisnik = id_korisnik;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.korisniko_ime = korisniko_ime;
        this.img_name = img_name;
        this.img_path = img_path;
        this.id_tip_korisnika = id_tip_korisnika;
    }

    public Integer getId_korisnik() {
        return id_korisnik;
    }

    public void setId_korisnik(Integer id_korisnik) {
        this.id_korisnik = id_korisnik;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKorisniko_ime() {
        return korisniko_ime;
    }

    public void setKorisniko_ime(String korisniko_ime) {
        this.korisniko_ime = korisniko_ime;
    }

    public Integer getId_tip_korisnika() {
        return id_tip_korisnika;
    }

    public void setId_tip_korisnika(Integer id_tip_korisnika) {
        this.id_tip_korisnika = id_tip_korisnika;
    }
}
