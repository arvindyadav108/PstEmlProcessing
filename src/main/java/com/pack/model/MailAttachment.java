package com.pack.model;

import javax.activation.DataSource;

public class MailAttachment {

    private String name;
    private String mimeType;
    private DataSource dataSource;
    private String disposition;
    private String description;
    private String contentId;

    public MailAttachment(MailAttachmentBuilder builder) {
        this.name = builder.getName();
        this.mimeType = builder.getMimeType();
        this.dataSource = builder.getDataSource();
        this.disposition = builder.getDisposition();
        this.description = builder.getDescription();
        this.contentId = builder.getContentId();
    }

    public String getName() {
        return name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public String getDisposition() {
        return disposition;
    }

    public String getDescription() {
        return description;
    }

    public String getContentId() {
        return contentId;
    }

    public static class MailAttachmentBuilder{
        private String name;
        private String mimeType;
        private DataSource dataSource;
        private String disposition;
        private String description;
        private String contentId;

        public String getName() {
            return name;
        }

        public MailAttachmentBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public String getMimeType() {
            return mimeType;
        }

        public MailAttachmentBuilder setMimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public DataSource getDataSource() {
            return dataSource;
        }

        public MailAttachmentBuilder setDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public String getDisposition() {
            return disposition;
        }

        public MailAttachmentBuilder setDisposition(String disposition) {
            this.disposition = disposition;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public MailAttachmentBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public String getContentId() {
            return contentId;
        }

        public MailAttachmentBuilder setContentId(String contentId) {
            this.contentId = contentId;
            return this;
        }

        public MailAttachment build(){
            return new MailAttachment(this);
        }
    }
}
