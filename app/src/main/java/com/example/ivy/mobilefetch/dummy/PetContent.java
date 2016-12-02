package com.example.ivy.mobilefetch.dummy;

import android.os.Parcel;
import android.os.Parcelable;

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
    /**
     * An array of sample (dummy) items.
     */
    public static final List<PetPhoto> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, PetPhoto> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPetPhoto(i));
        }
    }

    private static void addItem(PetPhoto item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PetPhoto createPetPhoto(int position) {
        return new PetPhoto(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Pet: ").append(position);
            builder.append("\nMore details information here.");
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class PetPhoto implements Parcelable{
        public final String id;
        public final String content;
        public final String details;

        public PetPhoto(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

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

        @Override
        public String toString() {
            return content;
        }

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
    }
}
