package tech.crm.crmserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tech.crm.crmserver.dao.Event;
import tech.crm.crmserver.dao.ToDoList;

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
public interface EventMapper extends BaseMapper<Event> {

    @Select("SELECT * " +
            "FROM event " +
            "WHERE user_id = #{userId} " +
            "ORDER BY FIELD(status,'active','done','deleted'), start_time")
    public List<Event> getEventsByUserId(@Param("userId") Integer userId);

}
