package hr.foi.myguide;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import hr.foi.database.entities.Korisnik;
import hr.foi.database.entities.Termin;
import hr.foi.database.entities.Tour;
import hr.foi.webservice.ZahtjevZaRezervaciju;

public class AddReservation extends AppCompatActivity {
    EditText etPerson;
    Button btnSubmit;
    private Tour tour;
    private Termin termin;
    private Double fullPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Make reservation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etPerson = (EditText) findViewById(R.id.etPerson);
        etPerson.setError("This field can not be blank");
        etPerson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(checkEtPerson(etPerson.getText().toString())){
                    etPerson.setError("This field can not be blank");
                }
            }
        });
        SessionManager sessionManager = new SessionManager(this);
        final Korisnik loggedUser = sessionManager.retrieveUser();

        tour = getIntent().getParcelableExtra("PARCELABLE_OBJEKT_TURA");
        termin = getIntent().getParcelableExtra("PARCELABLE_OBJEKT");
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Integer person = Integer.parseInt(etPerson.getText().toString());
                fullPrice = tour.getCijena() * person;


                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            //boolean success = jsonResponse.getBoolean("success");
                            JSONObject dataJSON = jsonResponse.getJSONObject("data");
                            boolean success = dataJSON.getBoolean("success");
                            if (success) {
                                        Toast.makeText(getApplicationContext(), "Reservation has been successfully submitted.",
                                        Toast.LENGTH_LONG).show();
                                etPerson.setText("");
                            } else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddReservation.this);
                                builder.setMessage("Error.")
                                        .setNegativeButton("Try again", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ZahtjevZaRezervaciju zahtjevZaRezervaciju = new ZahtjevZaRezervaciju(loggedUser.getId_korisnik(), termin.id_termin, person, fullPrice, responseListener);
                zahtjevZaRezervaciju.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue queue = Volley.newRequestQueue(AddReservation.this);
                queue.add(zahtjevZaRezervaciju);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public boolean checkEtPerson(String person) {
        if(person.isEmpty())
            return true;
        else
            return false;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()== android.R.id.home){
            //Ako je kliknuto na back button, zatvara se trenutna aktivnost i otvara se ona s koje smo došli na ovu
            //Moguće je ovaj princip jer smo na ovu aktivnost došli sa startActivity metodom
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

}
