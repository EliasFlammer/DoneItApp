package com.example.myjavaapp.ToDoPackage;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.MainActivity;
import com.example.myjavaapp.PointsViewModel;
import com.example.myjavaapp.R;
import com.example.myjavaapp.ToDoPackage.Adapter.ToDoAdapter;
import com.example.myjavaapp.utils.AddNewTask;
import com.example.myjavaapp.utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoFragment extends Fragment implements DialogCloseListener {
    private RecyclerView tasksRecyclerview;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fabAddTask;
    private FloatingActionButton fabDeleteTasks;
    private List<TodoModel> taskList;
    private DatabaseHandler db;
    private TextView pointsTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_todo_fragment, container, false);

        db = new DatabaseHandler(requireContext());
        db.openDatabase();

        taskList = new ArrayList<>();

        tasksRecyclerview = rootView.findViewById(R.id.tasksRecyclerView);
        tasksRecyclerview.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        PointsViewModel pointsViewModel = new ViewModelProvider(this).get(PointsViewModel.class);
        tasksAdapter = new ToDoAdapter(db, (MainActivity) getActivity(), pointsViewModel); //instead of "this" as in the tutorial, i refrence manually to the mainactivity
        tasksRecyclerview.setAdapter(tasksAdapter);

        fabAddTask = rootView.findViewById(R.id.addTaskButton);
        fabDeleteTasks = rootView.findViewById(R.id.deleteTaskFAB);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecylclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerview);
        taskList = db.getAllVisibleTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTask(taskList);

        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getChildFragmentManager(), AddNewTask.TAG);
            }
        });

        fabDeleteTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasksAdapter.hideCompletedItems();
            }
        });

        pointsViewModel.getPointsLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer score) {
                updateScoreTextView(score);
            }
        });
        pointsViewModel.calculateAllPoints(db);
        pointsTextView = rootView.findViewById(R.id.Score);

        return rootView;
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllVisibleTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTask(taskList);
        tasksAdapter.notifyDataSetChanged();


    }

    private void updateScoreTextView(int score) {
        pointsTextView.setText("Score: " + score);
    }
}
