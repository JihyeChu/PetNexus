package com.sparta.petnexus.chat.repository;

import com.sparta.petnexus.chat.dto.ChatMessageDto;
import com.sparta.petnexus.chat.dto.ChatRoomRequestDto;
import com.sparta.petnexus.chat.entity.ChatRoom;
import com.sparta.petnexus.chat.entity.ChatType;
import com.sparta.petnexus.common.redis.pubsub.RedisPublisher;
import com.sparta.petnexus.common.redis.pubsub.RedisSubscriber;
import com.sparta.petnexus.user.entity.User;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;


@Log4j2
@RequiredArgsConstructor
@Repository
public class ChatRoomRedisRepository {

    // Redis CacheKeys
    private static final String CHAT_ROOMS = "CHAT_ROOM"; // 채팅룸 저장
    private final RedisPublisher redisPublisher;
    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;
    // Redis
    private final RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    // 채팅방(topic)에 발행되는 메시지를 처리할 Listener
    // topic 에 메시지 발행을 기다리는 Listener
    private final RedisMessageListenerContainer redisMessageListener;
    // topic 이름으로 topic 정보를 가져와 메시지를 발송할 수 있도록 Map 에 저장
    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보
    // 서버별로 채팅방에 매치되는 topic 정보를 Map 에 넣어 roomId로 찾을수 있도록 한다.
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        // topic 정보를 담을 Map 을 초기화
        topics = new HashMap<>();
    }

    // 모든 채팅방 조회
    public List<ChatRoom> findAllRoom() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    // 특정 채팅방 조회
    public ChatRoom findRoomById(String roomId) {
        return opsHashChatRoom.get(CHAT_ROOMS, roomId);
    }

    // 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash 에 저장한다.
    public void createChatRoom(ChatRoomRequestDto requestDto, User user) {
        ChatRoom chatRoom = requestDto.toEntity(user);
        String roomId = chatRoom.getId();

        opsHashChatRoom.put(CHAT_ROOMS, roomId, chatRoom);

        // 신규 Topic 을 생성하고 Listener 등록 및 Topic Map 에 저장
        ChannelTopic topic = new ChannelTopic(roomId);
        redisMessageListener.addMessageListener(redisSubscriber, topic);
        topics.put(roomId, topic);
    }

    // 채팅방 입장 : redis 에 topic 을 만들고 pub/sub 통신을 하기 위해 리스너를 설정
    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null) {
            topic = new ChannelTopic(roomId);
        }
        redisMessageListener.addMessageListener(redisSubscriber, topic);
        topics.put(roomId, topic);
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

    // 특정 Topic 에 메시지 발행
    public void pushMessage(String roomId, ChatMessageDto messageDto) {
        ChannelTopic topic = topics.get(roomId);
        redisPublisher.publish(topic,
            ChatMessageDto.builder()
                .sender(messageDto.getSender())
                .roomId(roomId)
                .message(messageDto.getMessage())
                .type(ChatType.TALK)
                .build());
    }

    // Topic 삭제 후 Listener 해제, Topic Map 에서 삭제
    public void deleteRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        redisMessageListener.removeMessageListener(redisSubscriber, topic);
        topics.remove(roomId);
    }
}
