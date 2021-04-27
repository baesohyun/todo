package com.inzent.todo.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inzent.todo.dto.CheckListDto;
import com.inzent.todo.dto.MemberDto;
import com.inzent.todo.dto.ProjectCardDto;
import com.inzent.todo.dto.ProjectDto;
import com.inzent.todo.dto.ReportDto;
import com.inzent.todo.dto.TaskBoardListDto;
import com.inzent.todo.dto.TaskDto;
import com.inzent.todo.dto.TaskUpdateDto;
import com.inzent.todo.security.Auth;
import com.inzent.todo.service.ProjectService;
import com.inzent.todo.vo.CheckListItemVo;
import com.inzent.todo.vo.CheckListVo;
import com.inzent.todo.vo.CommentVo;
import com.inzent.todo.vo.FileVo;
import com.inzent.todo.vo.LabelVo;
import com.inzent.todo.vo.ProjectVo;
import com.inzent.todo.vo.ReportVo;
import com.inzent.todo.vo.UserVo;
import com.inzent.todo.vo.StarredTaskVo;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Auth
    @GetMapping("/")
    public Map<String, Object> getProjectList(HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        // System.out.println("Project Controller : get ProjectList");
        // System.out.println(user.toString());
        return projectService.getProjectList(user.getId());
        // return null;
    }

    @Auth
    @GetMapping("/{pid}")
    public Map<String, Object> getProjectInfo(HttpServletRequest req, @PathVariable("pid") String pid) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();
        // System.out.println("Project Controller : getProjectInfo : " + pid);
        // 멤버가 아니라면 못들어가게 막아야하지않나.?
        int memberNo = projectService.getMemberNo(pid, userId);
        System.out.println("현재 사용자의 프로젝트 MEMBER NUM : " + memberNo);
        // 이 프로젝트에 속한 업무(대) 중 private이면서, 내 id와 같은 업무 가져오기..
        List<TaskBoardListDto> taskBoardList = projectService.getTaskList(pid, memberNo);
        ProjectVo projectVo = projectService.getProject(pid);
        List<LabelVo> lableList = projectService.getLabelList(pid);
        List<MemberDto> memberList = projectService.getMemberList(pid);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("project", projectVo);
        map.put("memberNo", memberNo);
        map.put("memberList", memberList);
        map.put("taskBoardList", taskBoardList);
        map.put("labelList", lableList);
        // map.put("memberList", )
        return map;
    }

    @Auth
    @PostMapping("/updateProject")
    public Map<String, Object> updateProject(HttpServletRequest req, ProjectDto projectDto) {
        // System.out.println(projectDto);
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        projectService.updateProject(projectDto);
        ProjectVo projectVo = projectService.getProject(projectDto.getId());
        List<MemberDto> memberList = projectService.getMemberList(projectDto.getId());
        Map<String, Object> map = new HashMap<String, Object>();
        int memberNo = projectService.getMemberNo(projectDto.getId(), userId);
        map.put("memberNo", memberNo);
        map.put("project", projectVo);
        map.put("memberList", memberList);
        return map;
    }

    @Auth
    @PostMapping("/addProject")
    public ProjectVo addProject(HttpServletRequest req, ProjectDto projectDto) throws Exception {
        // System.out.println("Project Controller : addProject");
        UserVo user = (UserVo) req.getAttribute("user");
        return projectService.addProject(projectDto, user.getId());

        // System.out.println(projectDto.getCoverImg());
        // System.out.println(formObject.toString());
    }

    @PostMapping("/addSuperTask")
    public void addSuperTask(TaskDto taskDto, HttpServletRequest req) throws Exception {
        System.out.println("Project Controller : 여기안옴Task");
        projectService.addTask(taskDto);
        // System.out.println(taskDto.toString());
        // System.out.println(projectDto.getCoverImg());
    }

    @PostMapping("/addSubTask")
    public void addSubTask(TaskDto taskDto, HttpServletRequest req) throws Exception {
        System.out.println("Project Controller : addSubTask");
        // System.out.println(taskDto.toString());
        projectService.addTask(taskDto);
        // System.out.println(projectDto.getCoverImg());
        // System.out.println(formObject.toString());
    }

    @PostMapping("/updateTask")
    public void updateTask(TaskUpdateDto taskDto, HttpServletRequest req) throws Exception {
        // System.out.println("Project Controller : updateTask");
        // System.out.println(taskDto.getLabels().length());
        projectService.updateTask(taskDto);
        // System.out.println(projectDto.getCoverImg());
        // System.out.println(taskDto.toString());
    }

    @PostMapping("/addNewLabel")
    public void addNewLabel(@RequestBody LabelVo newLabel) {
        // System.out.println("Project Controller : addNewLabel");
        // System.out.println(newLabel.toString());
        projectService.addNewLabel(newLabel);
    }

    @PostMapping("/reorderTask")
    public List<TaskBoardListDto> reorderTask(@RequestBody TaskDto targetTask) {
        // System.out.println("Project Controller : reorderTask");
        // System.out.println(targetTask.toString());
        projectService.reorderTask(targetTask);
        // List<TaskBoardListDto> taskBoardList =
        return projectService.getTaskList(targetTask.getProjectId(), targetTask.getMemberNo());
        // memberNo);

    }

    @PostMapping("/addComment")
    public List<CommentVo> addComment(@RequestBody CommentVo comment) {
        // System.out.println("Project Controller : addComment");
        // System.out.println(comment.toString());
        projectService.addComment(comment);
        return projectService.getComments(comment.getTaskId());
        // projectService.reorderTask(targetTask);
        // List<TaskBoardListDto> taskBoardList =
        // return projectService.getTaskList(targetTask.getProjectId(),
        // targetTask.getMemberNo());
        // memberNo);

    }

    @PostMapping("/deleteComment")
    public List<CommentVo> deleteComment(@RequestBody CommentVo comment) {
        // System.out.println("Project Controller : addComment");
        // System.out.println(comment.toString());
        projectService.deleteComment(comment.getCommentNo());
        return projectService.getComments(comment.getTaskId());
        // projectService.reorderTask(targetTask);
        // List<TaskBoardListDto> taskBoardList =
        // return projectService.getTaskList(targetTask.getProjectId(),
        // targetTask.getMemberNo());
        // memberNo);

    }

    @GetMapping("/getLabels/{pid}")
    public List<LabelVo> getLabel(@PathVariable("pid") String pid) {
        List<LabelVo> lableList = projectService.getLabelList(pid);
        return lableList;
    }

    @GetMapping("/getTask/{taskId}")
    public Map<String, Object> getTask(@PathVariable("taskId") String taskId) {
        TaskDto task = projectService.getTask(taskId);
        List<FileVo> files = projectService.getFiles(taskId);
        List<CommentVo> comments = projectService.getComments(taskId);
        List<CheckListDto> checkLists = projectService.getCheckLists(taskId);
        // System.out.println(checkLists.toString());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("task", task);
        map.put("files", files);
        map.put("comments", comments);
        map.put("checkLists", checkLists);

        return map;
    }

    @PostMapping("/starredTask")
    public int addStaredTask(@RequestBody StarredTaskVo starred) {
        System.out.println("staredTask");
        return projectService.addStarredTask(starred);
    }

    @PostMapping("/deleteStarred")
    public void deleteStarred(@RequestBody String starId) {
        System.out.println("deleteStarred");
        starId = starId.substring(0, starId.length() - 1);
        projectService.deleteStarred(Integer.parseInt(starId));
    }

    @PostMapping("/addCheckList")
    public void addCheckList(@RequestBody CheckListVo checkList) {
        System.out.println("addCheckList");
        projectService.addCheckList(checkList);
    }

    @PostMapping("/addCheckListItem")
    public void addCheckListItem(@RequestBody CheckListItemVo checkListItem) {
        System.out.println("addCheckListItem" + checkListItem.toString());
        projectService.addCheckListItem(checkListItem);
        projectService.updateCheckListRate(checkListItem.getListNo());
    }

    @PostMapping("/getCheckLists")
    public List<CheckListDto> getCheckLists(@RequestBody String taskId) {
        System.out.println("getCheck..." + taskId);
        taskId = taskId.substring(0, taskId.length() - 1);
        return projectService.getCheckLists(taskId);
        // 진행률 갱신!

    }

    @PostMapping("/setCheckItem")
    public void setCheckItem(@RequestBody CheckListItemVo checkListItem) {
        System.out.println("setCheckItem...1 : " + checkListItem.getItemNo());
        projectService.updateCheckListItem(checkListItem.getItemNo());
        projectService.updateCheckListRate(checkListItem.getListNo());
    }

    @PostMapping("/deleteCheckItem")
    public void deleteCheckItem(@RequestBody CheckListItemVo checkListItem) {
        System.out.println("deleteCheckItem... : " + checkListItem.getItemNo());
        projectService.deleteCheckListItem(checkListItem.getItemNo());
        projectService.updateCheckListRate(checkListItem.getListNo());
    }

    @PostMapping("/deleteCheckList")
    public void deleteCheckList(@RequestBody CheckListVo checkList) {
        projectService.deleteCheckList(checkList.getListNo());
    }

    @PostMapping("/download")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, @RequestBody FileVo f) {
        // System.out.println("파일 다운");
        // System.out.println(f.toString());

        try {
            File file = new File("./upload/files", f.getSaveName());
            Files.copy(file.toPath(), response.getOutputStream());
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            String contentDisposition = String.format("attachment; filename=%s", file.getName());
            int fileSize = Long.valueOf(file.length()).intValue();
            // System.out.println(mimeType);
            // System.out.println(file.getName());
            // System.out.println(file.toPath());
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", contentDisposition);
            response.setContentLength(fileSize);
            // System.out.println(response.getContentType());
        } catch (FileNotFoundException e) {
            // System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @GetMapping("/deleteFile")
    public void deleteFile(@RequestParam int fileNo) {
        System.out.println("deleteFIle : " + fileNo);
        projectService.deleteFile(fileNo);
    }

    @PostMapping("/sendReport")
    public void sendReport(ReportDto report) throws Exception {
        System.out.println(report.toString());
        projectService.insertReport(report);
        // projectService.deleteCheckList(checkList.getListNo());
    }

    @Auth
    @PostMapping("/fetchReports")
    public Map<String, Object> fetchReports(HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();
        System.out.println("userId: " + userId);
        List<ReportDto> sendReports = projectService.getSendReports(userId);
        List<ReportDto> receiveReports = projectService.getReceiveReports(userId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sendReports", sendReports);
        map.put("receiveReports", receiveReports);
        return map;
        // projectService.deleteCheckList(checkList.getListNo());
    }

    @GetMapping("/getReportDetail/{rid}")
    public Map<String, Object> getReportDetail(@PathVariable("rid") int rid) {
        ReportDto report = projectService.getReportDetail(rid);
        List<FileVo> files = projectService.getFiles("RP" + rid);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("report", report);
        map.put("files", files);
        return map;
    }

}