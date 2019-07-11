package just.cse.mahfuz.eas;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.admin.v1beta1.Progress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attendance extends AppCompatActivity {

    TextView date, courseID, courseName, roll;
    ImageView present, absent;

    String sDept, sYear, sSemester;
    String sDate, sCourseID, sCourseName;
    List<String> sRoll;

    int iRoll=0;

    ProgressDialog progressDialog;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        date = findViewById(R.id.date);
        courseID = findViewById(R.id.courseID);
        courseName = findViewById(R.id.courseName);
        roll = findViewById(R.id.roll);
        present = findViewById(R.id.present);
        absent = findViewById(R.id.absent);

        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(Attendance.this);

        try {
            sDept = getIntent().getExtras().getString("dept");
            sYear = getIntent().getExtras().getString("year");
            sSemester = getIntent().getExtras().getString("semester");
        } catch (Exception e) {
            sDept="cse";
            sYear="3";
            sSemester="1";
            sCourseID="CSE-3201";
        }

        final String timeInMill=String.valueOf(System.currentTimeMillis());
        sDate= DateFormat.format("dd.MM.yy", Long.parseLong(timeInMill)).toString();

        date.setText(sDate);

        sRoll= new ArrayList<>();

        firebaseFirestore.collection("university").document("just")
                .collection("a")
                .document(sDept)
                .collection(sYear)
                .document(sSemester)
                .collection("student")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        //;

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if (task.getResult().isEmpty()) {
                                    Log.d("Error", "onSuccess: LIST EMPTY");

                                    return;
                                } else {
                                    sRoll.add(document.getId());
                                    Log.e("Error",  document.getId());

                                }
                            }
                            roll.setText(sRoll.get(iRoll));
                        } else {
                            Log.e("Error", "Error getting documents: ", task.getException());
                            progressDialog.dismiss();

                        }
                    }

                });




        present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Submiting..");
                progressDialog.show();

                Map<String,Object> attendance= new HashMap<>();
                attendance.put("attendance","present");
                firebaseFirestore.collection("university").document("just")
                        .collection("a")
                        .document("cse")
                        .collection(sYear)
                        .document(sSemester)
                        .collection("course")
                        .document(sCourseID)
                        .collection(roll.getText().toString())
                        .document(sDate).set(attendance)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Toast.makeText(Attendance.this,"Done",Toast.LENGTH_SHORT).show();
                            if (iRoll>roll.length()+1) {
                                Toast.makeText(Attendance.this,"Attendance Completed",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                iRoll++;
                                roll.setText(sRoll.get(iRoll));
                            }
                        }
                    })
                ;
            }
        });


        absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Submiting..");
                progressDialog.show();

                Map<String,Object> attendance= new HashMap<>();
                attendance.put("attendance","absent");
                firebaseFirestore.collection("university").document("just")
                        .collection("a")
                        .document("cse")
                        .collection(sYear)
                        .document(sSemester)
                        .collection("course")
                        .document(sCourseID)
                        .collection(roll.getText().toString())
                        .document(sDate).set(attendance)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                Toast.makeText(Attendance.this,"Done",Toast.LENGTH_SHORT).show();

                                if (iRoll>roll.length()+1) {
                                    Toast.makeText(Attendance.this,"Attendance Completed",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else {
                                    iRoll++;
                                    roll.setText(sRoll.get(iRoll));
                                }

                            }
                        })
                ;
            }
        });

    }
}
