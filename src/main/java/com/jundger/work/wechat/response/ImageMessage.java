package com.jundger.work.wechat.response;

public class ImageMessage extends BaseMessage {
    private com.jundger.work.wechat.response.Image Image;

    public com.jundger.work.wechat.response.Image getImage() {
        return Image;
    }

    public void setImage(com.jundger.work.wechat.response.Image image) {
        Image = image;
    }
}
