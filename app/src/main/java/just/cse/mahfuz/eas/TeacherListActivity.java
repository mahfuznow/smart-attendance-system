package just.cse.mahfuz.eas;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import just.cse.mahfuz.eas.adapter.TeacherRecyclerAdapter;
import just.cse.mahfuz.eas.model.Teacher;

public class TeacherListActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    FirebaseFirestore firebaseFirestore;

    ProgressDialog progressDialog;

    List<Teacher> teacherModel;

    TeacherRecyclerAdapter teacherRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Assign Course");
        actionBar.setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(TeacherListActivity.this);

        progressDialog.setMessage("Loading..");
        progressDialog.show();

        teacherModel= new ArrayList<>();

        firebaseFirestore.collection("university").document("just")
                .collection("a")
                .document("cse")
                .collection("teacher")
                .orderBy("serial", Query.Direction.ASCENDING)
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
                                    teacherModel=task.getResult().toObjects(Teacher.class);
                                    teacherRecycleAdapter = new TeacherRecyclerAdapter(TeacherListActivity.this, teacherModel);
                                    mRecyclerView.setAdapter(teacherRecycleAdapter);
                                    progressDialog.dismiss();
                                }
                            }
                        } else {
                            Log.e("Error", "Error getting documents: ", task.getException());
                            progressDialog.dismiss();

                        }
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

