package just.cse.mahfuz.eas;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import just.cse.mahfuz.eas.adapter.AttendaneRecyclerAdapter;

public class AttendanceActivityNew extends AppCompatActivity {

    TextView date, courseID, courseName, roll;
    ImageView present, absent;

    String sDept, sYear, sSemester;
    String sDate, sCourseID, sCourseName;
    List<String> sRoll;
    AttendaneRecyclerAdapter attendaneRecyclerAdapter;
    RecyclerView recyclerView;

    int iRoll=0;

    ProgressDialog progressDialog;

    FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_new);

        date = findViewById(R.id.date);
        courseID = findViewById(R.id.courseID);
        courseName = findViewById(R.id.courseName);
        recyclerView=findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(AttendanceActivityNew.this);

        progressDialog.setMessage("Loading..");
        progressDialog.show();

        try {
            //sDept = getIntent().getExtras().getString("dept");
            sDept="cse";
            sCourseID = getIntent().getExtras().getString("courseID");
            sCourseName = getIntent().getExtras().getString("courseName");

            courseID.setText(sCourseID);
            courseName.setText(sCourseName);

            if ("-".equals(String.valueOf(sCourseID.charAt(2)))) {
                sYear=String.valueOf(sCourseID.charAt(3));
                sSemester=String.valueOf(sCourseID.charAt(4));
            }
            else if ("-".equals(String.valueOf(sCourseID.charAt(3)))) {
                sYear=String.valueOf(sCourseID.charAt(4));
                sSemester=String.valueOf(sCourseID.charAt(5));
            }

//            sYear = getIntent().getExtras().getString("year");
//            sSemester = getIntent().getExtras().getString("semester");

        } catch (Exception e) {
//            sDept="cse";
//            sYear="3";
//            sSemester="2";
//            sCourseID="CSE-3201";
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
                            attendaneRecyclerAdapter= new AttendaneRecyclerAdapter(AttendanceActivityNew.this,sRoll,sYear,sSemester,sCourseID,sDate);
                            recyclerView.setAdapter(attendaneRecyclerAdapter);
                            progressDialog.dismiss();
                        } else {
                            Log.e("Error", "Error getting documents: ", task.getException());
                            progressDialog.dismiss();

                        }
                    }

                });






    }


}
