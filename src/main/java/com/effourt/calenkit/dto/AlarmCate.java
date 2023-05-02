package com.effourt.calenkit.dto;

//알람 울릴 카테고리
// => 일정 삭제(0),수정(1),초대(2),권한변경(3)
public enum AlarmCate {
    DELETE_SCHDULE, MODIFY_SCHEDULE, SAVE_TEAM, UPDATE_TEAMLEVEL;
    /*
    public static void main(String[] args) {
        //인덱스값 출력됨 - type int
        System.out.println(AlarmCate.DELETE_SCHDULE.ordinal()); //0
        System.out.println(AlarmCate.MODIFY_SCHEDULE.ordinal()); //1
        System.out.println(AlarmCate.SAVE_TEAM.ordinal()); //2
        System.out.println(AlarmCate.UPDATE_TEAMLEVEL.ordinal()); //3
    }
    */
}
