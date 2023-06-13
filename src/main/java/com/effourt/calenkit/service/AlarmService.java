package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.dto.AlarmCate;
import com.effourt.calenkit.repository.AlarmRepository;
import com.effourt.calenkit.repository.ScheduleRepository;
import com.effourt.calenkit.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 알람서비스는 myScheduleService 혹은 teamScheduleService에 의존하고 있으므로
// db와 관련된 예외처리는 하지 않았음
@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final TeamRepository teamRepository;
    private final ScheduleRepository scheduleRepository;
    private final AlarmRepository alarmRepository;

    /**
     * 일정번호로 team을 find해서 아이디만 배열로 반환하는 시스템 메소드
     */
    private String[] findIdList(Integer scNo) {
        List<Team> teamList = teamRepository.findBySno(scNo); //일정번호로 teamList 조회
        String[] idList = new String[teamList.size()]; //teamList의 갯수만큼 id를 담을 배열 초기화
        for (int i = 0; i < teamList.size(); i++) idList[i] = teamList.get(i).getTeamMid();
        return idList;
    }

    /**
     * 일정 수정 시 울릴 알람 서비스 - 일괄처리 save
     * 단, 동행이 있다면 함께 일괄 처리 되어야하므로 호출 시 다수의 알람 객체가 DB에 저장될 것임
     * @param scNo : 일정 번호
     */
    @Transactional
    public void addAlarmByModifySchedule(Integer scNo) {
        String[] idList = findIdList(scNo); //idList[0] , idList[1], idList[2] ...
        log.info("idList.length = {}",idList.length);
        for(int i=0; i<idList.length; i++){
            Alarm alarm = new Alarm();
            alarm.setAlMid(idList[i]);
            alarm.setAlScno(scNo);
            alarm.setAlCate(AlarmCate.MODIFY_SCHEDULE.ordinal());
            alarmRepository.save(alarm); //idList의 갯수 만큼 save(alarm) 호출
        }
    }

    /**
     * 일정 삭제(휴지통 이동) 시 관련 알람 미출력으로 변경 처리 후, 삭제처리 메세지가 울릴 알람 서비스
     * 1. 모든 동행이 가지고 있던 그동안 출력해온 해당 일정에 대한 알람을 모두 삭제해서 모두에게 출력이 안되게끔 제공 - 일괄처리 update
     * 2. 일정 삭제의 다수의 알람 객체가 DB에 저장될 것임 - 일괄처리 save
     * @param scNo : 일정 번호
     */
    @Transactional
    public void addAlarmByDeleteSchedule(Integer scNo) {
        //확실한 검증 - 일정번호가 삭제상태일 때만 아래의 로직 실행
        if(scheduleRepository.findByScNo(scNo).getScStatus()==0){
            //1.
            List<Alarm> findAlarmList = alarmRepository.findByAlScno(scNo);
            for(Alarm alarm:findAlarmList){
                alarm.setAlStatus(0);//알람상태 0으로 변경 - 동행에게는 해당 스케줄의 알람이 출력되지 않음
                alarmRepository.update(alarm);
            }
            //2.
            String[] idList = findIdList(scNo); //idList[0] , idList[1], idList[2] ...
            for(int i=0; i<idList.length; i++){
                Alarm alarm = new Alarm();
                alarm.setAlMid(idList[i]); //알람 울릴 아이디
                alarm.setAlScno(scNo); //알람 울릴 스케줄번호
                alarm.setAlCate(AlarmCate.DELETE_SCHDULE.ordinal());
                alarmRepository.save(alarm); //idList의 갯수 만큼 save(alarm) 호출
            }
        }
    }

    /**
     * 호스트가 휴지통에서 일정(스케줄)을 완전 삭제하면 DB에 저장된 알람 객체도 완전 삭제해주는 서비스
     * @param scNo : 일정 번호
     */
    @Transactional
    public void removeAlarmByScno(Integer scNo){
        String[] idList = findIdList(scNo);
        for(int i=0; i<idList.length; i++){
            alarmRepository.delete(idList[i],scNo); //알람삭제
        }
    }

    /**
     * 일정을 복구할 시 알람이 다시 출력되게끔 변경해주는 서비스
     * @param scNo : 일정 번호
     */
    @Transactional
    public void restoreAlarm(Integer scNo) {
       List<Alarm> alarmList=alarmRepository.findByAlScno(scNo); //해당 일정 알람 리스트

       for(Alarm alarm:alarmList) { //개별 출력
           if(alarm.getAlCate()==0) { //일정 삭제 알람일 경우
               alarmRepository.delete(alarm.getAlMid(), alarm.getAlScno()); //알람 삭제
           }
           if(alarm.getAlStatus()==0 && alarm.getAlCate()!=0) { //휴지통에 있는 알람이며 일정 삭제 알람이 아닐 경우
               alarm.setAlStatus(1); //상태 : 출력으로 변경
           }
           alarmRepository.update(alarm);
       }
    }

    /**
     * 일정에 초대할 시 울릴 알람 서비스
     * @param scNo : 일정 번호
     * @param addId : 일정에 초대할 아이디
     */
    @Transactional
    public Alarm addAlarmBySaveTeam(Integer scNo,String addId){
        Alarm alarm = new Alarm();
        alarm.setAlMid(addId);
        alarm.setAlScno(scNo);
        alarm.setAlCate(AlarmCate.SAVE_TEAM.ordinal());
        return alarmRepository.save(alarm);
    }

    /**
     * 일정에 삭제될 시 울릴 알람 서비스
     * @param scNo : 일정 번호
     * @param removeId : 일정을 삭제할 아이디
     */
    @Transactional
    public Alarm addAlarmByDeleteTeam(Integer scNo,String removeId){
        //기존 스케줄 알람 삭제
        alarmRepository.delete(removeId,scNo);
        //울릴 알람 추가
        Alarm newAlarm = new Alarm();
        newAlarm.setAlMid(removeId);
        newAlarm.setAlScno(scNo);
        newAlarm.setAlCate(AlarmCate.REMOVE_TEAM.ordinal());
        return alarmRepository.save(newAlarm);
    }

    /**
     * 일정의 권한을 읽기로 변경할 시 울릴 알람 서비스
     * @param scNo : 일정 번호
     * @param updateId : 일정 권한을 변경할 아이디
     */
    @Transactional
    public void addAlarmByUpdateTeamLevelRead(Integer scNo, String updateId) {
        Alarm alarm = new Alarm();
        alarm.setAlMid(updateId);
        alarm.setAlScno(scNo);
        alarm.setAlCate(AlarmCate.UPDATE_TEAMLEVEL_READ.ordinal());
        alarmRepository.save(alarm);
    }

    /**
     * 일정의 권한을 쓰기로 변경할 시 울릴 알람 서비스
     * @param scNo : 일정 번호
     * @param updateId : 일정 권한을 변경할 아이디
     */
    @Transactional
    public void addAlarmByUpdateTeamLevelWrite(Integer scNo, String updateId) {
        Alarm alarm = new Alarm();
        alarm.setAlMid(updateId);
        alarm.setAlScno(scNo);
        alarm.setAlCate(AlarmCate.UPDATE_TEAMLEVEL_WRITE.ordinal());
        alarmRepository.save(alarm);
    }
}
