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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import just.cse.mahfuz.eas.adapter.StudentRecyclerAdapter;

public class StudentAssignActivity extends AppCompatActivity {

    String shortName;
    String sYear,sSemester;
    ProgressDialog progressDialog;

    FirebaseFirestore firebaseFirestore;

    StudentRecyclerAdapter studentRecyclerAdapter;

    RecyclerView mRecyclerView;

    Button modify;



    List<String> sRoll;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assign);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Assign Student");
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(StudentAssignActivity.this);
        modify =findViewById(R.id.modify);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog.setMessage("Loading..");
        progressDialog.show();



        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StudentAssignActivity.this);

        View view = LayoutInflater.from(StudentAssignActivity.this).inflate(R.layout.custom_dialog_input_year_semester, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();

        final Spinner year = view.findViewById(R.id.year);
        final Spinner semester = view.findViewById(R.id.semester);
        final Button proceed = view.findViewById(R.id.proceed);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sYear= (String) parent.getItemAtPosition(position);
                        if ("--Year--".equals(sYear)) {
                            sYear="null";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sSemester= (String) parent.getItemAtPosition(position);
                        if ("--Semester--".equals(sSemester)) {
                            sSemester="null";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                if ("null".equals(sYear) || "null".equals(sSemester) || TextUtils.isEmpty(sYear) || TextUtils.isEmpty(sSemester) ) {
                    Toast.makeText(StudentAssignActivity.this,"Please fill required fields",Toast.LENGTH_SHORT).show();

                }
                else {
                    loadStudent();
                    activateModifyButton();
                    alertDialog.dismiss();
                }


            }
        });


        alertDialog.show();





    }

    public void activateModifyButton() {
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StudentAssignActivity.this);

                View view = LayoutInflater.from(StudentAssignActivity.this).inflate(R.layout.custom_dialog_input_student, null);
                builder.setView(view);
                builder.setCancelable(true);
                final AlertDialog alertDialog = builder.create();

                final RadioGroup radioGroup=view.findViewById(R.id.radioGroup);
                final RadioButton range=view.findViewById(R.id.range);
                final RadioButton insert=view.findViewById(R.id.insert);
                final RadioButton delete=view.findViewById(R.id.delete);

                final EditText roll1 = view.findViewById(R.id.roll1);
                final EditText roll2 = view.findViewById(R.id.roll2);
                final Button proceed = view.findViewById(R.id.proceed);

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId==R.id.range) {
                            roll2.setVisibility(View.VISIBLE);

                            proceed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressDialog.setMessage("Please wait...");
                                    progressDialog.show();

                                    final String sRoll1,sRoll2;
                                    sRoll1=roll1.getText().toString().trim();
                                    sRoll2=roll2.getText().toString().trim();


                                    if (!TextUtils.isEmpty(sRoll1) && !TextUtils.isEmpty(sRoll2)) {
                                        Map<String,Object> setStudent= new HashMap<>();
                                        setStudent.put("timeStamp",System.currentTimeMillis());

                                        for (int roll=Integer.parseInt(sRoll1);roll<=Integer.parseInt(sRoll2);roll++) {
                                            final int finalRoll = roll;
                                            firebaseFirestore.collection("university").document("just")
                                                    .collection("a")
                                                    .document("cse")
                                                    .collection(sYear)
                                                    .document(sSemester)
                                                    .collection("student")
                                                    .document(String.valueOf(roll))
                                                    .set(setStudent)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (String.valueOf(finalRoll).equals(sRoll2)) {
                                                                Toast.makeText(StudentAssignActivity.this,"Added Successfully",Toast.LENGTH_SHORT).show();
                                                                loadStudent();
                                                                alertDialog.dismiss();
                                                            }

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(StudentAssignActivity.this,"Error occured",Toast.LENGTH_SHORT).show();
                                                            progressDialog.dismiss();
                                                            alertDialog.dismiss();
                                                        }
                                                    })
                                            ;
                                        }


                                    }
                                    else {
                                        Toast.makeText(StudentAssignActivity.this,"Roll range is invalid",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        alertDialog.dismiss();
                                    }



                                }
                            });
                        }
                        else if (checkedId==R.id.insert) {
                            roll2.setVisibility(View.GONE);

                            proceed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressDialog.setMessage("Please wait...");
                                    progressDialog.show();

                                    final String sRoll1;
                                    sRoll1=roll1.getText().toString().trim();

                                    if (!TextUtils.isEmpty(sRoll1)) {
                                        Map<String,Object> setStudent= new HashMap<>();
                                        setStudent.put("timeStamp",System.currentTimeMillis());

                                        firebaseFirestore.collection("university").document("just")
                                                .collection("a")
                                                .document("cse")
                                                .collection(sYear)
                                                .document(sSemester)
                                                .collection("student")
                                                .document(String.valueOf(sRoll1))
                                                .set(setStudent)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        Toast.makeText(StudentAssignActivity.this,"Added Successfully",Toast.LENGTH_SHORT).show();
                                                        loadStudent();
                                                        alertDialog.dismiss();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(StudentAssignActivity.this,"Error occured",Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                        alertDialog.dismiss();
                                                    }
                                                })
                                        ;

                                    }
                                    else {
                                        Toast.makeText(StudentAssignActivity.this,"Roll range is invalid",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        alertDialog.dismiss();
                                    }



                                }
                            });
                        }
                        else if (checkedId==R.id.delete) {
                            roll2.setVisibility(View.GONE);
                            proceed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressDialog.setMessage("Please wait...");
                                    progressDialog.show();

                                    final String sRoll1;
                                    sRoll1=roll1.getText().toString().trim();

                                    if (!TextUtils.isEmpty(sRoll1)) {

                                        firebaseFirestore.collection("university").document("just")
                                                .collection("a")
                                                .document("cse")
                                                .collection(sYear)
                                                .document(sSemester)
                                                .collection("student")
                                                .document(String.valueOf(sRoll1))
                                                .delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        Toast.makeText(StudentAssignActivity.this,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                                                        loadStudent();
                                                        alertDialog.dismiss();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(StudentAssignActivity.this,"Error occured",Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                        alertDialog.dismiss();
                                                    }
                                                })
                                        ;

                                    }
                                    else {
                                        Toast.makeText(StudentAssignActivity.this,"Roll range is invalid",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        alertDialog.dismiss();
                                    }



                                }
                            });
                        }
                    }
                });






                alertDialog.show();
            }
        });
    }


    public void loadStudent() {

        sRoll= new ArrayList<>();

        firebaseFirestore.collection("university").document("just")
                .collection("a")
                .document("cse")
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
                            studentRecyclerAdapter= new StudentRecyclerAdapter(StudentAssignActivity.this,sRoll);
                            mRecyclerView.setAdapter(studentRecyclerAdapter);
                            progressDialog.dismiss();
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
