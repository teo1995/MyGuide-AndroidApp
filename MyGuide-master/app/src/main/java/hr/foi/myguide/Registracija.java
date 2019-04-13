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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import hr.foi.webservice.ZahtjevZaRegistraciju;

public class Registracija extends AppCompatActivity implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE = 1;
    RadioGroup rg;
    RadioButton rb;
    Integer userType;
    ImageView imageView;
    String imageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);
        final EditText etFirstName = (EditText) findViewById(R.id.etFirstNameReg);
        final EditText etLastName = (EditText) findViewById(R.id.etLastNameReg);
        final EditText etUsername = (EditText) findViewById(R.id.etUsernameReg);
        final EditText etEmail = (EditText) findViewById(R.id.etEmailReg);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!checkEmail(etEmail.getText().toString())){
                    etEmail.setError("Incorrect email");
                }
            }
        });
        final EditText etPassword = (EditText) findViewById(R.id.etPasswordReg) ;
        final Button btnRegister = (Button) findViewById(R.id.btnRegister);
        rg = (RadioGroup) findViewById(R.id.ergRadioGroup);
        imageView = (ImageView) findViewById(R.id.imageViewReg);
        imageView.setOnClickListener(this);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName = etFirstName.getText().toString();
                final String lastName = etLastName.getText().toString();
                final String username = etUsername.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

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
                            //boolean success = jsonResponse.getBoolean("success");
                            JSONObject dataJSON = jsonResponse.getJSONObject("data");
                            boolean success = dataJSON.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "You have successfully registered! Check your e-mail for activation link.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Registracija.this, Prijava.class);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Registracija.this);
                                builder.setMessage("Registracija neuspješna")
                                        .setNegativeButton("Ponovno", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                if (TextUtils.isEmpty(etFirstName.getText()) || TextUtils.isEmpty(etLastName.getText()) || TextUtils.isEmpty(etUsername.getText()) || TextUtils.isEmpty(etEmail.getText()) || TextUtils.isEmpty(etPassword.getText())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Registracija.this);
                    builder.setMessage("Please fill out all the fields.")
                            .setNegativeButton("Try again", null)
                            .create()
                            .show();
                }
                else{
                    ZahtjevZaRegistraciju zahtjevZaRegistraciju = new ZahtjevZaRegistraciju(firstName, lastName, email, username, password,imageName, imageString,userType , responseListener);
                    zahtjevZaRegistraciju.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue queue = Volley.newRequestQueue(Registracija.this);
                    queue.add(zahtjevZaRegistraciju);
                }



            }
        });

    }

    public boolean checkEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void rbclick(View v){
        int radioButtonid = rg.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(radioButtonid);

        if(rb.getText().equals("Guide")){
            userType = 1;

        }
        if(rb.getText().equals("Tourist")){
            userType = 2;

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewReg:
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
}
