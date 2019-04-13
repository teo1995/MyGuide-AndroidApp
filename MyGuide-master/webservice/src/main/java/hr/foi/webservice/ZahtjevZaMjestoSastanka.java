package hr.foi.webservice;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by Mateo on 21.1.2018..
 */

public class ZahtjevZaMjestoSastanka  extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://yeloo.hr/AiR/MyGuideWebServices/MjestoSastanka/fetchMeetingPoints";
    public ZahtjevZaMjestoSastanka(Response.Listener<String> listener) {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
    }
}
