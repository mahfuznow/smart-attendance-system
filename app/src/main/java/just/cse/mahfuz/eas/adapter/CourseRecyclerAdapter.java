package just.cse.mahfuz.eas.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import just.cse.mahfuz.eas.AttendanceActivityNew;
import just.cse.mahfuz.eas.R;
import just.cse.mahfuz.eas.model.Course;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.courseViewHolder> {

    Context context;
    List<Course> courseList;

    String courseID,courseName,teacher;
    String type;
    public CourseRecyclerAdapter() {
    }

    public CourseRecyclerAdapter(Context context, List<Course> courseList,String type) {
        this.context = context;
        this.courseList = courseList;
        this.type = type;

    }

    @NonNull
    @Override
    public courseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_course, viewGroup, false);
        return new courseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final courseViewHolder courseViewHolder, int i) {
        courseID= courseList.get(i).getCourseID();
        courseName= courseList.get(i).getCourseName();
        teacher= courseList.get(i).getTeacher();


        courseViewHolder.courseID.setText(courseID);
        courseViewHolder.courseName.setText(courseName);
        courseViewHolder.teacher.setText(teacher);

        if ("selectt".equals(type)) {


            courseViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context, AttendanceActivityNew.class);
                    intent.putExtra("courseID",courseID);
                    intent.putExtra("courseName",courseName);
                    intent.putExtra("type","t");
                    context.startActivity(intent);
                }
            });
        }
        else if ("selectv".equals(type)) {
            courseViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context, AttendanceActivityNew.class);
                    intent.putExtra("courseID",courseID);
                    intent.putExtra("courseName",courseName);
                    intent.putExtra("type","v");
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class courseViewHolder extends RecyclerView.ViewHolder{

        TextView courseName,courseID,teacher;
        LinearLayout linearLayout;

        public courseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseID =itemView.findViewById(R.id.courseID);
            courseName=itemView.findViewById(R.id.courseName);
            teacher=itemView.findViewById(R.id.teacher);
            linearLayout=itemView.findViewById(R.id.linearLayout);
        }
    }
}
