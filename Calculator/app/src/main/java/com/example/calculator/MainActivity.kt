package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {

    private var tvResult: TextView? = null
    var isLastNumeric: Boolean = true
    var isOperatorAdded: Boolean = false
    var isDotAdded: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)
    }

    fun onDigit(view: View) {
        val text: String = (view as Button).text as String
        tvResult?.append(text)
        isLastNumeric = true
    }

    fun onOperator(view: View) {
        isDotAdded = false

        if (isOperatorAdded) {
            val text: String = tvResult?.text.toString()
            if (isOperatorAdded(text)) {
                tvResult?.text = text.substring(0, text.length - 1)
                tvResult?.append((view as Button).text)
            } else {
                onEqual(view)
                isOperatorAdded = false
                isLastNumeric = false
            }
            return
        }

        if (tvResult?.text?.length == 0) {
            if ((view as Button).text.toString().equals("-")) {
                tvResult?.append(view.text)
                isLastNumeric = false
            }
            return
        }

        val text: String = tvResult?.text.toString()

        if (isLastNumeric && !isOperatorAdded(text)) {
            tvResult?.append((view as Button).text)
            isLastNumeric = false
            isOperatorAdded = true
        }
    }

    private fun isOperatorAdded(value: String): Boolean {
        return "+-*/".contains(value.elementAt(value.length - 1))
    }

    private fun splitValues(value: String, delimiter: String): Pair<Double, Double> {
        var prefix = 1
        var valueString = value

        if (valueString.startsWith("-")) {
            valueString = valueString.substring(1)
            prefix = -1
        }

        val splitValue = valueString.split(delimiter)

        var one = splitValue[0].toDouble() * prefix
        var two = splitValue[1].toDouble()

        return Pair(one, two)
    }

    private fun removeEndZeros(result: String): String {
        if (result.endsWith(".0")) {
            return result.substring(0, result.length - 2)
        }
        return result
    }

    private fun calculateAndSetResult(a: Double, b: Double, operation: (Double, Double) -> Double) {
        var result: Double = operation(a, b)
        tvResult?.text = removeEndZeros(result.toString())
        isOperatorAdded = false
        isDotAdded = false
    }

    fun onEqual(view: View) {
        if (isLastNumeric) {
            val tvValue = tvResult?.text.toString()

            try {
                with(tvValue) {
                    when {
                        contains("-") -> {
                            var (a, b) = splitValues(this, "-")
                            calculateAndSetResult(a, b, Double::minus)
                        }
                        contains("+") -> {
                            var (a, b) = splitValues(this, "+")
                            calculateAndSetResult(a, b, Double::plus)
                        }
                        contains("*") -> {
                            var (a, b) = splitValues(this, "*")
                            calculateAndSetResult(a, b, Double::times)
                        }
                        contains("/") -> {
                            var (a, b) = splitValues(this, "/")
                            calculateAndSetResult(a, b, Double::div)
                        }
                    }
                }
            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    fun onClear(view: View) {
        tvResult?.text = ""
        isLastNumeric = false
        isDotAdded = false
        isOperatorAdded = false
    }

    fun onDecimalPoint(view: View) {
        if (isLastNumeric && !isDotAdded) {
            tvResult?.append(".")
            isLastNumeric = false
            isDotAdded = true
        }
    }
}