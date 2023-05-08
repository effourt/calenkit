//도메인 주소
const domainURL = "http://localhost:8080";

//프로필 이미지 검증
let profileImageConfirm = true;
$("#profileImage").change(function(event) {
    const profileImage = $("#profileImage").val();
    const extension = profileImage.substring(profileImage.lastIndexOf("."));

    //이미지 미리보기 설정
    let file = event.target.files[0];
    let reader = new FileReader();
    reader.onload = function(e) {
        $("#imageContainer").prop("src", e.target.result);
    }
    reader.readAsDataURL(file);

    //선택한 이미지가 없으면 기본 메시지 출력, 있으면 선택한 이미지의 경로를 제외하고 이름만 출력하도록 설정
    if (profileImage == "") {
        $("#profileImageName").text("사진 추가");
    } else {
        $("#profileImageName").text(profileImage.substring(profileImage.lastIndexOf("\\") + 1));
    }
    if (profileImage != ""
        && extension != ".jpg"
        && extension != ".jpeg"
        && extension != ".png") {
        profileImageConfirm = false;
        $("#message").text("이미지 파일 형식에 맞춰 재등록해주세요.(jpg, jpeg, png)");
    } else {
        profileImageConfirm = true;
        $("#message").text("");
    }
});

//닉네임 검증
let nicknameConfirm = false;
const nicknameReg = /^[a-zA-Z가-힣]{2,10}$/;
$("#nickname").focusout(function() {
    if (!nicknameReg.test($("#nickname").val())) {
        nicknameConfirm = false;
        $("#message").text("2자리 이상 10자리 이하의 영문, 한글을 사용하세요.");
    } else {
        nicknameConfirm = true;
        $("#message").text("");
    }
});

//비밀번호 검증
let passwordConfirm = false;
const passwordReg = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/;
$("#loginPw").focusout(function() {
    if (!passwordReg.test($("#loginPw").val())) {
        passwordConfirm = false;
        $("#message").text("8자리 이상 15자리 이하의 영문, 숫자, 특수문자를 사용하세요.");
    } else {
        passwordConfirm = true;
        $("#message").text("");
    }
});

//비밀번호 확인 검증
let passwordCheckConfirm = false;
$("#loginPwCheck").focusout(function() {
    if ($("#loginPw").val() != $("#loginPwCheck").val()) {
        passwordCheckConfirm = false;
        $("#message").text("입력한 비밀번호와 같은 값을 입력해주세요.");
    } else {
        passwordCheckConfirm = true;
        $("#message").text("");
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
    const profileImage = $("#profileImage")[0].files[0];
    const nickname = $("#nickname").val();
    const password = $("#loginPw").val();

    let form = new FormData();
    const data = {
        "memId" : loginId,
        "profileImage" : profileImage,
        "memName" : nickname,
        "memPw" : password
    }
    form.append("member", new Blob([JSON.stringify(data)], {type : "application/json"}));
    form.append("profileImage", profileImage);
    $.ajax({
        url: domainURL + "/join",
        method: "POST",
        contentType: false,
        processData: false,
        dataType: "text",
        data: form,
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

//입력창에서 Enter키 입력 시 submit 버튼 클릭 이벤트 실행
$("input").keydown(function(key) {
    if (key.keyCode == 13) {
        $("#registerBtn").trigger("click");
    }
});