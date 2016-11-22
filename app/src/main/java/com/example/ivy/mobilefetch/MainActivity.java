package com.example.ivy.mobilefetch;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText zipcodeText;
    TextView responseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    //add inner class
    //have to have this class to use UI thread; AsyncTask<Params, Progress, Result>
    class GetPets extends AsyncTask<Void,Void,String> {
        private String zipcode;

        static final String METHOD = "pet.find?";
        static final String API_KEY = "1e75a3f1a65d2d1ab6441a067e8cd602";
        static final String API_URL = "http://api.petfinder.com/";
        static final String FORMAT = "format=json";
        /*
        Four methods with AsyncTask:
        1) onPreExecute()
        2) doInBackground(Void...urls)
        3) onProgressUpdate()
        4) onPostExecute(String result)
         */
        protected void onPreExecute() {
            responseView.setText("");

            zipcode = zipcodeText.getText().toString();
        }

        @Override
        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL(API_URL + METHOD  + FORMAT + "&key=" +  API_KEY  + "&location=" + zipcode);
                //API_REQUEST:http://api.petfinder.com/pet.get?format=json&key=12345&location=24601
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception error) {
                Log.e("Error",error.getMessage(),error);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "ERROR";
            }
            Log.i("INFO", response);
            responseView.setText(response);
        }
    }
}
