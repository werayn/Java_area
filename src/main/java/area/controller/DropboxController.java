package area.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Vector;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxStandardSessionStore;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import area.modules.dropbox.DropboxModule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class DropboxController {
  public DbxWebAuth m_auth;
  public String     m_token = new String("\0");

  DropboxController() {
    try {
      DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
      DbxAppInfo appInfo = new DbxAppInfo(DropboxModule.m_app_id, DropboxModule.m_app_secret);
      m_auth = new DbxWebAuth(config, appInfo);
    } catch (Exception e) {
      System.out.println("Error : DropboxController<init>");
    }
  }

    @RequestMapping("/dropboxAuth")
    public String dropboxAuth(Model model, HttpServletRequest request) {
      try {
        HttpSession session = request.getSession(true);
        String sessionKey = "dropbox-auth-csrf-token";
        DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, sessionKey);

        DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder().withRedirectUri(DropboxModule.m_redirectUrl, csrfTokenStore).build();

        return "redirect:" + m_auth.authorize(authRequest);
      } catch (Exception e) {
        System.out.println("Error : fct dropboxAuth");
      }
      return ("index");
    }

    @RequestMapping("/dropboxFinishAuth")
    public String dropboxFinishAuth(Model model, HttpServletRequest request) {
      try {
        // Fetch the session to verify our CSRF token
        HttpSession session = request.getSession(true);
        String sessionKey = "dropbox-auth-csrf-token";
        DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, sessionKey);

        DbxAuthFinish authFinish = m_auth.finishFromRedirect(DropboxModule.m_redirectUrl, csrfTokenStore, request.getParameterMap());
        m_token = authFinish.getAccessToken();
        } catch (Exception e) {
          System.out.println("Error : fct dropboxFinishAuth");
        }
        return "redirect:/dropbox";
    }

    @RequestMapping("/dropbox")
    public String dropbox(Model model) {
      if (m_token.equals("\0"))
        return ("dropbox");
      try {
        DropboxModule moduleDrop = new DropboxModule(m_token);
        String toPrint = new String("\0");

        toPrint = "Hello " + moduleDrop.getAccountNameRevert() + " ! :)<br/>What we found in your Dropbox folder :<br/>";
        Vector<String> files = moduleDrop.getAccountFiles();
        for (int i = 0; i < files.size(); i++)
          toPrint += files.get(i) + "<br/>";
        model.addAttribute("dropboxResult", toPrint);
      } catch (Exception e) {
        System.out.println("Error : fct dropbox");
        model.addAttribute("dropboxResult", "Error while login to Dropbox");
      }
      return "dropbox";
    }

}
