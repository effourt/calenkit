package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.domain.Schedule;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.dto.TeamShare;
import com.effourt.calenkit.exception.ExistsTeamException;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamScheduleService {
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    /**
     * 공유하고있는 사람들이 누구인지 List<TeamShare>로 찾아주는 서비스
     * @param scNo
     * @return
     */
    public List<TeamShare> getTeam(int scNo) /*throws ExistsTeamException*/{

        //teamList 유효값인지 확인
        List<Team> teamList = teamRepository.findBySno(scNo); //teamList.get(0)..
        if(teamList.size()==0){
            return null; //team은 반드시 한개는 존재해야하는데..?  존재하지 않는다에 대한 예외 처리 필요 - throw new ~
            //throw new ExistsTeamException();
        }

        //imageList에 image 담기
        List<String> imageList = new ArrayList<>(); //imageList.get(0)..
        for(int i=0; i<teamList.size(); i++){
            imageList.add(memberRepository.findByMemId(teamList.get(i).getTeamMid()).getMemImage());
        }

        //각 Team에 해당하는 image 매핑해서 반환
        List<TeamShare> teamShareList = new ArrayList<>();
        for(int i=0; i<teamList.size(); i++){
            TeamShare teamShare = new TeamShare();
            teamShare.setTeamNo(teamList.get(i).getTeamNo());
            teamShare.setTeamMid(teamList.get(i).getTeamMid());
            teamShare.setTeamSno(teamList.get(i).getTeamSno());
            teamShare.setTeamBookmark(teamList.get(i).getTeamBookmark());
            teamShare.setTeamLevel(teamList.get(i).getTeamLevel());
            teamShare.setImage(imageList.get(i));
            teamShareList.add(teamShare);
        }
        return teamShareList;
    }

    /**
     * 공유할 사람을 찾아서 추가하는서비스
     * @param findId : 공유받을 아이디
     * @param scNo   : 일정 번호
     */
    @Transactional
    public Team addTeam(Integer scNo, String findId) {

        //findId 유효값인지 확인
        Member findMember = memberRepository.findByMemId(findId);
        if(findMember.equals("") || findMember==null){ //존재하지 않을 경우
            return null; //예외 처리 - throw new
        }

        //scNo 유효값인지 확인
        List<Integer> teamSnoList = teamRepository.findByid(findId);
        for(Integer teamSno:teamSnoList){
            if(teamSno==scNo) { //이미 존재할 경우
                return null; //예외 처리  - throw new
            }
        }

        //Team 추가
        Team newTeam = new Team();
        newTeam.setTeamMid(findId);
        newTeam.setTeamSno(scNo);
        newTeam.setTeamLevel(0); //처음 insert 시는 권한레벨을 읽기로 준다(후에 수정 가능) - 읽기권한:0, 수정권한:1
        teamRepository.save(newTeam);
        return newTeam;
    }

    /**
     * 공유된 일정의 권한 정보를 변경하는서비스
     * @param teamMid         : 공유받은아이디
     * @param scNo            : 일정번호
     * @param updateTeamLevel : 변경할권한레밸 (0,1)
     */
    //=> 컨트롤러에서 API로 요청 사용 [사용자가 드롭다운으로 권한을 변경할 때 페이지 리로드 없이 비동기 처리로 변경되게 만들 것임 - [부분변경:PATCH]
    @Transactional
    public Team modifyTeamLevel(int scNo, String teamMid, int updateTeamLevel) {
        Boolean isvalid = false;

        //team 유효값인지 확인
        List<Integer> teamSnoList = teamRepository.findByid(teamMid);
        for (int teamSno : teamSnoList) {
            if (teamSno == scNo) {
                isvalid = true;
            }
        }

        Team updateTeam = new Team();
        if (isvalid) {
            updateTeam.setTeamSno(scNo);
            updateTeam.setTeamMid(teamMid);
            updateTeam.setTeamLevel(updateTeamLevel);
            teamRepository.update(updateTeam);//team에 최종적으로 권한레벨 변경
        }
        return updateTeam;
    }

    /**
     * 공유한 사람을 삭제하는 서비스
     * @param scNo
     * @param teamMid
     * @return
     */
    @Transactional
    public int removeTeam(int scNo, String teamMid) {
        ///int result = -1;

        //teamMid 유효값인지 확인
        Member findMember = memberRepository.findByMemId(teamMid);
        if(findMember.equals("") || findMember==null){ //존재하지 않을 경우
            return -1; //예외 처리 - throw new
        }

        /*
        //scNo 유효값인지 확인
        List<Integer> teamSnoList = teamRepository.findByid(teamMid);
        for(Integer teamSno:teamSnoList){
            if(teamSno==scNo) {//sc==scNo 일때만
                result =  //Team 삭제
            }
        }
         */
        return teamRepository.delete(scNo,teamMid);
    }


}

