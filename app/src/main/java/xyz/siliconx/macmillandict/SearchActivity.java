package xyz.siliconx.macmillandict;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private TextView searchResult;
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        autoCompleteTextView =  findViewById(R.id.new_word);
        Button submitButton = findViewById(R.id.submit);
        searchResult = (TextView) findViewById(R.id.search_result);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String word = autoCompleteTextView.getText().toString();
                if (word == null || word.length() == 0) {
                    return;
                }

                Toast.makeText(SearchActivity.this, "Searching...", Toast.LENGTH_SHORT).show();
                getResult(word);
            }
        });
    }

    private void getResult(final String word) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> result = Spider.extra(word);
                showResult(result);
            }
        }).start();

    }

    private void showResult(final ArrayList<String> result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (result == null || result.size() == 0) {
                    Toast.makeText(SearchActivity.this, "No Content", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(SearchActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }

                StringBuilder temp = new StringBuilder();
                for (int i = 0; i < result.size(); ++i) {
                    temp.append(String.format("%d: %s\n", i + 1, result.get(i)));
                }

                searchResult.setText(temp.toString());
            }
        });

    }
}