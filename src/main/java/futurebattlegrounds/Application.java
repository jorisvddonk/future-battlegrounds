package futurebattlegrounds;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "Future Battlegrounds", version = "0.0", description = "Future Battlegrounds API"))
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}