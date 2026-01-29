package com.teamgold.goldenharvest.domain.sales.command.infra;

import com.teamgold.goldenharvest.domain.inventory.query.dto.AvailableItemResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Optional;

@Component
public class InventoryApiClient {

    private final RestClient restClient;

    public InventoryApiClient(RestClient.Builder builder, @Value("${inventory.base-url}") String inventoryBaseUrl) {
        this.restClient = builder.baseUrl(inventoryBaseUrl).build();
    }

    public Optional<AvailableItemResponse> findAvailableItemBySkuNo(String skuNo) {
        try {
            List<AvailableItemResponse> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/item")
                            .queryParam("skuNo", skuNo)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (response != null && !response.isEmpty()) {
                // The response from /api/item is a list, so we take the first element.
                return Optional.of(response.get(0));
            }
        } catch (Exception e) {
            // Log the exception, handle it as per application's error handling strategy.
            // For now, we return empty to indicate the item was not found or an error occurred.
        }
        return Optional.empty();
    }
}
