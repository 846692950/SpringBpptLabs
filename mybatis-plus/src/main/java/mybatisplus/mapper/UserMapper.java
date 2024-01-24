package mybatisplus.mapper;

import mybatisplus.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ypw
 * @since 2024-01-24
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
