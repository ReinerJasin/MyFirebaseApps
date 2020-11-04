package com.example.myfirebaseapps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirebaseapps.Model.Lecturer;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LecturerDetail extends AppCompatActivity {

    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Toolbar bar;
    DatabaseReference dbLecturer;
    int pos = 0;
    TextView lbl_name, lbl_gender, lbl_expertise;
    Lecturer lecturer;
    ImageView btn_edit, btn_del;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_detail);

        bar = findViewById(R.id.toolbar_lecturer_detail);
        lbl_name = findViewById(R.id.lbl_nama);
        lbl_gender = findViewById(R.id.lbl_gender);
        lbl_expertise = findViewById(R.id.lbl_expertise);
        btn_edit = findViewById(R.id.btn_edit);
        btn_del = findViewById(R.id.btn_delete);

        dbLecturer = FirebaseDatabase.getInstance().getReference("lecturer");

        dialog = Glovar.loadingDialog(LecturerDetail.this);

        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        pos = intent.getIntExtra("position", 0);
        lecturer = intent.getParcelableExtra("data_lecturer");

        lbl_name.setText(lecturer.getName());
        lbl_gender.setText(lecturer.getGender());
        lbl_expertise.setText(lecturer.getExpertise());

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                new AlertDialog.Builder(LecturerDetail.this)
                        .setTitle("Konfirmasi")
                        .setIcon(R.drawable.ic_baseline_android_24)
                        .setMessage("Are you sure to delete " + lecturer.getName() + " data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();
                                        dbLecturer.child(lecturer.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                Intent in = new Intent(LecturerDetail.this, LecturerData.class);
                                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                Toast.makeText(LecturerDetail.this, "Delete success!", Toast.LENGTH_SHORT).show();
                                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerDetail.this);
                                                startActivity(in, options.toBundle());
                                                finish();
                                                dialogInterface.cancel();
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
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                Intent in = new Intent(LecturerDetail.this, AddLecturer.class);
                in.putExtra("action", "edit");
                in.putExtra("edit_data_lect", lecturer);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerDetail.this);
                startActivity(in, options.toBundle());
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent;
            intent = new Intent(LecturerDetail.this, LecturerData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerDetail.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(LecturerDetail.this, LecturerData.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerDetail.this);
        startActivity(intent, options.toBundle());
        finish();
    }
}