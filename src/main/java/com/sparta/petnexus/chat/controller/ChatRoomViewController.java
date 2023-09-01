package com.sparta.petnexus.chat.controller;

import com.sparta.petnexus.chat.dto.ChatListResponseDto;
import com.sparta.petnexus.chat.dto.ChatMessageDto;
import com.sparta.petnexus.chat.dto.ChatRoomListResponseDto;
import com.sparta.petnexus.chat.service.ChatService;
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
    private final ChatService chatService;

    // 채팅방 목록
    @GetMapping("/openchat")
    public String openChatList(Model model){
        ChatRoomListResponseDto chatRoomListResponseDto = chatRoomService.getOpenChatRooms();
        model.addAttribute("chatRoomList",chatRoomListResponseDto);
        return "openChat";
    }

    // 채팅방
    @GetMapping("/openchat/room")
    public String openChatList(@RequestParam Long chatId, Model model){
        ChatListResponseDto chatListResponseDto = chatService.getAllChatByRoomId(chatId);
        model.addAttribute("chatList",chatListResponseDto);
        return "openChatRoom";
        //return "websoket";
    }
}
