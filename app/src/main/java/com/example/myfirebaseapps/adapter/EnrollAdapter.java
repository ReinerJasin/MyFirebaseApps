package com.example.myfirebaseapps.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirebaseapps.Model.Course;
import com.example.myfirebaseapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EnrollAdapter extends RecyclerView.Adapter<EnrollAdapter.CardViewViewHolder> {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private Context context;

    private ArrayList<Course> listCourse;

    boolean timeConflict = false;

    public ArrayList<Course> getListCourse() {
        return listCourse;
    }

    public void setListCourse(ArrayList<Course> listCourse) {
        this.listCourse = listCourse;
    }

    public EnrollAdapter(final Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public EnrollAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.enroll_adapter, parent, false);
        return new EnrollAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull EnrollAdapter.CardViewViewHolder holder, int position) {

        final Course course = getListCourse().get(position);

        holder.enroll_course.setText(course.getSubject());
        holder.enroll_lecturer.setText(course.getLecturer());
        holder.enroll_day.setText(course.getDay());
        holder.enroll_time.setText(course.getStart());
        holder.enroll_time_end.setText(course.getEnd());

        holder.enroll_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConflictCheck(course);
                Toast.makeText(context, "Masuk", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListCourse().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {

        TextView enroll_course, enroll_lecturer, enroll_day, enroll_time, enroll_time_end;
        ImageButton enroll_button;

        public CardViewViewHolder(View itemView) {
            super(itemView);

            enroll_course = itemView.findViewById(R.id.enroll_course);
            enroll_lecturer = itemView.findViewById(R.id.enroll_lecturer);
            enroll_day = itemView.findViewById(R.id.enroll_day);
            enroll_time = itemView.findViewById(R.id.enroll_time);
            enroll_time_end = itemView.findViewById(R.id.enroll_time_end);
            enroll_button = itemView.findViewById(R.id.enroll_imageButton_add);

        }
    }

    MutableLiveData<Course> addCourse = new MutableLiveData<>();

    public MutableLiveData<Course> getAddCourse() {
        return addCourse;
    }

    public void ConflictCheck(final Course course_temp) {

        final int course_temp_time = Integer.parseInt(course_temp.getStart().replace(":", ""));
        final int course_temp_time_end = Integer.parseInt(course_temp.getEnd().replace(":", ""));
        final String course_temp_day = course_temp.getDay();

        mDatabase.child("student").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("course").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timeConflict = false;

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Course course = childSnapshot.getValue(Course.class);

                    String course_day = course.getDay();
                    int course_time = Integer.parseInt(course.getStart().replace(":", ""));
                    int course_time_end = Integer.parseInt(course.getEnd().replace(":", ""));

                    //ngecek kalau jadwal berada di hari yang sama
                    if (course_day.equalsIgnoreCase(course_temp_day)) {

                        //ngecek kalau jam mulai berada dalam range waktu yang sudah diambil
                        if (course_temp_time >= course_time && course_temp_time <= course_time_end) {

                            //ngecek kalau jam selesai berada dalam range waktu yang sudah diambil
                            if (course_temp_time_end >= course_time && course_temp_time_end <= course_time_end) {
                                timeConflict = true;
                            }

                        }

                    }

                }

                if (timeConflict == true) {
                    Toast.makeText(context, "Time conflict detected!", Toast.LENGTH_SHORT).show();
                } else {
                    addCourse.setValue(course_temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
