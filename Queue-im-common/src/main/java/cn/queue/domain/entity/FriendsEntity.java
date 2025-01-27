package cn.queue.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author: Larry
 * @Date: 2024 /05 /18 / 19:54
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("friends")
public class FriendsEntity {
  private Long id;
  private Long fromId;
  private Long toId;
  private String remark;
  private Integer black;
  private Integer status;
  private Date createTime;
  private String photo;
  private String extra;
  private Long clazzId;
}
