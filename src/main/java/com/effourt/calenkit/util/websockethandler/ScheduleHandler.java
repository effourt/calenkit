package com.effourt.calenkit.util.websockethandler;

import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ScheduleHandler extends TextWebSocketHandler {

    private TeamRepository teamRepository;

    public ScheduleHandler(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /**
     * 1:1 매핑
     * key - String : 사용자의 이메일
     * value - WebSocketSession : 사용자의 WebSocket 세션
     */
    Map<String, WebSocketSession> userSessionsMap = new ConcurrentHashMap<>();


    /**
     *  WebSocket 세션에서 사용자의 이메일을 가져오는 메서드
     *  세션의 속성에서 loginId라는 키로 저장된 사용자 정보를 확인하여 이메일을 반환하거나, 세션 ID를 반환
     */
    private String getEmail(WebSocketSession session) {
        Map<String, Object> httpSession = session.getAttributes();
        String loginId = (String) httpSession.get("loginId");
        if (loginId == null) {
            return session.getId();
        }
        return loginId;
    }

    /**
     * 클라이언트가 WebSocket에 연결되었을 때 (서버 접속 성공) 호출되는 메서드
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String email = getEmail(session); // 세션에 연결된 사용자의 이메일
        userSessionsMap.put(email, session); // 세션에 연결된 사용자의 이메일과 세션을 userSessionsMap에 저장
        log.info("email = {}", email);
        log.info("session = {}", session);
    }

    /**
     * WebSocketSession 으로 메시지가 수신되었을 때 (소켓에 메세지를 보냈을때) 호출되는 메서드
     * 수신된 메시지를 처리 후 특정 사용자에게 메시지 전달
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        Map<String, Object> httpSession = session.getAttributes();
        String loginId = (String) httpSession.get("loginId");
        log.info("msg = {}", msg);

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(msg);
        Integer scNo = Integer.parseInt((String) jsonObject.get("scNo"));
        String content = (String) jsonObject.get("content");
        log.info("scNo = {}", scNo);
        log.info("data = {}", content);

        List<Team> teamList = teamRepository.findBySno(scNo);

        for(Team team : teamList) {
            WebSocketSession loginSession = userSessionsMap.get(team.getTeamMid());
            if (loginId.equals(team.getTeamMid())) {//작성자에게는 미전송
                continue;
            }

            //알람받는 이가 로그인해서 있다면 실시간 알림
            if(loginSession != null && loginSession.isOpen()) {
                TextMessage tmpMsg = new TextMessage(content);
                log.info("tmpMsg = {}", tmpMsg);
                loginSession.sendMessage(tmpMsg);
            }
        }
    }



    /**
     * WebSocket 세션이 닫힐 때(연결 해제될때) 호출되는 메서드
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("afterConnectionClosed = {}, {}",session.getId(),status);
        userSessionsMap.remove(session.getId());
    }
}
