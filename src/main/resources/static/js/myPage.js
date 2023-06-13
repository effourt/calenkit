$(document).ready(function () {
    $('#memImage').change(function () {
        $('#files_send').click();
    });
});

//유효성 검사
$(document).on('change', '#memImage', function () {
    var input = this;
    var url = $(this).val();
    var ext = url.substring(url.lastIndexOf('.') + 1).toLowerCase();
    if (input.files && input.files[0] && (ext == "gif" || ext == "png" || ext == "jpeg" || ext == "jpg")) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#image-preview').attr('src', e.target.result);
            $('#image-preview2').attr('src', e.target.result);

            $('#image-preview').show();
            $('#image-preview2').show();
        }

        reader.readAsDataURL(input.files[0]);
    } else {
        $('#image-preview').attr('src', '#');
        $('#image-preview').hide();
        $('#image-preview2').attr('src', '#');
        $('#image-preview2').hide();
        alert("올바른 이미지 파일을 선택해주세요. (gif, png, jpg, jpeg 형식만 가능)");
        $(this).val('');
    }
});

//파일 미리보기 + 저장
$(document).on('click', '#files_send', function () {
    var formData = new FormData();

    for (var i = 0; i < $('#memImage')[0].files.length; i++) {
        formData.append('memImage', $('#memImage')[0].files[i]);
    }

    $.ajax({
        url: '/mypage/modify-image',
        data: formData,
        processData: false,
        contentType: false,
        type: 'POST',
        success: function (data) {
            alert("이미지 업로드 성공");
        }
    });
});

//삭제 팝업창
function openDeletePopup() {
    var url = "/mypage/delete";
    var name = "mypageDeletePopup";
    var width = 700;
    var height = 400;
    var left = (screen.width / 2) - (width / 2);
    var top = (screen.height / 2) - (height / 2);
    var specs = "width=" + width + ",height=" + height + ",left=" + left + ",top=" + top + ",resizable=yes";
    window.open(url, name, specs);
}


//비밀번호 변경 팝업창
function openPwModifyPopup() {
    var url = "/mypage/modify-pw";
    var name = "mypagepwModifyPopup";
    var width = 700;
    var height = 400;
    var left = (screen.width / 2) - (width / 2);
    var top = (screen.height / 2) - (height / 2);
    var specs = "width=" + width + ",height=" + height + ",left=" + left + ",top=" + top + ",resizable=yes";
    window.open(url, name, specs);
}


//닉네임 비동기
function checkName() {
    var memName = $('#memName').val(); // id 값이 "memName"인 입력란의 값을 저장
    $.ajax({
        url: '/mypage/check-name', // Controller에서 요청 받을 주소
        type: 'get', // GET 방식으로 전달
        data: {memName: memName},
        success: function (cnt) { // Controller에서 넘어온 cnt 값을 받는다
            if (cnt == 0) { // cnt가 0일 경우 -> 중복된 아이디
                $('.name_ok').css("display", "inline-block");
                $('.name_already').css("display", "none");
                $('.name_not-ready').css("display", "none");
            }
            if (cnt == 1) { // cnt가 1일 경우 -> 사용 가능한 아이디
                $('.name_already').css("display", "inline-block");
                $('.name_ok').css("display", "none");
                $('.name_not-ready').css("display", "none");
            }
            if (cnt == 2) { //형식에 맞지 않는 아이디
                $('.name_already').css("display", "none");
                $('.name_ok').css("display", "none");
                $('.name_not-ready').css("display", "inline-block");

            }
        },
        error: function () {

        }
    });
}