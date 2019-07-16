package nandroid.in.gaap;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextView registerHeading = view.findViewById(R.id.registertext);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GoogleSansRegular.ttf");
        registerHeading.setTypeface(typeface);

        final EditText userName = view.findViewById(R.id.userName_register);
        final EditText emailId = view.findViewById(R.id.userEmail_reg);
        final EditText mobNo = view.findViewById(R.id.userPhNo_reg);
        Spinner spinner = view.findViewById(R.id.departmentChoose);
        final String dept = spinner.getSelectedItem().toString();
        final EditText grietId = view.findViewById(R.id.userGrietIDNo_reg);
        final EditText passWord = view.findViewById(R.id.password_register);
        final Spinner typeOfPerson = view.findViewById(R.id.typeOfPerson);

        TextView textView = view.findViewById(R.id.loginOptionText);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.loginregFragment, new HomeFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> user = new HashMap<>();
                user.put("username", userName.getText().toString());
                user.put("emailID", emailId.getText().toString());
                user.put("mobile_number", mobNo.getText().toString());
                user.put("department", dept);
                user.put("grietId", grietId.getText().toString());
                user.put("password", passWord.getText().toString());
                user.put("typeOfPerson", typeOfPerson.getSelectedItem().toString());

                db.collection("usersInfo")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("FirebaseDbConnectivity", "DocumentSnapshot added with ID: " + documentReference.getId());
                                Snackbar.make(getView(),"Account created successfully. Login to continue",Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("FirebaseDbConnectivity", "Error adding document", e);
                                Snackbar.make(getView(),"Failed to create account",Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
        });

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
