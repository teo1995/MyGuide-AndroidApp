package hr.foi.webservice;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mateo on 21.1.2018..
 */

public class ZahtjevZaDodavanjeTermina extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://yeloo.hr/AiR/MyGuideWebServices/Termin/add";
    private Map<String, String> params;

    public ZahtjevZaDodavanjeTermina(String vrijeme_od, String vrijeme_do, String napomena, Integer id_mjesto_sastanka,Integer id_tura ,Response.Listener<String> listener){
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener,null);
        params = new HashMap<>();
        params.put("vrijeme_od",vrijeme_od);
        params.put("vrijeme_do", vrijeme_do +"");
        params.put("napomena",napomena);
        params.put("id_mjesto_sastanka", id_mjesto_sastanka +"");
        params.put("id_tura", id_tura + "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
