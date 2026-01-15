package com.teamgold.goldenharvest.common.infra.harvest;

import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterDataRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.price.PriceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class HarvestClient {

    private final RestClient client;

    @Value("${kamis.cert.key}")
    private String certKey;

    @Value("${kamis.cert.id}")
    private String certId;

    public String callPrice(PriceRequest req) {

        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/service/price/xml.do")
                        .queryParam("action", "dailyPriceByCategoryList")
                        .queryParam("p_cert_key", certKey)
                        .queryParam("p_cert_id", certId)
                        .queryParam("p_returntype", "json")
                        .queryParam("p_product_cls_code", req.getProduct_cls_code())
                        .queryParam("p_item_category_code", req.getItem_category_code())
                        .queryParam("p_country_code", req.getP_country_code())
                        .queryParam("p_regday", req.getP_regday())
                        .queryParam("p_convert_kg_yn", "N")
                        .build()
                )
                .retrieve()
                .body(String.class);


    }

    public String callProduct(MasterDataRequest req) {

        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/service/price/xml.do")
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
                        .build()
                )
                .retrieve()
                .body(String.class);
    }
}
