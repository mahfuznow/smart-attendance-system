package just.cse.mahfuz.eas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import just.cse.mahfuz.eas.adapter.CourseRecyclerAdapter;
import just.cse.mahfuz.eas.model.Course;

public class SelectCourseActivity extends AppCompatActivity {

    String shortName;
    ProgressDialog progressDialog;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String uid,sCategory;

    List<Course> courseModel;

    CourseRecyclerAdapter courseRecyclerAdapter;

    RecyclerView mRecyclerView;

    Button addCourse;
    String sType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_course);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Select Course");
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        uid=firebaseAuth.getUid();
        progressDialog = new ProgressDialog(SelectCourseActivity.this);
        addCourse=findViewById(R.id.add);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog.setMessage("Loading..");
        progressDialog.show();


        try {
            sType=getIntent().getExtras().getString("type");
        }
        catch (Exception e) {
            sType="t";
        }

        firebaseFirestore.collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                sCategory =documentSnapshot.getString("category");
                if("teacher".equals(sCategory)) {
                    shortName =documentSnapshot.getString("shortName");
                    courseModel = new ArrayList<>();

                    firebaseFirestore.collection("university").document("just")
                            .collection("a")
                            .document("cse")
                            .collection("teacher")
                            .document(shortName)
                            .collection("course")
                            .orderBy("courseID", Query.Direction.ASCENDING)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            if (task.getResult().isEmpty()) {
                                                Log.d("Error", "onSuccess: LIST EMPTY");
                                                return;
                                            } else {
                                                courseModel =task.getResult().toObjects(Course.class);
                                                courseRecyclerAdapter = new CourseRecyclerAdapter(SelectCourseActivity.this, courseModel,"select"+sType);
                                                mRecyclerView.setAdapter(courseRecyclerAdapter);
                                                progressDialog.dismiss();
                                            }
                                        }
                                    } else {
                                        Log.e("Error", "Error getting documents: ", task.getException());
                                        progressDialog.dismiss();

                                    }
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.e("Error", "Error getting documents: ", task.getException());
                            progressDialog.dismiss();
                        }
                    });

                }
                else  {
                    Toast.makeText(SelectCourseActivity.this,"Please login as teacher",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


//        try {
//            shortName=getIntent().getExtras().getString("shortName");
//        }
//        catch (Exception e) {
//            shortName="SMG";
//        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id==android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
