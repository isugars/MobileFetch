package com.example.ivy.mobilefetch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Altered Android Studio template.
 */
public class PhotoListActivity extends AppCompatActivity {
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PhotoListActivity.this, MainActivity.class));
            }
        });

        petItems = getIntent().getParcelableArrayListExtra("activitypets");

        for(int i = 0; i < petItems.size(); i++)
            PET_MAP.put(petItems.get(i).getName(), petItems.get(i));

        View recyclerView = findViewById(R.id.photo_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView, petItems);

        if (findViewById(R.id.photo_detail_container) != null) {
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Pet> petList) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(petList));
    }

    /**
     *
     */
    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final List<Pet> mValues;

        /**
         *
         * @param items
         */
        public SimpleItemRecyclerViewAdapter(List<Pet> items) {
            mValues = items;
        }

        /**
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.photo_list_content, parent, false);
            return new ViewHolder(view);
        }

        /**
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).getName() + "\n" + mValues.get(position).getCity() + ", " + mValues.get(position).getState());
            new DownloadImageTask(holder.mContentView).execute(mValues.get(position).getPhoto());
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(PhotoDetailFragment.ARG_ITEM_ID, holder.mItem.getName());
                        PhotoDetailFragment fragment = new PhotoDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.photo_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, PhotoDetailActivity.class);
                        intent.putExtra(PhotoDetailFragment.ARG_ITEM_ID, holder.mItem.getName());

                        intent.putExtra(PhotoDetailFragment.ARG_ITEM_ID + "_index", position);

                        intent.putParcelableArrayListExtra("activitypets", petItems);
                        context.startActivity(intent);
                    }
                }
            });
        }

        /**
         *
         * @return
         */
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /**
         *
         */
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
            Bitmap mImage = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mImage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mImage;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
