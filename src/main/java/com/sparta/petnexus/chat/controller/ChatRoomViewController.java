package com.sparta.petnexus.chat.controller;

import com.sparta.petnexus.chat.dto.ChatRoomListResponseDto;
import org.springframework.ui.Model;
import com.sparta.petnexus.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ChatRoomViewController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/openChat")
    public String openChatList(Model model){
        ChatRoomListResponseDto chatRoomListResponseDto = chatRoomService.getOpenChatRooms();
        model.addAttribute("chatRoom",chatRoomListResponseDto);
        return "openChat";
    }


}
