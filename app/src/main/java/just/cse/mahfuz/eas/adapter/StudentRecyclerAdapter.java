package just.cse.mahfuz.eas.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import just.cse.mahfuz.eas.R;

public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentRecyclerAdapter.StudentViewHolder> {

    Context context;
    List<String> sRoll;



    public StudentRecyclerAdapter() {
    }

    public StudentRecyclerAdapter(Context context, List<String> sRoll) {
        this.context = context;
        this.sRoll = sRoll;

    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_attendance, viewGroup, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentViewHolder studentViewHolder, final int i) {

        if (i<9) {
            studentViewHolder.serial.setText("0"+(i+1)+".");
        }
        else {
            studentViewHolder.serial.setText((i+1)+".");
        }

        studentViewHolder.roll.setText(sRoll.get(i));
        studentViewHolder.checkBox.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return sRoll.size();
    }


    public class StudentViewHolder extends RecyclerView.ViewHolder{
        TextView serial,roll;
        CheckBox checkBox;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            serial=itemView.findViewById(R.id.serial);
            roll=itemView.findViewById(R.id.roll);
            checkBox=itemView.findViewById(R.id.checkBox);
        }
    }



}
