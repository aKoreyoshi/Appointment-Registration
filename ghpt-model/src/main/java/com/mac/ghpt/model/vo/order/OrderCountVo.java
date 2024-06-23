package com.mac.ghpt.model.vo.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月19日, 20:55:10
 */
@Data
public class OrderCountVo {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private Integer count;


    /*
     具体的sql语句
    WITH RECURSIVE dates AS (
    SELECT CURRENT_DATE() AS date_only
    UNION ALL
    SELECT date_only - INTERVAL 1 DAY
    FROM dates
    WHERE date_only > CURRENT_DATE() - INTERVAL 6 DAY
)
SELECT dates.date_only, COALESCE(COUNT(order_info.create_time), 0) AS count
FROM dates
LEFT JOIN order_info ON DATE(dates.date_only) = DATE(order_info.create_time)
GROUP BY DATE(dates.date_only)
ORDER BY DATE(dates.date_only);
     */
}