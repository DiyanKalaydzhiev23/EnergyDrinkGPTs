package com.example.enrgydrinksgpts.utils.cloudinary;

import com.cloudinary.android.MediaManager;

public class CloudinaryMethods {
    public void uploadImage() {
        String requestId = MediaManager.get().upload("imageFile.jpg").dispatch();
    }
}
