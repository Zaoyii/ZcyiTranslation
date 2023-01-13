package com.zcyi.rorschach.MyEntity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "t_words")
public class Words {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "words_id")
    private int wordsId;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "words_labels")
    private String labels;
    @ColumnInfo(name = "words_translation")
    private String translation;
    @ColumnInfo(name = "words_query")
    private String query;
    @ColumnInfo(name = "words_explains")
    private String explains;
    @ColumnInfo(name = "words_shape")
    private String shape;
    @ColumnInfo(name = "words_web")
    private String web;
    @ColumnInfo(name = "words_c_phonetic")
    private String CnPhonetic;
    @ColumnInfo(name = "words_us_phonetic")
    private String USPhonetic;
    @ColumnInfo(name = "words_uk_phonetic")
    private String UKPhonetic;

    public int getWordsId() {
        return wordsId;
    }

    public void setWordsId(int wordsId) {
        this.wordsId = wordsId;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getCnPhonetic() {
        return CnPhonetic;
    }

    public void setCnPhonetic(String cnPhonetic) {
        CnPhonetic = cnPhonetic;
    }

    public String getUSPhonetic() {
        return USPhonetic;
    }

    public void setUSPhonetic(String USPhonetic) {
        this.USPhonetic = USPhonetic;
    }

    public String getUKPhonetic() {
        return UKPhonetic;
    }

    public void setUKPhonetic(String UKPhonetic) {
        this.UKPhonetic = UKPhonetic;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Words() {
    }

    @Override
    public String toString() {
        return "Words{" +
                "wordsId=" + wordsId +
                ", userId=" + userId +
                ", labels='" + labels + '\'' +
                ", translation='" + translation + '\'' +
                ", query='" + query + '\'' +
                ", explains='" + explains + '\'' +
                ", shape='" + shape + '\'' +
                ", web='" + web + '\'' +
                ", CnPhonetic='" + CnPhonetic + '\'' +
                ", USPhonetic='" + USPhonetic + '\'' +
                ", UKPhonetic='" + UKPhonetic + '\'' +
                '}';
    }
}
