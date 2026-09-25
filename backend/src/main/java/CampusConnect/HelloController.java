package CampusConnect;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = { "https://campusconnect-web-me6v.onrender.com", "https://campusconnect-dva.pages.dev" })
public class HelloController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello from CampusConnect Backend!";
    }
}