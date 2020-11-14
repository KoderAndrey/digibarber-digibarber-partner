package com.digibarber.app.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
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
import android.hardware.Camera;
import android.net.Uri;
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
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.digibarber.app.Beans.CustomGalleryImages;
import com.digibarber.app.CustomAdapters.CustomGalleryImagesAdapter;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;
import com.yalantis.ucrop.UCrop;

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

import static com.digibarber.app.CustomClasses.BaseActivity.TESTING_TAG;

public class CustomCameraLargePreviewActivity extends FragmentActivity {
    private static final String TAG = "CustomCamera";
    private static final int GALLERY_REQUEST = 237;
    private Camera camera;
    FrameLayout mPreview;

    private int rotation;
    private File imageFile;
    private String android_id;
    private String date;
    private CameraPreviewInternal mCameraPreview;
    private File userfile = null;
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

    private boolean bolFlash;
    Cursor cursor;
    String[] projection = {MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

    ArrayList<CustomGalleryImages> alImages = new ArrayList<>();

    CustomGalleryImagesAdapter objGalleryAdapter;
    private ImageView iv_capture_image;
    private ImageView iv_rotate_camera;
    private ImageView iv_photo_album;
    private ImageView iv_zoom_in_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera_large_preview);

        jpegCallback = new CustomPictureCallback();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE_GROUP);
            } else {
                createUI();
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
            Bundle mCropOptionsBundle = new Bundle();
            mCropOptionsBundle.putParcelable("ImageUri", resultUri);
            Intent it = new Intent();
            it.putExtras(mCropOptionsBundle);
            setResult(RESULT_OK, it);
            finish();
        } else if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {
            if (data != null) {
                String picturePath = Constants.getPath(data.getData(), CustomCameraLargePreviewActivity.this);
                userfile = new File(picturePath);
                Uri imageUri = Uri.fromFile(userfile);
                callCropMethod(imageUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }


    private void createUI() {
        camera = getCameraInstance();
        setCamFocusMode();
        mCameraPreview = new CameraPreviewInternal(this);
        mPreview = (FrameLayout) findViewById(R.id.preview);
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

                finish();
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

//			Display display = getWindowManager().getDefaultDisplay();
//			width_screen = display.getWidth();
//			height_screen = display.getHeight();
//			Toast.makeText(context, width_screen+" "+height_screen, Toast.LENGTH_LONG).show();

//			if (width_screen >=1080 && height_screen>=1920) {
//				resized = BITMAP_RESIZER(loadedImage, 550, 420);
//		    	rotated = Bitmap.createBitmap(resized, 0, 0, resized.getWidth(),
//				resized.getHeight(), matrix, true);
//
//			}
//			else if(width_screen <=360 && height_screen<=480)
//			{
//				resized = BITMAP_RESIZER(loadedImage, loadedImage.getHeight(), loadedImage.getWidth());
//		    	rotated = Bitmap.createBitmap(resized, 0, 0, resized.getWidth(),
//				resized.getHeight(), matrix, true);
//
//			}
//			else
//			{
            resized = BITMAP_RESIZER(loadedImage, 480, 360);
            rotated = Bitmap.createBitmap(resized, 0, 0, resized.getWidth(),
                    resized.getHeight(), matrix, true);

//			}

            try {
                // create folder if it doesnt exists
                String state = Environment.getExternalStorageState();
                File folder;
                folder = new File(CustomCameraLargePreviewActivity.this.getExternalFilesDir(null)
                        + "/CustomCam");
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdirs();
                }
                if (success) {
                    // Create a media file name
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
                stillCount++;
                //camera.startPreview();
                if (stillCount < maxFrames) {
                    new Thread() {
                        public void run() {

                            try {
                                sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    camera.takePicture(shutterCallback, rawCallback,
                                            jpegCallback);
                                }
                            });
                        }
                    }.start();

                } else {
                    stillCount = 0;
                    Uri sourceUri = Uri.fromFile(imageFile);
                    callCropMethod(sourceUri);
                    //  uploadFile();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error starting preview: " + e.toString());
            }
        }
    }
    private void callCropMethod(Uri sourceUri) {

        UCrop.Options options = new UCrop.Options();
        options.setStatusBarColor(Color.parseColor("#ffffff"));
        options.setToolbarColor(Color.parseColor("#4D31353A"));
        options.setActiveWidgetColor(Color.parseColor("#118FEB"));
        UCrop.of(sourceUri, sourceUri)
                .withOptions(options)
                .start(CustomCameraLargePreviewActivity.this);
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

}
