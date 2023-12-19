package com.example.myjavaapp.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myjavaapp.ToDoPackage.DialogCloseListener;
import com.example.myjavaapp.R;
import com.example.myjavaapp.ToDoPackage.TodoModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Collections;

//wenn ich de edit Task bar nöd am bode wott ha denn muss ich bottomdialog zu öppis anderem Ändere
public class AddNewTask  extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private EditText newTaskText;
    private Button newTaskSaveButton;
    private String selectedIntensity;
    private String selectedDuration;
    private DatabaseHandler db;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE); //this makes sure that you can see what oyu enter so that the keyboard is readjusted while typing
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = getView().findViewById(R.id.newTaskText);
        newTaskSaveButton = getView().findViewById(R.id.saveNewTaskButton);

        Spinner intensitySpinner = view.findViewById(R.id.spinnerIntensity);
        String[] intensityArray = getResources().getStringArray(R.array.intensity_array);
        ArrayAdapter<String> intensityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, intensityArray);
        intensityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intensitySpinner.setAdapter(intensityAdapter);

        Spinner durationSpinner = view.findViewById(R.id.spinnerDuration);
        String[] durationArray = getResources().getStringArray(R.array.duration_array);
        ArrayAdapter<String> durationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, durationArray);
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(durationAdapter);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("title"); //TODO EXAMPLE FOR CRASHING AND ERRORS-I USED TO HAVE TASK AS A KEY SO IT DIDNT MATCH AND GIVE A 0POINTER EXCEPTION
            newTaskText.setText(task);
            if (task.length() > 0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary)); //wenn meh farbe denn:  newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), com.google.android.material.R.color.xy));
        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            } //not required

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                } else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            } //not required
        });

        intensitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 selectedIntensity = intensityArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            selectedIntensity = null;
            }
        });

        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDuration = durationArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedDuration = null;
            }
        });

        boolean finalIsUpdate = isUpdate; // we keep the boolean value of isUpdate but "freeze" it
       newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString(); //: This retrieves the text entered into an EditText field named newTaskText and converts it to a string, storing it in the text variable.
                if (finalIsUpdate) { //if true then there is already a task and we want to update it
                    db.updateTitle(bundle.getInt("id"), text);

                    String intensityValue = selectedIntensity;
                    if (intensityValue != null) {
                        TodoModel.Intensity updatedIntensity = TodoModel.Intensity.valueOf(intensityValue);
                        db.updateIntensity(bundle.getInt("id"), updatedIntensity); }

                    String durationValue = selectedDuration;
                    if (durationValue != null) {
                        TodoModel.Duration updatedDuration = TodoModel.Duration.valueOf(durationValue);
                        db.updateDuration(bundle.getInt("id"), updatedDuration); }

                    } else { //new task
                        TodoModel task = new TodoModel();
                        task.setTitle(text);
                        task.setStatus(0);
                        task.setHidden(0);
                        task.setIntensity(TodoModel.Intensity.valueOf(selectedIntensity));
                        task.setDuration(TodoModel.Duration.valueOf(selectedDuration));
                        db.insertTask(task);
                    }

                    dismiss();
            }
        });

    }



    @Override
    public void onDismiss(DialogInterface dialog) { //when the dialog is closed the handleDialogClose function updates the view of all the tasks
       Fragment parent = getParentFragment();
        if (parent instanceof DialogCloseListener) {
            ((DialogCloseListener) parent).handleDialogClose(dialog);
        } //with this code it works besides refreshing after editing/


    }

}
