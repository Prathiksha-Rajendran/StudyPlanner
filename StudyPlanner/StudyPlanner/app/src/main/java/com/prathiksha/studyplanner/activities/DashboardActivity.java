package com.prathiksha.studyplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.prathiksha.studyplanner.R;
import com.prathiksha.studyplanner.adapters.SubjectAdapter;
import com.prathiksha.studyplanner.models.Subject;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    RecyclerView rvSubjects;
    Button btnAddSubject, btnLogout;
    SubjectAdapter adapter;
    List<Subject> subjectList;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        rvSubjects = findViewById(R.id.rvSubjects);
        btnAddSubject = findViewById(R.id.btnAddSubject);
        btnLogout = findViewById(R.id.btnLogout);

        subjectList = new ArrayList<>();
        adapter = new SubjectAdapter(subjectList, subject -> {
            Intent intent = new Intent(this, TasksActivity.class);
            intent.putExtra("subjectId", subject.getSubjectId());
            intent.putExtra("subjectName", subject.getSubjectName());
            startActivity(intent);
        });

        rvSubjects.setLayoutManager(new LinearLayoutManager(this));
        rvSubjects.setAdapter(adapter);

        loadSubjects();

        btnAddSubject.setOnClickListener(v -> showAddSubjectDialog());
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        });
    }

    private void loadSubjects() {
        db.collection("subjects")
                .whereEqualTo("userId", userId)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) return;
                    subjectList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        Subject s = doc.toObject(Subject.class);
                        s.setSubjectId(doc.getId());
                        subjectList.add(s);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void showAddSubjectDialog() {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_add_subject, null);
        EditText etName = dialogView.findViewById(R.id.etSubjectName);
        new AlertDialog.Builder(this)
                .setTitle("Add Subject")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    if (!name.isEmpty()) addSubject(name);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addSubject(String name) {
        Subject subject = new Subject(null, userId, name);
        db.collection("subjects").add(subject)
                .addOnSuccessListener(ref ->
                        Toast.makeText(this, "Subject added!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }
}