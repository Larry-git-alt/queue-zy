package cn.queue.online_judge.controller;
import cn.hutool.core.bean.BeanUtil;
import cn.queue.common.annnotation.LarryController;
import cn.queue.common.domain.CommonResult;
import cn.queue.online_judge.dto.CompetitionDTO;
import cn.queue.online_judge.pojo.Competition;
import cn.queue.online_judge.pojo.PageBean;
import cn.queue.online_judge.service.CommonService;
import cn.queue.online_judge.service.CompetitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@LarryController
@RequestMapping("/competition")
public class CompetitionController {
    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private CommonService commonService;



    @PostMapping("/create")
    public void creat(@RequestBody CompetitionDTO competitionDTO){
        log.info("新增比赛：competitionDTO：{}",competitionDTO);
        Competition competition = new Competition();
        BeanUtil.copyProperties(competitionDTO,competition);

        competitionService.create(competition, competitionDTO.getUserId());
    }

    @GetMapping
    public PageBean page(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        log.info("比赛分页查询，参数：{},{}",page,pageSize);
        //调用service分页查询
        return competitionService.page(page,pageSize);

    }


    @PostMapping
    public CommonResult unlockCom(Long comId, String invitationCode, Long userId) {
        // 检查比赛是否可以被解锁
        if (commonService.checkUnlock(comId)) {
            log.info("解锁比赛,comId:{},invitationCode:{}", comId, invitationCode);

            // 验证邀请码是否有效
            boolean isCode = competitionService.verifyCode(comId, invitationCode);
            if (isCode) {
                // 如果邀请码有效，则解锁比赛
                competitionService.unlockCom(comId, userId);
                return CommonResult.success();
            } else {
                // 如果邀请码无效，返回失败结果
                return CommonResult.fail("解锁失败，邀请码无效");
            }
        } else {
            // 如果比赛不在解锁范围内，返回失败结果
            return CommonResult.fail("解锁失败，比赛不在解锁范围内");
        }
    }

    //是否可以进去比赛
    @GetMapping("/{comId}")
    public CommonResult enterCom(@PathVariable Long comId){
        Long userId = 1L;
        if(commonService.checkCompetition(comId,userId)){
            return CommonResult.success();
        }
        else {
            return CommonResult.fail("比赛已结束");
        }

    }



}