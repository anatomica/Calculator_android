package ru.anatomica.calculator.Controller;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.TypedValue;
import android.widget.Button;

import androidx.core.widget.TextViewCompat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import ru.anatomica.calculator.MainActivity;

public class ArithmeticEngine {

    private MainActivity mainActivity;
    private CalculatorEngine calculatorEngine;
    private SKFEngine skfEngine;

    protected char action; // Арифметический метод
    protected double result = 0; // Результат выражения или значения
    protected double displayValue = 0; // Значение на экране

    public ArithmeticEngine(MainActivity mainActivity, CalculatorEngine calculatorEngine) {
        this.skfEngine = new SKFEngine(mainActivity, this);
        this.calculatorEngine = calculatorEngine;
        this.mainActivity = mainActivity;
    }

    @SuppressLint("SetTextI18n")
    public void algorithmEngine(String displayFieldText, String buttonLabel, Button button, boolean b) {

        if (displayFieldText.equals("-0.") & !b) {
            mainActivity.displayField.setText("0");
            return;
        }
        if (button == mainActivity.buttons.get(4)) {
            skfEngine.QT = 0;
            skfEngine.SKF = 0;
            skfEngine.age = 0;
            result = 0;
            skfEngine.kreatinin = 0;
            displayValue = 0;
            mainActivity.displayField.setText("0");
            calculatorEngine.changeSize();
            mainActivity.buttons.get(1).setText("Подсчет \nСКФ");
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) mainActivity.buttons.get(1).setAutoSizeTextTypeUniformWithConfiguration(2, 14, 1, TypedValue.COMPLEX_UNIT_SP);
            else TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(mainActivity.buttons.get(1), 2, 18, 1, TypedValue.COMPLEX_UNIT_SP);
            mainActivity.buttons.get(2).setText("Подсчет \nQT");
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) mainActivity.buttons.get(2).setAutoSizeTextTypeUniformWithConfiguration(2, 14, 1, TypedValue.COMPLEX_UNIT_SP);
            else TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(mainActivity.buttons.get(2), 2, 18, 1, TypedValue.COMPLEX_UNIT_SP);
        }
        else if (button == mainActivity.buttons.get(5)) {
            String str = mainActivity.displayField.getText().toString();
            if (str.length() > 0) {
                str = str.substring(0, str.length() - 1);
                mainActivity.displayField.setText(str);
            }
        } else if (button == mainActivity.buttons.get(19)) {
            action = '+';
            result = displayValue;
            calculatorEngine.mark = 1;
        } else if (button == mainActivity.buttons.get(15)) {
            if (displayValue == 0 && mainActivity.displayField.getText().equals(""))
                mainActivity.displayField.setText(buttonLabel + "0");
            else {
                action = '-';
                result = displayValue;
                calculatorEngine.mark = 1;
            }
        } else if (button == mainActivity.buttons.get(7)) {
            action = '/';
            result = displayValue;
            calculatorEngine.mark = 1;
        } else if (button == mainActivity.buttons.get(11)) {
            action = '*';
            result = displayValue;
            calculatorEngine.mark = 1;
        } else if (button == mainActivity.buttons.get(22)) {
            if (displayFieldText.indexOf(".") > 0) {
                mainActivity.displayField.setText(displayFieldText + "");
            } else {
                mainActivity.displayField.setText(displayFieldText + buttonLabel);
            }
        } else if (button == mainActivity.buttons.get(6)) {
            action = '^';
            result = displayValue;
            calculatorEngine.mark = 1;
        } else if (button == mainActivity.buttons.get(0)) {
            skfEngine.sex = 'W';
            if (button.getText().equals("Жен")) {
                button.setText("Муж");
                skfEngine.sex = 'M';
            } else {
                button.setText("Жен");
                skfEngine.sex = 'W';
            }
        } else if (button == mainActivity.buttons.get(1)) {
            if (button.getText().equals("Подсчет \nСКФ")) {
                button.setText("Введите \nкреатинин \nи нажмите");
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) button.setAutoSizeTextTypeUniformWithConfiguration(2, 10, 1, TypedValue.COMPLEX_UNIT_SP);
                else TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(button, 2, 18, 1, TypedValue.COMPLEX_UNIT_SP);
                action = 'K';
                skfEngine.age = displayValue;
                calculatorEngine.mark = 1;
                skfEngine.SKF = 1;
            }
            else if (button.getText().equals("Введите \nкреатинин \nи нажмите")) {
                button.setText("Введите \nвес");
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) button.setAutoSizeTextTypeUniformWithConfiguration(2, 12, 1, TypedValue.COMPLEX_UNIT_SP);
                else TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(button, 2, 18, 1, TypedValue.COMPLEX_UNIT_SP);
                action = 'W';
                skfEngine.kreatinin = displayValue;
                calculatorEngine.mark = 1;
                skfEngine.SKF = 1;
            }
        } else if (button == mainActivity.buttons.get(2)) {
            if (button.getText().equals("Подсчет \nQT")) {
                button.setText("Введите \nQT в мсек");
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) button.setAutoSizeTextTypeUniformWithConfiguration(2, 10, 1, TypedValue.COMPLEX_UNIT_SP);
                else TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(button, 2, 18, 1, TypedValue.COMPLEX_UNIT_SP);
                action = 'Q';
                result = displayValue;
                calculatorEngine.mark = 1;
                skfEngine.QT = 1;
            }
        } else if ((skfEngine.SKF == 1 || skfEngine.QT == 1) && button == mainActivity.buttons.get(3) && displayValue != 0) {
            if (skfEngine.SKF == 1 && action == 'K') mainActivity.showMessage(skfEngine.resultSKF("fromSKF"));
            if (skfEngine.SKF == 1 && action == 'W') mainActivity.showMessage(skfEngine.resultCockroft());
            if (skfEngine.QT == 1 && action == 'Q') mainActivity.showMessage(skfEngine.resultQTc());
        } else if (button == mainActivity.buttons.get(20)) {
            StringBuilder str = new StringBuilder(displayFieldText);
            if (str.length() > 0 && str.charAt(0) != '-') {
                str.insert(0, "-");
                mainActivity.displayField.setText(String.valueOf(str));
            } else if (str.length() > 0 && str.charAt(0) == '-') {
                str.deleteCharAt(0);
                mainActivity.displayField.setText(String.valueOf(str));
            }
        } else if (button == mainActivity.buttons.get(23)) {

            //  Арифметическое действие
            if (action == '+') {
                result = result + displayValue;
                mainActivity.displayField.setText("" + calculatorEngine.withFiveDigits(calculatorEngine.stringWithoutZero(result)));
                calculatorEngine.changeSize();
            } else if (action == '-') {
                result = result - displayValue;
                mainActivity.displayField.setText("" + calculatorEngine.withFiveDigits(calculatorEngine.stringWithoutZero(result)));
                calculatorEngine.changeSize();
            } else if (action == '/') {
                if (displayValue == 0) {
                    mainActivity.displayField.setTextSize(30);
                    mainActivity.displayField.setText("На ноль делить нельзя!");
                } else {
                    result = result / displayValue;
                    mainActivity.displayField.setText("" + calculatorEngine.withFiveDigits(calculatorEngine.stringWithoutZero(result)));
                    calculatorEngine.changeSize();
                }
            } else if (action == '*') {
                result = result * displayValue;
                mainActivity.displayField.setText("" + calculatorEngine.withFiveDigits(calculatorEngine.stringWithoutZero(result)));
                calculatorEngine.changeSize();
            } else if (action == '^') {
                double oldResult = result;
                for (int i = 1; i < displayValue; i++) {
                    result = result * oldResult;
                }
                mainActivity.displayField.setText("" + calculatorEngine.withFiveDigits(calculatorEngine.stringWithoutZero(result)));
                calculatorEngine.changeSize();
            } else if (action == 'K' && skfEngine.SKF == 1 && displayValue != 0) {
                skfEngine.resultSKF("");
            } else if (action == 'W' && skfEngine.SKF == 1 && displayValue != 0) {
                skfEngine.resultCockroft();
            } else if (action == 'Q' && skfEngine.QT == 1 && displayValue != 0) {
                skfEngine.resultQTc();
            } else if (action == 'I') {
                result = result / ((displayValue / 100) * (displayValue / 100));
                BigDecimal aroundIMT;
                if (result > 0.0) {
                    aroundIMT = new BigDecimal(result).setScale(1, RoundingMode.HALF_EVEN);
                    mainActivity.displayField.setText("" + aroundIMT);
                }
                else mainActivity.showMessage("Вы не верно ввели данные!");
            }
        }
    }
}
