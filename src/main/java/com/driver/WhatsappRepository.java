package com.driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WhatsappRepository {

    private List<User> userList = new ArrayList<>();
    private List<Group> groupList = new ArrayList<>();
    private List<Message> messageList = new ArrayList<>();
    private int count = 0;

    private HashMap<Group,User> groupToAdminMap = new HashMap<>();
    private HashMap<Group,List<User>> groupToUserListMap = new HashMap<>();
    private HashMap<User,List<Message>> userMessages = new HashMap<>();
    private HashMap<Group,List<Message>> groupMessages = new HashMap<>();
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addUser(User user) {
        userList.add(user);
        userMessages.put(user,new ArrayList<>());
    }

    public boolean checkUserExist(User user) {
        if(userList.contains(user)){
            return true;
        }
        return false;
    }

    public void addGroup(Group group) {
        groupList.add(group);
        groupMessages.put(group,new ArrayList<>());
    }

    public void addGroupToUserListMap(Group group, List<User> users) {
        groupToUserListMap.put(group,users);
    }

    public void addGroupToAdminMap(Group group,User user) {
        groupToAdminMap.put(group,user);
    }

    public int getMessageListSize() {
        return messageList.size();
    }

    public void addMessage(Message message) {
        messageList.add(message);
    }

    public boolean checkGroupExist(Group group) {
        return groupList.contains(group);
    }

    public boolean checkUserExistInGroup(User sender, Group group) {
        List<User> users = groupToUserListMap.get(group);
        return users.contains(sender);
    }

    public void addGroupMessage(Group group, Message message) {
        groupMessages.get(group).add(message);
    }

    public void addUserMessage(User sender, Message message) {
        userMessages.get(sender).add(message);
    }

    public int getSizeOfAllMessgesInGroup(Group group) {
        return groupMessages.get(group).size();
    }

    public User getAdminOfGroup(Group group) {
        return groupToAdminMap.get(group);
    }

    public void changeAdmin(Group group, User user) {
        groupToAdminMap.put(group,user);
    }

    public Group getGroupFromUser(User user) {
        for(Group group:groupToUserListMap.keySet()){
            List<User> users = groupToUserListMap.get(group);
            if(users.contains(user)){
                return group;
            }
        }
        return null;
    }

    public List<Message> getAllMessagesOfUser(User user) {
        return userMessages.get(user);
    }

    public List<Message> getAllMessagesOfGroup(Group group) {
        return groupMessages.get(group);
    }

    public void updateMessagesInGroup(Group group, List<Message> groupMessageList) {
        groupMessages.put(group,groupMessageList);
    }

    public void removeUserFromGroup(User user, Group group) {
        groupToUserListMap.get(group).remove(user);
        group.setNumberOfParticipants(group.getNumberOfParticipants()-1);
    }

    public void removeUser(User user) {
        userMessages.remove(user);
        userList.remove(user);
    }

    public List<Message> getAllMessages() {
        return messageList;
    }

    public void setMessageList(List<Message> allMessages) {
        messageList = allMessages;
    }
}
