package log;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LogApplicationTests {

    private static Logger logger = LoggerFactory.getLogger(LogApplicationTests.class);

    @Test
    void contextLoads() {
        logger.debug("这是一个debug");
        logger.info("这是一个info");
        logger.error("这是一个error");
    }

}
