package just.cse.mahfuz.eas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import just.cse.mahfuz.eas.adapter.CourseRecyclerAdapter;
import just.cse.mahfuz.eas.model.Course;

public class AssignCourseActivity extends AppCompatActivity {
    String shortName;
    ProgressDialog progressDialog;

    FirebaseFirestore firebaseFirestore;

    List<Course> courseModel;

    CourseRecyclerAdapter courseRecyclerAdapter;

    RecyclerView mRecyclerView;

    Button addCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_course);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Assign Course");
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(AssignCourseActivity.this);
        addCourse=findViewById(R.id.add);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog.setMessage("Loading..");
        progressDialog.show();

        try {
            shortName=getIntent().getExtras().getString("shortName");
        }
        catch (Exception e) {
            shortName="SMG";
        }


        loadCourses();


        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AssignCourseActivity.this);

                View view = LayoutInflater.from(AssignCourseActivity.this).inflate(R.layout.custom_dialog_input_course, null);
                builder.setView(view);
                builder.setCancelable(true);
                final AlertDialog alertDialog = builder.create();

                final EditText courseID = view.findViewById(R.id.courseID);
                final EditText courseName = view.findViewById(R.id.courseName);
                Button assign = view.findViewById(R.id.assign);

                assign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.setMessage("Assigning course...");
                        progressDialog.show();

                        String sCourseID,sCourseName;
                        sCourseID=courseID.getText().toString().trim();
                        sCourseName=courseName.getText().toString().trim();

                        if (TextUtils.isEmpty(sCourseID) || TextUtils.isEmpty(sCourseName)) {
                            Toast.makeText(AssignCourseActivity.this,"Please insert required fields",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else if (   ("-".equals(String.valueOf(sCourseID.charAt(2))) || "-".equals(String.valueOf(sCourseID.charAt(3))))  && (sCourseID.length()>=7)) {
                            Map<String,Object> setCourse= new HashMap<>();
                            setCourse.put("courseID",sCourseID);
                            setCourse.put("courseName",sCourseName);
                            setCourse.put("teacher",shortName);
                            firebaseFirestore.collection("university").document("just")
                                    .collection("a")
                                    .document("cse")
                                    .collection("teacher")
                                    .document(shortName)
                                    .collection("course")
                                    .document(sCourseID)
                                    .set(setCourse)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(AssignCourseActivity.this,"Added Successfully",Toast.LENGTH_SHORT).show();
                                            loadCourses();
                                            alertDialog.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AssignCourseActivity.this,"Error occured",Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            alertDialog.dismiss();
                                        }
                                    })
                            ;
                        }
                        else {
                            Toast.makeText(AssignCourseActivity.this,"Course Id format is invalid",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }



                    }
                });

                alertDialog.show();
            }
        });
    }


    public void loadCourses() {
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
                                    courseRecyclerAdapter = new CourseRecyclerAdapter(AssignCourseActivity.this, courseModel,"assign");
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
