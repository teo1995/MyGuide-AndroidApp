package hr.foi.webservice;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mateo on 29.11.2017..
 */

public class ZahtjevZaRegistraciju extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://yeloo.hr/AiR/MyGuideWebServices/Korisnik/register";
    private Map<String, String> params;

    public ZahtjevZaRegistraciju(String ime, String prezime, String email, String korisnicko_ime, String lozinka,String img_name, String img_path, Integer tip_korisika, Response.Listener<String> listener){
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener,null);
        params = new HashMap<>();
        params.put("ime", ime);
        params.put("prezime",prezime);
        params.put("email", email);
        params.put("korisnicko_ime",korisnicko_ime);
        params.put("lozinka", lozinka);
        params.put("img_name",img_name);
        params.put("img_path", img_path);
        params.put("id_tip_korisnika", tip_korisika + "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
