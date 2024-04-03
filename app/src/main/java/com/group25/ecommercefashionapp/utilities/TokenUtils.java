package com.group25.ecommercefashionapp.utilities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;


public class TokenUtils {
    public static boolean isTokenExpired(String token) {
        try {
            // Split the token into its three parts: header, payload, signature
            String[] parts = token.split("\\.");

            // Decode the payload
            byte[] payloadBytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
            }
            String payload = new String(payloadBytes);

            // Parse the payload as JSON
            JSONObject jsonObject = new JSONObject(payload);

            // Get the expiration time (exp) from the payload
            long expirationTime = jsonObject.getLong("exp");

            // Get the current time
            long currentTime = System.currentTimeMillis() / 1000;

            // Check if the token is expired
            Log.d("JWTUtils", "Current time: " + currentTime);
            Log.d("JWTUtils", "Expiration time: " + expirationTime);
            return currentTime >= expirationTime;
        } catch (JSONException e) {
            Log.e("JWTUtils", "Error parsing JWT payload: " + e.getMessage());
            return true; // Consider token as expired in case of parsing error
        }
    }

    public static String bearerToken(String token) {
        return "Bearer " + token;
    }
}
