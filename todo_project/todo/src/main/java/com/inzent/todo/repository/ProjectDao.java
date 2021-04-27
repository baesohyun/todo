package com.inzent.todo.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inzent.todo.dto.CheckListDto;
import com.inzent.todo.dto.MemberDto;
import com.inzent.todo.dto.ProjectCardDto;
import com.inzent.todo.dto.ProjectDto;
import com.inzent.todo.dto.ReportDto;
import com.inzent.todo.dto.TaskDto;
import com.inzent.todo.dto.TaskUpdateDto;
import com.inzent.todo.vo.CheckListItemVo;
import com.inzent.todo.vo.CheckListVo;
import com.inzent.todo.vo.CommentVo;
import com.inzent.todo.vo.FileVo;
import com.inzent.todo.vo.LabelVo;
import com.inzent.todo.vo.ProjectVo;
import com.inzent.todo.vo.SuperTaskVo;
import com.inzent.todo.vo.StarredTaskVo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDao {

    @Autowired
    private SqlSession sqlSession;

    public int insertProject(ProjectVo projectVo) {

        // System.out.println("ProjectDao - insertProject");

        // System.out.println(projectVo.toString());
        return sqlSession.insert("project.insertProject", projectVo);

    }

    public int insertSuperTask(TaskDto taskDto) {
        // System.out.println("ProjectDao - insertTaskSuper");
        return sqlSession.insert("project.insertTaskSuper", taskDto);
    }

    public int insertSubTask(TaskDto taskDto) {
        // System.out.println("ProjectDao - insertTaskSub");
        return sqlSession.insert("project.insertTaskSub", taskDto);
    }

    public int updateSubTask(TaskUpdateDto taskDto) {
        // System.out.println("## ProjectDao - updateTaskSub");
        return sqlSession.update("project.updateTaskSub", taskDto);
    }

    public int updateSuperTask(TaskUpdateDto taskDto) {
        // System.out.println("## ProjectDao - updateTaskSub");
        return sqlSession.update("project.updateTaskSuper", taskDto);
    }

    public List<ProjectCardDto> selectAllProjectList(String userId) {
        return sqlSession.selectList("project.selectAllProjectList", userId);
    }

    public List<ProjectCardDto> selectMyProjectList(String userId) {

        return sqlSession.selectList("project.selectMyProjectList", userId);
    }

    public ProjectVo selectProject(String pid) {
        return sqlSession.selectOne("project.selectProject", pid);
    }

    public int selectMemberNo(String pid, String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pid", pid);
        map.put("userId", userId);
        Integer mem = sqlSession.selectOne("project.selectMemberNo", map);
        return (mem == null ? -1 : mem);
    }

    public List<TaskDto> selectTaskList(String pid, int memNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pid", pid);
        map.put("memNo", memNo);
        return sqlSession.selectList("project.selectTaskSuperList", map);
    }

    public List<TaskDto> selectTaskSubList(String taskSuperId, int memNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskSuperId", taskSuperId);
        // map.put("userId", userId);
        map.put("memNo", memNo);
        return sqlSession.selectList("project.selectTaskSubList", map);
    }

    public int insertAllCheckListItems(List<CheckListItemVo> items, int listNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("lno", listNo);
        map.put("itemList", items);
        return sqlSession.insert("project.insertAllCheckListItems", map);
    }

    public int insertNewLabel(LabelVo label) {
        return sqlSession.insert("project.insertNewLabel", label);
    }

    public List<LabelVo> getLabelList(String pid) {
        return sqlSession.selectList("project.getLabelList", pid);
    }

    public List<MemberDto> getMemberList(String pid) {
        return sqlSession.selectList("project.getMemberList", pid);
    }

    public TaskDto getTask(String taskId) {
        if (taskId.startsWith("TP")) {
            return sqlSession.selectOne("project.getSuperTask", taskId);
        } else {
            return sqlSession.selectOne("project.getSubTask", taskId);
        }
    }

    public List<FileVo> getFiles(String taskId) {
        return sqlSession.selectList("project.getFiles", taskId);
    }

    public Object updateSortNo(TaskDto targetTask) {
        return sqlSession.update("project.updateSortNo", targetTask);
    }

    public int insertComment(CommentVo comment) {
        return sqlSession.insert("comment.insertComment", comment);
    }

    public int deleteComment(int commentNo) {
        return sqlSession.delete("comment.deleteComment", commentNo);
    }

    public List<CommentVo> selectComments(String taskId) {
        return sqlSession.selectList("comment.selectComments", taskId);
    }

    public int insertStarredTask(StarredTaskVo starred) {
        sqlSession.insert("project.insertStarred", starred);
        return starred.getStarId();
    }

    public int deleteStarred(int starId) {
        return sqlSession.delete("project.deleteStarred", starId);
    }

    public int insertCheckList(CheckListVo checkList) {
        return sqlSession.insert("project.insertCheckList", checkList);
    }

    public int insertCheckList(CheckListDto checkList) {
        return sqlSession.insert("project.insertCheckList", checkList);
    }

    public int insertCheckList(CheckListItemVo checkListItem) {
        return sqlSession.insert("project.insertCheckListItem", checkListItem);
    }

    public List<CheckListDto> selectCheckList(String taskId) {
        return sqlSession.selectList("project.selectCheckList", taskId);
    }

    public int updateCheckListRate(int listNo) {
        return sqlSession.update("project.updateCheckListRate", listNo);
    }

    public int updateCheckListItem(int itemNo) {
        return sqlSession.update("project.updateCheckListItem", itemNo);
    }

    public int deleteCheckListItem(int itemNo) {
        return sqlSession.delete("project.deleteCheckListItem", itemNo);
    }

    public int deleteCheckList(int listNo) {
        return sqlSession.delete("project.deleteCheckList", listNo);
    }

    public Object updateProject(ProjectDto projectDto) {
        return sqlSession.update("project.updateProject", projectDto);
    }

    public int updateProgressRate(String projectId) {
        return sqlSession.update("project.updateProgressRate", projectId);
    }

    public int insertReport(ReportDto report) {
        return sqlSession.insert("project.insertReport", report);
    }

    public List<ReportDto> selectSendReport(String userId) {
        return sqlSession.selectList("project.selectSendReport", userId);
    }

    public List<ReportDto> selectReceiveReport(String userId) {
        return sqlSession.selectList("project.selectReceiveReport", userId);
    }

    public ReportDto selectReportDetail(int rid) {
        return sqlSession.selectOne("project.selectReportDetail", rid);
    }

}