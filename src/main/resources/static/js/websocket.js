function receiveAlarm() { //웹소켓 수신
    let socket = new SockJS('/alarm');
    socket.onopen = function() {
        console.log('info: connection opened.');
    };
    socket.onmessage = function(event) {
        let data = event.data;
        console.log("ReceiveMessage : " + data + "\n");
        // 모달 알림
        toastr.success(data);
        //alert(data);
        //console.log(data);
    };
    socket.onclose = function() {
        console.log('connect close');
    };
    socket.onerror = function (err) {console.log('Errors : ' , err);};
}
function sendAlarm(teamSno, teamMid, alCate) {
    let socket = new SockJS('/alarm');
    socket.onopen = function() {
        console.log('info: connection opened.');
        const message = {
            "scNo":teamSno,
            "idList":[teamMid],
            "alCate":alCate
        };
        socket.send(JSON.stringify(message));
    };
    socket.onclose = function() {
        console.log('connect close');
    };
    socket.onerror = function (err) {console.log('Errors : ' , err);};
}


function receiveSchedule() { //웹소켓 수신
    let socket = new SockJS('/schedule');
    socket.onopen = function() {
        console.log('info: connection opened.');
    };
    socket.onmessage = function(event) {
        let data = event.data;
        console.log("ReceiveMessage : " + data + "\n");
        // 모달 알림
        alert(data);
        console.log(data);
    };
    socket.onclose = function() {
        console.log('connect close');
    };
    socket.onerror = function (err) {console.log('Errors : ' , err);};
}
function sendSchedule(teamSno,teamMid,html) {
    let socket = new SockJS('/schedule');
    socket.onopen = function() {
        console.log('info: connection opened.');
        socket.send(html);
    };
    const message = {
        "scNo":teamSno,
        "idList":[teamMid],
        "html":html
    };
    socket.onclose = function() {
        console.log('connect close');
    };
    socket.onerror = function (err) {console.log('Errors : ' , err);};
}