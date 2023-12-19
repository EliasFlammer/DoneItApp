package com.example.myjavaapp.ToDoPackage.Adapter;

import android.content.Context;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.CheckBox;
    import android.widget.CompoundButton;

    import androidx.recyclerview.widget.RecyclerView;


    import java.util.ArrayList;

    import java.util.List;

    import com.example.myjavaapp.MainActivity;
    import com.example.myjavaapp.PointsViewModel;
    import com.example.myjavaapp.R;
    import com.example.myjavaapp.ToDoPackage.TodoModel;
    import com.example.myjavaapp.utils.AddNewTask;
    import com.example.myjavaapp.utils.DatabaseHandler;

    public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
        private List<TodoModel> todoList;
        private MainActivity activity; //because we bring a new fragment later
        private DatabaseHandler db;

        private PointsViewModel pointsViewModel;
        //constructor:
        public ToDoAdapter(DatabaseHandler db, MainActivity activity, PointsViewModel pointsViewModel) {
            this.db = db;
            this.activity = activity;
            this.pointsViewModel = pointsViewModel;
        }
        //this creates a new viewholder(verbindet item/view mit recycler) by inflating the layout for a single item and returning it(that new instance of the Viewholder class)
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
            return new ViewHolder(itemView);
        }
        public void onBindViewHolder(ViewHolder holder, int position) { //this binds the data to each view
            db.openDatabase();
            TodoModel item = todoList.get(position);
            holder.task.setText(item.getTitle());
            holder.task.setChecked(toBoolean(item.getStatus()));
            holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        db.updateStatus(item.getId(),1);
                        item.setStatus(1); //this updates the local status, so that the getCompletedPositions gets the updated status of item
                        pointsViewModel.calculateAllPoints(db);

                    } else {
                        if (item.isHidden() != 1) {       //weil wenn task hidden ist isChecked nicht mehr stimmt aber der status nicht mehr geändert werden sll
                            db.updateStatus(item.getId(),0);
                            item.setStatus(0);
                            pointsViewModel.calculateAllPoints(db);
                        }
                    }
                }
            });
        }
        private boolean toBoolean(int n){
            return n!=0;
        } //because we had numbers instead of booleans for the checked status

        public int getItemCount(){
            return todoList.size();
        }

        public void setTask(List<TodoModel> todoList){ //this updates the todolist
            this.todoList = todoList;
            notifyDataSetChanged();
        }

        public Context getContext() {return activity; }

        public void deleteItem(int position){
            TodoModel item = todoList.get(position);
            db.deleteTask(item.getId());
            todoList.remove(position);
            notifyItemRemoved(position);
        }

        private List<Integer> getCompletedTaskPositions() {
            List<Integer> completedPositions = new ArrayList<>();
            for (int i = 0; i < todoList.size(); i++) {
                TodoModel item = todoList.get(i);
                if (item.getStatus() == 1) {
                    completedPositions.add(i);
                }
            }
            return completedPositions;
        }
        public void hideCompletedItems() {
            List<Integer> completedPositions = getCompletedTaskPositions();
            for (int i = 0; i < completedPositions.size(); i++) {
                int position = completedPositions.get(i);

                TodoModel item = todoList.get(position);
                item.setHidden(1);
                db.updateHidden(item.getId(), 1);
                notifyItemChanged(position);
            }
            todoList = db.getAllVisibleTasks(); // if this is removed then it doesnt mess up the order  but it doesnt update
            notifyDataSetChanged();
        }


        //for editing todos on a posittion with a certain input and showing them with the Add newtask fragment
        public void editItem(int position){ //(position because you take the item you want to update with a certain position)
            TodoModel item = todoList.get(position);
            Bundle bundle = new Bundle(); //die alte argument(id und text) werded im bundle gholt
            bundle.putInt("id", item.getId());
            bundle.putString("title",item.getTitle());

            bundle.putString("intensity",item.getIntensity().toString());
            bundle.putString("duration",item.getDuration().toString());

            AddNewTask fragment = new AddNewTask();
            fragment.setArguments(bundle);
            fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
            //TODO fix the updating of view after editItem. adddnewtask wird uf fragment called aber es aktualisiert nöd

    //zu activity glaub will mir es neus frgament holed und das in activity called und nöd im todofragment selber
        }
        public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        //Constructor:
            ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.TodoCheckBox);
            }
        }
    }
