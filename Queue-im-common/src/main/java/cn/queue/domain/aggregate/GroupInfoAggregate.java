package cn.queue.domain.aggregate;
import cn.queue.domain.dto.MemberDTO;
import cn.queue.domain.entity.GroupEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: Larry
 * @Date: 2024 /05 /19 / 13:30
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupInfoAggregate {
    //群信息
    private GroupEntity group;
    //群成员信息
    private List<MemberDTO> memberList;

}
