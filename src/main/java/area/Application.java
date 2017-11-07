package area;

import java.util.Set;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.mongodb.*;
import area.data.DataManager;

@SpringBootApplication
public class Application {
	public static DataManager m_bdd = new DataManager();

    public static void main(String[] args) {
    	SpringApplication.run(Application.class, args);
    	Client client = new Client();
    }

}
