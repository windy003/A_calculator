package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var result: EditText
    private var lastCalculated = false  // 标记是否刚完成计算

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        result = findViewById(R.id.result)
        result.setText("0")
        result.showSoftInputOnFocus = false
    }

    fun onDigit(view: View) {
        val digit = (view as Button).text.toString()
        val currentText = result.text.toString()
        
        if (lastCalculated) {
            // 如果刚完成计算，新数字会覆盖结果
            result.setText(digit)
            lastCalculated = false
        } else if (currentText == "0" && digit != ".") {
            result.setText(digit)
        } else {
            result.setText(currentText + digit)
        }
    }

    fun onOperator(view: View) {
        val operator = (view as Button).text.toString()
        val currentText = result.text.toString()
        
        // 如果刚完成计算，可以继续用结果进行运算
        if (lastCalculated) {
            result.setText(currentText + operator)
            lastCalculated = false
        } else if (operator == "-" && currentText == "0") {
            result.setText("-")
        } else if (!currentText.endsWith("+") && 
                  !currentText.endsWith("-") && 
                  !currentText.endsWith("×") && 
                  !currentText.endsWith("÷")) {
            result.setText(currentText + operator)
        }
    }

    fun onEquals(view: View) {
        try {
            val expression = result.text.toString()
            
            // 查找运算符
            var operatorIndex = -1
            var operator = ' '
            
            for (i in 1 until expression.length) {
                when (expression[i]) {
                    '+', '×', '÷' -> {
                        operatorIndex = i
                        operator = expression[i]
                        break
                    }
                    '-' -> {
                        if (i > 0 && !isOperator(expression[i-1])) {
                            operatorIndex = i
                            operator = expression[i]
                            break
                        }
                    }
                }
            }

            if (operatorIndex == -1) return

            // 计算
            val num1 = expression.substring(0, operatorIndex).toDouble()
            val num2 = expression.substring(operatorIndex + 1).toDouble()
            
            val finalResult = when (operator) {
                '+' -> num1 + num2
                '-' -> num1 - num2
                '×' -> num1 * num2
                '÷' -> {
                    if (num2 == 0.0) {
                        Toast.makeText(this, "除数不能为零", Toast.LENGTH_SHORT).show()
                        return
                    }
                    num1 / num2
                }
                else -> return
            }

            // 显示结果
            val resultText = if (finalResult % 1.0 == 0.0) {
                finalResult.toInt().toString()
            } else {
                String.format("%.2f", finalResult)
            }

            result.setText(resultText)
            lastCalculated = true  // 标记刚完成计算

        } catch (e: Exception) {
            Toast.makeText(this, "计算错误", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClear(view: View) {
        result.setText("0")
        lastCalculated = false
    }

    fun onCE(view: View) {
        val currentText = result.text.toString()
        if (currentText.length <= 1) {
            result.setText("0")
        } else {
            result.setText(currentText.substring(0, currentText.length - 1))
        }
        lastCalculated = false
    }

    private fun isOperator(c: Char): Boolean {
        return c in setOf('+', '-', '×', '÷')
    }
} 