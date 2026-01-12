package com.teamgold.goldenharvest.common.infra.harvest;

import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.price.PriceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class HarvestClient {

    private final RestTemplate template;

    @Value("${kamis.cert.key}")
    private String certKey;

    @Value("${kamis.cert.id}")
    private String certId;

    public String callPrice(PriceRequest req) {

        String url = UriComponentsBuilder
                .fromUriString("https://www.kamis.or.kr/service/price/xml.do")
                .queryParam("action", "dailyPriceByCategoryList")
                .queryParam("p_cert_key", certKey)
                .queryParam("p_cert_id", certId)
                .queryParam("p_returntype", "json")
                .queryParam("p_product_cls_code", req.getProduct_cls_code())
                .queryParam("p_item_category_code", req.getItem_category_code())
                .queryParam("p_country_code", req.getP_country_code())
                .queryParam("p_regday", req.getP_regday())
                .queryParam("p_convert_kg_yn", "N")
                .build(true)
                .toUriString();

        log.info("[KAMIS] request url={}", url);

        return getHeader(url);
    }

    public String callProduct(MasterRequest req) {

        String url = UriComponentsBuilder
                .fromUriString("https://www.kamis.or.kr/service/price/xml.do")
                .queryParam("action", "productInfo")
                .queryParam("p_returntype", "json")
                .queryParam("p_cert_key", certKey)
                .queryParam("p_cert_id", certId)
                .queryParam("p_startday", req.getPStartday())
                .queryParam("p_endday", req.getPEndday())
                .queryParam("p_countrycode", req.getPCountrycode())
                .queryParam("p_itemcategorycode", req.getPItemcategorycode())
                .queryParam("p_itemcode", req.getPItemcode())
                .queryParam("p_kindcode", req.getPKindcode())
                .queryParam("p_productrankcode", req.getPProductrankcode())
                .queryParam("p_convert_kg_yn", "N")
                .build(true)
                .toUriString();


        log.info("[KAMIS] request url={}", url);

        return getHeader(url);
    }

    public String getHeader(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT,
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120.0.0.0 Safari/537.36");
        headers.set(HttpHeaders.CONNECTION, "close");
        headers.setAccept(MediaType.parseMediaTypes("application/json"));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response =
                    template.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();

        } catch (Exception e) {
            log.error("[KAMIS] call failed", e);
            throw new IllegalArgumentException("KAMIS API 호출 실패", e);
        }
    }
}
