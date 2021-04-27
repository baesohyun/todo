package com.inzent.todo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.inzent.todo.dto.ArchiveSubDto;
import com.inzent.todo.dto.ArchiveSuperDto;
import com.inzent.todo.security.Auth;
import com.inzent.todo.service.ArchiveService;
import com.inzent.todo.vo.UserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/archive")
public class ArchiveController {

    @Autowired
    private ArchiveService archiveService;

    @Auth
    @GetMapping("/archiveSuper")
    public List<ArchiveSuperDto> getArchiveSupers(HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        List<ArchiveSuperDto> list = archiveService.getArchiveSupers(userId);
        return list;
    }

    @Auth
    @GetMapping("/archiveSub")
    public List<ArchiveSubDto> getArchiveSubs(HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        List<ArchiveSubDto> list = archiveService.getArchiveSubs(userId);
        return list;
    }

    @Auth
    @GetMapping("/deleteSub/{subId}")
    public void deleteSubTask(@PathVariable("subId") String subId) {
        int cnt = archiveService.deleteSubTask(subId);
        if (cnt == 1) {
            System.out.println("삭제 성공");
        } else {
            System.out.println("삭제 실패");
        }
    }

    @Auth
    @GetMapping("/deleteSuper/{superId}")
    public void deleteSuperTask(@PathVariable("superId") String superId) {
        int cnt = archiveService.deleteSuperTask(superId);
        if (cnt == 1) {
            System.out.println("삭제 성공");
        } else {
            System.out.println("삭제 실패");
        }
    }

    @Auth
    @GetMapping("/restoreSub/{subId}")
    public void restoreSubTask(@PathVariable("subId") String subId) {
        System.out.println("복구---" + subId);
        int cnt = archiveService.restoreSubTask(subId);
        if (cnt == 1) {
            System.out.println("삭제 성공");
        } else {
            System.out.println("삭제 실패");
        }
    }

    @Auth
    @GetMapping("/restoreSuper/{superId}")
    public void restoreSuperTask(@PathVariable("superId") String superId) {
        int cnt = archiveService.restoreSuperTask(superId);
        if (cnt == 1) {
            System.out.println("삭제 성공");
        } else {
            System.out.println("삭제 실패");
        }
    }

    @GetMapping("/sendToArchive")
    public void sendToArchive(@RequestParam String taskId) {
        System.out.println("아카이브 보내기 : " + taskId);
        int cnt = archiveService.sendToArchive(taskId);
        if (cnt == 1) {
            System.out.println("아카이브 보내기 성공");
        } else {
            System.out.println(" 실패");
        }
    }
}