package com.example.tugasspecial;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        String itemName = intent.getStringExtra("itemName");
        String imageUrl = intent.getStringExtra("imageUrl");

        TextView itemNameTextView = findViewById(R.id.itemNameTextView);
        itemNameTextView.setText(itemName);

        ImageView imageView = findViewById(R.id.imageView);
        Picasso.get().load(imageUrl).into(imageView);

        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(v -> showEditNameDialog());

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            if (position != -1) {
                deleteDataFromFile(position, "data.txt");
                deleteDataFromFile(position, "image_url.txt");

                Toast.makeText(EditActivity.this, "Data deleted", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);

                finish();
            } else {
                Toast.makeText(EditActivity.this, "Invalid position", Toast.LENGTH_SHORT).show();
            }
        });

        Button backToMainButton = findViewById(R.id.backToMainButton);
        backToMainButton.setOnClickListener(v -> finish());
    }

    private void deleteDataFromFile(int position, String fileName) {
        try {
            File directory = getExternalFilesDir(null);
            File file = new File(directory, fileName);
            if (file.exists()) {
                ArrayList<String> lines = new ArrayList<>();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                reader.close();

                if (position >= 0 && position < lines.size()) {
                    lines.remove(position);
                }

                FileWriter writer = new FileWriter(file);
                for (String updatedLine : lines) {
                    writer.write(updatedLine + System.getProperty("line.separator"));
                }
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showEditNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.pop_out_edit, null);
        EditText editNameEditText = dialogView.findViewById(R.id.editNameEditText);
        Button confirmEditButton = dialogView.findViewById(R.id.confirmEditButton);

        // Set the current item name to the EditText
        String currentName = ((TextView) findViewById(R.id.itemNameTextView)).getText().toString();
        editNameEditText.setText(currentName);

        // Set up the dialog
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Handle confirm button click
        confirmEditButton.setOnClickListener(v -> {
            String newName = editNameEditText.getText().toString();
            if(!newName.isEmpty()) {
                updateNameInUI(newName); // Update the name in the UI
                updateNameInFiles(newName, position); // Update the name in the data files
                Toast.makeText(EditActivity.this, "Name updated", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(EditActivity.this, "Name cannot be empty!", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss(); // Dismiss the dialog
        });

        // Show the dialog
        dialog.show();
    }

    private void updateNameInUI(String newName) {
        TextView itemNameTextView = findViewById(R.id.itemNameTextView);
        itemNameTextView.setText(newName);
    }

    private void updateNameInFiles(String newName, int position) {
        // Update the name in your data files (e.g., data.txt)
        try {
            File directory = getExternalFilesDir(null);
            if (directory != null) {
                // Update name in data.txt
                File dataFile = new File(directory, "data.txt");
                if (dataFile.exists()) {
                    ArrayList<String> dataLines = readLinesFromFile(dataFile);
                    if (position >= 0 && position < dataLines.size()) {
                        dataLines.set(position, newName);
                        writeLinesToFile(dataFile, dataLines);
                    }
                }

                // No need to update name in image_url.txt if positions correspond to each other

                // You can add similar logic for updating other files if necessary
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> readLinesFromFile(File file) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }

    private void writeLinesToFile(File file, ArrayList<String> lines) throws IOException {
        FileWriter writer = new FileWriter(file);
        for (String line : lines) {
            writer.write(line + System.getProperty("line.separator"));
        }
        writer.close();
    }


}


