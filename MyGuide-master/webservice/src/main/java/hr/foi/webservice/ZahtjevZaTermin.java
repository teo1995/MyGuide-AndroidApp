package hr.foi.webservice;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mateo on 21.1.2018..
 */

public class ZahtjevZaTermin extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://yeloo.hr/AiR/MyGuideWebServices/Termin/fetchTerminByTour";
    private Map<String, String> params;
    public ZahtjevZaTermin(Integer id_tura,Response.Listener<String> listener) {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_tura", id_tura +"");
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
