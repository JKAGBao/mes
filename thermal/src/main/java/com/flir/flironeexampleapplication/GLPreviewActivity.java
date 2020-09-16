package com.flir.flironeexampleapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.flir.flironeexampleapplication.util.FileComparator;
import com.flir.flironeexampleapplication.util.StatusBarUtils;
import com.flir.flironeexampleapplication.util.SystemUiHider;
import com.flir.flironeexampleapplication.util.ThermalFragment;
import com.flir.flironeexampleapplication.util.WaterMark;
import com.flir.flironesdk.Device;
import com.flir.flironesdk.FlirUsbDevice;
import com.flir.flironesdk.Frame;
import com.flir.flironesdk.FrameProcessor;
import com.flir.flironesdk.RenderedImage;
import com.flir.flironesdk.SimulatedDevice;

import java.io.File;
import java.net.Socket;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

/**
 * An example activity and delegate for FLIR One image streaming and device interaction.
 * Based on an example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 * @see Device.Delegate
 * @see FrameProcessor.Delegate
 * @see Device.StreamDelegate
 * @see Device.PowerUpdateDelegate
 */
public class GLPreviewActivity extends Activity implements Device.Delegate, FrameProcessor.Delegate, Device.StreamDelegate, Device.PowerUpdateDelegate {
    GLSurfaceView thermalSurfaceView;
    private volatile boolean imageCaptureRequested = false;
    private volatile Socket streamSocket = null;
    private boolean chargeCableIsConnected = true;
    View view;
    private int deviceRotation = 0;
    private OrientationEventListener orientationEventListener;
    LinearLayout layout;
    boolean mIsBack = true;
    private volatile Device flirOneDevice;
    private FrameProcessor frameProcessor;
    boolean mIsStop = false;
    private String lastSavedPath;
    private ImageView mImgThermal;
    private ImageView mIcBack;
    ProgressDialog mProgressDialog;
    CharSequence text;
    String mFileName = "";
    Bitmap bitmap1;
    FrameLayout mFlDrag;
    int startX;
    int startY;
    int left = 325;
    int top = 565;
    int leftx = 0;
    int topy = 0;
    int[] temp = new int[]{0, 0};
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Glide.with(GLPreviewActivity.this).load(mFileName).into(mImgThermal);
            } else if (msg.what == 2) {
                mProgressDialog.dismiss();
            }
        }
    };

    private Device.TuningState currentTuningState = Device.TuningState.Unknown;
    // Device Delegate methods

    // Called during device discovery, when a device is connected
    // During this callback, you should save a reference to device
    // You should also set the power update delegate for the device if you have one
    // Go ahead and start frame stream as soon as connected, in this use case
    // Finally we create a frame processor for rendering frames

    public void onDeviceConnected(Device device) {
        Log.i("ExampleApp", "Device connected!");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.pleaseConnect).setVisibility(View.GONE);
            }
        });

        flirOneDevice = device;
        flirOneDevice.setPowerUpdateDelegate(this);
        flirOneDevice.startFrameStream(this);

        final ToggleButton chargeCableButton = (ToggleButton) findViewById(R.id.chargeCableToggle);
        if (flirOneDevice instanceof SimulatedDevice) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chargeCableButton.setChecked(chargeCableIsConnected);
                    chargeCableButton.setVisibility(View.VISIBLE);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chargeCableButton.setChecked(chargeCableIsConnected);
                    chargeCableButton.setVisibility(View.INVISIBLE);
                    findViewById(R.id.connect_sim_button).setEnabled(false);

                }
            });
        }

        orientationEventListener.enable();
    }

    /**
     * Indicate to the user that the device has disconnected
     */
    public void onDeviceDisconnected(Device device) {
        Log.i("ExampleApp", "Device disconnected!");

        final ToggleButton chargeCableButton = (ToggleButton) findViewById(R.id.chargeCableToggle);
        final TextView levelTextView = (TextView) findViewById(R.id.batteryLevelTextView);
        final ImageView chargingIndicator = (ImageView) findViewById(R.id.batteryChargeIndicator);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.pleaseConnect).setVisibility(View.GONE);
                levelTextView.setText("--");
                chargeCableButton.setChecked(chargeCableIsConnected);
                chargeCableButton.setVisibility(View.INVISIBLE);
                chargingIndicator.setVisibility(View.GONE);
                findViewById(R.id.tuningProgressBar).setVisibility(View.GONE);
                findViewById(R.id.tuningTextView).setVisibility(View.GONE);
                findViewById(R.id.connect_sim_button).setEnabled(true);
            }
        });
        flirOneDevice = device;
        orientationEventListener.disable();
    }

    /**
     * If using RenderedImage.ImageType.ThermalRadiometricKelvinImage, you should not rely on
     * the accuracy if tuningState is not Device.TuningState.Tuned
     *
     * @param tuningState
     */
    public void onTuningStateChanged(Device.TuningState tuningState) {
        Log.i("ExampleApp", "Tuning state changed changed!");

        currentTuningState = tuningState;
        if (tuningState == Device.TuningState.InProgress) {
            runOnUiThread(new Thread() {
                @Override
                public void run() {
                    super.run();
                    findViewById(R.id.tuningProgressBar).setVisibility(View.VISIBLE);
                    findViewById(R.id.tuningTextView).setVisibility(View.VISIBLE);
                }
            });
        } else {
            runOnUiThread(new Thread() {
                @Override
                public void run() {
                    super.run();
                    findViewById(R.id.tuningProgressBar).setVisibility(View.GONE);
                    findViewById(R.id.tuningTextView).setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onAutomaticTuningChanged(boolean deviceWillTuneAutomatically) {

    }

    private ColorFilter originalChargingIndicatorColor = null;

    @Override
    public void onBatteryChargingStateReceived(final Device.BatteryChargingState batteryChargingState) {
        Log.i("ExampleApp", "Battery charging state received!");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView chargingIndicator = (ImageView) findViewById(R.id.batteryChargeIndicator);
                if (originalChargingIndicatorColor == null) {
                    originalChargingIndicatorColor = chargingIndicator.getColorFilter();
                }
                switch (batteryChargingState) {
                    case FAULT:
                    case FAULT_HEAT:
                        chargingIndicator.setColorFilter(Color.RED);
                        chargingIndicator.setVisibility(View.VISIBLE);
                        break;
                    case FAULT_BAD_CHARGER:
                        chargingIndicator.setColorFilter(Color.DKGRAY);
                        chargingIndicator.setVisibility(View.VISIBLE);
                    case MANAGED_CHARGING:
                        chargingIndicator.setColorFilter(originalChargingIndicatorColor);
                        chargingIndicator.setVisibility(View.VISIBLE);
                        break;
                    case NO_CHARGING:
                    default:
                        chargingIndicator.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    @Override
    public void onBatteryPercentageReceived(final byte percentage) {
        Log.i("ExampleApp", "Battery percentage received!");

        final TextView levelTextView = (TextView) findViewById(R.id.batteryLevelTextView);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                levelTextView.setText(String.valueOf((int) percentage) + "%");
            }
        });


    }

    // StreamDelegate method
    public void onFrameReceived(Frame frame) {
        Log.v("ExampleApp", "Frame received!");

        if (currentTuningState != Device.TuningState.InProgress) {
            frameProcessor.processFrame(frame, FrameProcessor.QueuingOption.CLEAR_QUEUED);
            thermalSurfaceView.requestRender();
        }
    }

    private Bitmap thermalBitmap = null;
    NumberFormat numberFormat = NumberFormat.getInstance();

    // Frame Processor Delegate method, will be called each time a rendered frame is produced
    public void onFrameProcessed(final RenderedImage renderedImage) {
        if (renderedImage.imageType() == RenderedImage.ImageType.ThermalRadiometricKelvinImage) {
            // Note: this code is not optimized
            int[] thermalPixels = renderedImage.thermalPixelValues();
            // average the center 9 pixels for the spot meter
            int width = renderedImage.width();
            int height = renderedImage.height();
            int centerPixelIndex = width * ((height + topy) / 2) + ((width + leftx) / 2);
            int[] centerPixelIndexes = new int[]{
                    centerPixelIndex, centerPixelIndex - 1, centerPixelIndex + 1,
                    centerPixelIndex - width,
                    centerPixelIndex - width - 1,
                    centerPixelIndex - width + 1,
                    centerPixelIndex + width,
                    centerPixelIndex + width - 1,
                    centerPixelIndex + width + 1
            };

            double averageTemp = 0;

            for (int i = 0; i < centerPixelIndexes.length; i++) {
                // Remember: all primitives are signed, we want the unsigned value,
                // we've used renderedImage.thermalPixelValues() to get unsigned values
                int pixelValue = (thermalPixels[centerPixelIndexes[i]]);
                averageTemp += (((double) pixelValue) - averageTemp) / ((double) i + 1);
            }
            double averageC = (averageTemp / 100) - 273.15;
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            final String spotMeterValue = numberFormat.format(averageC) + "ºC";
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView) findViewById(R.id.spotMeterValue)).setText(spotMeterValue);
                    handler.obtainMessage(2).sendToTarget();
                }
            });
        }

        /*
        Capture this image if requested.
        */
        if (this.imageCaptureRequested) {
            Log.i("CONG", "onFrameProcessed:2 ");
            imageCaptureRequested = false;
            final Context context = this;
            new Thread(new Runnable() {
                public void run() {
                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ssZ", Locale.getDefault());
                    String formatedDate = sdf.format(new Date());
                    String fileName = "FLIROne-" + formatedDate + ".jpg";

                    try {
                        lastSavedPath = path + "/" + fileName;

                        renderedImage.getFrame().save(new File(lastSavedPath), frameProcessor);
                        if (!mIsStop) {
//                            MediaScannerConnection.scanFile(context,
//                                    new String[]{path + "/" + fileName}, null,
//                                    new MediaScannerConnection.OnScanCompletedListener() {
//                                        @Override
//                                        public void onScanCompleted(String path, Uri uri) {
//                                            Log.i("ExternalStorage", "Scanned " + path + ":");
//                                            Log.i("ExternalStorage", "-> uri=" + uri);
//                                        }
//
//                                    });
                            text = ((TextView) findViewById(R.id.spotMeterValue)).getText();
                            Bitmap bitmap = WaterMark.getSDCardImg(lastSavedPath);
                            Bitmap center = WaterMark.createWaterMaskCenter(bitmap, BitmapFactory.decodeResource(getResources(), R.drawable.ic_thermal), leftx, topy);
                            Bitmap bitmap2 = WaterMark.drawTextToCenter(GLPreviewActivity.this, center, text + "", 14, 0xFF00008b, leftx, topy);
                            String string = WaterMark.dateToString();
                            bitmap1 = WaterMark.drawTextToLeftBottom(GLPreviewActivity.this, bitmap2, string, 10, 0xFFFFFFFF);
                            mFileName = WaterMark.saveBitmap(GLPreviewActivity.this, bitmap1);
                            WaterMark.savePhoto(GLPreviewActivity.this, bitmap1);
                            handler.obtainMessage(1).sendToTarget();
                        }
                        mIsStop = false;
//                        Drawable drawable = new BitmapDrawable(bitmap1);
//                        mImgThermal.setBackground(drawable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    //private SystemUiHider mSystemUiHider;
    public void onTuneClicked(View v) {
        if (flirOneDevice != null) {
            flirOneDevice.performTuning();
        }

    }

    public void onCaptureImageClicked(View v) {
        if (flirOneDevice != null) {
            this.imageCaptureRequested = true;
        }
    }

    public void onConnectSimClicked(View v) {
        if (flirOneDevice == null) {
            try {
                flirOneDevice = new SimulatedDevice(this, this, getResources().openRawResource(R.raw.sampleframes), 10);
                flirOneDevice.setPowerUpdateDelegate(this);
                chargeCableIsConnected = true;
            } catch (Exception ex) {
                flirOneDevice = null;
                Log.w("FLIROneExampleApp", "IO EXCEPTION");
                ex.printStackTrace();
            }
        } else if (flirOneDevice instanceof SimulatedDevice) {
            flirOneDevice.close();
            flirOneDevice = null;
        }
    }

    public void onSimulatedChargeCableToggleClicked(View v) {
        if (flirOneDevice instanceof SimulatedDevice) {
            chargeCableIsConnected = !chargeCableIsConnected;
            ((SimulatedDevice) flirOneDevice).setChargeCableState(chargeCableIsConnected);
        }
    }

    public void onRotateClicked(View v) {
        ToggleButton theSwitch = (ToggleButton) v;
        if (theSwitch.isChecked()) {
            thermalSurfaceView.setRotation(180);
        } else {
            thermalSurfaceView.setRotation(0);
        }
    }

    public void onChangeViewClicked(View v) {
        if (frameProcessor == null) {
            ((ToggleButton) v).setChecked(false);
            return;
        }
        ListView paletteListView = (ListView) findViewById(R.id.paletteListView);
        ListView imageTypeListView = (ListView) findViewById(R.id.imageTypeListView);
        if (((ToggleButton) v).isChecked()) {
            // only show palette list if selected image type is colorized
            paletteListView.setVisibility(View.INVISIBLE);
            for (RenderedImage.ImageType imageType : frameProcessor.getImageTypes()) {
                if (imageType.isColorized()) {
                    paletteListView.setVisibility(View.VISIBLE);
                    break;
                }
            }
            imageTypeListView.setVisibility(View.VISIBLE);
            findViewById(R.id.imageTypeListContainer).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.imageTypeListContainer).setVisibility(View.GONE);
        }


    }

    public void onImageTypeListViewClicked(View v) {
        int index = ((ListView) v).getSelectedItemPosition();
        RenderedImage.ImageType imageType = RenderedImage.ImageType.values()[index];
        frameProcessor.setGLOutputMode(imageType);
        int paletteVisibility = (imageType.isColorized()) ? View.VISIBLE : View.GONE;
        findViewById(R.id.paletteListView).setVisibility(paletteVisibility);
    }

    public void onPaletteListViewClicked(View v) {
        RenderedImage.Palette pal = (RenderedImage.Palette) (((ListView) v).getSelectedItem());
        frameProcessor.setImagePalette(pal);
    }

    /**
     * Example method of starting/stopping a frame stream to a host
     *
     * @param v The toggle button pushed
     */
    public void onVividClicked(View v) {
        final ToggleButton button = (ToggleButton) v;
        frameProcessor.setVividIrEnabled(button.isChecked());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mIsStop) {
            if (flirOneDevice != null) {
                this.imageCaptureRequested = true;
            }
        }
        Log.i("Cong", "onStart: 0");
        if (Device.getSupportedDeviceClasses(this).contains(FlirUsbDevice.class)) {
            mProgressDialog.dismiss();
            Toast.makeText(this, R.string.thermal_glpreview_toast_support_text, Toast.LENGTH_SHORT).show();
            findViewById(R.id.pleaseConnect).setVisibility(View.VISIBLE);
        }
        try {
            Device.startDiscovery(this, this);
        } catch (IllegalStateException e) {
            // it's okay if we've already started discovery
            Log.i("CONG", "onStart: " + e);
        } catch (SecurityException e) {
            // On some platforms, we need the user to select the app to give us permisison to the USB device.
            Toast.makeText(this, "Please insert FLIR One and select " + getString(R.string.app_name), Toast.LENGTH_LONG).show();
            // There is likely a cleaner way to recover, but for now, exit the activity and
            // wait for user to follow the instructions;
            finish();
        }
    }

    ScaleGestureDetector mScaleDetector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(this).inflate(R.layout.activity_gl_preview, null);
        setContentView(view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // getSupportActionBar().hide();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View controlsViewTop = findViewById(R.id.fullscreen_content_controls_top);
        final View contentView = findViewById(R.id.fullscreen_content);
        layout = findViewById(R.id.ll_count);
        layout.setVisibility(View.GONE);
        controlsView.setVisibility(View.GONE);
        controlsViewTop.setVisibility(View.GONE);
        mImgThermal = findViewById(R.id.img_thermal);
        mIcBack = findViewById(R.id.ic_back);
        RenderedImage.ImageType defaultImageType = RenderedImage.ImageType.BlendedMSXRGBA8888Image;
        frameProcessor = new FrameProcessor(this, this, EnumSet.of(RenderedImage.ImageType.ThermalRadiometricKelvinImage), true);
        frameProcessor.setGLOutputMode(defaultImageType);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.thermal_glpreview_progress_text));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        thermalSurfaceView = (GLSurfaceView) findViewById(R.id.imageView);
        thermalSurfaceView.setPreserveEGLContextOnPause(true);
        thermalSurfaceView.setEGLContextClientVersion(2);
        thermalSurfaceView.setRenderer(frameProcessor);
        thermalSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        thermalSurfaceView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR | GLSurfaceView.DEBUG_LOG_GL_CALLS);
        final String[] imageTypeNames = new String[]{"Visible", "Thermal", "MSX"};
        final RenderedImage.ImageType[] imageTypeValues = new RenderedImage.ImageType[]{
                RenderedImage.ImageType.VisibleAlignedRGBA8888Image,
                RenderedImage.ImageType.ThermalRGBA8888Image,
                RenderedImage.ImageType.BlendedMSXRGBA8888Image,
        };
        mFlDrag = findViewById(R.id.fl_drag);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mFlDrag.getLayoutParams();
        lp.setMargins(325, 555, 0, 0);
        mFlDrag.setLayoutParams(lp);
        mFlDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // touch down so check if the
                        startX = x;
                        startY = y;
                        temp[0] = (int) event.getX();
                        temp[1] = y - v.getTop();
                        break;
                    case MotionEvent.ACTION_MOVE: // touch drag with the ball
                        left = x - temp[0];
                        top = y - temp[1];
                        if (left < 0) {//控制左边界不超出
                            left = 0;
                        }
                        if (top < 100) {
                            top = 100;
                        }
                        if (top > 980) {
                            top = 980;
                        }
                        leftx = (int) ((left - 325) * 1.4);
                        topy = (int) ((top - 555) * 1.4);
                        v.layout(left, top, left + v.getWidth(), top + v.getHeight());//自由拖拽
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(x - startX) > 2 || Math.abs(y - startY) > 2) {
                            //将最后拖拽的位置定下来，否则页面刷新渲染后按钮会自动回到初始位置
                            //注意父容器
                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
                            lp.setMargins(left, top, 0, 0);
                            v.setLayoutParams(lp);
                            //确定是拖拽
                            //isMove = true;
                        }
                        break;
                }
                return true;
            }
        });
        ListView imageTypeListView = ((ListView) findViewById(R.id.imageTypeListView));
        imageTypeListView.setAdapter(new ArrayAdapter<>(this, R.layout.emptytextview, imageTypeNames));
        imageTypeListView.setSelection(defaultImageType.ordinal());
        imageTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (frameProcessor != null) {
                    RenderedImage.ImageType imageType = imageTypeValues[position];
                    frameProcessor.setGLOutputMode(imageType);
                    if (imageType.isColorized()) {
                        findViewById(R.id.paletteListView).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.paletteListView).setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        imageTypeListView.setDivider(null);
        mImgThermal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsBack = false;
                layout.setVisibility(View.VISIBLE);
                android.app.FragmentManager manager = getFragmentManager();
                android.app.FragmentTransaction transaction = manager.beginTransaction();
                ThermalFragment thermalFragment = new ThermalFragment();
                transaction.replace(R.id.ll_count, thermalFragment);
                transaction.commit();

            }
        });
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("CW", mFileName);
//                intent.putExtra("float", text);
//                setResult(4, intent);
                finish();
            }
        });
        // Palette List View Setup
        ListView paletteListView = ((ListView) findViewById(R.id.paletteListView));
        paletteListView.setDivider(null);
        paletteListView.setAdapter(new ArrayAdapter<>(this, R.layout.emptytextview, RenderedImage.Palette.values()));
        paletteListView.setSelection(frameProcessor.getImagePalette().ordinal());
        paletteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (frameProcessor != null) {
                    frameProcessor.setImagePalette(RenderedImage.Palette.values()[position]);
                }
            }
        });

        findViewById(R.id.change_view_button).setOnTouchListener(mDelayHideTouchListener);


        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                deviceRotation = orientation;
            }
        };
        mScaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Log.d("ZOOM", "zoom ongoing, scale: " + detector.getScaleFactor());
                frameProcessor.setMSXDistance(detector.getScaleFactor());
                return false;
            }
        });

        findViewById(R.id.fullscreen_content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    public void onComplete(View v) {
        Intent intent = new Intent();
        intent.putExtra("CW", mFileName);
        if (text == null || text.equals("")) {
            text = ((TextView) findViewById(R.id.spotMeterValue)).getText();
        }
        intent.putExtra("float", text);
        setResult(4, intent);
        finish();
    }

    public void setView() {
        layout.setVisibility(View.GONE);
        mIsBack = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        thermalSurfaceView.onPause();
//        if (flirOneDevice != null) {
//            flirOneDevice.stopFrameStream();
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        List<String> sd = getImagePathFromSD();
        if (sd.size() > 0) {
            Glide.with(this).load(sd.get(0)).into(mImgThermal);
        }
        thermalSurfaceView.onResume();
        if (flirOneDevice != null) {
            flirOneDevice.startFrameStream(this);
        }
    }

    @Override
    public void onStop() {
        // We must unregister our usb receiver, otherwise we will steal events from other apps
        Log.e("PreviewActivity", "onStop, stopping discovery!");
//        Device.stopDiscovery();
//        flirOneDevice = null;
        mIsStop = true;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (flirOneDevice != null) {
            flirOneDevice.stopFrameStream();
        }
        Device.stopDiscovery();
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mIsBack) {
//                Intent intent = new Intent();
//                intent.putExtra("CW", mFileName);
//                intent.putExtra("float", text);
//                setResult(4, intent);
                finish();
            } else {
                setView();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            //mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private List<String> getImagePathFromSD() {
        // 图片列表
        List<String> imagePathList = new ArrayList<String>();
        // 得到sd卡内image文件夹的路径   File.separator(/)
        List<File> fileList = new ArrayList<File>();
        String fileName = getExternalCacheDir().getPath();
        if (fileName != null) {
            File dir = new File(fileName + "/recordtest/thermal");
            if (!dir.exists()) {
                dir.mkdir();
            }
            fileName = dir + "/";
        }
        // 得到该路径文件夹下所有的文件
        File fileAll = new File(fileName);
        if (fileAll.exists() && fileAll.isDirectory()) {
            File[] files = fileAll.listFiles();
            for (int i = 0; i < files.length; i++) {
                fileList.add(files[i]);
            }
            Collections.sort(fileList, new FileComparator());
        }
        for (int i = 0; i < fileList.size(); i++) {
            imagePathList.add(fileList.get(i).getPath());
        }
        // 返回得到的图片列表
        return imagePathList;
    }

}
