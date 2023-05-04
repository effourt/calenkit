//도메인 주소
const domainURL = "http://localhost:8080";

//프로필 이미지 검증
let profileImageConfirm = true;
$("#profileImage").change(function() {
    const profileImage = $("#profileImage").val();
    const extension = profileImage.substring(profileImage.lastIndexOf("."));
    if (profileImage != ""
        && extension != ".jpg"
        && extension != ".jpeg"
        && extension != ".png") {
        profileImageConfirm = false;
        $("#profileImageMessage").text("이미지 파일 형식에 맞춰 재등록해주세요.(jpg, jpeg, png)");
    } else {
        profileImageConfirm = true;
        $("#profileImageMessage").text("");
    }
});

//닉네임 검증
let nicknameConfirm = false;
const nicknameReg = /^[a-zA-Z가-힣]{2,10}$/;
$("#nickname").focusout(function() {
    if (!nicknameReg.test($("#nickname").val())) {
        nicknameConfirm = false;
        $("#nicknameMessage").text("2자리 이상 10자리 이하의 영문, 한글을 사용하세요.");
    } else {
        nicknameConfirm = true;
        $("#nicknameMessage").text("");
    }
});

//비밀번호 검증
let passwordConfirm = false;
const passwordReg = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/;
$("#loginPw").focusout(function() {
    if (!passwordReg.test($("#loginPw").val())) {
        passwordConfirm = false;
        $("#passwordMessage").text("8자리 이상 15자리 이하의 영문, 숫자, 특수문자를 사용하세요.");
    } else {
        passwordConfirm = true;
        $("#passwordMessage").text("");
    }
});

//비밀번호 확인 검증
let passwordCheckConfirm = false;
$("#loginPwCheck").focusout(function() {
    if ($("#loginPw").val() != $("#loginPwCheck").val()) {
        passwordCheckConfirm = false;
        $("#passwordCheckMessage").text("입력한 비밀번호와 같은 값을 입력해주세요.");
    } else {
        passwordCheckConfirm = true;
        $("#passwordCheckMessage").text("");
    }
});

//회원가입 후 로그인 버튼 클릭 이벤트
$("#registerBtn").click(function() {
    if (profileImageConfirm == false
        || nicknameConfirm == false
        || passwordConfirm == false
        || passwordCheckConfirm == false) {
        return;
    }

    const loginId = $("#loginId").val();
    const profileImage = $("#profileImage").val();
    const nickname = $("#nickname").val();
    const password = $("#loginPw").val();
    $.ajax({
        url: domainURL + "/join",
        method: "POST",
        contentType: "application/json",
        dataType: "text",
        data: JSON.stringify({
            "memId" : loginId,
            "memImage" : profileImage,
            "memName" : nickname,
            "memPw" : password}),
        success: function(result) {
            if (result == "OK") {
                location.href = domainURL + "/";
            } else {
                $("#message").val(result);
            }
        },
        error: function(error) {
            alert("비밀번호로 로그인 중 에러 발생 : " + error.status);
        }
    });
});