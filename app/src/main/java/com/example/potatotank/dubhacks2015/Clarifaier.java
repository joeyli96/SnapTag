package com.example.potatotank.dubhacks2015;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.clarifai.api.exception.ClarifaiException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Joey on 10/18/15.
 */
public class Clarifaier {
    private static final String TAG = RecognitionActivity.class.getSimpleName();

    // IMPORTANT NOTE: you should replace these keys with your own App ID and secret.
    // These can be obtained at https://developer.clarifai.com/applications
    private static final String APP_ID = "ZPMfoZBIL_ZnTLucxgGwUnbHUqpvIHrupj74-SJT";
    private static final String APP_SECRET = "yOFi72C4h4l9lbrF7aGI3YB8MlDWAmIYMoDArENz";

    private static final int CODE_PICK = 1;

    private final ClarifaiClient client = new ClarifaiClient(APP_ID, APP_SECRET);
    private String filepath;

    public Clarifaier(String filepath) {
        this.filepath = filepath;
    }

    public String[] getTags(){
        Bitmap bitmap = BitmapFactory.decodeFile(filepath);
        String[] tags = new String[3];
        // Run recognition on a background thread since it makes a network call.
        RecognitionResult result = null;
        AsyncTask task = new AsyncTask<Bitmap, Void, RecognitionResult>() {
            @Override
            protected RecognitionResult doInBackground(Bitmap... bitmaps) {
                return recognizeBitmap(bitmaps[0]);
            }
        }.execute(bitmap);
        try {
            result = (RecognitionResult) task.get();
        } catch (ExecutionException ex) {
        } catch (InterruptedException ex) {
        }
        ;
        if (result != null) {
            if (result.getStatusCode() == RecognitionResult.StatusCode.OK) {
                // Display the list of tags in the UI.
                StringBuilder b = new StringBuilder();
                int count = 0;

                for (Tag tag : result.getTags()) {
                    if (count >= 3){
                        break;
                    }
                    tags[count++] = tag.getName();
                }

            }

        }
        return tags;
    }

    /**
     * Sends the given bitmap to Clarifai for recognition and returns the result.
     */
    private RecognitionResult recognizeBitmap(Bitmap bitmap) {
        try {
            // Scale down the image. This step is optional. However, sending large images over the
            // network is slow and  does not significantly improve recognition performance.
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 320,
                    320 * bitmap.getHeight() / bitmap.getWidth(), true);

            // Compress the image as a JPEG.
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            scaled.compress(Bitmap.CompressFormat.JPEG, 90, out);
            byte[] jpeg = out.toByteArray();

            // Send the JPEG to Clarifai and return the result.
            return client.recognize(new RecognitionRequest(jpeg)).get(0);
        } catch (ClarifaiException e) {
            Log.e(TAG, "Clarifai error", e);
            return null;
        }
    }
}
