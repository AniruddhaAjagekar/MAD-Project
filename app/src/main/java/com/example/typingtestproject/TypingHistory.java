package com.example.typingtestproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.CursorAdapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TypingHistory extends AppCompatActivity {
    TextView txt;
    ListView list;
    HistoryDatabase db = new HistoryDatabase(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typing_history);
        txt = findViewById(R.id.txtview);
        list = findViewById(R.id.list);
        displayRecords();
    }

    private void displayRecords() {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cur = db.read();
        if(cur == null){
            txt.setText("OOP's You have not give typing test yet");
        }
        else {
            while (cur.moveToNext()) {
                String date = cur.getString(1);
                String acc = cur.getString(2);
                String speed = cur.getString(3);
                String correct_words = cur.getString(4);
                String wrong_words = cur.getString(5);
                String data = ("\nYour Typing test Date :- " + date + "\n Your Accuracy :- " + acc + "\n Your Speed is :- " + speed +
                        "\n You have typed correct words :- " + correct_words + "\nwrong words :- " + wrong_words);
                arrayList.add(data);
            }
            ArrayAdapter<String> adapater = new ArrayAdapter<>(TypingHistory.this,android.R.layout.simple_list_item_1,arrayList);
            list.setAdapter(adapater);
        }
    }
}