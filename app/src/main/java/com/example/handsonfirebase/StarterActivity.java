package com.example.handsonfirebase;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StarterActivity extends AppCompatActivity {

    CardView cv_lecturer;
    CardView cv_register;
    CardView cv_course;
    CardView cv_login;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.starter_activity);

            cv_register = findViewById(R.id.cv_add_student);
            cv_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StarterActivity.this, RegisterActivity.class);
                    intent.putExtra("action", "add");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                    startActivity(intent, options.toBundle());
                    finish();
                }
            });

            cv_lecturer = findViewById(R.id.cv_add_lecturer);
            cv_lecturer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StarterActivity.this, LecturerActivity.class);
                    intent.putExtra("action", "add");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                    startActivity(intent, options.toBundle());
                    finish();
                }
            });

            cv_course = findViewById(R.id.cv_add_course);
            cv_course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StarterActivity.this, CourseActivity.class);
                    intent.putExtra("action", "add");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                    startActivity(intent, options.toBundle());
                    finish();
                }
            });

            cv_login = findViewById(R.id.cv_login);
            cv_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StarterActivity.this, LoginActivity.class);
                    intent.putExtra("action", "add");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                    startActivity(intent, options.toBundle());
                    finish();
                }
            });
        }
}
