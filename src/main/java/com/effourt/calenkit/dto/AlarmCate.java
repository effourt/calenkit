package com.effourt.calenkit.dto;

import lombok.Getter;

//알람 울릴 카테고리
// => 일정 삭제(0),일정 수정(1)
// => 일정 초대(2), 초대된 일정에서 삭제(3)
// => 권한 읽기로 변경(4), 권한 쓰기로 변경(5)
@Getter
public enum AlarmCate {
    DELETE_SCHDULE("delete-schedule"), MODIFY_SCHEDULE("modify-schedule"), SAVE_TEAM("save-team"), REMOVE_TEAM("remove-team"),UPDATE_TEAMLEVEL_READ("update-teamlevel-read"),UPDATE_TEAMLEVEL_WRITE("update-teamlevel-write");
    private String keyword;
    private AlarmCate(String keyword){
        this.keyword = keyword;
    }

    /*
    public static void main(String[] args) {
        //인덱스값 출력됨 - type int
        System.out.println(AlarmCate.DELETE_SCHDULE.ordinal()); //0
        System.out.println(AlarmCate.MODIFY_SCHEDULE.ordinal()); //1
        System.out.println(AlarmCate.SAVE_TEAM.ordinal()); //2
        System.out.println(AlarmCate.REMOVE_TEAM.ordinal()); //3
        System.out.println(AlarmCate.UPDATE_TEAMLEVEL_READ.ordinal()); //4
        System.out.println(AlarmCate.UPDATE_TEAMLEVEL_WRITE.ordinal()); //5
        System.out.println(AlarmCate.UPDATE_TEAMLEVEL_WRITE.keyword); //update_teamlevel_write
        System.out.println(AlarmCate.SAVE_TEAM.getKeyword()); //save_team
    }
    */

}
