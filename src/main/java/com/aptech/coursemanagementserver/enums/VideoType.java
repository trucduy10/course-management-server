package com.aptech.coursemanagementserver.enums;

public enum VideoType {
    MP4("video/mp4"),
    WEBM("video/webm"),
    AVI("video/x-msvideo"),
    MOV("video/quicktime"),
    OTHER("application/octet-stream");

    private final String mimeType;

    VideoType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}