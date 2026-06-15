package com.prathiksha.studyplanner.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.prathiksha.studyplanner.R;
import com.prathiksha.studyplanner.adapters.TaskAdapter;
import com.prathiksha.studyplanner.models.Task;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    RecyclerView rvTasks;
    Button btnAddTask;
    TextView tvSubjectName;
    TaskAdapter adapter;
    List<Task> taskList;
    FirebaseFirestore db;
    String subjectId, subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        db = FirebaseFirestore.getInstance();
        subjectId = getIntent().getStringExtra("subjectId");
        subjectName = getIntent().getStringExtra("subjectName");

        tvSubjectName = findViewById(R.id.tvSubjectName);
        rvTasks = findViewById(R.id.rvTasks);
        btnAddTask = findViewById(R.id.btnAddTask);

        tvSubjectName.setText(subjectName + " Tasks");

        taskList = new ArrayList<>();
        adapter = new TaskAdapter(taskList,
                task -> toggleTaskComplete(task),
                task -> deleteTask(task)
        );

        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        rvTasks.setAdapter(adapter);

        loadTasks();
        btnAddTask.setOnClickListener(v -> showAddTaskDialog());
    }

    private void loadTasks() {
        db.collection("tasks")
                .whereEqualTo("subjectId", subjectId)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) return;
                    taskList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        Task t = doc.toObject(Task.class);
                        t.setTaskId(doc.getId());
                        taskList.add(t);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void showAddTaskDialog() {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_add_task, null);
        EditText etTitle = dialogView.findViewById(R.id.etTaskTitle);
        EditText etDeadline = dialogView.findViewById(R.id.etDeadline);
        Button btnPick = dialogView.findViewById(R.id.btnPickDate);

        btnPick.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) ->
                    etDeadline.setText(d + "/" + (m + 1) + "/" + y),
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        new AlertDialog.Builder(this)
                .setTitle("Add Task")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String title = etTitle.getText().toString().trim();
                    String deadline = etDeadline.getText().toString().trim();
                    if (!title.isEmpty()) addTask(title, deadline);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addTask(String title, String deadline) {
        Task task = new Task(null, subjectId, title, deadline);
        db.collection("tasks").add(task)
                .addOnSuccessListener(ref ->
                        Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }

    private void toggleTaskComplete(Task task) {
        db.collection("tasks").document(task.getTaskId())
                .update("completed", !task.isCompleted());
    }

    private void deleteTask(Task task) {
        db.collection("tasks").document(task.getTaskId())
                .delete()
                .addOnSuccessListener(v ->
                        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show());
    }
}