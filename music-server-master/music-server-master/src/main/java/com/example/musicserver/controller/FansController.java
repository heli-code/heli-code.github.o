package com.example.musicserver.controller;

import com.example.musicserver.pojo.Fans;
import com.example.musicserver.service.FansService;
import com.example.musicserver.service.impl.FansServiceImpl;
import com.example.musicserver.utils.Result;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fans")
public class FansController {

    @Autowired
    FansServiceImpl fansService;

    @GetMapping("/myFocus")
    public Result getMyFocus(@RequestParam("userId") int userId){
        List<Fans> list = fansService.getMyFocus(userId);
        return Result.success(list);
    }

    @GetMapping("/myFans")
    public Result getMyFans(@RequestParam("userId") int userId){
        List<Fans> list = fansService.getMyFans(userId);
        return Result.success(list);
    }

    @GetMapping("/focusCount")
    public Result getFocusCount(@RequestParam("userId") int userId){
        int count = fansService.getFocusCount(userId);
        return Result.success(count);
    }

    @GetMapping("/fansCount")
    public Result getFansCount(@RequestParam("userId") int userId){
        int count = fansService.getFansCount(userId);
        return Result.success(count);
    }

    @GetMapping("/singerIsFocus")
    public Result singerIsFocus(@RequestParam("userId") int userId,@RequestParam("singerId") int singerId){
        int count = fansService.singerIsFocus(userId, singerId);
        return Result.success(count);
    }

    @GetMapping("/userIsFocus")
    public Result userIsFocus(@RequestParam("userId") int userId,@RequestParam("myId")int myId){
        int count = fansService.userIsFocus(userId, myId);
        return Result.success(count);
    }

    @GetMapping("/userIsFans")
    public Result userIsFans(@RequestParam("userId") int userId,@RequestParam("myId")int myId){
        int count = fansService.userIsFans(userId, myId);
        return Result.success(count);
    }

    @PostMapping("/addFocus")
    public Result addFocus(@RequestParam("userId")int userId,@RequestParam("myId") int myId,@RequestParam("type") int type){
        fansService.addFocus(userId, myId, type);
        return Result.success();
    }

    @DeleteMapping("delFocus")
    public Result delFocus(@RequestParam("userId")int userId,@RequestParam("myId") int myId,@RequestParam("type") int type){
        fansService.delFocus(userId, myId, type);
        return Result.success();
    }
}
