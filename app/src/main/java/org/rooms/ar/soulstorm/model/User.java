package org.rooms.ar.soulstorm.model;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class User {
    private String uuid;
    private String displayName;
    private Uri uri;

    public User(FirebaseUser firebaseUser) {
        uuid = firebaseUser.getUid();
        displayName = firebaseUser.getDisplayName();
        uri = firebaseUser.getPhotoUrl();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUri() {
        return uri.toString();
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(uuid, user.uuid) &&
                Objects.equals(displayName, user.displayName) &&
                Objects.equals(uri, user.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, displayName, uri);
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", displayName='" + displayName + '\'' +
                ", uri=" + uri.toString() +
                '}';
    }
}
