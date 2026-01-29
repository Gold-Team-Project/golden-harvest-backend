package com.teamgold.goldenharvest.domain.notification.query.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationSearchRequest {

    // 기본값 지정
    private Integer page = 1;
    private Integer size = 10;
    private String userEmail;

    // page가 null이거나 1보다 작으면 1로 보정
    public int getPage() {
        if (page == null || page < 1) {
            return 1;
        }
        return page;
    }

    // size가 null이거나 1보다 작거나 너무 큰 값이면 안전한 기본값으로 보정
    public int getSize() {
        if (size == null || size < 1) {
            return 10;
        }
        // 너무 큰 요청 방지
        if (size > 100) {
            return 100;
        }
        return size;
    }

    public int getOffset() {
        return (getPage() - 1) * getSize();
    }

    public int getLimit() {
        return getSize();
    }

}
