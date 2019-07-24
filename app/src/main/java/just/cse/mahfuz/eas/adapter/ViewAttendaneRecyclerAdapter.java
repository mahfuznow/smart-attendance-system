package just.cse.mahfuz.eas.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import just.cse.mahfuz.eas.R;

public class ViewAttendaneRecyclerAdapter extends RecyclerView.Adapter<ViewAttendaneRecyclerAdapter.AttendanceViewHolder> {

    Context context;
    List<String> sRoll;
    String sYear,sSemester,sCourseID,sDate;
    private SparseBooleanArray itemStateArray= new SparseBooleanArray();

   FirebaseFirestore firebaseFirestore;
   ProgressDialog progressDialog;



    public ViewAttendaneRecyclerAdapter() {
    }

    public ViewAttendaneRecyclerAdapter(Context context, List<String> sRoll, String sYear, String sSemester, String sCourseID, String sDate, SparseBooleanArray itemStateArray) {
        this.context = context;
        this.sRoll = sRoll;
        this.sYear=sYear;
        this.sSemester=sSemester;
        this.sCourseID=sCourseID;
        this.sDate=sDate;
        this.itemStateArray=itemStateArray;
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(context);

    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_attendance, viewGroup, false);
        return new AttendanceViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceViewHolder attendanceViewHolder, final int i) {


        attendanceViewHolder.checkBox.setEnabled(false);

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




    }

    @Override
    public int getItemCount() {
        return sRoll.size();
    }


    public class AttendanceViewHolder extends RecyclerView.ViewHolder{
        TextView serial,roll;
        CheckBox checkBox;


        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);

            serial=itemView.findViewById(R.id.serial);
            roll=itemView.findViewById(R.id.roll);
            checkBox=itemView.findViewById(R.id.checkBox);

//            this.setIsRecyclable(false);
        }
    }





}
