package com.example.ivy.mobilefetch;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class to create a pet object to pass to PhotoListActivity
 */
public class Pet implements Parcelable
{
    private final String name, photo, city, state, description, contact;

    /**
     *
     * @param name
     * @param photo
     * @param city
     * @param state
     * @param description
     * @param contact
     */
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

    /**
     *
     * @return
     */
    public String getName()
    {
        return(name);
    }

    /**
     *
     * @return
     */
    public String getPhoto()
    {
        return(photo);
    }

    /**
     *
     * @return
     */
    public String getCity()
    {
        return(city);
    }

    /**
     *
     * @return
     */
    public String getState()
    {
        return(state);
    }

    /**
     *
     * @return
     */
    public String getDescription()
    {
        return(description);
    }

    /**
     *
     * @return
     */
    public String getContact()
    {
        return(contact);
    }

    /**
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     *
     * @param dest
     * @param flags
     */
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
