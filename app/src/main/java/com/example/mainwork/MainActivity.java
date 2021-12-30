package com.example.mainwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button mainbt;

    EditText down_puls;
    EditText up_puls;

    Spinner sp_day;
    Spinner sp_month;
    Spinner sp_year;
    Spinner sp_sex;

    String[] results = {"good","okey", "bad", "error"};
    Integer[] days = new Integer[31];
    String[] months = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
    Integer[] years = new Integer[100];
    String[] sexs = {"М", "Ж"};
    int day, year;
    int inMonth = -1;
    int firstPuls, seconfPuls;
    String sex, month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // вызываем функцию для заполнения
        addVariables();
        //  переменные
        mainbt = findViewById(R.id.mainbt);
        mainbt.setOnClickListener(this);

        down_puls = findViewById(R.id.down_puls);
        up_puls = findViewById(R.id.up_puls);

        ArrayAdapter<Integer> days_adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, days);
        // указываем какой layout использовать для прорисовки пунктов выпадающего списка.
        days_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_day = (Spinner) findViewById(R.id.sp_day);
        sp_day.setAdapter(days_adapter);

        ArrayAdapter<String> months_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        months_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_month = (Spinner) findViewById(R.id.sp_month);
        sp_month.setAdapter(months_adapter);

        ArrayAdapter<Integer> years_adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, years);
        years_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_year = (Spinner) findViewById(R.id.sp_year);
        sp_year.setAdapter(years_adapter);

        ArrayAdapter<String> sex_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexs);
        sex_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_sex = (Spinner) findViewById(R.id.sp_sex);
        sp_sex.setAdapter(sex_adapter);

    }

    // Заполняем наши массивы числами, чтобы не делать это вручную
    public void addVariables() {
        for (int i = 0; i < years.length; i++) {
            years[i] = 2021 - i;
        }
        for (int i = 0; i < days.length; i++) {
            days[i] = i + 1;
        }

    }

    // для месяцев-30 дней и февраля
    public boolean checkInf() {
        switch (month) {
            case "Сентябрь":
                if (day <= 30) {
                    return true;
                }
                break;
            case "Ноябрь":
                if (day <= 30) {
                    return true;
                }
                break;
            case "Апрель":
                if (day <= 30) {
                    return true;
                }
                break;
            case "Июнь":
                if (day <= 30) {
                    return true;
                }
                break;
            case "Февраль":
                if (year % 4 == 0 && day <= 29 || day <= 28) {
                    return true;
                }
                break;
            default:
                return true;
        }
        return false;
    }



    public void chGender(){
        if (sex.equals("М")){
            sex = "1";
        } else {
            sex = "2";
        }
    }
    public void chMonth(){
        for (int i = 0; (i < months.length) && (inMonth == -1); i++) {
            if (months[i] == month) {
                inMonth = i+1;
            }
        }
    }
    @Override
    public void onClick(View view) {
        // проверяем на пустые значения
        if (up_puls.getText().toString().equals("") || down_puls.getText().toString().equals("")) {
            Toast.makeText(this, "Отсутствие значений!", Toast.LENGTH_SHORT).show();
        } else {
            day = Integer.parseInt(sp_day.getSelectedItem().toString());
            month = sp_month.getSelectedItem().toString();
            year = Integer.parseInt(sp_year.getSelectedItem().toString());
            sex = sp_sex.getSelectedItem().toString();

            // проверяем корректность данных
            if (!checkInf()){
                Toast.makeText(this, "Проверьте корректность даты!", Toast.LENGTH_SHORT).show();
            } else {
                // проверяем сам результат
                chGender();
                chMonth();
                firstPuls = Integer.parseInt(String.valueOf(down_puls.getText()));
                seconfPuls = Integer.parseInt(String.valueOf(up_puls.getText()));
                getThreadResponse();
//                int age = 2021-year;
//                result = results[(int)(Math.random()*3)];
//                double percent = Math.abs(seconfPuls - firstPuls);
//                int main_pulse = (firstPuls + seconfPuls)/2;
                // int diffPulse = Math.abs(seconfPuls - firstPuls);
                // переходим на другое Activity
//                Intent intent = new Intent(this, Result.class);
//                intent.putExtra("first", firstPuls);
//                intent.putExtra("second", seconfPuls);
//                intent.putExtra("result", result);
////                intent.putExtra("percent", percent);
//                startActivity(intent);
            }
        }

    }
    public void getThreadResponse() {
//        Thread thread = new Thread(() -> {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // URL; объект класса java.net.URL
                    URL url = new URL("http://abashin.ru/cgi-bin/ru/tests/burnout");
                    //вызываем метод openConnection() который возвратит нам HttpUrlConnection.
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //add reuqest header
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Host", "abashin.ru");
                    connection.setRequestProperty("Connection", "close");
                    connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3);q=0.9");
                    connection.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //Request Parameters you want to send
                    String parameters = "day=" + day + "&month=" + inMonth + "&year=" + year + "&sex=" + sex + "&m1=" + firstPuls + "&m2=" + seconfPuls;
                    // Send post request
                    //Чтобы загрузить (и получить) данные на веб-сервер через это соединение
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    //Передача данных, записанных в поток
                    OutputStream outputStream = connection.getOutputStream();
                    //Log.d("Hello", "2");
                    //чтоб записать наше тело запроса используем write()
                    outputStream.write(parameters.getBytes(StandardCharsets.UTF_8));
                    outputStream.close();
                    // Читает текст из потока ввода символов
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String infdata;
                    // изменяемый класс, где хранятся временные данные
                    StringBuilder response = new StringBuilder();
                    // читает строку текста пока не null
                    while ((infdata = bufferedReader.readLine()) != null) {
                        response.append(infdata);
                    }
                    // закрываем поток
                    bufferedReader.close();
                    String parsedData = new String(response.toString().getBytes(), StandardCharsets.UTF_8).replaceAll("<.*?>", "");
                    //System.out.println(parsedData);
                    String all_res;
                    if (parsedData.contains("отсутствию переутомления")) {
                        all_res = results[0];
                    } else if (parsedData.contains("небольшому переутомлению")) {
                        all_res = results[1];
                    } else if (parsedData.contains("высокому уровню переутомления")) {
                        all_res = results[2];
                    } else {
                        all_res = results[3];
                    }
                    Intent intent = new Intent("com.res.mainwork.action.results");
                    intent.putExtra("result", all_res);
                    startActivity(intent);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }

        });
        thread.start();
    }
}