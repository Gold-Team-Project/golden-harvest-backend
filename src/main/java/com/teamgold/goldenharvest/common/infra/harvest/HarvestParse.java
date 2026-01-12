package com.teamgold.goldenharvest.common.infra.harvest;

import com.teamgold.goldenharvest.domain.master.command.application.dto.response.master.MasterResponse;
import com.teamgold.goldenharvest.domain.master.command.application.dto.response.price.PriceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class HarvestParse {
    private final ObjectMapper mapper = new ObjectMapper();

    public List<PriceResponse> parsePrice(String response) {
        try {
            JsonNode root = mapper.readTree(response);
            JsonNode dataNode = root.path("data");

            if (!dataNode.isObject()) {
                return List.of();
            }

            JsonNode items = dataNode.path("item");
            if (!items.isArray()) {
                return List.of();
            }

            List<PriceResponse> result = new ArrayList<>();

            for (JsonNode node : items) {

                String rawPrice = safeText(node, "dpr1");
                if (rawPrice.isBlank() || "-".equals(rawPrice)) continue;

                String normalized = rawPrice.replaceAll("[^0-9]", "");
                if (normalized.isBlank()) continue;

                result.add(
                        PriceResponse.builder()
                                .itemCode(safeText(node, "item_code"))
                                .itemName(safeText(node, "item_name"))
                                .kindCode(safeText(node, "kind_code"))
                                .kindName(safeText(node, "kind_name"))
                                .rank(safeText(node, "rank_code"))
                                .unit(safeText(node,"unit"))
                                .dpr1(new BigDecimal(normalized))
                                .build()
                );
            }

            return result;

        } catch (Exception e) {
            throw new IllegalArgumentException("KAMIS 가격 파싱 실패", e);
        }
    }


    public List<MasterResponse> parseProduct(String response) {
        try {
            JsonNode root = mapper.readTree(response);
            JsonNode items = root.path("info");

            if (items.isMissingNode() || !items.isArray()) {
                log.warn("상품 마스터 데이터가 존재하지 않습니다. (info node missing or not array)");
                return List.of();
            }

            List<MasterResponse> result = new ArrayList<>();

            for (JsonNode node : items) {
                result.add(MasterResponse.builder()
                        .itemName(safeText(node, "itemname"))
                        .kindName(safeText(node, "kindname"))
                        .kindCode(safeText(node, "kindcode"))
                        .baseUnit(safeText(node, "wholesale_unit"))
                        .unitSize(safeText(node, "wholesale_unitsize"))
                        .countryName("국산")
                        .productRank(safeText(node, "retail_productrankcode"))
                        .itemCode(safeText(node, "itemcode"))
                        .build());
            }

            log.info("상품 마스터 파싱 완료: {}건", result.size());
            return result;

        } catch (Exception e) {
            log.error("KAMIS 상품 정보 파싱 중 에러 발생 Response: {}", response);
            throw new IllegalArgumentException("KAMIS 상품 정보 파싱 실패", e);
        }
    }


    private String safeText(JsonNode node, String key) {
        JsonNode valueNode = node.path(key);
        if (valueNode.isMissingNode() || valueNode.isArray() || valueNode.isNull()) {
            return "";
        }
        return valueNode.asText();
    }
}

