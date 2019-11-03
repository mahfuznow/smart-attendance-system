package just.cse.mahfuz.eas;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import just.cse.mahfuz.eas.adapter.AttendaneRecyclerAdapter;
import just.cse.mahfuz.eas.adapter.ViewAttendaneRecyclerAdapter;

public class AttendanceActivityNew extends AppCompatActivity {

    TextView date, courseID, courseName, roll;
    ImageView present, absent;

    String sDept, sYear, sSemester;
    String myunit, mydepartment;
    String sType;
    String sDate, sCourseID, sCourseName;
    List<String> sRoll;
    AttendaneRecyclerAdapter attendaneRecyclerAdapter;
    ViewAttendaneRecyclerAdapter viewAttendaneRecyclerAdapter;
    RecyclerView recyclerView;

    int iRoll = 0;

    ProgressDialog progressDialog;

    FirebaseFirestore firebaseFirestore;

    private SparseBooleanArray itemStateArray = new SparseBooleanArray();


    Calendar calendar;
    int dayint;
    String dayArray[] = {"saturday", "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
    public String dayOfWeek;
    int year, month, dayOfMonth;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_new);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Take Attendance");
        actionBar.setDisplayHomeAsUpEnabled(true);

        date = findViewById(R.id.date);
        courseID = findViewById(R.id.courseID);
        courseName = findViewById(R.id.courseName);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(AttendanceActivityNew.this);

        progressDialog.setMessage("Loading..");
        progressDialog.show();

        try {
            //sDept = getIntent().getExtras().getString("dept");
            sCourseID = getIntent().getExtras().getString("courseID");
            sCourseName = getIntent().getExtras().getString("courseName");
            myunit = getIntent().getExtras().getString("unit");
            mydepartment = getIntent().getExtras().getString("department");
            sType = getIntent().getExtras().getString("type");

            courseID.setText(sCourseID);
            courseName.setText(sCourseName);

            if ("-".equals(String.valueOf(sCourseID.charAt(2)))) {
                sYear = String.valueOf(sCourseID.charAt(3));
                sSemester = String.valueOf(sCourseID.charAt(4));
            } else if ("-".equals(String.valueOf(sCourseID.charAt(3)))) {
                sYear = String.valueOf(sCourseID.charAt(4));
                sSemester = String.valueOf(sCourseID.charAt(5));
            }

//            sYear = getIntent().getExtras().getString("year");
//            sSemester = getIntent().getExtras().getString("semester");

        } catch (Exception e) {
//            sDept="cse";
//            sYear="3";
//            sSemester="2";
//            sCourseID="CSE-3201";
        }


        //this is for viewing attendance
        if ("v".equals(sType)) {

            actionBar.setTitle("View Attendance");

            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(AttendanceActivityNew.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                            calendar.set(year, month, day);
                            dayint = calendar.get(Calendar.DAY_OF_WEEK);
                            dayOfWeek = dayArray[dayint];

//                                    date.setText(java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL).format(calendar.getTime()));

                            final String timeInMill = String.valueOf(calendar.getTimeInMillis());
                            sDate = DateFormat.format("dd.MM.yy", Long.parseLong(timeInMill)).toString();
                            date.setText(sDate);


                            sRoll = new ArrayList<>();

                            firebaseFirestore.collection("university").document("just")
                                    .collection(myunit)
                                    .document(mydepartment)
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
                                                        Log.e("Error", document.getId());

                                                    }
                                                }

                                                load();

                                            } else {
                                                Log.e("Error", "Error getting documents: ", task.getException());
                                                progressDialog.dismiss();

                                            }
                                        }

                                    });
                        }
                    }, year, month, dayOfMonth);
            //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();

        }


        //this is for taking attendance
        else {


            final String timeInMill = String.valueOf(System.currentTimeMillis());
            sDate = DateFormat.format("dd.MM.yy", Long.parseLong(timeInMill)).toString();

            date.setText(sDate);

            sRoll = new ArrayList<>();


            try {

                firebaseFirestore.collection("university").document("just")
                        .collection(myunit)
                        .document(mydepartment)
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
                                            Log.e("Error", document.getId());

                                        }
                                    }


                                    //checking attendance is present or not
                                    firebaseFirestore.collection("university").document("just")
                                            .collection(myunit)
                                            .document(mydepartment)
                                            .collection(sYear)
                                            .document(sSemester)
                                            .collection("course")
                                            .document(sCourseID)
                                            .collection(sRoll.get(1))
                                            .document(sDate)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {

                                                        if (task.getResult().exists()) {
                                                            Toast.makeText(AttendanceActivityNew.this, "Today's attendance has been taken", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        } else {
                                                            attendaneRecyclerAdapter = new AttendaneRecyclerAdapter(AttendanceActivityNew.this, sRoll, sYear, sSemester, sCourseID, sDate, myunit, mydepartment);
                                                            recyclerView.setAdapter(attendaneRecyclerAdapter);
                                                            progressDialog.dismiss();
                                                        }
                                                    } else {

                                                        Toast.makeText(AttendanceActivityNew.this, "Error", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });

                                } else {
                                    Log.e("Error", "Error getting documents: ", task.getException());
                                    progressDialog.dismiss();

                                }
                            }

                        });
            } catch (Exception e) {
                Toast.makeText(AttendanceActivityNew.this, "No Student in this class", Toast.LENGTH_SHORT).show();

            }


        }
    }


    public void load() {

        progressDialog.setMessage("loading..");
        progressDialog.show();


        for (int i = 0; i < sRoll.size(); i++) {

            final int finalI = i;
            firebaseFirestore.collection("university").document("just")
                    .collection(myunit)
                    .document(mydepartment)
                    .collection(sYear)
                    .document(sSemester)
                    .collection("course")
                    .document(sCourseID)
                    .collection(sRoll.get(i))
                    .document(sDate)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            //boolean bool=task.getResult().getBoolean("attendance");
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    boolean attendance = task.getResult().getBoolean("attendance");
                                    itemStateArray.put(finalI, attendance);
                                    if (finalI == sRoll.size() - 1) {

                                        viewAttendaneRecyclerAdapter = new ViewAttendaneRecyclerAdapter(AttendanceActivityNew.this, sRoll, sYear, sSemester, sCourseID, sDate, itemStateArray);
                                        recyclerView.setAdapter(viewAttendaneRecyclerAdapter);
                                        progressDialog.dismiss();
                                        progressDialog.dismiss();
                                        Toast.makeText(AttendanceActivityNew.this, "loaded", Toast.LENGTH_SHORT).show();

                                    }
                                }


                            } else {

                            }
                        }
                    });
        }


    }


}
