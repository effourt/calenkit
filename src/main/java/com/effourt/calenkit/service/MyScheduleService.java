package com.effourt.calenkit.service;

public class MyScheduleService {
    //[사이드바에서 내 스케줄 검색 행위] : findMySchedule()
    // => TeamRepository.find
    // => ScheduleRepository.find

    //[메인에서 내 스케줄 추가 행위] : addMySchedule()
    // => ScheduleRepository.save
    // => TeamRepository.save
    // => AlarmService.save

    //[메인이나 사이드바에서 내 스케줄 작성 행위] : modifyMySchedule()
    // => ScheduleRepository.update

    //[상세에서 내 스케줄 삭제 행위] : removeMySchedule()
    // => TeamRepository.update
    // => AlarmRepository.update
    // => ScheduleRepository.update

    // [휴지통에서 내 스케줄 완전삭제 행위] : removeRealMySchedule()
    // => TeamRepository.delete
    // => ScheduleRepository.delete
    // => AlarmRepository.delete

    // [내 스케줄 즐겨찾기 행위] : favoritesMySchedule()
    // => TeamRepository.update

    // [권한있는 일정 연월 기준 출력] : getMySchedule()
    // => TeamRepository.find
    // => ScheduleRepository.find
    // => 지정 연월 검색(default : 현재 연월)
}