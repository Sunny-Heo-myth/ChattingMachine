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
        .chattingBox{
            background-color: #000;
            width: 500px;
            height: 500px;
            overflow: auto;
        }
        .chattingBox .me{
            color: #F6F6F6;
            text-align: right;
        }
        .chattingBox .others{
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
    </style>
</head>

<body>
<div id="container" class="container">
    <h1>${roomName}</h1>

    <input type="hidden" id="sessionId" value="">
    <input type="hidden" id="roomId" value="${roomId}">

    <div id="chattingBox" class="chattingBox">
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
                <th><button onclick="send()" id="sendBtn">send</button></th>
            </tr>
        </table>
    </div>
</div>
</body>

<script type="text/javascript">
    var webSocket;

    // send WebSocket with current room number
    function webSocketOpen(){
        webSocket = new WebSocket("ws://" + location.host + "/chatting/" + $("#roomId").val());
        webSocketEvent();
    }

    function webSocketEvent() {
        // called when socket opened.
        webSocket.onopen = function(data){
        }

        // called when message is received.
        webSocket.onmessage = function(data) {
            var message = data.data;
            if(message != null && message.trim() !== ''){
                var parsedMessage = JSON.parse(message)

                if (parsedMessage.type === "getId") {     // when json's type is get(Session)Id,
                    var sessionId = parsedMessage.sessionId != null ? parsedMessage.sessionId : "";
                    if (sessionId !== "") {
                        $("#sessionId").val(sessionId);
                    }
                }

                else if (parsedMessage.type === "message") {      // when json's type is message,
                    if (parsedMessage.sessionId === $("#sessionId").val()) {
                        $("#chattingBox").append("<p class='me'>me : "
                            + parsedMessage.message + "</p>");
                    }
                    else {
                        $("#chattingBox").append("<p class='others'>" + parsedMessage.userName + " : "
                            + parsedMessage.message + "</p>");
                    }
                }

                else {
                    console.warn("unknown type!")
                }
                // $("#chattingBox").append("<p>" + message + "</p>");
            }
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
        // var userName = $("#userName").val();
        // var message = $("#chatting").val();
        // webSocket.send(userName+" : "+message);

        $('#chatting').val("");
    }
</script>
</html>