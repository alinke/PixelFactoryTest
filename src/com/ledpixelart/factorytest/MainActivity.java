package com.ledpixelart.factorytest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.os.Parcel;
//import android.os.Parcelable;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO.VersionType;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import alt.android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.OpenableColumns;
import android.provider.Settings;
//import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
//import android.view.GestureDetector;
//import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
//import android.hardware.SensorManager;

//import android.app.IntentService;
//import android.content.Intent;
//import android.content.BroadcastReceiver;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;

import java.util.UUID;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.ActionBarActivity;


//to do , think about deleting the decoded directory if led panel size changed

@SuppressLint({ "ParserError", "ParserError" })
public class MainActivity extends IOIOActivity implements OnItemClickListener, OnItemLongClickListener  {

	private static GifView gifView;
	private static ioio.lib.api.RgbLedMatrix matrix_;
	private static ioio.lib.api.RgbLedMatrix.Matrix KIND;  //have to do it this way because there is a matrix library conflict
	private static android.graphics.Matrix matrix2;
    private static final String TAG = "PixelFactory";	
	private static short[] frame_;
  	public static final Bitmap.Config FAST_BITMAP_CONFIG = Bitmap.Config.RGB_565;
  	private static byte[] BitmapBytes;
  	private static InputStream BitmapInputStream;
  	private static Bitmap canvasBitmap;
  	private static Bitmap IOIOBitmap;
  	private static Bitmap originalImage;
  	private static int width_original;
  	private static int height_original; 	  
  	private static float scaleWidth; 
  	private static float scaleHeight; 	  	
  	private static Bitmap resizedBitmap;  	
  	private static int deviceFound = 0;
  	
  	private SharedPreferences prefs;
	private String OKText;
	private Resources resources;
	private String app_ver;	
	private static  int matrix_model;
	private final String tag = "";	
	private final String LOG_TAG = "PixelFactory";
	private static String imagePath;
	private static int resizedFlag = 0;
	
	private ConnectTimer connectTimer; 	
    private static DecodedTimer decodedtimer; 
	private Canvas canvas;
	private static Canvas canvasIOIO;
	
	private static String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
    private static String basepath = extStorageDirectory;
   
    private static String decodedDirPath =  Environment.getExternalStorageDirectory() + "/pixel/factorytestgif/decoded"; //we'll change later for 64x64
    
    private static String GIFPath =  Environment.getExternalStorageDirectory() + "/pixel/factorytestgif/"; //put the pngs (for display purposes) and the gifs together in this same dir, code should take the png if it exists, otherwise take the gif
    private static String GIF64Path =  Environment.getExternalStorageDirectory() + "/pixel/factorytestgif64/";  //gifs 64x64, there will be a decoded directory here
    
    private static String whiteTestFileName = "whitetest";
    private static String whiteTestFileName64 = "whitetest64";
    private static String writeDemoFileName = "writedemo";
    private static String writeDemoFileName64 = "writedemo64";
    private static Context context;
    private Context frameContext;
    private GridView sdcardImages;
	
	///********** Timers
    //private MediaScanTimer mediascanTimer; 	
	private boolean noSleep = false;	
	private int countdownCounter;
	private static final int countdownDuration = 30;
	private static final int REQUEST_CODE_CHOOSE_PICTURE_FROM_GALLERY = 22;
	private static final int WENT_TO_PREFERENCES = 1;
	private static final int REQUEST_PAIR_DEVICE = 10;
	private static final int REQUEST_IMAGE_CAPTURE = 40;
	private Display display;
	private Cursor cursor;
	private int size;  //the number of pictures
	private ProgressDialog pDialog = null;
	private int columnIndex; 
	private boolean debug_;
	private static int appAlreadyStarted = 0;
	private int FPSOverride_ = 0;
	private static float fps = 0;
	private static int x = 0;
	private static int downloadCounter = 0;
	private static String selectedFileName;
	private static int selectedFileTotalFrames;
	private static int selectedFileDelay;
	private static int StreamModePlaying = 0;
	private static int selectedFileResolution;
	private static int currentResolution;
	private static String pixelFirmware = "Not Connected";
	private static String pixelBootloader = "Not Connected";
	private static String pixelHardwareID = "Not Connected";
	private static String IOIOLibVersion = "Not Connected";
	private static VersionType v;
	private static  ByteBuffer buffer; //Create a new buffer
	private int appCode;
	private static boolean mRunning = true;
	private static boolean readyForLocalPlayBack = false;
	public static Context baseContext;
	//private static MainActivity mainActivity2 = new MainActivity();  //had to add this due some context requirements
	private  ProgressDialog progress;
	public long frame_length;
    private RandomAccessFile raf = null;
	private File file;
	private int readytoWrite = 0;
	private static int matrix_number;
	private  AsyncTaskLoadFiles myAsyncTaskLoadFiles;
	private ImageAdapter2 myImageAdapter;
	private GridView gridview;
	private static boolean kioskMode_ = false;
	private String originalImagePath;
	private boolean gifonly_ = false;
	private boolean only64_ = false;
	private boolean showStartupMsg_ = true;
	private String gifPath_;
	//private String downloadURL_32;
	//private String downloadURL_64;
	private boolean saveMultipleCameraPics_;
	private boolean writeCameraFlag_ = false;
	private Bitmap cameraBMP;
	private TextView textView_;
	private TextView ProxTextView_;
	private TextView AlcoholTextView_;
	private TextView i2cText_;
	private static ImageButton startButton_;
	private static MainActivity instance = null;
	private static int whiteTestDone = 0;
	private RadioGroup pixelRadioGroup_ = null;
	private static RadioButton pixelRadio_;
	private static RadioButton superPixelRadio_;
	
	private TextView Grove1_6TextView;
	private TextView Grove1_35TextView;
	private TextView Grove2_4TextView;
	private TextView Grove2_5TextView;
	private TextView Grove3_31TextView;
	private TextView Grove3_32TextView;
	private TextView Grove4_33TextView;
	private TextView Grove4_34TextView;
	private TextView Grove5_1TextView;
	private TextView Grove5_2TextView;
	
	private MediaPlayer beginTestSound;
	private MediaPlayer checkWhiteLEDsSound;
	private static MediaPlayer endTestSound;
	private static MediaPlayer areLEDsWhite;
	private static MediaPlayer sdIOTest;
	private static MediaPlayer alertSound;
	private static MediaPlayer restartApp;
	private int height;
	private String CurrentFirmwareVersion = "PIXL0008";
	private OnSharedPreferenceChangeListener matrixChangedListener;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.instance = this;
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		 setContentView(R.layout.main);
	      display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	        
	      ///new gridview code
	     gridview = (GridView) findViewById(R.id.gridview);
	     myImageAdapter = new ImageAdapter2(this);
	     gridview.setAdapter(myImageAdapter);
	      ///*******************
	     
	     // gridview.setKeepScreenOn(true);
		 
	        gifView = (GifView) findViewById(R.id.gifView); //gifview takes care of the gif decoding
	        gifView.setGif(R.drawable.zzzblank);  //code will crash if a dummy gif is not loaded initially
	     // proxTextView_ = (TextView)findViewById(R.id.proxTextView);
	      
	       // textView_ = (TextView) findViewById(R.id.TextView);
	       
		//	toggleButton_ = (ToggleButton) findViewById(R.id.ToggleButton);
		//	ProxTextView_ = (TextView) findViewById(R.id.ProxTextView);
		//	AlcoholTextView_ = (TextView) findViewById(R.id.AlcoholTextView);
			startButton_ = (ImageButton)findViewById(R.id.startButton);
	      
			Grove1_6TextView = (TextView) findViewById(R.id.Grove1_6);
			Grove1_35TextView = (TextView) findViewById(R.id.Grove1_35);
			Grove2_4TextView = (TextView) findViewById(R.id.Grove2_4);
			Grove2_5TextView = (TextView) findViewById(R.id.Grove2_5);
			Grove3_31TextView = (TextView) findViewById(R.id.Grove3_31);
			Grove3_32TextView = (TextView) findViewById(R.id.Grove3_32);
			Grove4_33TextView = (TextView) findViewById(R.id.Grove4_33);
			Grove4_34TextView = (TextView) findViewById(R.id.Grove4_34);
			Grove5_1TextView = (TextView) findViewById(R.id.Grove5_1);
			Grove5_2TextView = (TextView) findViewById(R.id.Grove5_2);
			
			Grove1_35TextView.setTextColor(getResources().getColor(R.color.green)); //the analog pins get a different color
			Grove3_32TextView.setTextColor(getResources().getColor(R.color.green)); //the analog pins get a differnet color
		
			//height = getResources().getDisplayMetrics().heightPixels;
			
		//let's get the app version so we'll know if we need to add new animations to the user's app   
	        PackageInfo pinfo;
			try {
				pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
				  appCode = pinfo.versionCode;
			     String appVersion = pinfo.versionName;
			      
			} catch (NameNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	     
	    
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        try
        {
            app_ver = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        }
        catch (NameNotFoundException e)
        {
            Log.v(tag, e.getMessage());
        }
        
     
        
        pixelRadioGroup_ = (RadioGroup) findViewById(R.id.pixelRadioGroup);
        pixelRadio_ = (RadioButton) findViewById(R.id.pixelRadio);
        superPixelRadio_ = (RadioButton) findViewById(R.id.superPixelRadio);
        
        pixelRadio_.setEnabled(false);
        superPixelRadio_.setEnabled(false);
        
        matrixChangedListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			  public void onSharedPreferenceChanged(SharedPreferences prefs, String selected_matrix) {
			  // showToast("matrix model:" + matrix_model);
			   setPreferences();  
			    if (matrix_model == 3) {
			    	 pixelRadio_.setChecked(true);
			    	 superPixelRadio_.setChecked(false);
			    }
			    else { //it's super pixel
			    	pixelRadio_.setChecked(false);
			    	superPixelRadio_.setChecked(true);
			    }
			    
			   showToast(getString(R.string.LEDMatrixChanged));
			   restartApp.start(); //play the sound
			   startButton_.setVisibility(View.INVISIBLE);
			    
			  }
		};
			
	    prefs.registerOnSharedPreferenceChangeListener(matrixChangedListener);
        
        //******** preferences code
        resources = this.getResources();
        setPreferences();
        //***************************
        
    //    if (noSleep == true) {        	      	
        	this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //disables sleep mode
    //    }	
        
        connectTimer = new ConnectTimer(30000,5000); //pop up a message if it's not connected by this timer
 		connectTimer.start(); //this timer will pop up a message box if the device is not found
 		
 		context = getApplicationContext();
 		baseContext = getBaseContext();
 		
 		 if ( Locale.getDefault().getLanguage().equals("zh") || Locale.getDefault().getLanguage().equals("zh-CH") || Locale.getDefault().getLanguage().equals("zh-TW")) 
 	    	  beginTestSound = MediaPlayer.create(MainActivity.this, R.raw.beginning_test_chinese);
 	     else beginTestSound = MediaPlayer.create(MainActivity.this, R.raw.beginning_test_english);
 	    	  
 	      if ( Locale.getDefault().getLanguage().equals("zh") || Locale.getDefault().getLanguage().equals("zh-CH") || Locale.getDefault().getLanguage().equals("zh-TW")) 
 	    	  checkWhiteLEDsSound = MediaPlayer.create(MainActivity.this, R.raw.check_leds_white_chinese);
 	     else checkWhiteLEDsSound = MediaPlayer.create(MainActivity.this, R.raw.check_leds_white_english);
 	      
 	      if ( Locale.getDefault().getLanguage().equals("zh") || Locale.getDefault().getLanguage().equals("zh-CH")|| Locale.getDefault().getLanguage().equals("zh-TW")) 
 	    	  endTestSound = MediaPlayer.create(MainActivity.this, R.raw.testing_complete_chinese);
 	     else endTestSound = MediaPlayer.create(MainActivity.this, R.raw.testing_complete_english);
 	      
 	      if ( Locale.getDefault().getLanguage().equals("zh") || Locale.getDefault().getLanguage().equals("zh-CH")|| Locale.getDefault().getLanguage().equals("zh-TW")) 
 	    	  areLEDsWhite = MediaPlayer.create(MainActivity.this, R.raw.are_leds_white_chinese);
 	     else areLEDsWhite = MediaPlayer.create(MainActivity.this, R.raw.are_leds_white_english);
 	      
 	      if ( Locale.getDefault().getLanguage().equals("zh") || Locale.getDefault().getLanguage().equals("zh-CH")|| Locale.getDefault().getLanguage().equals("zh-TW")) 
 	    	  sdIOTest = MediaPlayer.create(MainActivity.this, R.raw.sd_and_io_test_chinese);
 	     else sdIOTest = MediaPlayer.create(MainActivity.this, R.raw.sd_and_io_test_english);
 	      
 	      if ( Locale.getDefault().getLanguage().equals("zh") || Locale.getDefault().getLanguage().equals("zh-CH")|| Locale.getDefault().getLanguage().equals("zh-TW")) 
 	    	  alertSound = MediaPlayer.create(MainActivity.this, R.raw.alert);
 	     else alertSound = MediaPlayer.create(MainActivity.this, R.raw.alert);
 	      
 	      if ( Locale.getDefault().getLanguage().equals("zh") || Locale.getDefault().getLanguage().equals("zh-CH")|| Locale.getDefault().getLanguage().equals("zh-TW")) 
 	    	  restartApp = MediaPlayer.create(MainActivity.this, R.raw.restart_app_chinese);
 	     else restartApp = MediaPlayer.create(MainActivity.this, R.raw.restart_app_english);
 		
 		
        
 		
      // superPixelRadio_.setOnClickListener(listener);

      /* pixelRadioGroup_.setOnCheckedChangeListener(new OnCheckedChangeListener() 
       {
           public void onCheckedChanged(RadioGroup group, int checkedId) {
               switch(checkedId){
                   case R.id.pixelRadio:
                	// matrix_model = 3;
                	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_32x32; //v2
        	    	 BitmapInputStream = getResources().openRawResource(R.raw.selectimage32);
        	    	 frame_length = 2048;
        	    	 currentResolution = 32;
          	  	     selectedFileResolution = 32;
          	  
                   break;

                   case R.id.superPixelRadio:
                	 matrix_model = 10;
                	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_64x64;
          	    	 BitmapInputStream = getResources().openRawResource(R.raw.select64by64);
          	    	 frame_length = 8192;
          	    	 currentResolution = 128; 
          	    	 selectedFileResolution = 128;
                   break;
               }
           }
       });*/
       
     
  
 		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {

            extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            
            	File artdir = new File(GIFPath);
            	
	            if (!artdir.exists()) { //no directory so let's now start the one time setup
	            	//sdcardImages.setVisibility(View.INVISIBLE); //hide the images as they're not loaded so we can show a splash screen instead
	            	//showToast(getResources().getString(R.string.oneTimeSetupString)); //replaced by direct text on view screen
	            	
	            	new copyFilesAsync().execute();
	               
	            }
	            else { //the directory was already there so no need to copy files or do a media re-scan so just continue on
	            	
	            	//now let's check the app version
	            	//if (appCode < 23) {  //if true, we need to copy some more animations
	            	//	
	            	//}
	            	//else {
	            	
	            	continueOnCreate();
	            	//}
	            }

      //  } else if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED_READ_ONLY)) {

           // showToast("Sorry, your device does not have an accessible SD card, this app will not work");//Or use your own method ie: Toast
      //  }
	            
        } else  {
        	AlertDialog.Builder alert=new AlertDialog.Builder(this);
 	      	alert.setTitle("No SD Card").setIcon(R.drawable.icon).setMessage("Sorry, your device does not have an accessible SD card, this app needs to copy some images to your SD card and will not work without it.\n\nPlease exit this app and go to Android settings and check that your SD card is mounted and available and then restart this app.\n\nNote for devices that don't have external SD cards, this app will utilize the internal SD card memory but you are most likely seeing this message because your device does have an external SD card slot.").setNeutralButton("OK", null).show();
            //showToast("Sorry, your device does not have an accessible SD card, this app will not work");//Or use your own method ie: Toast
        }
	}

	@SuppressLint("NewApi")
	 private class copyFilesAsync extends AsyncTask<Void, Integer, Void>{
		 
	     int progress_status;
	      
	     @Override
	  protected void onPreExecute() {
		   // update the UI immediately after the task is executed
		   super.onPreExecute();
		   
		   progress = new ProgressDialog(MainActivity.this);
		  
	       //progress.setMax(selectedFileTotalFrames);
		   progress.setTitle(getString(R.string.oneTimeSetup));
	      // progress.setTitle("One Time Setup");
		   progress.setMessage(getString(R.string.oneTimeSetupBody));
		   //progress.setMessage("Copying pixel art to your local memory....");
	       progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	       //progress.getWindow().getAttributes().verticalMargin = 0.8F;
	       
	       
	    /*   progress.getWindow().setGravity(Gravity.BOTTOM);
	       progress.getWindow().setGravity(Gravity.CENTER);
	       WindowManager.LayoutParams wmlp = progress.getWindow().getAttributes();
	       wmlp.y = height / 8;
	       progress.getWindow().setAttributes(wmlp);*/
	      
	       progress.show();
	 
	    progress_status = 0;
	    
	  }
	      
	  @Override
	  protected Void doInBackground(Void... params) {
		  	
			
			File artdir = new File(GIFPath);
			artdir.mkdirs();	
          
            File decodeddir = new File(GIFPath + "decoded");
		  	decodeddir.mkdirs();
		  	
		  	File gifSourcedir = new File(GIFPath + "gifsource");
			if (!gifSourcedir.exists()) {
				gifSourcedir.mkdirs();
		  	}
			
			File GIF64dir = new File(GIF64Path);
		  	if (!GIF64dir.exists()) {
		  		GIF64dir.mkdirs();
		  	}
			
			File gif64Sourcedir = new File(GIF64Path + "gifsource");
			if (!gif64Sourcedir.exists()) {
				gif64Sourcedir.mkdirs();
		  	}
			
			File GIF64decodeddir = new File(GIF64Path + "decoded");
		  	if (!GIF64decodeddir.exists()) {
		  		GIF64decodeddir.mkdirs();
		  	}
			
		  	copyArt(); //copy the .png and .gif files (mainly png) because we want to decode first
		  	copyGIFDecoded();  //copy the decoded files
			copyGIF64();
			copyGIFSource();
			copyGIF64Source();
			copyGIF64Decoded();
			
	   return null;
	  }
	  
	  @Override
	  protected void onProgressUpdate(Integer... values) {
	   super.onProgressUpdate(values);
	   progress.incrementProgressBy(1);
	    
	  }
	   
	  @Override
	  protected void onPostExecute(Void result) {
	   super.onPostExecute(result);
	   progress.dismiss(); //we're done so now hide the progress update box
	   continueOnCreate();
	   // btn_start.setEnabled(true);
	  }
	  
		//********** the copy functions
	  
		private void copyArt() {
	    	
	    	AssetManager assetManager = getResources().getAssets();
	        String[] files = null;
	        try {
	           files = assetManager.list("factorytestgif");
	           //files = assetManager.list(GIFname); //not sure why but putting a variable here doesn't work, only works if you put the string, weird...
	        } catch (Exception e) {
	            Log.e("read clipart ERROR", e.toString());
	            e.printStackTrace();
	        }
	        
	        //let's get the total numbers of files here and set the progress bar
	        progress.setMax(files.length*3);
	        
	        for(int i=0; i<files.length; i++) {
	            InputStream in = null;
	            OutputStream out = null;
	            try {
	             // in = assetManager.open("animatedgifs/" + files[i]);
	            //  out = new FileOutputStream(basepath + "/pixel/animatedgifs/" + files[i]);
	              
	             in = assetManager.open("factorytestgif/" + files[i]); //same thing, can't put a variable for gif
	            // out = new FileOutputStream(basepath + "/pixel/gif/" + files[i]);
	             // in = assetManager.open(GIFname +"/" + files[i]);
	             out = new FileOutputStream(GIFPath + files[i]);
	              
	              copyFile(in, out);
	              in.close();
	              in = null;
	              out.flush();
	              out.close();
	              out = null;    
	            
	           progress_status ++;
	  		   publishProgress(progress_status);  
	           
	            } catch(Exception e) {
	                Log.e("copy clipart ERROR", e.toString());
	                e.printStackTrace();
	            }       
	        }
	    }
		
		
		private void copyGIFDecoded() {
	    	
	    	AssetManager assetManager = getResources().getAssets();
	        String[] files = null;
	        try {
	            files = assetManager.list("factorytestgif/decoded");
	            //files2 = assetManager.list(GIFname + "/decoded");
	        } catch (Exception e) {
	            Log.e("read clipart ERROR", e.toString());
	            e.printStackTrace();
	        }
	        for(int i=0; i<files.length; i++) {
	        	progress_status ++;
		  		publishProgress(progress_status);  
	            InputStream in = null;
	            OutputStream out = null;
	            try {
	             in = assetManager.open("factorytestgif/decoded/" + files[i]);
	             //out = new FileOutputStream(basepath + "/pixel/gif/decoded/" + files2[i]);
	             // in = assetManager.open(GIFname + "/decoded/" + files2[i]);
	             out = new FileOutputStream(GIFPath + "decoded/" + files[i]);
	              copyFile(in, out);
	              in.close();
	              in = null;
	              out.flush();
	              out.close();
	              out = null;  
	              
	              //no need to register these with mediascanner as these are internal gifs , the workaround for the gifs with a black frame as the first frame
	           
	            } catch(Exception e) {
	                Log.e("copy clipart ERROR", e.toString());
	                e.printStackTrace();
	            }       
	        }
	    } //end copy gif decoded
		
		private void copyGIFSource() {
	    	
	    	AssetManager assetManager = getResources().getAssets();
	        String[] files = null;
	        try {
	            files = assetManager.list("factorytestgif/gifsource");
	        } catch (Exception e) {
	            Log.e("read clipart ERROR", e.toString());
	            e.printStackTrace();
	        }
	        for(int i=0; i<files.length; i++) {
	        	progress_status ++;
		  		publishProgress(progress_status);  
	            InputStream in = null;
	            OutputStream out = null;
	            try {
	             in = assetManager.open("factorytestgif/gifsource/" + files[i]);
	             out = new FileOutputStream(GIFPath + "gifsource/" + files[i]);
	              copyFile(in, out);
	              in.close();
	              in = null;
	              out.flush();
	              out.close();
	              out = null;
	           
	            } catch(Exception e) {
	                Log.e("copy clipart ERROR", e.toString());
	                e.printStackTrace();
	            }       
	        }
	    } //end copy gifsource
		
private void copyGIF64Source() {
	    	
	    	AssetManager assetManager = getResources().getAssets();
	        String[] files = null;
	        try {
	            files = assetManager.list("factorytestgif64/gifsource");
	        } catch (Exception e) {
	            Log.e("read clipart ERROR", e.toString());
	            e.printStackTrace();
	        }
	        for(int i=0; i<files.length; i++) {
	        	progress_status ++;
		  		publishProgress(progress_status);  
	            InputStream in = null;
	            OutputStream out = null;
	            try {
	             in = assetManager.open("factorytestgif64/gifsource/" + files[i]);
	             out = new FileOutputStream(GIF64Path + "gifsource/" + files[i]);
	              copyFile(in, out);
	              in.close();
	              in = null;
	              out.flush();
	              out.close();
	              out = null;
	           
	            } catch(Exception e) {
	                Log.e("copy clipart ERROR", e.toString());
	                e.printStackTrace();
	            }       
	        }
	    } //end copy gifsource
		
  private void copyGIF64Decoded() {
	    	
	    	AssetManager assetManager = getResources().getAssets();
	        String[] files = null;
	        try {
	            files = assetManager.list("factorytestgif64/decoded");
	            //files2 = assetManager.list(GIFname + "/decoded");
	        } catch (Exception e) {
	            Log.e("read clipart ERROR", e.toString());
	            e.printStackTrace();
	        }
	        for(int i=0; i<files.length; i++) {
	        	progress_status ++;
		  		publishProgress(progress_status);  
	            InputStream in = null;
	            OutputStream out = null;
	            try {
	             in = assetManager.open("factorytestgif64/decoded/" + files[i]);
	             //out = new FileOutputStream(basepath + "/pixel/gif/decoded/" + files2[i]);
	             // in = assetManager.open(GIFname + "/decoded/" + files2[i]);
	             out = new FileOutputStream(GIF64Path + "decoded/" + files[i]);
	              copyFile(in, out);
	              in.close();
	              in = null;
	              out.flush();
	              out.close();
	              out = null;  
	              
	              //no need to register these with mediascanner as these are internal gifs , the workaround for the gifs with a black frame as the first frame
	           
	            } catch(Exception e) {
	                Log.e("copy clipart ERROR", e.toString());
	                e.printStackTrace();
	            }       
	        }
	    } //end copy gif decoded
		
  
   private void copyGIF64() {
	   	
	   	AssetManager assetManager = getResources().getAssets();
	       String[] files = null;
	       try {
	           files = assetManager.list("factorytestgif64");
	       } catch (Exception e) {
	           Log.e("read clipart ERROR", e.toString());
	           e.printStackTrace();
	       }
	       for(int i=0; i<files.length; i++) {
	       	progress_status ++;
		  		publishProgress(progress_status);  
	           InputStream in = null;
	           OutputStream out = null;
	           try {
	            in = assetManager.open("factorytestgif64/" + files[i]);
	            out = new FileOutputStream(GIF64Path + files[i]); 
	             copyFile(in, out);
	             in.close();
	             in = null;
	             out.flush();
	             out.close();
	             out = null;  
	             
	             //no need to register these with mediascanner as these are internal gifs , the workaround for the gifs with a black frame as the first frame
	          
	           } catch(Exception e) {
	               Log.e("copy clipart ERROR", e.toString());
	               e.printStackTrace();
	           }       
	       }
	   } //end copyGIF64
		
} //end async task
	    
    private void continueOnCreate() {
         
         
    	 //now let's load the files asynch
    	
    	 myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
         myAsyncTaskLoadFiles.execute();

         gridview.setVisibility(View.GONE);
         
       //  gridview.setOnItemClickListener(MainActivity.this);
       //  gridview.setOnItemLongClickListener(MainActivity.this);
         
         startButton_.setOnClickListener(new OnClickListener(){

          @Override
          public void onClick(View arg0) { //the user has started the test
           
           //Cancel the previous running task, if exist.
           myAsyncTaskLoadFiles.cancel(true);
           
           if (deviceFound == 1) {  //let's cancel out the buttons
        	   playWhiteTest();
               startButton_.setVisibility(View.GONE);
               checkWhiteLEDsSound.start();
               //pixelRadioGroup_.setVisibility(View.GONE);
              // pixelRadio_.setEnabled(false);
              // superPixelRadio_.setEnabled(false);
           }
           else {
        	   //showToast("PIXEL was not found, did you Bluetooth pair?");
        	   showToast(getString(R.string.pixelNotFound));
           }
           
          }});
         
    }
    
    private void playWhiteTest() {
    	
    	if (deviceFound == 1) {
            
    		
	  		//********we need to reset everything because the user could have been already running an animation
	  	     x = 0;
	  	     
	  	     if (StreamModePlaying == 1) {
	  	    		decodedtimer.cancel();
	  	     }
	  	     ///****************************
    		
        	//don't forget about 64x64
    		
    		/*imagePath = GIFPath + whiteTestFileName + ".gif";
			gifView.setGif(imagePath);  //just sets the image , no decoding, decoding happens in the animateafterdecode method
			selectedFileName = whiteTestFileName;
			showToast(getString(R.string.RunningWhiteTest));
	     	animateAfterDecode(0);*/
	     	
	     	if (matrix_model == 10) { //we have super pixel 64x64
	     		
	     		imagePath = GIF64Path + whiteTestFileName64 + ".gif";
	     		selectedFileName = whiteTestFileName64;
	     		decodedDirPath = GIF64Path + "decoded";
	     	}
	     	else {
	     		imagePath = GIFPath + whiteTestFileName + ".gif";
	     		selectedFileName = whiteTestFileName;
	     		decodedDirPath = GIFPath + "decoded";
	     	}
	     	
			gifView.setGif(imagePath);  //just sets the image , no decoding, decoding happens in the animateafterdecode method
			showToast(getString(R.string.RunningWhiteTest));
			animateAfterDecode(0); //0 means no write
	
	     	
	   	 }
	   	 else {
	   		 showToast(getString(R.string.pixelNotFound));
		 }
    }
	    
	    private void copyFile(InputStream in, OutputStream out) throws IOException {
	        byte[] buffer = new byte[1024];
	        int read;
	        while((read = in.read(buffer)) != -1){
	          out.write(buffer, 0, read);
	        }
	    }
	
	private static void loadRGB565() {
	 	   
		try {
   			int n = BitmapInputStream.read(BitmapBytes, 0, BitmapBytes.length); // reads
   																				// the
   																				// input
   																				// stream
   																				// into
   																				// a
   																				// byte
   																				// array
   			Arrays.fill(BitmapBytes, n, BitmapBytes.length, (byte) 0);
   		} catch (IOException e) {
   			e.printStackTrace();
   		}

   		int y = 0;
   		for (int i = 0; i < frame_.length; i++) {
   			frame_[i] = (short) (((short) BitmapBytes[y] & 0xFF) | (((short) BitmapBytes[y + 1] & 0xFF) << 8));
   			y = y + 2;
   		}
   }
	
    /////*************GridView Stuff
	 /**
     * Free up bitmap related resources.
     */
    protected void onDestroy() {
    //	this.unregisterReceiver(receiver);
        super.onDestroy();
       // mThread.close();
       
        final GridView grid = gridview;
        final int count = grid.getChildCount();
        ImageView v = null;
        for (int i = 0; i < count; i++) {
            v = (ImageView) grid.getChildAt(i);
            ((BitmapDrawable) v.getDrawable()).setCallback(null);
        }
        
        if (deviceFound == 0) {  
    	     connectTimer.cancel();  //if user closes the program, need to kill this timer or we'll get a crash
        }
    
        
       /* if (deviceFound == 1) {  //was causing crashes
        	connectTimer.cancel();  //if user closes the program, need to kill this timer or we'll get a crash
	        decodedtimer.cancel();
        }*/
        
     //   ioio_.disconnect();
      //  imagedisplaydurationTimer.cancel();
 		//pausebetweenimagesdurationTimer.cancel();
 		
 		//matrix_.frame(frame_); 
 		
       // mediascanTimer.cancel(); 
        
    }
   
    
    public class AsyncTaskLoadFiles extends AsyncTask<Void, String, Void> {
    	  
    	  File targetDirector;
    	  File UserPNGtargetDirector;
    	  File UserGIFtargetDirector;
    	  File GIF64targetDirector;
    	  ImageAdapter2 myTaskAdapter;

    	  public AsyncTaskLoadFiles(ImageAdapter2 adapter) {
    	   myTaskAdapter = adapter;
    	  }

    	  @Override
    	  protected void onPreExecute() {
    		  
    	  //ideally it would be nice to access the pre-packaged pixel art from assets directly and not have to copy to the sd card but wtih Android you can't access assets via file path, only inputstreeam. So this is why we're using the sd card, you can turn a file from an inputstream but this will take longer, save this for another day, we'll use the sd card for now
    		  
    		String ExternalStorageDirectoryPath = Environment
    	   .getExternalStorageDirectory().getAbsolutePath();
    	   String targetPath = GIFPath;
    	   targetDirector = new File(targetPath);
    	   
    	   String GIF64targetPath = GIF64Path;
    	   GIF64targetDirector = new File(GIF64targetPath);
    	   
    	   myTaskAdapter.clear(); //TO DO add this to the sharing piece?
    	   
    	   super.onPreExecute();
    	  }

    	  @Override
    	  protected Void doInBackground(Void... params) {
		  
		  if (matrix_model == 10 && GIF64targetDirector.exists()) { //gif 64x64 content, only show if 64x64 led matrix is picked
	    	   File[] files = GIF64targetDirector.listFiles(new FilenameFilter() {
	   		    public boolean accept(File dir, String name) {
	   		        return name.toLowerCase().endsWith(".gif") || name.toLowerCase().endsWith(".png");
	   		    }
	   			});
	   	   
		   	   for (File file : files) {
		   	    publishProgress(file.getAbsolutePath());
		   	    if (isCancelled()) break;
		   	   }
		   }
    		  
		  if (only64_ == false && targetDirector.exists()) {  //gif or png, this is the gif directory 32x32 content
    		  File[] files = targetDirector.listFiles(new FilenameFilter() {
    		    public boolean accept(File dir, String name) {
    		        return name.toLowerCase().endsWith(".gif") || name.toLowerCase().endsWith(".png");
    		    }
    		});
    	   
    	   for (File file : files) {
    	    publishProgress(file.getAbsolutePath());
    	    if (isCancelled()) break;
    	   }
	    }
	  
	   	   
   return null;
   
  }	  

    	  @Override
    	  protected void onProgressUpdate(String... values) {
    	   myTaskAdapter.add(values[0]);
    	   super.onProgressUpdate(values);
    	  }

    	  @Override
    	  protected void onPostExecute(Void result) {
    	   myTaskAdapter.notifyDataSetChanged();
    	   super.onPostExecute(result);
    	  }

    	 }

	@Override
 public  boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {  
		if (deviceFound == 1) { 
	  		//********we need to reset everything because the user could have been already running an animation
	  	     x = 0;
	  	     
	  	     
	  	     if (StreamModePlaying == 1) {
	  	    	 //decodedtimer.cancel();
	  	    	// if(!pixelHardwareID.equals("PIXL")) {
	  	    		decodedtimer.cancel();
	  	    	// }
	  	    	// is.close();
	  	     }
	  	     ///****************************
    		  
	    	imagePath = (String) parent.getItemAtPosition(position);
	    	originalImagePath = (String) parent.getItemAtPosition(position);
	        selectedFileName = imagePath;
	        //here we need to get the file name to see if the file has already been decoded
	        //file name will be in a format like this sdcard/pixel/pixerinteractive/rain.gif , we want to extra just rain
	        String delims = "[/]";
	        String[] aFileName = selectedFileName.split(delims);
	        int aFileNameLength = aFileName.length;
	        selectedFileName = aFileName[aFileNameLength-1];
	        String fileType = aFileName[aFileNameLength-2];  //can be gif, png, userpng, usergif, png64, or gif64
	        String delims2 = "[.]";
	        String[] aFileName2 = selectedFileName.split(delims2);
	        int aFileNameLength2 = aFileName2.length;
	        selectedFileName = aFileName2[0];	//now we have just the short name with no extension
	        
	        //**** now let's handle the thumbnails
	        String filenameArray[] = imagePath.split("\\.");
	        String extension = filenameArray[filenameArray.length-1]; //.png
	        
	        //we need to find out which directory was selected so we can set the decodeddir
	        
	        if (fileType.equals("gif")) {
	        	decodedDirPath = GIFPath + "decoded";
	        	gifPath_ = GIFPath;
	        }
	       /* else if (fileType.equals("usergif")) {
	        	decodedDirPath = userGIFPath + "decoded";
	        	gifPath_ = userGIFPath;
	        }*/
	        else if (fileType.equals("gif64")) {
	        	decodedDirPath = GIF64Path + "decoded";
	        	gifPath_ = GIF64Path;
	        }
	        //if the file type was not one of these like a png for example, then we don't care about the decodeddirpath and we don't change it
	        
	        if (extension.equals("png")) {  //then we use the thumbnail, we just need to rename the image path to a gif
	        	String wholestring_no_extension = filenameArray[filenameArray.length-2]; // /storage/emulated/0/pixel/pixelanimate/tree
	        	String filenameArray2[] = wholestring_no_extension.split("\\/");
	        	String filename_no_extension = filenameArray2[filenameArray2.length-1]; //tree
	        	String newimagePath = wholestring_no_extension.replace(filename_no_extension, filename_no_extension + ".gif");
	        	
	        	imagePath = newimagePath;
	        	
	        	//now we need to check that filename/decoded/filename.rgb565 exists
	        	
	        	//File pngRGB565path = new File(basepath + "/pixel/animatedgifs/decoded/" + filename_no_extension + ".rgb565"); //sdcard/pixel/animatedgifs/decoded/tree.rgb565
	        	//File pngRGB565path = new File(GIFPath + "decoded/" + filename_no_extension + ".rgb565"); //sdcard/pixel/gifs/decoded/tree.rgb565
	        	File pngRGB565path = new File(gifPath_ + "decoded/" + filename_no_extension + ".rgb565"); //sdcard/pixel/gifs/decoded/tree.rgb565
	        	if (!pngRGB565path.exists()) { //if it doesn't exist
	        		
	        		//ok not there so let's see if the original gif is there as decoded may have been deleted because the led panel changed
	        		File originalGIF = new File(gifPath_ + "gifsource/" + filename_no_extension + ".gif"); //sdcard/pixel/gifs/gifsource/tree.rgb565
	        		if (originalGIF.exists()) { 
	        			//we've got the original gif so now let's decode it
	        			 imagePath = gifPath_ + "gifsource/" + filename_no_extension + ".gif";
	        			 gifView.setGif(imagePath);  //just sets the image , no decoding, decoding happens in the animateafterdecode method
				     	 animateAfterDecode(0);
	        		}
	        		else { //well we tried, no original gif so we'll treat it as a png
			            	//there's no rgb565 and we only have a single frame png so let's just send this single frame png to pixel
				        	
			        		//it's  not there so let's check the original gifs folder, if it's in there, then treat it like a gif and decode
			        		
			        		imagePath = originalImagePath;
			        		
			        		if (kioskMode_ == false) {
				        		try {
				        			matrix_.interactive();
									matrix_.writeFile(100); //since it's only one frame , doesn't matter what fps is
			    		        	WriteImagetoMatrix();
			    		        	matrix_.playFile();
								} catch (ConnectionLostException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			        		}
			        		else {
			        			try {
				        			showToast("Writing to PIXEL not supported in Kiosk mode");
			        				matrix_.interactive();
			    		        	WriteImagetoMatrix();
								} catch (ConnectionLostException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			        		}
			            }
	        		}
	        	else {  //the rgb565 is there
	        		//gifView.setGif(imagePath);  //this is causing a crash, TO DO figure out why later 
	        		animateAfterDecode(1); //the rgb565 is there so let's run the already decoded animation
			    } 
	        }
	        
	        else if (extension.equals("jpg") || extension.equals("jpeg")) {  
	        	imagePath = originalImagePath;
	        	try {
        			matrix_.interactive();
					matrix_.writeFile(fps);
        			WriteImagetoMatrix();
        			matrix_.playFile();
				} catch (ConnectionLostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        
	       else if (extension.equals("gif")) {  // if it's not a png, then it's a gif so let's animate
	     	   gifView.setGif(imagePath);  //just sets the image , no decoding, decoding happens in the animateafterdecode method
	     	   animateAfterDecode(1);
	       } 
	       return true;
	       // animateAfterDecode(0);  //0 means streaming mode, 1 means download mode 
    }
    else {
    	showToast(getString(R.string.pixelNotFound));
    	return true;
    }
}
    
  public void onItemClick(AdapterView<?> parent, View v, int position, long id) {    //we go here when the user tapped an image from the initial grid    
        
	     
	        if (deviceFound == 1) { 
			  		//********we need to reset everything because the user could have been already running an animation
			  	     x = 0;
			  	     
			  	     
			  	     if (StreamModePlaying == 1) {
			  	    	 //decodedtimer.cancel();
			  	    	// if(!pixelHardwareID.equals("PIXL")) {
			  	    		decodedtimer.cancel();
			  	    	// }
			  	    	// is.close();
			  	     }
			  	     ///****************************
		    		  
			    	imagePath = (String) parent.getItemAtPosition(position);
			    	originalImagePath = (String) parent.getItemAtPosition(position);
			        selectedFileName = imagePath;
			        //here we need to get the file name to see if the file has already been decoded
			        //file name will be in a format like this sdcard/pixel/pixerinteractive/rain.gif , we want to extra just rain
			        String delims = "[/]";
			        String[] aFileName = selectedFileName.split(delims);
			        int aFileNameLength = aFileName.length;
			        selectedFileName = aFileName[aFileNameLength-1];
			        String fileType = aFileName[aFileNameLength-2];  //can be gif, png, userpng, usergif, png64, or gif64
			        String delims2 = "[.]";
			        String[] aFileName2 = selectedFileName.split(delims2);
			        int aFileNameLength2 = aFileName2.length;
			        selectedFileName = aFileName2[0];	//now we have just the short name with no extension
			        
			        //**** now let's handle the thumbnails
			        String filenameArray[] = imagePath.split("\\.");
			        String extension = filenameArray[filenameArray.length-1]; //.png
			        
			        //we need to find out which directory was selected so we can set the decodeddir
			        
			        if (fileType.equals("gif")) {
			        	decodedDirPath = GIFPath + "decoded";
			        	gifPath_ = GIFPath;
			        }
			     /*   else if (fileType.equals("usergif")) {
			        	decodedDirPath = userGIFPath + "decoded";
			        	gifPath_ = userGIFPath;
			        }*/
			        else if (fileType.equals("gif64")) {
			        	decodedDirPath = GIF64Path + "decoded";
			        	gifPath_ = GIF64Path;
			        }
			        //if the file type was not one of these like a png for example, then we don't care about the decodeddirpath and we don't change it
			        
			        if (extension.equals("png")) {  //then we use the thumbnail, we just need to rename the image path to a gif
			        	String wholestring_no_extension = filenameArray[filenameArray.length-2]; // /storage/emulated/0/pixel/pixelanimate/tree
			        	String filenameArray2[] = wholestring_no_extension.split("\\/");
			        	String filename_no_extension = filenameArray2[filenameArray2.length-1]; //tree
			        	String newimagePath = wholestring_no_extension.replace(filename_no_extension, filename_no_extension + ".gif");
			        	
			        	imagePath = newimagePath;
			        	
			        	//now we need to check that filename/decoded/filename.rgb565 exists
			        	
			        	//File pngRGB565path = new File(basepath + "/pixel/animatedgifs/decoded/" + filename_no_extension + ".rgb565"); //sdcard/pixel/animatedgifs/decoded/tree.rgb565
			        	//File pngRGB565path = new File(GIFPath + "decoded/" + filename_no_extension + ".rgb565"); //sdcard/pixel/gifs/decoded/tree.rgb565
			        	File pngRGB565path = new File(gifPath_ + "decoded/" + filename_no_extension + ".rgb565"); //sdcard/pixel/gifs/decoded/tree.rgb565
			        	if (!pngRGB565path.exists()) { //if it doesn't exist
			        		
			        		//ok not there so let's see if the original gif is there as decoded may have been deleted because the led panel changed
			        		File originalGIF = new File(gifPath_ + "gifsource/" + filename_no_extension + ".gif"); //sdcard/pixel/gifs/gifsource/tree.rgb565
			        		if (originalGIF.exists()) { 
			        			//we've got the original gif so now let's decode it
			        			 imagePath = gifPath_ + "gifsource/" + filename_no_extension + ".gif";
			        			 gifView.setGif(imagePath);  //just sets the image , no decoding, decoding happens in the animateafterdecode method
						     	 animateAfterDecode(0);
			        		}
			        		else { //well we tried, no original gif so we'll treat it as a png
					            	//there's no rgb565 and we only have a single frame png so let's just send this single frame png to pixel
						        	
					        		//it's  not there so let's check the original gifs folder, if it's in there, then treat it like a gif and decode
					        		
					        		imagePath = originalImagePath;
					        		try {
					        			matrix_.interactive();  //this has to be here in case we were in interactive mode from a previous long tap
					        			WriteImagetoMatrix();
									} catch (ConnectionLostException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
					            }
			        		}
			        	else {  //the rgb565 is there
			        		//gifView.setGif(imagePath);  //this is causing a crash, TO DO figure out why later 
			        		animateAfterDecode(0); //the rgb565 is there so let's run the already decoded animation
					    } 
			        }
			        
			        else if (extension.equals("jpg") || extension.equals("jpeg")) {  
			        	imagePath = originalImagePath;
		        		try {
		        			matrix_.interactive(); //this has to be here in case we were in interactive mode from a previous long tap
		        			WriteImagetoMatrix();
						} catch (ConnectionLostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
			        
			       else if (extension.equals("gif")) {  // if it's not a png, then it's a gif so let's animate
			     	   gifView.setGif(imagePath);  //just sets the image , no decoding, decoding happens in the animateafterdecode method
			     	   animateAfterDecode(0);
			       } 
			   
			       // animateAfterDecode(0);  //0 means streaming mode, 1 means download mode 
	        }
	        else {
	        	showToast(getString(R.string.pixelNotFound));
	        }
  		}
  
  private void WriteImagetoMatrix() throws ConnectionLostException {  //here we'll take a PNG, BMP, or whatever and convert it to RGB565 via a canvas, also we'll re-size the image if necessary
  	
	    // ***** old code, had to switch to newer code below as was getting out of memory errors for when opening larger JPEGs (from Android gallery for example)
	    // originalImage = BitmapFactory.decodeFile(imagePath);   
		// width_original = originalImage.getWidth();
		// height_original = originalImage.getHeight();
	    //***********************************************************************************
	  
		originalImage = decodeSampledBitmapFromFile(imagePath, KIND.width,KIND.height);
		 
		width_original = originalImage.getWidth();
	    height_original = originalImage.getHeight();
		 
		 if (width_original != KIND.width || height_original != KIND.height) {
			 resizedFlag = 1;
			 //the iamge is not the right dimensions, so we need to re-size
			 scaleWidth = ((float) KIND.width) / width_original;
 		 	 scaleHeight = ((float) KIND.height) / height_original;
 		 	 
	   		 // create matrix for the manipulation
	   		 matrix2 = new Matrix();
	   		 // resize the bit map
	   		 matrix2.postScale(scaleWidth, scaleHeight);
	   		 resizedBitmap = Bitmap.createBitmap(originalImage, 0, 0, width_original, height_original, matrix2, false); //false means don't anti-alias which is what we want when re-sizing for super pixel 64x64
	   		 canvasBitmap = Bitmap.createBitmap(KIND.width, KIND.height, Config.RGB_565); 
	   		 Canvas canvas = new Canvas(canvasBitmap);
	   		 canvas.drawRGB(0,0,0); //a black background
	   	   	 canvas.drawBitmap(resizedBitmap, 0, 0, null);
	   		 ByteBuffer buffer = ByteBuffer.allocate(KIND.width * KIND.height *2); //Create a new buffer
	   		 canvasBitmap.copyPixelsToBuffer(buffer); //copy the bitmap 565 to the buffer		
	   		 BitmapBytes = buffer.array(); //copy the buffer into the type array
		 }
		 else {
			// then the image is already the right dimensions, no need to waste resources resizing
			 resizedFlag = 0;
			 canvasBitmap = Bitmap.createBitmap(KIND.width, KIND.height, Config.RGB_565); 
	   		 Canvas canvas = new Canvas(canvasBitmap);
	   	   	 canvas.drawBitmap(originalImage, 0, 0, null);
	   		 ByteBuffer buffer = ByteBuffer.allocate(KIND.width * KIND.height *2); //Create a new buffer
	   		 canvasBitmap.copyPixelsToBuffer(buffer); //copy the bitmap 565 to the buffer		
	   		 BitmapBytes = buffer.array(); //copy the buffer into the type array
		 }	   		
		 
		loadImage();  
		matrix_.frame(frame_);  //write to the matrix   
}
  
  private void WriteCameratoMatrix(Bitmap cameraBitmap) throws ConnectionLostException {  //here we'll take a PNG, BMP, or whatever and convert it to RGB565 via a canvas, also we'll re-size the image if necessary
	  	
		//originalImage = decodeSampledBitmapFromFile(imagePath, KIND.width,KIND.height);
		 
		width_original = cameraBitmap.getWidth();
	    height_original = cameraBitmap.getHeight();
		 
		 if (width_original != KIND.width || height_original != KIND.height) {
			 resizedFlag = 1;
			 //the iamge is not the right dimensions, so we need to re-size
			 scaleWidth = ((float) KIND.width) / width_original;
		 	 scaleHeight = ((float) KIND.height) / height_original;
		 	 
	   		 // create matrix for the manipulation
	   		 matrix2 = new Matrix();
	   		 // resize the bit map
	   		 matrix2.postScale(scaleWidth, scaleHeight);
	   		 resizedBitmap = Bitmap.createBitmap(cameraBitmap, 0, 0, width_original, height_original, matrix2, false); //false means don't anti-alias which is what we want when re-sizing for super pixel 64x64
	   		 canvasBitmap = Bitmap.createBitmap(KIND.width, KIND.height, Config.RGB_565); 
	   		 Canvas canvas = new Canvas(canvasBitmap);
	   		 canvas.drawRGB(0,0,0); //a black background
	   	   	 canvas.drawBitmap(resizedBitmap, 0, 0, null);
	   		 ByteBuffer buffer = ByteBuffer.allocate(KIND.width * KIND.height *2); //Create a new buffer
	   		 canvasBitmap.copyPixelsToBuffer(buffer); //copy the bitmap 565 to the buffer		
	   		 BitmapBytes = buffer.array(); //copy the buffer into the type array
		 }
		 else {
			// then the image is already the right dimensions, no need to waste resources resizing
			 resizedFlag = 0;
			 canvasBitmap = Bitmap.createBitmap(KIND.width, KIND.height, Config.RGB_565); 
	   		 Canvas canvas = new Canvas(canvasBitmap);
	   	   	 canvas.drawBitmap(cameraBitmap, 0, 0, null);
	   		 ByteBuffer buffer = ByteBuffer.allocate(KIND.width * KIND.height *2); //Create a new buffer
	   		 canvasBitmap.copyPixelsToBuffer(buffer); //copy the bitmap 565 to the buffer		
	   		 BitmapBytes = buffer.array(); //copy the buffer into the type array
		 }	   		
		 
		loadImage();  
		matrix_.frame(frame_);  //write to the matrix   
}
  
  public void loadImage() {
	  	

		int y = 0;
		for (int i = 0; i < frame_.length; i++) {
			frame_[i] = (short) (((short) BitmapBytes[y] & 0xFF) | (((short) BitmapBytes[y + 1] & 0xFF) << 8));
			y = y + 2;
		}
		
		//we're done with the images so let's recycle them to save memory
	    canvasBitmap.recycle();
	    //originalImage.recycle(); //was crashing on the camera2matrix 
	    
	    if ( resizedFlag == 1) {
	    	resizedBitmap.recycle(); //only there if we had to resize an image
	    }
	}
  
  public static Bitmap decodeSampledBitmapFromFile(String filePath, 
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(filePath, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(filePath, options);
	}
  
  public static int calculateInSampleSize(
          BitmapFactory.Options options, int reqWidth, int reqHeight) {
  // Raw height and width of image
  final int height = options.outHeight;
  final int width = options.outWidth;
  int inSampleSize = 1;

  if (height > reqHeight || width > reqWidth) {

      final int halfHeight = height / 2;
      final int halfWidth = width / 2;

      // Calculate the largest inSampleSize value that is a power of 2 and keeps both
      // height and width larger than the requested height and width.
      while ((halfHeight / inSampleSize) > reqHeight
              && (halfWidth / inSampleSize) > reqWidth) {
          inSampleSize *= 2;
      }
  }

  return inSampleSize;
}
  
  public static void animateAfterDecode(int longpress) {
	  
	  //first check if rgb565 file is there, proceed if so
	  // if not then decode
	  // also check if the led matrix selected now doesn't match the led matrix from the decoded file, re-decode if so
	  
	  
	  //********we need to reset everything because the user could have been already running an animation
	     x = 0;
	     downloadCounter = 0;
	     
	     if (StreamModePlaying == 1) {
	    	 decodedtimer.cancel();
	     }
	     ///****************************
     
     //now let's check if this file was already decoded by looking for the text meta data file
 	File decodedFile = new File(decodedDirPath + "/" + selectedFileName  + ".txt"); //decoded/rain.txt
		if(decodedFile.exists()) {
	   		    // ok good, now let's read it, we need to get the total numbers of frames and the frame speed
	   		  //File sdcard = Environment.getExternalStorageDirectory();
	   	      //Get the text file
	   	     // File file = new File(sdcard,"file.txt");
	   	      //Read text from file
	   	      StringBuilder text = new StringBuilder();
	   	      String fileAttribs = null;
	   	      
	   	      try {
	   	          BufferedReader br = new BufferedReader(new FileReader(decodedFile));
	   	          String line;
	   
	   	          while ((line = br.readLine()) != null) {
	   	              text.append(line);
	   	              text.append('\n');	   	             
	   	          }
	   	      }
	   	      catch (IOException e) {
	   	          //You'll need to add proper error handling here
	   			
	   	      }
	   	      
	   	    fileAttribs = text.toString();  //now convert to a string	   	      
	   	    String fdelim = "[,]"; //now parse this string considering the comma split  ie, 32,60
	        String[] fileAttribs2 = fileAttribs.split(fdelim);
	        selectedFileTotalFrames = Integer.parseInt(fileAttribs2[0].trim());
	    	selectedFileDelay = Integer.parseInt(fileAttribs2[1].trim());
	    	selectedFileResolution = Integer.parseInt(fileAttribs2[2].trim());
	    	//showToast("selected file resolution:" + selectedFileResolution);
	    	//showToast("current file resolution:" + currentResolution);
	    	
	    	//we need this for the decoder timer
	    	//now we need to compare the current resoluiton with the encoded resolution
	    	//if different, then we need to re-encode
	    	
	    	if (selectedFileResolution == currentResolution) {  //selected resoluton comes from the text file of the selected file and current comes from the selected led matrix type from preferences
	    	
			    	if (selectedFileDelay != 0) {  //then we're doing the FPS override which the user selected from settings
			    		fps = 1000.f / selectedFileDelay;
					} else { 
			    		fps = 0;
			    	}
			    	MainActivity myActivity = new MainActivity();  //had to add this due to some java requirement	  
			    	
		    		try {
		    			if (longpress == 1 && kioskMode_ == false && pixelHardwareID.substring(0,4).equals("PIXL")) {  //download mode
		    				StreamModePlaying = 0;
		    			//	gridview.setKeepScreenOn(true); //we don't want the screen going into sleep mode
		    				matrix_.interactive();
			    			matrix_.writeFile(fps);
			    			//MainActivity.writePixelAsync loadApplication = myActivity.new writePixelAsync();
			    			writePixelAsync loadApplication = myActivity.new writePixelAsync();
			    			loadApplication.execute();
		    			}
		    			else {
		    				matrix_.interactive(); //put PIXEL back in interactive mode, can't forget to do that! or we'll just be playing local animations
		    				//decodedtimer = myActivity.new DecodedTimer(300000,selectedFileDelay);  //stream mode
		    				
		    				decodedtimer = myActivity.new DecodedTimer(selectedFileTotalFrames*1000,selectedFileDelay);  //stream mode
		    				
		    				
		    	   		   /* file = new File(decodedDirPath + "/" + selectedFileName + ".rgb565");
		    	   			if (file.exists()) {
		    					
		    			   // RandomAccessFile raf = null;
		    					
		    					//let's setup the seeker object
		    					try {
		    						raf = new RandomAccessFile(file, "r"); //r means read only
		    						showToast("went here");
		    					} catch (FileNotFoundException e2) {
		    						// TODO Auto-generated catch block
		    						e2.printStackTrace();
		    					}  
		    	   			}*/
		    				
							decodedtimer.start();
							StreamModePlaying = 1; //our isStreamModePlaying flag	
		    			}
		    			
		    		} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	//StreamModePlaying = 1; //our isStreamModePlaying flag	        	
		   		}
	    	else {
	    		Toast toast6 = Toast.makeText(context, "LED panel model was changed, decoding again...", Toast.LENGTH_LONG);
		        toast6.show();
		        
		        //because the LED panel was changed, we need to delete the decoding dir and create it all again
		       // File decodeddir = new File(basepath + "/pixel/animatedgifs/decoded");
		       // File decodeddir = new File(GIFPath + "decoded");
		        File decodeddir = new File(decodedDirPath); //could be gif, gif64, or usergif, this was set above
		        
		        //before we delete the decoded dir, we have to renmae it, this is due to some strange Android bug http://stackoverflow.com/questions/11539657/open-failed-ebusy-device-or-resource-busy
		        final File to = new File(decodeddir.getAbsolutePath() + System.currentTimeMillis());
		        decodeddir.renameTo(to);
		        recursiveDelete(to);
		        //ok decoded dir is deleted, let's add it back now
			  	decodeddir.mkdirs();
		       
		        ///************** let's show a message on PIXEL letting the user know we're decoding
		        showDecoding();
		        ///*********************************************************************************
	    		gifView.play(); //this does the actual decoding, it was a class already written
	    		
	    	}
		}	
		else { //then we need to decode the gif first	
			Toast toast7 = Toast.makeText(context, "One time decode in process, just a moment...", Toast.LENGTH_SHORT);
	        toast7.show();
	        showDecoding();
			gifView.play();
		}
  } 
  
  private static void recursiveDelete(File fileOrDirectory) {
      if (fileOrDirectory.isDirectory())
          for (File child : fileOrDirectory.listFiles())
              recursiveDelete(child);

      fileOrDirectory.delete();
  }
  
  public class writePixelAsync extends AsyncTask<Void, Integer, Void>{
		
		 int progress_status;
	      
		  @Override
		  protected void onPreExecute() {
	      super.onPreExecute();
	    
	     //progress = new ProgressDialog(MainActivity.this); //this gives a null pointer
	     progress = new ProgressDialog( MainActivity.getInstance()); 
	    
		        progress.setMax(selectedFileTotalFrames);
		        //progress.setTitle("Writing to PIXEL, please do not interrupt or leave this screen");
		        progress.setTitle(context.getResources().getString(R.string.SDCardWritePrompt));
		      
		       // progress.getWindow().setGravity(Gravity.TOP); //this was putting it too high
		        
		        progress.getWindow().setGravity(Gravity.TOP);
		        android.view.WindowManager.LayoutParams params = progress.getWindow().getAttributes();
		        params.y = 100;
		        
		        /*progress.getWindow().getAttributes().verticalMargin = 0.8F;
			    progress.getWindow().setGravity(Gravity.CENTER);
			    WindowManager.LayoutParams wmlp = progress.getWindow().getAttributes();
			    wmlp.y = height / 8;
			    progress.getWindow().setAttributes(wmlp);*/
			       
			       
		        //progress.setMessage("Sending Animation to PIXEL....");
		        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		        progress.setCancelable(false); //must have this as we don't want users cancel while it's writing
		        progress.show();
	  }
	      
	  @Override
	  protected Void doInBackground(Void... params) {
			
	  int count = 0;
	  for (count=0;count< (selectedFileTotalFrames)*2;count++) {  //had to add this work around to loop through twice and write on the second time. For some weird reason, the first frame is getting skipped if only gong through the loop one time
				 
				/* if (downloadCounter < count) {  //won't need this but just in case, some weird problem caused by a bug with an extra timer going, the gc was interrupting
					Log.i("PixelAnimations ","STARTING OVER!"+ downloadCounter + " " + String.valueOf(selectedFileTotalFrames-1));
					matrix_.interactive();
					matrix_.writeFile(fps); 
					count = 0; //start it over
					downloadCounter = 0;
					
					//break; //get out of the loop because we had a garbage evet
				 }*/
				  
				  File file = new File(decodedDirPath + "/" + selectedFileName + ".rgb565"); //this is one big file now, no longer separate files
				  
					RandomAccessFile raf = null;
				  
					//let's setup the seeker object
					try {
						raf = new RandomAccessFile(file, "r");
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}  // "r" means open the file for reading
					
					try {
						raf.seek(0);
					} catch (IOException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					} //move pointer back to the beginning of the file
					
					if (x == selectedFileTotalFrames) { // Manju - Reached End of the file.
		   				x = 0;
		   				readytoWrite = 1;
		   				try {
							raf.seek(0); //move pointer back to the beginning of the file
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
		   			}
					
					 switch (selectedFileResolution) { //16x32 matrix = 1024k frame size, 32x32 matrix = 2048k frame size
			            case 16: frame_length = 1048;
			                     break;
			            case 32: frame_length = 2048;
			                     break;
			            case 64: frame_length = 4096;
			                     break;
			            case 128: frame_length = 8192;
	                     		  break;         
			            default: frame_length = 2048;
			                     break;
			          }
					 
					 Log.d("PixelAnimations","frame length: " + frame_length);
					
					//now let's see forward to a part of the file
						try {
							raf.seek(x*frame_length);
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						} 
						Log.d("PixelAnimations","from sd card write, x is: " + x);
						
						
						if (frame_length > Integer.MAX_VALUE) {
			   			    try {
								throw new IOException("The file is too big");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			   			}
						
						// Create the byte array to hold the data
			   			//byte[] bytes = new byte[(int)length];
			   			BitmapBytes = new byte[(int)frame_length];
			   			
			   			try {
							raf.read(BitmapBytes);
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
			   			
			   			// Read in the bytes
			   		//	int offset = 0;
			   		//	int numRead = 0;
			   			
			   			
			   			
			   		/*	try {
							while (offset < BitmapBytes.length && (numRead=raf.read(BitmapBytes, offset, BitmapBytes.length-offset)) >= 0) {
							    offset += numRead;
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			   			 
			   			// Ensure all the bytes have been read in
			   			if (offset < BitmapBytes.length) {
			   			    try {
								throw new IOException("The file was not completely read: "+file.getName());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			   			}*/
			   			x++;
			   			 
			   			// Close the input stream, all file contents are in the bytes variable
			   			try {
			   				raf.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	
							
							//now that we have the byte array loaded, load it into the frame short array
							
						int y = 0;
						for (int i = 0; i < frame_.length; i++) {
							frame_[i] = (short) (((short) BitmapBytes[y] & 0xFF) | (((short) BitmapBytes[y + 1] & 0xFF) << 8));
							y = y + 2;
						}
					
						//need to add something in here if the transfer got interruped, then go back to interactive mode and start over
						//downloadCounter++;
					  if (readytoWrite == 1)  {
 					   		try {
						   	 Log.v("PixelAnimations ","Starting-->"+ count + " " + String.valueOf(selectedFileTotalFrames-1));
						   		matrix_.frame(frame_);
						   		progress_status++;
							    publishProgress(progress_status);
						   	
							} catch (ConnectionLostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					  }		
					   	
			  }  // end for
			
		    
	   return null;
	  }
	  
	  @Override
	  protected void onProgressUpdate(Integer... values) {
	   super.onProgressUpdate(values);
	   
	   progress.incrementProgressBy(1);
	  
	  // firstTimeSetupCounter_.setText(values[0]+"%");
	    
	  }
	   
	  @Override
	  protected void onPostExecute(Void result) {
		  progress.dismiss();
	
	 
	   try {
		matrix_.playFile();
	} catch (ConnectionLostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 // gridview.setKeepScreenOn(false); //we're done so we can allow the screen to turn off again
	  super.onPostExecute(result);
	  startButton_.setVisibility(View.VISIBLE); //we are done so let's enable the buttons
	  endTestSound.start();
	 // pixelRadio_.setEnabled(true);
     // superPixelRadio_.setEnabled(true);
	  //showToast(getString(R.string.factoryTestComplete));
	  Toast toast16 = Toast.makeText(context, context.getResources().getString(R.string.factoryTestComplete), Toast.LENGTH_LONG);
	  toast16.show();
}
	
}
   
	private static void showDecoding()  {
		
	/*	switch (currentResolution) {  //get this from the preferences
	     case 16:
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding16);
	    	 break;
	     case 32:
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding32);
	    	 break;
	     case 64:
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding64);
	    	 break;
	     case 128:
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding128);
	    	 break;
	     
	     default:	    		 
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding32);
	     }*/
		
		 switch (matrix_number) {  //get this from the preferences
	     case 0:
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding16);
	    	 break;
	     case 1:
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding16);
	    	 break;
	     case 2:
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding32);
	    	 break;
	     case 3:
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding32);
	    	 break;
	     case 4:
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding64by32);
	    	 break;
	     case 5:
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding32by64);
	    	 break;	 
	     case 6:
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding32by64);
	    	 break;
	     case 7:
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding32by128);
	    	 break;	 
	     case 8:
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding128by32); 
	    	 break;	 	 	 
	     case 9:
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding32by128); 
	    	 break;	 	
	     case 10:
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding64by64); 
	    	 break;	 	
	     default:	    		 
	    	 BitmapInputStream = context.getResources().openRawResource(R.raw.decoding32);
	     }
		
	        loadRGB565(); //show the one time decoding message to the user
	        try {
				matrix_.frame(frame_);
			} catch (ConnectionLostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	
    private void showPleaseWait(final String str) {
		runOnUiThread(new Runnable() {
			public void run() {
				//pDialog = ProgressDialog.show(breath.this,analyzingText, justAmomentText, true);
				pDialog = ProgressDialog.show(MainActivity.this,getString(R.string.loadingImagesPlsWaitTitle), str, true);
				//pDialog.setCancelable(true);  //we don't need this one
			}
		});
	}
	
	////*****************************
	
    public void letsAnimate() {
    	
        // hack to call the non-static method
	    animateAfterDecode(0);
    }
	
	
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu) 
	    {
	       MenuInflater inflater = getMenuInflater();
	       inflater.inflate(R.menu.mainmenu, menu);
	       return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected (MenuItem item)
	    {
	       
			
	      if (item.getItemId() == R.id.menu_instructions) {
	 	    	AlertDialog.Builder alert=new AlertDialog.Builder(this);
	 	      	alert.setTitle(getResources().getString(R.string.setupInstructionsStringTitle)).setIcon(R.drawable.icon).setMessage(getResources().getString(R.string.setupInstructionsString)).setNeutralButton(getResources().getString(R.string.OKText), null).show();
	 	   }
	    	
		  if (item.getItemId() == R.id.menu_about) {
			  
			    AlertDialog.Builder alert=new AlertDialog.Builder(this);
		      	alert.setTitle(getString(R.string.menu_about_title)).setIcon(R.drawable.icon).setMessage(getString(R.string.menu_about_summary) + "\n\n" + getString(R.string.versionString) + " " + app_ver + "\n"
		      			+ getString(R.string.FirmwareVersionString) + " " + pixelFirmware + "\n"
		      			+ getString(R.string.HardwareVersionString) + " " + pixelHardwareID + "\n"
		      			+ getString(R.string.BootloaderVersionString) + " " + pixelBootloader + "\n"
		      			+ getString(R.string.LibraryVersionString) + " " + IOIOLibVersion).setNeutralButton(getResources().getString(R.string.OKText), null).show();	
		   }
	    	
	    	if (item.getItemId() == R.id.menu_prefs)
	       {
	    		
	    		appAlreadyStarted = 0;    		
	    		Intent intent = new Intent()
	       				.setClass(this,
	       						com.ledpixelart.factorytest.preferences.class);   
	    				this.startActivityForResult(intent, WENT_TO_PREFERENCES);
	       }
	    	
	    	if (item.getItemId() == R.id.menu_btPair)
		       {
	    			
	    		if (pixelHardwareID.substring(0,4).equals("MINT")) { //then it's a PIXEL V1 unit
	    			showToast("Bluetooth Pair to PIXEL using code: 4545");
	    		}
	    		else { //we have a PIXEL V2 unit
	    			//showToast("Bluetooth Pair to PIXEL using code: 0000");
	    			showToast(getString(R.string.bluetoothCode));
	    		}
	    		
	    		Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
		        startActivityForResult(intent, REQUEST_PAIR_DEVICE);
		        
		       }
	    	

	    /*	if (item.getItemId() == R.id.menu_pixelJoint)
		       {
	    			String downloadURL = "www.pixeljoint.com/pixels/new_icons.asp?search=&dimo=%3D&dim=32&colorso=%3E%3D&colors=2&tran=&anim=&iso=&av=&owner=&d=&dosearch=1&ob=search&action=search";
	    			
	    			if (matrix_model == 10) { //if 64x64
	    				downloadURL = downloadURL_64;
	    			}
	    			else {
	    				downloadURL = downloadURL_32;
	    			}
	    			
			    	Intent i = new Intent(Intent.ACTION_VIEW);
			    	i.setData(Uri.parse("http://" + downloadURL));
			    	startActivity(i);
		       }*/
	    	
	    	/*if (item.getItemId() == R.id.menu_camera)
		       {
	    		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	    	     startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    	    }
		       }*/
	    	
	       return true;
	    }
	    
	    


	@Override
	    public void onActivityResult(int reqCode, int resCode, Intent data) //we'll go into a reset after this
	    {
	    	super.onActivityResult(reqCode, resCode, data);    	
	    	
	    	
	    	//used to be resCode, changed to reqCode
	    	if (reqCode == WENT_TO_PREFERENCES)  {
	    		setPreferences(); //very important to have this here, after the menu comes back this is called, we'll want to apply the new prefs without having to re-start the app
	    		//showToast("returned from preferences");
	    	}	
	    	
	    	/*if (reqCode == REQUEST_IMAGE_CAPTURE && resCode == RESULT_OK) {  //we'll get the picture and write is to the userpng sd card directory and then load into the gridview like any other png or gif
	            Bundle extras = data.getExtras();
	            cameraBMP = (Bitmap) extras.get("data");
	            
	            //let's make sure our directory is there fist and create it if not
	           File outPath = new File(userPNGPath);
    		   if (!outPath.exists()) {  //create the dir if it does not exist
				  outPath.mkdirs();
			   }
	            
	            //let's save this bitmap to the sd card and then load it also
	        
	           OutputStream stream = null;
	           File newCamerafile = new File(userPNGPath + "camerapic.png");
	           String newCamerafileString = null;
	   		   
				if (newCamerafile.exists() && saveMultipleCameraPics_ == true) {  //if the file is already there AND we should save multiple camera images, then we need to create a unique name
	   				  String uuid = UUID.randomUUID().toString();
	   				  try {
						stream = new FileOutputStream(userPNGPath  + uuid + ".png");
						newCamerafileString = userPNGPath  + uuid + ".png";
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	   			  }
	   			  else {
	   				try {
						stream = new FileOutputStream(userPNGPath + "camerapic.png");
						newCamerafileString = userPNGPath + "camerapic.png";
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	   			  }
				
	             Write bitmap to file using JPEG or PNG and 100% quality hint for JPEG. 
	            cameraBMP.compress(CompressFormat.PNG, 100, stream);
	            try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 			 
 			  //we've copied in the new file so now we need to add it to the gridview
 			  myImageAdapter.add(newCamerafileString);
 			  //now let's re-load
 			  continueOnCreate();
	        }*/
	    } 
	    
	    private void setPreferences() //here is where we read the shared preferences into variables
	    {
	     SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);     
	    
	     //scanAllPics = prefs.getBoolean("pref_scanAll", false);
	     //slideShowMode = prefs.getBoolean("pref_slideshowMode", false);
	     //noSleep = prefs.getBoolean("pref_noSleep", false);
	     debug_ = prefs.getBoolean("pref_debugMode", false);
	     //kioskMode_ = prefs.getBoolean("pref_kioskMode", false);
	     //gifonly_ = prefs.getBoolean("pref_gifonly", false); //only load gifs, don't load static pngs if true
	    // only64_ = prefs.getBoolean("pref_only64", false); //only show 64x64 content
	   //  showStartupMsg_ = prefs.getBoolean("pref_showStartupMsg", true); //show the "long tap to write to pixel message
	     //saveMultipleCameraPics_ = prefs.getBoolean("pref_writeCamera", false);
	   
	    matrix_model = Integer.valueOf(prefs.getString(   //the selected RGB LED Matrix Type
	    	        resources.getString(R.string.selected_matrix),
	    	        resources.getString(R.string.matrix_default_value))); 
	     
	   /*  downloadURL_32 = prefs.getString(   //the selected RGB LED Matrix Type
	    	        resources.getString(R.string.downloadURL_32),
	    	        resources.getString(R.string.downloadURL_32Default)); 
	     
	     downloadURL_64 = prefs.getString(   //the selected RGB LED Matrix Type
	    	        resources.getString(R.string.downloadURL_64),
	    	        resources.getString(R.string.downloadURL_64Default)); */
	     
	    /* <EditTextPreference
		    android:title="@string/downloadURL_32"
		    android:key="@string/pref_downloadURL_32"
		    android:defaultValue="@string/downloadURL_32Default"
		    android:summary="@string/downloadURL_32Summary"/>
	    
	    <EditTextPreference
		    android:title="@string/downloadURL_64"
		    android:key="@string/pref_downloadURL_64"
		    android:defaultValue="@string/downloadURL_64Default"
		    android:summary="@string/downloadURL_64Summary"/>*/
	     
	   /*  if (matrix_model == 0 || matrix_model == 1) {
	    	 currentResolution = 16;
	     }
	     else
	     {
	    	 currentResolution = 32;
	     }*/
	     
	   /*  FPSOverride_ = Integer.valueOf(prefs.getString(   //the selected RGB LED Matrix Type
	    	        resources.getString(R.string.fps_override),
	    	        resources.getString(R.string.FPSOverrideDefault))); */
	    //this wasn't adding any value so removed it
	     
	     FPSOverride_ = 0;
	     
	     switch (FPSOverride_) {  //get this from the preferences
	     case 0:
	    	 fps = 0;
	    	 break;
	     case 1:
	    	 fps = 5;
	    	 break;
	     case 2:
	    	 fps = 10;
	    	 break;
	     case 3:
	    	 fps = 15;
	    	 break;
	     case 4:
	    	 fps = 24;
	    	 break;
	     case 5:
	    	 fps = 30;
	    	 break;
	     default:	    		 
	    	 fps = 0;
	     }
	   
	     //matrix_model = 3;
	     
	     switch (matrix_model) {  //get this from the preferences
	     case 0:
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_32x16;
	    	 BitmapInputStream = getResources().openRawResource(R.raw.selectimage16);
	    	 frame_length = 1048;
	    	 currentResolution = 16;
	    	 break;
	     case 1:
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.ADAFRUIT_32x16;
	    	 BitmapInputStream = getResources().openRawResource(R.raw.selectimage16);
	    	 frame_length = 1048;
	    	 currentResolution = 16;
	    	 break;
	     case 2:
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_32x32_NEW; //v1
	    	 BitmapInputStream = getResources().openRawResource(R.raw.selectimage32);
	    	 frame_length = 2048;
	    	 currentResolution = 32;
	    	 break;
	     case 3:
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_32x32; //pixelv2
	    	 BitmapInputStream = getResources().openRawResource(R.raw.selectimage32);
	    	 frame_length = 2048;
	    	 currentResolution = 32;
	    	 pixelRadio_.setChecked(true);
	    	// superPixelRadio_.setChecked(false);
	    	 break;
	     case 4:
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_64x32; 
	    	 BitmapInputStream = getResources().openRawResource(R.raw.select64by32);
	    	 frame_length = 8192;
	    	 currentResolution = 64; 
	    	 break;
	     case 5:
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_32x64; 
	    	 BitmapInputStream = getResources().openRawResource(R.raw.select32by64);
	    	 frame_length = 8192;
	    	 currentResolution = 64; 
	    	 break;	 
	     case 6:
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_2_MIRRORED; 
	    	 BitmapInputStream = getResources().openRawResource(R.raw.select32by64);
	    	 frame_length = 8192;
	    	 currentResolution = 64; 
	    	 break;	 	 
	     case 7:
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_4_MIRRORED;
	    	 BitmapInputStream = getResources().openRawResource(R.raw.select32by128);
	    	 frame_length = 8192;
	    	 currentResolution = 128; 
	     case 8:
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_128x32; //horizontal
	    	 BitmapInputStream = getResources().openRawResource(R.raw.select128by32);
	    	 frame_length = 8192;
	    	 currentResolution = 128;  
	    	 break;	 
	     case 9:
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_32x128; //vertical mount
	    	 BitmapInputStream = getResources().openRawResource(R.raw.select32by128);
	    	 frame_length = 8192;
	    	 currentResolution = 128; 
	    	 break;	 
	     case 10:
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_64x64;
	    	 BitmapInputStream = getResources().openRawResource(R.raw.select64by64);
	    	 frame_length = 8192;
	    	 currentResolution = 128; 
	    	 //pixelRadio_.setChecked(false);
	    	 superPixelRadio_.setChecked(true);
	    	 break;	 	 		 
	     default:	    		 
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_32x32; //v2 as the default
	    	 BitmapInputStream = getResources().openRawResource(R.raw.selectimage32);
	    	 frame_length = 2048;
	    	 currentResolution = 32;
	     }
	     
	     matrix_number = matrix_model;
	         
	     frame_ = new short [KIND.width * KIND.height];
		 BitmapBytes = new byte[KIND.width * KIND.height *2]; //512 * 2 = 1024 or 1024 * 2 = 2048
	
		 loadRGB565(); //load the select pic raw565 file
		 
	 }
	    
    public static MainActivity getInstance() {
        return instance;
    }
    
    private static void writeDemo() { //now let's write the demo animation to the sd card
    	
     	
    	//now let's first ask the user to validate the IO ports
    	 Toast toast16 = Toast.makeText(context, context.getResources().getString(R.string.ioTest_and_sdWrite), Toast.LENGTH_LONG);
		 toast16.show();
    	
		 sdIOTest.start();
    	
    	if (matrix_model == 10) { //we have super pixel
     		
     		imagePath = GIF64Path + writeDemoFileName64 + ".gif";
     		selectedFileName = writeDemoFileName64;
     		decodedDirPath = GIF64Path + "decoded";
     	}
     	else {
     		imagePath = GIFPath + writeDemoFileName + ".gif";
     		selectedFileName = writeDemoFileName;
     		decodedDirPath = GIFPath + "decoded";
     	}
     	
		gifView.setGif(imagePath);  //just sets the image , no decoding, decoding happens in the animateafterdecode method
     	animateAfterDecode(1);
    }
	    
    private static void animationTimerDone() {
    	
      if (whiteTestDone == 0) {
    	  
    	whiteTestDone = 1;
    	  
    	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
			        	
			        	writeDemo();
			        	whiteTestDone = 0; //must reset this flag
			        	
			            break;

			        case DialogInterface.BUTTON_NEGATIVE:
			            
			        	// Toast toast15 = Toast.makeText(context, "Please replace LED matrix and run test again", Toast.LENGTH_LONG);
			        	 Toast toast15 = Toast.makeText(context, context.getResources().getString(R.string.replaceMatrix), Toast.LENGTH_LONG);
						 toast15.show();
						 startButton_.setVisibility(View.VISIBLE); //the test has to be re-started because panel bad so let's re-enable buttons
						 //pixelRadio_.setEnabled(true);
			             //superPixelRadio_.setEnabled(true);
						 whiteTestDone = 0;
						 
			            break;
			        }
			    }
			};
			
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getInstance());
			//builder.setMessage("Are all the LEDs white?").setPositiveButton("Yes", dialogClickListener)
			 //   .setNegativeButton("No", dialogClickListener).show();
			builder.setCancelable(false);
			
			builder.setMessage(context.getResources().getString(R.string.allLEDsWhite)).setPositiveButton(context.getResources().getString(R.string.Yes), dialogClickListener)
		    .setNegativeButton(context.getResources().getString(R.string.No), dialogClickListener).show();
			
			areLEDsWhite.start();
    	}
      
      else {  //the all white test was done already
    	  
      }
    }
	
	
    
    class IOIOThread extends BaseIOIOLooper {
  		//private ioio.lib.api.RgbLedMatrix matrix_;
    	//public AnalogInput prox_;  //just for testing , REMOVE later
    	
    	/*	Grove	6, 35, 5V, GND	GPIO, 5V (optional alcohol sensor)
			Grove	4, 5, 5V, GND	GPIO, 5V
			Grove	31, 32, 3V, GND	Analog or GPIO, 3V (optional proximity sensor)
			Grove	33, 34, 3V, GND	Analog or GPIO, 3V
			Grove	1, 2, 5V, GND	I2C, 5V*/
    	
    	private DigitalInput grove1_6;
    	private AnalogInput grove1_35;
    	private DigitalInput grove2_4;
    	private DigitalInput grove2_5;
    	private DigitalInput grove3_31;
    	private DigitalInput grove3_32;
    	private DigitalInput grove4_33;
    	private DigitalInput grove4_34;
    	private DigitalInput grove5_1;
    	private DigitalInput grove5_2;
    	
    	//private AnalogInput input_;
		private AnalogInput ProxInput_;
		private AnalogInput AlcoholInput_;
		//private DigitalOutput led_;
		//private DigitalInput button_;
		//private DigitalInput i2cButton_;
		private boolean grove1_6_;
		//private boolean grove1_35_;
		private boolean grove2_4_;
		private boolean grove2_5_;
		private boolean grove3_31_;
		//private boolean grove3_32_;
		private boolean grove4_33_;
		private boolean grove4_34_;
		private boolean grove5_1_;
		private boolean grove5_2_;

  		@Override
  		protected void setup() throws ConnectionLostException { //we'll always come back here after an intent or loss of connection
  			
  			grove1_6 = ioio_.openDigitalInput(6, DigitalInput.Spec.Mode.PULL_UP);
  	    	//grove1_35 = ioio_.openDigitalInput(35, DigitalInput.Spec.Mode.PULL_UP);
  	    	
  	    	//grove1_35 = ioio_.openAnalogInput(35);	
  	    	
  	    	grove2_4 = ioio_.openDigitalInput(4, DigitalInput.Spec.Mode.PULL_UP);
  	    	grove2_5 = ioio_.openDigitalInput(5, DigitalInput.Spec.Mode.PULL_UP);
  	    	grove3_31 = ioio_.openDigitalInput(31, DigitalInput.Spec.Mode.PULL_UP);
  	    	//grove3_32 = ioio_.openDigitalInput(32, DigitalInput.Spec.Mode.PULL_UP);
  	    	grove4_33 = ioio_.openDigitalInput(33, DigitalInput.Spec.Mode.PULL_UP);
  	    	grove4_34 = ioio_.openDigitalInput(34, DigitalInput.Spec.Mode.PULL_UP);
  	    	grove5_1 = ioio_.openDigitalInput(1, DigitalInput.Spec.Mode.PULL_UP);
  	    	grove5_2 = ioio_.openDigitalInput(2,DigitalInput.Spec.Mode.PULL_UP);
  	    	
  			//button_ = ioio_.openDigitalInput(4);
			//input_ = ioio_.openAnalogInput(33);
			ProxInput_ = ioio_.openAnalogInput(32);
			AlcoholInput_ = ioio_.openAnalogInput(35);
  			
  			matrix_ = ioio_.openRgbLedMatrix(KIND);
  			deviceFound = 1; //if we went here, then we are connected over bluetooth or USB
  			connectTimer.cancel(); //we can stop this since it was found
  	
  			//**** let's get IOIO version info for the About Screen ****
  			pixelFirmware = ioio_.getImplVersion(v.APP_FIRMWARE_VER);
  			pixelBootloader = ioio_.getImplVersion(v.BOOTLOADER_VER);
  			pixelHardwareID = ioio_.getImplVersion(v.HARDWARE_VER); 
  			//pixelHardwareID = ioio_.getImplVersion(v.APP_FIRMWARE_VER).substring(0,4); //quick hack, fix later
  			IOIOLibVersion = ioio_.getImplVersion(v.IOIOLIB_VER);
  			//**********************************************************
  		
  			if (debug_ == true) {  			
  			   showToast(pixelHardwareID);
  			}
  			
  			if (debug_ == true) {  			
	  			showToast("Bluetooth Connected");
  			}
  			
  		
  			matrix_.frame(frame_); //this used to show select pic but we don't need now
  				
  			
  			appAlreadyStarted = 1;
  			
  			if (kioskMode_ == false && pixelHardwareID.substring(0,4).equals("PIXL")) { //if it's a PIXEL V2 unit
  	        	 //showToast(getString(R.string.StartupMessage) + " " + pixelFirmware.substring(4,8)); //PIXL006B or PIXL0008
  	        	 if (CurrentFirmwareVersion.equals(pixelFirmware)) {
  	        		showToast(pixelFirmware + " " + getString(R.string.correctFirmwareMsg)); //PIXL0008 is the correct firmware version
  	        	 }
  	        		
  	        	 else {
  	        		 alertSound.start();
  	        		 showToast(pixelFirmware + " " + getString(R.string.incorrectFirmwareMsg) + " " + CurrentFirmwareVersion); //PIXL0008 is NOT the latest firmware version, please upgrade to
  	        	 }
  	        	
  	        }
  		}

  		public void loop() throws ConnectionLostException, InterruptedException {
			//setNumber(input_.read());
			setProx(ProxInput_.read());
			setAlcohol(AlcoholInput_.read());
			
  			grove1_6_ = grove1_6.read(); 
			//grove1_35_ = grove1_35.read(); 
			grove2_4_ = grove2_4.read(); //IOIO pin 4
			grove2_5_ = grove2_5.read(); 
			grove3_31_ = grove3_31.read(); 
			//grove3_32_ = grove3_32.read(); 
			grove4_33_ = grove4_33.read(); 
			grove4_34_ = grove4_34.read(); 
			grove5_1_ = grove5_1.read(); 
			grove5_2_ = grove5_2.read(); 
			
			if (grove1_6_ == true)  setGrove1_6Text(true, "1: IOIO 6 is High"); 
			else setGrove1_6Text(false, "1: IOIO 6 is Low");
			
			/*if (grove1_35_ == true) setGrove1_35Text(true,"1: IOIO 35 is High");
			else setGrove1_35Text(false,"1: IOIO 35 is Low");*/
			
			if (grove2_4_ == true) setGrove2_4Text(true,"2: IOIO 4 is High");
			else setGrove2_4Text(false,"2: IOIO 4 is Low");
			
			if (grove2_5_ == true) setGrove2_5Text(true,"2: IOIO 5 is High");
			else setGrove2_5Text(false,"2: IOIO 5 is Low");
			
			if (grove3_31_ == true) setGrove3_31Text(true,"3: IOIO 31 is High");
			else setGrove3_31Text(false,"3: IOIO 31 is Low");
			
			/*if (grove3_32_ == true) setGrove3_32Text(true,"3: IOIO 32 is High");
			else setGrove3_32Text(false,"3: IOIO 32 is Low");*/
			
			if (grove4_33_ == true) setGrove4_33Text(true,"4: IOIO 33 is High");
			else setGrove4_33Text(false,"4: IOIO 33 is Low");
			
			if (grove4_34_ == true) setGrove4_34Text(true,"4: IOIO 34 is High");
			else setGrove4_34Text(false,"4: IOIO 34 is Low");
			
			if (grove5_1_ == true) setGrove5_1Text(true,"5: IOIO 1 is High");
			else setGrove5_1Text(false,"5: IOIO 1 is Low");
			
			if (grove5_2_ == true) setGrove5_2Text(true,"5: IOIO 2 is High");
			else setGrove5_2Text(false,"5: IOIO 2 is Low");
			
			
			Thread.sleep(100);
		}
  		
  		
  		
  		@Override
		public void disconnected() {   			
  			Log.i(LOG_TAG, "IOIO disconnected");
			if (debug_ == true) {  			
	  			showToast("Bluetooth Disconnected");
  			}			
		}

		@Override
		public void incompatible() {  //if the wrong firmware is there
			//AlertDialog.Builder alert=new AlertDialog.Builder(context); //causing a crash
			//alert.setTitle(getResources().getString(R.string.notFoundString)).setIcon(R.drawable.icon).setMessage(getResources().getString(R.string.bluetoothPairingString)).setNeutralButton(getResources().getString(R.string.OKText), null).show();	
			showToast("Incompatbile firmware!");
			showToast("This app won't work until you flash the IOIO with the correct firmware!");
			showToast("You can use the IOIO Dude application on your PC/Mac to upgrade to the correct firmware");
			Log.e(LOG_TAG, "Incompatbile firmware!");
		}
  		
  		}

  	@Override
  	protected IOIOLooper createIOIOLooper() {
  		return new IOIOThread();
  	}
    
    private  void showToast(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
                toast.show();
			}
		});
	}  
    
    private void showToastShort(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
                toast.show();
			}
		});
	} 
    
    private void setNumber(float f) {
		final String str = String.format("%.2f", f);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textView_.setText(str);
			}
		});
	}
	
	private void setProx(float f) {
		final String str = String.format("%.2f", f);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//ProxTextView_.setText(str);
				Grove3_32TextView.setText("3. Analog Input: " + str);
			}
		});
	}
	
	private void setAlcohol(float f) {
		final String str = String.format("%.2f", f);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//AlcoholTextView_.setText(str);
				Grove1_35TextView.setText("1. Analog Input: " + str);
			}
		});
	}
	
	private void setGrove1_6Text(final boolean inputPin , final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Grove1_6TextView.setText(str);
				if (inputPin == true) Grove1_6TextView.setTextColor(getResources().getColor(R.color.red));
				else Grove1_6TextView.setTextColor(getResources().getColor(R.color.white));
			}
		});
	}
	
	/*private void setGrove1_35Text(final boolean inputPin , final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Grove1_35TextView.setText(str);
				if (inputPin == true) Grove1_35TextView.setTextColor(getResources().getColor(R.color.red));
				else Grove1_35TextView.setTextColor(getResources().getColor(R.color.white));
			}
		});
	}*/
	
	private void setGrove2_4Text(final boolean inputPin , final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Grove2_4TextView.setText(str);
				if (inputPin == true) Grove2_4TextView.setTextColor(getResources().getColor(R.color.red));
				else Grove2_4TextView.setTextColor(getResources().getColor(R.color.white));
			}
		});
	}
	
	private void setGrove2_5Text(final boolean inputPin , final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Grove2_5TextView.setText(str);
				if (inputPin == true) Grove2_5TextView.setTextColor(getResources().getColor(R.color.red));
				else Grove2_5TextView.setTextColor(getResources().getColor(R.color.white));
			}
		});
	}
	
	private void setGrove3_31Text(final boolean inputPin , final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Grove3_31TextView.setText(str);
				if (inputPin == true) Grove3_31TextView.setTextColor(getResources().getColor(R.color.red));
				else Grove3_31TextView.setTextColor(getResources().getColor(R.color.white));
			}
		});
	}
	
	/*private void setGrove3_32Text(final boolean inputPin , final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Grove3_32TextView.setText(str);
				if (inputPin == true) Grove3_32TextView.setTextColor(getResources().getColor(R.color.red));
				else Grove3_32TextView.setTextColor(getResources().getColor(R.color.white));
			}
		});
	}*/
	
	private void setGrove4_33Text(final boolean inputPin , final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Grove4_33TextView.setText(str);
				if (inputPin == true) Grove4_33TextView.setTextColor(getResources().getColor(R.color.red));
				else Grove4_33TextView.setTextColor(getResources().getColor(R.color.white));
			}
		});
	}
	
	private void setGrove4_34Text(final boolean inputPin , final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Grove4_34TextView.setText(str);
				if (inputPin == true) Grove4_34TextView.setTextColor(getResources().getColor(R.color.red));
				else Grove4_34TextView.setTextColor(getResources().getColor(R.color.white));
			}
		});
	}
	
	private void setGrove5_1Text(final boolean inputPin , final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Grove5_1TextView.setText(str);
				if (inputPin == true) Grove5_1TextView.setTextColor(getResources().getColor(R.color.red));
				else Grove5_1TextView.setTextColor(getResources().getColor(R.color.white));
			}
		});
	}
	
	private void setGrove5_2Text(final boolean inputPin , final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Grove5_2TextView.setText(str);
				if (inputPin == true) Grove5_2TextView.setTextColor(getResources().getColor(R.color.red));
				else Grove5_2TextView.setTextColor(getResources().getColor(R.color.white));
			}
		});
	}
    
    public class ConnectTimer extends CountDownTimer
   	{

   		public ConnectTimer(long startTime, long interval)
   			{
   				super(startTime, interval);
   			}

   		@Override
   		public void onFinish()
   			{
   				if (deviceFound == 0) {
   					showNotFound(); 					
   				}
   				
   			}

   		@Override
   		public void onTick(long millisUntilFinished)				{
   			//not used
   		}
   	}
	 
    public class DecodedTimer extends CountDownTimer
   	{

   		public DecodedTimer(long startTime, long interval)
   			{
   				super(startTime, interval);
   			}

   		@Override
   		public void onFinish()
   			{
   			//decodedtimer.start(); //normally we re-start the timer to loop but not here
   			
   			//we are done to now proceed to the next test
   			//animationTimerDone();
   			//showToast("done");
   			
   			/*MainActivity  cls2 = new MainActivity();
   			cls2.animationTimerDone();*/
   			
   			
   			MainActivity.animationTimerDone();
   				
   			}
   		
   		@Override
   		public void onTick(long millisUntilFinished)	{
   			
   			//to do bug: the first frame is not display on pixel the first go around but does get displayed after looping
   			//now we need to read in the raw file, it's already in RGB565 format and scaled so we don't need to do any scaling
   			//Log.d("Animations","inside the decoder timer");
   			
   			File file = new File(decodedDirPath + "/" + selectedFileName + ".rgb565");
   			if (file.exists()) {
   				
				
				RandomAccessFile raf = null;
				
				//let's setup the seeker object
				try {
					raf = new RandomAccessFile(file, "r");
					try {
						raf.seek(0);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}  // "r" means open the file for reading
			
				
				//if (x == selectedFileTotalFrames - 1) { // Manju - Reached End of the file.
	   			//	x = 0;
	   		//	}
				
				if (x == selectedFileTotalFrames) { // Manju - Reached End of the file.
					x = 0;
	   				try {
						raf.seek(0); //go to the beginning of the rgb565 file
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	   			}
				
				 switch (selectedFileResolution) {
		            case 16: frame_length = 1048;
		                     break;
		            case 32: frame_length = 2048;
		                     break;
		            case 64: frame_length = 4096;
		                     break;
		            case 128: frame_length = 8192;
                    		break;
		            default: frame_length = 2048;
		                     break;
		          }
				
				//now let's see forward to a part of the file
				try {
					raf.seek(x*frame_length);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} 
				//Log.d("PixelAnimations","x is: " + x);
				//Log.d("seeker","seeker is: " + x*frame_length);
				
	   			 
	   			if (frame_length > Integer.MAX_VALUE) {
	   			    try {
						throw new IOException("The file is too big");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	   			}
	   			 
	   			// Create the byte array to hold the data
	   			//byte[] bytes = new byte[(int)length];
	   			BitmapBytes = new byte[(int)frame_length];
	   			
	   			//BitmapBytes = raf.readFully(byte[] b, int off, int len);
	   		//	readFully(BitmapBytes, 0, 2048);
	   		 // read the full data into the buffer
	         //   dis.readFully(buf);
	   			//Reads exactly len bytes from this file into the byte array, starting at the current file pointer.
	   			 
	   			// Read in the bytes
	   			int offset = 0;
	   			int numRead = 0;
	   			try {
					while (offset < BitmapBytes.length && (numRead=raf.read(BitmapBytes, offset, BitmapBytes.length-offset)) >= 0) {
					    offset += numRead;
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	   			 
	   			// Ensure all the bytes have been read in
	   			if (offset < BitmapBytes.length) {
	   			    try {
						throw new IOException("The file was not completely read: "+file.getName());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	   			}
	   			 
	   			// Close the input stream, all file contents are in the bytes variable
	   			try {
	   				raf.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
	   			
	   			//BitmapBytes = bos.toByteArray();
	   			
	   			//now that we have the byte array loaded, load it into the frame short array
	   			
	   			int y = 0;
	     		for (int i = 0; i < frame_.length; i++) {
	     			frame_[i] = (short) (((short) BitmapBytes[y] & 0xFF) | (((short) BitmapBytes[y + 1] & 0xFF) << 8));
	     			y = y + 2;
	     		}
	     		
	     		//we're done with the images so let's recycle them to save memory
	    	   // canvasBitmap.recycle();
	    	 //  bitmap.recycle(); 
	   		
		   		//and then load to the LED matrix
	     		
			   	try {
					matrix_.frame(frame_);
					
				} catch (ConnectionLostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   	x++;
   			}
   			
   			else {
   				showToast("We have a problem, couldn't find the decoded file");
   			}
   		}
   	}

   	
    
    private void showNotFound() {	
		AlertDialog.Builder alert=new AlertDialog.Builder(this);
		alert.setTitle(getResources().getString(R.string.notFoundString)).setIcon(R.drawable.icon).setMessage(getResources().getString(R.string.bluetoothPairingString)).setNeutralButton(getResources().getString(R.string.OKText), null).show();	
    }

	
	public static class GifView extends View {

		public static final int IMAGE_TYPE_UNKNOWN = 0;
		public static final int IMAGE_TYPE_STATIC = 1;
		public static final int IMAGE_TYPE_DYNAMIC = 2;

		public static final int DECODE_STATUS_UNDECODE = 0;
		public static final int DECODE_STATUS_DECODING = 1;
		public static final int DECODE_STATUS_DECODED = 2;

		private GifDecoder decoder;
		private Bitmap bitmap;

		public int imageType = IMAGE_TYPE_UNKNOWN;
		public int decodeStatus = DECODE_STATUS_UNDECODE;

		private int width;
		private int height;

		private long time;
		private int index = 0;

		private int resId;
		private String filePath;

		private boolean playFlag = false;
		//private FileOutputStream fos;  //true means append, false is over-write

		public  GifView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}
		

		/**
		 * Constructor
		 */
		///***** action code in the view is here ************************
		
		public GifView(Context context) {
			super(context);
	       //  LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	        // setGif(R.drawable.mushroom);
	         //setGif(basepath + "/pixelpiledriver/" + "frank.gif");
	        // Bitmap mBitmap = Bitmap.decodeFile(path.getPath()+"/"+ fileNames[i]);

	       //  play();
		}
		
		

		private InputStream getInputStream() {
			if (filePath != null)
				try {
					return new FileInputStream(filePath);
				} catch (FileNotFoundException e) {
				}
			if (resId > 0)
				return getContext().getResources().openRawResource(resId);
			return null;
		}

		/**
		 * set gif file path
		 * 
		 * @param filePath
		 */
		public void setGif(String filePath) {
			Bitmap bitmap = BitmapFactory.decodeFile(filePath);
			setGif(filePath, bitmap);
		}

		/**
		 * set gif file path and cache image
		 * 
		 * @param filePath
		 * @param cacheImage
		 */
		public void setGif(String filePath, Bitmap cacheImage) {
			this.resId = 0;
			this.filePath = filePath;
			imageType = IMAGE_TYPE_UNKNOWN;
			decodeStatus = DECODE_STATUS_UNDECODE;
			playFlag = false;
			bitmap = cacheImage;
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			//setLayoutParams(new LayoutParams(width, height));
			setLayoutParams(new LayoutParams(32, 32)); //changed this because otherwise the larger animations were taking up the whole screen
		}

		/**
		 * set gif resource id
		 * 
		 * @param resId
		 */
		public void setGif(int resId) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
			setGif(resId, bitmap);
		}

		/**
		 * set gif resource id and cache image
		 * 
		 * @param resId
		 * @param cacheImage
		 */
		
		// to do, handle the case if it's a single frame gif
		public void setGif(int resId, Bitmap cacheImage) {
			this.filePath = null;
			this.resId = resId;
			imageType = IMAGE_TYPE_UNKNOWN;
			decodeStatus = DECODE_STATUS_UNDECODE;
			playFlag = false;
			bitmap = cacheImage;
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			setLayoutParams(new LayoutParams(width, height));
		}

		private void decode() {
			release();
			index = 0;

			decodeStatus = DECODE_STATUS_DECODING;

			new Thread() {
				@Override
				public void run() {
					decoder = new GifDecoder();
					decoder.read(getInputStream());
					if (decoder.width == 0 || decoder.height == 0) {
						imageType = IMAGE_TYPE_STATIC;
					} else {
						imageType = IMAGE_TYPE_DYNAMIC;
					}
					postInvalidate();
					time = System.currentTimeMillis();
					decodeStatus = DECODE_STATUS_DECODED;
				}
			}.start();
		}

		public void release() {
			decoder = null;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			if (decodeStatus == DECODE_STATUS_UNDECODE) {
				canvas.drawBitmap(bitmap, 100, 100, null);
			
				if (playFlag) {
					decode();
					invalidate();
				}
			} else if (decodeStatus == DECODE_STATUS_DECODING) {
				canvas.drawBitmap(bitmap, 0, 0, null); //could add a decoding in progress message here
				invalidate();
			} else if (decodeStatus == DECODE_STATUS_DECODED) {
				if (imageType == IMAGE_TYPE_STATIC) { //static gif
					canvas.drawBitmap(bitmap, 0, 0, null);
				} else if (imageType == IMAGE_TYPE_DYNAMIC) {
					if (playFlag) {
						//long now = System.currentTimeMillis();
						index++;
						//showToast("delay: " + decoder.getDelay(index)); //took out the time delay to make decoding faster
						/*if (time + decoder.getDelay(index) < now) {
							//time += decoder.getDelay(index);
							incrementFrameIndex(); //this increments the index, we started at 0, so now we start at 1
						}*/
						Bitmap bitmap = decoder.getFrame(index);
						if (bitmap != null) {  //this is the main one, let's ioio here
							//canvas.drawBitmap(bitmap, 0, 0, null); //removed this because we'll re-write to canvas ioioBitmap instead
							
							    width_original = bitmap.getWidth();
					   		    height_original = bitmap.getHeight();
					   		    
						   		 if (width_original != KIND.width || height_original != KIND.height) { //we need to re-size
						    			 resizedFlag = 1;
						    			 //the iamge is not the right dimensions, so we need to re-size
						    			 scaleWidth = ((float) KIND.width) / width_original;
						 	   		 	 scaleHeight = ((float) KIND.height) / height_original;
						 	 	   		 // create matrix for the manipulation
						 	 	   		 matrix2 = new Matrix();
						 	 	   		 // resize the bit map
						 	 	   		 matrix2.postScale(scaleWidth, scaleHeight);
						 	 	   		 //resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width_original, height_original, matrix2, true);
						 	 	   		 resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width_original, height_original, matrix2, false);
						 	 	   		 IOIOBitmap = Bitmap.createBitmap(KIND.width, KIND.height, Config.RGB_565); 
						 	 	   		 canvasIOIO = new Canvas(IOIOBitmap);
						 	 	   		 canvasIOIO.drawRGB(0,0,0); //a black background
						 	 	   		 canvasIOIO.drawBitmap(resizedBitmap, 0, 0, null);
						 	 	   		 //ByteBuffer buffer = ByteBuffer.allocate(KIND.width * KIND.height *2); //Create a new buffer
						 	 	   		  buffer = ByteBuffer.allocate(KIND.width * KIND.height *2); //Create a new buffer
						 	 	   		 IOIOBitmap.copyPixelsToBuffer(buffer); //copy the bitmap 565 to the buffer		
						 	 	   		 BitmapBytes = buffer.array(); //copy the buffer into the type array
						 	 	   		 canvas.drawBitmap(IOIOBitmap, 0, 0, null); //where the image gets drawn to the screen
						    		 }
						   		 else {
									 	resizedFlag = 0; 			
										//but need to convert this image to 565 before we can send to the matrix
							   			IOIOBitmap = Bitmap.createBitmap(KIND.width, KIND.height, Config.RGB_565); //blank bitmap
										canvasIOIO = new Canvas(IOIOBitmap); //blank canvas with blank bitmap
										canvasIOIO.drawRGB(0,0,0); //a black background
										canvasIOIO.drawBitmap(bitmap, 0, 0, null); //now let's draw the real .gif bitmap onto it
								 	    ByteBuffer buffer = ByteBuffer.allocate(KIND.width * KIND.height *2); //Create a new buffer
								 	    IOIOBitmap.copyPixelsToBuffer(buffer); //now IOIOBitmap has real bits in it because of the canvas work		
								 	    BitmapBytes = buffer.array(); //copy the buffer into the type array	
								 	    canvas.drawBitmap(IOIOBitmap, 0, 0, null); //write the animated .gif to the screen
						   		 } 
				   		
					   	//**** had to add this as the decoded dir could have been deleted if the user changed the led panel type
					    //File decodeddir = new File(basepath + "/pixel/animatedgifs/decoded");
					    /*File decodeddir = new File(GIFPath + "decoded");
					    if(decodeddir.exists() == false)
			             {
					    	decodeddir.mkdirs();
			             }*/
					    
					    File decodeddir = new File(decodedDirPath); //this could be gif, gif64, or usergif
					    if(decodeddir.exists() == false)
			             {
					    	decodeddir.mkdirs();
			             }
						//*********************   		 
					   
				   		if (index <= decoder.getFrameCount()) { 	
					   			try {
									//writeFile(BitmapBytes, decodedDirPath + "/" + selectedFileName + "/" + selectedFileName + index + ".rgb565");  //this one the original one
									appendWrite(BitmapBytes, decodedDirPath + "/" + selectedFileName + ".rgb565"); //this writes one big file instead of individual ones
									//Log.d("PixelAnimate","initial write: " + index);
									//Log.d("PixelAnimate", "frame count: " + decoder.getFrameCount());
									//index++;
								  
									
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									Log.e("PixelAnimate", "Had a problem writing the original unified animation rgb565 file");
									e1.printStackTrace();
								}
				   		}
						   		
				   		else {  //ok we're done, now let's write the text file meta data with delay time in between frames and number frames				         
				        ///***************************************************************
				   		Log.v("PixelAnimate", "Frame Count: " + decoder.getFrameCount());
				       // String filetag = String.valueOf(decoder.getFrameCount()) + "," + String.valueOf(decoder.getDelay(index-1)) + "," + String.valueOf(currentResolution);  //our format is number of frames,delay 32,60,32 equals 32 frames, 60 ms time delay, 32 resolution   resolution is 16 for 16x32 or 32 for 32x32 led matrix, we need this in case the user changes the resolution in the app, then we'd need to catch this mismatch and re-decode
				       //note here we're using the delay of the first frame, that becomes the delay for all the frames in the gif
				   		
				   		int frameDelay = decoder.getDelay(1); //we'll use the second frame as some animated gifs have a longer frame rate for the first frame
				   		
				   		if (decoder.getDelay(1) == 0 || decoder.getFrameCount() == 1) {  //the code crashes on a 0 frame delay so we'll need to check that and change to 100 ms if 0 and also if it's a single frame gif, we'll hardcode the frame delay
				   			frameDelay = 100;
				   		}
				   		
				   		//the 64x64 configuration skips frame is the speed is greater than 70 so we need to reset the frame speed here if below 70
				   		/*if (currentResolution == 128 && decoder.getDelay(1) < 70) {  //70ms is the fastest for 64x64
				   			frameDelay = 70; //if it's too fast, then we need to slow down to 70ms frame delay
				   		}*/
				   		
				   		Log.v("PixelAnimate", "Frame Delay: " + frameDelay);
				   		
				   		//String filetag = String.valueOf(decoder.getFrameCount()) + "," + String.valueOf(decoder.getDelay(1)) + "," + String.valueOf(currentResolution);  //our format is number of frames,delay 121,60,32 equals 121 frames, 60 ms time delay, 32 resolution   resolution is 16 for 16x32 or 32 for 32x32 led matrix, we need this in case the user changes the resolution in the app, then we'd need to catch this mismatch and re-decode
				   		String filetag = String.valueOf(decoder.getFrameCount()) + "," + String.valueOf(frameDelay) + "," + String.valueOf(currentResolution); //current resolution may need to change to led panel type
				   		
				   	 //  Toast toast14 = Toast.makeText(context, "delay is: " + String.valueOf(frameDelay), Toast.LENGTH_LONG);
					 //  toast14.show();	
				   	
				        String exStorageState = Environment.getExternalStorageState();
				     	if (Environment.MEDIA_MOUNTED.equals(exStorageState)){
				     		try {
				     			
				     		   File myFile = new File(decodedDirPath + "/" + selectedFileName + ".txt");  //decoded/rain.txt						       
				     		   myFile.createNewFile();
					           FileOutputStream fOut = null;
							   fOut = new FileOutputStream(myFile);
					           OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
							   myOutWriter.append(filetag);  
							  // myOutWriter.append(String.valueOf(decoder.getDelay(index)));
							   myOutWriter.close();
							   fOut.close();
							   
							   //Toast toast1 = Toast.makeText(context, "One Time Decode Finished", Toast.LENGTH_LONG);
						      // toast1.show();	
						       gifView.stop();
						       MainActivity nonstaticCall = new MainActivity();
						       //*****************************************************
							   //we're done decoding and we've written our file so let's animate!
						       nonstaticCall.letsAnimate();     // this is a hack to make a call to animateAfterDecode which needs to be non-static, didn't have time to make gifview non-static
						     // Log.d("PixelAnimate","made it to the nonstatic call");
						       // animateAfterDecode(0);
				     			
				     		} catch (IOException e) {
				     			e.printStackTrace();
				     			//Toast.makeText(this, "Couldn't save", Toast.LENGTH_SHORT);
				     		}
					     	}
					     	else {					     		
					     		Toast toast2 = Toast.makeText(context, "Storage Not Available, Cannot Continue", Toast.LENGTH_LONG);
						        toast2.show();	
					     	}
				   		}	
					}
						
						invalidate();
					} else {
						Bitmap bitmap = decoder.getFrame(index);
						canvas.drawBitmap(bitmap, 0, 0, null);
					}
				} 
				
				else {
					canvas.drawBitmap(bitmap, 0, 0, null);
				}
			}
			
		
     		//we're done with the images so let's recycle them to save memory
    	   // canvasBitmap.recycle();
    	 //  bitmap.recycle(); 
    	    
    	  //if ( resizedFlag == 1) {
    	    //	IOIOBitmap.recycle(); //only there if we had to resize an image
    	 // }
			
		   
		}
		
		/*public void writeFile(byte[] data, String fileName) throws IOException{
			  FileOutputStream out = new FileOutputStream(fileName);
			  out.write(data);
			  out.close();
		}*/
		
		public void appendWrite(byte[] data, String filename) throws IOException {
			 FileOutputStream fos = new FileOutputStream(filename, true);  //true means append, false is over-write
		     fos.write(data);
		     fos.close();
		}


		/*private void incrementFrameIndex() {
			index++;
			//if (index >= decoder.getFrameCount()) {
			//	index = 0;
			//}
		}

		private void decrementFrameIndex() {
			index--;
			if (index < 0) {
				index = decoder.getFrameCount() - 1;
			}
		}*/

		public void play() {
			time = System.currentTimeMillis();
			playFlag = true;
			invalidate();
		}

		/*public void pause() {
			playFlag = false;
			invalidate();
		}*/

		public void stop() {
			playFlag = false;
			index = 0;
			invalidate();
			
		}

		/*public void nextFrame() {
			if (decodeStatus == DECODE_STATUS_DECODED) {
				incrementFrameIndex();
				invalidate();
			}
		}

		public void prevFrame() {  //not used
			if (decodeStatus == DECODE_STATUS_DECODED) {
				decrementFrameIndex();
				invalidate();
			}
		}*/
	}
}