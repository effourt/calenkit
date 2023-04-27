package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.repository.AlarmRepository;
import com.effourt.calenkit.repository.ScheduleRepository;
import com.effourt.calenkit.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final TeamRepository teamRepository;
    private final ScheduleRepository scheduleRepository;
    private final AlarmRepository alarmRepository;

    /**
     * 일정 추가 시 울릴 알람 서비스 - 없음
     */

    /**
     * 일정 수정 시 울릴 알람 서비스 - 일괄처리 save
     * 단, 동행이 있다면 함께 일괄 처리 되어야하므로 호출 시 다수의 알람 객체가 DB에 저장될 것임
     * @param scNo : 일정 번호
     */
    @Transactional
    public void addAlarmByModifySchedule(Integer scNo){
        String[] idList = findIdList(scNo); //idList[0] , idList[1], idList[2] ...
        for(int i=0; i<idList.length; i++){
            Alarm alarm = new Alarm();
            //1. 알람 번호 - 자동증가값
            alarm.setAlMid(idList[i]); //2. 알람 울릴 아이디
            alarm.setAlScno(scNo); //3. 알람 울릴 스케줄번호
            //4. 알람 상태 - 1(고정해놨음)
            //5. 알람 추가한 시간 - sysdate
            alarm.setAlCate(1); //6. 알람의 종류(1) - 일정 수정 [scNo 스케줄이 수정되었습니다.라고 출력할 것임]
            alarmRepository.save(alarm); //idList의 갯수 만큼 save(alarm) 호출
        }
    }

    /**
     * 일정 삭제 시 울릴 알람 서비스
     * 1. 모든 동행이 가지고 있던 그동안 출력해온 해당 일정에 대한 알람을 모두 삭제해서 모두에게 출력이 안되게끔 제공 - 일괄처리 update
     * 2. 일정 삭제의 다수의 알람 객체가 DB에 저장될 것임 - 일괄처리 save
     * @param scNo : 일정 번호
     */
    @Transactional
    public void addAlarmByDeleteSchedule(Integer scNo){
        //확실한 검증 - 일정번호가 삭제상태일 때만 아래의 로직 실행
        if(scheduleRepository.findByScNo(scNo).getScStatus()==0){
            //1.
            List<Alarm> findAlarmList = alarmRepository.findByAlScno(scNo);
            for(Alarm alarm:findAlarmList){
                alarm.setAlStatus(0);
                alarmRepository.update(alarm);
            }
            //2.
            String[] idList = findIdList(scNo); //idList[0] , idList[1], idList[2] ...
            for(int i=0; i<idList.length; i++){
                Alarm alarm = new Alarm();
                //1. 알람 번호 - 자동증가값
                alarm.setAlMid(idList[i]); //2. 알람 울릴 아이디
                alarm.setAlScno(scNo); //3. 알람 울릴 스케줄번호
                //4. 알람 상태 - 1(고정해놨음)
                //5. 알람 추가한 시간 - sysdate
                alarm.setAlCate(0); //6. 알람의 종류(0) - 일정 삭제 [scNo 스케줄이 삭제되었습니다.라고 출력할 것임]
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
        List<Alarm> findAlarmList = alarmRepository.findByAlScno(scNo);
        for(Alarm alarm:findAlarmList){
            alarm.setAlStatus(0);
            alarmRepository.delete(alarm.getAlNo());
        }
    }

    /**
     * 일정에 초대할 시 울릴 알람 서비스
     * 단, 일정에 초대받은 아이디의 알람 객체만 DB에 저장될 것임 - 일괄처리로 알람객체가 저장되는 것이 아님
     * @param addId : 일정에 초대할 아이디
     * @param scNo : 일정 번호
     */
    @Transactional
    public Alarm addAlarmBySaveTeam(String addId, Integer scNo){
        Alarm alarm = new Alarm();
        //1. 알람 번호 - 자동증가값
        alarm.setAlMid(addId); //2. 알람 울릴 아이디
        alarm.setAlScno(scNo); //3. 알람 울릴 스케줄
        //4. 알람 상태 - 1(고정해놨음)
        //5. 알람 추가한 시간 - sysdate
        alarm.setAlCate(1); //6. 알람의 종류(2) - 권한 변경 [scNo 스케줄에 최대되엇습니다.라고 출력할 것임]
        return alarmRepository.save(alarm);

    }

    /**
     * 일정의 권한을 변경할 시 울릴 알람 서비스
     * 단, 권한을 변경받은 동행의 알람 객체만 DB에 저장될 것임 - 일괄처리로 알람객체가 저장되는 것이 아님
     * @param updateId : 일정 권한을 변경할 아이디
     * @param scNo : 일정 번호
     */
    @Transactional
    public void addAlarmByUpdateTeamLevel(String updateId, Integer scNo){
        String[] idList = findIdList(scNo); //idList[0] , idList[1], idList[2] ...
        for(String id:idList){
            if(id.equals(updateId)) {
                Alarm alarm = new Alarm();
                //1. 알람 번호 - 자동증가값
                alarm.setAlMid(updateId); //2. 알람 울릴 아이디
                alarm.setAlScno(scNo); //3. 알람 울릴 스케줄
                //4. 알람 상태 - 1(고정해놨음)
                //5. 알람 추가한 시간 - sysdate
                alarm.setAlCate(1); //6. 알람의 종류(3) - 권한 변경 [scNo 스케줄의 권한이 변경되었습니다.라고 출력할 것임]
                alarmRepository.save(alarm);
            }
        }
    }






    //동행이 있는지 확인하는 시스템 메소드 - 동행이 있다면 true, 동행이 없다면 false
    private Boolean isTeam(Integer scNo){
        List<Team> teamList = teamRepository.findBySno(scNo);
        //if(teamList.size()==0){ //이럴 수는 없다 예외발생시키기 }
        if(teamList.size()==1) return false;
        return true;
    }

    //일정번호로 team을 find해서 아이디만 배열로 반환하는 시스템 메소드
    private String[] findIdList(Integer scNo) {
        List<Team> teamList = teamRepository.findBySno(scNo); //일정번호로 teamList 조회
        //if(teamList.size()==0){  } //이럴 수는 없다 예외발생시키기
        String[] idList = new String[teamList.size()]; //teamList의 갯수만큼 id를 담을 배열 초기화
        for (int i = 0; i < teamList.size(); i++) idList[i] = teamList.get(i).getTeamMid();
        return idList;
    }


}
