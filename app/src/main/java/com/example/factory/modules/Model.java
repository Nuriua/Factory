package com.example.factory.modules;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Model implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name="name")
    public String name;
    @ColumnInfo(name="timestamp")
    public long timestamp;

    public Model(){}

    protected Model(Parcel in) {
        uid = in.readInt();
        name = in.readString();
        timestamp = in.readLong();
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return uid == model.uid && timestamp == model.timestamp && Objects.equals(name, model.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, timestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(uid);
        parcel.writeString(name);
        parcel.writeLong(timestamp);
    }
}
