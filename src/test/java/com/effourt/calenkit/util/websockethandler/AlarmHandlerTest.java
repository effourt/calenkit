package com.effourt.calenkit.util.websockethandler;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@SpringBootTest
public class AlarmHandlerTest {

    @DisplayName("/alarm으로 메시지가 수신되었을 때 호출될 메서드")
    @Test
    void a() throws JSONException {
        String jsonString = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}";
        // JSON 문자열 출력
        System.out.println(jsonString);
        // JSON 객체 생성
        JSONObject jsonObject = new JSONObject(jsonString);
        // 특정 키에 해당하는 값을 가져오기
        String name = jsonObject.getString("name");
        int age = jsonObject.getInt("age");
        String city = jsonObject.getString("city");
        // 결과 출력
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("City: " + city);

    }
}

