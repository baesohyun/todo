package com.inzent.todo.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.inzent.todo.dto.CalDateDetailDto;
import com.inzent.todo.dto.CalFilterItemDto;
import com.inzent.todo.dto.ChkProjectDto;
import com.inzent.todo.dto.ChkSuperTasksDto;
import com.inzent.todo.dto.ClickDateDto;
import com.inzent.todo.dto.FilterDto;
import com.inzent.todo.dto.GanttChartInfoDto;
import com.inzent.todo.dto.MemberDto;
import com.inzent.todo.dto.ScheduleDto;
import com.inzent.todo.security.Auth;
import com.inzent.todo.service.ProjectService;
import com.inzent.todo.service.ScheduleService;
import com.inzent.todo.vo.UserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ProjectService projectService;

    @Auth
    @GetMapping("/calendar")
    public List<ScheduleDto> getCalendarList(HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();
        List<ScheduleDto> list = scheduleService.getCalendarList(userId);
        return list;
    }

    @Auth
    @PostMapping("/getProjects")
    public List<Map<String, Object>> getProjects(HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();
        List<Map<String, Object>> list = scheduleService.getProjects(userId);
        // System.out.println("잘아아아아" + list);
        return list;
    }

    // 일정 등록 - 선택된 프로젝트에 관련된 업무대 출력
    @Auth
    @PostMapping("/chkproject")
    public List<ChkSuperTasksDto> getSuperTasks(@RequestBody ChkProjectDto chkprjdto, HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        chkprjdto.setId(userId);

        List<ChkSuperTasksDto> list = scheduleService.getSuperTasks(chkprjdto);
        return list;
    }

    // 선택한 프로젝트 정보 가지고 있기
    @Auth
    @PostMapping("/chkProjectInfo")
    public Map<String, Object> getChkProjectInfo(@RequestBody ChkProjectDto chkprjdto, HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        chkprjdto.setId(userId);

        Map<String, Object> map = scheduleService.getChkProjectInfo(chkprjdto);
        List<MemberDto> memberList = projectService.getMemberList(chkprjdto.getChkProject());
        map.put("memberList", memberList);
        return map;
    }

    // 선택한 상위업무 정보 가지고 있기
    @Auth
    @GetMapping("/subSortno/{chkSuperTask}")
    public String getSubSortNo(@PathVariable("chkSuperTask") String chkSuperTask) {
        // UserVo user = (UserVo) req.getAttribute("user");
        // String userId = user.getId();
        System.out.println("정렬번호------" + scheduleService.getSubSortNo(chkSuperTask));
        return scheduleService.getSubSortNo(chkSuperTask);
    }

    // 해당날짜 업무 조회
    @Auth
    @PostMapping("/clickdate")
    public List<CalDateDetailDto> getClickDateList(@RequestBody ClickDateDto cddto, HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        cddto.setId(userId);

        List<CalDateDetailDto> list = scheduleService.getClickDateList(cddto);
        return list;
    }

    // 해당 업무대의 업무 소 조회
    @Auth
    @PostMapping("/sublist")
    public List<CalDateDetailDto> getSubList(@RequestBody String superId, HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();
        // System.out.println("얄루루루1111" + superId);
        int length = superId.length();
        superId = superId.substring(0, length - 1);

        // System.out.println("얄루루루2222" + superId);
        List<CalDateDetailDto> list = scheduleService.getSubList(superId, userId);
        // System.out.println("제바라라랄" + list);
        return list;
    }

    // 필터 조회
    @Auth
    @GetMapping("/filter")
    public List<FilterDto> getFilter(HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        List<FilterDto> list = scheduleService.getFilter(userId);
        return list;
    }

    // 필터 조회
    @Auth
    @GetMapping("/chkFilterItem")
    public String getChkFilterItem(HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        String calItem = scheduleService.getChkFilterItem(userId);
        return calItem;
    }

    // 필터값 추가후 꺼내오기
    @Auth
    @PostMapping("/addcalitem")
    public void addCalFilterItem(@RequestBody CalFilterItemDto cfidto, HttpServletRequest req) {

        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();
        StringBuilder sbPrj = new StringBuilder();
        StringBuilder sbMem = new StringBuilder();
        for (String prj : cfidto.getPrjData()) {
            if (sbPrj.length() > 0) {
                sbPrj.append(",");
            }
            sbPrj.append(prj);
        } // end for
        for (String mem : cfidto.getMemData()) {
            if (sbMem.length() > 0) {
                sbMem.append(",");
            }
            sbMem.append(mem);
        } // end for
        String calItem = sbPrj.toString() + "::" + sbMem.toString() + "::" + cfidto.getUseData();
        String calFilter = scheduleService.addCalFilterItem(calItem, userId);
        // System.out.println("결과아아ㅏ아아아" + calFilter);
    }

    @Auth
    @PostMapping("/resetCalFilter")
    public void resetCalFilter(HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        scheduleService.resetCalFilter(userId);
        // System.out.println("초기화 성공");
    }

    ////////////////////////////////////// 간트차트 //////////////////////////////
    @Auth
    @GetMapping("/ganttSuper")
    public List<GanttChartInfoDto> getGanttChartSuperInfo(HttpServletRequest req) {

        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        List<GanttChartInfoDto> list = scheduleService.getGanttChartSuperInfo(userId);
        list.addAll(getGanttChartSubInfo(userId));
        // System.out.println("결과-------" + list);
        return list;
    }

    public List<GanttChartInfoDto> getGanttChartSubInfo(String userId) {

        return scheduleService.getGanttChartSubInfo(userId);
    }

    // 필터 조회
    @Auth
    @GetMapping("/chkGanttFilterItem")
    public String getChkGanttFilterItem(HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        String calItem = scheduleService.getChkGanttFilterItem(userId);
        return calItem;
    }

    // 필터값 추가후 꺼내오기
    @Auth
    @PostMapping("/addganttitem")
    public void addGanttFilterItem(@RequestBody CalFilterItemDto cfidto, HttpServletRequest req) {

        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();
        StringBuilder sbPrj = new StringBuilder();
        StringBuilder sbMem = new StringBuilder();
        for (String prj : cfidto.getPrjData()) {
            if (sbPrj.length() > 0) {
                sbPrj.append(",");
            }
            sbPrj.append(prj);
        } // end for
        for (String mem : cfidto.getMemData()) {
            if (sbMem.length() > 0) {
                sbMem.append(",");
            }
            sbMem.append(mem);
        } // end for
        String ganttItem = sbPrj.toString() + "::" + sbMem.toString() + "::" + cfidto.getUseData();
        String ganttFilter = scheduleService.addGanttFilterItem(ganttItem, userId);
        System.out.println("결과아아ㅏ아아아" + ganttFilter);
    }

    @Auth
    @PostMapping("/resetGanttFilter")
    public void resetGanttFilter(HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        scheduleService.resetGanttFilter(userId);
        System.out.println("초기화 성공");
    }

}