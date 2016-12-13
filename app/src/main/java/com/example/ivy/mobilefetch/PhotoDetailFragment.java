package com.example.ivy.mobilefetch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;


/**
 * A fragment representing a single Photo detail screen.
 * This fragment is either contained in a {@link PhotoListActivity}
 * in two-pane mode (on tablets) or a {@link PhotoDetailActivity}
 * on handsets.
 * Altered Android Studio template.
 */
public class PhotoDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private Pet petItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PhotoDetailFragment() {
    }

    /**
     * Method that handles initialization at creation of PhotoDetailFragment.
     * @param savedInstanceState -  If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently supplied
     *                           in onSaveInstanceState(Bundle), otherwise null.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            petItem = PhotoListActivity.PET_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(petItem.getName());
            }
        }
    }

    /**
     * Method to have fragment instantiate UI.
     * @param inflater -  The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container -  If non-null, this is the parent view that the fragment's UI should be attached
     *                  to. The fragment should not add the view itself, but this can be used to generate
     *                  the LayoutParams of the view.
     * @param savedInstanceState -  If non-null, this fragment is being re-constructed from a previous
     *                              saved state as given here.
     * @return -  The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_detail, container, false);
        if (petItem != null) {
            ((TextView) rootView.findViewById(R.id.photo_detail)).setText(petItem.getDescription());
            new DownloadImageTask((ImageView)rootView.findViewById(R.id.content)).execute(petItem.getPhoto());
        }
        return rootView;
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
