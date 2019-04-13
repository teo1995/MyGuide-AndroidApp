package hr.foi.myguide;

import android.content.Context;
import android.content.DialogInterface;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import hr.foi.database.entities.MjestoSastanka;
import hr.foi.database.entities.Termin;
import hr.foi.database.entities.Tour;
import hr.foi.webservice.ZahtjevZaDodavanjeTermina;
import hr.foi.webservice.ZahtjevZaDodavanjeTure;
import hr.foi.webservice.ZahtjevZaMjestoSastanka;
import hr.foi.webservice.ZahtjevZaTermin;

public class TourSchedule extends AppCompatActivity {
    Button btnSubmitSchedule, btnDateTimeFrom, btnDateTimeTo;
    EditText etDateTimeFrom;
    EditText etDateTimeTo, etNote;
    Spinner locationSpinner;
    private Tour tour;
    MjestoSastanka mjestoSastankaInstance;
    List<String> addresses;
    public String dateFrom;
    public String dateTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_tour_schedule);
        etDateTimeFrom = (EditText) findViewById(R.id.selectedDateTimeFrom);
        etDateTimeTo = (EditText) findViewById(R.id.selectedDateTimeTo);
        // Spinner element
        locationSpinner = (Spinner) findViewById(R.id.locationDropdown);
        etNote = (EditText) findViewById(R.id.noteEditText);
        btnSubmitSchedule = (Button) findViewById(R.id.btnSubmit) ;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tour schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btnDateTimeFrom = (Button) findViewById(R.id.btnDateTimeFrom);
        btnDateTimeTo = (Button) findViewById(R.id.btnDateTimeTo);

        tour = getIntent().getParcelableExtra("PARCELABLE_OBJEKT");
        btnDateTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateAndTimeDialog(TourSchedule.this, etDateTimeFrom, "dateFrom");
            }
        });

        btnDateTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateAndTimeDialog(TourSchedule.this, etDateTimeTo, "dateTo");
            }
        });
        btnSubmitSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String napomena = etNote.getText().toString();
                String spinnerSelectedValue = locationSpinner.getSelectedItem().toString();

                Integer selectedValueIndex = addresses.indexOf(spinnerSelectedValue);

                final Integer id_mjesto_sastanka = mjestoSastankaInstance.mjestoSastankaList.get(selectedValueIndex).getId_mjesto_sastanka();

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            //boolean success = jsonResponse.getBoolean("success");
                            JSONObject dataJSON = jsonResponse.getJSONObject("data");
                            boolean success = dataJSON.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "Termin has been successfully submitted.",
                                        Toast.LENGTH_LONG).show();
                                        etDateTimeFrom.setText("");
                                        etDateTimeTo.setText("");
                                        etNote.setText("");
                            } else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(TourSchedule.this);
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

                if (TextUtils.isEmpty(etDateTimeFrom.getText()) || TextUtils.isEmpty(etDateTimeTo.getText()) || TextUtils.isEmpty(etNote.getText())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TourSchedule.this);
                    builder.setMessage("Please fill out all the fields.")
                            .setNegativeButton("Try again", null)
                            .create()
                            .show();
                }
                else {
                    ZahtjevZaDodavanjeTermina zahtjevZaDodavanjeTermina = new ZahtjevZaDodavanjeTermina(dateFrom, dateTo, napomena, id_mjesto_sastanka, tour.getId_tura(), responseListener);
                    RequestQueue queueAddTermin = Volley.newRequestQueue(TourSchedule.this);
                    queueAddTermin.add(zahtjevZaDodavanjeTermina);
                }
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
//        Response.Listener<String> responseListenerTermin = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonResponse = new JSONObject(response);
//                    JSONObject dataJSON = jsonResponse.getJSONObject("data");
//                    boolean success = dataJSON.getBoolean("success");
//                    // Integer idUserType = loggedUser.getId_tip_korisnika();
//                    if (success) {
//                        JSONArray termin = dataJSON.getJSONArray("rows");
//                        Termin terminInstance = new Termin();
//                        terminInstance.fetchTermin(termin);
//
//                        //terminInstance.terminList.get(0).getVrijeme_od();
//                        //adapter.updateToursList(terminInstance.terminList);
//                    } else {
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        ZahtjevZaTermin zahtjevZaTermin = new ZahtjevZaTermin( 1,responseListenerTermin);
//        RequestQueue queueTermin = Volley.newRequestQueue(TourSchedule.this);
//        queueTermin.add(zahtjevZaTermin);


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject dataJSON = jsonResponse.getJSONObject("data");
                    boolean success = dataJSON.getBoolean("success");
                    // Integer idUserType = loggedUser.getId_tip_korisnika();
                    if (success) {
                        JSONArray mjestoSastanka = dataJSON.getJSONArray("rows");
                        mjestoSastankaInstance = new MjestoSastanka();
                        mjestoSastankaInstance.fetchMjestoSastanka(mjestoSastanka);

                        // Spinner Drop down elements
                        addresses = new ArrayList<>();
                        for (int i=0;i<mjestoSastankaInstance.mjestoSastankaList.size();i++) {
                            addresses.add(mjestoSastankaInstance.mjestoSastankaList.get(i).getAdresa());
                        }


                        // Creating adapter for spinner
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(TourSchedule.this,
                                android.R.layout.simple_spinner_item, addresses);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        locationSpinner.setAdapter(dataAdapter);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ZahtjevZaMjestoSastanka zahtjevZaMjestoSastanka = new ZahtjevZaMjestoSastanka(responseListener);
        RequestQueue queue = Volley.newRequestQueue(TourSchedule.this);
        queue.add(zahtjevZaMjestoSastanka);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Integer id = item.getItemId();
        // idTour = tour.getId_tura();
//        if(id == R.id.delete){
//            Response.Listener<String> responseListener = new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jsonResponse = new JSONObject(response);
//                        JSONObject dataJSON = jsonResponse.getJSONObject("data");
//                        boolean success = dataJSON.getBoolean("success");
//                        if (success) {
//
//                            Toast.makeText(getApplicationContext(), "Tour has been successfully deleted.",
//                                    Toast.LENGTH_LONG).show();
//
//                            Intent intent = new Intent(TourDetails.this, MyTour.class);
//                            TourDetails.this.startActivity(intent);
//                            finish();
//
//                        } else {
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            ZahtjevZaBrisanjeTure zahtjevZaBrisanjeTure = new ZahtjevZaBrisanjeTure(idTour,responseListener);
//            RequestQueue queue = Volley.newRequestQueue(TourDetails.this);
//            queue.add(zahtjevZaBrisanjeTure);
//        }
//        if(id == R.id.edit){
//            Intent intent = new Intent(this, EditTour.class);
//            intent.putExtra("PARCELABLE_OBJEKT", tour);
//            startActivity(intent);
//        }
        /*else*/ if(item.getItemId()== android.R.id.home){
            //Ako je kliknuto na back button, zatvara se trenutna aktivnost i otvara se ona s koje smo došli na ovu
            //Moguće je ovaj princip jer smo na ovu aktivnost došli sa startActivity metodom
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    private void showDateAndTimeDialog(final Context context, final EditText dateEditText, final String datumType) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_date_time, null, false);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                return viewPager.getChildAt(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Date";
                    case 1:
                        return "Time";
                }
                return super.getPageTitle(position);
            }

            @Override
            public int getCount() {
                return viewPager.getChildCount();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        final TabLayout layoutTab = (TabLayout) view.findViewById(R.id.layout_tab);
        layoutTab.setupWithViewPager(viewPager);

        final AlertDialog dateAndTimeDialog = new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null)
                .create();
        dateAndTimeDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {

                /*
                    We override the default BUTTON_POSITIVE OnClickListener to prevent the dialog
                    from dismissing when the user presses OK
                 */

                dateAndTimeDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (viewPager.getCurrentItem() == 0) {
                                    viewPager.setCurrentItem(1);
                                    return;
                                }

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.MONTH, datePicker.getMonth());
                                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                                calendar.set(Calendar.YEAR, datePicker.getYear());

                                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                                // Bitmask used to determine timestamp format
                                int displayMask = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR;


                                // Resultant timestamp
                                String timestamp = DateUtils.formatDateTime(context, calendar.getTimeInMillis(), displayMask);

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d H:m:s");

                                if (datumType == "dateFrom") {
                                    dateFrom = sdf.format(calendar.getTimeInMillis());
                                }
                                else {
                                    dateTo = sdf.format(calendar.getTimeInMillis());
                                }

                                Log.d(TourSchedule.class.getCanonicalName(),
                                        "Timestamp: " + timestamp);

                                dateEditText.setText(timestamp);

                                // We must dismiss the dialog as we have overriden the default behavior

                                dialog.dismiss();

                            }
                        });
            }
        });

        dateAndTimeDialog.show();
    }

}
