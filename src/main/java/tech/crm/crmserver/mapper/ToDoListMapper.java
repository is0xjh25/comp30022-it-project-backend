package tech.crm.crmserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tech.crm.crmserver.dao.ToDoList;
import tech.crm.crmserver.dto.TaskStatDTO;
import tech.crm.crmserver.dto.UserPermissionDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@Repository
public interface ToDoListMapper extends BaseMapper<ToDoList> {

    @Select("SELECT * " +
            "FROM to_do_list " +
            "WHERE user_id = #{userId} " +
            "ORDER BY FIELD(status,'in progress','to do','done', date_time)")
    public List<ToDoList> getTodoListDataByUserId(@Param("userId") Integer userId);

    @Select("SELECT COUNT(if(status='to do', 1, null)) to_do, COUNT(if(status='in progress', 1, null)) in_progress, COUNT(if(status='done', 1, null)) done\n" +
            "FROM to_do_list " +
            "where user_id = #{userId} AND " +
            "(date_time > #{a} AND date_time < #{b}) ")
    public TaskStatDTO getStat(@Param("userId") Integer userId, @Param("a") LocalDateTime startTime, @Param("b") LocalDateTime finishTime);

}
