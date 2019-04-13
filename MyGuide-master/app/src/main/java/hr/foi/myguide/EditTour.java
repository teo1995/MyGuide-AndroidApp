package hr.foi.myguide;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.Inet4Address;

import hr.foi.database.entities.Korisnik;
import hr.foi.database.entities.Tour;
import hr.foi.webservice.ZahtjevZaDodavanjeTure;
import hr.foi.webservice.ZahtjevZaUredjivanjeTure;

public class EditTour extends AppCompatActivity implements View.OnClickListener{
    private static final int RESULT_LOAD_IMAGE = 1;
    EditText etTourName, etTourDescription, etTourPrice;
    ImageView imageView;
    Button btnUpload;
    String imageName;
    private Tour tour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit tour");
        SessionManager sessionManager = new SessionManager(this);
        final Korisnik loggedUser = sessionManager.retrieveUser();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnUpload.setText("Save changes");

        Intent i = getIntent();
        tour = getIntent().getParcelableExtra("PARCELABLE_OBJEKT");


        etTourName = (EditText) findViewById(R.id.etTourName);
        etTourDescription = (EditText) findViewById(R.id.etTourDesc);
        etTourPrice = (EditText) findViewById(R.id.etTourPrice);
        imageView = (ImageView) findViewById(R.id.imageViewUpload);
        btnUpload = (Button) findViewById(R.id.btnUpload);

        etTourName.setText(tour.getNaziv());
        etTourDescription.setText(tour.getOpis());
        etTourPrice.setText(tour.getCijena().toString());
        //imageView.setText(get);



        imageView.setOnClickListener(this);
        //btnUpload.setOnClickListener(this);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( !TextUtils.isEmpty(etTourName.getText().toString()) && !TextUtils.isEmpty(etTourDescription.getText().toString()) && !TextUtils.isEmpty(etTourPrice.getText().toString())){

                    final Integer idTura = tour.getId_tura();
                    final String tourName = etTourName.getText().toString();
                    final String tourDescription = etTourDescription.getText().toString();
                    final Double tourPrice = Double.valueOf(etTourPrice.getText().toString());
                    final Integer idKorisnik = loggedUser.getId_korisnik();

                    final Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String imageString = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

                    //Tour tourInstance = new Tour(tourName,tourDescription,tourPrice, imageName, imageString, idKorisnik);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonResponse = new JSONObject(response);
                                //boolean success = jsonResponse.getBoolean("success");
                                JSONObject dataJSON = jsonResponse.getJSONObject("data");
                                boolean success = dataJSON.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "Tour has been successfully submitted.",
                                            Toast.LENGTH_LONG).show();
                                    etTourName.setText("");
                                    etTourPrice.setText("");
                                    etTourDescription.setText("");

                                    imageView.setImageResource(android.R.color.transparent);

                                    Intent intent = new Intent(EditTour.this, MyTour.class);
                                    EditTour.this.startActivity(intent);
                                    finish();

                                } else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditTour.this);
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
                    ZahtjevZaUredjivanjeTure zahtjevZaUredjivanjeTure = new ZahtjevZaUredjivanjeTure(idTura, tourName, tourDescription, tourPrice, imageName, imageString, idKorisnik, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(EditTour.this);
                    queue.add(zahtjevZaUredjivanjeTure);


                }else if(TextUtils.isEmpty(etTourName.getText().toString()) || TextUtils.isEmpty(etTourDescription.getText().toString()) || TextUtils.isEmpty(etTourPrice.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditTour.this);
                    builder.setMessage("You must fill in all the fields.")
                            .setNegativeButton("Try again", null)
                            .create()
                            .show();
                }
            }
        });
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewUpload:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);//metoda koja se poziva kada se odabere slika iz galerije
        if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK && data!=null){
            Uri selectedImage = data.getData();
            imageView.setImageURI(selectedImage);

            String[] projection = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, projection, null, null, null);
            cursor.moveToFirst();

            //Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null

            File f = new File(picturePath);

            imageName = f.getName();
            cursor.close();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            Intent intent = new Intent(EditTour.this, MyTour.class);
            EditTour.this.startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
