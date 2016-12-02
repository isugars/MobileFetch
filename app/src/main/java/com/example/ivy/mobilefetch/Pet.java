package com.example.ivy.mobilefetch;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ivy on 12/2/2016.
 */

public class Pet implements Parcelable
{
    final String name, photo, city, state, description;
    public Pet(String name, String photo, String city, String state, String description)
    {
        this.name = name;
        this.photo = photo;
        this.city = city;
        this.state = state;
        this.description = description;
    }

    public String getName()
    {
        return(name);
    }

    public String getPhoto()
    {
        return(photo);
    }

    public String getCity()
    {
        return(city);
    }

    public String getState()
    {
        return(state);
    }

    public String getDescription()
    {
        return(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
