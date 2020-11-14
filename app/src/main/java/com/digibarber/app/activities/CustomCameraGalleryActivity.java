package com.digibarber.app.activities;

import android.Manifest;
import android.app.AlertDialog;
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
import android.graphics.Matrix;
import android.graphics.Paint;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.digibarber.app.Beans.CustomGalleryImages;
import com.digibarber.app.CustomAdapters.CustomGalleryImagesAdapter;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.CustomClasses.RecyclerItemClickListener;
import com.digibarber.app.R;
import com.digibarber.app.apicalls.ApiClient;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomCameraGalleryActivity extends BaseActivity implements RecyclerItemClickListener.OnItemClickListener {

    private static final String TAG = "CustomCamera";
    private static final int LARGER_CAMERA = 236;
    private Camera camera;
    private ImageView back_icon;
    private ImageView tv_next;
    FrameLayout mPreview;

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
    private int prevPosition = -1;
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
    private ImageView iv_preview;
    private ImageView iv_photo_album;
    private ImageView iv_zoom_in_out;
    private LinearLayout ll_add_image;
    private ImageView iv_placehoder;
    private String From = "CustomCamera";
    ArrayList<Boolean> alSelectImagesTick = new ArrayList<>();
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

    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        try {
            bd = getIntent().getExtras();
            Log.i(TESTING_TAG, "is extras null = " + (bd == null));
            if (bd != null) {
                Log.i(TESTING_TAG, "extras = " + bd);
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

            }
            Log.i(TESTING_TAG, "from " + From);
        } catch (NullPointerException e) {
            Log.i(TESTING_TAG, "exception " + e);
        }
        setContentView(R.layout.activity_custom_camera_gallery);
        iv_placehoder = (ImageView) findViewById(R.id.iv_placehoder);
        mPreview = (FrameLayout) findViewById(R.id.preview);
        back_icon = (ImageView) findViewById(R.id.back_icon);
        tv_next = (ImageView) findViewById(R.id.tv_next);
        rv_gallery = (RecyclerView) findViewById(R.id.rv_gallery);
        ll_add_image = (LinearLayout) findViewById(R.id.ll_add_image);

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (From != null && From.equalsIgnoreCase("EditPrfoile")) {

                    finish();

                } else {
                    Intent it = new Intent(CustomCameraGalleryActivity.this, LocationOnMapActivity.class);
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
                if (From != null && From.equalsIgnoreCase("EditPrfoile")) {
                    Log.i(TESTING_TAG, "first case");
                    if (filetoUplaod != null) {
                        Log.i(TESTING_TAG, "first case inside");
                        Intent it = new Intent();
                        it.putExtra("image", filetoUplaod.toString());
                        setResult(RESULT_OK, it);
                        finish();
                    }
                } else {
                    Log.i(TESTING_TAG, "first case");
                    if (filetoUplaod != null) {
                        Log.i(TESTING_TAG, "first case inside");
                        callAddBarberProfile();
                    }
                }
            }
        });


        if (From != null && From.equalsIgnoreCase("EditPrfoile")) {
            tv_next.setImageResource(R.mipmap.done);
        } else {
            tv_next.setImageResource(R.mipmap.next);
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


    private void callAddBarberProfile() {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(CustomCameraGalleryActivity.this);
            Log.i(TESTING_TAG, "AddBarberProfile M url:" + Constants.AddBarberProfile);
            SimpleMultiPartRequest req = new SimpleMultiPartRequest(Request.Method.POST, Constants.AddBarberProfile,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i(TESTING_TAG, "AddBarberProfile M response:" + response);
                            Constants.dismissProgress();
                            try {
                                JSONObject jsonobj = new JSONObject(response);
                                String response_values = jsonobj.getString("success");
                                if (!response_values.equalsIgnoreCase("false")) {
                                    prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "profile_image").apply();
                                    String profile_image = jsonobj.getString("profile_image");
                                    prefs.edit().putString("profile_image", profile_image).apply();
                                    Intent it = new Intent(CustomCameraGalleryActivity.this, AddGalleryImagesActivity.class);
                                    startActivity(it);
                                    finish();
                                } else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                    Log.i(TESTING_TAG, "Error: " + error.getMessage());
                    Constants.dismissProgress();
                    Constants.showPopupServer(activity);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getApiHeaders();
                }
            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            req.addFile("profile_image", filetoUplaod.getPath());
            req.addStringParam("lat", prefs.getString(Constants.KEY_LAT, ""));
            req.addStringParam("lon", prefs.getString(Constants.KEY_LOG, ""));
            req.addStringParam("address", "");
            req.addStringParam("postcode", "");
            req.addStringParam("workplace", "");
            req.addStringParam("phone", "");
            req.addStringParam("open_hours", "");

            AppController.getInstance().addToRequestQueue(req, "LOGIN");
        } else {
            Constants.showPopupInternet(activity);
        }
    }

    public void callImagesMethod() {
        AsyncTask<Void, Void, Void> ayc = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Constants.showPorgess(CustomCameraGalleryActivity.this);
                //  ll_loader.setVisibility(View.VISIBLE);
                //  p_loader.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        null,
                        null,
                        orderBy);
                Log.i(TESTING_TAG, "doInBackground");
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
                Constants.dismissProgress();
                Log.i(TESTING_TAG, "size images list " + alImages.size());
                objGalleryAdapter = new CustomGalleryImagesAdapter(alImages, alSelectImagesTick, CustomCameraGalleryActivity.this, new CustomGalleryImagesAdapter.CustomGalleryListner() {
                    @Override
                    public void onItemClick(String path, int position) {
                        try {
                            imageFile = new File((path));
                            Log.i(TESTING_TAG, "image " + imageFile);
                            Log.i(TESTING_TAG, "path " + path);
                            Picasso.get().load(imageFile).into(iv_preview);
                            Uri sourceUri = Uri.fromFile(imageFile);
                            callCropMethod(sourceUri);
                            objGalleryAdapter.notifyItemChanged(position);
                            if (prevPosition != -1 && prevPosition != position) {
                                objGalleryAdapter.images.get(prevPosition).isChoosed = false;
                                objGalleryAdapter.notifyItemChanged(prevPosition);
                            }
                            prevPosition = position;
                        } catch (Exception e) {
                            Log.i(TESTING_TAG, "Error starting preview: " + e.toString());
                        }
                    }
                });
                rv_gallery.setLayoutManager(new GridLayoutManager(CustomCameraGalleryActivity.this, 2, GridLayoutManager.HORIZONTAL, false));
                rv_gallery.setHasFixedSize(true);
                rv_gallery.setNestedScrollingEnabled(false);
                rv_gallery.setAdapter(objGalleryAdapter);
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
            Log.i(TESTING_TAG, "1");
            final Uri resultUri = UCrop.getOutput(data);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                reCreateFile(bitmap, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ll_add_image.setVisibility(View.GONE);
            Picasso.get().load(resultUri).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv_preview);
        } else if (resultCode == RESULT_OK && requestCode == LARGER_CAMERA) {
            final Uri resultUri = data.getParcelableExtra("ImageUri");
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                reCreateFile(bitmap, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Picasso.get().load(resultUri).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv_preview);
            ll_add_image.setVisibility(View.GONE);
        } else if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {
            if (data != null) {
                String picturePath = Constants.getPath(data.getData(), CustomCameraGalleryActivity.this);
                userfile = new File(picturePath);
                Uri imageUri = Uri.fromFile(userfile);
                callCropMethod(imageUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    public void reCreateFile(Bitmap _bitmapScaled, int maxImageSize) {
        try {
            Log.i(TESTING_TAG, "recreate");
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
            Log.i(TESTING_TAG, "new file " + filetoUplaod);
            FileOutputStream f_out = new FileOutputStream(filetoUplaod);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 50, f_out);
        } catch (IOException e) {
            Log.i(TESTING_TAG, "IOException " + e);
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
                .start(CustomCameraGalleryActivity.this);
    }

    private void createUI() {
        camera = getCameraInstance();
        setCamFocusMode();
        mCameraPreview = new CameraPreviewInternal(this);
        mPreview = (FrameLayout) findViewById(R.id.preview);
        iv_preview = (ImageView) findViewById(R.id.iv_preview);
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

                iv_placehoder.setVisibility(View.GONE);
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

                Intent it = new Intent(CustomCameraGalleryActivity.this, CustomCameraLargePreviewActivity.class);
                startActivityForResult(it, LARGER_CAMERA);

            }
        });

    }


    private void setCamFocusMode() {

        if (null == camera) {
            return;
        }

        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();


        camera.setParameters(parameters);
        camera.startPreview();
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
            //camera = Camera.open();
            /*Camera.Parameters params = camera.getParameters();
            List<String> flashModes = params.getSupportedFlashModes();
			if (flashModes.contains(Parameters.FLASH_MODE_ON)) {
				params.setFlashMode(Parameters.FLASH_MODE_ON);
			}
// ... set other parameters
			camera.setParameters(params);*/
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

        iv_placehoder.setVisibility(View.GONE);
        String imagePAth = alImages.get(position).images;
        File file = new File(imagePAth);
        Uri imageUri = Uri.fromFile(file);
        callCropMethod(imageUri);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

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
                    if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    }
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
                File folder;
                folder = new File(CustomCameraGalleryActivity.this.getExternalFilesDir(null)
                        + "/CustomCam");
                boolean success = true;
                Log.i(TESTING_TAG, "external old " + Environment.getExternalStorageDirectory());
                Log.i(TESTING_TAG, "external new " + CustomCameraGalleryActivity.this.getExternalFilesDir(null));
                if (!folder.exists()) {
                    success = folder.mkdirs();
                    Log.i(TESTING_TAG, "folder not exist. success " + success + ", " + folder.exists());
                }
                if (success) {

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                            Locale.getDefault()).format(new Date());

                    imageFile = new File(folder.getAbsolutePath()
                            // + File.separator + "IMG_" + i + ".jpg");
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
                Picasso.get().load(imageFile).into(iv_preview);
                Uri sourceUri = Uri.fromFile(imageFile);
                callCropMethod(sourceUri);

            } catch (Exception e) {
                Log.i(TESTING_TAG, "Error starting preview: " + e.toString());
            }
        }
    }


    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            Log.i(TESTING_TAG, "onShutter'd");
        }
    };
    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.i(TESTING_TAG, "onPictureTaken - raw");
            //CameraDemo.this.camera.startPreview();
        }
    };
}
