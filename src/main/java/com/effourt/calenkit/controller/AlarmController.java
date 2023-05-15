package com.effourt.calenkit.controller;

import com.effourt.calenkit.dto.AlarmCate;
import com.effourt.calenkit.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    // 클라 -> /app/find-alarm/delete-schedule : DELETE_SCHDULE
    // 클라 -> /app/find-alarm/modify-schedule : MODIFY_SCHEDULE

    // 클라 -> /app/find-alarm/save-team : SAVE_TEAM
    // 클라 -> /app/find-alarm/remove-team : REMOVE_TEAM
    // 클라 -> /app/find-alarm/update-teamlevel-read : UPDATE_TEAMLEVEL_READ
    // 클라 -> /app/find-alarm/update-teamlevel-write : UPDATE_TEAMLEVEL_WRITE

    /**
     * 알람 추가
     * @param scNo
     * @param alCate
     * @param map
     * @return
     */
    //  http://localhost:8080/alarms/145/delete-schedule
    //  http://localhost:8080/alarms/145/modify-schedule
    //  http://localhost:8080/alarms/145/save-team id:""
    //  http://localhost:8080/alarms/145/remove-team id:""
    //  http://localhost:8080/alarms/145/update-teamlevel-read id:""
    //  http://localhost:8080/alarms/145/update-teamlevel-write id:""
    @PostMapping("/alarms/{scNo}/{alCate}")
    @ResponseBody
    public String addAlarms(@PathVariable Integer scNo, @PathVariable String alCate, @RequestBody HashMap<String,String> map) {
        String alMid = map.get("id");
        if (alCate.equals(AlarmCate.DELETE_SCHDULE.getKeyword())) {
            alarmService.addAlarmByDeleteSchedule(scNo);
            return "delete-schedule-add-alarm";
        } else if (alCate.equals(AlarmCate.MODIFY_SCHEDULE.getKeyword())) {
            alarmService.addAlarmByModifySchedule(scNo);
            return "modify-schedule-add-alarm";
        } else if (alCate.equals(AlarmCate.SAVE_TEAM.getKeyword())) {
            alarmService.addAlarmBySaveTeam(scNo, alMid);
            return "save-team-add-alarm";
        } else if (alCate.equals(AlarmCate.REMOVE_TEAM.getKeyword())) {
            alarmService.addAlarmByDeleteTeam(scNo, alMid);
            return "remove-team-add-alarm";
        } else if (alCate.equals(AlarmCate.UPDATE_TEAMLEVEL_READ.getKeyword())) {
            alarmService.addAlarmByUpdateTeamLevelRead(scNo, alMid);
            return "update-teamlevel-read-add-alarm";
        } else if (alCate.equals(AlarmCate.UPDATE_TEAMLEVEL_WRITE.getKeyword())) {
            alarmService.addAlarmByUpdateTeamLevelWrite(scNo, alMid);
            return "update-teamlevel-write-add-alarm";
        }
        return "fail";
    }

    /**
     * 알람 완전 삭제
     * @param scNo
     * @return
     */
    @DeleteMapping ("/alarms/{scNo}")
    @ResponseBody
    public String deleteAlarms(@PathVariable Integer scNo) {
        alarmService.removeAlarmByScno(scNo);
        return "remove-alarm";
    }

    /**
     * 알람 복구
     * @param scNo
     * @return
     */
    @PostMapping("/alarms/{scNo}")
    @ResponseBody
    public String restoreAlarms(@PathVariable Integer scNo) {
        alarmService.restoreAlarm(scNo);
        return "restore-alarm";
    }

}