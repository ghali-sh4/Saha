package com.example.myallproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class AddMealFragment extends Fragment {

    private EditText etName, etDesc, etPrice;
    private Button btnSaveMeal;
    private FirebaseServices fbs;
    private String restaurantId;

    private ImageView img;
//    private Button btnSelectImage;
    private Uri selectedImageUri;
    private StorageReference storageRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_meal, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FirebaseServices.getInstance();
        restaurantId = getArguments().getString("restaurantId");
        img = getView().findViewById(R.id.ivMealImage);
        etName = getView().findViewById(R.id.etMealName);
        etDesc = getView().findViewById(R.id.etMealDescription);
        etPrice = getView().findViewById(R.id.etMealPrice);
        btnSaveMeal = getView().findViewById(R.id.btnSaveMeal);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        btnSaveMeal.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();

            if (name.isEmpty() || desc.isEmpty() || priceStr.isEmpty() ) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Price must be a number", Toast.LENGTH_SHORT).show();
                return;
            }


            if (selectedImageUri != null) {
                StorageReference imageRef = storageRef.child("meals/" + UUID.randomUUID().toString());

                imageRef.putFile(selectedImageUri)
                        .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imgUrl = uri.toString();
                            Meal meal = new Meal(name, price, desc, imgUrl);

                            fbs.getFire()
                                    .collection("restaurants")
                                    .document(restaurantId)
                                    .collection("menu")
                                    .add(meal)
                                    .addOnSuccessListener(doc -> {
                                        Toast.makeText(getContext(), "Meal added successfully!", Toast.LENGTH_SHORT).show();
                                        getParentFragmentManager().popBackStack(); // go back to menu
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getContext(), "Failed to add meal", Toast.LENGTH_SHORT).show();
                                    });
                        }));
            } else {
                Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private  void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,123);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == getActivity().RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                img.setImageURI(selectedImageUri);
            }
        }
    }

    private void gotoMealsfragment(){
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.mainActivity, new ResturantOwnerHomeFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
}

