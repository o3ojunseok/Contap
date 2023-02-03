package com.project.contap.common;

import com.project.contap.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RunWith implements ApplicationRunner {

    private final UserService userService;
    private final Common common;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userService.ActiveUsercntSet();
        if(args.getNonOptionArgs().size() > 0 && args.getNonOptionArgs().get(0).equals("redisSet"))
            common.DbinfoToRedis();
    }
}
