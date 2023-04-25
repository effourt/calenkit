package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.domain.Schedule;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.repository.AlarmRepository;
import com.effourt.calenkit.repository.ScheduleRepository;
import com.effourt.calenkit.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final TeamRepository teamRepository;
    private final ScheduleRepository scheduleRepository;
    private final AlarmRepository alarmRepository;

    /*
    private Integer alNo;
    private String alScno;
    private String alMid;
    private Integer alStatus;
    private String alTime;
    private Integer alCate;
     */
    // => TeamRepository.find

    // => TeamRepository.modify
    // => AlarmRepository.save
    //일정 수정(1),초대(2),시 호출

    /**
     * 스케줄 추가 시 알람 서비스 - 없음
     */

    /**
     * 스케줄 수정 시 알람 서비스 - 단 동행이 있다면 같이 알람이 추가되어야함
     * @param mId : 로그인 아이디
     * @param scNo : 일정 번호
     */
    public void addAlarmModifySchedule(String mId, int scNo){

        if(isfindTeam(scNo)) { //동행이 있다면 동행들도 추가

        } else{ //동행이 없다면 나만 추가

        }
        /*
        Schedule findSchedule = scheduleRepository.findByScNo(scNo);
        findSchedule.get
        Alarm alarm = new Alarm();
        alarm.setAlMid(mId);
        alarm.getAlStatus();
        alarm.setAlCate(1);
        alarmRepository.save(alarm);
        */
    }

    /**
     * 스케줄 초대 시 알람 서비스 - 단 동행이 있다면 같이 알람이 추가되어야함
     * @param mId : 로그인 아이디
     * @param scNo : 일정 번호
     */
    public void addAlarmInviteTeam(String mId, int scNo){
        Alarm alarm = new Alarm();
        alarm.setAlMid(mId);
        alarm.getAlStatus();
        alarm.setAlCate(2);
        alarmRepository.save(alarm);
    }

    /**
     * 스케줄 삭제 시 알람 서비스
     * @param mId : 로그인 아이디
     * @param scNo : 일정 번호
     */
    public void addAlarmDeleteSchedule(String mId, int scNo){
        Schedule findSchedule = scheduleRepository.findByScNo(scNo); //해당 스케줄 조회
        Alarm newAlarm = new Alarm();
        if(findSchedule.getScStatus()==0){ //만약 스케줄이 삭제 상태라면 새로운 알람 추가
            newAlarm.setAlScno(scNo); //스케줄 번호
            newAlarm.setAlMid(mId); //로그인 아이디
            newAlarm.setAlCate(0); //알람 카테고리(0) - scNo 스케줄이 삭제되었습니다.라고 출력할 것임
        }
        alarmRepository.save(newAlarm); //테이블에 알람이 추가되면서 사용자에게 알람이 출력된다.
    }


    //동행이 있는지 확인하는 시스템 메소드 - 동행이 있다면 true, 동행이 없다면 false
    private Boolean isfindTeam(int scNo){
        List<Team> teamList = teamRepository.findBySno(scNo);
        /*
        if(teamList.size()==0){
            //이럴 수는 없다 예외발생시키기
        }
        */
        if(teamList.size()==1){
            return false;
        }
        return true;
    }
}
