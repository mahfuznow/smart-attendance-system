package just.cse.mahfuz.eas.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import just.cse.mahfuz.eas.R;

public class AttendaneRecyclerAdapter extends RecyclerView.Adapter<AttendaneRecyclerAdapter.AttendanceViewHolder> {

    Context context;
    List<String> sRoll;
    String sYear,sSemester,sCourseID,sDate;
    String myunit, mydepartment;
    private SparseBooleanArray itemStateArray= new SparseBooleanArray();

   FirebaseFirestore firebaseFirestore;
   AlertDialog progressDialog;



    public AttendaneRecyclerAdapter() {
    }

    public AttendaneRecyclerAdapter(Context context, List<String> sRoll,String sYear,String sSemester,String sCourseID,String sDate,String myunit, String mydepartment) {
        this.context = context;
        this.sRoll = sRoll;
        this.sYear=sYear;
        this.sSemester=sSemester;
        this.sCourseID=sCourseID;
        this.sDate=sDate;
        this.myunit=myunit;
        this.mydepartment=mydepartment;
        firebaseFirestore = FirebaseFirestore.getInstance();
        //progressDialog=new ProgressDialog(context);
        //custom progress dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        View view1 = LayoutInflater.from(context).inflate(R.layout.custom_dialog_loading, null);
        builder.setView(view1);
        builder.setCancelable(true);
        progressDialog = builder.create();

    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
//        View view = inflater.inflate(R.layout.row_attendance, viewGroup, false);
//        return new AttendanceViewHolder(view);

        View view;

        if(i == R.layout.row_attendance){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_attendance, viewGroup, false);
        }

        else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.submit_button_layout, viewGroup, false);
        }

        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceViewHolder attendanceViewHolder, final int i) {


        if (i<sRoll.size()) {
            if (i<9) {
                attendanceViewHolder.serial.setText("0"+(i+1)+".");
            }
            else {
                attendanceViewHolder.serial.setText((i+1)+".");
            }

            attendanceViewHolder.roll.setText(sRoll.get(i));

            if (!itemStateArray.get(i, false)) {
                attendanceViewHolder.checkBox.setChecked(false);}
            else {
                attendanceViewHolder.checkBox.setChecked(true);
            }


            attendanceViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = i;
                    if (!itemStateArray.get(adapterPosition, false)) {
                        attendanceViewHolder.checkBox.setChecked(true);
                        itemStateArray.put(adapterPosition, true);
                    }
                    else  {
                        attendanceViewHolder.checkBox.setChecked(false);
                        itemStateArray.put(adapterPosition, false);
                    }
                }
            });
        }

//
//        if (i==sRoll.size()) {
//            attendanceViewHolder.serial.setVisibility(View.GONE);
//            attendanceViewHolder.checkBox.setVisibility(View.GONE);
//            attendanceViewHolder.roll.setText("Submit");
////            attendanceViewHolder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
////                            LinearLayout.LayoutParams.WRAP_CONTENT));
//            attendanceViewHolder.roll.setBackgroundResource(R.color.colorOrangeDark);
//            attendanceViewHolder.roll.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }


        //this is for bottom button
        if(i == sRoll.size()) {
            attendanceViewHolder.submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, "Uploaded successfully", Toast.LENGTH_LONG).show();
                   upload();
                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return sRoll.size()+1;
    }


    public class AttendanceViewHolder extends RecyclerView.ViewHolder{
        TextView serial,roll;
        CheckBox checkBox;
        Button submit;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);

            serial=itemView.findViewById(R.id.serial);
            roll=itemView.findViewById(R.id.roll);
            checkBox=itemView.findViewById(R.id.checkBox);
            submit=itemView.findViewById(R.id.submit);

//            this.setIsRecyclable(false);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return (position == sRoll.size()) ? R.layout.submit_button_layout : R.layout.row_attendance;
    }


    public void upload() {

        //progressDialog.setMessage("Submiting..");
        progressDialog.show();


        for (int i=0;i<sRoll.size();i++) {

            Map<String,Boolean> attendance= new HashMap<>();
            attendance.put("attendance",itemStateArray.get(i,false));

            final int finalI = i;
            firebaseFirestore.collection("university").document("just")
                    .collection(myunit)
                    .document(mydepartment)
                    .collection(sYear)
                    .document(sSemester)
                    .collection("course")
                    .document(sCourseID)
                    .collection(sRoll.get(i))
                    .document(sDate).set(attendance)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (finalI ==sRoll.size()-1) {
                                progressDialog.dismiss();
                                Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show();
                                ((Activity)context).finish();
                            }
                        }
                    })
            ;
        }


    }

}
