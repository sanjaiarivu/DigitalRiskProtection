package com.brandshield.controller;

import com.brandshield.dto.request.BrandCreateRequest;
import com.brandshield.dto.response.BrandDetailResponse;
import com.brandshield.dto.response.BrandResponse;
import com.brandshield.service.BrandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BrandControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private StubBrandService brandService;

    static class StubBrandService extends BrandService {
        BrandResponse nextCreated;
        BrandDetailResponse nextDetail;
        List<BrandResponse> listToReturn;

        public StubBrandService() {
            super(null, null);
        }

        @Override
        public List<BrandResponse> getAllBrands() {
            return listToReturn != null ? listToReturn : List.of();
        }

        @Override
        public BrandResponse createBrand(BrandCreateRequest request) {
            return nextCreated;
        }

        @Override
        public BrandDetailResponse getBrand(Long id) {
            return nextDetail;
        }
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        brandService = new StubBrandService();
        mockMvc = MockMvcBuilders.standaloneSetup(new BrandController(brandService)).build();
    }

    @Test
    @DisplayName("GET /api/v1/brands returns 200 with list of brands")
    void testListBrands() throws Exception {
        BrandResponse response = BrandResponse.builder()
                .id(1L)
                .name("TechVault")
                .website("https://techvault.com")
                .aliases(List.of("Tech Vault"))
                .assetCount(2)
                .build();

        brandService.listToReturn = List.of(response);

        mockMvc.perform(get("/api/v1/brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("TechVault"))
                .andExpect(jsonPath("$[0].assetCount").value(2));
    }

    @Test
    @DisplayName("POST /api/v1/brands creates brand with valid payload")
    void testCreateBrand() throws Exception {
        BrandCreateRequest request = BrandCreateRequest.builder()
                .name("SecurePay")
                .website("https://securepay.io")
                .aliases(List.of("Secure Pay"))
                .build();

        BrandResponse response = BrandResponse.builder()
                .id(2L)
                .name("SecurePay")
                .website("https://securepay.io")
                .build();

        brandService.nextCreated = response;

        mockMvc.perform(post("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("SecurePay"));
    }

    @Test
    @DisplayName("POST /api/v1/brands rejects empty brand name with 400 Bad Request")
    void testCreateBrand_ValidationError() throws Exception {
        BrandCreateRequest invalidRequest = BrandCreateRequest.builder()
                .name("") // Blank name violates @NotBlank
                .build();

        mockMvc.perform(post("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/brands/{id} returns brand details")
    void testGetBrandDetails() throws Exception {
        BrandDetailResponse detail = BrandDetailResponse.builder()
                .id(1L)
                .name("TechVault")
                .website("https://techvault.com")
                .assets(List.of())
                .build();

        brandService.nextDetail = detail;

        mockMvc.perform(get("/api/v1/brands/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("TechVault"));
    }
}
