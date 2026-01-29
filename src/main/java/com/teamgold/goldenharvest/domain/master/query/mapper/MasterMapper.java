package com.teamgold.goldenharvest.domain.master.query.mapper;

import com.teamgold.goldenharvest.domain.master.query.dto.response.master.MasterDataDetailResponse;
import com.teamgold.goldenharvest.domain.master.query.dto.response.master.MasterDataListResponse;
import com.teamgold.goldenharvest.domain.master.query.dto.response.price.OriginPriceResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MasterMapper {
    @Select("""
            <script>
            SELECT
                s.sku_no         AS skuNo,
                pm.item_code     AS itemCode,
                pm.item_name     AS itemName,
                v.variety_name   AS varietyName,
                g.grade_name     AS gradeName,
                pm.created_at    AS createdAt,
                pm.updated_at    AS updatedAt,
                pm.is_active     AS status
            FROM tb_sku s
            INNER JOIN tb_produce_master pm
                ON s.item_code = pm.item_code
            INNER JOIN tb_variety v
                ON s.item_code = v.item_code
               AND s.variety_code = v.variety_code
            INNER JOIN tb_grade g
                ON s.grade_code = g.grade_code
            WHERE 1=1
            
            <if test="itemName != null and itemName != ''">
                AND pm.item_name LIKE CONCAT('%', #{itemName}, '%')
            </if>
            
            <if test="varietyName != null and varietyName != ''">
                AND v.variety_name LIKE CONCAT('%', #{varietyName}, '%')
            </if>
            
            <if test="itemCode != null and itemCode != ''">
                AND pm.item_code LIKE CONCAT('%', #{itemCode}, '%')
            </if>
            
            <if test="gradeName != null and gradeName != ''">
                AND g.grade_name LIKE CONCAT('%', #{gradeName}, '%')
            </if>
            
            <if test="isActive != null">
                AND pm.is_active = #{isActive}
            </if>
            
            ORDER BY pm.item_code ASC
            LIMIT #{limit}
            OFFSET #{offset}
            </script>
            """)
    List<MasterDataListResponse> findAllMasterData(
            @Param("itemName") String itemName,
            @Param("varietyName") String varietyName, // 추가됨
            @Param("itemCode") String itemCode,
            @Param("gradeName") String gradeName,
            @Param("isActive") Boolean isActive,
            @Param("limit") int limit,
            @Param("offset") int offset
    );


    @Select("""
                SELECT
                    s.sku_no             AS skuNo,
                    pm.item_code         AS itemCode,
                    pm.item_name         AS itemName,
                    v.variety_name       AS varietyName,
                    pm.base_unit         AS baseUnit,
                    pm.pack_to_kg        AS packToKg,
                    g.grade_name         AS grade,
                    pm.created_at        AS createdAt,
                    pm.updated_at        AS updatedAt,
                    pm.country           AS country,
                    pm.description       AS description,
                    pm.shelf_life_days   AS shelfLifeDays,
                    pm.storage_temp_min  AS storageTempMin,
                    pm.storage_temp_max  AS storageTempMax,
                    pm.file_url          AS fileUrl
            
                FROM tb_sku s
                INNER JOIN tb_variety v
                    ON s.item_code = v.item_code
                   AND s.variety_code = v.variety_code
                INNER JOIN tb_produce_master pm
                    ON s.item_code = pm.item_code
                INNER JOIN tb_grade g
                    ON s.grade_code = g.grade_code
                WHERE s.sku_no = #{skuNo}
            """)
    MasterDataDetailResponse findDetailMasterData(@Param("skuNo") String skuNo);


    @Select("""
                SELECT
                    op.origin_price AS originPrice,
                    op.unit         AS unit,
                    op.created_at   AS createdAt
                FROM tb_origin_price op
                WHERE op.sku_no = #{skuNo}
                ORDER BY op.created_at DESC
                LIMIT 7
            """)
    List<OriginPriceResponse> findRecentOriginPrices(
            @Param("skuNo") String skuNo
    );

}


