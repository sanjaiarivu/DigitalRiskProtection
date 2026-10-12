-- ====================================================================
-- BrandShield AI — Relational Schema
-- Supports Brand Management, Official Whitelisting, Impersonation Detection,
-- Evidence Explanations, and Audit History.
-- ====================================================================

CREATE TABLE IF NOT EXISTS brands (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    website VARCHAR(500),
    logo_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS brand_aliases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id BIGINT NOT NULL,
    alias_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (brand_id) REFERENCES brands(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS brand_assets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id BIGINT NOT NULL,
    asset_type VARCHAR(50) NOT NULL,
    platform VARCHAR(100),
    identifier VARCHAR(500) NOT NULL,
    display_name VARCHAR(255),
    canonical_url VARCHAR(500),
    verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (brand_id) REFERENCES brands(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS social_candidates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    platform VARCHAR(100) NOT NULL,
    username VARCHAR(255),
    display_name VARCHAR(255),
    account_id VARCHAR(255),
    profile_url VARCHAR(500),
    description TEXT,
    avatar_url VARCHAR(500),
    external_links TEXT,
    follower_count INT,
    is_synthetic BOOLEAN DEFAULT FALSE,
    source VARCHAR(100) DEFAULT 'IMPORT',
    imported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS app_candidates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    app_name VARCHAR(255) NOT NULL,
    package_id VARCHAR(500),
    bundle_id VARCHAR(500),
    publisher_name VARCHAR(255),
    publisher_id VARCHAR(255),
    store_platform VARCHAR(100),
    store_url VARCHAR(500),
    description TEXT,
    icon_url VARCHAR(500),
    external_links TEXT,
    download_count VARCHAR(100),
    rating DOUBLE,
    is_synthetic BOOLEAN DEFAULT FALSE,
    source VARCHAR(100) DEFAULT 'IMPORT',
    imported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS threat_detections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id BIGINT NOT NULL,
    detection_type VARCHAR(50) NOT NULL,
    candidate_type VARCHAR(50) NOT NULL,
    social_candidate_id BIGINT,
    app_candidate_id BIGINT,
    risk_score INT NOT NULL DEFAULT 0,
    risk_level VARCHAR(20) NOT NULL DEFAULT 'LOW',
    status VARCHAR(30) NOT NULL DEFAULT 'NEW',
    is_official_asset BOOLEAN DEFAULT FALSE,
    summary TEXT,
    ai_explanation TEXT,
    detected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (brand_id) REFERENCES brands(id),
    FOREIGN KEY (social_candidate_id) REFERENCES social_candidates(id),
    FOREIGN KEY (app_candidate_id) REFERENCES app_candidates(id)
);

CREATE TABLE IF NOT EXISTS detection_evidence (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    detection_id BIGINT NOT NULL,
    signal_name VARCHAR(100) NOT NULL,
    signal_category VARCHAR(50),
    observed_value TEXT,
    expected_value TEXT,
    evidence_explanation TEXT NOT NULL,
    score_contribution INT NOT NULL DEFAULT 0,
    confidence VARCHAR(20) DEFAULT 'MEDIUM',
    FOREIGN KEY (detection_id) REFERENCES threat_detections(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS investigation_notes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    detection_id BIGINT NOT NULL,
    author VARCHAR(255) DEFAULT 'analyst',
    note_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (detection_id) REFERENCES threat_detections(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS investigation_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    detection_id BIGINT NOT NULL,
    previous_status VARCHAR(30),
    new_status VARCHAR(30) NOT NULL,
    changed_by VARCHAR(255) DEFAULT 'analyst',
    change_reason TEXT,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (detection_id) REFERENCES threat_detections(id) ON DELETE CASCADE
);
