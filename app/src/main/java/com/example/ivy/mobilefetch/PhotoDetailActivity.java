package com.example.ivy.mobilefetch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * An activity representing a single Photo detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link PhotoListActivity}.
 */
public class PhotoDetailActivity extends AppCompatActivity {

    ArrayList<Pet> pets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        pets = getIntent().getParcelableArrayListExtra("activitypets");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Contact: "  + pets.get(getIntent().getIntExtra(PhotoDetailFragment.ARG_ITEM_ID+"_index",0)).getContact(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).setDuration(Snackbar.LENGTH_INDEFINITE).show();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(PhotoDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(PhotoDetailFragment.ARG_ITEM_ID));
            PhotoDetailFragment fragment = new PhotoDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.photo_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, PhotoListActivity.class);
            intent.putParcelableArrayListExtra("activitypets", pets);

            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
