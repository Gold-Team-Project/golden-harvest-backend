package com.teamgold.goldenharvest.domain.sales.query.adapter.in.web;

import com.teamgold.goldenharvest.domain.sales.query.application.dto.response.MyOrderResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.service.SalesOrderQueryService;
import com.teamgold.goldenharvest.domain.sales.query.adapter.in.web.SalesOrderQueryController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SalesOrderQueryController.class)
class SalesOrderQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalesOrderQueryService salesOrderQueryService;

    @Test
    void getMyOrders() throws Exception {
        // given
        String userEmail = "test@example.com";
        MyOrderResponse myOrderResponse = MyOrderResponse.builder()
                .salesOrderId("order-123")
                .orderStatus("PENDING")
                .createdAt(LocalDate.now())
                .totalAmount(BigDecimal.valueOf(100.00))
                .build();
        List<MyOrderResponse> myOrders = Collections.singletonList(myOrderResponse);

        given(salesOrderQueryService.getMyOrders(userEmail)).willReturn(myOrders);

        // when & then
        mockMvc.perform(get("/api/sales-orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].salesOrderId").value("order-123"))
                .andExpect(jsonPath("$[0].orderStatus").value("PENDING"));
    }
}
