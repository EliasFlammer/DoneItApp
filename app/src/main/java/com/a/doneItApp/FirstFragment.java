package com.a.doneItApp;
//aint made for darkmode
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.a.doneItApp.databinding.FragmentFirstBinding;
import com.a.doneItApp.utils.DatabaseHandler;

public class FirstFragment extends Fragment {
    private TextView scoreTextView;
    private PointsViewModel pointsViewModel;
    private FragmentFirstBinding binding;
    private DatabaseHandler db;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View fragmentFirstLayout = inflater.inflate(R.layout.fragment_first, container, false);
        scoreTextView = fragmentFirstLayout.findViewById(R.id.score2);

        db = new DatabaseHandler(requireContext());
        db.openDatabase();

        return fragmentFirstLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pointsViewModel = new ViewModelProvider(this).get(PointsViewModel.class);


        view.findViewById(R.id.toast_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                Toast myToast = Toast.makeText(getActivity(), "Still in development, coming soon:)", Toast.LENGTH_LONG);
                myToast.show();
            }
        });

        view.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() { //TODO careful, this button does not only reset the points but it does also set the done tasks status to 0(undone) if i want to do something that needs the status of the task after reset i need to change this
            @Override
            public void onClick(View view1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Reset Score");
                builder.setMessage("Are you sure you want to reset your score?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.resetAllTaskStatus();
                                pointsViewModel.calculateAllPoints(db);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        pointsViewModel.getPointsLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer score) {
                updateScoreTextView(score);
            }
        });
        pointsViewModel.calculateAllPoints(db);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void updateScoreTextView(int score) {
        scoreTextView.setText("Score: " + score);
    }
}