package com.inzent.todo.service;

import java.util.List;

import com.inzent.todo.dto.ArchiveSubDto;
import com.inzent.todo.dto.ArchiveSuperDto;
import com.inzent.todo.repository.ArchiveDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArchiveService {

    @Autowired
    private ArchiveDao archiveDao;

    public List<ArchiveSuperDto> getArchiveSupers(String userId) {

        return archiveDao.getArchiveSupers(userId);
    }

    public List<ArchiveSubDto> getArchiveSubs(String userId) {

        return archiveDao.getArchiveSubs(userId);
    }

    public int deleteSubTask(String subId) {
        return archiveDao.deleteSubTask(subId);
    }

    public int deleteSuperTask(String superId) {
        return archiveDao.deleteSuperTask(superId);
    }

    public int restoreSubTask(String subId) {
        return archiveDao.restoreSubTask(subId);
    }

    public int restoreSuperTask(String superId) {
        return archiveDao.restoreSuperTask(superId);
    }

    public int sendToArchive(String taskId) {

        if (taskId.startsWith("TP")) {
            return archiveDao.sendToArchiveSuper(taskId);
        } else {
            return archiveDao.sendToArchiveSub(taskId);
        }

    }
}