package com.web.mapper;

import com.web.dto.request.ChatRequest;
import com.web.entity.Chat;
import com.web.utils.UserUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    @Autowired
    private ModelMapper mapper;

    public Chat chatRequestToChat(ChatRequest request){
        Chat chat = mapper.map(request, Chat.class);
        return chat;
    }
}
