package fr.univlyon3.sncf.transverse;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration("h2Configuration")
public class H2Configuration {

        @Bean(initMethod = "start", destroyMethod = "stop")
        public Server h2TcpServer() throws SQLException {
            return Server.createTcpServer(
                    "-tcp",
                    "-tcpAllowOthers",
                    "-tcpPort", "9092"
            );
        }

}
