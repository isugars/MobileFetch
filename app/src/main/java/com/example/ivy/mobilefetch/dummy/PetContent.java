package com.example.ivy.mobilefetch.dummy;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ivy.mobilefetch.Pet;
import com.example.ivy.mobilefetch.PhotoListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PetContent {
//make photo items from JSON response!!!!!!!!!!!!

    //STUFF IN HERE IS NOT BEING USED ANYMORE DUE TO SCOPING ISSUES -- BYPASSED ENTIRELY AND USING PET OBJECTS INSTEAD!!

    /**
     * An array of sample (dummy) items.
     */
    public static final List<PetPhoto> ITEMS = new ArrayList<>();
    public static Pet[] PetContentList = new Pet[0];

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, PetPhoto> ITEM_MAP = new HashMap<>();

    private static final int COUNT = ITEMS.size(); //PhotoListActivity.petList.length;

    static {
        // Add some sample items.
        for (int i = 0; i <= COUNT; i++) {
            addItem(createPetPhoto(i));
        }
    }

    private static void addItem(Pet item) {
        PetPhoto result = new PetPhoto(item.getName(),item.getPhoto(),item.getDescription());
        ITEMS.add(result);
        ITEM_MAP.put(item.getName(), result);
    }

    private static Pet createPetPhoto(int position) {
        if(PetContentList.length > 0)
            return new Pet(PetContentList[position].getName(), PetContentList[position].getPhoto(), PetContentList[position].getCity(), PetContentList[position].getState(), makeDetails(position));
        else
            return new Pet("Nothing found","","","","");
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about ").append(PetContentList[position].getName());
            builder.append(PetContentList[position].getDescription());
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class PetPhoto //implements Parcelable
    {
        public final String id;
        public final String content;
        public final String details;

        public PetPhoto(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }
        /*
        protected PetPhoto(Parcel in) {
            id = in.readString();
            content = in.readString();
            details = in.readString();
        }

        public static final Creator<PetPhoto> CREATOR = new Creator<PetPhoto>() {
            @Override
            public PetPhoto createFromParcel(Parcel in) {
                return new PetPhoto(in);
            }

            @Override
            public PetPhoto[] newArray(int size) {
                return new PetPhoto[size];
            }
        };
        */
        @Override
        public String toString() {
            return content;
        }
        /*
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(content);
            dest.writeString(details);
        }
        */
    }
}
