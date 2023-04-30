package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class TeamScheduleService {
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

/*
DOMAIN	REPOSITORY(mybatis3, JPA)	SERVICE
        INSERT	SAVE	ADD
        UPDATE	UPDATE	MODIFY
        DELETE	DELETE	REMOVE
        SELECT	FIND	GET
*/
    /**
     * 공유할 사람을 찾아서 추가하는서비스
     * @param findId : 공유받을 아이디
     * @param scNo : 일정 번호
     */
   /* public Team addTeam(String findId, int scNo){
        Member findMember = memberRepository.findByMemId(findId);
        Team team = new Team();
        team.setTeamMid(findMember.getMemId());
        team.setTeamSno(scNo);
        team.setTeamLevel(0); //처음 insert 시는 권한레벨을 읽기로 준다(후에 수정 가능) - 읽기권한:0, 수정권한:1

        //이메일을 보내는 객체의 메소드 호출

        //만약 사용자가 받은 이메일의 링크를 수락할 경우, team에 최종적으로 추가
        return teamRepository.save(team);
    }*/

    /**
     * 공유된 일정의 권한 정보를 변경하는서비스
     * @param teamMid : 공유받은아이디
     * @param scNo : 일정번호
     * @param updateTeamLevel : 변경할권한레밸
     */
    //=> 컨트롤러에서 API로 요청 사용 [사용자가 드롭다운으로 권한을 변경할 때 페이지 리로드 없이 비동기 처리로 변경되게 만들 것임 - [부분변경:PATCH]
    public void modifyTeamLevel(String teamMid, int scNo, int updateTeamLevel) {
        //전달받은 아이디로 팀 테이블 검색
        List<Team> teamList = teamRepository.findByMid(teamMid);

        Team updateTeam = new Team();
        for(Team team:teamList){
            if(team.getTeamSno().equals(scNo)){
                updateTeam.setTeamLevel(updateTeamLevel);
            }
        }

        //team에 최종적으로 권한레벨 변경
        teamRepository.update(updateTeam);
    }

}
