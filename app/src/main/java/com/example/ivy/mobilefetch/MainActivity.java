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

/**
 * @author Michael Seils and Ivy Sugars
 * 12/13/16
 * @version 2.5
 */
public class MainActivity extends AppCompatActivity {
    EditText zipcodeText;
    TextView responseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        defineUI(savedInstanceState);
        retrieveWidgets();
    }

    private void defineUI(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void retrieveWidgets(){
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


    /**
     *
     */
    class GetPets extends AsyncTask<Void,Void,String> {
        private String zipcode;
        private URL url;
        static final int MAXPETS=50;
        static final String METHOD = "pet.find?";
        static final String API_KEY = "1e75a3f1a65d2d1ab6441a067e8cd602";
        static final String API_URL = "http://api.petfinder.com/";
        static final String FORMAT = "format=json";
        Intent photoActivityIntent;

        /**
         *
         */
        protected void onPreExecute() {
            responseView.setText("");
            zipcode = zipcodeText.getText().toString();
        }

        /**
         *
         * @param urls
         * @return
         */
        @Override
        protected String doInBackground(Void... urls) {

            try {
                url = new URL(API_URL + METHOD  + FORMAT + "&key=" +  API_KEY  + "&location=" + zipcode);
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

        /**
         *
         * @param response
         */
        protected void onPostExecute(String response) {
            handNullResponse(response);
            ArrayList<Pet> pets = new ArrayList<>();
            parsePets(pets,response);
            setIntentForNextActivity(pets);
        }

        private void handNullResponse(String response){
            if (response == null) {
                response = "ERROR";
            }
            Log.i("No response: ", response);
        }

        private void parsePets(ArrayList<Pet> pets, String response){
            try {
                JSONArray objList = getListOfPets(response);

                for(int i = 2; i < objList.length(); i++)
                {
                    JSONObject pet = objList.getJSONObject(i);
                    String name, photo, city, state, description, contact;
                    name = pet.getJSONObject("name").get("$t").toString();
                    photo = pet.getJSONObject("media").getJSONObject("photos").getJSONArray("photo").getJSONObject(1).get("$t").toString();
                    city = pet.getJSONObject("contact").getJSONObject("city").get("$t").toString();
                    state = pet.getJSONObject("contact").getJSONObject("state").get("$t").toString();
                    description = pet.getJSONObject("description").get("$t").toString();
                    contact = pet.getJSONObject("contact").getJSONObject("email").get("$t").toString();
                    pets.add(new Pet(name, photo, city, state, description,contact));
                }
            }catch (JSONException e){
                Log.e("Invalid JSON", pets.toString());
            }
        }

        private JSONArray getListOfPets(String response) {
            JSONArray objList = null;
            try {
                JSONObject obj = (JSONObject) new JSONTokener(response).nextValue();
                objList = obj.getJSONObject("petfinder").getJSONObject("pets").getJSONArray("pet");
            } catch (JSONException e) {
                Log.i("Not a valid response: ", response);
            }
            return objList;
        }

        private void setIntentForNextActivity(ArrayList<Pet> pets){
            photoActivityIntent = new Intent(MainActivity.this, PhotoListActivity.class);
            photoActivityIntent.putParcelableArrayListExtra("activitypets", pets);
            startActivity(photoActivityIntent);
        }

    }
}
