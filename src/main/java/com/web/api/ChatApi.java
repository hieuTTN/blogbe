package com.web.api;

import com.web.dto.request.ChatRequest;
import com.web.entity.Chat;
import com.web.entity.User;
import com.web.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatApi {

    @Autowired
    private ChatService chatService;

    @PostMapping("/all/savechat")
    public Chat save(@RequestBody ChatRequest request){
        Chat chat = chatService.save(request);
        return chat;
    }

    @DeleteMapping("/all/deletechat")
    public void delete(@RequestParam Long idChat){
        chatService.delete(idChat);
    }

    @GetMapping("/all/getchat")
    public List<Chat> getChat(@RequestParam Long idReceiver){
        return chatService.findByUser(idReceiver);
    }

    @GetMapping("/all/getuser")
    public List<User> getUser(){
        return chatService.listUserChated();
    }
}
