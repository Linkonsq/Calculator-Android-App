package mycalculator.linkon.siddique.mycalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screen = (TextView) findViewById(R.id.textView);
        screen.setText(display);
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
            display += btn.getText();
            dotPressed = true;
        }
        updateScreen();
    }

    public void onClickOperator(View v) {
        Button btn = (Button) v;

        /**
        if(display.equals("+") || display.equals("-") || display.equals("*") || display.equals("/")) {
            display = String.valueOf(btn.getText());
        }//!"+".equals(lastChar) || !"-".equals(lastChar) || !"*".equals(lastChar) || !"/".equals(lastChar)
        **/

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

        List<String> result = new ArrayList<String>();
        String lastChar = display.substring(display.length() -1);

        if(lastChar.equals("+") || lastChar.equals("-") || lastChar.equals("*") || lastChar.equals("/")) {
            result = getResultArrayOf(display.substring(0, display.length() -1), operators);
        }
        else result = getResultArrayOf(display, operators);

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
        listToString = "";
        finalResult = true;
        updateScreen();
    }

    private void secondValueIncrement() {
        double fnresult = 0.0;
        if(secondLastOperator.equals("+")) {
            fnresult = Double.parseDouble(display) + Double.parseDouble(lastValue);
        }
        else if(secondLastOperator.equals("-")) {
            fnresult = Double.parseDouble(display) - Double.parseDouble(lastValue);
        }
        else if(secondLastOperator.equals("*")) {
            fnresult = Double.parseDouble(display) * Double.parseDouble(lastValue);
        }
        else if(secondLastOperator.equals("/")) {
            fnresult = Double.parseDouble(display) / Double.parseDouble(lastValue);
        }

        display = String.valueOf(fnresult);
        updateScreen();
    }

    public void onClickEqual(View v) {
        if (display.equals("")) return;
        else if (display.length()!=0 && !finalResult) calculation();
        else if (display.length()!=0 && finalResult) secondValueIncrement();
    }
}
