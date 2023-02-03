package com.example.karori.data.User;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class User implements Parcelable {
    private String name;
    private String email;
    private String idToken;
    private int weight;
    private int height;
    private int kilocalorie;
    private int age;


    public User( String name, String email, String idToken){
        this.name = name;
        this.email = email;
        this.idToken = idToken;
        this.weight = 0;
        this.height = 0;
        this.kilocalorie = 0;
        this.age = 0;
    }
    public User(String name, String email, String idToken, int weight, int height, int kilokalorie, int age){
        this.name = name;
        this.email       = email;
        this.idToken     = idToken;
        this.weight      = weight;
        this.height      = height;
        this.kilocalorie = kilokalorie;
        this.age        = age;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    @Exclude
    public String getIdToken() {return idToken;}

    public void setIdToken(String idToken) {this.idToken = idToken;}

    public int getWeight() {return weight;}

    public void setWeight(int weight) {this.weight = weight;}

    public int getHeight() {return height;}

    public void setHeight(int height) {this.height = height;}

    public int getKilocalorie() {return kilocalorie;}

    public void setKilocalorie() {this.kilocalorie=kilocalorie;}

    public int getAge() {return age;}

    public void setAge(int goal) {this.age = age;}


    @Override
    public String toString() {
        return "User{" +
                "user= " + name + '\'' +
                " email='" + email + '\'' +
                ", idToken='" + idToken + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", kilokalorie=" + kilocalorie +
                ", age=" + age +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.idToken);
        dest.writeInt(this.height);
        dest.writeInt(this.weight);
        dest.writeInt(this.kilocalorie);
        dest.writeInt(this.age);

    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.email = source.readString();
        this.idToken = source.readString();
        this.weight = source.readInt();
        this.height = source.readInt();
        this.kilocalorie = source.readInt();
        this.age = source.readInt();
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.idToken = in.readString();
        this.weight = in.readInt();
        this.height = in.readInt();
        this.kilocalorie = in.readInt();
        this.age = in.readInt();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
