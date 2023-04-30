package com.effourt.calenkit.controller;
import com.effourt.calenkit.repository.TeamRepository;
import com.effourt.calenkit.service.TeamScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamScheduleService teamScheduleService;
    private final TeamRepository teamRepository;

}
