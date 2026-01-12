    package com.teamgold.goldenharvest.domain.master.command.application.dto.response.price;

    import lombok.Builder;
    import lombok.Getter;

    import java.math.BigDecimal;
    import java.time.LocalDate;

    @Getter
    @Builder
    public class PriceResponse {

        private String itemCode;
        private String itemName;

        private String kindCode;
        private String kindName;

        private String rank;
        private String unit;

        private BigDecimal dpr1;
    }
