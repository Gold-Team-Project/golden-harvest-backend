package com.teamgold.goldenharvest.domain.sales.command.infra;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.inventory.query.dto.AvailableItemResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class InventoryApiClient {

    private final RestClient restClient;

    public InventoryApiClient(RestClient.Builder builder, @Value("${inventory.base-url}") String inventoryBaseUrl) {
        this.restClient = builder.baseUrl(inventoryBaseUrl).build();
    }

    public Optional<AvailableItemResponse> findAvailableItemBySkuNo(String authorizationHeader, String skuNo) {
        try {
            ApiResponse<List<AvailableItemResponse>> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/items")
                            .queryParam("skuNo", skuNo)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", authorizationHeader)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (response != null && response.isSuccess() && response.getData() != null && !response.getData().isEmpty()) {
                // The response from /api/item is a list within ApiResponse, so we take the first element.
                return Optional.of(response.getData().get(0));
            }
        } catch (Exception e) {
            log.error("Error fetching item from inventory service for skuNo: {}. Error: {}", skuNo, e.getMessage());
        }
        return Optional.empty();
    }
}
