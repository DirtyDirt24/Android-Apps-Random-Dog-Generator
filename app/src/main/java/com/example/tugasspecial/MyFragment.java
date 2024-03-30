package com.example.tugasspecial;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MyFragment extends Fragment {

    GridView gridView;
    GridAdapter gridAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my, container, false);

        String[] list = readDataFromFile("data.txt");
        String[] img = readDataFromFile("image_url.txt");

        gridView = rootView.findViewById(R.id.gridView);
        gridAdapter = new GridAdapter(requireContext(), list, img);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            String clickedItem = list[position];
            String clickedImageUrl = img[position];

            Intent intent = new Intent(requireContext(), EditActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("itemName", clickedItem);
            intent.putExtra("imageUrl", clickedImageUrl);
            startActivity(intent);
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();
    }
    private String[] readDataFromFile(String fileName) {
        ArrayList<String> dataList = new ArrayList<>();
        try {
            File directory = requireContext().getExternalFilesDir(null);
            File file = new File(directory, fileName);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = br.readLine()) != null) {
                dataList.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Convert ArrayList to array
        String[] dataArray = new String[dataList.size()];
        dataArray = dataList.toArray(dataArray);
        return dataArray;
    }

    private void reloadData() {
        String[] list = readDataFromFile("data.txt");
        String[] img = readDataFromFile("image_url.txt");

        gridAdapter = new GridAdapter(requireContext(), list, img);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            String clickedItem = list[position];
            String clickedImageUrl = img[position];

            Intent intent = new Intent(requireContext(), EditActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("itemName", clickedItem);
            intent.putExtra("imageUrl", clickedImageUrl);
            startActivity(intent);
        });
    }
}
