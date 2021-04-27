package com.inzent.todo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.inzent.todo.repository.SearchDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    @Autowired
    private SearchDao searchDao;

    public List<String> getAllList(Map<String, Object> map) {
        String option = (String) map.get("option");
        List<String> list = new ArrayList<>();
        if ("opp".equals(option)) { // 프로젝트일때
            list = searchDao.selectProjectList(map);
        } else if ("opt".equals(option)) { // 업무일떄
            list = searchDao.selectTaskList(map);
        } else if ("opm".equals(option)) { // 담당자 일때
            list = searchDao.selectManagerList(map);
        } else {
            // 전체일떄
            list = searchDao.selectAllList(map);
        }
        return list;
    }
}