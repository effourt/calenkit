//도메인 주소
const domainURL = "";

//아이디, 비밀번호 존재 여부 체크
$("#checkIdBtn").click(function () {
    const loginId = $("#loginId").val();
    $.ajax({
        url: domainURL + "/login/check",
        method: "POST",
        contentType: "application/json",
        dataType: "text",
        data: JSON.stringify({"id": loginId}),
        success: function (result) {
            if (result == "PASSWORD_LOGIN") {
                $("#passwordDiv").show()
                $("#checkId").hide()
            } else if (result == "CODE_LOGIN") {
                sendCode(loginId);
                $("#loginCodeDiv").show();
                $("#checkId").hide();
            } else if (result == "JOIN_LOGIN") {
                sendCode(loginId);
                $("#registerCodeDiv").show();
                $("#checkId").hide();
            } else {
                $("#message").text(result);
            }
        },
        error: function (error) {
            alert("아이디 체크 중 에러 발생 : " + error.status);
        }
    });
});

function sendCode(loginId) {
    $("#message").text("이메일로 코드를 전송하였습니다.")
    let value = "";
    $.ajax({
        url: domainURL + "/login/send-code",
        method: "POST",
        contentType: "application/json",
        dataType: "text",
        data: JSON.stringify({"id": loginId}),
        success: function(result) {
            value = result;
            if (result != "OK") {
                $("#message").text(result);
            }
        },
        error: function(error) {
            alert("이메일 전송 API 호출 중 에러 발생 : " + error.status);
        }
    });
    return value;
}

//보일 태그, 감출 태그, 값 초기화할 태그 설정 함수
function showTag(visible, invisible, initial) {
    for (let i in visible) {
        visible[i].show();
    }

    for (let i in invisible) {
        invisible[i].hide();
    }

    for (let i in initial) {
        initial[i].text("");
    }
}

//입력한 이메일이 변경되었을 때, 입력창 상태 초기화
$("#loginId").change(function () {
    const visible = [$("#findPassword"), $("#checkId")];
    const invisible = [$("#passwordDiv"), $("#loginCodeDiv"), $("#registerCodeDiv")];
    const initial = [$("#loginPw"), $("#loginCode"), $("#initializeCode"), $("#message")];
    showTag(visible, invisible, initial);
});

//비밀번호 찾기 이메일이 변경되었을 때, 입력창 상태 초기화
$("#findPasswordId").change(function () {
    const visible = [$("#initializePassword")];
    const invisible = [$("#initializeCodeDiv")];
    const initial = [$("#initializeCode"), $("#message")];
    showTag(visible, invisible, initial);
});

//카카오 로그인
$("#kakaoLogin").click(function () {
    location.href="https://kauth.kakao.com/oauth/authorize" +
        "?client_id=6d49b86907e3887ab27ba34050122bd1" +
        "&redirect_uri=http://localhost:8080/login/kakao" +
        "&response_type=code" +
        "&scope=profile_nickname,profile_image,account_email";
});

//비밀번호로 로그인 버튼 클릭 이벤트
$("#passwordBtn").click(function () {
    const loginId = $("#loginId").val();
    const loginPw = $("#loginPw").val();
    $.ajax({
        url: domainURL + "/login/password",
        method: "POST",
        contentType: "application/json",
        dataType: "text",
        data: JSON.stringify({"memId" : loginId, "memPw" : loginPw}),
        success: function(result) {
            if (result == "OK") {
                location.href = domainURL + "/return-uri";
            } else {
                $("#message").text(result);
            }
        },
        error: function(error) {
            alert("비밀번호로 로그인 중 에러 발생 : " + error.status);
        }
    });
});

//로그인 코드로 로그인 버튼 클릭 이벤트
$("#loginCodeBtn").click(function () {
    const loginId = $("#loginId").val();
    const loginCode = $("#loginCode").val();
    $.ajax({
        url: domainURL + "/login/login-code",
        method: "POST",
        contentType: "application/json",
        dataType: "text",
        data: JSON.stringify({"id" : loginId, "loginCode" : loginCode}),
        success: function(result) {
            if (result == "OK") {
                location.href = domainURL + "/return-uri";
            } else {
                $("#message").text(result);
            }
        },
        error: function(error) {
            alert("비밀번호로 로그인 중 에러 발생 : " + error.status);
        }
    });
});

//이메일로 회원가입 버튼 클릭 이벤트
$("#registerCodeBtn").click(function () {
    const loginId = $("#loginId").val();
    const registerCode = $("#registerCode").val();
    $.ajax({
        url: domainURL + "/login/register-code",
        method: "POST",
        contentType: "application/json",
        dataType: "text",
        data: JSON.stringify({"id" : loginId, "registerCode" : registerCode}),
        success: function(result) {
            if (result == "OK") {
                $("#loginForm").prop("action", domainURL + "/join/form");
                $("#loginForm").submit();
            } else {
                $("#message").text(result);
            }
        },
        error: function(error) {
            alert("비밀번호로 로그인 중 에러 발생 : " + error.status);
        }
    });
});

//로그인 입력창 버튼 클릭 이벤트
$("#loginBtn").click(function() {
    const visible = [$("#loginId"), $("#findPassword"), $("#checkId")];
    const invisible = [$("#findPasswordId"), $("#initializePassword"), $("#initializeCodeDiv"), $("#login")];
    const initial = [$("#findPasswordId"), $("#initializeCode"), $("#message")];
    showTag(visible, invisible, initial);
});

//비밀번호 찾기 버튼 클릭 이벤트
$("#findPasswordBtn").click(function() {
    const visible = [$("#findPasswordId"), $("#initializePassword"), $("#login")];
    const invisible = [$("#loginId"), $("#findPassword"), $("#checkId"), $("#registerCodeDiv"), $("#loginCodeDiv"), $("#passwordDiv")];
    const initial = [$("#loginId"), $("#loginPw"), $("#loginCode"), $("#registerCode"), $("#message")];
    showTag(visible, invisible, initial);
});

//비밀번호 초기화용 로그인 코드 이메일로 전송
$("#initializePasswordBtn").click(function() {
    const loginId = $("#findPasswordId").val();
    $.ajax({
        url: domainURL + "/login/check",
        method: "POST",
        contentType: "application/json",
        dataType: "text",
        data: JSON.stringify({"id" : loginId}),
        success: function(result) {
            if (result == "JOIN_LOGIN") {
                $("#message").text("이메일이 올바르지 않습니다.");
            } else if (result == "PASSWORD_LOGIN" || result == "CODE_LOGIN") {
                let value = sendCode(loginId);
                //이메일 전송 성공 시 [초기화 코드 보내기] 버튼 숨기고, 초기화 코드로 로그인 div 보이기
                $("#initializePassword").hide();
                $("#initializeCodeDiv").show();
                if (value != "") {
                    $("#initializePassword").show();
                    $("#initializeCodeDiv").hide();
                }
            } else {
                $("#message").text(result);
            }
        },
        error: function(error) {
            alert("아이디 체크 중 에러 발생 : " + error.status);
        }
    });
});

//비밀번호 초기화 코드로 로그인 버튼 클릭 이벤트
$("#initializeCodeBtn").click(function() {
    const loginId = $("#findPasswordId").val();
    const initializeCode = $("#initializeCode").val();
    $.ajax({
        url: domainURL + "/login/initialize-code",
        method: "POST",
        contentType: "application/json",
        dataType: "text",
        data: JSON.stringify({"id" : loginId, "initializeCode" : initializeCode}),
        success: function(result) {
            if (result == "OK") {
                location.href = domainURL + "/return-uri";
            } else {
                $("#message").text(result);
            }
        },
        error: function(error) {
            alert("비밀번호로 로그인 중 에러 발생 : " + error.status);
        }
    });
});

//입력창에서 Enter키 누르면 현재 보여지는 submit 버튼 클릭 이벤트 실행
$("input").keydown(function(key) {
    if (key.keyCode == 13) {
        const btnList = [$("#checkId"), $("#passwordDiv"), $("#loginCodeDiv"), $("#registerCodeDiv"), $("#initializeCodeDiv"), $("#initializePassword")];
        for (let i in btnList) {
            if (btnList[i].css("display") == "block") {
                if (btnList[i].is($("#checkId"))) {
                    $("#checkIdBtn").trigger("click");
                    return;
                } else if (btnList[i].is($("#initializePassword"))) {
                    $("#initializePasswordBtn").trigger("click");
                    return;
                } else {
                    btnList[i].children("button").trigger("click");
                    return;
                }
            }
        }
    }
});