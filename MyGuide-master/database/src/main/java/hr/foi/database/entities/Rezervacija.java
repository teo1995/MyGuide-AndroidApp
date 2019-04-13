package hr.foi.database.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateo on 22.1.2018..
 */

public class Rezervacija {

    public Integer id_korisnik;
    public Integer id_termin;
    public Integer broj_osoba;


    public List<Rezervacija> rezervacijaList = new ArrayList<Rezervacija>();

    public Rezervacija(){

    }

    public Rezervacija(Integer id_korisnik, Integer id_termin, Integer broj_osoba) {
        this.id_korisnik = id_korisnik;
        this.id_termin = id_termin;
        this.broj_osoba = broj_osoba;
    }



    public Integer getId_korisnik() {
        return id_korisnik;
    }

    public void setId_korisnik(Integer id_korisnik) {
        this.id_korisnik = id_korisnik;
    }

    public Integer getId_termin() {
        return id_termin;
    }

    public void setId_termin(Integer id_termin) {
        this.id_termin = id_termin;
    }

    public Integer getBroj_osoba() {
        return broj_osoba;
    }

    public void setBroj_osoba(Integer broj_osoba) {
        this.broj_osoba = broj_osoba;
    }

    public void fetchRezervacija(JSONArray jsonData) {
        for (int i = 0; i < jsonData.length(); i++) {
            //JSONObject currentRow = tours.getJSONObject(i).getInt("id_tura");
            JSONObject currentRezervacija = null;
            try {
                currentRezervacija = jsonData.getJSONObject(i);
                Rezervacija rezervacijaInstance = new Rezervacija();
                rezervacijaInstance.id_korisnik = currentRezervacija.getInt("id_korisnik");
                rezervacijaInstance.id_termin = currentRezervacija.getInt("id_termin");
                rezervacijaInstance.broj_osoba = currentRezervacija.getInt("broj_osoba");
                rezervacijaList.add(rezervacijaInstance);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
