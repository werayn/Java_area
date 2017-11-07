package area.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import area.data.DataManager;
import area.Application;

@Controller
public class RegisterController {
  @RequestMapping("/registerAction")
  public String registerAction(@RequestParam(value="email", required=true) String email,
  @RequestParam(value="pwd", required=true) String pwd1,
  @RequestParam(value="pwdConfirm", required=true) String pwd2, Model model) {
    if (!pwd1.equals(pwd2)) {
      model.addAttribute("registerMessage", "Les 2 mots de passe ne sont pas identique !");
      return ("register");
    }
    if (Application.m_bdd.add_user(email, pwd1) == -1) {
      model.addAttribute("registerMessage", "Cette adresse email est déjà utilisée !");
      return ("register");
    }
    model.addAttribute("registerMessage", "Merci de votre inscription !");
    return ("register");
  }
}
