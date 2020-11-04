package com.example.handsonfirebase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.handsonfirebase.adapter.CourseFragmentAdapter;
import com.example.handsonfirebase.adapter.ScheduleFragmentAdapter;
import com.example.handsonfirebase.model.Course;
import com.example.handsonfirebase.model.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleFragment extends Fragment {

    DatabaseReference dbStudent;
    DatabaseReference dbCourse;
    Student student;
    private FirebaseAuth mAuth;
    String userKey;
    FirebaseAuth firebaseAuth;
    ArrayList<Course> listCourse = new ArrayList<>();
    Button button;
    @BindView(R.id.progressBar3)
    ProgressBar loading;
    @BindView(R.id.rv_schedule_fragment)
    RecyclerView rv_schedule_fragment;

    public ScheduleFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(ScheduleFragment.this, view);

        showLoading(true);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userKey = user.getUid();
        dbStudent = FirebaseDatabase.getInstance().getReference("student").child(userKey);


        dbCourse = FirebaseDatabase.getInstance().getReference("course");

        fetchScheduleData();
    }

    public void fetchScheduleData(){
        dbStudent.child("course").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCourse.clear();
                rv_schedule_fragment.setAdapter(null);
                for (DataSnapshot childSnapshot : snapshot.getChildren()){
                    Course course = childSnapshot.getValue(Course.class);
                    listCourse.add(course);

                }
                showCourseData(listCourse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showCourseData(final ArrayList<Course> list){
        showLoading(false);
        rv_schedule_fragment.setLayoutManager(new LinearLayoutManager(getActivity()));
        ScheduleFragmentAdapter scheduleFragmentAdapter = new ScheduleFragmentAdapter(getActivity());
        scheduleFragmentAdapter.setListSchedule(list);
        rv_schedule_fragment.setAdapter(scheduleFragmentAdapter);
    }

    private void showLoading(Boolean state) {
        if (state) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }
}