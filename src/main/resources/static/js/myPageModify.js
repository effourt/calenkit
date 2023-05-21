//비밀번호 null이 아닐 경우 현재 비밀번호 입력 창 출력.
var passwordDiv = document.querySelector('.password');
var mPw = $('#mPw').val();
console.log("mPw=" + mPw);
if (mPw != null && mPw !== "") {
    passwordDiv.style.display = 'block';
}

function checkPw() {

    var memPw = $('#memPw').val(); //memId값이 "memId"인 입력란의 값을 저장
    var checkMemPw = $('#checkMemPw').val();//memId값이 "memId"인 입력란의 값을 저장

    $.ajax({
        url: '/mypage/check-pw', //Controller에서 요청 받을 주소
        type: 'get', //GET 방식으로 전달
        data: {
            memPw: memPw,
            checkMemPw: checkMemPw
        },
        success: function (cnt) { //컨트롤러에서 넘어온 cnt값을 받는다
            if (cnt == 0) { //cnt가 1이 아니면(=0일 경우) -> 일치하지 않는 아이디
                $('.pw_already').css("display", "inline-block");
                $('.pw_ok').css("display", "none");
                $('.updateButton').css("display", "none");
            }
            if (cnt == 1) { // cnt가 1일 경우 -> 일치하는 아이디
                $('.pw_ok').css("display", "inline-block");
                $('.pw_already').css("display", "none");
            }
        },
        error: function () {

        }
    });
};

function checkPassword() {

    var memPw = $('#memPw').val(); //memId값이 "memId"인 입력란의 값을 저장

    var password1 = $('#password1').val(); //memId값이 "memId"인 입력란의 값을 저장
    console.log(password1);

    var password2 = $('#password2').val(); //memId값이 "memId"인 입력란의 값을 저장
    console.log(password2);
    $.ajax({
        url: '/mypage/check-password', //Controller에서 요청 받을 주소
        type: 'get', //GET 방식으로 전달
        data: {
            password1: password1,
            password2: password2,
            memPw: memPw
        },
        success: function (cnt) { //컨트롤러에서 넘어온 cnt값을 받는다
            if (cnt == 0) { //cnt : 0 = 조건 미일치
                $('.password_already').css("display", "inline-block");
                $('.password_ok').css("display", "none");
                $('.updateButton').css("display", "none");

            }
            if (cnt == 1) { // cnt가 1일 경우 = 조건 일치, 아이디 미일치
                $('.password_ok').css("display", "inline-block");
                $('.password_already').css("display", "none");
                $('.updateButton').css("display", "inline-block");
            }

        },
        error: function () {

        }
    });
};

function checkPassword2() {
    var password1 = $('#password1').val(); //memId값이 "memId"인 입력란의 값을 저장
    console.log(password1);

    $.ajax({
        url: '/mypage/check-password2', //Controller에서 요청 받을 주소
        type: 'get', //GET 방식으로 전달
        data: {password1: password1},
        success: function (cnt) { //컨트롤러에서 넘어온 cnt값을 받는다
            if (cnt == 0) { //cnt : 0 = 조건 미일치
                $('.password_nonematch').css("display", "inline-block");
                $('.password_match').css("display", "none");
                $('.updateButton').css("display", "none");
            }

            if (cnt == 1) { // cnt가 1일 경우 = 조건 일치, 아이디 미일치
                $('.password_nonematch').css("display", "none");
                $('.password_match').css("display", "inline-block");
                $('.updateButton').css("display", "none");
            }
        },
        error: function () {

        }
    });
};