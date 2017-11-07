package area.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import area.modules.facebook.FacebookModule;

@Controller
public class FacebookController {
  public static final String m_app_id = "161094664483293";
  public static final String m_app_secret = "2def24ad8ab2d6aed07b61af404d1bc6";
  public static final String m_redirectUrl = "http://localhost:8080/facebook";
  public FacebookModule m_fbModule;

    @RequestMapping("/facebook")
    public String facebook(@RequestParam(value="code", required=false) String code, Model model) {
        // model.addAttribute("code", name);
        // System.out.println("Code : " + code);
        return "welcome";
    }
}
