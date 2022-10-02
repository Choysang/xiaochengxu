package com.example.androiddingjing.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.androiddingjing.R;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.engine.OpenCVEngineInterface;
import org.opencv.imgcodecs.Imgcodecs;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
    Currently, this activity is not used because of the ambiguous usage scenario
 */

public class CameraActivity extends AppCompatActivity implements  SurfaceHolder.Callback,Handler.Callback {

    private ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader imageReader) {
            Image img = imageReader.acquireLatestImage();

            // image will be CV_8UC3 && RGB
            Mat mat_buffer = new Mat(img.getHeight(),img.getWidth(), CvType.CV_8UC3);
            ByteBuffer buffer = img.getPlanes()[0].getBuffer();
            byte[] bytes_array = new byte[buffer.remaining()];
            buffer.get(bytes_array);

            // 1 发图片元数据
            String str_w="0x"+Integer.toHexString(img.getWidth());
            String str_h="0x"+Integer.toHexString(img.getHeight());
            byte[] byte_w = str_w.getBytes(StandardCharsets.UTF_8);
            byte[] delim_1 = ",".getBytes(StandardCharsets.UTF_8);
            byte[] byte_h = str_w.getBytes(StandardCharsets.UTF_8);
            byte[] delim_2 = ",".getBytes(StandardCharsets.UTF_8);
            // 2 bytes_array发出去。（图像信息



//            mat_buffer.put(0,0,bytes_array);
//            Mat mat = Imgcodecs.imdecode(mat_buffer, Imgcodecs.IMREAD_COLOR);
//              imageReader.close();
        }
    };
    private static final int CAMERA_REQUEST_CODE = 100;
    private Button camera_capture_button;
    private ImageReader imageReader;

    private Surface previewSurface;
    private CameraManager cameraManager;
    private String cameraId;
    private CameraCharacteristics cameraCharacteristics;
    SurfaceHolder surfaceHolder;

    //
    private final Handler camHandler = new Handler(this);
    private final int MSG_CAMERA_OPENED = 1;
    private final int MSG_SURFACE_READY = 2;
    private boolean isSurfaceCreated = false;
    private boolean isCameraConfigured = false;


    private static Integer IMG_WIDTH = 1080;
    private static Integer IMG_HEIGHT = 1920;
    private static Integer IMG_BUFFER = 50;
    private static Integer IMG_FORMAT = ImageFormat.JPEG;

    private CameraDevice cameraDevice;
    private CameraCaptureSession camCaptureSession;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Log.e("Capture", "COMPLETE");
        }
    };

    private CameraCaptureSession.StateCallback stateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
            camCaptureSession = cameraCaptureSession;
            try {
                captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                captureRequestBuilder.addTarget(previewSurface);
                captureRequestBuilder.addTarget(imageReader.getSurface());
                camCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), captureCallback, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

        }
    };

    // do when camera status changed
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Toast.makeText(getApplicationContext(), "onOpened", Toast.LENGTH_SHORT).show();
            cameraDevice = camera;
            camHandler.sendEmptyMessage(MSG_CAMERA_OPENED);
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (camCaptureSession != null) {
                camCaptureSession.stopRepeating();
                camCaptureSession.close();
                camCaptureSession = null;
            }

            isCameraConfigured = false;
        } catch (final CameraAccessException ignore) {
        } finally {
            if (cameraDevice != null) {
                cameraDevice.close();
                cameraDevice = null;
                camCaptureSession = null;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                Toast.makeText(getApplicationContext(), "request permission", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "PERMISSION_ALREADY_GRANTED", Toast.LENGTH_SHORT).show();
            try {
                cameraManager.openCamera(cameraId, mStateCallback, new Handler());
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

    }

    // MAIN

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        camera_capture_button = (Button) findViewById(R.id.camera_capture_button);
        camera_capture_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("HIIIII", "HIIIII");
                    }
                }
        );


        // Request camera permissions
        requestPermissions();
//        imReaderSurface = ImageReader.newInstance(IMG_HEIGHT,IMG_WIDTH,IMG_FORMAT,IMG_NUM);
        // init cameradevice
        cameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        // set SurfaceView for previewing
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);


        // initialize two destinations of output
//        previewSurface = surfaceView
//        previewSurface = surfaceView.getHolder().getSurface();
//        surfaceHolder = surfaceView.getHolder();
//        surfaceHolder.addCallback(this);
        try {
            cameraId = getFrontFacingCameraId(cameraManager);
            cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        imageReader = ImageReader.newInstance(IMG_WIDTH, IMG_HEIGHT, IMG_FORMAT, IMG_BUFFER);
        imageReader.setOnImageAvailableListener(onImageAvailableListener, null);
    }


    @Override
    public boolean handleMessage(@NotNull Message msg) {
        switch (msg.what) {
            case MSG_CAMERA_OPENED:
            case MSG_SURFACE_READY:
                // if both surface is created and camera device is opened
                // - ready to set up preview and other things
                if (isSurfaceCreated && (cameraDevice != null)
                        && !isCameraConfigured) {
                    configureCamera();
                }
                break;
        }

        return true;
    }

    private void configureCamera() {
        // prepare list of surfaces to be used in capture requests
        List<Surface> surfaceList = new ArrayList<Surface>();

        surfaceList.add(previewSurface); // surface for viewfinder preview
        surfaceList.add(imageReader.getSurface());

        // configure camera with all the surfaces to be ever used
        try {
            cameraDevice.createCaptureSession(surfaceList,
                    stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        isCameraConfigured = true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    private String getFrontFacingCameraId(CameraManager cameraManager) {
        try {
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(id);
                Integer cameraOrientation = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                if (cameraOrientation != null && cameraOrientation == CameraMetadata.LENS_FACING_FRONT) {
                    return id;
                }
            }

        } catch (CameraAccessException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        previewSurface = holder.getSurface();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        previewSurface = holder.getSurface();
        isSurfaceCreated = true;
        camHandler.sendEmptyMessage(MSG_SURFACE_READY);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isSurfaceCreated = false;
    }
}