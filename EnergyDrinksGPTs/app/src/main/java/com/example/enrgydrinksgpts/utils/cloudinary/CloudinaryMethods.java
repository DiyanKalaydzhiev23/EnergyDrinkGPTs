package com.example.enrgydrinksgpts.utils.cloudinary;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.Map;

public class CloudinaryMethods {

    public interface UploadListener {
        void onUploadSuccess(String imageUrl);
        void onUploadError(String error);
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
