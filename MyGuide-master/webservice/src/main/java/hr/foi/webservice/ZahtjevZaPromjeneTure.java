package hr.foi.webservice;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mateo on 9.12.2017..
 */

public class ZahtjevZaPromjeneTure extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://yeloo.hr/AiR/MyGuideWebServices/Tura/fetchByGuide";
    private Map<String, String> params;

    public ZahtjevZaPromjeneTure(Integer id_korisnik,Response.Listener<String> listener) {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_korisnik", id_korisnik + "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
