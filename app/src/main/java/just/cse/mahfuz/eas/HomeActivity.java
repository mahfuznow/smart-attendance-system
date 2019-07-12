package just.cse.mahfuz.eas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {
    Button attendance,viewAttendance,CTmark,viewCTmark,courseAssign,editCourseAssigned;

    FirebaseFirestore firebaseFirestore;
    String mycategory, uid;
    FirebaseAuth auth;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        attendance = findViewById(R.id.attendance);
        viewAttendance = findViewById(R.id.viewAttendance);

        CTmark = findViewById(R.id.CTmark);
        viewCTmark = findViewById(R.id.viewCTmark);

        courseAssign = findViewById(R.id.courseAssign);
        editCourseAssigned = findViewById(R.id.editCourseAssigned);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        uid = auth.getUid();


        progressDialog = new ProgressDialog(HomeActivity.this);

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        firebaseFirestore.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                mycategory=task.getResult().getString("category");

                if ("admin".equals(mycategory)) {
                    attendance.setVisibility(View.GONE);
                    viewAttendance.setVisibility(View.GONE);
                    CTmark.setVisibility(View.GONE);
                    viewCTmark.setVisibility(View.GONE);
                    courseAssign.setVisibility(View.VISIBLE);
                    editCourseAssigned.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
                else if ("teacher".equals(mycategory)) {

                    attendance.setVisibility(View.VISIBLE);
                    viewAttendance.setVisibility(View.VISIBLE);
                    CTmark.setVisibility(View.VISIBLE);
                    viewCTmark.setVisibility(View.VISIBLE);
                    courseAssign.setVisibility(View.GONE);
                    editCourseAssigned.setVisibility(View.GONE);
                    progressDialog.dismiss();

                    progressDialog.dismiss();
                }
                else if ("student".equals(mycategory)) {

                    attendance.setVisibility(View.GONE);
                    viewAttendance.setVisibility(View.VISIBLE);
                    CTmark.setVisibility(View.GONE);
                    viewCTmark.setVisibility(View.VISIBLE);
                    courseAssign.setVisibility(View.GONE);
                    editCourseAssigned.setVisibility(View.GONE);
                    progressDialog.dismiss();

                    progressDialog.dismiss();

                }

            }
        });


        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,Attendance.class));
            }
        });
        courseAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,TeacherListActivity.class));
            }
        });



    }
    //setting doted menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/developer?id=Mahfuzur+Rahman");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent,"Share play store link via"));

            return true;
        }
        if (id == R.id.rate) {
            String appPackageName=getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=")));
            }
            catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/apps/details?id="+appPackageName)));
            }
            return true;
        }

        if (id==android.R.id.home) {
            finish();
        }
        if (id==R.id.logout) {
            progressDialog.setMessage("logging Out ...");
            progressDialog.show();
            auth.signOut();
            progressDialog.dismiss();
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

}