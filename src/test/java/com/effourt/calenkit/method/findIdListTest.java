package com.effourt.calenkit.method;

import com.effourt.calenkit.domain.Team;

import java.util.ArrayList;
import java.util.List;

//배열로 담을 아이디를 테스트하기 위해 만듦 - AlarmService 시스템 메소드 테스트
public class findIdListTest {
    public static void main(String[] args) {

            Team team1 = new Team();
            Team team2 = new Team();
            Team team3 = new Team();

            team1.setTeamMid("abc123");
            team2.setTeamMid("xyz123");
            team3.setTeamMid("qwe123");
            team1.setTeamSno(1);
            team2.setTeamSno(1);
            team3.setTeamSno(1);

            List<Team> teamList = new ArrayList<>();
            teamList.add(team1);
            teamList.add(team2);
            teamList.add(team3);


            String[] idList = new String[teamList.size()]; //team의 id만 담을 배열 초기화
            for (int i = 0; i < teamList.size(); i++) {
                idList[i] = teamList.get(i).getTeamMid();
            }

        System.out.println(idList.length);
        System.out.println(idList[0]);
        System.out.println(idList[1]);
        System.out.println(idList[2]);
    }
}
