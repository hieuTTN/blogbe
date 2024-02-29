package com.web.service;

import com.web.dto.request.ChatRequest;
import com.web.entity.Chat;
import com.web.entity.User;
import com.web.exception.MessageException;
import com.web.mapper.ChatMapper;
import com.web.repository.ChatRepository;
import com.web.repository.UserRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    ChatMapper chatMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserUtils userUtils;

    public Chat save(ChatRequest request){
        Optional<User> user = userRepository.findById(request.getReceiverId());
        if(user.isEmpty()){
            throw new MessageException("Người nhận không tồn tại");
        }
        Chat chat = chatMapper.chatRequestToChat(request);
        chat.setReceiver(user.get());
        chat.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        chat.setSender(userUtils.getUserWithAuthority());
        chatRepository.save(chat);
        return chat;
    }

    public void delete(Long idChat){
        Optional<Chat> chat = chatRepository.findById(idChat);
        if(chat.isEmpty()){
            throw new MessageException("not found");
        }
        if(chat.get().getSender().getId() != userUtils.getUserWithAuthority().getId()){
            throw new MessageException("403 access denied");
        }
        chatRepository.deleteById(idChat);
    }

    public List<Chat> findByUser(Long idReceiver){
        Long me = userUtils.getUserWithAuthority().getId();
        List<Chat> list = chatRepository.findByUser(me, idReceiver);
        return list;
    }

    public List<User> listUserChated(){
        return userRepository.listUserChated(userUtils.getUserWithAuthority().getId());
    }
}
