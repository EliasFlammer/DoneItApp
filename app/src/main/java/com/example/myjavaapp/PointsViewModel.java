package com.example.myjavaapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myjavaapp.ToDoPackage.TodoModel;
import com.example.myjavaapp.utils.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PointsViewModel extends ViewModel {
    private MutableLiveData<Integer> pointsLiveData = new MutableLiveData<>();

    public LiveData<Integer> getPointsLiveData() {
        return pointsLiveData;
    }

    public void setPointsLiveData(int points) {
        pointsLiveData.setValue(points);
    }

    public void calculateAllPoints(DatabaseHandler db) {
        int pointValue = 0;

        List<TodoModel> allTodos = db.getAllTasks();
        for (TodoModel ufgabe : allTodos) {
            if (ufgabe.getStatus() == 1) {
                TodoModel.Intensity intensity = ufgabe.getIntensity();
                TodoModel.Duration duration = ufgabe.getDuration();
                switch (intensity) {
                    case LOW:
                        switch (duration) {
                            case SHORT:
                                pointValue += 10;
                                break;
                            case MODERATE:
                                pointValue += 20;
                                break;
                            case LONG:
                                pointValue += 30;
                                break;
                        }
                        break;
                    case MEDIUM:
                        switch (duration) {
                            case SHORT:
                                pointValue += 20;
                                break;
                            case MODERATE:
                                pointValue += 30;
                                break;
                            case LONG:
                                pointValue += 40;
                                break;
                        }
                        break;
                    case HIGH:
                        switch (duration) {
                            case SHORT:
                                pointValue += 30;
                                break;
                            case MODERATE:
                                pointValue += 40;
                                break;
                            case LONG:
                                pointValue += 50;
                                break;
                        }
                        break;
                }


            }

        }

        pointsLiveData.setValue(pointValue);
    }
}
