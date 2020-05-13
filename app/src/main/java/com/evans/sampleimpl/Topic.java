package com.evans.sampleimpl;

public class Topic {

    private String topicId, topicTitle, topicDesc, videoUrl;

    public Topic(String topicId, String topicTitle, String topicDesc, String videoUrl) {
        this.topicId = topicId;
        this.topicTitle = topicTitle;
        this.topicDesc = topicDesc;
        this.videoUrl = videoUrl;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public String getTopicDesc() {
        return topicDesc;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
