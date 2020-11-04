package com.example.handsonfirebase;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.handsonfirebase.model.Lecturer;
import com.example.handsonfirebase.model.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserFragment extends Fragment {

    DatabaseReference dbStudent;
    Student student;
    private FirebaseAuth mAuth;
    String userKey;
    FirebaseAuth firebaseAuth;
    Button button;
    @BindView(R.id.progressBar)
    ProgressBar loading;
    @BindView(R.id.account_address)
    TextView account_address;
    @BindView(R.id.account_age)
    TextView account_age;
    @BindView(R.id.account_nim)
    TextView account_nim;
    @BindView(R.id.account_gender)
    TextView account_gender;
    @BindView(R.id.account_name)
    TextView account_name;
    @BindView(R.id.button_logout)
    Button button_logout;
    @BindView(R.id.button_edit_profile)
    Button button_edit_profile;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_user, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        showLoading(true);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userKey = user.getUid();
        dbStudent = FirebaseDatabase.getInstance().getReference("student").child(userKey);

        dbStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                showLoading(false);
                student = dataSnapshot.getValue(Student.class);

                account_name.setText(student.getName());
                account_nim.setText(student.getNim());
                account_age.setText(student.getAge());
                account_gender.setText(student.getGender());
                account_address.setText(student.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), StarterActivity.class);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

        button_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra("action", "login");
                intent.putExtra("edit_data_stud", student);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            account_address.setText("");
            account_age.setText("");
            account_nim.setText("");
            account_name.setText("");
            account_gender.setText("");
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }

}