package nandroid.in.gaapadminportal;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final int[] token = new int[4];

        final TextView textView = findViewById(R.id.totalInfo);
        final ProgressBar progressBar = findViewById(R.id.loader);

        final ArrayList<String> ordersFromBlock1 = new ArrayList<>();
        final ArrayList<String> ordersFromBlock2 = new ArrayList<>();
        final ArrayList<String> ordersFromBlock3 = new ArrayList<>();
        final ArrayList<String> ordersFromBlock4 = new ArrayList<>();
        final int[] total = {0};
        db.collection("ordersForToday")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                        for(int i=0;i<4;i++) {
                            token[i] = 0;
                        }

                        assert queryDocumentSnapshots != null;
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            total[0] = total[0] + 1;
                            if(Objects.equals(document.getString("blockNo"), "1")) {
                                if( document.getString("id") != null) {
                                    ordersFromBlock1.add(document.getString("id") );
                                    token[0] = token[0] + 1;
                                }
                            } else if(Objects.equals(document.getString("blockNo"), "2")) {
                                if(document.getString("id") != null) {
                                    ordersFromBlock2.add( document.getString("id"));
                                    token[1] = token[1] + 1;
                                }
                            } else if(Objects.equals(document.getString("blockNo"), "3")) {
                                if(document.getString("id") != null) {
                                    ordersFromBlock3.add(document.getString("id"));
                                    token[2] = token[2] + 1;
                                }
                            } else if (Objects.equals(document.getString("blockNo"), "4")) {
                                if(document.getString("id") != null) {
                                    ordersFromBlock4.add(document.getString("id"));
                                    token[3] = token[3] + 1;
                                }
                            }


                            String Info = "Block 1: " + token[0] + "\n\n";
                            for (String a: ordersFromBlock1 ) {
                                Info = Info + a + "\n";
                            }
                            Info = Info + "\nBlock 2: " + token[1] + "\n\n";
                            for (String a: ordersFromBlock2 ) {
                                Info = Info + a + "\n";
                            }
                            Info = Info + "\nBlock 3: " + token[2] + "\n\n";
                            for (String a: ordersFromBlock3 ) {
                                Info = Info + a + "\n";
                            }
                            Info = Info + "\nBlock 4: " + token[3] + "\n\n";
                            for (String a: ordersFromBlock4 ) {
                                Info = Info + a + "\n";
                            }
                            Info = Info + "\nTotal :" + total[0] + "\n";

                            textView.setText(Info);
                            progressBar.setVisibility(View.INVISIBLE);

                        }

                    }
                });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
