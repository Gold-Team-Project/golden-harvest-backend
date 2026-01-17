package com.teamgold.goldenharvest.domain.sales.query.application.mapper;

import com.teamgold.goldenharvest.domain.sales.query.application.dto.OrderHistoryItem;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.OrderHistoryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SalesOrderMapper {
    List<OrderHistoryResponse> findOrderHistoryByUserEmail(String userEmail);
    OrderHistoryResponse findOrderDetailBySalesOrderId(String salesOrderId);

    @Select("""
    SELECT
        so.sales_order_id,
        sos.sales_status_name,
        so.created_at,
        so.total_amount,
        sku.item_name,
        sku.grade_name,
        sku.variety_name,
        soi.quantity AS item_quantity,
        soi.price AS item_price
    FROM
        tb_sales_order so
            JOIN
        tb_sales_order_status sos ON so.order_status_id = sos.sales_status_id
            LEFT JOIN
        tb_sales_order_item soi ON so.sales_order_id = soi.sales_order_id
            LEFT JOIN
        tb_sales_sku sku ON soi.sku_no = sku.sku_no
    ORDER BY
        so.created_at DESC
    """)
    @ResultMap("orderHistoryResultMap")
    List<OrderHistoryResponse> findAllOrderHistory();

}
