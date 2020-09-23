package ru.anatomica.calculator.Controller;

import android.annotation.SuppressLint;
import java.math.BigDecimal;
import java.math.RoundingMode;
import ru.anatomica.calculator.MainActivity;

public class SKFEngine {

    private MainActivity mainActivity;
    private ArithmeticEngine arithmeticEngine;

    protected char sex = 'W';
    protected double age;
    protected double kreatinin;
    protected String value;
    protected int SKF = 0;
    protected int QT = 0;

    public SKFEngine(MainActivity mainActivity, ArithmeticEngine arithmeticEngine) {
        this.mainActivity = mainActivity;
        this.arithmeticEngine = arithmeticEngine;
    }

    @SuppressLint("SetTextI18n")
    public String resultSKF(String from) {
        double newAge = Math.pow (0.993, age);
        if (from.equals("fromSKF")) kreatinin =  arithmeticEngine.displayValue;
        double mg = kreatinin/88.4;
        double skf = 1;
        double GFR = 1;
        if (sex == 'W' && mg <= 0.7) {
            skf = Math.pow ((mg/0.7), -0.329);
            GFR = 144 * skf * newAge;
        }
        if (sex == 'W' && mg > 0.7) {
            skf = Math.pow ((mg/0.7), -1.209);
            GFR = 144 * skf * newAge;
        }
        if (sex == 'M' && mg <= 0.9) {
            skf = Math.pow ((mg/0.9), -0.411);
            GFR = 141 * skf * newAge;
        }
        if (sex == 'M' && mg > 0.9) {
            skf = Math.pow ((mg/0.9), -1.209);
            GFR = 141 * skf * newAge;
        }

        if (age <= 0.0) {
            return "Неверный расчет! \nЗначение 'возраст' отрицательное! \nПожалуйста, повторите вычисление!";
        }
        if (mg <= 0.0) {
            return "Неверный расчет! \nЗначение 'креатинин' отрицательное! \nПожалуйста, повторите вычисление!";
        }
        if (skf <= 0.0) {
            return "Неверный расчет! \nВычисление 'СКФ' отрицательное! \nПожалуйста, повторите вычисление!";
        }

        BigDecimal aroundGFR;
        if (Double.isNaN(GFR) || Double.isInfinite(GFR)) {
            return "Неверный расчет! \nВы частично не указали данные!";
        }
        else if (GFR <= 0.0) {
            return "Неверный расчет! \nЗначение вычисления отрицательное! \nПожалуйста, посмотрите инструкцию \nи повторите вычисление!";
        }
        else aroundGFR = new BigDecimal(GFR).setScale(0, RoundingMode.HALF_EVEN);

        mainActivity.buttons.get(1).setText("Подсчет \nСКФ");
        mainActivity.changeTextSize(1);
        if (from.equals("fromSKF")) mainActivity.displayField.setText("" + aroundGFR);

        if (GFR > 90)
            value = "1";
        if (GFR< 90 && GFR >= 60)
            value = "2";
        if (GFR < 60 && GFR >= 45)
            value = "3а";
        if (GFR < 45 && GFR >= 30)
            value = "3б";
        if (GFR < 30 && GFR >= 15)
            value = "4";
        if (GFR < 15 && GFR > 0)
            value = "5";

        SKF = 0;
        return ("СКФ (по формуле CKD-EPI): = " + aroundGFR + " мл/мин/1,73м2\n" +
                "Градация " + value + "  (по классификации KDIGO)");
    }

    @SuppressLint("SetTextI18n")
    public String resultCockroft() {
        double weight = arithmeticEngine.displayValue;
        double men = 1.23;
        double women = 1.05;
        double GFR = 1;
        if (sex == 'M') GFR = men * ((140 - age) * weight) / kreatinin;
        if (sex == 'W') GFR = women * ((140 - age) * weight) / kreatinin;

        if (age <= 0.0) {
            return "Неверный расчет! \nЗначение 'возраст' отрицательное! \nПожалуйста, повторите вычисление!";
        }
        if (weight <= 0.0) {
            return "Неверный расчет! \nЗначение 'вес' отрицательное! \nПожалуйста, повторите вычисление!";
        }

        BigDecimal aroundGFR;
        if (Double.isNaN(GFR) || Double.isInfinite(GFR)) {
            return "Неверный расчет! \nВы частично не указали данные!";
        }
        else if (GFR <= 0.0) {
            return "Неверный расчет! \nЗначение вычисления отрицательное! \nПожалуйста, посмотрите инструкцию \nи повторите вычисление!";
        }
        else aroundGFR = new BigDecimal(GFR).setScale(0, RoundingMode.HALF_EVEN);

        mainActivity.buttons.get(1).setText("Подсчет \nСКФ");
        mainActivity.changeTextSize(1);
        mainActivity.displayField.setText("" + aroundGFR);
        SKF = 0;

        if (sex == 'M') return (resultSKF("fromCockroft") + "\n\nСКФ (по формуле Кокрофта-Голта): = " + aroundGFR + " мл/мин\n" +
                "В норме для мужчин: 90 - 150 мл/мин");
        else return (resultSKF("fromCockroft") + "\n\nСКФ (по формуле Кокрофта-Голта): = " + aroundGFR + " мл/мин\n" +
                "В норме для женщин: 90 - 130 мл/мин");
    }

    @SuppressLint("SetTextI18n")
    public String resultQTc() {
        if (arithmeticEngine.result >= 60 && arithmeticEngine.result <= 100) {
            double RR = 60 / arithmeticEngine.result;
            double QTc = arithmeticEngine.displayValue / Math.sqrt(RR);

            if (arithmeticEngine.result <= 0.0) {
                return "Неверный расчет! \nЗначение 'ЧСС' отрицательное! \nПожалуйста, повторите вычисление!";
            }
            if (arithmeticEngine.displayValue <= 0.0) {
                return "Неверный расчет! \nВычисление 'QT' отрицательное! \nПожалуйста, повторите вычисление!";
            }

            BigDecimal aroundQTc;
            if (Double.isNaN(QTc) || Double.isInfinite(QTc)) {
                mainActivity.showMessage("Неверный расчет! \nВы частично не указали данные!");
                return "false";
            }
            else if (QTc <= 0.0) {
                return "Неверный расчет! \nЗначение вычисления отрицательное! \nПожалуйста, посмотрите инструкцию \nи повторите вычисление!";
            }
            else aroundQTc = new BigDecimal(QTc).setScale(0, RoundingMode.HALF_EVEN);

            mainActivity.displayField.setText("" + aroundQTc);
            QT = 0;
            return ("QTc (по формуле Базетта) = " + aroundQTc + " мсек\n\nРеферентные значения корригированного QT: \n320-430 для мужчин и 320-450 для женщин");
        } else {
            double RR = 60 / arithmeticEngine.result;
            double cons = 0.154;
            double QTc = arithmeticEngine.displayValue + (cons * (1 - RR)) * 1000;

            if (arithmeticEngine.result <= 0.0) {
                return "Неверный расчет! \nЗначение 'ЧСС' отрицательное! \nПожалуйста, повторите вычисление!";
            }
            if (arithmeticEngine.displayValue <= 0.0) {
                return "Неверный расчет! \nВычисление 'QT' отрицательное! \nПожалуйста, повторите вычисление!";
            }

            BigDecimal aroundQTc;
            if (Double.isNaN(QTc) || Double.isInfinite(QTc)) {
                mainActivity.showMessage("Неверный расчет! \nВы частично не указали данные!");
                return "false";
            }
            else if (QTc <= 0.0) {
                return "Неверный расчет! \nЗначение вычисления отрицательное! \nПожалуйста, посмотрите инструкцию \nи повторите вычисление!";
            }
            else aroundQTc = new BigDecimal(QTc).setScale(0, RoundingMode.HALF_EVEN);

            mainActivity.displayField.setText("" + aroundQTc);
            QT = 0;
            return ("QTc (по формуле Framingham) = " + aroundQTc + " мсек\n\nРеферентные значения корригированного QT: \n320-430 для мужчин и 320-450 для женщин");
        }
    }
}
