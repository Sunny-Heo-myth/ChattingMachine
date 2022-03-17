<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <meta charset="UTF-8">
    <title>Room</title>
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
        .roomContainer{
            background-color: #F6F6F6;
            width: 500px;
            height: 500px;
            overflow: auto;
        }
        .roomList{
            border: none;
        }
        .roomList th{
            border: 1px solid #FFBB00;
            background-color: #fff;
            color: #FFBB00;
        }
        .roomList td{
            border: 1px solid #FFBB00;
            background-color: #fff;
            text-align: left;
            color: #FFBB00;
        }
        .roomList .roomId{
            width: 75px;
            text-align: center;
        }
        .roomList .roomName{
            width: 350px;
        }
        .roomList .go{
            width: 71px;
            text-align: center;
        }
        button{
            background-color: #FFBB00;
            font-size: 14px;
            color: #000;
            border: 1px solid #000;
            border-radius: 5px;
            padding: 3px;
            margin: 3px;
        }
        .inputTable th{
            padding: 5px;
        }
        .inputTable input{
            width: 330px;
            height: 25px;
        }
    </style>
</head>

<body>
<div class="container">
    <h1>rooms</h1>
    <div id="roomContainer" class="roomContainer">
        <table id="roomList" class="roomList"></table>
    </div>
    <div>
        <table class="inputTable">
            <tr>
                <th>room name</th>
                <th><input type="text" name="roomName" id="roomName"></th>
                <th><button id="createRoom">create room</button></th>
            </tr>
        </table>
    </div>
</div>
</body>

<script type="text/javascript">
    var ws;
    window.onload = function(){
        getRoom();
        createRoom();
    }

    function commonAjax(url, parameter, type, callback, contentType){
        // send ajax request to url with branch response
        $.ajax({
            url: url,
            data: parameter,
            type: type,
            contentType : contentType != null ? contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
            success: function (res) {
                callback(res);
            },
            error : function(err){
                console.log('ajax error');
                callback(err);
            }
        });
    }

    function getRoom(){
        commonAjax('/getRoom', "", 'post', function(result){
            createRoomList(result);
        });
    }

    function createRoom(){
        $("#createRoom").click(function(){
            var msg = {	roomName : $('#roomName').val() };

            commonAjax('/createRoom', msg, 'post', function(result){
                createRoomList(result);
            });

            $("#roomName").val("");
        });
    }

    function goChat(roomId, roomName){
        location.href="/goChat?roomId=" + roomId + "&"+"roomName=" + roomName;
    }

    function createRoomList(res){
        if(res != null){
            var tag = "<tr><th class='roomNumber'>room number</th>" +
                "<th class='roomName'>room name</th><th class='go'></th></tr>";
            res.forEach(function (data, index) {
                var roomName = data.roomName.trim();
                var roomId = "room" + (index + 1);
                tag += "<tr>" +
                    "<td class='roomNumber'>" + (index + 1) + "</td>" +
                    "<td class='roomName'>" + roomName + "</td>" +
                    "<td class='go'><button type='button' onclick='goChat(\"" +
                    roomId + "\", \"" + roomName + "\")'>join</button></td>" +
                    "</tr>";
            });
            $("#roomList").empty().append(tag);
        }
    }
</script>
</html>
