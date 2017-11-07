package area.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import area.data.DataManager;
import area.Application;

@Controller
public class ConnectController {
  @RequestMapping("/connectAction")
  public String controllerAction(@RequestParam(value="email", required=false) String email,
  @RequestParam(value="pwd", required=false) String pwd, Model model) {
    if (Application.m_bdd.check_user(email, pwd) == -1) {
      model.addAttribute("connectMessage", "Adresse mail ou mot de passe incorrect.");
      return ("connect");
    }
    model.addAttribute("connectMessage", "Vous êtes connecté !");
    return ("connect");
  }
}
