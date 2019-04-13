package hr.foi.myguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import hr.foi.database.entities.Korisnik;
import hr.foi.database.entities.Tour;
import hr.foi.webservice.ZahtjevZaBrisanjeTure;

public class TourDetails extends AppCompatActivity {


    TextView textViewTitle, textViewShortDesc, textViewPrice, textViewTourId, textViewName, textViewEmail;
    Button btnAddSchedule, btnViewSchedules;
    ImageView imgView;
    private Tour tour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_tour_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tour details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        SessionManager sessionManager = new SessionManager(this);
        final Korisnik loggedUser = sessionManager.retrieveUser();

        if (loggedUser.getId_tip_korisnika() == 2) {
            Button btnAddSchedule = (Button) findViewById(R.id.btnAddSchedule);
            btnAddSchedule.setVisibility(View.GONE);
        }

        tour = getIntent().getParcelableExtra("PARCELABLE_OBJEKT");
        textViewTourId  = (TextView) findViewById(R.id.textViewTourId);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewShortDesc = (TextView) findViewById(R.id.textViewShortDesc);
        textViewPrice = (TextView)  findViewById(R.id.textViewPrice);
        imgView = (ImageView)  findViewById(R.id.imageView);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewTourId.setText(tour.getId_tura().toString());
        textViewTitle.setText(tour.getNaziv());
        textViewShortDesc.setText(tour.getOpis());
        textViewName.setText(tour.getIme_vodica()+ " " + tour.getPrezime_vodica());
        textViewEmail.setText(tour.getEmail_vodica());
        textViewPrice.setText(String.format("%.2f", tour.getCijena()) + "kn");
        Picasso.with(this)
                .load(tour.getImg_path())
                .into(imgView);
        btnAddSchedule = (Button) findViewById(R.id.btnAddSchedule);
        btnViewSchedules = (Button) findViewById(R.id.btnViewSchedules);
        btnAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TourDetails.this, TourSchedule.class);
                intent.putExtra("PARCELABLE_OBJEKT", tour);

                startActivity(intent);
            }
        });
        btnViewSchedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourDetails.this, ViewSchedule.class );
                intent.putExtra("PARCELABLE_OBJEKT", tour);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (getIntent().hasExtra("caller")==false) {
            //caller je postavljen samo kad dolazimo sa MyTour pa se ovisno o tome prikazuju gumbovi za edit i delete
            menu.findItem(R.id.edit).setVisible(false);
            menu.findItem(R.id.delete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Integer id = item.getItemId();
        Integer idTour = tour.getId_tura();
        if(id == R.id.delete){
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONObject dataJSON = jsonResponse.getJSONObject("data");
                        boolean success = dataJSON.getBoolean("success");
                        if (success) {

                            Toast.makeText(getApplicationContext(), "Tour has been successfully deleted.",
                                    Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(TourDetails.this, MyTour.class);
                            TourDetails.this.startActivity(intent);
                            finish();

                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            ZahtjevZaBrisanjeTure zahtjevZaBrisanjeTure = new ZahtjevZaBrisanjeTure(idTour,responseListener);
            RequestQueue queue = Volley.newRequestQueue(TourDetails.this);
            queue.add(zahtjevZaBrisanjeTure);
        }
        if(id == R.id.edit){
            Intent intent = new Intent(this, EditTour.class);
            intent.putExtra("PARCELABLE_OBJEKT", tour);
            startActivity(intent);
        }
        else if(item.getItemId()== android.R.id.home){
            //Ako je kliknuto na back button, zatvara se trenutna aktivnost i otvara se ona s koje smo došli na ovu
            //Moguće je ovaj princip jer smo na ovu aktivnost došli sa startActivity metodom
            finish();
        }

        return super.onOptionsItemSelected(item);

    }



}
