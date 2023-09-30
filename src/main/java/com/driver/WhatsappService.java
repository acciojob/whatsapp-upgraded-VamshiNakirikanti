package com.driver;

import java.util.Date;
import java.util.List;

public class WhatsappService {
    WhatsappRepository whatsappRepository = new WhatsappRepository();
    public String createUser(String name, String mobile) throws Exception {
        User user = new User();
        user.setName(name);
        user.setMobile(mobile);
        boolean userExist = whatsappRepository.checkUserExist(user);
        if(userExist){
            throw new Exception("User already exists");
        }
        whatsappRepository.addUser(user);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        Group group = new Group();
        String groupName = "";
        if(users.size()==2){
            groupName = users.get(1).getName();
        }
        else{
            int count = whatsappRepository.getCount();
            count++;
            groupName = "Group "+count;
            whatsappRepository.setCount(count);
        }

        group.setName(groupName);
        group.setNumberOfParticipants(users.size());
        whatsappRepository.addGroup(group);

        whatsappRepository.addGroupToAdminMap(group,users.get(0));
        whatsappRepository.addGroupToUserListMap(group,users);

        return group;
    }

    public int createMessage(String content) {
        Message message = new Message();
        int messageListSize = whatsappRepository.getMessageListSize();
        messageListSize++;

        message.setId(messageListSize);
        message.setContent(content);
        message.setTimestamp(new Date());

        whatsappRepository.addMessage(message);

        return message.getId();
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        boolean groupExist = whatsappRepository.checkGroupExist(group);
        if(!groupExist){
            throw new Exception("Group does not exist");
        }

        boolean userExistInGroup = whatsappRepository.checkUserExistInGroup(sender,group);
        if(!userExistInGroup){
            throw new Exception("You are not allowed to send message");
        }

        whatsappRepository.addUserMessage(sender,message);
        whatsappRepository.addGroupMessage(group,message);

        return whatsappRepository.getSizeOfAllMessgesInGroup(group);
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        boolean groupExist = whatsappRepository.checkGroupExist(group);
        if(!groupExist){
            throw new Exception("Group does not exist");
        }

        User currAdmin = whatsappRepository.getAdminOfGroup(group);
        if(!currAdmin.equals(approver)){
            throw new Exception("Approver does not have rights");
        }

        boolean userExistInGroup = whatsappRepository.checkUserExistInGroup(user,group);
        if(!userExistInGroup){
            throw new Exception("User is not a participant");
        }

        whatsappRepository.changeAdmin(group,user);

        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception {
        if(user==null){
            throw new Exception("User not found");
        }
        Group group = whatsappRepository.getGroupFromUser(user);
        if(group==null){
            throw new Exception("User not found");
        }

        User admin = whatsappRepository.getAdminOfGroup(group);
        if(user.equals(admin)){
            throw new Exception("Cannot remove admin");
        }

        List<Message> messageList = whatsappRepository.getAllMessagesOfUser(user);
        List<Message> groupMessageList = whatsappRepository.getAllMessagesOfGroup(group);
        List<Message> allMessages = whatsappRepository.getAllMessages();

        for(Message message:messageList){
            groupMessageList.remove(message);
            allMessages.remove(message);
        }

        whatsappRepository.updateMessagesInGroup(group,groupMessageList);
        whatsappRepository.removeUserFromGroup(user,group);
        whatsappRepository.setMessageList(allMessages);

        whatsappRepository.removeUser(user);
        int numberOfMessagesInAGroup = whatsappRepository.getSizeOfAllMessgesInGroup(group);
        return group.getNumberOfParticipants() +numberOfMessagesInAGroup + allMessages.size();
    }
}
