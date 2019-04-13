package hr.foi.myguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hr.foi.database.entities.Termin;
import hr.foi.database.entities.Tour;
import hr.foi.myguide.Adapters.TerminAdapter;
import hr.foi.myguide.Adapters.TerminAdapterListener;
import hr.foi.myguide.Adapters.TourAdapter;
import hr.foi.webservice.ZahtjevZaTermin;
import hr.foi.webservice.ZahtjevZaTuru;

public class ViewSchedule extends AppCompatActivity implements TerminAdapterListener {
    RecyclerView recyclerView;
    List<Termin> listTermin;
    private Tour tour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tour schedules");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTourSchedule);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tour = getIntent().getParcelableExtra("PARCELABLE_OBJEKT");
        listTermin = new ArrayList<>();

        //creating recyclerview adapter
        final TerminAdapter terminAdapter = new TerminAdapter(this, listTermin);

        //setting adapter to recyclerview
        recyclerView.setAdapter(terminAdapter);

        terminAdapter.setListener(this);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject dataJSON = jsonResponse.getJSONObject("data");
                    boolean success = dataJSON.getBoolean("success");
                    //Integer idUserType = loggedUser.getId_tip_korisnika();
                    if (success) {
                        JSONArray termin = dataJSON.getJSONArray("rows");
                        Termin terminInstance = new Termin();
                        terminInstance.fetchTermin(termin);
                        terminAdapter.updateTerminsList(terminInstance.terminList);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ZahtjevZaTermin zahtjevZaTermin = new ZahtjevZaTermin(tour.getId_tura(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(ViewSchedule.this);
        queue.add(zahtjevZaTermin);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
        //Ako je kliknuto na back button, zatvara se trenutna aktivnost i otvara se ona s koje smo došli na ovu
        //Moguće je ovaj princip jer smo na ovu aktivnost došli sa startActivity metodom
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(int position) {
        Intent intent = new Intent(this, TourScheduleDetail.class);
        intent.putExtra("PARCELABLE_OBJEKT", listTermin.get(position));
        intent.putExtra("PARCELABLE_OBJEKT_TURA", tour);////dodano nakon Github commita
        intent.putExtra("typeOfDisplay","google");
        startActivity(intent);
    }

    @Override
    public void itemLongClicked(int position) {
        Intent intent = new Intent(this, TourScheduleDetail.class);
        intent.putExtra("PARCELABLE_OBJEKT", listTermin.get(position));
        intent.putExtra("PARCELABLE_OBJEKT_TURA", tour);////dodano nakon Github commita
        intent.putExtra("typeOfDisplay","basic");
        startActivity(intent);
    }
}
