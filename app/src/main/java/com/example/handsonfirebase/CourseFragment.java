package com.example.handsonfirebase;

import android.content.Intent;
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
import android.widget.Toolbar;

import com.example.handsonfirebase.adapter.CourseAdapter;
import com.example.handsonfirebase.adapter.CourseFragmentAdapter;
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


public class CourseFragment extends Fragment {

    DatabaseReference dbStudent;
    DatabaseReference dbCourse;
    Student student;
    private FirebaseAuth mAuth;
    String userKey;
    FirebaseAuth firebaseAuth;
    ArrayList<Course> listCourse = new ArrayList<>();
    Button button;
    @BindView(R.id.progressBar2)
    ProgressBar loading;
    @BindView(R.id.rv_course_fragment)
    RecyclerView rv_course_fragment;

    public CourseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course, container, false);
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


        dbCourse = FirebaseDatabase.getInstance().getReference("course");

        fetchCourseData();

    }

    public void fetchCourseData(){
        dbCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCourse.clear();
                rv_course_fragment.setAdapter(null);
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
        rv_course_fragment.setLayoutManager(new LinearLayoutManager(getActivity()));
        CourseFragmentAdapter courseFragmentAdapter = new CourseFragmentAdapter(getActivity());
        courseFragmentAdapter.setListCourse(list);
        rv_course_fragment.setAdapter(courseFragmentAdapter);
    }

    private void showLoading(Boolean state) {
        if (state) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }

}