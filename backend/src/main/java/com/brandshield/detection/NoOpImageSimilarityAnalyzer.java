package com.brandshield.detection;

import org.springframework.stereotype.Component;

@Component
public class NoOpImageSimilarityAnalyzer implements ImageSimilarityAnalyzer {
    @Override
    public ImageSimilarityResult analyzeImageSimilarity(String imageUrl1, String imageUrl2) {
        return ImageSimilarityResult.unavailable();
    }
}
