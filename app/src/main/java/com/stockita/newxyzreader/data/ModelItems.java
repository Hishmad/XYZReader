package com.stockita.newxyzreader.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model or POJO Class
 */
public class ModelItems implements Parcelable {

    // State
    private String _id = "_id";
    private String serverId = "server_id";
    private String title = "title";
    private String author = "author";
    private String body = "body";
    private String thumbUrl = "thumb_url";
    private String photoUrl = "photo_url";
    private String aspectRatio = "aspect_ratio";
    private String publishedDate = "published_date";

    /**
     * Constructor
     */
    public ModelItems(){}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    protected ModelItems(Parcel in) {
        _id = in.readString();
        serverId = in.readString();
        title = in.readString();
        author = in.readString();
        body = in.readString();
        thumbUrl = in.readString();
        photoUrl = in.readString();
        aspectRatio = in.readString();
        publishedDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(serverId);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(body);
        dest.writeString(thumbUrl);
        dest.writeString(photoUrl);
        dest.writeString(aspectRatio);
        dest.writeString(publishedDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ModelItems> CREATOR = new Parcelable.Creator<ModelItems>() {
        @Override
        public ModelItems createFromParcel(Parcel in) {
            return new ModelItems(in);
        }

        @Override
        public ModelItems[] newArray(int size) {
            return new ModelItems[size];
        }
    };
}
