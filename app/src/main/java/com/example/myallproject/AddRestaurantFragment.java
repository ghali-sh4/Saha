package com.example.myallproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRestaurantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRestaurantFragment extends Fragment {
    private EditText etName, etLocation, etPhone, etCategory;
    private TextView tvSelected;
    private Button btnAdd;
    private ImageView img;
    private FirebaseServices fbs;
    private Uri selectedImageUri;
    private double selectedLatitude = 0.0;
    private double selectedLongitude = 0.0;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddRestaurantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        connectComponents();

        getParentFragmentManager().setFragmentResultListener(
                MapPickerFragment.RESULT_KEY,
                getViewLifecycleOwner(),
                (requestKey, bundle) -> {
                    selectedLatitude = bundle.getDouble("latitude");
                    selectedLongitude = bundle.getDouble("longitude");

                    String locText = "Lat: " + selectedLatitude + ", Lng: " + selectedLongitude;
                    Toast.makeText(getActivity(), "Location Selected:\n" + locText, Toast.LENGTH_SHORT).show();

                    // Optionally set it into a TextView to show the user
//                    etLocation.setText(locText); // or a TextView if you prefer
                    tvSelected.setText(locText);
                });

    }

    private void connectComponents() {
        fbs = FirebaseServices.getInstance();
        etName = getActivity().findViewById(R.id.etName);
        etLocation=getActivity().findViewById(R.id.etCity);
        etPhone=getActivity().findViewById(R.id.etPhone);
        etCategory=getActivity().findViewById(R.id.etCategory);
        tvSelected = getActivity().findViewById(R.id.tvSelectedLocation);
        btnAdd=getActivity().findViewById(R.id.btnSaveRestaurant);
        img = getView().findViewById(R.id.ivPreview);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        Button btnPickLocation = getActivity().findViewById(R.id.btnSelectLocation);
        btnPickLocation.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.mainActivity, new MapPickerFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String location = etLocation.getText().toString();
                String phone = etPhone.getText().toString();
                String category = etCategory.getText().toString();
                String uid = fbs.getAuth().getInstance().getCurrentUser().getUid();

                if (name.trim().isEmpty() || location.trim().isEmpty() || phone.trim().isEmpty() || category.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Some fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedImageUri == null) {
                    Toast.makeText(getActivity(), "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Upload the image to Firebase Storage
                String imageName = UUID.randomUUID().toString() + ".jpg";
                StorageReference storageReference = fbs.getStorage().getReference().child("restaurant_images/" + imageName);
                storageReference.putFile(selectedImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get the image URL after the upload is successful
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String photoUrl = uri.toString(); // Get the image URL

                                        // Create the Restaurant object with the image URL
                                        Resturant restaurant = new Resturant(name, location, phone, category, photoUrl, uid, selectedLatitude, selectedLongitude);

                                        // Add the Restaurant object to Firestore
                                        fbs.getFire().collection("restaurants").add(restaurant)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Toast.makeText(getActivity(), "Restaurant added successfully", Toast.LENGTH_SHORT).show();
                                                        gotoAllRestaurantFragment();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity(), "Failed to add restaurant", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                            }
                        });
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRestaurantFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRestaurantFragment newInstance(String param1, String param2) {
        AddRestaurantFragment fragment = new AddRestaurantFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_restaurant, container, false);
    }
    private void gotoAllRestaurantFragment() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.mainActivity, new ResturantOwnerHomeFragment());
        ft.addToBackStack(null);
        ft.commit();

    }
}