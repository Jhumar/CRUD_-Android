package com.example.crud;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private static List<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nameTextView.setText(product.getName());
        holder.unitTextView.setText(product.getUnit());
        holder.priceTextView.setText(String.format(Locale.getDefault(), "%.2f", product.getPrice()));
        holder.expirationDateTextView.setText(product.getExpirationDate());
        holder.quantityTextView.setText(String.format(Locale.getDefault(), "%d", product.getQuantity()));

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

//            holder.imageView.setImageURI(Uri.parse(product.getImageUri()));
        } else {
            // Permission not granted, handle appropriately
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView unitTextView;
        private TextView priceTextView;
        private TextView expirationDateTextView;
        private TextView quantityTextView;
        private ImageView imageView;

        private ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            unitTextView = itemView.findViewById(R.id.unitTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            expirationDateTextView = itemView.findViewById(R.id.expirationDateTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            imageView = itemView.findViewById(R.id.imageView);

            deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Product product = productList.get(position);
                        deleteProduct(product);
                    }
                }

                private void deleteProduct(Product product) {
                    // Get a writable database
                    DatabaseHelper dbHelper = new DatabaseHelper(context);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    // Define the WHERE clause for the delete query
                    String selection = "_id = ?";
                    String[] selectionArgs = { String.valueOf(product.getId()) };

                    // Execute the delete query
                    int deletedRows = db.delete("products", selection, selectionArgs);

                    // Check if the delete query was successful
                    if (deletedRows > 0) {
                        // Product deleted successfully
                        productList.remove(product);
                        notifyItemRemoved(getAdapterPosition());
                    } else {
                        // Failed to delete product
                    }

                    // Close the database connection
                    db.close();
                }
            });
        }
    }
}
