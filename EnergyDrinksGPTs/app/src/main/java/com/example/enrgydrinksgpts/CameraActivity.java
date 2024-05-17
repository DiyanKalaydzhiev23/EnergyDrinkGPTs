package com.example.enrgydrinksgpts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.enrgydrinksgpts.energyDrinksCards.CongratsView;
import com.example.enrgydrinksgpts.utils.cloudinary.CloudinaryMethods;
import com.example.enrgydrinksgpts.utils.imageCompare.ImageCompareUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity implements CloudinaryMethods.UploadListener, ImageCompareUtil.BestMatchCallback {

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openCamera();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (Exception ex) {
                Log.e("CameraActivity", "Error occurred while creating the file", ex);
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.enrgydrinksgpts.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startForResult.launch(takePictureIntent);
            }
        }
    }

    private final ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        CloudinaryMethods cloudinaryMethods = new CloudinaryMethods();
                        cloudinaryMethods.uploadImage(currentPhotoPath, CameraActivity.this);
                    }
                }
            });

    private File createImageFile() throws Exception {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    @Override
    public void onUploadSuccess(String imageUrl) {
        Log.d("Upload Success", "Image URL: " + imageUrl);
        ImageCompareUtil imageCompareUtil = new ImageCompareUtil();
        imageCompareUtil.findBestMatch(imageUrl, this);
    }

    @Override
    public void onBestMatchResult(String bestMatchUrl, double percentDifference) {
        Intent intent = new Intent(CameraActivity.this, CongratsView.class);
        intent.putExtra("bestMatchUrl", bestMatchUrl);
        intent.putExtra("percentDifference", percentDifference);
        startActivity(intent);
    }

    @Override
    public void onError(String error) {
        Log.e("Comparison Error", error);
        Intent intent = new Intent(CameraActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUploadError(String error) {
        Log.e("Upload Error", error);
        Intent intent = new Intent(CameraActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
