package nandroid.in.gaap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.IpSecManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class BookAMealFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public BookAMealFragment() {
        // Required empty public constructor
    }
    public static BookAMealFragment newInstance(String param1, String param2) {
        BookAMealFragment fragment = new BookAMealFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_ameal, container, false);
        TextView textView = view.findViewById(R.id.HeadingLabelMeal);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GoogleSansRegular.ttf");
        textView.setTypeface(typeface);

        final TextView tokenView = view.findViewById(R.id.tokenView);

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0);
        final String uName = pref.getString("user",null);
        final String uNameId = pref.getString("id",null);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Spinner spinner = view.findViewById(R.id.selectedBlockNumber);
        final String output = spinner.getSelectedItem().toString();
        Button button = view.findViewById(R.id.bookAMealButton);

        final ProgressBar progressBar = view.findViewById(R.id.loader);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                progressBar.setVisibility(View.VISIBLE);
                SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
                String currentDateandTime = sdf.format(new Date());
                int timeValue = Integer.parseInt(currentDateandTime);

                if(timeValue < 130000 && timeValue > 80000) {

                    Snackbar.make(getView(),"Can't order at this time. Timings (1:00PM - 8:00AM)",Snackbar.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);

                } else {

                    final int[] token = new int[1];
                    DocumentReference usersRef = db.collection("ordersForToday").document(uName);
                    usersRef.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()) {
                                        Snackbar.make(getView(),"You have already booked your meal", Snackbar.LENGTH_LONG).show();

                                        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("MyPref", 0);
                                        final String uName = pref.getString("user",null);

                                        tokenView.setText("Your Token ID is: "+ documentSnapshot.getString("grietId") + "\n\n" + "Pick up your meal between 12:10PM and 12:20PM at Block number " + output);

                                    } else {

                                        Map<String, Object> user = new HashMap<>();
                                        user.put("username", uName);
                                        user.put("blockNo", output);
                                        user.put("id", uNameId );

                                        db.collection("ordersForToday").document(uName)
                                                .set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        token[0] = token[0]+1;
                                                        tokenView.setText("Your Token ID is: "+ uNameId + "\n\n" + "Pick up your meal between 12:10PM and 12:20PM at Block number " + output);

                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        Snackbar.make(getView(), "Meal booked successfully", Snackbar.LENGTH_LONG).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Snackbar.make(getView(),"Error booking meal. Try in a few minutes", Snackbar.LENGTH_LONG).show();
                                                    }
                                                });

                                    }

                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
