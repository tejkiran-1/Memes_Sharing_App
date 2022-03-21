package com.example.memessharingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Visibility;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    //TextView textView;
    Button nextMeme, shareMeme;
    ImageView memeImage;
    ProgressBar progressBar;
    String currImageURL = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextMeme = findViewById(R.id.nextMeme);
        shareMeme = findViewById(R.id.share);
        memeImage = findViewById(R.id.memeImage);
        progressBar = findViewById(R.id.progressBar);
        //textView = findViewById(R.id.textView);

        loadMeme();



        nextMeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                loadMeme();
            }
        });

        shareMeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra("message", "Hey! Check out this cool meme: ".concat(currImageURL));
                Intent chooserTarget = Intent.createChooser(intent, "Share this meme using");
                startActivity(chooserTarget);
            }
        });
    }


    private void loadMeme() {
        //RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://meme-api.herokuapp.com/gimme";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            currImageURL = response.getString("url");
                            Glide.with(MainActivity.this).load(currImageURL).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    //Toast.makeText(MainActivity.this, "Error in loading the meme. Click next!", Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            }).into(memeImage);
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "Error in loading the meme. Click next!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error in loading the meme. Click next!", Toast.LENGTH_SHORT).show();
                    }
            });

        // Add the request to the RequestQueue.
        //queue.add(jsonObjectRequest);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

}