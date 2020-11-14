package com.digibarber.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.CustomClasses.Utility;
import com.digibarber.app.CustomMediaPicker.CMediaPicker;
import com.digibarber.app.Interfaces.ApiCallback;
import com.digibarber.app.R;
import com.digibarber.app.apicalls.ApiClient;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChangePrivateInformationActivity extends BaseActivity {

    SharedPreferences prefs;
    EditText fname, lname, email, phone;
    TextView bank_details;

    ImageView updateProfilePicture;
    CircleImageView client_profile_image;
    ImageView tv_next_account_detail, iv_cross1, iv_cross2, iv_cross3;

    private final static int REQUEST_CAMERA = 1;
    private final static int SELECT_FILE = 2;
    private final static int SELECT_CARD_DEATIL = 3;
    private final static int SELECT_IMAGE = 4;
    private final static int SELECT_ADDRESS = 5;
    public final static int SELECT_PHONE_VERIFICATION = 5242;
    public final static int STRIPE_CONNECT = 6252;


    String userChoosenTask = "", final_image_path = "";
    private String Id;
    private EditText workplace, address;
    private ImageView tv_next_address;

    TextView tv_text_error_f_name, tv_text_error_sur_name, tv_text_error_workplace;
    TextView tv_text_error_email, tv_text_error_phone, tv_text_error_address, text_connected, text_dashboard;

    ImageView iv_line_f_name, iv_line_su_name, iv_line_email;
    ImageView iv_line_phone, iv_line_workplace, iv_line_address;
    RelativeLayout btn_connect_stripe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_private_information_activity);

        prefs = getSharedPreferences("Digibarber", MODE_PRIVATE);

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        workplace = findViewById(R.id.workplace);
        iv_cross1 = findViewById(R.id.iv_cross1);
        iv_cross2 = findViewById(R.id.iv_cross2);
        iv_cross3 = findViewById(R.id.iv_cross3);
        btn_connect_stripe = findViewById(R.id.btn_connect_stripe);

        address = findViewById(R.id.address);

        //   iv_next_upload_center = (ImageView) findViewById(R.id.iv_next_upload_center);
        updateProfilePicture = findViewById(R.id.updateProfilePicture);
        client_profile_image = findViewById(R.id.client_profile_image);
        tv_next_address = findViewById(R.id.tv_next_address);

        tv_text_error_f_name = findViewById(R.id.tv_text_error_f_name);
        tv_text_error_sur_name = findViewById(R.id.tv_text_error_sur_name);
        tv_text_error_email = findViewById(R.id.tv_text_error_email);
        tv_text_error_phone = findViewById(R.id.tv_text_error_phone);
        tv_text_error_workplace = findViewById(R.id.tv_text_error_workplace);
        tv_text_error_address = findViewById(R.id.tv_text_error_address);
        text_connected = findViewById(R.id.textConnected);
        text_dashboard = findViewById(R.id.view_dashboard);


        iv_line_f_name = findViewById(R.id.iv_line_f_name);
        iv_line_su_name = findViewById(R.id.iv_line_su_name);
        iv_line_email = findViewById(R.id.iv_line_email);
        iv_line_phone = findViewById(R.id.iv_line_phone);
        iv_line_workplace = findViewById(R.id.iv_line_workplace);
        iv_line_address = findViewById(R.id.iv_line_address);

        fname.addTextChangedListener(new CustomTextWatcher(fname, tv_text_error_f_name, iv_line_f_name));
        lname.addTextChangedListener(new CustomTextWatcher(lname, tv_text_error_sur_name, iv_line_su_name));
        email.addTextChangedListener(new CustomTextWatcher(email, tv_text_error_email, iv_line_email));
        phone.addTextChangedListener(new CustomTextWatcher(phone, tv_text_error_phone, iv_line_phone));
        workplace.addTextChangedListener(new CustomTextWatcher(workplace, tv_text_error_workplace, iv_line_workplace));
        address.addTextChangedListener(new CustomTextWatcher(address, tv_text_error_address, iv_line_address));

        iv_cross1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.setText("");
            }
        });


        iv_cross2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone.setText("+44");
            }
        });

        iv_cross3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workplace.setText("");
            }
        });

        btn_connect_stripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean connected = prefs.getBoolean(Constants.STRIPE_CONNECTED, false);
                if (connected) {
                    startActivity(new Intent(ChangePrivateInformationActivity.this, StripeDashboardActivity.class));
                } else {
                    Intent intent = new Intent(ChangePrivateInformationActivity.this, StripeConnectActivity.class);
                    startActivityForResult(intent, STRIPE_CONNECT);
                }

            }
        });
        updateProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePrivateInformationActivity.this, CMediaPicker.class);
                intent.putExtra("max", 1);
                startActivityForResult(intent, 5);
            }
        });

        tv_next_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ChangePrivateInformationActivity.this, AddWorkPlaceLocationActivity.class);
                it.putExtra("workplace", prefs.getString(Constants.KEY_WORKPLACE, ""));
                it.putExtra("address", prefs.getString(Constants.KEY_ADDRESS, ""));
                it.putExtra("From", "EditPrfoile");
                startActivity(it);
                finish();
            }
        });
        this.loadProfileData();
    }

    private class CustomTextWatcher implements TextWatcher {
        private EditText mEditText;
        private TextView tv_errror;
        ImageView ivLine;

        public CustomTextWatcher(EditText etOpenClsoe, TextView tv_errror, ImageView ivLine) {
            mEditText = etOpenClsoe;
            this.tv_errror = tv_errror;
            this.ivLine = ivLine;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (mEditText.getText().toString().length() > 0 && tv_errror.getVisibility() == View.VISIBLE) {
                tv_errror.setVisibility(View.INVISIBLE);

                if (mEditText.getId() == R.id.first_name || mEditText.getId() == R.id.last_name) {
                    ivLine.setBackgroundResource(R.mipmap.first_name_line);
                } else {
                    ivLine.setBackgroundResource(R.mipmap.line_signup_grey);
                }
            }
        }
    }


    private void loadValues() {

        //KEY_ACCOUNT_NUMBER
        String bankAccount = prefs.getString(Constants.KEY_ACCOUNT_NUMBER, "");
        Id = prefs.getString(Constants.KEY_BANK_DETAIL, "");

        String first_name = prefs.getString("first_name", "");
        if (first_name != null && !first_name.equalsIgnoreCase("") && !first_name.equalsIgnoreCase("null")) {
            fname.setText(first_name);
        } else {
            fname.setText(" ");
        }

        String LastName = prefs.getString("last_name", "");
        if (LastName != null && !LastName.equalsIgnoreCase("") && !LastName.equalsIgnoreCase("null")) {
            lname.setText(LastName);
        } else {
            lname.setText(" ");
        }


        email.setText(prefs.getString("email", ""));
        phone.setText(prefs.getString("phone", ""));

        String WorkPlace = prefs.getString(Constants.KEY_WORKPLACE, "");
        if (WorkPlace != null && !WorkPlace.equalsIgnoreCase("") && !WorkPlace.equalsIgnoreCase("null")) {
            workplace.setText(WorkPlace);
        } else {
            workplace.setText(" ");
        }

        String Address = prefs.getString(Constants.KEY_ADDRESS, "");
        if (Address != null && !Address.equalsIgnoreCase("") && !Address.equalsIgnoreCase("null")) {
            address.setText(Address);
        } else {
            address.setText(" ");
        }

        boolean connected = prefs.getBoolean(Constants.STRIPE_CONNECTED, false);
        if (connected) {
            text_connected.setVisibility(View.VISIBLE);
            text_dashboard.setText("View dashboard");
        } else {
            text_connected.setVisibility(View.GONE);
            text_dashboard.setText("Connect to Stripe");
        }

        if (!prefs.getString("profile_image", "").equalsIgnoreCase("")) {
            Log.i("test_test", "1 " + prefs.getString("profile_image", ""));
            Picasso.get()
                    .load(prefs.getString("profile_image", ""))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).fit().into(client_profile_image);
        }
    }

    /* Camera Intent */
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    /* Gallery Intent*/
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else if (requestCode == 1) {

                }
                break;
        }
    }

    private void callCropMethod(Uri sourceUri) {
        UCrop.Options options = new UCrop.Options();
        options.withAspectRatio(1, 1);
        options.setStatusBarColor(Color.parseColor("#ffffff"));
        options.setToolbarColor(Color.parseColor("#4D31353A"));
        options.setActiveWidgetColor(Color.parseColor("#118FEB"));
        UCrop.of(sourceUri, sourceUri)
                .withOptions(options)
                .start(ChangePrivateInformationActivity.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 5) {
                boolean isFromCamera = data.getBooleanExtra("from_camera", false);
                if (isFromCamera) {

                    String url =  data.getStringArrayListExtra("photos").get(0);
                    File file = new File(url);
                    Picasso.get().load(file).fit().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(client_profile_image);
                    Log.i(TESTING_TAG, "from camera " + Uri.fromFile(file).getPath());
                    Log.i(TESTING_TAG, "from camera " + url);
                    Log.i(TESTING_TAG, "from camera size " + file.length());
                    callChangePrivateInfo(url, phone.getText().toString());
                } else {
                    ArrayList<String> list = data.getStringArrayListExtra("photos");
                    Uri imageUri = Uri.parse(list.get(0));
                    callCropMethod(imageUri);
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                final Uri resultUri = UCrop.getOutput(data);
                Picasso.get().load(resultUri).fit().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(client_profile_image);
                File file = new File(resultUri.getPath());
                Log.i(TESTING_TAG, "from crop " + Uri.fromFile(file).getPath());
                Log.i(TESTING_TAG, "from crop size " +file.length());
                if (file.length() > 1500000){
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                        reCreateFile(bitmap, 500, file);
                        Log.i(TESTING_TAG, "after crop size " +file.length());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                callChangePrivateInfo(Uri.fromFile(file).getPath(), phone.getText().toString());
            } else if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == SELECT_CARD_DEATIL) {
                loadValues();
            } else if (requestCode == SELECT_ADDRESS) {
                loadValues();
            }
        }
        if (resultCode == SELECT_PHONE_VERIFICATION) {
            String newNumber = (String) data.getExtras().getString("phone");
            callChangePrivateInfo("empty", newNumber);
        }
        if (resultCode == STRIPE_CONNECT) {
            this.loadProfileData();
        }
    }

    public void reCreateFile(Bitmap _bitmapScaled, int maxImageSize, File filetoUplaod) {
        try {
            Log.i(TESTING_TAG, "recreate");
            float ratio = Math.min(
                    (float) maxImageSize / _bitmapScaled.getWidth(),
                    (float) maxImageSize / _bitmapScaled.getHeight());
            int width = Math.round(ratio * _bitmapScaled.getWidth());
            int height = Math.round(ratio * _bitmapScaled.getHeight());
            Bitmap newBitmap = Bitmap.createScaledBitmap(_bitmapScaled, width,
                    height, true);
            Log.i(TESTING_TAG, "new file " + filetoUplaod);
            FileOutputStream f_out = new FileOutputStream(filetoUplaod);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 50, f_out);
        } catch (IOException e) {
            Log.i(TESTING_TAG, "IOException " + e);
            e.printStackTrace();
        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        client_profile_image.setImageBitmap(bm);

        byte[] byteArray = bytes.toByteArray();
        final_image_path = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        client_profile_image.setImageBitmap(thumbnail);

        byte[] byteArray = bytes.toByteArray();
        final_image_path = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    /* Back click */
    public void backClick(View v) {
        this.finish();
    }

    /* Done click */
    public void doneClick(View v) {
        String phoneNumberFromPref = prefs.getString("phone", "").replace("+44", "");
        String phoneNumber = phone.getText().toString();
        if (Constants.EmptyText(ChangePrivateInformationActivity.this, fname.getText().toString(), getResources().getString(R.string.empty_email), fname)) {
            iv_line_f_name.setBackgroundResource(R.mipmap.red_line_first_name);
            tv_text_error_f_name.setVisibility(View.VISIBLE);
            tv_text_error_f_name.setText(getResources().getString(R.string.empty_first_name));
        } else if (Constants.EmptyText(ChangePrivateInformationActivity.this, lname.getText().toString(), getResources().getString(R.string.empty_password), lname)) {
            iv_line_su_name.setBackgroundResource(R.mipmap.red_line_first_name);
            tv_text_error_sur_name.setVisibility(View.VISIBLE);
            tv_text_error_sur_name.setText(getResources().getString(R.string.empty_last_name));
        } else if (Constants.EmptyText(ChangePrivateInformationActivity.this, email.getText().toString(), getResources().getString(R.string.empty_password), email)) {
            iv_line_email.setBackgroundResource(R.mipmap.line_signup_red);
            tv_text_error_email.setVisibility(View.VISIBLE);
            tv_text_error_email.setText(getResources().getString(R.string.empty_email));
        } else if (!Constants.isEmailValid(email.getText().toString())) {
            // ZoomConst.ErrorMessages(LoginActivity.this, getResources().getString(R.string.email_valid), mEmailView);
            iv_line_email.setBackgroundResource(R.mipmap.line_signup_red);
            tv_text_error_email.setVisibility(View.VISIBLE);
            tv_text_error_email.setText(getResources().getString(R.string.email_valid));
        } else if (Constants.EmptyText(ChangePrivateInformationActivity.this, phone.getText().toString(), getResources().getString(R.string.empty_password), lname)) {
            iv_line_phone.setBackgroundResource(R.mipmap.red_line_first_name);
            tv_text_error_phone.setVisibility(View.VISIBLE);
            tv_text_error_phone.setText("Please use a valid mobile number");
        } else if (phone.getText().length() < 10) {
            iv_line_phone.setBackgroundResource(R.mipmap.red_line_first_name);
            tv_text_error_phone.setVisibility(View.VISIBLE);
            tv_text_error_phone.setText("Please use a valid mobile number");
        } else if (Constants.EmptyText(ChangePrivateInformationActivity.this, workplace.getText().toString(), getResources().getString(R.string.empty_password), workplace)) {
            iv_line_workplace.setBackgroundResource(R.mipmap.red_line_first_name);
            tv_text_error_workplace.setVisibility(View.VISIBLE);
            tv_text_error_workplace.setText("Please enter name of place of work!");

        } else if (Constants.EmptyText(ChangePrivateInformationActivity.this, workplace.getText().toString(), getResources().getString(R.string.empty_password), workplace)) {
            iv_line_workplace.setBackgroundResource(R.mipmap.red_line_first_name);
            tv_text_error_workplace.setVisibility(View.VISIBLE);
            tv_text_error_workplace.setText("Please enter name of place of work!");
        } else if (Constants.EmptyText(ChangePrivateInformationActivity.this, address.getText().toString(), getResources().getString(R.string.empty_password), address)) {
            iv_line_address.setBackgroundResource(R.mipmap.red_line_first_name);
            tv_text_error_address.setVisibility(View.VISIBLE);
            tv_text_error_address.setText("Please enter the address of your place of work");
        } else if (!(phoneNumber.equalsIgnoreCase(phoneNumberFromPref))) {
            sendVerificationCode(phoneNumber);
        } else {
            callChangePrivateInfo("empty", phone.getText().toString());
        }
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            prefs.edit().putBoolean(Constants.ISMOBILEVERIFIED, false).apply();
            Intent it = new Intent(ChangePrivateInformationActivity.this, VerificationActivity.class);
            it.putExtra("From", "changeprivateinfo");
            it.putExtra("phone", (phone.getText().toString().contains("+44")) ? phone.getText().toString() : ("+44" + phone.getText().toString()));
            it.putExtra("verificationId", s);
            it.putExtra("forceResendingToken", forceResendingToken);
            startActivityForResult(it, SELECT_PHONE_VERIFICATION);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void sendVerificationCode(String number) {
        // progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 0, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);
    }

    public void loadProfileData() {
        ApiClient.getInstance().getBarberProfile(this, prefs, new ApiCallback() {
            @Override
            public void onSuccess() {
                loadValues();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void callChangePrivateInfo(final String imagePath, String newNumber) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(ChangePrivateInformationActivity.this);
            SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, Constants.ChangePrivateInfo,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Constants.dismissProgress();
                            try {
                                JSONObject jObj = new JSONObject(response);
                                String message = jObj.getString("message");
                                String profile_image = jObj.getJSONArray("data").getJSONObject(0).getString("profile_image");
                                String phone = jObj.getJSONArray("data").getJSONObject(0).getString("phone");
                                String last_name = jObj.getJSONArray("data").getJSONObject(0).getString("last_name");
                                String bank_detail = jObj.getJSONArray("data").getJSONObject(0).getString("bank_detail");
                                String first_name = jObj.getJSONArray("data").getJSONObject(0).getString("first_name");
                                String email = jObj.getJSONArray("data").getJSONObject(0).getString("email");

                                String address = jObj.getJSONArray("data").getJSONObject(0).getString("address");

                                String lon = jObj.getJSONArray("data").getJSONObject(0).getString("lon");
                                String lat = jObj.getJSONArray("data").getJSONObject(0).getString("lat");
                                String postcode = jObj.getJSONArray("data").getJSONObject(0).getString("postcode");
                                String workplace = jObj.getJSONArray("data").getJSONObject(0).getString("workplace");

                                prefs.edit().putString(Constants.KEY_EMAIL, email).apply();
                                prefs.edit().putString(Constants.KEY_BANK_DETAIL, bank_detail).apply();
                                prefs.edit().putString(Constants.KEY_PROFILE_IMAGE, profile_image).apply();
                                prefs.edit().putString(Constants.KEY_PHONE, phone).apply();
                                prefs.edit().putString(Constants.KEY_ADDRESS, address).apply();
                                prefs.edit().putString(Constants.KEY_WORKPLACE, workplace).apply();
                                prefs.edit().putString(Constants.KEY_POSTCODE, postcode).apply();
                                prefs.edit().putString(Constants.KEY_LAT, lat).apply();
                                prefs.edit().putString(Constants.KEY_LOG, lon).apply();

                                if (imagePath != null && imagePath.equalsIgnoreCase("empty")) {
                                    finish();
                                } else {

                                }

                            } catch (JSONException e) {
                                // JSON error
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "" + "Problem Updating profile_notification", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "" + "Problem Updating profile_notification", Toast.LENGTH_LONG).show();
                    Constants.dismissProgress();
                    Constants.showPopupServer(activity);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getApiHeaders();
                }

                @Override
                public Map<String, MultiPartParam> getMultipartParams() {
                    return super.getMultipartParams();
                }
            };
            if (!imagePath.equalsIgnoreCase("empty")) {
                smr.addFile("profile_image", imagePath);
            }
            smr.addStringParam("first_name", "" + fname.getText().toString());
            smr.addStringParam("last_name", "" + lname.getText().toString());
            smr.addStringParam("email", "" + email.getText().toString());
            smr.addStringParam("phone", newNumber);
            smr.addStringParam("bank_detail", "" + Id);
            smr.addStringParam("lat", prefs.getString(Constants.KEY_LAT, ""));
            smr.addStringParam("lon", prefs.getString(Constants.KEY_LOG, ""));
            smr.addStringParam("address", address.getText().toString());
            smr.addStringParam("postcode", prefs.getString(Constants.KEY_POSTCODE, ""));
            smr.addStringParam("workplace", workplace.getText().toString());
            AppController.getInstance().addToRequestQueue(smr);
        } else {
            Constants.showPopupInternet(activity);
        }
    }
}



