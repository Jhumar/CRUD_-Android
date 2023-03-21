package com.example.crud;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MaterialPickerOnPositiveButtonClickListener<Long> {
    private static final int PERMISSION_REQUEST_CODE = 123;

    private DatabaseHelper db;
    private TextInputEditText name, unit, price, expirationDate, quantity;
    private RecyclerView recyclerView;
    private MaterialButton buttonCalendar, buttonSubmit;
    private ImageView preview;
    private Uri previewUri;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private ProductAdapter productAdapter;

    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_view);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
        else {
            loadProductData();
        }

        name = findViewById(R.id.name);
        unit = findViewById(R.id.unit);
        price = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);

        buttonCalendar = findViewById(R.id.button_calendar);
        buttonCalendar.setOnClickListener((v) -> {
            showDatePickerDialog();
        });

        expirationDate = findViewById(R.id.expiration_date);
        expirationDate.setOnClickListener((v) -> {
            showDatePickerDialog();
        });

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            previewUri = uri;
            preview.setImageURI(uri);
            preview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        });

        preview = findViewById(R.id.preview);
        preview.setOnClickListener((v) -> {
            imagePickerLauncher.launch("image/*");
        });

        buttonSubmit = findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener((v) -> {
            String sName = String.valueOf(name.getText());
            String sUnit =  String.valueOf(unit.getText());
            String sPrice = String.valueOf(price.getText());
            String sExpirationDate = String.valueOf(expirationDate.getText());
            String sQuantity = String.valueOf(quantity.getText());

            try {
                if (sName.length() <= 0) {
                    throw new Exception("Name is required.");
                }

                if (sUnit.length() <= 0) {
                    throw new Exception("Unit is required.");
                }

                if (sPrice.length() <= 0) {
                    throw new Exception("Price is required.");
                }

                if (sExpirationDate.length() <= 0) {
                    throw new Exception("Expiration date is required.");
                }

                if (sQuantity.length() <= 0) {
                    throw new Exception("Quantity is required.");
                }

                if (previewUri == null || previewUri.toString().length() <= 0) {
                    throw new Exception("Image is required.");
                }

                Product product = new Product();
                product.setName(sName);
                product.setUnit(sUnit);
                product.setPrice(Double.valueOf(sPrice));
                product.setExpirationDate(sExpirationDate);
                product.setQuantity(Integer.valueOf(sQuantity));
                product.setImageUri(previewUri.toString());

                db.addProduct(product);

                loadProductData();
                Snackbar.make(this, v, "Success", Snackbar.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Snackbar.make(this, v, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void loadProductData() {
        productList = db.getAllProducts();
        productAdapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showDatePickerDialog() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();

        MaterialDatePicker<Long> picker = builder
                .setTitleText("Select expiration date")
                .build();

        picker.addOnPositiveButtonClickListener(this);
        picker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
    }

    @Override
    public void onPositiveButtonClick(Long selection) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        expirationDate.setText(sdf.format(new Date(selection)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission has been granted
                    loadProductData();
                } else {
                    // Permission has been denied
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}