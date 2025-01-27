package cn.queue.imcore.controller;

import cn.queue.domain.dto.GroupInviteDTO;
import cn.queue.domain.entity.GroupEntity;
import cn.queue.imcore.service.IGroupService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/group")
public class GroupController {

    @Resource
    private IGroupService groupService;


    /**
     * 创建群聊
     * @param group
     */
    @PostMapping
    public void createGroup(@RequestBody GroupEntity group) {
        groupService.createGroup(group);
    }

    /**
     * 邀请成员
     * @param groupInviteDTO
     */
    @PostMapping("/invite")
    public void invite(@RequestBody GroupInviteDTO groupInviteDTO) {
        groupService.invite(groupInviteDTO);
    }
}
