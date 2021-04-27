package com.inzent.todo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.inzent.todo.security.Auth;
import com.inzent.todo.service.SearchService;
import com.inzent.todo.vo.UserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Auth
    @GetMapping("/fetchAll")
    public List<String> getAllList(HttpServletRequest req,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "option", required = false) String option,
            @RequestParam(value = "range", required = false) String range) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("search", search);
        paramMap.put("option", option);
        paramMap.put("range", range);
        paramMap.put("userId", userId);

        return searchService.getAllList(paramMap);
    }
}