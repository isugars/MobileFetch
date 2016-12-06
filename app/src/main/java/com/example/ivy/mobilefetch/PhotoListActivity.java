package com.example.ivy.mobilefetch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An activity representing a list of Photos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PhotoDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PhotoListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    public static ArrayList<Pet> petItems = new ArrayList<>();
    public static Map<String, Pet> PET_MAP = new HashMap<>();

    EditText zipcodeText;
    TextView responseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        zipcodeText = (EditText)findViewById(R.id.zipcodeText);
        responseView = (TextView)findViewById(R.id.responseView);

        /* (search bar) commented out for later removal
        Button queryButton = (Button) findViewById(R.id.button_search);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new GetPets().execute();
                startActivity(new Intent(PhotoListActivity.this, PhotoListActivity.class));
            }
        });
        */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // commented out for later removal (or later use as template)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */
                startActivity(new Intent(PhotoListActivity.this, MainActivity.class));
            }
        });


        //get list of pets from intent
        petItems = getIntent().getParcelableArrayListExtra("activitypets");

        //outputs list size to console for reference and debugging
        System.out.println(petItems.size());

        //fill hashmap for detail fragment
        for(int i = 0; i < petItems.size(); i++)
            PET_MAP.put(petItems.get(i).getName(), petItems.get(i));

        View recyclerView = findViewById(R.id.photo_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView, petItems);

        if (findViewById(R.id.photo_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Pet> petList) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(petList));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        //results from the iterated JSON String--make a list of JSON objects that are photos
        private final List<Pet> mValues;

        //takes the list of Pet objects and sets them to items
        public SimpleItemRecyclerViewAdapter(List<Pet> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.photo_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).getName() + "\n" + mValues.get(position).getCity() + ", " + mValues.get(position).getState());
            new DownloadImageTask(holder.mContentView).execute(mValues.get(position).getPhoto());

            //accordian expansion to display details
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {//ignore for now;this is tablet view
                        Bundle arguments = new Bundle();
                        arguments.putString(PhotoDetailFragment.ARG_ITEM_ID, holder.mItem.getName());
                        PhotoDetailFragment fragment = new PhotoDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.photo_detail_container, fragment)
                                .commit();
                    } else {//this is what we are worried about
                        Context context = v.getContext();
                        Intent intent = new Intent(context, PhotoDetailActivity.class);//photo details handled in PhotoDetailActivity.java
                        intent.putExtra(PhotoDetailFragment.ARG_ITEM_ID, holder.mItem.getName());

                        intent.putParcelableArrayListExtra("activitypets", petItems);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final ImageView mContentView;
            public final TextView mLocationView;
            public Pet mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (ImageView) view.findViewById(R.id.content);
                mLocationView = (TextView) view.findViewById(R.id.location);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mIdView.getText() + "'";
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
