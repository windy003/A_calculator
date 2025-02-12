package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText result;
    private boolean lastCalculated = false;  // 标记是否刚完成计算

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);
        result.setText("0");
        result.setShowSoftInputOnFocus(false);
    }

    public void onDigit(View view) {
        String digit = ((Button) view).getText().toString();
        String currentText = result.getText().toString();
        
        if (lastCalculated) {
            // 如果刚完成计算，新数字会覆盖结果
            result.setText(digit);
            lastCalculated = false;
        } else if (currentText.equals("0") && !digit.equals(".")) {
            result.setText(digit);
        } else {
            result.setText(currentText + digit);
        }
    }

    public void onOperator(View view) {
        String operator = ((Button) view).getText().toString();
        String currentText = result.getText().toString();
        
        // 如果刚完成计算，可以继续用结果进行运算
        if (lastCalculated) {
            result.setText(currentText + operator);
            lastCalculated = false;
        } else if (operator.equals("-") && currentText.equals("0")) {
            result.setText("-");
        } else if (!currentText.endsWith("+") && 
                  !currentText.endsWith("-") && 
                  !currentText.endsWith("×") && 
                  !currentText.endsWith("÷")) {
            result.setText(currentText + operator);
        }
    }

    public void onEquals(View view) {
        try {
            String expression = result.getText().toString();
            
            // 查找运算符
            int operatorIndex = -1;
            char operator = ' ';
            
            for (int i = 1; i < expression.length(); i++) {
                char c = expression.charAt(i);
                switch (c) {
                    case '+':
                    case '×':
                    case '÷':
                        operatorIndex = i;
                        operator = c;
                        i = expression.length(); // 跳出循环
                        break;
                    case '-':
                        if (i > 0 && !isOperator(expression.charAt(i-1))) {
                            operatorIndex = i;
                            operator = c;
                            i = expression.length(); // 跳出循环
                        }
                        break;
                }
            }

            if (operatorIndex == -1) return;

            // 计算
            double num1 = Double.parseDouble(expression.substring(0, operatorIndex));
            double num2 = Double.parseDouble(expression.substring(operatorIndex + 1));
            
            double finalResult;
            switch (operator) {
                case '+':
                    finalResult = num1 + num2;
                    break;
                case '-':
                    finalResult = num1 - num2;
                    break;
                case '×':
                    finalResult = num1 * num2;
                    break;
                case '÷':
                    if (num2 == 0.0) {
                        Toast.makeText(this, "除数不能为零", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    finalResult = num1 / num2;
                    break;
                default:
                    return;
            }

            // 显示结果
            String resultText;
            if (finalResult % 1.0 == 0.0) {
                resultText = String.valueOf((int) finalResult);
            } else {
                resultText = String.format("%.2f", finalResult);
            }

            result.setText(resultText);
            lastCalculated = true;  // 标记刚完成计算

        } catch (Exception e) {
            Toast.makeText(this, "计算错误", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClear(View view) {
        result.setText("0");
        lastCalculated = false;
    }

    public void onCE(View view) {
        String currentText = result.getText().toString();
        if (currentText.length() <= 1) {
            result.setText("0");
        } else {
            result.setText(currentText.substring(0, currentText.length() - 1));
        }
        lastCalculated = false;
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '×' || c == '÷';
    }
} 