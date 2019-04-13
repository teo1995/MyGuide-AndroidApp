package hr.foi.myguide;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import hr.foi.database.entities.Korisnik;
import hr.foi.webservice.ZahtjevZaUredenjeProfila;

public class Profile extends AppCompatActivity implements View.OnClickListener{
    private static final int RESULT_LOAD_IMAGE = 1;
    EditText  etName, etLastname;
    ImageView imageView;
    Button btnUpload;
    String imageName;
    private Korisnik korisnik;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        SessionManager sessionManager = new SessionManager(this);

        korisnik = sessionManager.retrieveUser();
        etName = (EditText) findViewById(R.id.etNameEdit);
        etLastname = (EditText) findViewById(R.id.etLastnameEdit);
        imageView = (ImageView) findViewById(R.id.ivProfileImage);
        etName.setText(korisnik.getIme());
        etLastname.setText(korisnik.getPrezime());

        Picasso.with(this)
                .load(korisnik.getImg_path())
                .fit()
                .centerInside()
                .into(imageView);

        imageView.setOnClickListener(this);
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
        getMenuInflater().inflate(R.menu.activity_save, menu);
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivProfileImage:
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
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null
            File f = new File(picturePath);
            imageName = f.getName();
            cursor.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Integer id = item.getItemId();
        if(id == R.id.save){
            if(!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etLastname.getText().toString())){
                final String name = etName.getText().toString();
                final String lastname = etLastname.getText().toString();
                final Integer idKorisnik = korisnik.getId_korisnik();

                final Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONObject dataJSON = jsonResponse.getJSONObject("data");
                            boolean success = dataJSON.getBoolean("success");
                            if (success) {
                                //imageView.setImageResource(android.R.color.transparent);
                                korisnik.setIme(dataJSON.getString("ime"));
                                korisnik.setPrezime(dataJSON.getString("prezime"));
                                korisnik.setEmail(dataJSON.getString("email"));
                                korisnik.setImg_name(dataJSON.getString("img_name"));
                                korisnik.setImg_path(dataJSON.getString("img_path"));

                                SessionManager sessionManager = new SessionManager(Profile.this);
                                sessionManager.createLoginSession(korisnik);

                                etName.setText(dataJSON.getString("ime"));
                                etLastname.setText(dataJSON.getString("prezime"));
                                Picasso.with(Profile.this)
                                        .load(korisnik.getImg_path())
                                        .fit()
                                        .centerInside()
                                        .into(imageView);

                                Toast.makeText(getApplicationContext(), "Profile has been successfully updated.",
                                        Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(Profile.this, Profile.class);
                                Profile.this.startActivity(intent);
                                finish();
                            } else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
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
                ZahtjevZaUredenjeProfila zahtjevZaUredenjeProfila = new ZahtjevZaUredenjeProfila(idKorisnik,name, lastname, imageName, imageString,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Profile.this);
                queue.add(zahtjevZaUredenjeProfila);
            }else if(TextUtils.isEmpty(etName.getText().toString()) || TextUtils.isEmpty(etLastname.getText().toString())){
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                builder.setMessage("You must fill in all the fields.")
                        .setNegativeButton("Try again", null)
                        .create()
                        .show();
        }}
        if(item.getItemId()== android.R.id.home){
            Intent intent = new Intent(Profile.this, PocetnaStranica.class);
            Profile.this.startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
