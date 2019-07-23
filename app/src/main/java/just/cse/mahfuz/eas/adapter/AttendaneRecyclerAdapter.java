package just.cse.mahfuz.eas.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import just.cse.mahfuz.eas.R;

public class AttendaneRecyclerAdapter extends RecyclerView.Adapter<AttendaneRecyclerAdapter.AttendanceViewHolder> {

    Context context;
    List<String> sRoll;
    private SparseBooleanArray itemStateArray= new SparseBooleanArray();

    public AttendaneRecyclerAdapter() {
    }

    public AttendaneRecyclerAdapter(Context context, List<String> sRoll) {
        this.context = context;
        this.sRoll = sRoll;

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
            attendanceViewHolder.serial.setText("0"+(i+1)+".");
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
                    Toast.makeText(context, "Uploaded successfully", Toast.LENGTH_LONG).show();
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
}
