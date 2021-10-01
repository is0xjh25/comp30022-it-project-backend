package tech.crm.crmserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tech.crm.crmserver.dao.ToDoList;
import tech.crm.crmserver.dto.UserPermissionDTO;

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
            "ORDER BY FIELD(status,'in progress','to do','done')")
    public List<ToDoList> getTodoListDataByUserId(@Param("userId") Integer userId);

}
