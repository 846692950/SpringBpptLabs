package mybatisplus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MyBatisPlusConf implements MetaObjectHandler {

    // 插入时的填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入数据！");
        if (metaObject.hasGetter("createTime")) {
            this.setFieldValByName("createTime", new Date(), metaObject);
        }
        if (metaObject.hasGetter("updateTime")) {
            this.setFieldValByName("updateTime", new Date(), metaObject);
        }
        if (metaObject.hasGetter("updateTime")) {
            this.setFieldValByName("deleted", 0, metaObject);
        }

    }

    // 更新时的填充策略
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新数据！");
        if (metaObject.hasGetter("updateTime")) {
            this.setFieldValByName("updateTime", new Date(), metaObject);
        }
    }

}
