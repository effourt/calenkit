package com.effourt.calenkit.util.websockethandler;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//구현 목록
// => 동행에 참여하는 로그인 회원 중 누군가 알람 db에 저장하는 행위가 발생될 때 실시간으로 알림 전송
// => 서버에서는 로그인 회원의 이메일별로 SocketSession 관리
@Slf4j
public class AlarmHandler extends TextWebSocketHandler {

    /**
     * 로그인한 전체 WebSocketSession을 저장하는 리스트
     */
    List<WebSocketSession> sessionList = new ArrayList<>();

    /**
     * 1:1 매핑
     * key - String : 사용자의 이메일
     * value - WebSocketSession : 사용자의 WebSocket 세션
     */
    Map<String, WebSocketSession> userSessionsMap = new HashMap<>();


    /**
     *  WebSocket 세션에서 사용자의 이메일을 가져오는 메서드
     *  세션의 속성에서 loginId라는 키로 저장된 사용자 정보를 확인하여 이메일을 반환하거나, 세션 ID를 반환
     */
    private String getEmail(WebSocketSession session) {
        Map<String, Object> httpSession = session.getAttributes();
        String loginId = (String) httpSession.get("loginId");
        if(loginId == null || loginId.equals("")) {
            return session.getId(); //세션ID 반환
        } else {
            return loginId; //이메일 반환
        }
    }

    /**
     * 클라이언트가 WebSocket에 연결되었을 때 (서버 접속 성공) 호출되는 메서드
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionList.add(session); // 로그인한 전체 WebSocket 세션을 sessionList에 추가
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
        log.info("msg = {}", msg);

        JSONParser parser = new JSONParser();

        JSONObject jsonObject = (JSONObject) parser.parse(msg);
        String scNo = (String) jsonObject.get("scNo");
        JSONArray idList = (JSONArray) jsonObject.get("idList");
        String alCate = (String) jsonObject.get("alCate");

        log.info("scNo = {}", scNo);
        log.info("idList = {}", idList);
        log.info("alCate = {}", alCate);
        for(Object id : idList){
            String stringId = (String) id;
            log.info("stringId = {}",stringId);
        }

        for(Object id : idList){
            String stringId = (String) id;
            WebSocketSession loginSession = userSessionsMap.get(stringId);

            //알람받는 이가 로그인해서 있다면 실시간 알림
            if(loginSession != null && loginSession.isOpen()) {
                TextMessage tmpMsg = null;
                switch(alCate) {
                    case "UPDATE_TEAMLEVEL_WRITE":
                        tmpMsg = new TextMessage(scNo+"번호의 스케줄의 권한이 쓰기로 변경되었습니다.");
                        break;
                    case "UPDATE_TEAMLEVEL_READ":
                        tmpMsg = new TextMessage(scNo+"번호의 스케줄의 권한이 읽기로 변경되었습니다.");
                        break;
                    case "REMOVE_TEAM":
                        tmpMsg = new TextMessage(scNo+"번호의 스케줄의 동행에서 삭제되었습니다.");
                        break;
                    case "SAVE_TEAM":
                        tmpMsg = new TextMessage(scNo+"번호의 스케줄에 동행으로 초대되었습니다.");
                        break;
                    case "MODIFY_SCHEDULE":
                        tmpMsg = new TextMessage(scNo+"번호의 스케줄이 수정되었습니다.");
                        break;
                    case "DELETE_SCHDULE":
                        tmpMsg  = new TextMessage(scNo+"번호의 스케줄이 삭제되었습니다.");
                        break;
                }
                log.info("tmpMsg = {}",tmpMsg);
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
        sessionList.remove(session); //세션과 관련된 정보 제거
    }

}
