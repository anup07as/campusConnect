package CampusConnect;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class HelloController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello from CampusConnect Backend!";
    }
}