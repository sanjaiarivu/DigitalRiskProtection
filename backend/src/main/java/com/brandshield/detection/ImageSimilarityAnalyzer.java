package com.brandshield.detection;

public interface ImageSimilarityAnalyzer {
    ImageSimilarityResult analyzeImageSimilarity(String imageUrl1, String imageUrl2);

    record ImageSimilarityResult(
        boolean available,
        double similarity,
        String method
    ) {
        public static ImageSimilarityResult unavailable() {
            return new ImageSimilarityResult(false, -1.0, "NONE");
        }
    }
}
