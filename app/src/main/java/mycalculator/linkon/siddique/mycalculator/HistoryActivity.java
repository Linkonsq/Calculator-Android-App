package mycalculator.linkon.siddique.mycalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HistoryActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        tv = (TextView) findViewById(R.id.textView3);
        readFromFile();
    }

    public void onClickBack(View v) {
        Intent mainIntent = new Intent(HistoryActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    private void readFromFile() {
        StringBuilder text = new StringBuilder();

        try {
            InputStream inStream = openFileInput("Text.txt");

            if (inStream != null) {
                InputStreamReader inputReader = new InputStreamReader(inStream);
                BufferedReader bufferReader = new BufferedReader(inputReader, 8000);

                String line = null;

                while ((line = bufferReader.readLine()) != null) {
                    text.append(line + "\n");
                }
            }
            tv.setText(text.toString());
            Log.d("a", "Read Done");
        }
        catch (Exception e) {
            Toast.makeText(this, "Read Exception", Toast.LENGTH_SHORT);
            Log.d("a", "Read Exception");
        }
    }
}
