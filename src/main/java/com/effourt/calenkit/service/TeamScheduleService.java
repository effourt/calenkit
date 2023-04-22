package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.dto.TeamMember;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TeamScheduleService {
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    //단순 find만 하는 로직
    /*
    public Team 상세페이지클릭하기(Member loginMember, int scNo){
        int teamSno = scNo;
        List<Team> teamList = teamRepository.findBySno(teamSno);
        Team loginTeam = new Team();
        for(Team team:teamList){
            if(team.getTeamMid().equals(loginMember.getMemId())){
                loginTeam.setTeamNo(team.getTeamNo());
                loginTeam.setTeamSno(team.getTeamSno());
                loginTeam.setTeamBookmark(team.getTeamBookmark());
                loginTeam.setTeamLevel(team.getTeamLevel());
                loginTeam.setTeamMid(team.getTeamMid());
            }
        }
        return loginTeam;
    }
   */

    public void 공유할사람을찾아서_추가하는서비스(String 공유받을아이디, int scNo){
        Member 공유받을회원 = memberRepository.findByMemId(공유받을아이디);
        int teamSno=scNo;

        Team team = new Team();
        team.setTeamMid(공유받을회원.getMemId());
        team.setTeamSno(teamSno);
        team.setTeamLevel(0); //처음 insert 시는 권한레벨을 읽기로 준다(후에 수정 가능) - 읽기권한:0, 수정권한:1

        //이메일을 보내는 객체의 메소드 호출

        //만약 사용자가 받은 이메일의 링크를 수락할 경우, team에 최종적으로 추가
        teamRepository.save(team);
    }


    //=> 컨트롤러에서 API로 요청 사용 [사용자가 드롭다운으로 권한을 변경할 때 페이지 리로드 없이 비동기 처리로 변경되게 만들 것임 - [부분변경:PATCH]
    public void 공유된일정의권한정보를_변경하는서비스(String 공유받은아이디, int scNo, int 변경할권한레밸) {
        //전달받은 아이디로 팀 테이블 검색
        List<Team> teamList = teamRepository.findByMid(공유받은아이디);

        Team 변경할team = new Team();
        for(Team team:teamList){
            if(team.getTeamSno().equals(scNo)){
                변경할team.setTeamLevel(변경할권한레밸);
            }
        }
        //team에 최종적으로 권한레벨 변경
        teamRepository.update(변경할team);
    }

}
