package com.soumio.inceptiontutorial.Classify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soumio.inceptiontutorial.ActivityHistory;
import com.soumio.inceptiontutorial.Analyze.ChooseModel;
import com.soumio.inceptiontutorial.Leads;
import com.soumio.inceptiontutorial.R;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Classify_L2 extends AppCompatActivity {

    private static final String TAG = "Classify_L2";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    //Firebase connection
    /*  private FirebaseFirestore db = FirebaseFirestore.getInstance();*/

    // presets for rgb conversion
    private static final int RESULTS_TO_SHOW = 3;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;

    // options for model interpreter
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();
    Uri uri, uri2, uri3, uri4, uri5, uri6, uri7, uri8, uri9, uri10, uri11, uri12;
    Bitmap bitmap, bitmap2, bitmap3, bitmap4, bitmap5, bitmap6, bitmap7, bitmap8, bitmap9, bitmap10, bitmap11, bitmap12;
    Bitmap bitmap_orig;
    Bitmap bitmap_orig2;
    Bitmap bitmap_orig3;
    Bitmap bitmap_orig4;
    Bitmap bitmap_orig5;
    Bitmap bitmap_orig6;
    Bitmap bitmap_orig7;
    Bitmap bitmap_orig8;
    Bitmap bitmap_orig9;
    Bitmap bitmap_orig10;
    Bitmap bitmap_orig11;
    Bitmap bitmap_orig12;

    DatabaseReference databaseLeads;
    Button btstart;
    // tflite graph
    private Interpreter tflite;
    // holds all the possible labels for model
    private List<String> labelList;
    // holds the selected image data as bytes
    private ByteBuffer imgData = null;
    // holds the probabilities of each label for non-quantized graphs
    private float[][] labelProbArray = null;
    // holds the probabilities of each label for quantized graphs
    private byte[][] labelProbArrayB = null;
    // array that holds the labels with the highest probabilities
    private String[] topLables, topLables2, topLables3, topLables4, topLables5, topLables6, topLables7, topLables8, topLables9, topLables10, topLables11, topLables12 = null;
    // array that holds the highest probabilities
    private String[] topConfidence, topConfidence2, topConfidence3, topConfidence4, topConfidence5, topConfidence6, topConfidence7, topConfidence8, topConfidence9, topConfidence10, topConfidence11, topConfidence12 = null;
    // selected classifier information received from extras
    private String chosen;
    private boolean quant;
    // input image dimensions for the Inception Model
    private int DIM_IMG_SIZE_X = 224;
    private int DIM_IMG_SIZE_Y = 224;
    private int DIM_PIXEL_SIZE = 3;
    // int array to hold image data
    private int[] intValues;
    // activity elements
    private ImageView selected_image, selected_image2, selected_image3, selected_image4, selected_image5,
            selected_image6, selected_image7, selected_image8, selected_image9, selected_image10, selected_image11, selected_image12;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12, btnCategory, upload_btn;
    private Button back_button, history_button;
    private TextView label1, label2, label3, label4, label5, label6, label7, label8, label9, label10, label11, label12, labelCategory;
    private TextView Confidence1;
    private TextView Confidence2;
    private TextView Confidence3;
    private TextView Confidence4;
    private TextView Confidence5;
    private TextView Confidence6;
    private EditText level;
    private ProgressDialog progressDialog;


    // priority queue that will hold the top results from the CNN
    private PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    (o1, o2) -> (o1.getValue()).compareTo(o2.getValue()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // get all selected classifier data from classifiers
        chosen = getIntent().getStringExtra("chosen");
        quant = getIntent().getBooleanExtra("quant", false);
        // initialize array that holds image data
        intValues = new int[DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y];
        super.onCreate(savedInstanceState);

        init();
        showPDialog1();
        showDialog2();

        new Handler().postDelayed(() -> {
            Toast.makeText(getBaseContext(), "Completed!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }, 10000);

        int noOfSecond = 1;
        new Handler().postDelayed(() -> {
                    //TODO Set your button auto perform click.
                    btn1.performClick();
                }, noOfSecond * 1000
        );
        //initilize graph and labels
        run1();
        // initialize byte array. The size depends if the input data needs to be quantized or not
        if (quant) {
            imgData =
                    ByteBuffer.allocateDirect(
                            DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE);
        } else {
            imgData =
                    ByteBuffer.allocateDirect(
                            4 * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE);
        }
        imgData.order(ByteOrder.nativeOrder());

        // initialize probabilities array. The datatypes that array holds depends if the input data needs to be quantized or not
        if (quant) {
            labelProbArrayB = new byte[1][labelList.size()];
        } else {
            labelProbArray = new float[1][labelList.size()];
        }
        setContentView(R.layout.activity_classify__l2);

        databaseLeads = FirebaseDatabase.getInstance().getReference("LEADS");

        // labels that hold top three results of CNN
        label1 = findViewById(R.id.label1);
        label2 = findViewById(R.id.label2);
        label3 = findViewById(R.id.label3);
        // displays the probabilities of top labels
       /*      Confidence1 = (TextView) findViewById(R.id.Confidence1);
        Confidence2 = (TextView) findViewById(R.id.Confidence2);
        Confidence3 = (TextView) findViewById(R.id.Confidence3);*/

        // labels that hold top three results of CNN
        label4 = findViewById(R.id.label4);
        labelCategory = findViewById(R.id.labelCategory);
        label5 = findViewById(R.id.label5);
        label6 = findViewById(R.id.label6);
        label7 = findViewById(R.id.label7);
        label8 = findViewById(R.id.label8);
        label9 = findViewById(R.id.label9);
        label10 = findViewById(R.id.label10);
        label11 = findViewById(R.id.label11);
        label12 = findViewById(R.id.label12);

        // initialize imageView that displays selected image to the user
        selected_image = findViewById(R.id.selected_image1);
        selected_image2 = findViewById(R.id.selected_image2);
        selected_image3 = findViewById(R.id.selected_image3);
        selected_image4 = findViewById(R.id.selected_image4);
        selected_image5 = findViewById(R.id.selected_image5);
        selected_image6 = findViewById(R.id.selected_image6);
        selected_image7 = findViewById(R.id.selected_image7);
        selected_image8 = findViewById(R.id.selected_image8);
        selected_image9 = findViewById(R.id.selected_image9);
        selected_image10 = findViewById(R.id.selected_image10);
        selected_image11 = findViewById(R.id.selected_image11);
        selected_image12 = findViewById(R.id.selected_image12);

        // initialize array to hold top labels
        topLables = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence = new String[RESULTS_TO_SHOW];

        topLables2 = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence2 = new String[RESULTS_TO_SHOW];

        topLables3 = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence3 = new String[RESULTS_TO_SHOW];

        topLables4 = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence4 = new String[RESULTS_TO_SHOW];

        topLables4 = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence4 = new String[RESULTS_TO_SHOW];

        topLables5 = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence5 = new String[RESULTS_TO_SHOW];

        topLables6 = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence6 = new String[RESULTS_TO_SHOW];

        topLables7 = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence7 = new String[RESULTS_TO_SHOW];

        topLables8 = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence8 = new String[RESULTS_TO_SHOW];

        topLables9 = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence9 = new String[RESULTS_TO_SHOW];

        topLables10 = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence10 = new String[RESULTS_TO_SHOW];

        topLables11 = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence11 = new String[RESULTS_TO_SHOW];

        topLables12 = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence12 = new String[RESULTS_TO_SHOW];


        // allows user to go back to activity to select a different image
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(v -> {
            Intent back = new Intent(Classify_L2.this, ChooseModel.class);
            startActivity(back);
        });

        // classify current dispalyed image
        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(view -> {
            // get current bitmap from imageView
            if (bitmap_orig != null) {
                bitmap_orig = ((BitmapDrawable) selected_image.getDrawable()).getBitmap();
                // resize the bitmap to the required input size to the CNN
                Bitmap bitmap = getResizedBitmap(bitmap_orig, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
                // convert bitmap to byte array
                convertBitmapToByteBuffer(bitmap);
            }
            // pass byte data to the graph
            if (quant) {
                tflite.run(imgData, labelProbArrayB);
            } else {
                tflite.run(imgData, labelProbArray);
            }
            // display the results
            printTopKLabels();

            //----------------------------------------------------------------------------------
            if (bitmap_orig2 != null) {
                // get current bitmap from imageView
                bitmap_orig2 = ((BitmapDrawable) selected_image2.getDrawable()).getBitmap();
                // resize the bitmap to the required input size to the CNN
                Bitmap bitmap2 = getResizedBitmap(bitmap_orig2, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
                // convert bitmap to byte array
                convertBitmapToByteBuffer(bitmap2);
            }
            // pass byte data to the graph
            if (quant) {
                tflite.run(imgData, labelProbArrayB);
            } else {
                tflite.run(imgData, labelProbArray);
            }
            // display the results
            printTopKLabels2();
            //----------------------------------------------------------------------------------
            if (bitmap_orig3 != null) {
                // get current bitmap from imageView
                bitmap_orig3 = ((BitmapDrawable) selected_image3.getDrawable()).getBitmap();
                // resize the bitmap to the required input size to the CNN
                Bitmap bitmap3 = getResizedBitmap(bitmap_orig3, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
                // convert bitmap to byte array
                convertBitmapToByteBuffer(bitmap3);
            }
            // pass byte data to the graph
            if (quant) {
                tflite.run(imgData, labelProbArrayB);
            } else {
                tflite.run(imgData, labelProbArray);
            }
            // display the results
            printTopKLabels3();
            //----------------------------------------------------------------------------------
            if (bitmap_orig4 != null) {
                // get current bitmap from imageView
                bitmap_orig4 = ((BitmapDrawable) selected_image4.getDrawable()).getBitmap();
                // resize the bitmap to the required input size to the CNN
                Bitmap bitmap4 = getResizedBitmap(bitmap_orig4, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
                // convert bitmap to byte array
                convertBitmapToByteBuffer(bitmap4);
            }
            // pass byte data to the graph
            if (quant) {
                tflite.run(imgData, labelProbArrayB);
            } else {
                tflite.run(imgData, labelProbArray);
            }
            // display the results
            printTopKLabels4();
            //----------------------------------------------------------------------------------
            if (bitmap_orig5 != null) {
                // get current bitmap from imageView
                bitmap_orig5 = ((BitmapDrawable) selected_image5.getDrawable()).getBitmap();
                // resize the bitmap to the required input size to the CNN
                Bitmap bitmap5 = getResizedBitmap(bitmap_orig5, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
                // convert bitmap to byte array
                convertBitmapToByteBuffer(bitmap5);
            }
            // pass byte data to the graph
            if (quant) {
                tflite.run(imgData, labelProbArrayB);
            } else {
                tflite.run(imgData, labelProbArray);
            }
            // display the results
            printTopKLabels5();
            //----------------------------------------------------------------------------------
            if (bitmap_orig6 != null) {
                // get current bitmap from imageView
                bitmap_orig6 = ((BitmapDrawable) selected_image6.getDrawable()).getBitmap();
                // resize the bitmap to the required input size to the CNN
                Bitmap bitmap6 = getResizedBitmap(bitmap_orig6, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
                // convert bitmap to byte array
                convertBitmapToByteBuffer(bitmap6);
            }
            // pass byte data to the graph
            if (quant) {
                tflite.run(imgData, labelProbArrayB);
            } else {
                tflite.run(imgData, labelProbArray);
            }
            // display the results
            printTopKLabels6();
            //----------------------------------------------------------------------------------
            if (bitmap_orig7 != null) {
                // get current bitmap from imageView
                bitmap_orig7 = ((BitmapDrawable) selected_image7.getDrawable()).getBitmap();
                // resize the bitmap to the required input size to the CNN
                Bitmap bitmap7 = getResizedBitmap(bitmap_orig7, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
                // convert bitmap to byte array
                convertBitmapToByteBuffer(bitmap7);
            }
            // pass byte data to the graph
            if (quant) {
                tflite.run(imgData, labelProbArrayB);
            } else {
                tflite.run(imgData, labelProbArray);
            }
            // display the results
            printTopKLabels7();
            //----------------------------------------------------------------------------------
            if (bitmap_orig8 != null) {
                // get current bitmap from imageView
                bitmap_orig8 = ((BitmapDrawable) selected_image8.getDrawable()).getBitmap();
                // resize the bitmap to the required input size to the CNN
                Bitmap bitmap8 = getResizedBitmap(bitmap_orig8, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
                // convert bitmap to byte array
                convertBitmapToByteBuffer(bitmap8);
            }
            // pass byte data to the graph
            if (quant) {
                tflite.run(imgData, labelProbArrayB);
            } else {
                tflite.run(imgData, labelProbArray);
            }
            // display the results
            printTopKLabels8();
            //----------------------------------------------------------------------------------
            if (bitmap_orig9 != null) {
                // get current bitmap from imageView
                bitmap_orig9 = ((BitmapDrawable) selected_image9.getDrawable()).getBitmap();
                // resize the bitmap to the required input size to the CNN
                Bitmap bitmap9 = getResizedBitmap(bitmap_orig9, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
                // convert bitmap to byte array
                convertBitmapToByteBuffer(bitmap9);
            }
            // pass byte data to the graph
            if (quant) {
                tflite.run(imgData, labelProbArrayB);
            } else {
                tflite.run(imgData, labelProbArray);
            }
            // display the results
            printTopKLabels9();
            //----------------------------------------------------------------------------------
            if (bitmap_orig10 != null) {
                // get current bitmap from imageView
                bitmap_orig10 = ((BitmapDrawable) selected_image10.getDrawable()).getBitmap();
                // resize the bitmap to the required input size to the CNN
                Bitmap bitmap10 = getResizedBitmap(bitmap_orig10, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
                // convert bitmap to byte array
                convertBitmapToByteBuffer(bitmap10);
            }
            // pass byte data to the graph
            if (quant) {
                tflite.run(imgData, labelProbArrayB);
            } else {
                tflite.run(imgData, labelProbArray);
            }
            // display the results
            printTopKLabels10();
            //----------------------------------------------------------------------------------
            if (bitmap_orig11 != null) {
                // get current bitmap from imageView
                bitmap_orig11 = ((BitmapDrawable) selected_image11.getDrawable()).getBitmap();
                // resize the bitmap to the required input size to the CNN
                Bitmap bitmap11 = getResizedBitmap(bitmap_orig11, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
                // convert bitmap to byte array
                convertBitmapToByteBuffer(bitmap11);
            }
            // pass byte data to the graph
            if (quant) {
                tflite.run(imgData, labelProbArrayB);
            } else {
                tflite.run(imgData, labelProbArray);
            }
            // display the results
            printTopKLabels11();
            //----------------------------------------------------------------------------------
            if (bitmap_orig12 != null) {
                // get current bitmap from imageView
                bitmap_orig12 = ((BitmapDrawable) selected_image12.getDrawable()).getBitmap();
                // resize the bitmap to the required input size to the CNN
                Bitmap bitmap12 = getResizedBitmap(bitmap_orig12, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
                // convert bitmap to byte array
                convertBitmapToByteBuffer(bitmap12);
            }
            // pass byte data to the graph
            if (quant) {
                tflite.run(imgData, labelProbArrayB);
            } else {
                tflite.run(imgData, labelProbArray);
            }
            // display the results
            printTopKLabels12();
        });

        upload_btn = findViewById(R.id.upload_btn);
        upload_btn.setOnClickListener(view -> addUploadData());

        history_button = findViewById(R.id.history_btn);
        history_button.setOnClickListener(view -> {
            Intent history = new Intent(Classify_L2.this, ActivityHistory.class);
            startActivity(history);

        });

        //Category
        btnCategory = findViewById(R.id.btnCategory);
        btnCategory.setOnClickListener(view -> {

            if (selected_image != null && selected_image2 != null && selected_image3 != null && selected_image4 != null
                    && selected_image5 != null && selected_image6 != null && selected_image7 != null && selected_image8 != null
                    && selected_image9 != null && selected_image10 != null && selected_image11 != null && selected_image12 != null) {


                if (topLables[2].equalsIgnoreCase("invalid") || topLables2[2].equalsIgnoreCase("invalid") ||
                        topLables3[2].equalsIgnoreCase("invalid") || topLables4[2].equalsIgnoreCase("invalid") ||
                        topLables5[2].equalsIgnoreCase("invalid") || topLables6[2].equalsIgnoreCase("invalid") ||
                        topLables7[2].equalsIgnoreCase("invalid") || topLables8[2].equalsIgnoreCase("invalid") ||
                        topLables9[2].equalsIgnoreCase("invalid") || topLables10[2].equalsIgnoreCase("invalid") ||
                        topLables11[2].equalsIgnoreCase("invalid") || topLables12[2].equalsIgnoreCase("invalid")) {

                    labelCategory.setText("You entered Invalid Image Please CHECK!");

                } else if (topLables[2].equalsIgnoreCase("normal") && topLables2[2].equalsIgnoreCase("normal") &&
                        topLables3[2].equalsIgnoreCase("normal") && topLables4[2].equalsIgnoreCase("normal") &&
                        topLables5[2].equalsIgnoreCase("normal") && topLables6[2].equalsIgnoreCase("normal") &&
                        topLables7[2].equalsIgnoreCase("normal") && topLables8[2].equalsIgnoreCase("normal") &&
                        topLables9[2].equalsIgnoreCase("normal") && topLables10[2].equalsIgnoreCase("normal") &&
                        topLables11[2].equalsIgnoreCase("normal") && topLables12[2].equalsIgnoreCase("normal")) {
                    labelCategory.setText("NORMAL ECG");
                } else if (topLables[2].equalsIgnoreCase("irregular") && topLables2[2].equalsIgnoreCase("irregular") &&
                        topLables3[2].equalsIgnoreCase("irregular") && topLables4[2].equalsIgnoreCase("irregular") &&
                        topLables5[2].equalsIgnoreCase("irregular") && topLables6[2].equalsIgnoreCase("irregular") &&
                        topLables7[2].equalsIgnoreCase("irregular") && topLables8[2].equalsIgnoreCase("irregular") &&
                        topLables9[2].equalsIgnoreCase("irregular") && topLables10[2].equalsIgnoreCase("normal") &&
                        topLables11[2].equalsIgnoreCase("irregular") && topLables12[2].equalsIgnoreCase("irregular")) {
                    labelCategory.setText("IRREGULAR ECG");

                } else if (topLables[2].equalsIgnoreCase("irregular") && topLables5[2].equalsIgnoreCase("irregular") &&
                        topLables11[2].equalsIgnoreCase("irregular") && topLables12[2].equalsIgnoreCase("irregular")) {
                    labelCategory.setText("LATERAL WALL");

                } else if (topLables2[2].equalsIgnoreCase("irregular") && topLables3[2].equalsIgnoreCase("irregular") &&
                        topLables6[2].equalsIgnoreCase("irregular")) {

                    labelCategory.setText("INFERIOR WALL");
                } else if (topLables7[2].equalsIgnoreCase("irregular") && topLables8[2].equalsIgnoreCase("irregular")) {
                    labelCategory.setText("SEPTAL WALL");

                } else if (topLables10[2].equalsIgnoreCase("irregular") && topLables11[2].equalsIgnoreCase("irregular")) {
                    labelCategory.setText("ANTERIOR WALL");

                } else {
                    labelCategory.setText("IRREGULAR ECG");
                }
            } else {
                Toast.makeText(Classify_L2.this, "Please Upload All Images! ", Toast.LENGTH_LONG).show();

            }

        });

        // get image from previous activity to show in the imageView
        uri = getIntent().getParcelableExtra("resID_uri");
        try {
            if (uri != null && bitmap != null) {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }
            Glide.with(this).asBitmap().load(bitmap).into(selected_image);
            // not sure why this happens, but without this the image appears on its side
            selected_image.setRotation(selected_image.getRotation() + 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get image from previous activity to show in the imageView

        uri2 = getIntent().getParcelableExtra("resID_uri2");
        try {
            if (uri2 != null && bitmap2 != null) {
                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
            }
            Glide.with(this).asBitmap().load(bitmap2).into(selected_image2);
            // not sure why this happens, but without this the image appears on its side
            selected_image2.setRotation(selected_image2.getRotation() + 0);
        } catch (IOException e) {
            e.printStackTrace();
        }


        uri3 = getIntent().getParcelableExtra("resID_uri3");
        try {
            if (uri3 != null && bitmap3 != null) {
                bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri3);
            }
            Glide.with(this).asBitmap().load(bitmap3).into(selected_image3);
            // not sure why this happens, but without this the image appears on its side
            selected_image3.setRotation(selected_image3.getRotation() + 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get image from previous activity to show in the imageView
        uri4 = getIntent().getParcelableExtra("resID_uri4");
        try {
            if (uri4 != null && bitmap4 != null) {
                bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri4);
            }
            Glide.with(this).asBitmap().load(bitmap4).into(selected_image4);
            // not sure why this happens, but without this the image appears on its side
            selected_image4.setRotation(selected_image4.getRotation() + 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get image from previous activity to show in the imageView
        uri5 = getIntent().getParcelableExtra("resID_uri5");
        try {
            if (uri5 != null && bitmap5 != null) {
                bitmap5 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri5);
            }
            Glide.with(this).asBitmap().load(bitmap5).into(selected_image5);
            // not sure why this happens, but without this the image appears on its side
            selected_image5.setRotation(selected_image5.getRotation() + 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get image from previous activity to show in the imageView
        uri6 = getIntent().getParcelableExtra("resID_uri6");
        try {
            if (uri6 != null && bitmap6 != null) {
                bitmap6 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri6);
            }
            Glide.with(this).asBitmap().load(bitmap6).into(selected_image6);
            // not sure why this happens, but without this the image appears on its side
            selected_image6.setRotation(selected_image6.getRotation() + 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get image from previous activity to show in the imageView
        uri7 = getIntent().getParcelableExtra("resID_uri7");
        try {
            if (uri7 != null && bitmap7 != null) {
                bitmap7 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri7);
            }
            Glide.with(this).asBitmap().load(bitmap7).into(selected_image7);
            // not sure why this happens, but without this the image appears on its side
            selected_image7.setRotation(selected_image7.getRotation() + 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get image from previous activity to show in the imageView
        uri8 = getIntent().getParcelableExtra("resID_uri8");
        try {
            if (uri8 != null && bitmap8 != null) {
                bitmap8 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri8);
            }
            Glide.with(this).asBitmap().load(bitmap8).into(selected_image8);
            // not sure why this happens, but without this the image appears on its side
            selected_image8.setRotation(selected_image8.getRotation() + 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get image from previous activity to show in the imageView
        uri9 = getIntent().getParcelableExtra("resID_uri9");
        try {
            if (uri9 != null && bitmap9 != null) {
                bitmap9 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri9);
            }
            Glide.with(this).asBitmap().load(bitmap9).into(selected_image9);
            // not sure why this happens, but without this the image appears on its side
            selected_image9.setRotation(selected_image9.getRotation() + 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get image from previous activity to show in the imageView
        uri10 = getIntent().getParcelableExtra("resID_uri10");
        try {
            if (uri10 != null && bitmap10 != null) {
                bitmap10 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri10);
            }
            Glide.with(this).asBitmap().load(bitmap10).into(selected_image10);
            // not sure why this happens, but without this the image appears on its side
            selected_image10.setRotation(selected_image10.getRotation() + 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get image from previous activity to show in the imageView
        uri11 = getIntent().getParcelableExtra("resID_uri11");
        try {
            if (uri11 != null && bitmap11 != null) {
                bitmap11 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri11);
            }
            Glide.with(this).asBitmap().load(bitmap11).into(selected_image11);
            // not sure why this happens, but without this the image appears on its side
            selected_image11.setRotation(selected_image11.getRotation() + 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get image from previous activity to show in the imageView
        uri12 = getIntent().getParcelableExtra("resID_uri12");
        try {
            if (uri12 != null && bitmap12 != null) {
                bitmap12 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri12);
            }
            Glide.with(this).asBitmap().load(bitmap12).into(selected_image12);
            // not sure why this happens, but without this the image appears on its side
            selected_image12.setRotation(selected_image12.getRotation() + 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void init() {
        this.progressDialog = new ProgressDialog(this);
    }

    private void showPDialog1() {
    }

    private void showDialog2() {
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dial);
    }

    private void addUploadData() {
        String category = labelCategory.getText().toString().trim();
        String lead1 = label1.getText().toString().trim();
        String lead2 = label2.getText().toString().trim();
        String lead3 = label3.getText().toString().trim();
        String lead4 = label4.getText().toString().trim();
        String lead5 = label5.getText().toString().trim();
        String lead6 = label6.getText().toString().trim();
        String lead7 = label7.getText().toString().trim();
        String lead8 = label8.getText().toString().trim();
        String lead9 = label9.getText().toString().trim();
        String lead10 = label10.getText().toString().trim();
        String lead11 = label11.getText().toString().trim();
        String lead12 = label12.getText().toString().trim();

        if (!TextUtils.isEmpty(lead1) && !TextUtils.isEmpty(lead2) && !TextUtils.isEmpty(lead3) && !TextUtils.isEmpty(lead4) && !TextUtils.isEmpty(lead5)
                && !TextUtils.isEmpty(lead6) && !TextUtils.isEmpty(lead7) && !TextUtils.isEmpty(lead8) && !TextUtils.isEmpty(lead9) && !TextUtils.isEmpty(lead10)
                && !TextUtils.isEmpty(lead11) && !TextUtils.isEmpty(lead12)) {


            if (!TextUtils.isEmpty(category)) {
                String id = databaseLeads.push().getKey();

                Leads leads = new Leads(category, id, lead1, lead2, lead3, lead4, lead5, lead6, lead7, lead8, lead9, lead10, lead11, lead12);

                databaseLeads.child(id).setValue(leads);

                Toast.makeText(this, "Successful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Please Categorize First!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "You should filled all", Toast.LENGTH_LONG).show();
        }
    }

    private void run1() {
        try {
            tflite = new Interpreter(loadModelFile(), tfliteOptions);
            labelList = loadLabelList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    // loads tflite grapg from file
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd(chosen);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


    // converts bitmap to byte array which is passed in the tflite graph
    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // loop through all pixels
        int pixel = 0;
        for (int i = 0; i < DIM_IMG_SIZE_X; ++i) {
            for (int j = 0; j < DIM_IMG_SIZE_Y; ++j) {
                final int val = intValues[pixel++];
                // get rgb values from intValues where each int holds the rgb values for a pixel.
                // if quantized, convert each rgb value to a byte, otherwise to a float
                if (quant) {
                    imgData.put((byte) ((val >> 16) & 0xFF));
                    imgData.put((byte) ((val >> 8) & 0xFF));
                    imgData.put((byte) (val & 0xFF));
                } else {
                    imgData.putFloat((((val >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                    imgData.putFloat((((val >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                    imgData.putFloat((((val) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                }
            }
        }
    }

    // loads the labels from the label txt file in assets into a string array
    private List<String> loadLabelList() throws IOException {
        List<String> labelList = new ArrayList<String>();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(this.getAssets().open("ecg.txt")));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

    // print the top labels and respective confidences
    public void printTopKLabels() {
        // add all results to priority queue
        for (int i = 0; i < labelList.size(); ++i) {
            if (quant) {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), (labelProbArrayB[0][i] & 0xff) / 255.0f));
            } else {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            }
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }

        // get top results from priority queue
        final int size = sortedLabels.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Float> label = sortedLabels.poll();
            topLables[i] = label.getKey();
            topConfidence[i] = String.format("%.0f%%", label.getValue() * 100);
        }
        // set the corresponding textviews with the results
        label1.setText(topLables[2]);
    }

    public void printTopKLabels2() {
        // add all results to priority queue
        for (int i = 0; i < labelList.size(); ++i) {
            if (quant) {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), (labelProbArrayB[0][i] & 0xff) / 255.0f));
            } else {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            }
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        // get top results from priority queue
        final int size = sortedLabels.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Float> label = sortedLabels.poll();
            topLables2[i] = label.getKey();
            topConfidence2[i] = String.format("%.0f%%", label.getValue() * 100);
        }
        // set the corresponding textviews with the results
        label2.setText(topLables2[2]);

    }

    public void printTopKLabels3() {
        // add all results to priority queue
        for (int i = 0; i < labelList.size(); ++i) {
            if (quant) {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), (labelProbArrayB[0][i] & 0xff) / 255.0f));
            } else {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            }
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        // get top results from priority queue
        final int size = sortedLabels.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Float> label = sortedLabels.poll();
            topLables3[i] = label.getKey();
            topConfidence3[i] = String.format("%.0f%%", label.getValue() * 100);
        }
        // set the corresponding textviews with the results
        label3.setText(topLables3[2]);

    }

    public void printTopKLabels4() {
        // add all results to priority queue
        for (int i = 0; i < labelList.size(); ++i) {
            if (quant) {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), (labelProbArrayB[0][i] & 0xff) / 255.0f));
            } else {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            }
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        // get top results from priority queue
        final int size = sortedLabels.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Float> label = sortedLabels.poll();
            topLables4[i] = label.getKey();
            topConfidence4[i] = String.format("%.0f%%", label.getValue() * 100);
        }
        // set the corresponding textviews with the results
        label4.setText(topLables4[2]);
    }

    public void printTopKLabels5() {
        // add all results to priority queue
        for (int i = 0; i < labelList.size(); ++i) {
            if (quant) {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), (labelProbArrayB[0][i] & 0xff) / 255.0f));
            } else {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            }
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        // get top results from priority queue
        final int size = sortedLabels.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Float> label3 = sortedLabels.poll();
            topLables5[i] = label3.getKey();
            topConfidence5[i] = String.format("%.0f%%", label3.getValue() * 100);
        }
        // set the corresponding textviews with the results
        label5.setText(topLables5[2]);

    }

    public void printTopKLabels6() {
        // add all results to priority queue
        for (int i = 0; i < labelList.size(); ++i) {
            if (quant) {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), (labelProbArrayB[0][i] & 0xff) / 255.0f));
            } else {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            }
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        // get top results from priority queue
        final int size = sortedLabels.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Float> label3 = sortedLabels.poll();
            topLables6[i] = label3.getKey();
            topConfidence6[i] = String.format("%.0f%%", label3.getValue() * 100);
        }
        // set the corresponding textviews with the results
        label6.setText(topLables6[2]);

    }

    public void printTopKLabels7() {
        // add all results to priority queue
        for (int i = 0; i < labelList.size(); ++i) {
            if (quant) {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), (labelProbArrayB[0][i] & 0xff) / 255.0f));
            } else {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            }
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        // get top results from priority queue
        final int size = sortedLabels.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Float> label3 = sortedLabels.poll();
            topLables7[i] = label3.getKey();
            topConfidence7[i] = String.format("%.0f%%", label3.getValue() * 100);
        }
        // set the corresponding textviews with the results
        label7.setText(topLables7[2]);
    }

    public void printTopKLabels8() {
        // add all results to priority queue
        for (int i = 0; i < labelList.size(); ++i) {
            if (quant) {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), (labelProbArrayB[0][i] & 0xff) / 255.0f));
            } else {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            }
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        // get top results from priority queue
        final int size = sortedLabels.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Float> label3 = sortedLabels.poll();
            topLables8[i] = label3.getKey();
            topConfidence8[i] = String.format("%.0f%%", label3.getValue() * 100);
        }
        // set the corresponding textviews with the results
        label8.setText(topLables8[2]);
    }

    public void printTopKLabels9() {
        // add all results to priority queue
        for (int i = 0; i < labelList.size(); ++i) {
            if (quant) {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), (labelProbArrayB[0][i] & 0xff) / 255.0f));
            } else {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            }
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        // get top results from priority queue
        final int size = sortedLabels.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Float> label3 = sortedLabels.poll();
            topLables9[i] = label3.getKey();
            topConfidence9[i] = String.format("%.0f%%", label3.getValue() * 100);
        }
        // set the corresponding textviews with the results
        label9.setText(topLables9[2]);
    }

    public void printTopKLabels10() {
        // add all results to priority queue
        for (int i = 0; i < labelList.size(); ++i) {
            if (quant) {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), (labelProbArrayB[0][i] & 0xff) / 255.0f));
            } else {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            }
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        // get top results from priority queue
        final int size = sortedLabels.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Float> label3 = sortedLabels.poll();
            topLables10[i] = label3.getKey();
            topConfidence10[i] = String.format("%.0f%%", label3.getValue() * 100);
        }
        // set the corresponding textviews with the results
        label10.setText(topLables10[2]);
    }

    public void printTopKLabels11() {
        // add all results to priority queue
        for (int i = 0; i < labelList.size(); ++i) {
            if (quant) {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), (labelProbArrayB[0][i] & 0xff) / 255.0f));
            } else {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            }
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        // get top results from priority queue
        final int size = sortedLabels.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Float> label3 = sortedLabels.poll();
            topLables11[i] = label3.getKey();
            topConfidence11[i] = String.format("%.0f%%", label3.getValue() * 100);
        }
        // set the corresponding textviews with the results
        label11.setText(topLables11[2]);

    }

    public void printTopKLabels12() {
        // add all results to priority queue
        for (int i = 0; i < labelList.size(); ++i) {
            if (quant) {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), (labelProbArrayB[0][i] & 0xff) / 255.0f));
            } else {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            }
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        // get top results from priority queue
        final int size = sortedLabels.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Float> label3 = sortedLabels.poll();
            topLables12[i] = label3.getKey();
            topConfidence12[i] = String.format("%.0f%%", label3.getValue() * 100);
        }
        // set the corresponding textviews with the results
        label12.setText(topLables12[2]);

    }

    // resizes bitmap to given dimensions
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
}
