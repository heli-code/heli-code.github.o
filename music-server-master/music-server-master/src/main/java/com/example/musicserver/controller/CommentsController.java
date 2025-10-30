package com.example.musicserver.controller;

import com.example.musicserver.pojo.Comments;
import com.example.musicserver.service.impl.CommentsServiceImpl;
import com.example.musicserver.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    CommentsServiceImpl commentsService;

    @GetMapping("/allComments")
    public Result allComments(int commTargetId,int commType){
        List<Comments> list= commentsService.queryComments(commTargetId,commType);
        System.out.println(list);
        return Result.success(list);
    }

    @PostMapping("/addComm")
    public Result addComm(@RequestBody Comments comments){
        commentsService.addComm(comments);
        return Result.success();
    }

    @DeleteMapping("/delComm/{id}")
    public Result delSinger(@PathVariable int id){
        commentsService.delComm(id);
        return Result.success();
    }

    @PostMapping("/addLikeComm")
    public Result addLikeComm(int commId,int userId){
        commentsService.addLikeComm(commId, userId);
        return Result.success();
    }

    @DeleteMapping("/delLikeComm")
    public Result delLikeComm(int commId,int userId){
        commentsService.delLikeComm(commId, userId);
        return Result.success();
    }
}
