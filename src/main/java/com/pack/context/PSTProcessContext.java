package com.pack.context;

public class PSTProcessContext {

    private String emlOutputPath;
    private String zipOutputPath;
    private long zipThresholdSize;
    private boolean zipEml;
    private String processId;
    private String tmpFilePath;

    public String getProcessId() {
        return processId;
    }

    public String getEmlOutputPath() {
        return emlOutputPath;
    }

    public String getZipOutputPath() {
        return zipOutputPath;
    }

    public long getZipThresholdSize() {
        return zipThresholdSize;
    }

    public boolean isZipEml() {
        return zipEml;
    }

    public String getTmpFilePath() {
        return tmpFilePath;
    }

    public PSTProcessContext(PSTProcessContextBuilder builder){
        this.emlOutputPath = builder.getEmlOutputPath();
        this.zipEml = builder.isZipEml();
        this.zipOutputPath = builder.getZipOutputPath();
        this.zipThresholdSize = builder.getZipThresholdSize();
        this.processId = builder.getProcessId();
        this.tmpFilePath = builder.getTmpFilePath();
    }

    public static class PSTProcessContextBuilder {
        private String emlOutputPath;
        private String zipOutputPath;
        private long zipThresholdSize;
        private boolean zipEml;
        private String processId;
        private String tmpFilePath;

        public String getEmlOutputPath() {
            return emlOutputPath;
        }

        public PSTProcessContextBuilder setEmlOutputPath(String emlOutputPath) {
            this.emlOutputPath = emlOutputPath;
            return this;
        }

        public String getZipOutputPath() {
            return zipOutputPath;
        }

        public PSTProcessContextBuilder setZipOutputPath(String zipOutputPath) {
            this.zipOutputPath = zipOutputPath;
            return this;
        }

        public long getZipThresholdSize() {
            return zipThresholdSize;
        }

        public PSTProcessContextBuilder setZipThresholdSize(long zipThresholdSize) {
            this.zipThresholdSize = zipThresholdSize;
            return this;
        }

        public boolean isZipEml() {
            return zipEml;
        }

        public PSTProcessContextBuilder setZipEml(boolean zipEml) {
            this.zipEml = zipEml;
            return this;
        }

        public String getProcessId() {
            return processId;
        }

        public PSTProcessContextBuilder setProcessId(String processId) {
            this.processId = processId;
            return this;
        }

        public String getTmpFilePath() {
            return tmpFilePath;
        }

        public PSTProcessContextBuilder setTmpFilePath(String tmpFilePath) {
            this.tmpFilePath = tmpFilePath;
            return this;
        }

        public PSTProcessContext build(){
            return new PSTProcessContext(this);
        }
    }

}
