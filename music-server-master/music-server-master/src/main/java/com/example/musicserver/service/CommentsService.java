package com.example.musicserver.service;

import com.example.musicserver.pojo.Comments;
import java.util.List;

public interface CommentsService {

    List<Comments> queryComments(int commTargetId,int commType);

    int countOfComments(int commType);

    void addComm(Comments comments);

    void delComm(int commId);

    void addLikeComm(int commId,int userId);

    void delLikeComm(int commId,int userId);
}
