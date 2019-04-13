package hr.foi.database.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateo on 21.1.2018..
 */

public class MjestoSastanka {
    public int id_mjesto_sastanka;
    public String adresa;
    public String longitude;
    public String latitude;

    public List<MjestoSastanka> mjestoSastankaList = new ArrayList<MjestoSastanka>();

    public MjestoSastanka(){

    }

    public MjestoSastanka(int id_mjesto_sastanka, String adresa, String longitude, String latitude) {
        this.id_mjesto_sastanka = id_mjesto_sastanka;
        this.adresa = adresa;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId_mjesto_sastanka() {
        return id_mjesto_sastanka;
    }

    public void setId_mjesto_sastanka(int id_mjesto_sastanka) {
        this.id_mjesto_sastanka = id_mjesto_sastanka;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void fetchMjestoSastanka(JSONArray jsonData) {
        for (int i = 0; i < jsonData.length(); i++) {
            //JSONObject currentRow = tours.getJSONObject(i).getInt("id_tura");
            JSONObject currentSastanak = null;
            try {
                currentSastanak = jsonData.getJSONObject(i);
                MjestoSastanka mjestoSastankaInstance = new MjestoSastanka();
                mjestoSastankaInstance.id_mjesto_sastanka = currentSastanak.getInt("id_mjesto_sastanka");
                mjestoSastankaInstance.adresa = currentSastanak.getString("adresa");
                mjestoSastankaInstance.longitude = currentSastanak.getString("longitude");
                mjestoSastankaInstance.latitude = currentSastanak.getString("latitude");
                mjestoSastankaList.add(mjestoSastankaInstance);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
