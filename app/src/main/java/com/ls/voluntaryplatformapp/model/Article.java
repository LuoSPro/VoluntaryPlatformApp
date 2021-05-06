package com.ls.voluntaryplatformapp.model;

import android.text.TextUtils;

import androidx.annotation.Nullable;

public class Article {


    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof Article))
            return false;
        Article newArticle = (Article)obj;
        return id == newArticle.id &&
                TextUtils.equals(title,newArticle.title) &&
                score == newArticle.score &&
                status == newArticle.status &&
                personNumber == newArticle.personNumber &&
                totalNumber == newArticle.totalNumber &&
                star == newArticle.star &&
                TextUtils.equals(startTime,newArticle.startTime) &&
                TextUtils.equals(endTime,newArticle.endTime) &&
                TextUtils.equals(address,newArticle.address) &&
                TextUtils.equals(subtitle,newArticle.subtitle) &&
                TextUtils.equals(imageUrl,newArticle.imageUrl);
    }

    /**
     * title : testtitle
     * score : 1
     * status : 0
     * personNumber : 0
     * totalNumber : 10
     * star : 0
     * startTime : 2021-01-01
     * endTime : 2021-01-02
     * address : 四川师范大学图书馆
     * subtitle : testsubtitle
     * imageUrl : www.baidu.com
     */



    private int id;
    private String title;
    private int score;
    private int status;
    private int personNumber;
    private int totalNumber;
    private int star;
    private String startTime;
    private String endTime;
    private String address;
    private String subtitle;
    private String imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(int personNumber) {
        this.personNumber = personNumber;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
