package com.google.android.gms.samples.vision.ocrreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class Result extends AppCompatActivity {
    Intent intent;
    String value;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        intent = getIntent();
        value = intent.getStringExtra("result"); //if it's a string you stored.

        text = (TextView) findViewById(R.id.textView);

        String result;

        try {
            result = find(value);
        } catch (IOException e) {
            result = "";
        }

        if(result.isEmpty() || result == null)
            text.setText("Sorry no record found");
        else
            text.setText( result );
    }
    //function for searching the number plate and scrapping the result
    public String find(String number) throws IOException {

        StringBuffer string = new StringBuffer();
        String url = "http://www.mtmis.excise-punjab.gov.pk";
        Document doc = Jsoup.connect(url)
                .data("vhlno", number)
                .post();

        Element table = doc.select("table").get(0); //select the first table.
        if(table != null) {
            Elements trs = table.select("tr");

            for (Element tr : trs) { //in each row
                Elements tds = tr.select("td"); //get all cells

                for (Element td : tds) { // in each cell
                    String text = td.text();
                    string.append(String.format("%" + -22 + "s", text));//get text
                }

                string.append("\n");
            }
        }
        else
            string.delete(0, string.length());

        return string.toString();
    }
}