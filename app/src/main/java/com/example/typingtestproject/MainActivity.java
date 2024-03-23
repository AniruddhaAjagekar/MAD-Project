package com.example.typingtestproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    HistoryDatabase db = new HistoryDatabase(this);
    TextView correctTxt,wrongTxt,accuracyTxt,speedTxt,contentTxt,timer;
    EditText edtContent;
    Button startBtn,RestartBtn;
    private int correctWords = 0;
    private int totalWords = 0;
    private int wrongWords = 0 ;
    private long startTime = 0;
    private static String randow_word = " ";
    private CountDownTimer count_timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        correctTxt = findViewById(R.id.correctTxt);
        wrongTxt = findViewById(R.id.wrongTxt);
        accuracyTxt = findViewById(R.id.accuracyTxt);
        speedTxt = findViewById(R.id.speedTxt);
        contentTxt = findViewById(R.id.contentTxt);

        startBtn = findViewById(R.id.starttyping);
        RestartBtn = findViewById(R.id.Restarttyping);
        timer = findViewById(R.id.stoptyping);

        edtContent = findViewById(R.id.edtContent);
        edtContent.setEnabled(false);
        List<String> words = initailizeText();
        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtContent.getText().toString().length() > contentTxt.getText().toString().length()) {

                    if(edtContent.getText().toString().trim().equals(contentTxt.getText().toString()))
                    {
                        correctWords++;
                        totalWords++;
                        correctTxt.setText(Integer.toString(correctWords));
                    }
                    else{
                        wrongWords++;
                        totalWords++;
                        wrongTxt.setText(Integer.toString(wrongWords));

                    }
                    MainActivity.randow_word = displayText(words);
                    edtContent.setText(" ");

                }

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setEnabled(false);
                correctTxt.setText("0");
                correctWords=0;
                wrongTxt.setText("0");
                wrongWords=0;
                speedTxt.setText("0");
                accuracyTxt.setText("0");
                edtContent.setText("");
                MainActivity.randow_word = displayText(words);
                Log.d("random word","value:-"+randow_word);
                edtContent.setEnabled(true);
                startTime = System.currentTimeMillis();
                count_timer = new CountDownTimer(60000, 1000) {
                    @SuppressLint("SetTextI18n")
                    public void onTick(long millisUntilFinished) {
                        timer.setText("Remaining Time :-"+(millisUntilFinished/1000));
                    }
                    public void onFinish() {
                        displayStats();
                        startBtn.setEnabled(true);
                        edtContent.setEnabled(false);

                    }
                }.start();
            }
        });
        RestartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startBtn.isEnabled()){
                    Toast.makeText(MainActivity.this, "Please start the test first !!", Toast.LENGTH_SHORT).show();
                }
                else{
                    count_timer.cancel();
                    startBtn.setEnabled(false);
                    correctTxt.setText("0");
                    correctWords=0;
                    wrongTxt.setText("0");
                    wrongWords=0;
                    speedTxt.setText("0");
                    accuracyTxt.setText("0");
                    edtContent.setText("");
                    MainActivity.randow_word = displayText(words);
                    Log.d("random word","value:-"+randow_word);
                    edtContent.setEnabled(true);
                    startTime = System.currentTimeMillis();
                    count_timer.start();

                }
            }
        });


    }

    @SuppressLint("SetTextI18n")
    public void displayStats(){
        double [] result = calculateSpeedAndAccuracy(correctWords,totalWords,60);
        speedTxt.setText(Double.toString(result[0]));
        accuracyTxt.setText(Integer.toString((int) result[1]));
        alertDialog();

    }

    public String displayText(List<String> words){
        Random rand = new Random(System.currentTimeMillis());
        String randomWord = words.get(rand.nextInt(words.size()));
        contentTxt.setText(randomWord);
        return randomWord;
    }
    public List<String> initailizeText(){
        String data = getResources().getString(R.string.words);
        String[] wordsLine = data.split(" ");
        return new ArrayList<String>(Arrays.asList(wordsLine));
    }
    public  double[] calculateSpeedAndAccuracy(int correctWords, int totalWords, int timeTaken) {
        double[] stats = new double[2];
        double speed = (60.0 * correctWords) / timeTaken;
        double accuracy = ((double) correctWords / totalWords) * 100.0;
        stats[0] = speed;
        stats[1] = accuracy;
        return stats;
    }
    private void alertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setMessage("Are you want to store the history of your current typing test .....");
        dialog.setTitle("Storing In History");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        DateTimeFormatter formatter;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            LocalDateTime now = LocalDateTime.now();
                            formatter = DateTimeFormatter.ofPattern("EEE dd/MM h:mm a");
                            String formattedDateTime = now.format(formatter);
                            db.insert(formattedDateTime.toString(),accuracyTxt.getText().toString(),speedTxt.getText().toString(),
                                    correctTxt.getText().toString(),wrongTxt.getText().toString());
                            Toast.makeText(getApplicationContext(),"Your Data is begin Recorded",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Please make sure your android is up to date", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getApplicationContext(),"Data is not stored in History",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }
}