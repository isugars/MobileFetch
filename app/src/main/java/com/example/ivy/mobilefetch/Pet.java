package com.example.ivy.mobilefetch;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class to create a Pet object to pass to PhotoListActivity
 */
public class Pet implements Parcelable
{
    private final String name, photo, city, state, description, contact;

    /**
     * Constructor creates a Pet object.
     * @param name - String represents the name of a specific Pet.
     * @param photo - String represents the photo of a specific Pet.
     * @param city - String represents the city of a specific Pet.
     * @param state -String represents the state of a specific Pet.
     * @param description - String represents the description of a specific Pet.
     * @param contact - String represents the contact of a specific Pet.
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
     * Method to get the name of a Pet.
     * @return - name
     */
    public String getName()
    {
        return(name);
    }

    /**
     * Method to get the photo of a Pet.
     * @return - photo
     */
    public String getPhoto()
    {
        return(photo);
    }

    /**
     * Method to get the city of a Pet.
     * @return city
     */
    public String getCity()
    {
        return(city);
    }

    /**
     * Method to get the state of a Pet.
     * @return state
     */
    public String getState()
    {
        return(state);
    }

    /**
     * Method to get the description of a Pet.
     * @return description
     */
    public String getDescription()
    {
        return(description);
    }

    /**
     * Method to get the contact of a Pet.
     * @return contact
     */
    public String getContact()
    {
        return(contact);
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     *
     * @see #CONTENTS_FILE_DESCRIPTOR
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     * May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
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
