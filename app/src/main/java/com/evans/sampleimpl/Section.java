package com.evans.sampleimpl;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Section {

    @SerializedName("id")
    private String sectionId;

    @SerializedName("sectionDescription")
    private String description;

    @SerializedName("sectionTest")
    private String test;

    @SerializedName("level")
    private String level;

    @SerializedName("sectionTitle")
    private String title;

    public Section(String sectionId, String description, String test, String level, String title) {
        this.sectionId = sectionId;
        this.description = description;
        this.test = test;
        this.level = level;
        this.title = title;
    }

    public String getSectionId() {
        return sectionId;
    }

    public String getDescription() {
        return description;
    }

    public String getTest() {
        return test;
    }

    public String getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }
}
