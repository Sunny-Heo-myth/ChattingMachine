<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <meta charset="UTF-8">
    <title>Chatting</title>
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
    <h1>CHAT</h1>

    <input type="hidden" id="sessionId" value="">

    <div id="chattingBox" class="chattingBox">
    </div>

    <div id="yourName">
        <table class="inputTable">
            <tr>
                <th>name</th>
                <th><input type="text" name="userName" id="userName"></th>
                <th><button onclick="chatName()" id="startBtn">register name</button></th>
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

    function webSocketOpen(){
        webSocket = new WebSocket("ws://" + location.host + "/chatting");
        webSocketEvent();
    }

    function webSocketEvent() {
        // activated when socket is opened.
        webSocket.onopen = function(data){

        }

        // activated when message is received.
        webSocket.onmessage = function(data) {
            var message = data.data;
            if(message != null && message.trim() !== ''){
                var messageData = JSON.parse(message)
                if (messageData.type === "getId") {
                    var sessionId = messageData.sessionId;
                    if (sessionId !== '') {
                        $("#sessionId").val(sessionId);
                    }
                }
                else if (messageData.type === "message") {
                    if (messageData.sessionId === $("#sessionId").val()) {
                        $("#chattingBox").append("<p class='me'>me : " + messageData.message + "</p>");
                    }
                    else {
                        $("#chattingBox").append("<p class='others'>"
                            + messageData.userName + " : " + messageData.message + "</p>");
                    }
                }
                else {
                    console.warn("unknown type!")
                }
                // $("#chattingBox").append("<p>" + message + "</p>");
            }
        }

        document.addEventListener("keypress", function(e){
            if(e.keyCode === 13){ //enter press
                send();
            }
        });
    }

    function chatName(){
        var userName = $("#userName").val();
        if(userName == null || userName.trim() === ""){
            alert("Insert user name.");
            $("#userName").focus();
        }else{
            webSocketOpen();
            $("#yourName").hide();
            $("#yourMsg").show();
        }
    }

    function send() {
        var object = {
            type: "message",
            sessionId : $("#sessionId").val(),
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