package area.modules.facebook;

import java.util.Scanner;
import java.util.List;

import com.restfb.FacebookClient;
import com.restfb.DefaultFacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;
import com.restfb.types.FacebookType;

/*
  getVar(String msg) : Ask to standard input (with message msg) and return
*/

public class                FacebookModule {
  private Scanner           m_sc = new Scanner(System.in);
  private FacebookClient    m_client;
  private User              me;

  public FacebookModule(String token) {
    m_client = new DefaultFacebookClient(token);

    me = m_client.fetchObject("me", User.class, Parameter.with("fields", "email,birthday"));
    System.out.println(getInfo());
    // publish("Hello world\nAll right ?");
  }

  public void     publish(String str) {
    try {
      System.out.println("Publishing : \"" + str + "\" on Facebook ...");
      m_client.publish("me/feed", FacebookType.class, Parameter.with("message", str));
      System.out.println("Published !");
    } catch (Exception e) {
      System.out.println("Error while publishing on Facebook (maybe you've already publish this ?).");
    }
  }

  public String   getInfo() {
    String        str = new String("\0");

    str += "Name : " + me.getName() + '\n';
    str += "Email : " + me.getEmail() + '\n';
    str += "Birthday : " + getBirthdayFr();
    return (str);
  }

  public String   getName() {
    return (me.getName());
  }

  public String   getEmail() {
    return (me.getEmail());
  }

  public String getBirthdayFr() {
    String[]    birth = me.getBirthday().split("/");
    String      str = new String("\0");

    str = birth[1] + "/" + birth[0];
    if (birth.length == 3)
      str +=  "/" + birth[2];
    return (str);
  }

	private String getVar(String txt) {
		boolean	ok = false;
		String	r = "\0";

		while (ok != true) {
			System.out.println(txt);
			while (!m_sc.hasNextLine());
			r = m_sc.nextLine();
			if (r.isEmpty() == false)
				ok = true;
		}
		return (r);
	}
}
