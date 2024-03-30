package com.example.tugasspecial;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HomeFragment extends Fragment {

    private ImageView imageView;
    private TextView label;
    private Button saveButton;
    private String url_txt;
    private String url_jpg;
    private ProgressBar loadingProgressBar;
    private TextView loadingTextView;
    private static final int MY_PERMISSIONS_REQUEST_MANAGE_EXTERNAL_STORAGE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        imageView = rootView.findViewById(R.id.imageView);
        label = rootView.findViewById(R.id.label);
        Button loadImageButton = rootView.findViewById(R.id.loadImageButton);
        saveButton = rootView.findViewById(R.id.saveButton);
        loadingProgressBar = rootView.findViewById(R.id.loadingProgressBar);
        loadingTextView = rootView.findViewById(R.id.loadingTextView);

        loadImageButton.setOnClickListener(v -> {
            imageView.setImageDrawable(null);
            fetchImage();
        });

        saveButton.setOnClickListener(v -> {
            checkAndRequestPermission();
            // Save data
            if(url_txt != null && url_jpg != null)
            {
                saveDataToFile(url_txt, "data.txt");
                saveDataToFile(url_jpg, "image_url.txt");
                Toast.makeText(requireContext(), "Data saved!", Toast.LENGTH_SHORT).show();
            }

            url_txt = null;
            url_jpg = null;
            saveButton.setVisibility(View.GONE);
        });


        return rootView;
    }

    private void fetchImage() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        loadingTextView.setVisibility(View.VISIBLE);

        String url = "https://dog.ceo/api/breeds/image/random";
        String error_msg = "Failed to fetch data!";
        String name_url = "https://api.namefake.com/english-united-states/random/";

        JsonObjectRequest name_req = new JsonObjectRequest(Request.Method.GET, name_url, null,
                response -> {
                    try {
                        url_txt = response.getString("name");
                        label.setText(url_txt);
                    } catch (Exception e) {
                        url_txt = null;
                        label.setText("name");
                    }
                },
                error -> {
                    label.setText("name");
                    url_txt = null;
                }

        );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        loadingProgressBar.setVisibility(View.GONE);
                        loadingTextView.setVisibility(View.GONE);
                        url_jpg = response.getString("message");
                        Picasso.get().load(url_jpg).into(imageView);
                        saveButton.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        loadingProgressBar.setVisibility(View.GONE);
                        loadingTextView.setVisibility(View.GONE);
                        url_jpg = null;
                        label.setText(error_msg);
                        saveButton.setVisibility(View.GONE);
                    }
                },
                error -> {
                    url_jpg = null;
                    label.setText(error_msg);
                    saveButton.setVisibility(View.GONE);
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(request);
        requestQueue.add(name_req);
    }

    private void saveDataToFile(String data, String fileName) {
        File directory = requireContext().getExternalFilesDir(null); // Use app's external files directory
        File file = new File(directory, fileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(file, true);
            outputStream.write(data.getBytes());
            outputStream.write("\n".getBytes()); // Add a new line after writing data
            outputStream.close();
            // Toast.makeText(requireContext(), "Data saved to file: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to save data", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_MANAGE_EXTERNAL_STORAGE);
        } else {
            // Permission is already granted, save data to files
            saveDataToFiles();
        }
    }

    private void saveDataToFiles() {
        // Save url_txt to a file
        if (url_txt != null)
            saveDataToFile(url_txt, "data.txt");

        // Save url_jpg to a different file
        if (url_jpg != null)
            saveDataToFile(url_jpg, "image_url.txt");
    }
}
