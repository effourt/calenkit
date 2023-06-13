package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.dto.TeamShare;
import com.effourt.calenkit.exception.ExistsTeamException;
import com.effourt.calenkit.exception.MemberNotFoundException;
import com.effourt.calenkit.exception.ScheduleNotFoundException;
import com.effourt.calenkit.exception.TeamNotFoundException;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.repository.ScheduleRepository;
import com.effourt.calenkit.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamScheduleService {
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final ScheduleRepository scheduleRepository;

    /**
     * 공유하고있는 사람들이 누구인지 List<TeamShare>로 찾아주는 서비스
     * @param scNo
     * @return
     */
    public List<TeamShare> getTeam(int scNo) throws TeamNotFoundException, ScheduleNotFoundException{
        existSchedule(scNo);

        //teamList 유효성 검사
        List<Team> teamList = teamRepository.findBySno(scNo);
        if(teamList.size()==0){
            throw new TeamNotFoundException("존재하지 않는 동행 스케줄 입니다.", scNo);
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
     * @param addId : 공유받을 아이디
     * @param scNo   : 일정 번호
     */
    @Transactional
    public void addTeam(Integer scNo, String addId) throws ExistsTeamException, MemberNotFoundException, ScheduleNotFoundException{

        existSchedule(scNo);

        //findId 유효성 검사
        Member findMember = memberRepository.findByMemId(addId);
        if(findMember==null || findMember.equals("")){
            throw new MemberNotFoundException("해당 아이디는 존재하지 않습니다.");
        }
        Team findTeam = teamRepository.findBySnoAndMid(scNo, addId);
        if(findTeam == null){
            //Team 추가
            Team newTeam = new Team();
            newTeam.setTeamMid(addId);
            newTeam.setTeamSno(scNo);
            newTeam.setTeamLevel(0); //처음 insert 시는 권한레벨을 읽기로 준다(후에 수정 가능) - 읽기권한:0, 수정권한:1
            teamRepository.save(newTeam);
        } else if(findTeam != null ||!findTeam.equals("")) { //team 유효성 검사
            throw new ExistsTeamException("이미 동행으로 참여하고 있습니다.",findTeam);
        }


    }

    /**
     * 공유된 일정의 권한 정보를 변경하는서비스
     * @param updateId         : 공유받은아이디
     * @param scNo            : 일정번호
     * @param updateTeamLevel : 변경할권한레밸 (0,1)
     */
    @Transactional
    public Team modifyTeamLevel(int scNo, String updateId, int updateTeamLevel) throws TeamNotFoundException, ScheduleNotFoundException {
        existSchedule(scNo);
        existTeam(scNo,updateId);

        Team updateTeam = new Team();
        updateTeam.setTeamSno(scNo);
        updateTeam.setTeamMid(updateId);
        updateTeam.setTeamLevel(updateTeamLevel);
        teamRepository.update(updateTeam);//team에 최종적으로 권한레벨 변경
        return updateTeam;
    }

    /**
     * 공유한 사람을 삭제하는 서비스
     * @param scNo
     * @param removeId
     * @return
     */
    @Transactional
    public int removeTeam(int scNo, String removeId) throws TeamNotFoundException, ScheduleNotFoundException{
        existSchedule(scNo);
        existTeam(scNo,removeId);

        return teamRepository.delete(scNo,removeId);
    }

    /**
    scNo 유효성 검사
     */
    public void existSchedule(Integer scNo)throws ScheduleNotFoundException{
        if(scheduleRepository.findByScNo(scNo)==null || scheduleRepository.findByScNo(scNo).equals("")|| scheduleRepository.findByScNo(scNo).getScStatus()==0){
            throw new ScheduleNotFoundException("존재하지 않는 스케줄입니다.", scNo);
        }
    }
    /**
     team 유효성 검사
     */
    public void existTeam(Integer scNo, String id)throws TeamNotFoundException{
        Team findTeam = teamRepository.findBySnoAndMid(scNo,id);
        if(findTeam==null || findTeam.equals("")){
            throw new TeamNotFoundException("존재하지 않는 동행 입니다.", scNo, id);
        }
    }

}