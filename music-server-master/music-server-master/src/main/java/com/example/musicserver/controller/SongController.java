package com.example.musicserver.controller;

import com.example.musicserver.pojo.Page;
import com.example.musicserver.pojo.Song;
import com.example.musicserver.service.impl.ListsServiceImpl;
import com.example.musicserver.service.impl.SongServiceImpl;
import com.example.musicserver.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/song")
public class SongController {

    @Autowired
    SongServiceImpl songService;
    @Autowired
    ListsServiceImpl listsService;

    @GetMapping("/querySongs")
    public Result querySongs(@RequestParam("pageNum")int pageNum,@RequestParam("pageSize") int pageSize){
        List<Song> list =songService.queryAllSong((pageNum-1)*pageSize,pageSize);
        int count=songService.countOfSongs();
        Map<String,Object> map =new HashMap<>();
        map.put("list",list);
        map.put("totalSize",count);
        return Result.success(map);
    }

    @GetMapping("/songOfLists")
    public Result songByList(int listId){
        List<Song> songList=songService.querySongOfList(listId);
        return Result.success(songList);
    }

    @GetMapping("/songByName")
    public Result songByName(@RequestParam("songName") String songName){
        List<Song> list= songService.querySongByName(songName);
        return Result.success(list);
    }

    @GetMapping("/songOfAlbum")
    public Result songOfAlbum(@RequestParam("albumId") int albumId){
        List<Song> songList=songService.querySongOfAlbum(albumId);
        return Result.success(songList);
    }

    @GetMapping("/songOfSinger")
    public Result songOfSinger(@RequestParam("singerId") int singerId){
        List<Song> songList=songService.querySongOfSinger(singerId);
        return Result.success(songList);
    }

    @GetMapping("/songOfUser")
    public Result songOfUser(@RequestParam("userId") int userId){
        List<Song> song=songService.querySongOfUser(userId);
        return Result.success(song);
    }

    @PostMapping("/addSongOfUser")
    public Result addCol(@RequestParam("userId") int userId,@RequestParam("songId") int songId){
        songService.addSongOfUser(userId, songId);//收藏音乐
        listsService.addSongToMyList(songId,userId);//同时放入我喜欢的音乐这个歌单里
        List<Song> song=songService.querySongOfUser(userId);
        return Result.success(song);
    }

    @DeleteMapping("/delSongOfUser/{userId}/{songId}")
    public Result delCol(@PathVariable("userId") int userId,@PathVariable("songId") int songId){
        songService.delSongOfUser(userId, songId);//取消收藏音乐
        listsService.delSongToMyList(songId,userId);//同时删除我喜欢的音乐这个歌单里
        List<Song> song=songService.querySongOfUser(userId);
        return Result.success(song);
    }

    @PostMapping("/addSong")
    public Result addSong(@RequestBody Song song){
        songService.addSong(song);
        return Result.success();
    }

    @PutMapping("/putSong")
    public Result putSong(@RequestBody Song song){
        songService.putSong(song);
        return Result.success();
    }

    @DeleteMapping("/delSong/{id}")
    public Result delSong(@PathVariable int id){
        songService.delSong(id);
        return Result.success();
    }

}
