package ru.anatomica.calculator.Controller;

import android.annotation.SuppressLint;
import android.widget.Button;
import ru.anatomica.calculator.MainActivity;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorEngine {

    private ArithmeticEngine arithmeticEngine;
    private MainActivity mainActivity;

    protected int mark = 0; // Метка

    public CalculatorEngine(MainActivity mainActivity) {
        this.arithmeticEngine = new ArithmeticEngine(mainActivity, this);
        this.mainActivity = mainActivity;
    }

    @SuppressLint("SetTextI18n")
    public void workWithButtons(String buttonLabel, Button button) {

        if (mainActivity.displayField.getText().toString().equals("На ноль делить нельзя!") ||
                mainActivity.displayField.getText().toString().equals("Неверный расчет! Повторите!")) {
            mainActivity.displayField.setText("");
            changeSize();
        }

        String displayFieldText = mainActivity.displayField.getText().toString();
        if (!displayFieldText.equals("") && !displayFieldText.equals(".") &&
                !displayFieldText.equals("-") && !displayFieldText.equals("-."))
            arithmeticEngine.displayValue = Double.parseDouble(displayFieldText);

        if (displayFieldText.equals("-") || displayFieldText.equals("-.")) {
            mainActivity.displayField.setText("0");
            displayFieldText = mainActivity.displayField.getText().toString();
        }

        final boolean b = buttonLabel.equals("1") || buttonLabel.equals("2") ||
                buttonLabel.equals("3") || buttonLabel.equals("4") ||
                buttonLabel.equals("5") || buttonLabel.equals("6") ||
                buttonLabel.equals("7") || buttonLabel.equals("8") ||
                buttonLabel.equals("9") || buttonLabel.equals("0") ||
                buttonLabel.equals(".");

        if (mark == 0 && b) {
            mainActivity.displayField.setText(displayFieldText + buttonLabel);
        }

        if (mark == 1 && b) {
            if (buttonLabel.equals(".")) return;
            mainActivity.displayField.setText("" + buttonLabel);
            mark = 0;
        }

        StringBuilder builder = new StringBuilder(mainActivity.displayField.getText());
        if ((builder.length() > 0 && builder.length() <= 3) && builder.charAt(0) == '-' && builder.charAt(1) == '0') {
            builder.deleteCharAt(1);
            mainActivity.displayField.setText(String.valueOf(builder));
        }
        if (builder.length() > 0 && builder.charAt(0) == '.') {
            builder.deleteCharAt(0);
            builder.insert(0, ".");
            builder.insert(0, "0");
            mainActivity.displayField.setText(String.valueOf(builder));
        }
        if (builder.length() > 1) {
            if (builder.charAt(0) == '0' && builder.charAt(1) != '.') {
                builder.deleteCharAt(0);
                mainActivity.displayField.setText(String.valueOf(builder));
            }
        }

        changeSize();
        arithmeticEngine.algorithmEngine(displayFieldText, buttonLabel, button, b);
    }

    public String stringWithoutZero(double result) {
        String newValue = String.valueOf(result);
        if (newValue.endsWith(".0")) newValue = newValue.substring(0, newValue.length() - 2);
        return newValue;
    }

    public String withFiveDigits(String result) {
        BigDecimal aroundResult;
        if (result.split("\\.").length > 1) {
            aroundResult = new BigDecimal(result).setScale(4, RoundingMode.HALF_EVEN);
            String stringAroundResult = String.valueOf(aroundResult);
            if (stringAroundResult.endsWith(".0000"))
                stringAroundResult = stringAroundResult.substring(0, stringAroundResult.length() - 5);
            if (stringAroundResult.endsWith("000"))
                stringAroundResult = stringAroundResult.substring(0, stringAroundResult.length() - 3);
            if (stringAroundResult.endsWith("00"))
                stringAroundResult = stringAroundResult.substring(0, stringAroundResult.length() - 2);
            if (stringAroundResult.endsWith("0"))
                stringAroundResult = stringAroundResult.substring(0, stringAroundResult.length() - 1);
            return stringAroundResult;
        }
        return result;
    }

    public void changeSize() {
        mainActivity.displayField.setTextSize(90);
        mainActivity.displayField.setPadding(0, 0, 20, 0);
        if (mainActivity.displayField.getText().toString().length() > 7) mainActivity.displayField.setTextSize(60);
        if (mainActivity.displayField.getText().toString().length() > 10) mainActivity.displayField.setTextSize(40);
        if (mainActivity.displayField.getText().toString().length() > 16) mainActivity.displayField.setTextSize(25);
    }

}
