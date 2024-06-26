package com.example.enrgydrinksgpts.utils.camera;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageCompareUtil {
    public static final HashMap<String, String> imageUrls = new HashMap<>();

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
        imageUrls.put("https://res.cloudinary.com/dhqp5qtsw/image/upload/v1715160882/classic_red_bull_mvhvlb.png", "Classic Red Bull");
        imageUrls.put("https://res.cloudinary.com/dhqp5qtsw/image/upload/v1717073220/psxlrxdkp8doefvgxzjm.jpg", "Watermelon Red Bull");
        imageUrls.put("https://res.cloudinary.com/dhqp5qtsw/image/upload/v1715616756/sugar_free_red_bull_ecmxth.png", "Sugar Free Red Bull");
        imageUrls.put("https://res.cloudinary.com/dhqp5qtsw/image/upload/v1717073793/jenuqfoeef8zirna8ebl.png", "Tropical Red Bull");
    }

    public void findBestMatch(String targetImageUrl, BestMatchCallback callback) {
        executorService.execute(() -> {
            String bestMatchUrl = null;
            double biggestPercentDiff = Double.MIN_VALUE;

            for (Map.Entry<String, String> entry : imageUrls.entrySet()) {
                String compareUrl = entry.getKey();

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
                        System.out.println("The percent diff is2 " + percentDifference + " " + compareUrl);

                        if (percentDifference > biggestPercentDiff) {
                            biggestPercentDiff = percentDifference;
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

            postBestMatchResult(bestMatchUrl, biggestPercentDiff, callback);
        });
    }

    private static void postBestMatchResult(String bestMatchUrl, double percentDifference, BestMatchCallback callback) {
        handler.post(() -> callback.onBestMatchResult(bestMatchUrl, percentDifference));
    }

    private static void postError(String error, BestMatchCallback callback) {
        handler.post(() -> callback.onError(error));
    }
}