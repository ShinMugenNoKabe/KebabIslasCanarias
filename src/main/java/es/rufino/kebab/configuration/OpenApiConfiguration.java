package es.rufino.kebab.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Kebab Islas Canarias",
                        email = "admin@gmail.com",
                        url = "https://github.com/ShinMugenNoKabe/KebabIslasCanarias"
                ),
                description = "Kebab store",
                title = "Kebab Islas Canarias",
                version = "1.0.0"
        ),
        servers = {
                @Server(description = "Local Server", url = "http://localhost:9000")
        }
//        This is used if we want to ensure all requests need the Bearer token
//        We don't want this because the register and login endpoints must be public
//        security = {
//                @SecurityRequirement(
//                        name = "bearerAuth"
//                )
//        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Bearer Authentication with JWT Tokens",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {
}
