package area.modules.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import com.dropbox.core.v2.files.UploadErrorException;

import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.io.FileNotFoundException;

/*
  getAccountNameRevert() : Return user's Surname + ' ' + Name
  getAccountName() : Return user's Name + ' ' + Surname
  getAccountFiles() : Return folder's file (in a Vector<String>)
  askUploadFile() : Ask to the user if he wants to upload a file. (if yes -> upload file)
  uploadFile(String fileName) : Upload the file [fileName]
  getVar(String msg) : Ask to standard input (with message msg) and return
*/

public class                DropboxModule {
  public static final String  m_app_id = "nmipoj4h2q4rmgt";
  public static final String  m_app_secret = "1yb5p0kw2jtb5lu";
  public static final String  m_redirectUrl = "http://localhost:8080/dropboxFinishAuth";
  private Scanner             m_sc = new Scanner(System.in);
  private DbxRequestConfig    m_config;
  private DbxClientV2         m_client;

  public DropboxModule(String token) throws DbxException, IOException {
    m_config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
    m_client = new DbxClientV2(m_config, token);
  }

  public String   getAccountNameRevert() throws Exception {
    FullAccount   account = m_client.users().getCurrentAccount();
    String        name = account.getName().getDisplayName();
    String[]      names = name.split(" ");

    return (new String(names[1] + " " + names[0]));
  }

  public String   getAccountName() throws Exception {
    FullAccount   account = m_client.users().getCurrentAccount();

    return (account.getName().getDisplayName());
  }

  public Vector<String> getAccountFiles() throws Exception {
    ListFolderResult    result = m_client.files().listFolder("");
    Vector<String>      files = new Vector<String>();

    while (true) {
      for (Metadata metadata : result.getEntries()) {
        files.add(metadata.getPathLower());
      }

      if (!result.getHasMore()) {
          break;
      }
    }
    return (files);
  }

  public void   askUploadFile() throws Exception {
    boolean loop = true;

    while (loop) {
      switch (getVar("Do you want to upload a file ? [y/n]")) {
        case "YES":
        case "yes":
        case "y":
        case "Y":
          try {
            uploadFile(getVar("Enter file's name :"));
            loop = false;
          } catch (FileNotFoundException e) {
            System.out.println("The file is a directory or doesn't exist.");
          } catch (UploadErrorException e) {
            System.out.println("Upload error. Maybe the file you selected is already in the folder.");
          }
          break;
        case "NO":
        case "no":
        case "N":
        case "n":
          loop = false;
          break;
        default:
          System.out.println("Please answer yes or no.");
          break;
      }
    }
  }

  public void   uploadFile(String fileName) throws Exception {
    InputStream in = new FileInputStream(fileName);
    FileMetadata metadata = m_client.files().uploadBuilder("/" + fileName).uploadAndFinish(in);

    System.out.println("Your file has been uploaded !");
    // catch (Exception e) {
    //   System.out.println("Error while uploading file : maybe it doesn't exist on your computer, or it already exist on Dropbox.");
    // }
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
