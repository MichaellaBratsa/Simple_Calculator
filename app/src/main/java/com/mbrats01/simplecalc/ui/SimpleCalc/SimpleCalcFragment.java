package com.mbrats01.simplecalc.ui.SimpleCalc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;

import com.mbrats01.simplecalc.R;

public class SimpleCalcFragment extends Fragment {

    private TextView tvDisplay;
    private StringBuilder expression = new StringBuilder();
    private boolean isNewInput = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simplecalc, container, false);

        tvDisplay = view.findViewById(R.id.tv_display);

        if (savedInstanceState != null) {
            String savedExpr = savedInstanceState.getString("expressionText", "0");
            expression.setLength(0);
            expression.append(savedExpr);
            tvDisplay.setText(expression.toString());
        }

        // Digit buttons between 0-9
        int[] digitIds = {R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9};

        View.OnClickListener digitListener = v -> {
            Button b = (Button) v;
            if (isNewInput) {
                expression.setLength(0);
                isNewInput = false;
            }
            expression.append(b.getText().toString());
            tvDisplay.setText(expression.toString());
        };

        for (int id : digitIds) {
            view.findViewById(id).setOnClickListener(digitListener);
        }

        view.findViewById(R.id.btn_open).setOnClickListener(v -> addSymbol("("));
        view.findViewById(R.id.btn_close).setOnClickListener(v -> addSymbol(")"));
        view.findViewById(R.id.btn_dot).setOnClickListener(v -> addSymbol("."));
        view.findViewById(R.id.btn_add).setOnClickListener(v -> addSymbol("+"));
        view.findViewById(R.id.btn_sub).setOnClickListener(v -> addSymbol("-"));
        view.findViewById(R.id.btn_mul).setOnClickListener(v -> addSymbol("*"));
        view.findViewById(R.id.btn_div).setOnClickListener(v -> addSymbol("/"));
        view.findViewById(R.id.btn_com).setOnClickListener(v -> addSymbol(","));
        view.findViewById(R.id.btn_log).setOnClickListener(v -> addSymbol("log("));
        view.findViewById(R.id.btn_sqrt).setOnClickListener(v -> addSymbol("sqrt("));
        view.findViewById(R.id.btn_percentage).setOnClickListener(v -> addSymbol("%"));
        view.findViewById(R.id.btn_fact).setOnClickListener(v -> addSymbol("fact("));
        view.findViewById(R.id.btn_pow).setOnClickListener(v -> addSymbol("pow("));
        view.findViewById(R.id.btn_abs).setOnClickListener(v -> addSymbol("abs("));
        view.findViewById(R.id.btn_gcd).setOnClickListener(v -> addSymbol("gcd("));
        view.findViewById(R.id.btn_lcm).setOnClickListener(v -> addSymbol("lcm("));

        // Equal
        view.findViewById(R.id.btn_equal).setOnClickListener(v -> evaluateExpression());

        view.findViewById(R.id.btn_del).setOnClickListener(v -> {
            if (expression.length() > 0) {
                expression.deleteCharAt(expression.length() - 1);
                tvDisplay.setText(expression.length() > 0 ? expression.toString() : "0");
            }
        });

        // Clear all
        view.findViewById(R.id.btn_clear).setOnClickListener(v -> {
            expression.setLength(0);
            tvDisplay.setText("0");
            isNewInput = false;
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("expressionText", expression.toString());
    }

    private void addSymbol(String s) {
        if (isNewInput) {
            isNewInput = false;
        }
        expression.append(s);
        tvDisplay.setText(expression.toString());
    }

    private void evaluateExpression() {
        if (expression.length() == 0) return;

        try {
            // Custom logarithm function with base 10
            Function log = new Function("log", 1) {
                @Override
                public double apply(double... args) {
                    double value = args[0];
                    return Math.log(value) / Math.log(10);
                }
            };

            // Custom percentage operator (%)
            Operator percent = new Operator("%", 1, true, Operator.PRECEDENCE_POWER + 1) {
                @Override
                public double apply(double... args) {
                    double x = args[0];

                    return (x/100);
                }
            };

            // Custom Function for the Greatest Common Divisor (GCD) calculation between 2 numbers
            Function gcd = new Function("gcd", 2) {
                @Override
                public double apply(double... args) {

                    int num1,num2, max, result = 1;

                    num1 = (int)(args[0]);
                    num2 = (int)(args[1]);

                    if(num1 == 0 && num2 == 0)
                    {
                        throw new ArithmeticException("Undefined GCD for 0,0");
                    }

                    if(num1 == 0)
                        return num2;
                    else if(num2 == 0)
                        return num1;

                    if(num1 < num2)
                        max = num1;
                    else
                        max = num2;

                    for(int i=max;i>=1;i--)
                    {
                        if((num1 % i == 0) && (num2 % i == 0))
                            return i;
                    }
                    return result;
                }
            };

            // Custom Function for Least Common Multiple calculation between 2 numbers
            Function lcm = new Function("lcm", 2) {
                @Override
                public double apply(double... args) {

                    int num1,num2, max;

                    num1 = (int)(args[0]);
                    num2 = (int)(args[1]);

                    if(num1 ==0 || num2 == 0)
                        return 0;

                    if(num1 > num2)
                        max = num1;
                    else
                        max = num2;

                    while((max%num1) != 0 || (max%num2) != 0)
                        max++;

                    return max;
                }
            };

            // Custom function for factorial (!) calculation
            Function factorial = new Function("fact", 1) {
                @Override
                public double apply(double... args) {
                    double x = args[0];
                    int result = 1;

                    if((int)(x)<0)
                    {
                        throw new ArithmeticException("Undefined 0!");
                    }
                    if((int)x == 0)
                    {
                        result = 1;
                    }
                    else if(x == Math.floor(x) && x>0)
                    {
                        for(int i = (int) x; i>=1; i--)
                            result *= i;
                    }

                    return result;
                }
            };

            Expression exp = new ExpressionBuilder(expression.toString())
                    .functions(log, factorial, gcd, lcm) // register custom functions
                    .operator(percent)
                    .build();

            // the exp.evaluate() performs the operation for us :)
            double result = exp.evaluate();

            // Round to a fixed number of decimals so that the calculation of double
            // numbers is done correct (this is an issue with Java and exp4j)
            result = Math.round(result * 1_000_000_000d) / 1_000_000_000d;
            tvDisplay.setText(String.valueOf(result));
            expression.setLength(0);
            expression.append(result);
        }
        catch (ArithmeticException e){
            tvDisplay.setText("Undefined");
            expression.setLength(0);
        }
        catch (Exception e) {
            tvDisplay.setText("The operation is not valid!");
            expression.setLength(0);
        }
        isNewInput = true;
    }
}

