package com.android.rahul.myselfieapp.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.client.util.Key;
import com.kinvey.java.LinkedResources.LinkedGenericJson;

/**
 * Created by rkrde on 06-11-2016.
 */

public class UpdateEntity extends LinkedGenericJson implements Parcelable {
    @Key("text")
    private String text;
    public UpdateEntity() {
        //"attachment" is the JSON element used to maintain a Linked File.
        putFile("attachment");
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
    }

    protected UpdateEntity(Parcel in) {
        this.text = in.readString();
    }

    public static final Parcelable.Creator<UpdateEntity> CREATOR = new Parcelable.Creator<UpdateEntity>() {
        @Override
        public UpdateEntity createFromParcel(Parcel source) {
            return new UpdateEntity(source);
        }

        @Override
        public UpdateEntity[] newArray(int size) {
            return new UpdateEntity[size];
        }
    };
}
