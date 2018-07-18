package mycalculator.linkon.siddique.mycalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView screen;
    private String display = "";
    private String listToString = "";
    private double dataResult;
    private boolean dotPressed = false;
    private boolean finalResult = false;
    private String lastValue = "";
    private String secondLastOperator = "";
    private float value;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);
        }
        else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_landscape);
        }
        screen = (TextView) findViewById(R.id.textView);
        screen.setText(display);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("str", display);
        savedInstanceState.putBoolean("bool1", dotPressed);
        savedInstanceState.putBoolean("bool2", finalResult);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String myString = savedInstanceState.getString("str");
        dotPressed = savedInstanceState.getBoolean("bool1");
        finalResult = savedInstanceState.getBoolean("bool2");
        display = myString;
        updateScreen();
    }

    private void updateScreen() {
        screen.setText(display);
    }

    public void onClickDigit(View v) {
        Button btn = (Button) v;
        if(finalResult) {
            display = "";
            display += btn.getText();
            finalResult = false;
            updateScreen();
        }
        else {
            display += btn.getText();
            updateScreen();
        }
    }

    public void onClickDot(View v) {
        Button btn = (Button) v;
        if(dotPressed) {
            return;
        }
        else {
            if (finalResult) {
                display = "";
                display += btn.getText();
                finalResult = false;
                dotPressed = true;
            }
            else {
                display += btn.getText();
                dotPressed = true;
            }
        }
        updateScreen();
    }

    public void onClickOperator(View v) {
        Button btn = (Button) v;

        if (display.equals("")) {
            return;
        }
        else {
            String lastChar = display.substring(display.length() -1);

            if(lastChar.equals("0") || lastChar.equals("1") || lastChar.equals("2") || lastChar.equals("3") || lastChar.equals("4") || lastChar.equals("5") || lastChar.equals("6") || lastChar.equals("7") || lastChar.equals("8") || lastChar.equals("9")){
                display += btn.getText();
                dotPressed = false;
                finalResult = false;
            }
            else if (lastChar.equals("+") || lastChar.equals("-") || lastChar.equals("*") || lastChar.equals("/")) {
                display = display.substring(0, display.length() -1);
                display += btn.getText();
                dotPressed = false;
                finalResult = false;
            }
        }

        updateScreen();
    }

    public void onClickClear(View v) {
        display = "";
        dotPressed = false;
        finalResult = false;
        updateScreen();
    }

    public void onClickDel(View v) {
        if(display.length()!=0) {
            String lastChar = display.substring(display.length() -1);
            if (lastChar.equals(".")){
                display = display.substring(0, display.length() -1);
                dotPressed = false;
            }
            else {
                display = display.substring(0, display.length() -1);
            }
        }
        updateScreen();
    }

    private void writeInFile(String beforeEqual, String afterEqual) {
        try {
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput("Text.txt", MODE_APPEND));
            String hist = beforeEqual + " = " + afterEqual + "\n";
            out.write(hist);
            out.close();
            Log.d("a", "Write Done");
            Log.d("b", hist);
        }
        catch (Exception e) {
            Toast.makeText(this, "Write Exception", Toast.LENGTH_SHORT);
            Log.d("a", e.getMessage());
        }
    }

    private static List<String> getResultArrayOf(String str , ArrayList<String> operators) {
        List<String> result = new ArrayList<String>();
        String data = "";

        for(int i = 0 ; i < str.length();i++) {
            String currentElement = "" + str.charAt(i);

            if(operators.contains(currentElement)) {
                result.add(data);
                result.add(currentElement);
                data = "";
                continue;
            }

            if(!currentElement.equals(" "))
                data += currentElement;
        }

        if(!data.equals(""))
            result.add(data);

        return result;

    }

    private static List<String> getResult(int index1, int index2, double calculation, List<String> result) {
        boolean endFound = false;
        List<String> newResult = new ArrayList<String>();

        for (int index=0; index<result.size()+1; index++) {
            if (index >= index1 && index <= index2) {
                if (index == index2) {
                    endFound = true;
                }
                continue;
            }

            if (endFound) {
                newResult.add(String.valueOf(calculation));
                endFound = false;
            }

            if (index < result.size()) {
                newResult.add(result.get(index));
            }
        }

        return newResult;
    }

    private void calculation() {
        ArrayList<String> operators = new ArrayList<String>();
        operators.add("+");
        operators.add("-");
        operators.add("*");
        operators.add("/");
        //List<String> result = getResultArrayOf(display, operators);

        String beforeEqual = display;
        String afterEqual = "";
        List<String> result = new ArrayList<String>();
        String lastChar = display.substring(display.length() -1);

        if(lastChar.equals("+") || lastChar.equals("-") || lastChar.equals("*") || lastChar.equals("/")) {
            result = getResultArrayOf(display.substring(0, display.length() -1), operators);
        }
        else result = getResultArrayOf(display, operators);

        if (result.size() > 1) {
            lastValue = result.get(result.size()-1);
            secondLastOperator = result.get(result.size()-2);

            while (result.size()!=1) {

                for (int i=0; i<result.size(); i++) {
                    String data = result.get(i);
                    if (operators.contains(data)) {

                        if (data.equals("*")) {
                            dataResult = Double.parseDouble(result.get(i-1)) * Double.parseDouble(result.get(i+1));
                            System.out.println(dataResult);
                            result = getResult(i-1, i+1, dataResult, result);
                            break;
                        }

                        if (data.equals("/")) {
                            dataResult = Double.parseDouble(result.get(i-1)) / Double.parseDouble(result.get(i+1));
                            result = getResult(i-1, i+1, dataResult, result);
                            break;
                        }

                        if (data.equals("+")) {
                            dataResult = Double.parseDouble(result.get(i-1)) + Double.parseDouble(result.get(i+1));
                            result = getResult(i-1, i+1, dataResult, result);
                            break;
                        }

                        if (data.equals("-")) {
                            dataResult = Double.parseDouble(result.get(i-1)) - Double.parseDouble(result.get(i+1));
                            result = getResult(i-1, i+1, dataResult, result);
                            break;
                        }
                    }
                }
            }

            for (String s : result) {
                listToString += s;
            }

            display = listToString;
            afterEqual = listToString;
            listToString = "";
            finalResult = true;
            dotPressed = false;
            writeInFile(beforeEqual, afterEqual);
            updateScreen();
        }
        else return;
    }

    private void secondValueIncrement() {
        double fnresult = 0.0;
        if(secondLastOperator.equals("+")) {
            fnresult = Double.parseDouble(display) + Double.parseDouble(lastValue);
            writeInFile(display + "+" + lastValue, String.valueOf(fnresult));
        }
        else if(secondLastOperator.equals("-")) {
            fnresult = Double.parseDouble(display) - Double.parseDouble(lastValue);
            writeInFile(display + "-" + lastValue, String.valueOf(fnresult));
        }
        else if(secondLastOperator.equals("*")) {
            fnresult = Double.parseDouble(display) * Double.parseDouble(lastValue);
            writeInFile(display + "*" + lastValue, String.valueOf(fnresult));
        }
        else if(secondLastOperator.equals("/")) {
            fnresult = Double.parseDouble(display) / Double.parseDouble(lastValue);
            writeInFile(display + "/" + lastValue, String.valueOf(fnresult));
        }

        display = String.valueOf(fnresult);
        updateScreen();
    }

    public void onClickEqual(View v) {
        if (display.equals("")) { return; }
        else if (display.length()!=0 && !finalResult) calculation();
        else if (display.length()!=0 && finalResult) secondValueIncrement();
    }

    private boolean writeInMemory(float v) {
        try {
            sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat("memory", v);
            editor.commit();
            return true;
        }
        catch (Exception e) { return false; }
    }

    private float readFromMemory() {
        try {
            sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            float defaultValue = 0;
            float memValue = sharedPref.getFloat("memory", defaultValue);
            return memValue;
        }
        catch (Exception e) {
            Toast.makeText(this, "HAHAHA", Toast.LENGTH_SHORT);
            return -1;
        }
    }

    public void onClickMR(View v) {
        float memValue = readFromMemory();
        display = String.valueOf(memValue);
        finalResult = true;
        dotPressed = false;
        updateScreen();
    }

    public void onClickMClear(View v) {
        value = 0;
        if (writeInMemory(value)) {
            Toast.makeText(this,"cleared", Toast.LENGTH_SHORT);
        }
        else {
            Toast.makeText(this,"not cleared", Toast.LENGTH_SHORT);
        }
    }

    public void onClickMPlus(View v) {
        if (display.equals("")) { return; }
        else {
            float memValue = readFromMemory();
            memValue += Float.valueOf(display);
            if (writeInMemory(memValue)) {
                Toast.makeText(this, "added", Toast.LENGTH_SHORT);
            }
            else {
                Toast.makeText(this, "not added", Toast.LENGTH_SHORT);
            }
        }
    }

    public void onClickMMinus(View v) {
        if (display.equals("")) { return; }
        else {
            float memValue = readFromMemory();
            memValue -= Float.valueOf(display);
            if (writeInMemory(memValue)) {
                Toast.makeText(this, "added", Toast.LENGTH_SHORT);
            }
            else {
                Toast.makeText(this, "not added", Toast.LENGTH_SHORT);
            }
        }
    }

    public void onClickHistory(View v) {
        Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(historyIntent);
    }
}