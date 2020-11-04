package com.example.myfirebaseapps.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.Observer;

import com.example.myfirebaseapps.Model.Course;
import com.example.myfirebaseapps.R;
import com.example.myfirebaseapps.adapter.EnrollAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseFragment extends Fragment {

    DatabaseReference dbCourse;
    ArrayList<Course> listCourse = new ArrayList<>();
    RecyclerView rv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        dbCourse = FirebaseDatabase.getInstance().getReference("course");
        rv = view.findViewById(R.id.rv_fragmentCourse);

        dbCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCourse.clear();
                rv.setAdapter(null);

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Course course = childSnapshot.getValue(Course.class);
                    listCourse.add(course);
                }

                showData(listCourse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void showData(ArrayList<Course> listCourse) {

        rv.setLayoutManager(new LinearLayoutManager(CourseFragment.this.getActivity()));
        EnrollAdapter enrollAdapter = new EnrollAdapter(CourseFragment.this.getActivity());
        enrollAdapter.setListCourse(listCourse);
        rv.setAdapter(enrollAdapter);

        final Observer<Course> courseObserver = new Observer<Course>() {
            @Override
            public void onChanged(Course course) {
                FirebaseDatabase.getInstance().getReference().child("student").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("course").child(course.getId()).setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Add Course Success!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Add Course Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        enrollAdapter.getAddCourse().observe(this, courseObserver);
    }
}
