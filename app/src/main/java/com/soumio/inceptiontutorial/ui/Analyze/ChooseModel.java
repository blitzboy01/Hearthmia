package com.soumio.inceptiontutorial.ui.Analyze;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.soumio.inceptiontutorial.R;
import com.soumio.inceptiontutorial.ui.Classify.Classify_L2;
import com.soumio.inceptiontutorial.ui.Navigation.ActivityUser;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;

public class ChooseModel extends AppCompatActivity implements View.OnClickListener {

    // for permission requests
    public static final int REQUEST_PERMISSION = 300;
    // request code for permission requests to the os for image
    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_IMAGE2 = 200;
    public static final int REQUEST_IMAGE3 = 300;
    public static final int REQUEST_IMAGE4 = 400;
    public static final int REQUEST_IMAGE5 = 500;
    public static final int REQUEST_IMAGE6 = 600;
    public static final int REQUEST_IMAGE7 = 700;
    public static final int REQUEST_IMAGE8 = 800;
    public static final int REQUEST_IMAGE9 = 900;
    public static final int REQUEST_IMAGE10 = 1000;
    public static final int REQUEST_IMAGE11 = 1100;
    public static final int REQUEST_IMAGE12 = 1200;

    private static final int RC_CROP = 100;
    private static final int RC_CROP1 = 200;
    // will hold uri of image obtained from camera
    public Uri imageUri, imageUri2, imageUri3, imageUri4, imageUri5, imageUri6,
            imageUri7, imageUri8, imageUri9, imageUri10, imageUri11, imageUri12,
            dest_1, dest_2, dest_3, dest_4, dest_5, dest_6,
            dest_7, dest_8, dest_9, dest_10, dest_11, dest_12;
    //database store
    StorageReference mStorageRef;
    private ChooseViewModel chooseViewModel;
    // button for each available classifier
    private Button buttonLead1, buttonLead2, buttonLead3, buttonAvR, buttonAvL,
            buttonAvF, buttonV1, buttonV2, buttonV3, buttonV4, buttonV5, buttonV6;
    private Button inceptionFloat2, inceptionFloat3;
    //        private Button inceptionFloat3;
    private Button Upload_Button;
    private Button inceptionQuant;
    private ImageView imahe, imageViewLead1, imageViewLead2, imageViewLead3, imageViewAvR,
            imageViewAvL, imageViewAvF, imageViewV1, imageViewV2, imageViewV3,
            imageViewV4, imageViewV5, imageViewV6, back_img;
    // string to send to next activity that describes the chosen classifier
    private String chosen, chosen2, chosen3, chosen4, chosen5, chosen6, chosen7, chosen8, chosen9, chosen10, chosen11, chosen12;


    private Bitmap bitmap1, bitmap2, bitmap3, bitmap4, bitmap5, bitmap6,
            bitmap7, bitmap8, bitmap9, bitmap10, bitmap11, bitmap12;


    //boolean value dictating if chosen model is quantized version or not.
    private boolean quant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_model);

        chooseViewModel = ViewModelProviders.of(this).get(ChooseViewModel.class);

        //storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");

        // request permission to use the camera on the user's phone
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION);
        }

        // request permission to write data (aka images) to the user's external storage of their phone
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }

        // request permission to read data (aka images) from the user's external storage of their phone
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }

        // on click for inception float model
        buttonLead1 = this.findViewById(R.id.lead_1_button);
        buttonLead2 = this.findViewById(R.id.lead_2_button);
        buttonLead3 = this.findViewById(R.id.lead_3_button);

        buttonAvR = this.findViewById(R.id.lead_AvR_button);
        buttonAvL = this.findViewById(R.id.lead_AvL_button);
        buttonAvF = this.findViewById(R.id.lead_AvF_button);

        buttonV1 = this.findViewById(R.id.lead_V1_button);
        buttonV2 = this.findViewById(R.id.lead_V2_button);
        buttonV3 = this.findViewById(R.id.lead_V3_button);
        buttonV4 = this.findViewById(R.id.lead_V4_button);
        buttonV5 = this.findViewById(R.id.lead_V5_button);
        buttonV6 = this.findViewById(R.id.lead_V6_button);

        imageViewLead1 = findViewById(R.id.lead_1_image_view);
        imageViewLead2 = findViewById(R.id.lead_2_image_view);
        imageViewLead3 = findViewById(R.id.lead_3_image_view);

        imageViewAvR = findViewById(R.id.AvR_image_view);
        imageViewAvL = findViewById(R.id.AvL_image_view);
        imageViewAvF = findViewById(R.id.AvF_image_view);

        imageViewV1 = findViewById(R.id.V1_image_view);
        imageViewV2 = findViewById(R.id.V2_image_view);
        imageViewV3 = findViewById(R.id.V3_image_view);
        imageViewV4 = findViewById(R.id.V4_image_view);
        imageViewV5 = findViewById(R.id.V5_image_view);
        imageViewV6 = findViewById(R.id.V6_image_view);

        back_img = findViewById(R.id.Mainback);

        inceptionFloat2 = findViewById(R.id.inception_float2);

        Upload_Button = findViewById(R.id.Upload);


        buttonLead1.setOnClickListener(this);
        buttonLead2.setOnClickListener(this);
        buttonLead3.setOnClickListener(this);
        buttonAvL.setOnClickListener(this);
        buttonAvF.setOnClickListener(this);
        buttonAvR.setOnClickListener(this);
        buttonV1.setOnClickListener(this);
        buttonV2.setOnClickListener(this);
        buttonV3.setOnClickListener(this);
        buttonV4.setOnClickListener(this);
        buttonV5.setOnClickListener(this);
        buttonV6.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case (R.id.lead_1_button):
                // filename in assets
                chosen = "cnn_12.tflite";
                // model in not quantized
                quant = false;
                // open camera
                OpenCameraIntent1();
                break;
            case (R.id.lead_2_button):
                // filename in assets
                chosen2 = "cnn_12.tflite";
                // model in not quantized
                quant = false;
                // open camera
                OpenCameraIntent2();
                break;
            case (R.id.lead_3_button):
                // filename in assets
                chosen3 = "cnn_12.tflite";
                // model in not quantized
                quant = false;
                // open camera
                OpenCameraIntent3();
                break;
            case (R.id.lead_AvR_button):
                // filename in assets
                chosen4 = "cnn_12.tflite";
                // model in not quantized
                quant = false;
                // open camera
                OpenCameraIntent4();
                break;
            case (R.id.lead_AvL_button):
                // filename in assets
                chosen5 = "cnn_12.tflite";
                // model in not quantized
                quant = false;
                // open camera
                OpenCameraIntent5();
                break;
            case (R.id.lead_AvF_button):
                // filename in assets
                chosen = "cnn_12.tflite";
                // model in not quantized
                quant = false;
                // open camera
                OpenCameraIntent6();
                break;
            case (R.id.lead_V1_button):
                // filename in assets
                chosen = "cnn_12.tflite";
                // model in not quantized
                quant = false;
                // open camera
                OpenCameraIntent7();
                break;
            case (R.id.lead_V2_button):
                // filename in assets
                chosen = "cnn_12.tflite";
                // model in not quantized
                quant = false;
                // open camera
                OpenCameraIntent8();
                break;
            case (R.id.lead_V3_button):
                // filename in assets
                chosen = "cnn_12.tflite";
                // model in not quantized
                quant = false;
                // open camera
                OpenCameraIntent9();
                break;
            case (R.id.lead_V4_button):
                // filename in assets
                chosen = "cnn_12.tflite";
                // model in not quantized
                quant = false;
                // open camera
                OpenCameraIntent10();
                break;
            case (R.id.lead_V5_button):
                // filename in assets
                chosen = "cnn_12.tflite";
                // model in not quantized
                quant = false;
                // open camera
                OpenCameraIntent11();
                break;
            case (R.id.lead_V6_button):
                // filename in assets
                chosen = "cnn_12.tflite";
                // model in not quantized
                quant = false;
                // open camera
                OpenCameraIntent12();
                break;
            default:
                Toast.makeText(this, "Cameras are not working, please try again!", Toast.LENGTH_SHORT).show();

        }
        back_img = findViewById(R.id.Mainback);
        back_img.setOnClickListener(view -> {
            Intent login = new Intent(this, ActivityUser.class);
            startActivity(login);

        });


    }


    // opens camera for user
    private void OpenCameraIntent1() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        // tell camera where to store the resulting picture
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // start camera, and wait for it to finish
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    // opens camera for user
    private void OpenCameraIntent2() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture2");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera2");
        // tell camera where to store the resulting picture
        imageUri2 = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri2);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // start camera, and wait for it to finish
        startActivityForResult(intent, REQUEST_IMAGE2);
    }

    private void OpenCameraIntent3() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture3");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera3");
        // tell camera where to store the resulting picture
        imageUri3 = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri3);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // start camera, and wait for it to finish
        startActivityForResult(intent, REQUEST_IMAGE3);
    }

    private void OpenCameraIntent4() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture4");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera4");
        // tell camera where to store the resulting picture
        imageUri4 = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri4);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // start camera, and wait for it to finish
        startActivityForResult(intent, REQUEST_IMAGE4);
    }

    private void OpenCameraIntent5() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture5");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera5");
        // tell camera where to store the resulting picture
        imageUri5 = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri5);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // start camera, and wait for it to finish
        startActivityForResult(intent, REQUEST_IMAGE5);
    }

    private void OpenCameraIntent6() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture6");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera6");
        // tell camera where to store the resulting picture
        imageUri6 = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri6);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // start camera, and wait for it to finish
        startActivityForResult(intent, REQUEST_IMAGE6);
    }

    // opens camera for user
    private void OpenCameraIntent7() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture7");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera7");
        // tell camera where to store the resulting picture
        imageUri7 = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri7);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // start camera, and wait for it to finish
        startActivityForResult(intent, REQUEST_IMAGE7);
    }

    // opens camera for user
    private void OpenCameraIntent8() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture8");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera8");
        // tell camera where to store the resulting picture
        imageUri8 = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri8);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // start camera, and wait for it to finish
        startActivityForResult(intent, REQUEST_IMAGE8);
    }

    private void OpenCameraIntent9() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture9");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera9");
        // tell camera where to store the resulting picture
        imageUri9 = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri9);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // start camera, and wait for it to finish
        startActivityForResult(intent, REQUEST_IMAGE9);
    }

    private void OpenCameraIntent10() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture10");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera10");
        // tell camera where to store the resulting picture
        imageUri10 = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri10);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // start camera, and wait for it to finish
        startActivityForResult(intent, REQUEST_IMAGE10);
    }

    private void OpenCameraIntent11() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture11");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera11");
        // tell camera where to store the resulting picture
        imageUri11 = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri11);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // start camera, and wait for it to finish
        startActivityForResult(intent, REQUEST_IMAGE11);
    }

    private void OpenCameraIntent12() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture12");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera12");
        // tell camera where to store the resulting picture
        imageUri12 = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri12);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // start camera, and wait for it to finish
        startActivityForResult(intent, REQUEST_IMAGE12);
    }

  /*  private String getExtension(Uri imageUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(imageUri));
    }

    private void FileUploader() {
        StorageReference Ref = mStorageRef.child(System.currentTimeMillis() + "." + "jpeg");

        Ref.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get a URL to the uploaded content
                    Toast.makeText(getApplicationContext(), "Image Database Successful!", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(exception -> {
                    // Handle unsuccessful uploads
                    // ...
                    Toast.makeText(getApplicationContext(), "Image Upload Unsuccessful", Toast.LENGTH_LONG).show();
                });
    }*/

    // checks that the user has allowed all the required permission of read and write and camera. If not, notify the user and close the application
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(), "This application needs read, write, and camera permissions to run. Application now closing.", Toast.LENGTH_LONG).show();
                System.exit(0);
            }
        }
    }

    // dictates what to do after the user takes an image, selects and image, or crops an image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the camera activity is finished, obtained the uri, crop it to make it square, and send it to 'Classify' activity
        if (requestCode == REQUEST_IMAGE) {
            try {
                Uri source_uri = imageUri;
                dest_1 = Uri.fromFile(new File(getCacheDir(), "cropped"));
                Crop.of(source_uri, dest_1)
                        .withMaxSize(612, 229)
                        .start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE2) {
            try {
                Uri source_uri = imageUri2;
                dest_2 = Uri.fromFile(new File(getCacheDir(), "cropped2"));
                Crop.of(source_uri, dest_2)
                        .withMaxSize(612, 229)
                        .start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE3) {
            try {
                Uri source_uri = imageUri3;
                dest_3 = Uri.fromFile(new File(getCacheDir(), "cropped3"));
                Crop.of(source_uri, dest_3)
                        .withMaxSize(612, 229)
                        .start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE4) {
            try {
                Uri source_uri = imageUri4;
                dest_4 = Uri.fromFile(new File(getCacheDir(), "cropped4"));
                Crop.of(source_uri, dest_4)
                        .withMaxSize(612, 229)
                        .start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE5) {
            try {
                Uri source_uri = imageUri5;
                dest_5 = Uri.fromFile(new File(getCacheDir(), "cropped5"));
                Crop.of(source_uri, dest_5)
                        .withMaxSize(612, 229)
                        .start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE6) {
            try {
                Uri source_uri = imageUri6;
                dest_6 = Uri.fromFile(new File(getCacheDir(), "cropped6"));
                Crop.of(source_uri, dest_6)
                        .withMaxSize(612, 229)
                        .start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE7) {

            try {
                Uri source_uri = imageUri7;
                dest_7 = Uri.fromFile(new File(getCacheDir(), "cropped7"));
                Crop.of(source_uri, dest_7)
                        .withMaxSize(612, 229)
                        .start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE8) {
            try {
                Uri source_uri = imageUri8;
                dest_8 = Uri.fromFile(new File(getCacheDir(), "cropped8"));
                Crop.of(source_uri, dest_8)
                        .withMaxSize(612, 229)
                        .start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE9) {
            try {
                Uri source_uri = imageUri9;
                dest_9 = Uri.fromFile(new File(getCacheDir(), "cropped9"));
                Crop.of(source_uri, dest_9)
                        .withMaxSize(612, 229)
                        .start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE10) {

            try {
                Uri source_uri = imageUri10;
                dest_10 = Uri.fromFile(new File(getCacheDir(), "cropped10"));
                Crop.of(source_uri, dest_10)
                        .withMaxSize(612, 229)
                        .start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE11) {
            try {
                Uri source_uri = imageUri11;
                dest_11 = Uri.fromFile(new File(getCacheDir(), "cropped11"));
                Crop.of(source_uri, dest_11)
                        .withMaxSize(612, 229)
                        .start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE12) {
            try {
                Uri source_uri = imageUri12;
                dest_12 = Uri.fromFile(new File(getCacheDir(), "cropped12"));
                Crop.of(source_uri, dest_12)
                        .withMaxSize(612, 229)
                        .start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                if (dest_1 != null) {

                    bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), dest_1);
//                Glide.with(this).load(dest_1).into(imageViewLead1);
                }
                if (dest_2 != null) {
                    bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), dest_2);
//                Glide.with(this).load(dest_2).into(imageViewLead2);
                }
                if (dest_3 != null) {
                    Glide.with(this).load(dest_3).into(imageViewLead3);
                    bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), dest_3);

                }
                if (dest_4 != null) {
//                    Glide.with(this).load(dest_4).into(imageViewAvR);
                    bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(), dest_4);

                }
                if (dest_5 != null) {
//                    Glide.with(this).load(dest_5).into(imageViewAvL);
                    bitmap5 = MediaStore.Images.Media.getBitmap(getContentResolver(), dest_5);

                }
                if (dest_6 != null) {
//                    Glide.with(this).load(dest_6).into(imageViewAvF);
                    bitmap6 = MediaStore.Images.Media.getBitmap(getContentResolver(), dest_6);

                }
                if (dest_7 != null) {
//                    Glide.with(this).load(dest_7).into(imageViewV1);
                    bitmap7 = MediaStore.Images.Media.getBitmap(getContentResolver(), dest_7);
                }
                if (dest_8 != null) {
//                    Glide.with(this).load(dest_8).into(imageViewV2);
                    bitmap8 = MediaStore.Images.Media.getBitmap(getContentResolver(), dest_8);

                }
                if (dest_9 != null) {
//                    Glide.with(this).load(dest_9).into(imageViewV3);
                    bitmap9 = MediaStore.Images.Media.getBitmap(getContentResolver(), dest_9);

                }
                if (dest_10 != null) {
//                    Glide.with(this).load(dest_10).into(imageViewV4);
                    bitmap10 = MediaStore.Images.Media.getBitmap(getContentResolver(), dest_10);

                }
                if (dest_11 != null) {
//                    Glide.with(this).load(dest_11).into(imageViewV5);
                    bitmap11 = MediaStore.Images.Media.getBitmap(getContentResolver(), dest_11);

                }
                if (dest_12 != null) {
//                    Glide.with(this).load(dest_12).into(imageViewV6);
                    bitmap12 = MediaStore.Images.Media.getBitmap(getContentResolver(), dest_12);

                }
            } catch (IOException ie) {
                ie.printStackTrace();
            }
            Glide.with(this).asBitmap().load(bitmap1).into(imageViewLead1);
            //            Glide.with(this).asBitmap().load(bitmap1).into(imageViewLead1);
            Glide.with(this).asBitmap().load(bitmap2).into(imageViewLead2);
            Glide.with(this).asBitmap().load(bitmap3).into(imageViewLead3);

            Glide.with(this).asBitmap().load(bitmap4).into(imageViewAvR);
            Glide.with(this).asBitmap().load(bitmap5).into(imageViewAvL);
            Glide.with(this).asBitmap().load(bitmap6).into(imageViewAvF);

            Glide.with(this).asBitmap().load(bitmap7).into(imageViewV1);
            Glide.with(this).asBitmap().load(bitmap8).into(imageViewV2);
            Glide.with(this).asBitmap().load(bitmap9).into(imageViewV3);
            Glide.with(this).asBitmap().load(bitmap10).into(imageViewV4);
            Glide.with(this).asBitmap().load(bitmap11).into(imageViewV5);
            Glide.with(this).asBitmap().load(bitmap12).into(imageViewV6);


            /*imageViewLead1.setRotation(imageViewLead1.getRotation() + 360);
            imageViewLead2.setImageBitmap(bitmap2);
            imageViewLead2.setRotation(imageViewLead2.getRotation() + 360);
            imageViewLead3.setImageBitmap(bitmap3);
            imageViewLead3.setRotation(imageViewLead3.getRotation() + 360);


            imageViewAvR.setImageBitmap(bitmap4);
            imageViewAvR.setRotation(imageViewAvR.getRotation() + 360);
            imageViewAvL.setImageBitmap(bitmap5);
            imageViewAvL.setRotation(imageViewAvL.getRotation() + 360);
            imageViewAvF.setImageBitmap(bitmap6);
            imageViewAvF.setRotation(imageViewAvF.getRotation() + 360);*/

          /*  imageViewV1.setImageBitmap(bitmap7);
            imageViewV1.setRotation(imageViewV1.getRotation() + 360);
            imageViewV2.setImageBitmap(bitmap8);
            imageViewV2.setRotation(imageViewV2.getRotation() + 360);
            imageViewV3.setImageBitmap(bitmap9);
            imageViewV3.setRotation(imageViewV3.getRotation() + 360);
            imageViewV4.setImageBitmap(bitmap10);
            imageViewV4.setRotation(imageViewV4.getRotation() + 360);
            imageViewV5.setImageBitmap(bitmap11);
            imageViewV5.setRotation(imageViewV5.getRotation() + 360);
            imageViewV6.setImageBitmap(bitmap12);
            imageViewV6.setRotation(imageViewV6.getRotation() + 360);*/

            inceptionFloat2.setOnClickListener(view -> {

                //sa Custom directory i-sinave
                Intent i = new Intent(ChooseModel.this, Classify_L2.class);
                // put image data in extras to send

                i.putExtra("resID_uri", dest_1);
                i.putExtra("resID_uri2", dest_2);
                i.putExtra("resID_uri3", dest_3);
                i.putExtra("resID_uri4", dest_4);
                i.putExtra("resID_uri5", dest_5);
                i.putExtra("resID_uri6", dest_6);
                i.putExtra("resID_uri7", dest_7);
                i.putExtra("resID_uri8", dest_8);
                i.putExtra("resID_uri9", dest_9);
                i.putExtra("resID_uri10", dest_10);
                i.putExtra("resID_uri11", dest_11);
                i.putExtra("resID_uri12", dest_12);

                // put filename in extras
                i.putExtra("chosen", chosen);
                //put filename in extras
                i.putExtra("quant", quant);

                // send other required data
                startActivity(i);

            });

        }
    }
}



