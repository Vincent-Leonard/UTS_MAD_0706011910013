package com.example.handsonfirebase.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handsonfirebase.CourseActivity;
import com.example.handsonfirebase.CourseFragment;
import com.example.handsonfirebase.Glovar;
import com.example.handsonfirebase.R;
import com.example.handsonfirebase.StarterActivity;
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

public class CourseFragmentAdapter extends RecyclerView.Adapter<CourseFragmentAdapter.CardViewViewHolder> {
    private Context context;
    private ArrayList<Course> listCourse;
    private ArrayList<Course> getListCourse() {
        return listCourse;
    }
    public void setListCourse(ArrayList<Course> listCourse) {
        this.listCourse = listCourse;
    }
    public CourseFragmentAdapter(Context context) {
        this.context = context;
    }
    Dialog dialog;
    DatabaseReference dbCourse;
    DatabaseReference dbStudent;
    private FirebaseAuth mAuth;
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    FirebaseAuth firebaseAuth;
    String userKey;
    String cid;
    String dayC;
    private DatabaseReference mDatabase;
    private DatabaseReference secondDatabase;

    MutableLiveData<Course> courseToAdd = new MutableLiveData<>();
    public MutableLiveData<Course> getCourseToAdd(){
        return courseToAdd;
    }
    boolean conflict = false;

    @NonNull
    @Override
    public CourseFragmentAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_course_adapter, parent, false);
        return new CourseFragmentAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final CourseFragmentAdapter.CardViewViewHolder holder, int position) {
        final Course course = getListCourse().get(position);
        holder.lbl_subject.setText(course.getName());
        holder.lbl_day.setText(course.getDay());
        holder.lbl_start.setText(course.getStart());
        holder.lbl_end.setText(course.getEnd());
        holder.lbl_lecturer.setText(course.getLecturer());


        dbCourse = FirebaseDatabase.getInstance().getReference("course");
        dialog = Glovar.loadingDialog(context);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        userKey = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("student");


        holder.button_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConflict(course);
                v.startAnimation(klik);

            }
        });

    }

    public void checkConflict(final Course enroll){
        final int  chosenCourseStart = Integer.parseInt(enroll.getStart().replace(":",""));
        final int chosenCourseEnd = Integer.parseInt(enroll.getEnd().replace(":",""));
        final String course_temp_day = enroll.getDay();

        FirebaseDatabase.getInstance()
                .getReference("student")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("course")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        conflict = false;
                        for(DataSnapshot childSnapshot : snapshot.getChildren()){
                            Course course = childSnapshot.getValue(Course.class);
                            int courseStart = Integer.parseInt(course.getStart().replace(":",""));
                            int courseEnd = Integer.parseInt(course.getEnd().replace(":",""));
                            String course_day = course.getDay();

                            if (course_day.equalsIgnoreCase(course_temp_day)){
                                if (chosenCourseStart > courseStart && chosenCourseStart < courseEnd){
                                    conflict = true;
                                    break;
                                }
                                if (courseEnd > courseStart && chosenCourseStart < courseEnd){
                                    conflict = true;
                                    break;
                                }
                            }
                        }

                        if (conflict){
                            Toast.makeText(context, "Course Conflict!", Toast.LENGTH_SHORT).show();
                        } else {
                            cid = enroll.getId();
                            String subject = enroll.getName();
                            String day = enroll.getDay();
                            String start = enroll.getStart();
                            String end = enroll.getEnd();
                            String lecturer = enroll.getLecturer();

                            Course course = new Course(cid, subject, day, start, end, lecturer);
                            mDatabase.child(userKey).child("course").child(cid).setValue(course);
                            Toast.makeText(context, "Course Succesfully Added!", Toast.LENGTH_SHORT).show();
//                            courseToAdd.setValue(chosenCourse);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return getListCourse().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView lbl_subject, lbl_day, lbl_start, lbl_end, lbl_lecturer;
        ImageView button_enroll;

        CardViewViewHolder(View itemView) {
            super(itemView);
            lbl_subject = itemView.findViewById(R.id.lbl_coursefragment_name);
            lbl_day = itemView.findViewById(R.id.lbl_coursefragment_day);
            lbl_start = itemView.findViewById(R.id.lbl_coursefragment_start);
            lbl_end = itemView.findViewById(R.id.lbl_coursefragment_end);
            lbl_lecturer = itemView.findViewById(R.id.lbl_coursefragment_lecturer);

            button_enroll = itemView.findViewById(R.id.btn_enroll);

        }
    }
}
