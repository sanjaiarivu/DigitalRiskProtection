package com.brandshield.dto.response;

public class ScanResultResponse {
    private int candidatesScanned;
    private int detectionsCreated;
    private int officialAssetsExcluded;
    private String scanDuration;

    public ScanResultResponse() {
    }

    public ScanResultResponse(int candidatesScanned, int detectionsCreated,
                              int officialAssetsExcluded, String scanDuration) {
        this.candidatesScanned = candidatesScanned;
        this.detectionsCreated = detectionsCreated;
        this.officialAssetsExcluded = officialAssetsExcluded;
        this.scanDuration = scanDuration;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getCandidatesScanned() {
        return candidatesScanned;
    }

    public void setCandidatesScanned(int candidatesScanned) {
        this.candidatesScanned = candidatesScanned;
    }

    public int getDetectionsCreated() {
        return detectionsCreated;
    }

    public void setDetectionsCreated(int detectionsCreated) {
        this.detectionsCreated = detectionsCreated;
    }

    public int getOfficialAssetsExcluded() {
        return officialAssetsExcluded;
    }

    public void setOfficialAssetsExcluded(int officialAssetsExcluded) {
        this.officialAssetsExcluded = officialAssetsExcluded;
    }

    public String getScanDuration() {
        return scanDuration;
    }

    public void setScanDuration(String scanDuration) {
        this.scanDuration = scanDuration;
    }

    public static class Builder {
        private int candidatesScanned;
        private int detectionsCreated;
        private int officialAssetsExcluded;
        private String scanDuration;

        public Builder candidatesScanned(int candidatesScanned) {
            this.candidatesScanned = candidatesScanned;
            return this;
        }

        public Builder detectionsCreated(int detectionsCreated) {
            this.detectionsCreated = detectionsCreated;
            return this;
        }

        public Builder officialAssetsExcluded(int officialAssetsExcluded) {
            this.officialAssetsExcluded = officialAssetsExcluded;
            return this;
        }

        public Builder scanDuration(String scanDuration) {
            this.scanDuration = scanDuration;
            return this;
        }

        public ScanResultResponse build() {
            return new ScanResultResponse(candidatesScanned, detectionsCreated, officialAssetsExcluded, scanDuration);
        }
    }
}
