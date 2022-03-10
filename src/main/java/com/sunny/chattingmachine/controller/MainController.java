package com.sunny.chattingmachine.controller;

import com.sunny.chattingmachine.VO.Room;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class MainController {

    List<Room> roomList = new ArrayList<>();
    static int roomNumber = 0;

    @RequestMapping("/chat")
    public ModelAndView chat(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("chat");
        return modelAndView;
    }

    @RequestMapping("/room")
    public ModelAndView room() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("room");
        return modelAndView;
    }

    @RequestMapping("/createRoom")
    @ResponseBody
    public List<Room> createRoom(@RequestParam HashMap<Object, Object> reqParams) {
        String roomName = (String) reqParams.get("roomName");
        if (roomName != null && !roomName.trim().equals("")) {
            Room room = new Room();
            room.setRoomId("room" + ++roomNumber);
            room.setRoomName(roomName);
            roomList.add(room);
        }
        return roomList;
    }

    @RequestMapping("/getRoom")
    @ResponseBody
    public List<Room> getRoom(@RequestParam HashMap<Object, Object> reqParams) {
        return roomList;
    }

    @RequestMapping("/goChat")
    public ModelAndView goChat(@RequestParam HashMap<Object, Object> reqParams) {
        ModelAndView modelAndView = new ModelAndView();
        String roomId = (String) reqParams.get("roomId");

        List<Room> tempRoomList = this.roomList.stream()
                .filter(o -> Objects.equals(o.getRoomId(), "room" + roomNumber))
                .collect(Collectors.toList());

        if (tempRoomList.size() > 0) {
            modelAndView.addObject("roomId", reqParams.get("roomId"));
            modelAndView.addObject("roomName", reqParams.get("roomName"));
            modelAndView.setViewName("chat");
        }
        else {
            modelAndView.setViewName("room");
        }
        return modelAndView;
    }
}
