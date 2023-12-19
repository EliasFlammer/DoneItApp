package com.example.myjavaapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myjavaapp.ToDoPackage.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String STATUS = "status";
    private static final String  HIDDEN = "hidden";
    private static final String INTENSITY = "intensity";
    private static final String DURATION = "duration";

    private static final String CREATE_TODO_TABLE =  "CREATE TABLE " + TODO_TABLE + "(" +
            ID + " INTEGER PRIMARY  KEY AUTOINCREMENT, " +
            TITLE + " TEXT, " +
            STATUS + " INTEGER, " +
            INTENSITY + " TEXT, " +
            DURATION + " TEXT, " +
            HIDDEN + " INTEGER)";
    private SQLiteDatabase db;
//constructor
public DatabaseHandler(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop the older Tables
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        //Create new Table
        onCreate(db);
    }
    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void insertTask(TodoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TITLE, task.getTitle());
        cv.put(STATUS, 0);
        cv.put(HIDDEN, 0);
        cv.put(INTENSITY, task.getIntensity().toString()); //the .name()/toString takes the text of the enum so that we have a string for our table since it dosnt take enums
        cv.put(DURATION, task.getDuration().toString());
        db.insert(TODO_TABLE, null, cv);
    }

    public List<TodoModel> getAllVisibleTasks(){
        List<TodoModel> taskListVisible = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()){
                    do {
                        TodoModel task = new TodoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTitle(cur.getString(cur.getColumnIndex(TITLE)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        task.setHidden(cur.getInt(cur.getColumnIndex(HIDDEN)));
                        if (!cur.isNull(cur.getColumnIndex(INTENSITY))) {
                            task.setIntensity(TodoModel.Intensity.valueOf(cur.getString(cur.getColumnIndex(INTENSITY)))); //TodoModel.Intensity.valueOf should convert the string back to the enum value zb "HIGH" but not 100% sure if that works
                        }
                        if (!cur.isNull(cur.getColumnIndex(DURATION))) {
                            task.setDuration(TodoModel.Duration.valueOf(cur.getString(cur.getColumnIndex(DURATION))));
                        }
                        if (task.isHidden() != 1) {
                            taskListVisible.add(task);
                        }
                    }while (cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            cur.close();
        }
        return taskListVisible;
    }
    public List<TodoModel> getAllTasks(){
        List<TodoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()){
                    do {
                        TodoModel task = new TodoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTitle(cur.getString(cur.getColumnIndex(TITLE)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        task.setHidden(cur.getInt(cur.getColumnIndex(HIDDEN)));
                        if (!cur.isNull(cur.getColumnIndex(INTENSITY))) {
                            task.setIntensity(TodoModel.Intensity.valueOf(cur.getString(cur.getColumnIndex(INTENSITY)))); //TodoModel.Intensity.valueOf should convert the string back to the enum value zb "HIGH" but not 100% sure if that works
                        }
                        if (!cur.isNull(cur.getColumnIndex(DURATION))) {
                            task.setDuration(TodoModel.Duration.valueOf(cur.getString(cur.getColumnIndex(DURATION))));
                        }
                        taskList.add(task);
                    }while (cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateTitle(int id, String title){
        ContentValues cv = new ContentValues();
        cv.put(TITLE, title);
        db.update(TODO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }
    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }
    public void updateHidden(int id, int hidden){
        ContentValues cv = new ContentValues();
        cv.put(HIDDEN, hidden);
        db.update(TODO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }
    public void updateIntensity(int id, TodoModel.Intensity intensity) {
        ContentValues cv = new ContentValues();
        cv.put(INTENSITY, intensity.toString());
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});

    }
    public void updateDuration(int id, TodoModel.Duration duration) {
        ContentValues cv = new ContentValues();
        cv.put(DURATION, duration.toString());
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});
    }
    public void resetAllTaskStatus() {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, 0);
        db.update(TODO_TABLE, cv, null, null);
    }


}

