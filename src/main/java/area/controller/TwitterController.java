package area.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import area.Client;

@Controller
public class TwitterController {

    @RequestMapping("/twitter")
    public String twitter(@RequestParam(value="token", required=false) String token, Model model) {
      try {
        // Twitter Authentification
        if (token == null) {
          String redirect = Client.twitterModule.getLink();
          System.out.println("Redirect = " + redirect + "token = " + token);
          model.addAttribute("linkTwitter", redirect);
        }
        else {
          System.out.println("VERIFIER = <" + token +">");
          Client.twitterModule.setVerifier(token);
          Client.run();
        }
      } catch (Exception e) {
        System.out.println("Error : fct twitter");
      }
      return ("twitter");
    }

    @RequestMapping("/likeTweetAboutMe")
    public String likeTweetAboutMe() {
      Client.modules.add("getTag");
      Client.modules.add("likeTweet");
      return ("twitter");
    }

    @RequestMapping("/retweetTweetAboutMe")
    public String retweetTweetAboutMe() {
      Client.modules.add("getTag");
      Client.modules.add("retweetTweet");
      return ("twitter");
    }

    @RequestMapping("retweetThat")
    public String retweetThat(@RequestParam(value="hashtag", required=false) String hashtag, Model model) {
      Client.twitterModule.setHashtag(hashtag);
      Client.modules.add("getHashtag");
      Client.modules.add("retweetTweet");
      return ("twitter");
    }

    @RequestMapping("likeThat")
    public String likeThat(@RequestParam(value="hashtag", required=false) String hashtag, Model model) {
      Client.twitterModule.setHashtag(hashtag);
      Client.modules.add("getHashtag");
      Client.modules.add("likeThat");
      return ("twitter");
    }
}
