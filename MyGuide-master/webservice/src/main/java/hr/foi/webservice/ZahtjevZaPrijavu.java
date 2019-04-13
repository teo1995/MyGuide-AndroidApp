package hr.foi.webservice;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mateo on 29.11.2017..
 */

public class ZahtjevZaPrijavu extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://yeloo.hr/AiR/MyGuideWebServices/Korisnik/login";
    private Map<String, String> params;

    public ZahtjevZaPrijavu(String korisnicko_ime, String lozinka, Response.Listener<String> listener) {
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("korisnicko_ime", korisnicko_ime);
        params.put("lozinka", lozinka);
    }


    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
