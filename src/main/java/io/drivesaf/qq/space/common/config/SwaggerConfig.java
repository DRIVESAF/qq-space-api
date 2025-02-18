package io.drivesaf.qq.space.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: DRIVESAF
 * @createTime: 2024/11/15 16:24
 * @description:
 **/
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("QQ 空间 API")
                        .contact(new Contact().name("drivesaf").email("axao.@qq.com"))
                        .version("1.0")
                        .description("QQ 空间 API 接口文档")
                        .license(new License().name("Apache 2.0").url("http://doc.xiaominfo.com"))
                );
    }
}
