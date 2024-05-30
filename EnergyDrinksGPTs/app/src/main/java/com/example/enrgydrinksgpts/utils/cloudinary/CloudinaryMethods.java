package com.example.enrgydrinksgpts.utils.cloudinary;

import android.content.Context;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.Map;

public class CloudinaryMethods {
    private static boolean initialized = false;

    public interface UploadListener {
        void onUploadSuccess(String imageUrl);

        void onUploadError(String error);
    }

    public static void initialize(Context context, Map<String, String> config) {
        if (!initialized) {
            MediaManager.init(context, config);
            initialized = true;
        }
    }

    public static boolean hasBeenInitialized() {
        return initialized;
    }

    public void uploadImage(String filePath, UploadListener listener) {
        MediaManager.get().upload(filePath)
                .callback(new UploadCallback() {
                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String url = (String) resultData.get("url");
                        listener.onUploadSuccess(url);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        listener.onUploadError(error.getDescription());
                    }

                    @Override
                    public void onStart(String requestId) {

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        listener.onUploadError("Rescheduled due to: " + error.getDescription());
                    }
                }).dispatch();
    }
}
