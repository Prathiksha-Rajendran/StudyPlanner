package com.prathiksha.studyplanner.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.prathiksha.studyplanner.R;
import com.prathiksha.studyplanner.models.Task;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    public interface OnTaskActionListener {
        void onAction(Task task);
    }

    private List<Task> tasks;
    private OnTaskActionListener onToggle, onDelete;

    public TaskAdapter(List<Task> tasks,
                       OnTaskActionListener onToggle,
                       OnTaskActionListener onDelete) {
        this.tasks = tasks;
        this.onToggle = onToggle;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Task task = tasks.get(position);
        h.tvTitle.setText(task.getTitle());
        h.tvDeadline.setText("Due: " + task.getDeadline());
        h.cbDone.setChecked(task.isCompleted());

        if (task.isCompleted()) {
            h.tvTitle.setPaintFlags(
                    h.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            h.tvTitle.setPaintFlags(
                    h.tvTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        h.cbDone.setOnClickListener(v -> onToggle.onAction(task));
        h.btnDelete.setOnClickListener(v -> onDelete.onAction(task));
    }

    @Override
    public int getItemCount() { return tasks.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDeadline;
        CheckBox cbDone;
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            cbDone = itemView.findViewById(R.id.cbDone);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}