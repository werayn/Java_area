package area.modules.twitter;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;

import java.net.URL;
import static java.lang.System.exit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;
import java.util.function.Supplier;

public class TwitterModule {

    private OAuth10aService service;
    private OAuth1RequestToken requestToken;
    private OAuth1AccessToken accessToken;
    private String username;
    private long lastHashtagId;
    private long lastTagId;
    private String hasthag;
    private String verifier;
    public Map<String, Function<Vector<String>, Integer>> responses = new HashMap<>();
    public Map<String, Supplier<Vector<String>>> actions = new HashMap<>();

    public TwitterModule(String hasthag)
    {
        this.verifier = null;
        this.hasthag = hasthag;
        this.lastHashtagId = 0;
        this.lastTagId = 0;
        this.service = new ServiceBuilder("RZDQDfBwg4dWD81fJCaWl3Rc0")
                .apiSecret("c4ijCJPDntJVpPcVlIo4o2GF1SPcA9QX0zbp92kJrZwdeK3bVe")
                .build(TwitterApi.instance());
    }

    public TwitterModule()
    {
        this("Area2017");

        // Populate commands map
        actions.put("getTag", this::getTag);
        actions.put("getHashtag", this::getHashtag);
        responses.put("likeTweet", this::likeTweet);
        responses.put("retweetTweet", this::retweetTweet);
    }

    public void setHashtag(String hasthag) {
        this.hasthag = hasthag;
    }

    // Retourne un vecteur de string contenant les ids des tweet contenant le Hashtag
    public Vector<String> getHashtag()
    {
        // System.out.println("In getHashTag");
        final OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/search/tweets.json?q=%23" + this.hasthag + "&rpp=100&include_entities=true&result_type=mixed");
        this.service.signRequest(this.accessToken, request);
        Vector<String> vector = new Vector<String>();
        try {
            final Response response = this.service.execute(request);
            // System.out.println(response.getBody());
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response.getBody());
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray items =  (JSONArray) jsonObject.get("statuses");
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = (JSONObject) items.get(i);
                if (i == 0)
                    this.lastHashtagId = (long)item.get("id");
                vector.add(item.get("id").toString());
            }
        }
        catch (Exception e)
        {
            // System.out.println("Caught Exception in checkHashtag" + e);
            exit(-1);
        }
        return vector;
    }

    // Retourne un vecteur de string contenant les ids des tweet mentionnant l'utilisateur
    public Vector<String> getTag()
    {
        // System.out.println("In getTag");
        final OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/search/tweets.json?q=%40" + this.username + "&rpp=5&include_entities=true&with_twitter_user_id=true&result_type=mixed&since_id=" + this.lastTagId);
        this.service.signRequest(this.accessToken, request);
        Vector<String> ids = new Vector<String>();
        try {
            final Response response = this.service.execute(request);
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response.getBody());
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray items =  (JSONArray) jsonObject.get("statuses");
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = (JSONObject) items.get(i);
                if (i == 0)
                    this.lastTagId = (long)item.get("id");
                ids.add(item.get("id").toString());
            }
        }
        catch (Exception e)
        {
            // System.out.println("Caught Exception in checkHashtag" + e);
            exit(-1);
        }
        return ids;
    }

    // Prend en parametre un vecteur de String des ids des tweets à like
    public Integer likeTweet(Vector<String> ids)
    {
        // System.out.println("In likeTweet");
        for (int i = 0; i < ids.size(); i++)
        {
            try {
                final OAuthRequest like = new OAuthRequest(Verb.POST,"https://api.twitter.com/1.1/favorites/create.json?id=" + ids.elementAt(i) + "&include_entities=true");
                this.service.signRequest(this.accessToken, like);
                final Response response1 = this.service.execute(like);
                // System.out.println("check hashtag code = " + response1.getCode());
            }
            catch (Exception e)
            {
                exit(-1);
            }
        }
        return 0;
    }

    // Prend en parametre un vecteur de String des ids des tweets à retweet
    public Integer retweetTweet(Vector<String> ids)
    {
        // System.out.println("In retweetTweet");
        for (int i = 0; i < ids.size(); i++)
        {
            try {
                final OAuthRequest like = new OAuthRequest(Verb.POST, "https://api.twitter.com/1.1/statuses/retweet/" + ids.elementAt(i) + ".json");
                this.service.signRequest(this.accessToken, like);
                final Response response = this.service.execute(like);
                // System.out.println("check tag code = " + response.getCode());
            }
            catch (Exception e)
            {
                exit(-1);
            }
        }
        return 0;
    }


    public String  getLink()
    {
        URL url = null;
        try {
            this.requestToken = this.service.getRequestToken();
            url = new URL(this.service.getAuthorizationUrl(this.requestToken));
        }
        catch (Exception e)
        {
            exit(-1);
        }
        return(url.toString());
    }

    public void setVerifier(String verifier)
    {
        this.verifier = verifier;
        try {
            this.accessToken = this.service.getAccessToken(this.requestToken, verifier);
        }
        catch (Exception e) {
            exit(-1);
        }
        String raw = this.accessToken.getRawResponse();
        raw = raw.substring(raw.indexOf("screen_name=") + 12);
        this.username = raw.substring(0, raw.indexOf('&'));
    }

    public void run() {
        if (this.verifier == null)
        {
            // System.out.println("VERIFIER PAS SET CALL SETVERIFIER AVANT");
            exit(-1);
        }
        // Pour retweet les tweet ou le user est mentionné
        // this.retweetTweet(this.getTag());
        // Pour like les tweet ou le hashtag est présent
        // this.likeTweet(this.getHashtag());
        // Vector<String> action = actions.get(0);

        // System.out.println("TwitterModule::run");
        Supplier<Vector<String>> action = actions.get("getTag");
        Function<Vector<String>, Integer> response = responses.get("retweetTweet");
        Vector<String> v = action.get();
        Integer r = response.apply(v);
    }
}
