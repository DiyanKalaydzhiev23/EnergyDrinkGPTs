package com.example.enrgydrinksgpts.utils.imageCompare;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageCompareUtil {
    private final ArrayList<String> imageUrls = new ArrayList<>();

    public interface BestMatchCallback {
        void onBestMatchResult(String bestMatchUrl, double percentDifference);
        void onError(String error);
    }

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public ImageCompareUtil() {
        populateImageUrls();
    }

    private void populateImageUrls() {
        imageUrls.add("https://res.cloudinary.com/dhqp5qtsw/image/upload/v1715160882/classic_red_bull_mvhvlb.png");
        imageUrls.add("https://res.cloudinary.com/dhqp5qtsw/image/upload/v1715161761/red_red_bull_rpnoht.png");
        imageUrls.add("https://res.cloudinary.com/dhqp5qtsw/image/upload/v1715616756/sugar_free_red_bull_ecmxth.png");
    }

    public void findBestMatch(String targetImageUrl, BestMatchCallback callback) {
        executorService.execute(() -> {
            String bestMatchUrl = null;
            double lowestPercentDifference = Double.MAX_VALUE;

            for (String compareUrl : imageUrls) {
                try {
                    URL url = new URL("https://image-compare.hcti.io/api?image_url=" + targetImageUrl +
                            "&other_image_url=" + compareUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        double percentDifference = jsonResponse.getDouble("percent_difference");
                        System.out.println("The percent diff is " + percentDifference);

                        if (percentDifference < lowestPercentDifference) {
                            lowestPercentDifference = percentDifference;
                            bestMatchUrl = compareUrl;
                        }
                    } else {
                        postError("HTTP error response: " + connection.getResponseMessage(), callback);
                        return;
                    }
                } catch (Exception e) {
                    postError("Network error: " + e.getMessage(), callback);
                    return;
                }
            }

            postBestMatchResult(bestMatchUrl, lowestPercentDifference, callback);
        });
    }

    private static void postBestMatchResult(String bestMatchUrl, double percentDifference, BestMatchCallback callback) {
        handler.post(() -> callback.onBestMatchResult(bestMatchUrl, percentDifference));
    }

    private static void postError(String error, BestMatchCallback callback) {
        handler.post(() -> callback.onError(error));
    }
}
