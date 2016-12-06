package com.example.ivy.mobilefetch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText zipcodeText;
    TextView responseView;
    protected JSONArray photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zipcodeText = (EditText)findViewById(R.id.zipcodeText);
        responseView = (TextView)findViewById(R.id.responseView);

        Button queryButton = (Button) findViewById(R.id.button_search);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetPets().execute();
            }
        });
    }



    //add inner class
    //have to have this class to use UI thread; AsyncTask<Params, Progress, Result>
    class GetPets extends AsyncTask<Void,Void,String> {
        private String zipcode;

        static final String METHOD = "pet.find?";
        static final String API_KEY = "1e75a3f1a65d2d1ab6441a067e8cd602";
        static final String API_URL = "http://api.petfinder.com/";
        static final String FORMAT = "format=json";
        Intent photoActivityIntent;
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

            //init list of Pet objects to be passed to new activity
            ArrayList<Pet> pets = new ArrayList<>();

            //response output to console for reference and debugging
            System.out.println("response length: " + response.length());
            System.out.println(response);

            try {
                //get list of pets
                JSONObject obj = (JSONObject) new JSONTokener(response).nextValue();
                JSONArray objList = obj.getJSONObject("petfinder").getJSONObject("pets").getJSONArray("pet");

                //pet list size output to console for reference and debugging
                System.out.println("list length: " + objList.length());

                for(int i = 2; i < objList.length(); i++)
                {
                    //maximum threshold to avoid having way too many results shown
                    if(i > 50)
                        break;

                    //create JSON object with response
                    JSONObject pet = objList.getJSONObject(i);

                    //get relevant data
                    String name, photo, city, state, description, contact;
                    name = pet.getJSONObject("name").get("$t").toString();
                    photo = pet.getJSONObject("media").getJSONObject("photos").getJSONArray("photo").getJSONObject(1).get("$t").toString();
                    city = pet.getJSONObject("contact").getJSONObject("city").get("$t").toString();
                    state = pet.getJSONObject("contact").getJSONObject("state").get("$t").toString();
                    description = pet.getJSONObject("description").get("$t").toString();
                    contact = pet.getJSONObject("contact").getJSONObject("email").get("$t").toString();

                    //put data in a ArrayList of Pet objects
                    pets.add(new Pet(name, photo, city, state, description,contact));
                }
            }catch (JSONException e){
                System.out.println("caught stuff");
            }

            //new activity for search results
            photoActivityIntent = new Intent(MainActivity.this, PhotoListActivity.class);

            //put ArrayList of Pets into the intent
            photoActivityIntent.putParcelableArrayListExtra("activitypets", pets);

            //start activity w/ bundle of pets
            startActivity(photoActivityIntent);
        }

    }
}
