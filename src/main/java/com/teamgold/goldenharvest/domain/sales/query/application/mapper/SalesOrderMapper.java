package com.teamgold.goldenharvest.domain.sales.query.application.mapper;

import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderDetailResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderHistoryResponse;
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
        sc.customer_company AS company,
        pm.item_name,       -- ProduceMaster를 통해 Variety에서 가져옴
        g.grade_name,       -- Grade에서 가져옴
        v.variety_name,     -- Variety에서 가져옴
        soi.quantity AS item_quantity,
        soi.price AS item_price
    FROM
        tb_sales_order so
            JOIN
        tb_sales_order_status sos ON so.order_status_id = sos.sales_status_id
            LEFT JOIN
        tb_sales_order_item soi ON so.sales_order_id = soi.sales_order_id
            LEFT JOIN
        tb_sku s ON soi.sku_no = s.sku_no
            LEFT JOIN
        tb_variety v ON s.item_code = v.item_code AND s.variety_code = v.variety_code
            LEFT JOIN
        tb_produce_master pm ON v.item_code = pm.item_code
            LEFT JOIN
        tb_grade g ON s.grade_code = g.grade_code
            LEFT JOIN
        tb_sales_customer_info sc ON so.user_email = sc.customer_email
    ORDER BY
        so.created_at DESC;
    
    """)
    @ResultMap("adminOrderHistoryResultMap")
    List<AdminOrderHistoryResponse> findAllOrderHistory();

    AdminOrderDetailResponse findAdminOrderDetailBySalesOrderId(String salesOrderId);
}
