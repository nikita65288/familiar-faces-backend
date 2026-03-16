package com.github.nikita65288.enums;

public enum AllowedContentType {

    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_GIF("image/gif");

    private final String mimeType;

    AllowedContentType(String mimeType) {
        this.mimeType = mimeType;
    }
    public String getMimeType() {
        return mimeType;
    }

    public static boolean isValid(String type) {
        for (AllowedContentType t : values()) {
            if (t.getMimeType().equalsIgnoreCase(type)) return true;
        }
        return false;
    }
}
