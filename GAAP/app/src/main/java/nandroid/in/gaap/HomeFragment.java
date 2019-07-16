package nandroid.in.gaap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {

    OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = view.findViewById(R.id.registerOptionText);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.loginregFragment, new RegisterFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        TextView textView1 = view.findViewById(R.id.logintext);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GoogleSansRegular.ttf");
        textView1.setTypeface(typeface);


        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        final EditText username = view.findViewById(R.id.userName_login);
        final EditText password = view.findViewById(R.id.password_login);

        final ArrayList<String> userNames = new ArrayList<>();
        final ArrayList<String> grietIds = new ArrayList<>();
        final ArrayList<String> passwords = new ArrayList<>();

        db.collection("usersInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                userNames.add(document.getString("emailID"));
                                grietIds.add(document.getString("grietId"));
                                passwords.add(document.getString("password"));
                                Log.d("Data", Objects.requireNonNull(document.getString("emailID")));
                                Log.d("Data", Objects.requireNonNull(document.getString("grietId")));
                                Log.d("Data", Objects.requireNonNull(document.getString("password")));
                            }
                        } else {
                            Log.d("FirebaseDbActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });

        final ConstraintLayout constraintLayout = view.findViewById(R.id.loadingView);
        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                constraintLayout.setVisibility(View.VISIBLE);

                if((userNames.indexOf(username.getText().toString()) > -1
                        || grietIds.indexOf(username.getText().toString()) > -1
                ) && passwords.indexOf(password.getText().toString()) > -1) {
                    SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("user", username.getText().toString());
                    editor.putString("id",grietIds.get(userNames.indexOf(username.getText().toString())));
                    editor.apply();

                    startActivity(new Intent(getContext(), MainActivity.class));
                } else {
                    Snackbar.make(getView(), "Wrong credentials. Try again",Snackbar.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }

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
