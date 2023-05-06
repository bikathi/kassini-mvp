package com.hobbyzhub.chatservice.service;

import com.hobbyzhub.chatservice.entity.ChatModel;
import com.hobbyzhub.chatservice.repository.ChatModelRepository;
import com.mongodb.client.model.geojson.LineString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatModelService {
    @Autowired
    ChatModelRepository chatModelRepository;

    @Autowired
    MongoTemplate chatModelTemplate;

    public void createNewChatModel(ChatModel newChatModel) {
        chatModelRepository.save(newChatModel);
    }

    public void updateChatModelName(
        Query query, UpdateDefinition updateDefinition, FindAndModifyOptions options, Class entityClass) {
        chatModelTemplate.findAndModify(query, updateDefinition, options, entityClass);
    }

    public void addParticipantToChatModel(
        Query query, UpdateDefinition updateDefinition, FindAndModifyOptions options, Class entityClass) {
        chatModelTemplate.findAndModify(query, updateDefinition, options, entityClass);
    }

    public void makeParticipantAnAdmin(Query query, UpdateDefinition updateDefinition, Class entityClass) {
        chatModelTemplate.updateFirst(query, updateDefinition, entityClass);
    }

    public void deleteParticipantFromChatModel(Query query, UpdateDefinition updateDefinition, Class entityClass) {
        chatModelTemplate.updateFirst(query, updateDefinition, entityClass);
    }

    public List<ChatModel.ChatParticipants> getPagedListOfParticipants(String chatId, Integer pageNumber, Integer pageSize) {
        Query findQuery = new Query(Criteria.where("chatId").is(chatId));
        ChatModel chatModel = chatModelTemplate.findOne(findQuery, ChatModel.class);

        if(chatModel != null) {
            Aggregation aggregationFunction = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("chatId").is(chatId)),
                Aggregation.unwind("chatParticipants"),
                Aggregation.skip((long) pageNumber * pageSize), // TODO : Test this to ensure it works
                Aggregation.limit(pageSize)
            );

            AggregationResults<ChatModel.ChatParticipants> aggregationResults =
                chatModelTemplate.aggregate(aggregationFunction, ChatModel.class, ChatModel.ChatParticipants.class);

            List<ChatModel.ChatParticipants> requiredList = aggregationResults.getMappedResults();

            return requiredList;
        } else {
            return new ArrayList<>();
        }
    }

    public void deleteEntireChatModel(String chatModelId) {
        chatModelRepository.deleteById(chatModelId);
    }
}
