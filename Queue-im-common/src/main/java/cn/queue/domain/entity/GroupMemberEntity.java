package cn.queue.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Larry
 * @Date: 2024 /05 /19 / 13:47
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("group_member")
public class GroupMemberEntity {
    private Long id;
    private Long groupId;
    private Long memberId;
    private Integer role;
    //是否处于禁用
    private Integer status;
}
