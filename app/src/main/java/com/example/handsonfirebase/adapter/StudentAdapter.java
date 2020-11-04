package com.example.handsonfirebase.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handsonfirebase.Glovar;
import com.example.handsonfirebase.LecturerActivity;
import com.example.handsonfirebase.LecturerData;
import com.example.handsonfirebase.LecturerDetail;
import com.example.handsonfirebase.R;
import com.example.handsonfirebase.RegisterActivity;
import com.example.handsonfirebase.StarterActivity;
import com.example.handsonfirebase.StudentData;
import com.example.handsonfirebase.model.Lecturer;
import com.example.handsonfirebase.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.CardViewViewHolder> {

    private Context context;
    private ArrayList<Student> listStudent;
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Dialog dialog;
    DatabaseReference dbStudent;
    Student student;
    String uid;
    private ArrayList<Student> getListStudent() {
        return listStudent;
    }
    public void setListStudent(ArrayList<Student> listStudent) {
        this.listStudent = listStudent;
    }
    public StudentAdapter(Context context) {
        this.context = context;
    }
    private FirebaseAuth mAuth;

    @NonNull
    @Override
    public StudentAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_adapter, parent, false);
        return new StudentAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final StudentAdapter.CardViewViewHolder holder, int position) {
        final Student student = getListStudent().get(position);
        holder.lbl_name.setText(student.getName());
        holder.lbl_nim.setText(student.getNim());
        holder.lbl_email.setText(student.getEmail());
        holder.lbl_age.setText(student.getAge());
        holder.lbl_gender.setText(" (" + student.getGender() + ")");
        holder.lbl_address.setText(student.getAddress());
        dbStudent = FirebaseDatabase.getInstance().getReference("student");
        dialog = Glovar.loadingDialog(context);
        mAuth = FirebaseAuth.getInstance();

        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                new AlertDialog.Builder(context)
                        .setTitle("Konfirmasi")
                        .setMessage("Are you sure to delete "+student.getName()+" data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();

                                        String uid = student.getId();
                                        mAuth.signInWithEmailAndPassword(student.getEmail(),student.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                FirebaseUser user = mAuth.getInstance().getCurrentUser();
                                                if (user != null) {
                                                    Toast.makeText(context, "User Account Detected", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(context, "User Account Unknown", Toast.LENGTH_SHORT).show();
                                                }

                                                user.delete()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(context, "User Account Deleted", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                dbStudent.child(uid).removeValue(new DatabaseReference.CompletionListener() {
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
                Intent in = new Intent(context, RegisterActivity.class);
                Student stud = new Student(student.getId(), student.getName(), student.getEmail(), student.getPassword(), student.getGender(), student.getNim(), student.getAge(), student.getAddress());
                in.putExtra("action", "edit");
                in.putExtra("edit_data_stud", student);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity)context);
                context.startActivity(in, options.toBundle());
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListStudent().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView lbl_name, lbl_nim, lbl_email, lbl_age, lbl_gender, lbl_address;
        ImageView button_edit, button_delete;

        CardViewViewHolder(View itemView) {
            super(itemView);
            lbl_name = itemView.findViewById(R.id.lbl_student_name);
            lbl_nim = itemView.findViewById(R.id.lbl_student_nim);
            lbl_email = itemView.findViewById(R.id.lbl_student_email);
            lbl_age = itemView.findViewById(R.id.lbl_student_age);
            lbl_gender = itemView.findViewById(R.id.lbl_student_gender);
            lbl_address = itemView.findViewById(R.id.lbl_student_address);

            button_edit = itemView.findViewById(R.id.lbl_student_edit);
            button_delete = itemView.findViewById(R.id.lbl_student_delete);


        }
    }
}
