package tech.crm.crmserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tech.crm.crmserver.dao.Event;
import tech.crm.crmserver.dao.ToDoList;

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
public interface EventMapper extends BaseMapper<Event> {

    @Select("SELECT * " +
            "FROM event " +
            "WHERE user_id = #{userId} " +
            "ORDER BY FIELD(status,'to do', 'in progress', 'done'), start_time")
    public List<Event> getEventsByUserId(@Param("userId") Integer userId);

    @Select("select * " +
            "from event " +
            "where user_id = #{userId} AND" +
            "((start_time > #{a} AND start_time < #{b}) OR (start_time < #{a} AND finish_time > #{b}) OR (finish_time > #{a} AND finish_time < #{b}))")
    public List<Event> getEventsBetween(@Param("userId") Integer userId, @Param("a") LocalDateTime startTime, @Param("b") LocalDateTime finishTime);

    @Select("select event.start_time " +
            "from event " +
            "where user_id = #{userId}")
    public List<LocalDateTime> getStartTime(@Param("userId") Integer userId);
}
