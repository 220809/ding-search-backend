package com.dingzk.dingsearch.data;

import com.dingzk.dingsearch.data.impl.UserDataResource;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDataResourceConfig implements DataResourceConfigurer {
    @Resource
    private UserDataResource userDataResource;

    @Override
    @Autowired
    public void addDataResource(DataResourceRegistry registry) {
        registry.addDataResource(userDataResource);
    }
}
