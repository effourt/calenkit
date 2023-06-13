$(window).on('load', function() {
    // url에서 keyword를 가져오기 위한 코드
    var urlParams = new URLSearchParams(window.location.search);
    var keyword = urlParams.get('keyword');
    console.log(keyword);
    // Ajax를 이용해 memberList 데이  터를 가져온 후 렌더링하는 코드
    $.ajax({
        url: "/admin/list",
        data:{keyword:keyword},
        success: function (data) {
            // 가져온 member 데이터를 통해 tbody 내부에 새로운 tr을 추가하는 코드
            $.each(data, function (index, member) {
                var status = "";
                if (member.memStatus == 0) {
                    status = "<select ><option value='0' selected>탈퇴회원</option><option value='1'>일반회원</option><option value='2'>휴먼회원</option><option value='9'>관리자</option></select>";
                } else if (member.memStatus == 1) {
                    status = "<select ><option value='0'>탈퇴회원</option><option value='1' selected>일반회원</option><option value='2'>휴먼회원</option><option value='9'>관리자</option></select>";
                } else if (member.memStatus == 2) {
                    status = "<select ><option value='0'>탈퇴회원</option><option value='1'>일반회원</option><option value='2' selected>휴먼회원</option><option value='9'>관리자</option></select>";
                } else if (member.memStatus == 9) {
                    status = "<select ><option value='0'>탈퇴회원</option><option value='1'>일반회원</option><option value='2'>휴먼회원</option><option value='9' selected>관리자</option></select>";
                }


                var checkboxValue = member.memId;
                var row = "<tr>" +
                    "<td style='text-align: center'><input type='checkbox' value='" + checkboxValue + "'></td>" +
                    "<td>" + member.memId + "</td>" +
                    "<td>" + member.memName + "</td>" +
                    "<td>" + member.memLogin + "</td>" +
                    "<td>" + status + "</td>" +
                    "</tr>";

                var $row = $(row);
                var $select = $row.find('select');
                $select.on('change', function () {
                    var $row = $(this).closest('tr'); // 선택 상자가 포함 된 행을 찾음
                    var selectedValue = $(this).val();
                    var memId = $row.find('td:eq(1)').text(); // 두 번째 열(td:eq(1))의 텍스트(memId)를 찾음
                    handleSelectChange(selectedValue, memId);
                });


                $("#memberTableBody").append($row);
            });
        }
    });
})
// "all_checkbox" 체크박스 클릭 이벤트 처리
$('#all_checkbox').click(function () {
    // 만약 "all_checkbox" 체크박스가 체크되어 있다면
    if ($(this).is(':checked')) {
        // 모든 체크박스를 선택
        $('input[type="checkbox"]').prop('checked', true);
    } else {
        // 그렇지 않다면 모든 체크박스를 선택 해제
        $('input[type="checkbox"]').prop('checked', false);
    }
});


//호스트정보 삭제
function remove() {
    var count = $("input[type='checkbox']:checked").length;
    var removeArray = new Array();
    $("input[type='checkbox']:checked").each(function () {
        removeArray.push($(this).val());
    });
    if (count == 0) {
        alert("선택된 회원이 없습니다.");
    } else {
        if (confirm("회원 정보를 삭제 하시겠습니까?")) {
            $.ajax({
                type: "DELETE",
                url: "/admin/delete",
                data: {memIdList: JSON.stringify(removeArray)},
                dataType: "text",
                success: function (response) {
                    alert("회원정보가 삭제되었습니다.")
                    location.reload();
                },
                error: function (xhr) {
                    alert("에러코드(회원 정보 삭제) = " + xhr.status);
                }
            });
        }
    }
}



function handleSelectChange(selectedValue, memId) {
    console.log("Selected value: " + selectedValue + ", memId: " + memId);
    selectedValue = parseInt(selectedValue);
    // AJAX로 데이터 업데이트
    $.ajax({
        url: "/admin/update",
        type: "patch",
        data: {
            memId: memId,
            selectedValue: selectedValue
        },
        success: function (response) {
            alert("회원정보가 변경되었습니다.")
            location.reload();
        },
        error: function (xhr, status, error) {
            // 업데이트가 실패했을 때 실행되는 코드
            console.log("Error: " + error);
        }
    });
}


