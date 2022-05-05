package es.mybi.demo;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Sql(
        scripts = {
                "classpath:sql/cleanDB.sql",
                "classpath:sql/createDB.sql",
        },
        config = @SqlConfig(
                encoding = "utf8"
        )
)
public @interface MemoryDatabaseConfig {
}
