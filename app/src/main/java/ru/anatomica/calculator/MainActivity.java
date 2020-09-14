package ru.anatomica.calculator;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.TextViewCompat;
import androidx.gridlayout.widget.GridLayout;

import android.os.StrictMode;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import ru.anatomica.calculator.Controller.CalculatorEngine;

public class MainActivity extends AppCompatActivity {

    public CalculatorEngine calculatorEngine;
    public AlertDialog.Builder alertDialog;
    public ArrayList<String> buttonName;
    public ArrayList<Button> buttons;
    public CoordinatorLayout mainLayout;
    public GridLayout buttonLayout;
    public TextView displayField;

    String[] nameOfButtons = {"Жен", "Подсчет \nСКФ", "Подсчет \nQT", "= СКФ\n= QTc", "Сброс", "<=", "(^)", "/",
            "1", "2", "3", "*", "4", "5", "6", "-", "7", "8", "9", "+", "+/-", "0", ".", "="};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.VmPolicy vmPolicy = new StrictMode.VmPolicy.Builder().build();
        StrictMode.setThreadPolicy(threadPolicy);
        StrictMode.setVmPolicy(vmPolicy);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calculatorEngine = new CalculatorEngine(this);
        mainLayout = findViewById(R.id.activity_main);
        buttonLayout = findViewById(R.id.buttonLayout);
        displayField = findViewById(R.id.digitalView);
        displayField.setEnabled(false);
        buttonName = new ArrayList<>();
        buttons = new ArrayList<>(24);
        createDisplay();
        createBtn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_about:
                about();
                break;
            case R.id.btn_howCreateSKF:
                howCreateSKF();
                break;
            case R.id.btn_howCreateCrockroft:
                howCreateCrockroft();
                break;
            case R.id.btn_howCreateQTc:
                howCreateQTc();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createDisplay() {
        displayField.setText("0");
        displayField.setTextSize(90);
        displayField.setPadding(0, 0, 20, 0);
        displayField.setTextColor(Color.BLACK);
        displayField.setBackgroundColor(Color.LTGRAY);
    }

    public void createBtn() {
        buttonName.addAll(Arrays.asList(nameOfButtons));
        for (int i = 0; i < buttonName.size(); i++) {
            buttons.add(new Button(this));
        }
        viewButton();
    }

    public void viewButton() {
        for (int i = 0; i < buttons.size(); i++) {
            final int id_ = buttons.get(i).getId();
            buttons.get(i).setId(i);
            buttons.get(i).setText(buttonName.get(i));
            buttons.get(i).getBackground().setAlpha(100);
            buttons.get(i).setTextSize(23);
            buttons.get(i).setTextColor(Color.YELLOW);
            if (i <= 6) {
                TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(buttons.get(i), 2, 14, 1, TypedValue.COMPLEX_UNIT_SP);
                buttons.get(i).setTextColor(Color.LTGRAY);
            }
            if (i == 7 || i == 11 || i == 15 || i == 19 || i == 20 || i == 22 || i == 23) {
                buttons.get(i).setTextSize(25);
                buttons.get(20).setTextSize(20);
                buttons.get(i).setTextColor(Color.LTGRAY);
            }

            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f),
                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f));
            layoutParams.width = 0;
            layoutParams.height = 0;
            buttons.get(i).setLayoutParams(layoutParams);
            buttonLayout.addView(buttons.get(i));

            final int finalI = i;
            buttons.get(i).setOnClickListener(v -> {
                calculatorEngine.workWithButtons(buttonName.get(finalI), buttons.get(finalI));
            });
            buttons.get(i).setOnLongClickListener(v -> {
                return true;
            });
        }
    }

    public void howCreateSKF() {
        showMessage("Для подсчета СКФ (по формуле CKD-EPI) вам необходимо " +
                "сначала выбрать пол пациента и ввести его возраст; потом нажать на кнопку 'Подсчет СКФ' и " +
                "ввести уровень креатинина в мкмоль/л. Для получения результата нажмите '= СКФ'", "Инструкция!");
    }

    public void howCreateCrockroft() {
        showMessage("Для подсчета СКФ (по формуле Кокрофта-Голта) вам необходимо " +
                "сначала выбрать пол пациента и ввести его возраст; потом нажать на кнопку 'Подсчет СКФ', " +
                "ввести уровень креатинина в мкмоль/л, снова нажать на ту же кнопку и " +
                "ввести вес пациента. Для получения результата нажмите '= СКФ'", "Инструкция!");
    }

    public void howCreateQTc() {
        showMessage("Для подсчета корригированного QT вам необходимо " +
                "сначала ввести ЧСС, потом нажать на кнопку 'Подсчет QT' и " +
                "ввести значение QT в мсек. Для получения результата нажмите '= QTc'", "Инструкция!");
    }

    public void about() {
        showMessage("Версия калькулятора 1.6 \nMaxim Fomin © 2020 \nВсе права защищены.", "О приложении:");
    }

    public void showMessage(String message) {
        alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Результат расчета:");
        alertDialog.setMessage(message);
        // alertDialog.setIcon(R.drawable.ic_stat_ic_notification);
        alertDialog.setPositiveButton("ОК", (dialog, which) -> { dialog.cancel();});
        alertDialog.show();
    }

    public void showMessage(String message, String type) {
        alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle(type);
        alertDialog.setMessage(message);
        // alertDialog.setIcon(R.drawable.ic_stat_ic_notification);
        alertDialog.setPositiveButton("ОК", (dialog, which) -> { dialog.cancel();});
        alertDialog.show();
    }

    public void informationMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void informationMessage(String message, String type) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
