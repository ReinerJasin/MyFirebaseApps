package com.example.myfirebaseapps.adapter;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirebaseapps.Glovar;
import com.example.myfirebaseapps.Model.Course;
import com.example.myfirebaseapps.R;
import com.example.myfirebaseapps.StudentData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.CardViewViewHolder> {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private Context context;

    Dialog dialog;

    private ArrayList<Course> listCourse;

    public ArrayList<Course> getListCourse() {
        return listCourse;
    }

    public void setListCourse(ArrayList<Course> listCourse) {
        this.listCourse = listCourse;
    }

    public ScheduleAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.enroll_adapter, parent, false);
        return new ScheduleAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder holder, int position) {
        final Course course = getListCourse().get(position);

        holder.enroll_course.setText(course.getSubject());
        holder.enroll_lecturer.setText(course.getLecturer());
        holder.enroll_day.setText(course.getDay());
        holder.enroll_time.setText(course.getStart());
        holder.enroll_time_end.setText(course.getEnd());

        holder.enroll_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = Glovar.loadingDialog(v.getContext());

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Konfirmasi")
                        .setIcon(R.drawable.ic_baseline_android_24)
                        .setMessage("Are you sure to delete " + course.getSubject() + " data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {

                                dialog.show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        mDatabase.child("student").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("course").child(course.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                                            }
                                        });

                                        dialog.cancel();
                                        Toast.makeText(context, "Delete Success!", Toast.LENGTH_SHORT).show();
                                    }
                                }, 1000);
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();

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

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);

            enroll_course = itemView.findViewById(R.id.enroll_course);
            enroll_lecturer = itemView.findViewById(R.id.enroll_lecturer);
            enroll_day = itemView.findViewById(R.id.enroll_day);
            enroll_time = itemView.findViewById(R.id.enroll_time);
            enroll_time_end = itemView.findViewById(R.id.enroll_time_end);
            enroll_button = itemView.findViewById(R.id.enroll_imageButton_add);

            enroll_button.setImageResource(R.drawable.ic_baseline_delete_24);

        }
    }
}
