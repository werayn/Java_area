package area.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class WelcomeController {
    @RequestMapping("/")
    public String Welcome() {
        return "redirect:/welcome";
    }

    @RequestMapping("/welcome")
    public String WelcomePage() {
        return "welcome";
    }

    @RequestMapping("/index")
    public String index() {
        return "redirect:/welcome";
    }

    @RequestMapping("/login")
    public String Login() {
        return "login";
    }

  @RequestMapping("/intra")
    public String Intra() {
        return "intra";
    }

  @RequestMapping("/contact")
    public String Contact() {
        return "contact";
    }

  @RequestMapping("/connect")
    public String Connect() {        
        return "connect";
    }

  @RequestMapping("/register")
    public String register() {
        return "register";
    }

}