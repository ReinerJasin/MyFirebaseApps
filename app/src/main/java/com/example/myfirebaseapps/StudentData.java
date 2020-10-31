package com.example.myfirebaseapps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myfirebaseapps.Model.Student;
import com.example.myfirebaseapps.R;
import com.example.myfirebaseapps.adapter.LecturerAdapter;
import com.example.myfirebaseapps.adapter.StudentAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentData extends AppCompatActivity {

    Toolbar toolbar;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    ArrayList<Student> listStudent = new ArrayList<>();
    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;
    StudentAdapter adapter;
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    String action = "";
    Student student;
    Dialog dialog;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_data);
        toolbar = findViewById(R.id.toolbar_studentData);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("student");
        rv = findViewById(R.id.rv_stu_data);
        dialog = Glovar.loadingDialog(StudentData.this);

        fetchStudentData();

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        student = intent.getParcelableExtra("data_student");
        action = intent.getStringExtra("action");

        if (action.equalsIgnoreCase("delete")) {

            new AlertDialog.Builder(StudentData.this)
                    .setTitle("Konfirmasi")
                    .setIcon(R.drawable.ic_baseline_android_24)
                    .setMessage("Are you sure to delete " + student.getName() + " data?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {

                            dialog.show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.cancel();
                                    mAuth.signInWithEmailAndPassword(student.getEmail(), student.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            mAuth.getCurrentUser().delete();
                                            databaseReference.child(listStudent.get(position).getUid()).removeValue(new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                    Intent intent = new Intent(StudentData.this, StudentData.class);
                                                    intent.putExtra("action", "none");
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    Toast.makeText(StudentData.this, "Delete Success!", Toast.LENGTH_SHORT).show();
                                                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentData.this);
                                                    startActivity(intent, options.toBundle());
                                                    finish();
                                                    dialogInterface.cancel();
                                                }
                                            });
                                        }
                                    });

                                }
                            }, 2000);
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

//        layoutManager = new LinearLayoutManager(this);
//        rv.setLayoutManager(layoutManager);

        }

        //findViewById
        toolbar = findViewById(R.id.toolbar_studentData);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentData.this, Starter.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void fetchStudentData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listStudent.clear();
                rv.setAdapter(null);
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Student student = childSnapshot.getValue(Student.class);
                    listStudent.add(student);
                }
                showStudentData(listStudent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showStudentData(final ArrayList<Student> list) {
        rv.setLayoutManager(new LinearLayoutManager(StudentData.this));
        StudentAdapter studentAdapter = new StudentAdapter(StudentData.this);
        studentAdapter.setListStudent(list);
        rv.setAdapter(studentAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(StudentData.this, StudentRegister.class);
        intent.putExtra("action", "add");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentData.this);
        startActivity(intent, options.toBundle());
        finish();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.student_list) {
//            Intent intent;
//            intent = new Intent(StudentData.this, AddLecturer.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentData.this);
//            startActivity(intent, options.toBundle());
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}