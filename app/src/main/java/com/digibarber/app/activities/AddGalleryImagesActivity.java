package com.digibarber.app.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.ParseError;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.digibarber.app.Beans.CustomGalleryImages;
import com.digibarber.app.Beans.SelectedImages;
import com.digibarber.app.CustomAdapters.CustomGalleryImagesAdapter;
import com.digibarber.app.CustomAdapters.SelectedGalleryImagesRecyclerAdapter;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.CustomClasses.GridSpacingItemDecoration;
import com.digibarber.app.CustomClasses.RecyclerItemClickListener;
import com.digibarber.app.Interfaces.RemoveGalleryImages;
import com.digibarber.app.R;
import com.yalantis.ucrop.UCrop;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddGalleryImagesActivity extends BaseActivity implements RecyclerItemClickListener.OnItemClickListener, RemoveGalleryImages {


    private static final String TAG = "CustomCamera";
    private static final int LARGER_CAMERA = 236;
    private Camera camera;
    private ImageView back_icon;
    private ImageView tv_next;
    FrameLayout mPreview;

    private final int PICK_IMAGE_REQUEST = 21;
    private int currentSelectedIndex = -1;

    private int rotation;
    private File imageFile;
    private String android_id;
    private String date;
    private CameraPreviewInternal mCameraPreview;
    private File userfile = null;
    private File filetoUplaod = null;

    int stillCount = 0;
    int i = 0;
    static int result;
    ArrayList<String> timestamps = new ArrayList<String>();
    private int cameraId;
    static Context context;
    int width_screen = 0;
    int height_screen = 0;

    private boolean isPreview = false;

    private CustomPictureCallback jpegCallback;

    private final int REQUEST_PERMISSION_CAMERA = 234;
    private final int REQUEST_PERMISSION_STORAGE_GROUP = 235;

    RecyclerView rv_gallery;
    private boolean bolFlash;
    Cursor cursor;
    String[] projection = {MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
    private static final int GALLERY_REQUEST = 237;
    ArrayList<CustomGalleryImages> alImages = new ArrayList<>();

    CustomGalleryImagesAdapter objGalleryAdapter;
    private ImageView iv_capture_image;
    private ImageView iv_rotate_camera;
    // private ImageView iv_preview;
    private ImageView iv_photo_album;
    private ImageView iv_zoom_in_out;
    // private LinearLayout ll_add_image;
    private RecyclerView rv_gallery_selected;
    SelectedGalleryImagesRecyclerAdapter adapterSelctedImages;
    ArrayList<SelectedImages> alSelectedImages = new ArrayList<>();

    int selectedPosTick = -1;

    ArrayList<Boolean> alSelectImagesTick = new ArrayList<>();
    private String From = "AddGalleryImages";
    private JSONArray galleryImages = new JSONArray();
    LinearLayout ll_message;
    boolean isDecorationAdd = false;

    private String Latitude;
    private String Longitude;
    private String address;
    private String workplace;
    private String postcode;
    private String firstname;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";
    private String mobile;

    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        alSelectedImages = new ArrayList<>();
        setContentView(R.layout.activity_add_gallery_images);
        mobile = getIntent().getStringExtra("mobile");
        try {
            bd = getIntent().getExtras();
            if (bd != null) {
                From = bd.getString("From");
                Latitude = bd.getString("Latitude");
                Longitude = bd.getString("Longitude");
                workplace = bd.getString("workplace");
                address = bd.getString("address");
                postcode = bd.getString("postcode");
                From = bd.getString("From");
                firstname = bd.getString("first_name");
                lastName = bd.getString("last_name");
                email = bd.getString("email");
                password = bd.getString("password");
                phone = bd.getString("phone");
                galleryImages = new JSONArray(bd.getString("galleryImages"));

            }
        } catch (NullPointerException e) {

        } catch (JSONException e) {

        }
        mPreview = (FrameLayout) findViewById(R.id.preview);
        back_icon = (ImageView) findViewById(R.id.back_icon);
        tv_next = (ImageView) findViewById(R.id.tv_next);
        rv_gallery = (RecyclerView) findViewById(R.id.rv_gallery);
        rv_gallery_selected = (RecyclerView) findViewById(R.id.rv_gallery_selected);
        ll_message = (LinearLayout) findViewById(R.id.ll_message);

        //ll_add_image = (LinearLayout) findViewById(R.id.ll_add_image);

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (From != null && From.equalsIgnoreCase("EditProfile")) {
                    finish();
                } else {
                    Intent it = new Intent(AddGalleryImagesActivity.this, CustomCameraGalleryActivity.class);
                    it.putExtra("Latitude", Latitude);
                    it.putExtra("Longitude", Longitude);
                    it.putExtra("workplace", workplace);
                    it.putExtra("address", address);
                    it.putExtra("postcode", postcode);
                    it.putExtra("first_name", firstname);
                    it.putExtra("last_name", lastName);
                    it.putExtra("email", email);
                    it.putExtra("password", password);
                    it.putExtra("phone", phone);
                    it.putExtra("From", From);
                    startActivity(it);
                    finish();
                }
            }
        });
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alSelectedImages.size() > 0) {
                    callAddGalleryImages();
                }else{
//                    AlertDialog.Builder builder = new AlertDialog.Builder(AddGalleryImagesActivity.this);
//                    builder.setMessage("Upload Gallery images now or later");
//                    builder.setNegativeButton("Now", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                        }
//                    });
//                    builder.setPositiveButton("Later", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            navigateToServices();
//                        }
//                    });
//                    builder.create().show();
                    Constants.showGalleryPopUp(AddGalleryImagesActivity.this);
                   // Constants.showPopupWithMsg(AddGalleryImagesActivity.this,"Upload Gallery images now or later","Gallery Images");
                }
            }
        });

        if (alSelectedImages.size() > 0) {
            ll_message.setVisibility(View.GONE);
            rv_gallery_selected.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
            rv_gallery_selected.setLayoutManager(layoutManager);
            rv_gallery_selected.setHasFixedSize(true);
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
            if (!isDecorationAdd) {
                rv_gallery_selected.addItemDecoration(new GridSpacingItemDecoration(3, spacingInPixels, false));
                isDecorationAdd = true;
            }
            adapterSelctedImages = new SelectedGalleryImagesRecyclerAdapter(alSelectedImages, AddGalleryImagesActivity.this, this);
            rv_gallery_selected.setAdapter(adapterSelctedImages);
            adapterSelctedImages.notifyDataSetChanged();
        } else {
            ll_message.setVisibility(View.VISIBLE);
            rv_gallery_selected.setVisibility(View.GONE);
        }
        jpegCallback = new CustomPictureCallback();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE_GROUP);
            } else {
                createUI();
                callImagesMethod();
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (From != null && From.equalsIgnoreCase("EditProfile")) {
            finish();
        } else {
            Intent it = new Intent(AddGalleryImagesActivity.this, CustomCameraGalleryActivity.class);
            it.putExtra("Latitude", Latitude);
            it.putExtra("Longitude", Longitude);
            it.putExtra("workplace", workplace);
            it.putExtra("address", address);
            it.putExtra("postcode", postcode);
            it.putExtra("first_name", firstname);
            it.putExtra("last_name", lastName);
            it.putExtra("email", email);
            it.putExtra("password", password);
            it.putExtra("phone", phone);
            it.putExtra("From", From);
            startActivity(it);
            finish();
        }
    }

    @Override
    public void RemoveGalleryImages(int position) {
        int pos = alSelectedImages.get(position).SelectedTickPos;
        if (pos != -1) {
            alSelectImagesTick.set(pos, false);
            objGalleryAdapter.notifyDataSetChanged();
        }
        alSelectedImages.remove(position);
        adapterSelctedImages.notifyDataSetChanged();
    }

    @Override
    public void ReplaceGalleryImages(int pos) {
        currentSelectedIndex = pos;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public class callAsyckDownalodImage extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = 0; i < galleryImages.length(); i++) {
                    String gallery_image = galleryImages.getJSONObject(i).getString("gallery_image");
                    try {
                        URL url = new URL(gallery_image);
                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                        String state = Environment.getExternalStorageState();
                        File folder = null;
                        if (state.contains(Environment.MEDIA_MOUNTED)) {
                            folder = new File(Environment.getExternalStorageDirectory()
                                    + "/CustomCam");
                        } else {
                            folder = new File(Environment.getExternalStorageDirectory()
                                    + "/CustomCam");
                        }
                        boolean success = true;
                        if (!folder.exists()) {
                            success = folder.mkdirs();
                        }
                        File downLoadFile = null;
                        if (success) {
                            // Create a media file name
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss.SSS",
                                    Locale.getDefault()).format(new Date());
                            downLoadFile = new File(folder.getAbsolutePath()
                                    + File.separator + timeStamp + ".jpg");
                            timestamps.add(timeStamp);
                            downLoadFile.createNewFile();
                        } else {
                            Toast.makeText(getBaseContext(), "Image Not saved",
                                    Toast.LENGTH_SHORT).show();
                        }
                        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                        // save image into gallery
                        image.compress(Bitmap.CompressFormat.JPEG, 95, ostream);
                        FileOutputStream fout = new FileOutputStream(downLoadFile);
                        fout.write(ostream.toByteArray());
                        fout.close();
                        Log.e("Deep", "Photo_path1:" + downLoadFile.getPath());

                        alSelectedImages.add(new SelectedImages(downLoadFile.getPath(), -1));
                    } catch (IOException e) {
                        System.out.println(e);
                        e.printStackTrace();
                        Constants.dismissProgress();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Constants.dismissProgress();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Constants.dismissProgress();
            if (alSelectedImages.size() > 0) {
                ll_message.setVisibility(View.GONE);
                rv_gallery_selected.setVisibility(View.VISIBLE);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(AddGalleryImagesActivity.this, 3);
                rv_gallery_selected.setLayoutManager(layoutManager);
                rv_gallery_selected.setHasFixedSize(true);
                int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
                if (!isDecorationAdd) {
                    rv_gallery_selected.addItemDecoration(new GridSpacingItemDecoration(3, spacingInPixels, false));
                    isDecorationAdd = true;
                }
                adapterSelctedImages = new SelectedGalleryImagesRecyclerAdapter(alSelectedImages, AddGalleryImagesActivity.this, AddGalleryImagesActivity.this);
                rv_gallery_selected.setAdapter(adapterSelctedImages);
                adapterSelctedImages.notifyDataSetChanged();
            } else {
                rv_gallery_selected.setVisibility(View.GONE);
                ll_message.setVisibility(View.VISIBLE);
            }
        }
    }

    private void callAddGalleryImages() {

        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {

            Constants.showPorgess(AddGalleryImagesActivity.this);
            String Url = "";
            if (From != null && From.equalsIgnoreCase("EditProfile")) {
                Url = Constants.Add_Gallery_Images;
            } else {
                Url = Constants.Add_Gallery_Images;
            }
            Log.e("Deep", "Add_Gallery_Images M Url:" + Url);

            MultipartRequest multipartRequest;
            String tag_string_req = "string_req";
            multipartRequest = new MultipartRequest(com.android.volley.Request.Method.POST, Url, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Constants.dismissProgress();
                    Log.e("onErrorResponse", "Error :" + error);
                }
            }, alSelectedImages) {
            };
            multipartRequest.setShouldCache(false);
            AppController.getInstance().addToRequestQueue(multipartRequest, tag_string_req, AddGalleryImagesActivity.this);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
        } else {
            Constants.showPopupInternet(activity);
        }
    }


    public void callImagesMethod() {
        AsyncTask<Void, Void, Void> ayc = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Constants.showPorgess(AddGalleryImagesActivity.this);
            }

            @Override
            protected Void doInBackground(Void... params) {
                cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        null,
                        null,
                        orderBy);

                if (cursor.moveToFirst()) {
                    final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    do {
                        final String Photo_path = cursor.getString(dataColumn);

                        CustomGalleryImages objGalleryImages = new CustomGalleryImages();
                        objGalleryImages.images = Photo_path;
                        alImages.add(objGalleryImages);
                        alSelectImagesTick.add(false);
                    } while (cursor.moveToNext());
                }
                cursor.close();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                objGalleryAdapter = new CustomGalleryImagesAdapter(alImages, alSelectImagesTick, AddGalleryImagesActivity.this, new CustomGalleryImagesAdapter.CustomGalleryListner() {
                    @Override
                    public void onItemClick(String path) {

                        userfile = new File(path);
                        Uri imageUri = Uri.fromFile(userfile);
                        callCropMethod(imageUri);
                    }
                });
                rv_gallery.setLayoutManager(new GridLayoutManager(AddGalleryImagesActivity.this, 2, GridLayoutManager.HORIZONTAL, false));
                rv_gallery.setHasFixedSize(true);
                rv_gallery.setNestedScrollingEnabled(false);
                rv_gallery.setAdapter(objGalleryAdapter);
                objGalleryAdapter.notifyDataSetChanged();
                if (galleryImages != null && galleryImages.length() > 0) {

                    new callAsyckDownalodImage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    Constants.dismissProgress();
                }
            }
        };
        ayc.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE_GROUP);
                } else {
                    createUI();
                    callImagesMethod();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Sorry! We can't continue without camera permission");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.create().show();
            }
        }
        if (requestCode == REQUEST_PERMISSION_STORAGE_GROUP) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createUI();
                callImagesMethod();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Sorry! We can't continue without storage permission");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.create().show();
            }
        }

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (camera == null) {
            camera = getCameraInstance();
            setCamFocusMode();
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                reCreateFile(bitmap, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //   ll_add_image.setVisibility(View.GONE);
            //  Picasso.with(AddGalleryImagesActivity.this).load(resultUri).skipMemoryCache().into(iv_preview);
        } else if (resultCode == RESULT_OK && requestCode == LARGER_CAMERA) {
            final Uri resultUri = data.getParcelableExtra("ImageUri");
            //  Picasso.with(AddGalleryImagesActivity.this).load(resultUri).skipMemoryCache().into(iv_preview);
            //  ll_add_image.setVisibility(View.GONE);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                reCreateFile(bitmap, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {
            if (data != null) {
                String picturePath = Constants.getPath(data.getData(), AddGalleryImagesActivity.this);
                userfile = new File(picturePath);
                Uri imageUri = Uri.fromFile(userfile);
                callCropMethod(imageUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                reCreateFile(bitmap, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reCreateFile(Bitmap _bitmapScaled, int maxImageSize) {
        try {
            float ratio = Math.min(
                    (float) maxImageSize / _bitmapScaled.getWidth(),
                    (float) maxImageSize / _bitmapScaled.getHeight());
            int width = Math.round((float) ratio * _bitmapScaled.getWidth());
            int height = Math.round((float) ratio * _bitmapScaled.getHeight());
            Bitmap newBitmap = Bitmap.createScaledBitmap(_bitmapScaled, width,
                    height, true);
            String state = Environment.getExternalStorageState();
            File folder = null;
            if (state.contains(Environment.MEDIA_MOUNTED)) {
                folder = new File(Environment.getExternalStorageDirectory()
                        + "/DigiProfileImages");
            } else {
                folder = new File(Environment.getExternalStorageDirectory()
                        + "/DigiProfileImages");
            }
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
                filetoUplaod = new File(folder.getAbsolutePath()
                        // + File.separator + "IMG_" + i + ".jpg");
                        + File.separator + timeStamp + ".jpg");
                filetoUplaod.createNewFile();
            } else {
                Toast.makeText(getBaseContext(), "Image Not saved",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            FileOutputStream f_out = new FileOutputStream(filetoUplaod);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 50, f_out);
            if (currentSelectedIndex != -1){
                alSelectedImages.remove(currentSelectedIndex);
                alSelectedImages.add(currentSelectedIndex,new SelectedImages(filetoUplaod.getPath(), selectedPosTick));
                currentSelectedIndex = -1;
            }else{
                alSelectedImages.add(new SelectedImages(filetoUplaod.getPath(), selectedPosTick));
            }

            if (alSelectedImages.size() > 0) {
                ll_message.setVisibility(View.GONE);
                rv_gallery_selected.setVisibility(View.VISIBLE);


                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(AddGalleryImagesActivity.this, 3);
                int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
                if (!isDecorationAdd) {
                    rv_gallery_selected.addItemDecoration(new GridSpacingItemDecoration(3, spacingInPixels, false));
                    isDecorationAdd = true;
                }
                rv_gallery_selected.setLayoutManager(layoutManager);
                adapterSelctedImages = new SelectedGalleryImagesRecyclerAdapter(alSelectedImages, AddGalleryImagesActivity.this, AddGalleryImagesActivity.this);
                rv_gallery_selected.setAdapter(adapterSelctedImages);
                adapterSelctedImages.notifyDataSetChanged();
            } else {
                ll_message.setVisibility(View.VISIBLE);
                rv_gallery_selected.setVisibility(View.GONE);
            }
            selectedPosTick = -1;
        } catch (IOException e) {
            e.printStackTrace();
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
                .start(AddGalleryImagesActivity.this);
    }

    private void createUI() {
        camera = getCameraInstance();
        setCamFocusMode();
        mCameraPreview = new CameraPreviewInternal(this);
        mPreview = (FrameLayout) findViewById(R.id.preview);
        //iv_preview = (ImageView) findViewById(R.id.iv_preview);
        iv_capture_image = (ImageView) findViewById(R.id.iv_capture_image);
        iv_photo_album = (ImageView) findViewById(R.id.iv_photo_album);
        iv_zoom_in_out = (ImageView) findViewById(R.id.iv_zoom_in_out);
        iv_rotate_camera = (ImageView) findViewById(R.id.iv_rotate_camera);
        mPreview.addView(mCameraPreview);
        android_id = Settings.Secure.getString(getApplicationContext()
                .getContentResolver(), Settings.Secure.ANDROID_ID);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd, hh:mm");
        date = df.format(new Date());
        System.out.println("ID is" + android_id + " Date is" + date);

        iv_capture_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timestamps.clear();
                jpegCallback.setMaxFrames(1);
                camera.takePicture(shutterCallback, rawCallback, jpegCallback);
            }
        });
        iv_rotate_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (camera != null) {
                    if (isPreview) {
                        camera.stopPreview();
                    }
                    camera.release();
                    if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    } else {
                        cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                    }
                    camera = getCameraInstance();
                    //setCameraDisplayOrientation(CameraDemo.this,cameraId,camera);
                    mCameraPreview.recreatePreview();
                }
            }
        });
        iv_photo_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLERY_REQUEST);
            }
        });

        iv_zoom_in_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(AddGalleryImagesActivity.this, CustomCameraLargePreviewActivity.class);
                startActivityForResult(it, LARGER_CAMERA);

            }
        });

    }


    private void setCamFocusMode() {

        if (null == camera) {
            return;
        }
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        Camera.Size size = sizes.get(0);
        for (int i = 0; i < sizes.size(); i++) {
            if (sizes.get(i).width > size.width)
                size = sizes.get(i);
        }
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setJpegQuality(100);
        parameters.setPictureSize(size.width, size.height);
        camera.setParameters(parameters);
    }

    public Camera getCameraInstance() {
        Camera camera = null;
        try {
            if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
            } else {
                cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
            }
            camera = Camera.open(cameraId);
            setUpCamera(camera);

        } catch (Exception e) {
            // cannot get camera or does not exist
            e.printStackTrace();
        }
        return camera;
    }

    private void setUpCamera(Camera c) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;
            default:
                break;
        }
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            rotation = (info.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
        } else {
            rotation = (info.orientation - degree + 360) % 360;

        }
        c.setDisplayOrientation(rotation);


    }

    public Bitmap BITMAP_RESIZER(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }

    @Override
    public void onItemClick(View childView, int position) {

        if (alSelectedImages.size() >= 9) {
            showPopupMaxPrice();
        } else {
            selectedPosTick = position;
            alSelectImagesTick.set(position, true);
            objGalleryAdapter.notifyDataSetChanged();
            String imagePAth = alImages.get(position).images;
            File file = new File(imagePAth);
            Uri imageUri = Uri.fromFile(file);
            callCropMethod(imageUri);
        }
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    public void showPopupMaxPrice() {
        final Dialog dialog_first = new Dialog(AddGalleryImagesActivity.this);
        // setting custom layout to dialog
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_max_price);

        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);
        TextView tv_messageprice = (TextView) dialog_first.findViewById(R.id.tv_messageprice);
        tv_messageprice.setText("Maz 9 images");
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();
            }
        });
        dialog_first.show();
    }


    public class CameraPreviewInternal extends SurfaceView implements
            SurfaceHolder.Callback {
        private SurfaceHolder mSurfaceHolder;
        //private Camera mCamera;
        private Context context;

        //private int cameraId;
        // Constructor that obtains context and camera
        @SuppressWarnings("deprecation")
        public CameraPreviewInternal(Context context) {
            super(context);
            this.context = context;
            //prevCamera = mCamera;
            //this.mCamera = camera;
            // mCamera = callBack.getCameraInstance();
            this.mSurfaceHolder = this.getHolder();
            this.mSurfaceHolder.addCallback(this);
            this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            try {
                Camera.Parameters mParameters = camera.getParameters();
                List<String> flashModes = mParameters.getSupportedFlashModes();
                if (bolFlash) {
                    if (flashModes.contains(Camera.Parameters.FLASH_MODE_ON)) {
                        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    }
                } else {
                   /* if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    }*/
                }
                camera.setParameters(mParameters);
                camera.setDisplayOrientation(90);
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                isPreview = true;
            } catch (IOException e) {
                // left blank for now
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (camera != null) {
                camera.stopPreview();
            }
            isPreview = false;
            // mCamera.release();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
                                   int width, int height) {
            // start preview with new settings
            try {
                //Toast.makeText(context, "calling", Toast.LENGTH_LONG).show();
                //	CameraDemo.setCameraDisplayOrientation(activity, cameraId, mCamera);
                camera.setDisplayOrientation(90);
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();

                //mCamera = Camera.open(cameraId);
                //setUpCamera(mCamera);

            } catch (Exception e) {
                // intentionally left blank for a test
            }
        }

        public void recreatePreview() {
            //recreate();;
            try {
                camera.setDisplayOrientation(90);
                camera.setPreviewDisplay(mSurfaceHolder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    class CustomPictureCallback implements Camera.PictureCallback {
        private int maxFrames = 3;

        public void setMaxFrames(int maxFrames) {
            this.maxFrames = maxFrames;
        }

        public void onPictureTaken(byte[] data, final Camera camera) {
            camera.startPreview();
            i++;
            if (i > maxFrames) {
                i = 1;
            }
            File f = null;
            Bitmap loadedImage = null;
            Bitmap resized = null;
            Bitmap rotated = null;
            Matrix matrix = new Matrix();

            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                matrix.preScale(-1.0f, 1.0f);    //To resolve issue of 180 invert in case of front camera
            }
            matrix.postRotate(rotation);
            loadedImage = BitmapFactory.decodeByteArray(data, 0, data.length);

            resized = BITMAP_RESIZER(loadedImage, 480, 360);
            rotated = Bitmap.createBitmap(resized, 0, 0, resized.getWidth(),
                    resized.getHeight(), matrix, true);


            try {
                // create folder if it doesnt exists
                String state = Environment.getExternalStorageState();
                File folder = null;
                if (state.contains(Environment.MEDIA_MOUNTED)) {
                    folder = new File(Environment.getExternalStorageDirectory()
                            + "/CustomCam");
                } else {
                    folder = new File(Environment.getExternalStorageDirectory()
                            + "/CustomCam");
                }
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdirs();
                }
                if (success) {
                    // Create a media file name
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                            Locale.getDefault()).format(new Date());
                    imageFile = new File(folder.getAbsolutePath()
                            + File.separator + timeStamp + ".jpg");
                    timestamps.add(timeStamp);
                    imageFile.createNewFile();


                } else {
                    Toast.makeText(getBaseContext(), "Image Not saved",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                // save image into gallery
                rotated.compress(Bitmap.CompressFormat.JPEG, 95, ostream);
                FileOutputStream fout = new FileOutputStream(imageFile);
                fout.write(ostream.toByteArray());
                fout.close();
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.MediaColumns.DATA,
                        imageFile.getAbsolutePath());
                String[] projection = {MediaStore.Images.Media._ID};
                Cursor imagecursor = getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection, MediaStore.Images.Media.DATA + " like ? ",
                        new String[]{"%/CustomCam/%"}, null);

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                // Picasso.with(AddGalleryImagesActivity.this).load(imageFile).skipMemoryCache().into(iv_preview);
                Uri sourceUri = Uri.fromFile(imageFile);
                callCropMethod(sourceUri);
            } catch (Exception e) {
                Log.e(TAG, "Error starting preview: " + e.toString());
            }
        }
    }


    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            Log.e(TAG, "onShutter'd");
        }
    };
    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.e(TAG, "onPictureTaken - raw");
            //CameraDemo.this.camera.startPreview();
        }
    };

    public class MultipartRequest extends com.android.volley.Request<String> {
        MultipartEntity entity = new MultipartEntity();


        private static final String FILE_PART_NAME = "gallery_images";
        private static final String STRING_PART_NAME = "text";


        ArrayList<SelectedImages> selectedImagesArry;

        public MultipartRequest(int method, String url, com.android.volley.Response.ErrorListener errorListener,
                                ArrayList<SelectedImages> selectedImagesArry) {
            super(method, url, errorListener);

            this.selectedImagesArry = selectedImagesArry;
            buildMultipartEntity();


        }

        private void buildMultipartEntity() {
            for (int i = 0; i < selectedImagesArry.size(); i++) {
                File file;

                file = new File(alSelectedImages.get(i).image);
                entity.addPart(FILE_PART_NAME, new FileBody(file));
            }
        }


        @Override
        public String getBodyContentType() {
            return entity.getContentType().getValue();
        }


        @Override
        public byte[] getBody() throws AuthFailureError {


            Log.e("getBody", "getBody");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                entity.writeTo(bos);
            } catch (IOException e) {
                VolleyLog.e("IOException writing to ByteArrayOutputStream");
            }
            return bos.toByteArray();
        }


        @Override
        protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {

            try {
                if (response.data.length == 0) {
                    byte[] responseData = "{}".getBytes("UTF8");
                    response = new NetworkResponse(response.statusCode,
                            responseData, response.headers, response.notModified);
                }

                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers));
                return com.android.volley.Response.success(jsonString,
                        getCacheEntry()
                );
            } catch (UnsupportedEncodingException e) {
                System.out.println("VolleyQueue: Encoding Error for " + getTag()
                        + " (" + getSequence() + ")");
                return com.android.volley.Response.error(new ParseError(e));
            }
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return getApiHeaders();
        }

        @Override
        protected void deliverResponse(String response) {
            Log.e("Custom Req", "Response ::" + response);
            Constants.dismissProgress();
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("success").equalsIgnoreCase("true")) {
                    if (From != null && From.equalsIgnoreCase("EditProfile")) {
                        finish();
                    } else {

                        if (jObj.has("gallery")) {
                            JSONArray jaGallery = jObj.getJSONArray("gallery");
                            prefs.edit().putString(Constants.KEY_GALLERY_IMAGES, jaGallery.toString()).apply();
                        } else {
                            prefs.edit().putString(Constants.KEY_GALLERY_IMAGES, "").apply();
                        }
                        prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "gallery_images").apply();
                        navigateToServices();
                    }
                } else {
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "" + "Problem Updating profile", Toast.LENGTH_LONG).show();
            }
            //   mListener.onResponse(response);
        }
    }

    public void navigateToServices(){
        Intent it = new Intent(AddGalleryImagesActivity.this, PickServiceActivity.class);
        it.putExtra("Latitude", Latitude);
        it.putExtra("Longitude", Longitude);
        it.putExtra("workplace", workplace);
        it.putExtra("address", address);
        it.putExtra("postcode", postcode);
        it.putExtra("first_name", firstname);
        it.putExtra("last_name", lastName);
        it.putExtra("email", email);
        it.putExtra("password", password);
        it.putExtra("phone", phone);
        it.putExtra("mobile", mobile);
        it.putExtra("From", "Signup");
        startActivity(it);
    }


}
