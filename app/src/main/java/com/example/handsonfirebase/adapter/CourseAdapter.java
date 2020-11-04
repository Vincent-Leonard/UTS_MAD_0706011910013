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
import androidx.recyclerview.widget.RecyclerView;

import com.example.handsonfirebase.CourseActivity;
import com.example.handsonfirebase.Glovar;
import com.example.handsonfirebase.R;
import com.example.handsonfirebase.RegisterActivity;
import com.example.handsonfirebase.StarterActivity;
import com.example.handsonfirebase.model.Course;
import com.example.handsonfirebase.model.Lecturer;
import com.example.handsonfirebase.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CardViewViewHolder>{

    private Context context;
    private ArrayList<Course> listCourse;
    private ArrayList<Course> getListCourse() {
        return listCourse;
    }
    public void setListCourse(ArrayList<Course> listCourse) {
        this.listCourse = listCourse;
    }
    public CourseAdapter(Context context) {
        this.context = context;
    }
    Dialog dialog;
    DatabaseReference dbCourse;
    private FirebaseAuth mAuth;
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);

    @NonNull
    @Override
    public CourseAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_adapter, parent, false);
        return new CourseAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final CourseAdapter.CardViewViewHolder holder, int position) {
        final Course course = getListCourse().get(position);
        holder.lbl_subject.setText(course.getName());
        holder.lbl_day.setText(course.getDay());
        holder.lbl_start.setText(course.getStart());
        holder.lbl_end.setText(course.getEnd());
        holder.lbl_lecturer.setText(course.getLecturer());


        dbCourse = FirebaseDatabase.getInstance().getReference("course");
        dialog = Glovar.loadingDialog(context);
        mAuth = FirebaseAuth.getInstance();

        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                new AlertDialog.Builder(context)
                        .setTitle("Konfirmasi")
                        .setMessage("Are you sure to delete "+course.getName()+" data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();

                                        String uid = course.getId();

                                                dbCourse.child(uid).removeValue(new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                        Intent in = new Intent(context, StarterActivity.class);
                                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        Toast.makeText(context, "Delete success!", Toast.LENGTH_SHORT).show();
                                                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity)context);
                                                        context.startActivity(in, options.toBundle());
                                                        ((Activity)context).finish();
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


        holder.button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                Intent in = new Intent(context, CourseActivity.class);
                Course courses = new Course(course.getId(), course.getName(), course.getDay(), course.getStart(), course.getEnd(), course.getLecturer());
                in.putExtra("action", "edit");
                in.putExtra("edit_data_course", course);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity)context);
                context.startActivity(in, options.toBundle());
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListCourse().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView lbl_subject, lbl_day, lbl_start, lbl_end, lbl_lecturer;
        ImageView button_edit, button_delete;

        CardViewViewHolder(View itemView) {
            super(itemView);
            lbl_subject = itemView.findViewById(R.id.lbl_course_subject);
            lbl_day = itemView.findViewById(R.id.lbl_course_day);
            lbl_start = itemView.findViewById(R.id.lbl_course_start);
            lbl_end = itemView.findViewById(R.id.lbl_course_end);
            lbl_lecturer = itemView.findViewById(R.id.lbl_course_lecturer);

            button_edit = itemView.findViewById(R.id.lbl_course_edit);
            button_delete = itemView.findViewById(R.id.lbl_course_delete);

        }
    }
}
