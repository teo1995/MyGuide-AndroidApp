package hr.foi.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mateo on 21.1.2018..
 */

public class Termin implements Parcelable {
    public Integer id_termin;
    public String vrijeme_od;
    public String vrijeme_do;
    public String napomena;
    public Integer id_mjesto_sastanka;
    public Integer id_tura;
    public String adresa;
    public Double latitude;
    public Double longitude;

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<Termin> terminList = new ArrayList<Termin>();

    public Termin () {

    }

    public Termin(Integer id_termin, String vrijeme_od,String vrijeme_do, String napomena, Integer id_mjesto_sastanka, Integer id_tura, String adresa, Double latitude, Double longitude) {
        this.id_termin = id_termin;
        this.vrijeme_od = vrijeme_od;
        this.vrijeme_do = vrijeme_do;
        this.napomena = napomena;
        this.id_mjesto_sastanka = id_mjesto_sastanka;
        this.id_tura = id_tura;
        this.adresa = adresa;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Termin(Parcel in) {
        id_termin = in.readInt();
        vrijeme_od = in.readString();
        vrijeme_do = in.readString();
        napomena = in.readString();
        id_mjesto_sastanka = in.readInt();
        id_tura = in.readInt();
        adresa = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        terminList = in.createTypedArrayList(Termin.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_termin);
        dest.writeString(vrijeme_od);
        dest.writeString(vrijeme_do);
        dest.writeString(napomena);
        dest.writeInt(id_mjesto_sastanka);
        dest.writeInt(id_tura);
        dest.writeString(adresa);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);

        dest.writeTypedList(terminList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Termin> CREATOR = new Creator<Termin>() {
        @Override
        public Termin createFromParcel(Parcel in) {
            return new Termin(in);
        }

        @Override
        public Termin[] newArray(int size) {
            return new Termin[size];
        }
    };


    public Integer getId_termin() {
        return id_termin;
    }

    public void setId_termin(Integer id_termin) {
        this.id_termin = id_termin;
    }

    public String getVrijeme_od() {
        return vrijeme_od;
    }

    public void setVrijeme_od(String vrijeme_od) {
        this.vrijeme_od = vrijeme_od;
    }

    public String getVrijeme_do() {
        return vrijeme_do;
    }

    public void setVrijeme_do(String vrijeme_do) {
        this.vrijeme_do = vrijeme_do;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public Integer getId_mjesto_sastanka() {
        return id_mjesto_sastanka;
    }

    public void setId_mjesto_sastanka(Integer id_mjesto_sastanka) {
        this.id_mjesto_sastanka = id_mjesto_sastanka;
    }

    public Integer getId_tura() {
        return id_tura;
    }

    public void setId_tura(Integer id_tura) {
        this.id_tura = id_tura;
    }

    public void fetchTermin(JSONArray jsonData) {
        for (int i = 0; i < jsonData.length(); i++) {
            //JSONObject currentRow = tours.getJSONObject(i).getInt("id_tura");
            JSONObject currentTermin = null;
            try {
                currentTermin = jsonData.getJSONObject(i);
                Termin tourInstance = new Termin();
                tourInstance.id_termin = currentTermin.getInt("id_termin");
                tourInstance.vrijeme_od = currentTermin.getString("vrijeme_od");
                tourInstance.vrijeme_do = currentTermin.getString("vrijeme_do");
                tourInstance.napomena = currentTermin.getString("napomena");
                tourInstance.id_mjesto_sastanka = currentTermin.getInt("id_mjesto_sastanka");
                tourInstance.id_tura = currentTermin.getInt("id_tura");
                tourInstance.adresa = currentTermin.getString("adresa");
                tourInstance.latitude = currentTermin.getDouble("latitude");
                tourInstance.longitude = currentTermin.getDouble("longitude");
                terminList.add(tourInstance);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
