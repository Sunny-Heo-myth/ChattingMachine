package com.sunny.chattingmachine.controller;

import com.sunny.chattingmachine.domain.Room;
import org.apache.commons.lang3.StringUtils;
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

    // todo private
    // todo do not leave field variable in singleton object instead save in db or file
    private List<Room> roomList = new ArrayList<>();
    // todo db table id
    private static int roomNumber = 0;

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

    // todo hashmap<> model define
    // todo spring boot message converter (from where to where)
    @RequestMapping("/createRoom")
    @ResponseBody
    public List<Room> createRoom(@RequestParam HashMap<Object, Object> reqParams) {
        // which is in url comes after ?
        String roomName = (String) reqParams.get("roomName");
        if (StringUtils.isBlank(roomName)) {
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
