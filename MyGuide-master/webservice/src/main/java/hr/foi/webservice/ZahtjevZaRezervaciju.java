package hr.foi.webservice;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mateo on 22.1.2018..
 */

public class ZahtjevZaRezervaciju extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://yeloo.hr/AiR/MyGuideWebServices/Rezervacija/add";
    private Map<String, String> params;
    public ZahtjevZaRezervaciju(Integer id_korisnik,Integer id_termin, Integer broj_osoba, Double cijena, Response.Listener<String> listener) {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_korisnik", id_korisnik +"");
        params.put("id_termin", id_termin +"");
        params.put("broj_osoba", broj_osoba +"");
        params.put("ukupna_cijena",cijena +"" );

    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}