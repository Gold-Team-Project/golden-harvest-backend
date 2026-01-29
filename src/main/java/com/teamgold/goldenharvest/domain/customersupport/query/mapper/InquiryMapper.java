package com.teamgold.goldenharvest.domain.customersupport.query.mapper;

import com.teamgold.goldenharvest.domain.customersupport.query.dto.response.AdminInquiryDetailResponse;
import com.teamgold.goldenharvest.domain.customersupport.query.dto.response.AdminInquiryListResponse;
import com.teamgold.goldenharvest.domain.customersupport.query.dto.response.InquiryDetailResponse;
import com.teamgold.goldenharvest.domain.customersupport.query.dto.response.InquiryListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InquiryMapper {
    //문의 상세
    @Select("""
            SELECT
                i.title         AS title,
                i.body          AS body,
                i.comment       AS comment,
                NULL            AS fileId,     
                SUBSTRING_INDEX(i.file_url, '/', -1) AS fileName,
                NULL            AS contentType, 
                i.file_url      AS downloadUrl
            FROM tb_inquiry i
            WHERE i.inquiry_id = #{inquiryNo}
        """)
    InquiryDetailResponse findDetailInquiry(@Param("inquiryNo") String inquiryNo);

    //문의 내역
    @Select("""
                SELECT
                    i.inquiry_id       AS inquiryNo,
                    i.created_at       AS createdAt,
                    i.title            AS title,
                    i.processing_status AS processingStatus
                FROM tb_inquiry i
                WHERE i.user_id = #{userId}
                ORDER BY i.created_at DESC
                LIMIT #{limit} OFFSET #{offset}
            """)
    List<InquiryListResponse> findAllInquiry(
            @Param("userId") String userId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Select("""
    SELECT
        i.inquiry_id        AS inquiryNo,      
        i.created_at        AS createdAt,      
        s.company           AS company,        
        s.name              AS name,           
        s.phone_number      AS phoneNumber,    
        s.email             AS email,          
        i.title             AS title,          
        i.body              AS body,           
        i.comment           AS comment,        
        i.processing_status AS processingStatus, 
        NULL                AS fileId,        
        SUBSTRING_INDEX(i.file_url, '/', -1) AS fileName, 
        NULL                AS contentType,    
        i.file_url          AS downloadUrl     
    FROM tb_inquiry i
    LEFT JOIN tb_inquiry_writer_snapshot s
      ON i.inquiry_id = s.inquiry_id
    WHERE i.inquiry_id = #{inquiryNo}
""")
    AdminInquiryDetailResponse findDetailAdminInquiry(@Param("inquiryNo") String inquiryNo);

    //문의 내역(관리자)
    @Select("""
                SELECT  
                    i.inquiry_id        AS inquiryNo,
                    i.created_at        AS createdAt,
                    s.company           AS company,
                    i.title             AS title,
                    i.processing_status AS processingStatus
                FROM tb_inquiry i 
                LEFT JOIN tb_inquiry_writer_snapshot s 
                  ON i.inquiry_id = s.inquiry_id
                ORDER BY i.created_at DESC
                LIMIT #{limit} OFFSET #{offset}
            """)
    List<AdminInquiryListResponse> findAllAdminInquiry(@Param("limit") int limit, @Param("offset") int offset);
}
