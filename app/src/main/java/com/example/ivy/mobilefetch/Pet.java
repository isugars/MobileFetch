package com.example.ivy.mobilefetch;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ivy on 12/2/2016.
 */

public class Pet implements Parcelable
{
    private final String name, photo, city, state, description, contact;
    public Pet(String name, String photo, String city, String state, String description, String contact)
    {
        this.name = name;
        this.photo = photo;
        this.city = city;
        this.state = state;
        this.description = description;
        this.contact = contact;
    }

    protected Pet(Parcel in) {
        name = in.readString();
        photo = in.readString();
        city = in.readString();
        state = in.readString();
        description = in.readString();
        contact = in.readString();
    }

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };

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

    public String getContact()
    {
        return(contact);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(description);
        dest.writeString(contact);
    }
}
