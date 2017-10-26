package com.patelheggere.listfeed.data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by dell on 10/25/2017.
 */

public class CommentModel implements Serializable {
    public CommentModel(){}
    String key,CommentedBy,CommentMessage,CommentedByName;
    Map<String,String> CommentedOn;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCommentedBy() {
        return CommentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        CommentedBy = commentedBy;
    }

    public String getCommentMessage() {
        return CommentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        CommentMessage = commentMessage;
    }

    public Map<String, String> getCommentedOn() {
        return CommentedOn;
    }

    public void setCommentedOn(Map<String, String> commentedOn) {
        CommentedOn = commentedOn;
    }

    public String getCommentedByName() {
        return CommentedByName;
    }

    public void setCommentedByName(String commentedByName) {
        CommentedByName = commentedByName;
    }
}
