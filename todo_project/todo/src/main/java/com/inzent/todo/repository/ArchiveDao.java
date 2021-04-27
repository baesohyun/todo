package com.inzent.todo.repository;

import java.util.ArrayList;
import java.util.List;

import com.inzent.todo.dto.ArchiveSubDto;
import com.inzent.todo.dto.ArchiveSuperDto;
import com.inzent.todo.vo.RestoreSubVo;
import com.inzent.todo.vo.RestoreSuperVo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ArchiveDao {

    @Autowired
    private SqlSession sqlsession;

    public List<ArchiveSuperDto> getArchiveSupers(String userId) {

        return sqlsession.selectList("archive.getArchiveSupers", userId);
    }

    public List<ArchiveSubDto> getArchiveSubs(String userId) {
        return sqlsession.selectList("archive.getArchiveSubs", userId);
    }

    public int deleteSubTask(String subId) { // 하위업무 삭제됬을 때 관련 테이블 다 삭제
        int subCnt = sqlsession.delete("archive.delSub", subId);
        if (subCnt == 1) {
            String id = subId;
            sqlsession.delete("archive.delFile", id);
            sqlsession.delete("archive.delChkList", id);
            sqlsession.delete("archive.delChkListItem", id);
            sqlsession.delete("archive.delComment", id);
            sqlsession.delete("archive.delManager", id);
        }
        return subCnt;
    }

    public int deleteSuperTask(String superId) {// 상위업무 삭제됬을 때 관련 테이블 다 삭제
        List<String> archiveSuperId = sqlsession.selectList("archive.existArchiveSuperId", superId);
        List<String> taskSubIds = sqlsession.selectList("archive.existTaskSuperId", superId);
        int superCnt = sqlsession.delete("archive.delSuper", superId);
        int subCnt = 0;
        String id = superId;
        if (superCnt == 1) { // 상위업무가 삭제되었다면
            sqlsession.delete("archive.delFile", id);
            sqlsession.delete("archive.delChkList", id);
            sqlsession.delete("archive.delChkListItem", id);
            sqlsession.delete("archive.delComment", id);
            sqlsession.delete("archive.delManager", id);
            if (archiveSuperId.size() > 0) { // 아카이브 테이블에서 삭제
                subCnt = sqlsession.delete("archive.delArchiveSub", superId);
            } // end if
            if (taskSubIds.size() > 0) { // 관련 하위업무와 그 관련 테이블 다 삭제
                // taskSubIds = sqlsession.selectList("archive.getsubId", superId);
                for (String taskSubId : taskSubIds) {
                    subCnt = sqlsession.delete("archive.delTaskSub", taskSubId);
                    if (subCnt == 1) {
                        id = taskSubId;
                        sqlsession.delete("archive.delFile", id);
                        sqlsession.delete("archive.delChkList", id);
                        sqlsession.delete("archive.delChkListItem", id);
                        sqlsession.delete("archive.delComment", id);
                        sqlsession.delete("archive.delManager", id);
                    } // end if
                } // end for
            } // end if
            System.out.println("삭제 성공!!");
        } else {
            System.out.println("삭제 실패");
        } // end else
        return subCnt;
    }

    public int restoreSubTask(String subId) {
        // 해당 id의 업무정보를 조회하고 vo에 담는다.
        RestoreSubVo vo = new RestoreSubVo();
        vo = sqlsession.selectOne("archive.getRestoreSubId", subId);
        System.out.println("=====" + vo);
        // vo에 담은 정보를 다시 복구시켜준 후
        int insertCnt = sqlsession.insert("archive.restoreSub", vo);
        int delCnt = 0;
        if (insertCnt == 1) { // 복구가 성공하면 해당 테이블에서 지움
            delCnt = sqlsession.delete("archive.delSub", subId);
        } else {
            System.out.println("복구 실패...........");
        }
        return delCnt;
    }

    public int restoreSuperTask(String superId) {
        RestoreSuperVo vo = new RestoreSuperVo();
        System.out.println(superId);
        vo = sqlsession.selectOne("archive.getRestoreSuperId", superId);
        System.out.println("=====" + vo);
        int insertCnt = sqlsession.insert("archive.restoreSuper", vo);
        int delCnt = 0;
        if (insertCnt == 1) { // 복구가 성공하면 해당 테이블에서 지움
            delCnt = sqlsession.delete("archive.delSuper", superId);
        } else {
            System.out.println("복구 실패...........");
        }
        return delCnt;
    }

    public Object sendToArchive(String taskId) {
        return null;
    }

    public int sendToArchiveSuper(String taskId) {
        RestoreSuperVo vo = new RestoreSuperVo();
        vo = sqlsession.selectOne("archive.getTaskSuperId", taskId);
        System.out.println("SUper TASK -> ARCIVE =====");
        // vo에 담은 정보를 다시 복구시켜준 후
        int insertCnt = sqlsession.insert("archive.insertTaskSuper", vo);
        int delCnt = 0;
        if (insertCnt == 1) { // 복구가 성공하면 해당 테이블에서 지움
            delCnt = sqlsession.delete("archive.delSuperFromTask", taskId);
        } else {
            System.out.println("복구 실패...........");
        }
        return delCnt;
    }

    public int sendToArchiveSub(String taskId) {
        RestoreSubVo vo = new RestoreSubVo();
        vo = sqlsession.selectOne("archive.getTaskSubId", taskId);
        System.out.println("SUB TASK -> ARCIVE =====");
        // vo에 담은 정보를 다시 복구시켜준 후
        int insertCnt = sqlsession.insert("archive.insertTaskSub", vo);
        int delCnt = 0;
        if (insertCnt == 1) { // 복구가 성공하면 해당 테이블에서 지움
            delCnt = sqlsession.delete("archive.delSubFromTask", taskId);
        } else {
            System.out.println("복구 실패...........");
        }
        return delCnt;
    }
}