package com.example.myallproject;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllRestaurantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllRestaurantFragment extends Fragment {
    private FirebaseServices fbs;
    private ArrayList<Resturant> rests;
    private RecyclerView rvRests;
    private RestaurantAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllRestaurantFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllRestaurantFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllRestaurantFragment newInstance(String param1, String param2) {
        AllRestaurantFragment fragment = new AllRestaurantFragment();
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
        return inflater.inflate(R.layout.fragment_all_restaurant, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FirebaseServices.getInstance();
        rests = new ArrayList<>();
        rvRests = getView().findViewById(R.id.rvRestaurantsRestFragment);
        rvRests.setHasFixedSize(true);
        rvRests.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RestaurantAdapter(getActivity(), rests, v->{
            Resturant clickedResturant = (Resturant) v.getTag(); // Retrieve project from tag
            openResturantDetails(clickedResturant);
        });
        rvRests.setAdapter(adapter);
        fbs.getFire().collection("restaurants").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){
                    Resturant rest = dataSnapshot.toObject(Resturant.class);
                    rest.setId(dataSnapshot.getId());

                    rests.add(rest);
                }

                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "No data available", Toast.LENGTH_SHORT).show();
                Log.e("AllRestaurantsFragment", e.getMessage());
            }
        });
    }

    private void openResturantDetails(Resturant resturant) {
        // Example: Navigate to another activity or show a Toast
        Toast.makeText(getContext(), "Clicked: " + resturant.getName(), Toast.LENGTH_SHORT).show();

        // If you want to open a new activity
        // Intent intent = new Intent(getContext(), ProjectDetailsActivity.class);
        // intent.putExtra("project", project);
        // startActivity(intent);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainActivity, new MenuFragment(resturant.getId(),resturant.getName(),resturant.getLatitude(),resturant.getLongitude())); // Correct ID
        ft.addToBackStack(null); // Allows back navigation
        ft.commit();


    }
}