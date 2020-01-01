package just.cse.mahfuz.eas.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import just.cse.mahfuz.eas.R;

public class LoginActivity extends AppCompatActivity {


    CircleImageView img;
    Button login,reset;
    EditText emailL, passL,emailF;
    TextView forgot,backToLogin;
    String myemailL, mypassL,myemailF;

    LinearLayout loginForm, forgetForm;

    ProgressDialog dialog;
    FirebaseAuth mauth;




    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mauth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        img = findViewById(R.id.img);
        login = findViewById(R.id.login);
        reset= findViewById(R.id.reset);
        emailL = findViewById(R.id.etEmailL);
        emailF= findViewById(R.id.etEmailF);
        passL = findViewById(R.id.etPassL);

        forgot = findViewById(R.id.forgot);
        backToLogin = findViewById(R.id.backToLogin);

        loginForm= findViewById(R.id.loginFormL);
        forgetForm = findViewById(R.id.forgetFormF);


        //animateImg();

        dialog = new ProgressDialog(LoginActivity.this);

        FirebaseApp.initializeApp(LoginActivity.this);
        mauth = FirebaseAuth.getInstance();

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginForm.setVisibility(View.GONE);
                forgetForm.setVisibility(View.VISIBLE);
            }
        });
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginForm.setVisibility(View.VISIBLE);
                forgetForm.setVisibility(View.GONE);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.setMessage("logging In ...");
                dialog.show();

                myemailL = emailL.getText().toString().trim();
                mypassL = passL.getText().toString().trim();


                if ((!TextUtils.isEmpty(myemailL)) && (!TextUtils.isEmpty(mypassL))) {

                    mauth.signInWithEmailAndPassword(myemailL, mypassL).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);

                            } else if (!isNetworkAvailable()) {
                                Toast.makeText(LoginActivity.this, "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(LoginActivity.this, "LogIn failed. Please input correct Email & password", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Please input both Email & password", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });




        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("Resetting password ...");
                dialog.show();
                String myemailF = emailL.getText().toString().trim();
                if ((!TextUtils.isEmpty(myemailF))) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(myemailF)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Please check your emailL, we have sent a link to reset your password", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        loginForm.setVisibility(View.VISIBLE);
                                        forgetForm.setVisibility(View.GONE);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "An Error occured", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this, "Please input Email", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });


    }

    public void animateImg() {
        img.animate().translationY(-500).alphaBy(0).setDuration(1000);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(LoginActivity.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to exit?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                moveTaskToBack(true);
                finish();
                // android.os.Process.killProcess(android.os.Process.myPid());
                // System.exit(1);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        //  super.onBackPressed();
    }

}
