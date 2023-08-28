package com.sparta.petnexus.chat.controller;

import com.sparta.petnexus.chat.dto.ChatRoomListResponseDto;
import org.springframework.ui.Model;
import com.sparta.petnexus.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ChatRoomViewController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/openChat")
    public String openChatList(Model model){
        ChatRoomListResponseDto chatRoomListResponseDto = chatRoomService.getOpenChatRooms();
        model.addAttribute("chatRoomList",chatRoomListResponseDto);
        return "openChat";
    }

    @GetMapping("/openChat/room")
    public String openChatList(@RequestParam Long chatId){
        return "websoket";
    }
}
