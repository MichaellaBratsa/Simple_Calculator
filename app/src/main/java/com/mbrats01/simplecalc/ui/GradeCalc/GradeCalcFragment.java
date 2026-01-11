package com.mbrats01.simplecalc.ui.GradeCalc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mbrats01.simplecalc.R;

public class GradeCalcFragment extends Fragment {

    private EditText HW1, HW2, HW3, HW4, teamProject, midTerm, finalExam, classParticipation;
    private Button calcButton;
    private TextView resultText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_grade, container, false);

        HW1 = root.findViewById(R.id.HW1);
        HW2 = root.findViewById(R.id.HW2);
        HW3 = root.findViewById(R.id.HW3);
        HW4 = root.findViewById(R.id.HW4);
        teamProject = root.findViewById(R.id.teamProject);
        midTerm = root.findViewById(R.id.midTerm);
        finalExam = root.findViewById(R.id.finalExam);
        classParticipation = root.findViewById(R.id.classParticipation);
        resultText = root.findViewById(R.id.resultText);

        calcButton = root.findViewById(R.id.calcButton);

        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hw1Str = HW1.getText().toString();
                String hw2Str = HW2.getText().toString();
                String hw3Str = HW3.getText().toString();
                String hw4Str = HW4.getText().toString();
                String teamProjectStr = teamProject.getText().toString();
                String midTermStr = midTerm.getText().toString();
                String finalExamStr = finalExam.getText().toString();
                String classParticipationStr = classParticipation.getText().toString();

                // Check if the grades given correct and transform them from strings into doubles
                if (!hw1Str.isEmpty() && !hw2Str.isEmpty() && !hw3Str.isEmpty() && !hw4Str.isEmpty() && !teamProjectStr.isEmpty() && !midTermStr.isEmpty() && !finalExamStr.isEmpty() && !classParticipationStr.isEmpty()) {
                    double hw1 = Double.parseDouble(hw1Str);
                    double hw2 = Double.parseDouble(hw2Str);
                    double hw3 = Double.parseDouble(hw3Str);
                    double hw4 = Double.parseDouble(hw4Str);
                    double teamProject = Double.parseDouble(teamProjectStr);
                    double midTerm = Double.parseDouble(midTermStr);
                    double finalExam = Double.parseDouble(finalExamStr);
                    double classParticipation = Double.parseDouble(classParticipationStr);

                    // Calculate final grade according to the percentages
                    double finalGrade = (hw1 * 0.05 + hw2 * 0.05 + hw3 * 0.05 + hw4 * 0.05 + teamProject * 0.25 + midTerm * 0.2 + finalExam * 0.3 + classParticipation * 0.05) / 10;
                    double semesterGrade = 0.0;
                    double finalGradeRemainder = finalGrade - (int)(finalGrade);

                    // Convert the final grade into University's scale
                    if(finalGradeRemainder >= 0.25 && finalGradeRemainder <= 0.74)
                        semesterGrade = (int)(finalGrade) + 0.5;
                    else if(finalGradeRemainder >= 0.0 && finalGradeRemainder <= 0.24)
                        semesterGrade = (int)(finalGrade);
                    else if (finalGradeRemainder >= 0.75)
                        semesterGrade = (int)(finalGrade) + 1.0;

                    resultText.setText("Final Semester Grade at CS498: " + semesterGrade + "(Actual Grade: " + finalGrade + ")");

                } else {
                    Toast.makeText(getActivity(), "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }
}
