package com.example.handsonfirebase;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.handsonfirebase.model.Course;
import com.example.handsonfirebase.model.Lecturer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseActivity extends AppCompatActivity implements TextWatcher {

    Button button_add_course;
    Toolbar bar;
    Dialog dialog;
    Spinner spinner_day, spinner_start, spinner_end, spinner_lecturer;
    String subject = "",day = "", start = "", end = "", lecturer = "", idcourse = "";
    String action = "";
    TextInputLayout input_course;
    Course course;
    ArrayList<String> listLecturer = new ArrayList<>();
    DatabaseReference dbLecturer;
    private DatabaseReference mDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_add);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        dialog = Glovar.loadingDialog(CourseActivity.this);
        bar = findViewById(R.id.toolbar_course_fragment);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        input_course = findViewById(R.id.input_course);
        input_course.getEditText().addTextChangedListener(this);

        dbLecturer = FirebaseDatabase.getInstance().getReference();

        spinner_day = findViewById(R.id.spinner_input_day);
        ArrayAdapter<CharSequence> adapterday = ArrayAdapter.createFromResource(CourseActivity.this,
                R.array.day, android.R.layout.simple_spinner_item);
        adapterday.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(adapterday);
        spinner_end = findViewById(R.id.spinner_input_time_end);
        spinner_start = findViewById(R.id.spinner_input_time_start);
        ArrayAdapter<CharSequence> adapterstart = ArrayAdapter.createFromResource(CourseActivity.this,
                R.array.jam_start_array, android.R.layout.simple_spinner_item);
        adapterstart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_start.setAdapter(adapterstart);

        spinner_lecturer = findViewById(R.id.spinner_input_lecturer);
        dbLecturer.child("lecturer").addValueEventListener(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                 for (DataSnapshot childSnapshot : snapshot.getChildren()){
                                                     String lecturerName = childSnapshot.child("name").getValue(String.class);
                                                     listLecturer.add(lecturerName);
                                                 }
                                                 ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CourseActivity.this, android.R.layout.simple_spinner_item, listLecturer);
                                                 arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                                                 spinner_lecturer.setAdapter(arrayAdapter);

                                             }

                                             @Override
                                             public void onCancelled(@NonNull DatabaseError error) {

                                             }
                                         });

        spinner_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> adapterend = null;
                if(position==0){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end0730, android.R.layout.simple_spinner_item);
                }else if(position==1){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end0800, android.R.layout.simple_spinner_item);
                }else if(position==2){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end0830, android.R.layout.simple_spinner_item);
                }else if(position==3){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end0900, android.R.layout.simple_spinner_item);
                }else if(position==4){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end0930, android.R.layout.simple_spinner_item);
                }else if(position==5){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end1000, android.R.layout.simple_spinner_item);
                }else if(position==6){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end1030, android.R.layout.simple_spinner_item);
                }else if(position==7){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end1100, android.R.layout.simple_spinner_item);
                }else if(position==8){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end1130, android.R.layout.simple_spinner_item);
                }else if(position==9){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end1200, android.R.layout.simple_spinner_item);
                }else if(position==10){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end1230, android.R.layout.simple_spinner_item);
                }else if(position==11){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end1300, android.R.layout.simple_spinner_item);
                }else if(position==12){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end1330, android.R.layout.simple_spinner_item);
                }else if(position==13){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end1400, android.R.layout.simple_spinner_item);
                }else if(position==14){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end1430, android.R.layout.simple_spinner_item);
                }else if(position==15){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end1500, android.R.layout.simple_spinner_item);
                }else if(position==16){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end1530, android.R.layout.simple_spinner_item);
                }else if(position==17){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end1600, android.R.layout.simple_spinner_item);
                }else if(position==18){
                    adapterend = ArrayAdapter.createFromResource(CourseActivity.this, R.array.jam_end1630, android.R.layout.simple_spinner_item);
                }

                adapterend.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_end.setAdapter(adapterend);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        button_add_course = findViewById(R.id.button_add_course);

        Intent intent =getIntent();
        action = intent.getStringExtra("action");
        if (action.equals("add")){
            button_add_course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subject = input_course.getEditText().getText().toString().trim();
                    day = spinner_day.getSelectedItem().toString();
                    end = spinner_end.getSelectedItem().toString();
                    start = spinner_start.getSelectedItem().toString();
                    lecturer = spinner_lecturer.getSelectedItem().toString();
                    addCourse(subject, day, start, end, lecturer);
                    finish();
                }
            });
        }else if(action.equalsIgnoreCase("edit")){
            getSupportActionBar().setTitle("Edit Course");
            course = intent.getParcelableExtra("edit_data_course");

            input_course.getEditText().setText(course.getName());

            spinner_day.setSelection(adapterday.getPosition(course.getDay()));
            spinner_start.setSelection(adapterstart.getPosition(course.getStart()));
//            spinner_lecturer.setSelection(arrayAdapter.getPosition(course.getLecturer()));

            button_add_course.setText("Edit Course");
            button_add_course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    subject = input_course.getEditText().getText().toString().trim();
                    day = spinner_day.getSelectedItem().toString();
                    end = spinner_end.getSelectedItem().toString();
                    start = spinner_start.getSelectedItem().toString();
                    lecturer = spinner_lecturer.getSelectedItem().toString();
                    Map<String, Object> params = new HashMap<>();
                    params.put("name", subject);
                    params.put("day", day);
                    params.put("end", end);
                    params.put("start", start);
                    params.put("lecturer", lecturer);
                    mDatabase.child("course").child(course.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.cancel();
                            Intent intent;
                            intent = new Intent(CourseActivity.this, CourseData.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CourseActivity.this);
                            startActivity(intent, options.toBundle());
                            finish();
                        }
                    });
                }
            });
        }
    }

    public void addCourse(String mcourse, String mday, String mstart, String mend, String mlecturer){
        String mid = mDatabase.child("course").push().getKey();
        Course course = new Course(mid, mcourse, mday, mstart, mend, mlecturer);
        mDatabase.child("course").child(mid).setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CourseActivity.this, "Add Course Succesfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CourseActivity.this, "Add Course Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(CourseActivity.this, StarterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CourseActivity.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }else if(id == R.id.course_list){
            Intent intent;
            intent = new Intent(CourseActivity.this, CourseData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CourseActivity.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(CourseActivity.this, StarterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CourseActivity.this);
        startActivity(intent, options.toBundle());
        finish();
    }

}
