package hr.foi.myguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hr.foi.database.entities.Korisnik;
import hr.foi.database.entities.Tour;
import hr.foi.myguide.Adapters.TourAdapter;
import hr.foi.myguide.Adapters.TourAdapterListener;
import hr.foi.webservice.ZahtjevZaPromjeneTure;

public class MyTour extends AppCompatActivity implements TourAdapterListener {
    RecyclerView recyclerView;
    List<Tour> listTour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocetna_stranica);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My tours");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SessionManager sessionManager = new SessionManager(this);

        final Korisnik loggedUser = sessionManager.retrieveUser();

        if (loggedUser.getId_tip_korisnika() == 2) {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.INVISIBLE);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listTour = new ArrayList<>();
        //creating recyclerview adapter
        final TourAdapter adapter = new TourAdapter(this, listTour);
        //setting adapter to recyclerview

        recyclerView.setAdapter(adapter);
        adapter.setListener(this);
        Integer idUser = loggedUser.getId_korisnik();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject dataJSON = jsonResponse.getJSONObject("data");
                    boolean success = dataJSON.getBoolean("success");
                    if (success) {
                        JSONArray tours = dataJSON.getJSONArray("rows");
                        Tour tourInstance = new Tour();
                        tourInstance.fetchTours(tours, "All123", 1.0);
                        adapter.updateToursList(tourInstance.toursList);


                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ZahtjevZaPromjeneTure zahtjevZaPromjeneTure = new ZahtjevZaPromjeneTure(idUser,responseListener);
        RequestQueue queue = Volley.newRequestQueue(MyTour.this);
        queue.add(zahtjevZaPromjeneTure);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyTour.this, AddTour.class);
                MyTour.this.startActivity(intent);
            }
        });

//        ImageView img = (ImageView) findViewById(R.id.imageView);
//        img.setLongClickable(true);
//        registerForContextMenu(img);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.activity_menu,menu);
    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.edit:
//                Toast.makeText(this, item.toString(), Toast.LENGTH_LONG).show();
//                break;
//            case R.id.delete:
//                Toast.makeText(this, item.toString(), Toast.LENGTH_LONG).show();
//
//                break;
//        }
//        return super.onContextItemSelected(item);
//
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            Intent intent = new Intent(MyTour.this, PocetnaStranica.class);
            MyTour.this.startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(int position) {
        //Toast.makeText(this, "item na poziciji "+ position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, TourDetails.class);
        intent.putExtra("caller", "MyTour");
        intent.putExtra("PARCELABLE_OBJEKT", listTour.get(position));
        startActivity(intent);
    }
}