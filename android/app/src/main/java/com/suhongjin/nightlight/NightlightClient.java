package com.suhongjin.nightlight;

import android.support.annotation.VisibleForTesting;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Client that sends request to the server.
 */

public class NightlightClient {

    private static NightlightClient instance;

    private static final String TAG = NightlightClient.class.getSimpleName();
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;

    public static synchronized NightlightClient getInstance() {
        if (instance == null) {
            instance = new NightlightClient();
        }
        return instance;
    }

    private NightlightClient() {
        client = new OkHttpClient();
    }

    public void sendColorChangeRequest(
            final int r,
            final int g,
            final int b,
            final Callback callback) throws IOException, JSONException {
        Log.d(TAG, "Calling send color change request.");

        RequestBody body = RequestBody.create(JSON, createColorChangeParams(r, g, b));
        Request request = new Request.Builder().url(Utils.SERVER_URL + Utils.UPDATE_COLOR_HANDLER)
                .post(body).build();
        client.newCall(request).enqueue(callback);
    }

    @VisibleForTesting
    String createColorChangeParams(
            final int r,
            final int g,
            final int b) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(Utils.POST_RED_PARAM, r);
        json.put(Utils.POST_GREEN_PARAM, g);
        json.put(Utils.POST_BLUE_PARAM, b);

        return json.toString();
    }

    public void getNightlightColor(Callback callback) {
        Log.d(TAG, "Requesting nightlight color.");

        Request request = new Request.Builder().url(Utils.SERVER_URL + Utils.GET_COLOR_HANDLER)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void getNightlightPowerState(Callback callback) {
        Log.d(TAG, "Requesting nightlight power state.");

        Request request = new Request.Builder().url(Utils.SERVER_URL + Utils.IS_POWER_ON_HANDLER)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void sendPowerFlipRequest(Callback callback) {
        RequestBody body = RequestBody.create(null, new byte[0]);
        Request request = new Request.Builder().url(Utils.SERVER_URL + Utils.FLIP_POWER_HANDLER)
                .post(body).build();
        client.newCall(request).enqueue(callback);
    }
}
