package cn.queue.online_judge.service;

import cn.queue.online_judge.pojo.Competition;
import cn.queue.online_judge.pojo.PageBean;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CompetitionService extends IService<Competition> {
    void create(Competition competition,Long userId);

    PageBean page(Integer page, Integer pageSize);

    boolean verifyCode(Long comId, String invitationCode);

    void unlockCom(Long comId, Long userId);
}
