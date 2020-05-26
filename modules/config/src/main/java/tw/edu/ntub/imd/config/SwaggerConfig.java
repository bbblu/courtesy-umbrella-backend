package tw.edu.ntub.imd.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class SwaggerConfig {
    private final BuildProperties buildProperties;

    @Autowired
    public SwaggerConfig(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(getInfo())
                .addServersItem(getDevelopmentServer())
                .addServersItem(getProductionServer())
                .components(getComponents());
    }

    private Info getInfo() {
        return new Info().title("愛心傘租借系統 - API")
                .description("此為所有系統API功能列表，如有疑問，請洽負責人員\n" +
                        "\n" +
                        "以下為標準回傳格式，data請替換成API的RequestBody\n" +
                        "```json\n" +
                        "{\n" +
                        "    \"result\": boolean,\n" +
                        "    \"errorCode\": string,\n" +
                        "    \"message\": string,\n" +
                        "    \"data\": (參考RequestBody)\n" +
                        "}\n" +
                        "```")
                .version(buildProperties.getVersion())
                .contact(new Contact().name("李恩瑋").email("10646007@ntub.edu.tw"));
    }

    private Server getDevelopmentServer() {
        return new Server().url("http://211.75.1.201:50004").description("測試機");
    }

    private Server getProductionServer() {
        return new Server().url("http://211.75.1.201:50004").description("正式機");
    }

    private Components getComponents() {
        return new Components();
    }

    @Bean("defaultOpenApiCustomiser")
    public OpenApiCustomiser customerGlobalHeaderOpenApiCustomiser() {
        return openApi -> openApi.getPaths().values().parallelStream().map(PathItem::readOperations).flatMap(Collection::stream).forEach(operation -> {
            ApiResponses apiResponses = operation.getResponses();
            apiResponses.addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), new ApiResponse()
                    .description("請求缺少必要參數，請檢查Path/Query/Parameters/RequestBody")
            ).addApiResponse(String.valueOf(HttpStatus.UNAUTHORIZED.value()), new ApiResponse()
                    .description("使用者尚未登入")
            ).addApiResponse(String.valueOf(HttpStatus.FORBIDDEN.value()), new ApiResponse()
                    .description("使用者無權限使用此API")
            ).addApiResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), new ApiResponse()
                    .description("伺服器錯誤，請聯繫負責人員")
            );
        });
    }

    @Bean("userApiCustomiser")
    public OpenApiCustomiser userApiCustomiser(@Qualifier("defaultOpenApiCustomiser") OpenApiCustomiser openApiCustomiser) {
        return openApi -> {
            Paths paths = new Paths();
            Map<String, Schema> userLoginProperties = new LinkedHashMap<>();
            userLoginProperties.put("account", new Schema().type("string").description("帳號"));
            userLoginProperties.put("password", new Schema().type("string").description("密碼"));
            paths.addPathItem("/login", new PathItem().post(
                    new Operation()
                            .tags(Collections.singletonList("User"))
                            .summary("登入")
                            .requestBody(new RequestBody()
                                    .description("帳號與密碼")
                                    .required(true)
                                    .content(new Content()
                                            .addMediaType(APPLICATION_JSON_VALUE, new MediaType()
                                                    .schema(new Schema<>().type("object").properties(userLoginProperties))
                                            )
                                    )
                            )
                            .responses(new ApiResponses()
                                    .addApiResponse(String.valueOf(HttpStatus.OK.value()), new ApiResponse()
                                            .addHeaderObject("X-Auth-Token", new Header().schema(new Schema<>().type("string")).description("JWT Token"))
                                            .content(new Content().addMediaType(APPLICATION_JSON_VALUE, new MediaType().schema(new Schema<>().$ref("User"))))
                                    )
                            )
            ));
            openApi.getPaths().forEach(paths::addPathItem);
            openApi.setPaths(paths);
            openApiCustomiser.customise(openApi);
        };
    }

    @Bean
    public GroupedOpenApi userApi(@Qualifier("userApiCustomiser") OpenApiCustomiser openApiCustomiser) {
        return GroupedOpenApi.builder()
                .setGroup("使用者 - User")
                .packagesToScan("tw.edu.ntub.imd.courtesyumbrella.controller")
                .pathsToMatch("/user/**", "/login")
                .addOpenApiCustomiser(openApiCustomiser)
                .build();
    }
}
