package area;

import area.modules.twitter.TwitterModule;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public class Client {
    public static TwitterModule twitterModule = new TwitterModule();
    public static ArrayList modules = new ArrayList();



    public Client() {

    }

    static public void run() {
        while (true) {
            try {
                for (int i = 0; i < modules.size(); i = i + 2) {
                    System.out.println(i +  " = " + modules.get(i));
                    System.out.println(i+1 +  " = " + modules.get(i+1));
                    Supplier<Vector<String>> action = twitterModule.actions.get(modules.get(i));
                    Function<Vector<String>, Integer> response = twitterModule.responses.get(modules.get(i + 1));
                    Vector<String> v = action.get();
                    response.apply(v);
                }
                TimeUnit.SECONDS.sleep(10);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
