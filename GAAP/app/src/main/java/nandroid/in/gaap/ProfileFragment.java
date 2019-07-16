package nandroid.in.gaap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.Objects;


public class ProfileFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("MyPref", 0);
        final String uName = pref.getString("user",null);

        TextView headingL = view.findViewById(R.id.HeadingLabel);
        TextView userL = view.findViewById(R.id.userLabel);
        TextView eN = view.findViewById(R.id.EmailLabel);
        TextView MobL = view.findViewById(R.id.MobileLabel);
        TextView blLa = view.findViewById(R.id.BlockLabel);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GoogleSansRegular.ttf");
        headingL.setTypeface(typeface);
        userL.setTypeface(typeface);
        eN.setTypeface(typeface);
        MobL.setTypeface(typeface);
        blLa.setTypeface(typeface);

        final TextView userName = view.findViewById(R.id.userNameDisplay);
        final TextView emailDisp = view.findViewById(R.id.EmailDisplay);
        final TextView mobDisp = view.findViewById(R.id.MobileDisplay);
        final TextView blockDisp = view.findViewById(R.id.BlockDisplay);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final ProgressBar progressBar = view.findViewById(R.id.loader);

        db.collection("usersInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                if(document.getString("emailID").equals(uName) || document.getString("grietId").equals(uName)) {
                                    userName.setText(document.getString("username"));
                                    emailDisp.setText(document.getString("emailID"));
                                    mobDisp.setText(document.getString("mobile_number"));

                                    String BlockNumber = "";

                                    if(document.getString("department").equals("CSE")) {
                                        BlockNumber = "1";
                                    } else if(document.getString("department").equals("ECE")) {
                                        BlockNumber = "2";
                                    } else if(document.getString("department").equals("EEE")
                                            || document.getString("department").equals("Mechanical")
                                            ||document.getString("department").equals("Civil")) {
                                        BlockNumber = "4";
                                    } else if(document.getString("department").equals("IT")) {
                                        BlockNumber = "3";
                                    }

                                    blockDisp.setText(BlockNumber);

                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        } else {
                            Log.d("FirebaseDbActivity", "Error getting documents: ", task.getException());
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
