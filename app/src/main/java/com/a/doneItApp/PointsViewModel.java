package com.a.doneItApp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.a.doneItApp.ToDoPackage.TodoModel;
import com.a.doneItApp.utils.DatabaseHandler;

import java.util.List;

public class PointsViewModel extends ViewModel {
    private MutableLiveData<Integer> pointsLiveData = new MutableLiveData<>();

    public LiveData<Integer> getPointsLiveData() {
        return pointsLiveData;
    }

    public void calculateAllPoints(DatabaseHandler db) {
        int pointValue = 0;

        List<TodoModel> allTodos = db.getAllTasks();
        for (TodoModel ufgabe : allTodos) {
            if (ufgabe.getStatus() == 1) {
                TodoModel.Intensity intensity = ufgabe.getIntensity();
                TodoModel.Duration duration = ufgabe.getDuration();
                switch (intensity) {
                    case INTENSITY:
                        pointValue += 0;
                        break;
                    case LOW:
                        switch (duration) {
                            case DURATION:
                                pointValue+= 0;
                                break;
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
                            case DURATION:
                                pointValue+= 0;
                                break;
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
                            case DURATION:
                                pointValue+= 0;
                                break;
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
