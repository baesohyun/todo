package com.inzent.todo.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inzent.todo.dto.CheckListDto;
import com.inzent.todo.dto.MemberDto;
import com.inzent.todo.dto.ProjectDto;
import com.inzent.todo.dto.ReportDto;
import com.inzent.todo.dto.TaskBoardListDto;
import com.inzent.todo.dto.TaskDto;
import com.inzent.todo.dto.TaskUpdateDto;
import com.inzent.todo.repository.FileDao;
import com.inzent.todo.repository.MemberDao;
import com.inzent.todo.repository.ProjectDao;
import com.inzent.todo.util.DBUtil;
import com.inzent.todo.util.FileUtil;
import com.inzent.todo.vo.CheckListItemVo;
import com.inzent.todo.vo.CheckListVo;
import com.inzent.todo.vo.CommentVo;
import com.inzent.todo.vo.FileVo;
import com.inzent.todo.vo.LabelVo;
import com.inzent.todo.vo.MemberVo;
import com.inzent.todo.vo.ProjectVo;
import com.inzent.todo.vo.StarredTaskVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private MemberDao memberDao;

    public ProjectVo addProject(ProjectDto projectDto, String userId) throws Exception {
        // System.out.println("ProjectService.addproject ---");
        // System.out.println(projectDto.toString());
        // System.out.println(projectDto.getMembers().length);
        ProjectVo projectVo = new ProjectVo();
        String projectId = DBUtil.generateKey("PJ");
        projectVo.setId(projectId);
        projectVo.setTitle(projectDto.getTitle());
        projectVo.setDescription(projectDto.getDescription());
        projectVo.setUsePublic(projectDto.getUsePublic());
        projectVo.setStartDate(projectDto.getStartDate());
        projectVo.setEndDate(projectDto.getEndDate());
        // 프로젝트 테이블에 insert
        // projectVo에 Dto내용 뽑아서 담기!
        projectVo.setImgNo(projectDto.getImgNo());

        // TODO 프로젝트 이미지가 있다면 저장, 없다면,색깔? 테마?
        // if (projectDto.getCoverImg() == null) {
        // projectVo.setImgNo("#" + projectDto.getCoverColor());
        // } else {
        // projectVo.setImgNo(projectDto.getCoverImg());
        // }
        // TODO 프로젝트 멤버테이블에 생성한사람, 멤버가 있다면 멤버도 추가

        MemberVo mem = new MemberVo();
        mem.setProjectId(projectId);
        mem.setUserId(userId);
        projectVo.setManager(memberDao.insertMember(mem)); // 프로젝트 생성자 (매니저) 등록

        if (projectDto.getMembers().length != 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            // System.out.println("멤버가 있지롱");
            // System.out.println(projectDto.getMembers().length);
            map.put("projectId", projectId);
            map.put("members", projectDto.getMembers());
            memberDao.insertMembers(map);
        }
        projectDao.insertProject(projectVo);

        return projectVo;
    }

    public void addTask(TaskDto taskDto) throws Exception {
        System.out.println("-----------ProjectService.addTask ----------");
        System.out.println(taskDto.toString());
        System.out.println("--------------------------------------------");

        if (taskDto.getSortNo() == 0) {
            taskDto.setSortNo(65535);
        }
        String taskId = "";
        if (taskDto.getTaskSuperId() == null) {
            // System.out.println(" :: ADD SUPER TASK:: ");
            taskId = DBUtil.generateKey("TP");
            taskDto.setTaskId(taskId);
            projectDao.insertSuperTask(taskDto);
            projectDao.updateProgressRate(taskDto.getProjectId());
        } else {
            // System.out.println(" :: ADD SUB TASK:: ");
            taskId = DBUtil.generateKey("TB");
            taskDto.setTaskId(taskId);
            projectDao.insertSubTask(taskDto);

        }
        if (taskDto.getAttachFiles() != null) {
            // System.out.println("Attach files... ");
            for (MultipartFile f : taskDto.getAttachFiles()) {
                String orgFileName = f.getOriginalFilename();
                int size = (int) f.getSize();
                String fileExtName = orgFileName.substring(orgFileName.lastIndexOf('.') + 1, orgFileName.length());
                String saveFileName = DBUtil.generateSaveFileName(fileExtName);
                // 파일 업로드
                FileUtil.writeFile(f, saveFileName);
                // DB에 img 저장
                FileVo file = new FileVo();
                file.setSaveName(saveFileName);
                file.setOrgName(orgFileName);
                file.setExt(fileExtName);
                file.setSize(size);
                file.setTaskId(taskId);
                fileDao.insertFile(file);
            }
        }
        // 체크리스트가 있다면 추가
        if (taskDto.getCheckLists() != null) {
            System.out.println("체크리스트 있음");
            Gson gson = new Gson();
            Type listType = new TypeToken<List<CheckListDto>>() {
            }.getType();
            Type ItemType = new TypeToken<List<CheckListItemVo>>() {
            }.getType();
            List<CheckListDto> list = gson.fromJson(taskDto.getCheckLists(), listType);
            System.out.println(list.toString());
            for (CheckListDto chk : list) {
                chk.setTaskId(taskId);
                System.out.println(chk.toString());
                projectDao.insertCheckList(chk);
                System.out.println("추가된 리스트 no : " + chk.getListNo());
                List<CheckListItemVo> items = gson.fromJson(chk.getCheckListItems(), ItemType);
                System.out.println(items.toString());
                if (items.size() > 0) {
                    projectDao.insertAllCheckListItems(items, chk.getListNo());
                }
                // System.out.println(items.toString());
            }
            /*
             * CheckListDto dto = taskDto.getCheckList(); CheckListVo chk = new
             * CheckListVo(); chk.setTitle(dto.getTitle());
             * chk.setMemberNo(dto.getMemberNo()); chk.setTaskId(dto.getTaskId());
             * projectDao.insertCheckList(chk); System.out.println(chk.toString());
             */

        }
        // 담당자가 있다면, 담당자 테이블에 추가!
        if (taskDto.getManager().length != 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            // System.out.println("멤버가 있지롱");
            map.put("taskId", taskId);
            map.put("managers", taskDto.getManager());
            memberDao.insertManagers(map);
            // 여러명등록해줘야해.ㅠㅠ
        }
    }

    public void updateTask(TaskUpdateDto taskDto) throws Exception {
        // if (taskDto.getSortNo() == 0) {
        // taskDto.setSortNo(65535);
        // }
        // String taskId = "";
        System.out.println(taskDto.toString());
        if (taskDto.getTaskId().startsWith("TP")) {
            System.out.println(" :: UPDATE SUPER TASK:: ");
            projectDao.updateSuperTask(taskDto);
            projectDao.updateProgressRate(taskDto.getProjectId());
        } else {
            System.out.println(" :: UPDATE SUB TASK:: ");
            projectDao.updateSubTask(taskDto);
        }

        // TODO 만약 삭제가 있다면, 삭제도 전달!!
        if (taskDto.getAttachFiles() != null) {
            // System.out.println("Attach files... ");
            for (MultipartFile f : taskDto.getAttachFiles()) {
                String orgFileName = f.getOriginalFilename();
                int size = (int) f.getSize();
                String fileExtName = orgFileName.substring(orgFileName.lastIndexOf('.') + 1, orgFileName.length());
                String saveFileName = DBUtil.generateSaveFileName(fileExtName);
                // 파일 업로드
                FileUtil.writeFile(f, saveFileName);
                // DB에 img 저장
                FileVo file = new FileVo();
                file.setSaveName(saveFileName);
                file.setOrgName(orgFileName);
                file.setExt(fileExtName);
                file.setSize(size);
                file.setTaskId(taskDto.getTaskId());
                fileDao.insertFile(file);
            }
        }
        // 담당자가 있다면, 담당자 테이블에 추가!
        if (taskDto.getAddManager().length != 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            // System.out.println("멤버 추가가 있지롱");
            map.put("taskId", taskDto.getTaskId());
            map.put("managers", taskDto.getAddManager());
            memberDao.insertManagers(map);
            // 여러명등록해줘야해.ㅠㅠ
        }
        if (taskDto.getSubManager().length != 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            // System.out.println("멤버 삭제가 있지롱");
            map.put("taskId", taskDto.getTaskId());
            map.put("managers", taskDto.getSubManager());
            memberDao.deleteManagers(map);
            // 여러명등록해줘야해.ㅠㅠ
        }

    }

    public Map<String, Object> getProjectList(String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("allProject", projectDao.selectAllProjectList(userId));
        map.put("myProject", projectDao.selectMyProjectList(userId));

        return map;
    }

    public List<TaskBoardListDto> getTaskList(String pid, int memNo) {
        // System.out.println("UserID : " + userId);
        List<TaskBoardListDto> taskBoardList = new ArrayList<>();

        List<TaskDto> superTaskList = projectDao.selectTaskList(pid, memNo);
        // System.out.println("superTaskList size : " + superTaskList.size());
        // System.out.println("--------------------------------------------");
        for (TaskDto superTask : superTaskList) {
            TaskBoardListDto tbl = new TaskBoardListDto();
            tbl.setSuperTask(superTask);
            List<TaskDto> subTaskList = projectDao.selectTaskSubList(superTask.getTaskId(), memNo);
            if (subTaskList == null) {
                // System.out.println("서브태스크가 없습니다.");
            } else {
                // System.out.println("서브테스크가 있습니다.");
                // System.out.println(subTaskList.toString());
                tbl.setSubTaskList(subTaskList);
            }
            taskBoardList.add(tbl);
        }

        return taskBoardList;
    }

    public ProjectVo getProject(String pid) {
        return projectDao.selectProject(pid);
    }

    public int getMemberNo(String pid, String userId) {
        return projectDao.selectMemberNo(pid, userId);
    }

    public void addNewLabel(LabelVo label) {
        projectDao.insertNewLabel(label);
    }

    public List<LabelVo> getLabelList(String pid) {
        return projectDao.getLabelList(pid);
    }

    public List<MemberDto> getMemberList(String pid) {
        return projectDao.getMemberList(pid);
    }

    public TaskDto getTask(String taskId) {
        return projectDao.getTask(taskId);
    }

    public List<FileVo> getFiles(String taskId) {
        return projectDao.getFiles(taskId);
    }

    public void reorderTask(TaskDto targetTask) {
        projectDao.updateSortNo(targetTask);
    }

    public void addComment(CommentVo comment) {
        projectDao.insertComment(comment);
    }

    public void deleteComment(int commentNo) {
        projectDao.deleteComment(commentNo);
    }

    public List<CommentVo> getComments(String taskId) {
        return projectDao.selectComments(taskId);
    }

    public int addStarredTask(StarredTaskVo starred) {
        return projectDao.insertStarredTask(starred);
    }

    public void addCheckList(CheckListVo checkList) {
        projectDao.insertCheckList(checkList);
    }

    public List<CheckListDto> getCheckLists(String taskId) {
        System.out.println("getCehckList..... " + taskId);
        return projectDao.selectCheckList(taskId);
    }

    public int addCheckListItem(CheckListItemVo checkListItem) {
        return projectDao.insertCheckList(checkListItem);
    }

    public void updateCheckListRate(int listNo) {
        projectDao.updateCheckListRate(listNo);
    }

    public void updateCheckListItem(int itemNo) {
        projectDao.updateCheckListItem(itemNo);
    }

    public void deleteCheckListItem(int itemNo) {
        projectDao.deleteCheckListItem(itemNo);
    }

    public void deleteCheckList(int listNo) {
        projectDao.deleteCheckList(listNo);
    }

    public void deleteFile(int fileNo) {
        fileDao.deleteFile(fileNo);
    }

    public void updateProject(ProjectDto projectDto) {

        if (projectDto.getNewMems().length != 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            // System.out.println("멤버가 있지롱");
            // System.out.println(projectDto.getMembers().length);
            map.put("projectId", projectDto.getId());
            map.put("members", projectDto.getNewMems());
            memberDao.insertMembers(map);
        }
        if (projectDto.getDelMems().length != 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            // System.out.println("멤버가 있지롱");
            // System.out.println(projectDto.getMembers().length);
            map.put("projectId", projectDto.getId());
            map.put("members", projectDto.getDelMems());
            memberDao.deleteMembers(map);
        }

        projectDao.updateProject(projectDto);
    }

    public void deleteStarred(int starId) {
        projectDao.deleteStarred(starId);
    }

    public void insertReport(ReportDto report) throws Exception {
        projectDao.insertReport(report);
        System.out.println("인서트 후 no: " + report.getReportNo());
        if (report.getAttachFiles() != null) {
            // System.out.println("Attach files... ");
            for (MultipartFile f : report.getAttachFiles()) {
                String orgFileName = f.getOriginalFilename();
                int size = (int) f.getSize();
                String fileExtName = orgFileName.substring(orgFileName.lastIndexOf('.') + 1, orgFileName.length());
                String saveFileName = DBUtil.generateSaveFileName(fileExtName);
                // 파일 업로드
                FileUtil.writeFile(f, saveFileName);
                // DB에 img 저장
                FileVo file = new FileVo();
                file.setSaveName(saveFileName);
                file.setOrgName(orgFileName);
                file.setExt(fileExtName);
                file.setSize(size);
                file.setTaskId("RP" + report.getReportNo());
                fileDao.insertFile(file);
            }
        }

    }

    public List<ReportDto> getSendReports(String userId) {
        return projectDao.selectSendReport(userId);
    }

    public List<ReportDto> getReceiveReports(String userId) {
        return projectDao.selectReceiveReport(userId);
    }

    public ReportDto getReportDetail(int rid) {
        return projectDao.selectReportDetail(rid);
    }

}