<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <meta charset="UTF-8">
    <title>Chat</title>
    <style>
        *{
            margin:0;
            padding:0;
        }
        .container{
            width: 500px;
            margin: 0 auto;
            padding: 25px
        }
        .container h1{
            text-align: left;
            padding: 5px 5px 5px 15px;
            color: #FFBB00;
            border-left: 3px solid #FFBB00;
            margin-bottom: 20px;
        }
        .chattingLine{
            background-color: #000;
            width: 500px;
            height: 500px;
            overflow: auto;
        }
        .chattingLine .me{
            color: #F6F6F6;
            text-align: right;
        }
        .chattingLine .others{
            color: #FFE400;
            text-align: left;
        }
        input{
            width: 330px;
            height: 25px;
        }
        #yourMsg{
            display: none;
        }
        .msgImg{
            width: 200px;
            height: 125px;
        }
        .clearBoth{
            clear: both;
        }
        .img{
            float: right;
        }
    </style>
</head>

<body>
<div id="container" class="container">
    <h1>${roomName}</h1>

    <input type="hidden" id="sessionId" value="">
    <input type="hidden" id="roomId" value="${roomId}">

    <div id="chattingLine" class="chattingLine">
    </div>

    <div id="yourName">
        <table class="inputTable">
            <tr>
                <th>name</th>
                <th><input type="text" name="userName" id="userName"></th>
                <th><button onclick="chatName()" id="startBtn">register</button></th>
            </tr>
        </table>
    </div>

    <div id="yourMsg">
        <table class="inputTable">
            <tr>
                <th>message</th>
                <th><input id="chatting" placeholder="Insert Message."></th>
                <th><button onclick="send()" id="sendButton">send</button></th>
            </tr>
<%--            <tr>--%>
<%--                <th>upload file</th>--%>
<%--                <th><input type="file" id="fileUpload"></th>--%>
<%--                <th height="60px"><button onclick="fileSend()" id="sendFileButton">upload</button></th>--%>
<%--            </tr>--%>
        </table>
    </div>
</div>
</body>

<script type="text/javascript">
    var webSocket;

    // send WebSocket with current room number (there will be n sockets with n rooms.)
    function webSocketOpen(){
        webSocket = new WebSocket("ws://" + location.host + "/chatting/" + $("#roomId").val());
        webSocketEvent();
    }

    function webSocketEvent() {
        // socket opened.
        webSocket.onopen = function(data){
        }

        // message received.
        webSocket.onmessage = function(data) {
            var message = data.data;
            // text message received
            if(message != null && message.type !== ''){
                var parsedMessage = JSON.parse(message)
                // setting #sessionId
                if (parsedMessage.type === "getId") {     // when type of json is get(Session)Id,
                    var sessionId = parsedMessage.sessionId != null ? parsedMessage.sessionId : "";
                    if (sessionId !== "") {
                        $("#sessionId").val(sessionId);
                    }
                }
                // represent message
                else if (parsedMessage.type === "message") {      // when type of json is message,
                    if (parsedMessage.sessionId === $("#sessionId").val()) {
                        $("#chattingLine").append("<p class='me'>me: "
                            + parsedMessage.message + "</p>");
                    }
                    else {
                        $("#chattingLine").append("<p class='others'>" + parsedMessage.userName + ": "
                            + parsedMessage.message + "</p>");
                    }
                }

                else {      // when it is unknown type
                    console.warn("unknown type!")
                }
            }
            // binary message received
            // else if (message != null) {
            //     var url = URL.createObjectURL(new Blob([message]));
            //     if (parsedMessage.sessionId === $("#sessionId").val()) {
            //         $("#chattingLine").append("<div class='me'>me : " +
            //             "<img class='msgImg' src=" + url + "></div><div class='clearBoth'></div>");
            //     }
            //     else {
            //         $("#chattingLine").append("<div class='others'>" + parsedMessage.userName +
            //             ": <img class='msgImg' src=" + url + "></div><div class='clearBoth'></div>");
            //     }
            // }
        }

        document.addEventListener("keypress", function(e){
            if(e.keyCode === 13){       // when "ENTER" pressed.
                send();
            }
        });
    }

    function chatName() {
        var userName = $("#userName").val();
        if (userName == null || userName.trim() === "") {
            alert("Insert user name.");
            $("#userName").focus();
        } else {
            // Open websocket if there is name of user.
            webSocketOpen();
            $("#yourName").hide();
            $("#yourMsg").show();
        }
    }

    function send() {
        var object = {
            type : "message",
            roomId : $("#roomId").val(),
            sessionId: $("#sessionId").val(),
            userName : $("#userName").val(),
            message : $("#chatting").val()
        }
        webSocket.send(JSON.stringify(object))

        $('#chatting').val("");
    }

    // function fileSend() {
    //     var file = document.querySelector("#fileUpload").files[0];
    //     var fileReader = new FileReader();
    //     fileReader.onload = function () {
    //         var param = {
    //             type: "fileUpload",
    //             file: file,
    //             roomId : $("#roomId").val(),
    //             sessionId: $("#sessionId").val(),
    //             userName: $("#userName").val(),
    //             message: $("#chatting").val()
    //         }
    //         webSocket.send(JSON.stringify(param));
    //
    //         var arrayBuffer = this.result;
    //         webSocket.send(arrayBuffer);
    //     };
    //     fileReader.readAsArrayBuffer(file);
    // }
</script>
</html>