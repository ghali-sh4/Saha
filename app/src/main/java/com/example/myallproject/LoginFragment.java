package com.example.myallproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvSignupLink;
    private FirebaseServices fbs;

    private TextView tvForgotPassword;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        // connecting components
        fbs= FirebaseServices.getInstance();
        etUsername=getView().findViewById(R.id.etUsernameLogin);
        etPassword=getView().findViewById(R.id.etPasswordLogin);
        btnLogin=getView().findViewById(R.id.btnLoginLogin);
        tvSignupLink=getView().findViewById(R.id.tvSignupLinkLogin);
        tvForgotPassword=getView().findViewById(R.id.tvForgotPassword);
        tvSignupLink.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                gotoSignupFragment();

                                            }
                                        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  data validation
                String username=etUsername.getText().toString();
                String password= etPassword.getText().toString();
                if (username.trim().isEmpty()&& password.trim().isEmpty()){
                    Toast.makeText(getActivity(), "SOME FIELDS ARE EMPTY", Toast.LENGTH_SHORT).show();
                    return;

                }
                // Login  procedure
                fbs.getAuth().signInWithEmailAndPassword(username,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                                                                         @Override
                                                                                                         public void onSuccess(AuthResult authResult) {
                                                                                                             Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                                                                                             FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                                                                             ft.replace(R.id.mainActivity, new ResturantOwnerHomeFragment());
                                                                                                             ft.commit();


                                                                                                         }
                                                                                                     }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoForgotPasswordFragment();

            }

            private void gotoForgotPasswordFragment() {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainActivity,new ForgotPasswordFragment());
                ft.commit();
            }
        });
    }
   private void gotoSignupFragment(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainActivity, new SignupFragment());
        ft.commit();

    }

   }


