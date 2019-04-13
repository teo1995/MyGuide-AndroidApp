package hr.foi.myguide;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

import hr.foi.core.tourschedulelocation.AppModule;
import hr.foi.core.tourschedulelocation.TourDisplayModule;
import hr.foi.database.entities.Termin;
import hr.foi.database.entities.Tour;
import hr.foi.tourschedulebasicdisplay.BasicDisplayTour;
import hr.foi.tourschedulegoogledisplay.GoogleDisplayTour;



public class TourScheduleDetail extends AppCompatActivity implements AppModule {
    TextView tourStartTextView, tourEndTextView, meetingPointTextView, noteTextView, terminIdTextView;
    private Termin termin;
    Button btnBook;

    private Tour tour;

    SupportMapFragment mapFragment;

    TourDisplayModule tourDisplayModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_tour_schedule_detail);

        //Defining toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tour schedule details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent i = getIntent();
        termin = getIntent().getParcelableExtra("PARCELABLE_OBJEKT");
        tour = getIntent().getParcelableExtra("PARCELABLE_OBJEKT_TURA");//dodano nakon Github commita
        String typeOfDisplay = getIntent().getStringExtra("typeOfDisplay");
        if(typeOfDisplay.equals("google")){
            tourDisplayModule = new GoogleDisplayTour();
        }
        else{
            tourDisplayModule = new BasicDisplayTour();
            //kreiraš drugu instancu modula
        }

        tourDisplayModule.setAppModule(this);
        tourDisplayModule.showTour(termin.getLatitude(), termin.getLongitude(), termin.getAdresa());

        //Getting layout element
        terminIdTextView = (TextView) findViewById(R.id.terminIdTextView);
        tourStartTextView = (TextView) findViewById(R.id.tourStartTextView);
        tourEndTextView = (TextView) findViewById(R.id.tourEndTextView);
        meetingPointTextView = (TextView) findViewById(R.id.meetingPointTextView);
        noteTextView = (TextView) findViewById(R.id.noteTextView);

        //Setting text to TextViews
        terminIdTextView.setText(termin.getId_termin().toString());
        tourStartTextView.setText(termin.getVrijeme_od());
        tourEndTextView.setText(termin.getVrijeme_do());
        meetingPointTextView.setText(termin.getAdresa());
        noteTextView.setText(termin.getNapomena());



        btnBook = (Button) findViewById(R.id.btnBookSchedule) ;

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(TourScheduleDetail.this, AddReservation.class);
                    intent.putExtra("PARCELABLE_OBJEKT", termin);
                    intent.putExtra("PARCELABLE_OBJEKT_TURA", tour);
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

        //Event listener da google mapa ima prioritet kod dodira ekrana
        final ScrollView mainScrollView = (ScrollView) findViewById(R.id.tourScheduleDetailScrollView);
        final RelativeLayout mapViewLayout = (RelativeLayout) findViewById(R.id.gMapsLayout);

        mapViewLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
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
    public void displayTourFragment(Fragment fragment) {
        //FrameLayout fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
        //fragmentContainer.removeAllViewsInLayout();
        //transaction manager


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment)
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
//        fragment.onAttach(this);
        this.tourDisplayModule.fragmentShown();
    }

    @Override
    public void removeDisplayTourFragment(Fragment fragment) {
        this.tourDisplayModule.fragmentRemoved();
    }
}
