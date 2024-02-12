package it.unisalento.pas.smartcitywastemanagement.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Configura il mapping CORS per tutti i percorsi
                .allowedOrigins("http://localhost:4200") // Consenti solo le richieste dalle origini specificate
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Consenti solo i metodi HTTP specificati
                .allowCredentials(true); // Consenti l'invio di credenziali (se necessario)
    }

}
