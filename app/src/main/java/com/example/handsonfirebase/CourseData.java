package com.example.handsonfirebase;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handsonfirebase.adapter.CourseAdapter;
import com.example.handsonfirebase.adapter.LecturerAdapter;
import com.example.handsonfirebase.model.Course;
import com.example.handsonfirebase.model.Lecturer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseData extends AppCompatActivity {

    Toolbar toolbar;
    DatabaseReference dbCourse;
    ArrayList<Course> listCourse = new ArrayList<>();
    RecyclerView rv_course_data;
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_recycler);
        toolbar = findViewById(R.id.toolbar_course_recycler);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rv_course_data = findViewById(R.id.rv_course);
        dbCourse = FirebaseDatabase.getInstance().getReference("course");

        fetchCourseData();
    }

    public void fetchCourseData(){
        dbCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCourse.clear();
                rv_course_data.setAdapter(null);
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
        rv_course_data.setLayoutManager(new LinearLayoutManager(CourseData.this));
        CourseAdapter courseAdapter = new CourseAdapter(CourseData.this);
        courseAdapter.setListCourse(list);
        rv_course_data.setAdapter(courseAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(CourseData.this, CourseActivity.class);
            intent.putExtra("action", "add");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CourseData.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
