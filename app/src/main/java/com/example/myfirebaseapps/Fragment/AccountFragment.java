package com.example.myfirebaseapps.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfirebaseapps.Model.Student;
import com.example.myfirebaseapps.R;
import com.example.myfirebaseapps.Starter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    TextView frag_nama, frag_nim, frag_email, frag_gender, frag_age, frag_address;
    Button button_logout;
    FirebaseUser fUser;
    DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        frag_nama = view.findViewById(R.id.frag_nama);
        frag_nim = view.findViewById(R.id.frag_nim);
        frag_email = view.findViewById(R.id.frag_email);
        frag_gender = view.findViewById(R.id.frag_gender);
        frag_age = view.findViewById(R.id.frag_age);
        frag_address = view.findViewById(R.id.frag_address);

        button_logout = view.findViewById(R.id.button_logout);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("student").child(fUser.getUid());

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Starter.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Student student = snapshot.getValue(Student.class);
                frag_nama.setText(student.getName());
                frag_nim.setText("NIM : " + student.getNim());
                frag_email.setText("Email : " + student.getEmail());
                frag_gender.setText("Gender : " + student.getGender());
                frag_age.setText("Age : " + student.getAge() + "yo");
                frag_address.setText("Address : \n" + student.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}
