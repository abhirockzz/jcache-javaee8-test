package test.jcachejavaee8bridge;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.enterprise.cache.CacheContext;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("{jsrNum}")
public class JavaEE8Resource {

    @Inject
    @CacheContext("jsrinfocache")
    Cache<String, String> cache;
            
    @PostConstruct
    public void init() {
        System.out.println(JavaEE8Resource.class.getName() + " constructed successfully on " + new Date().toString());
        System.out.println("Cache name: " + cache.getName());
    }

    @GET
    //@Path("mailinglist")
    public Response getinfo(@PathParam("jsrNum") String jsrNum) {
        try {
            validate(jsrNum);
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage() 
                        + "\n Want more? Visit http://www.adam-bien.com/roller/abien/entry/the_ingredients_of_java_ee").build();
        }
        
        String resp = null;
        if(!cache.containsKey(jsrNum)){
            resp = "Visit https://jcp.org/en/jsr/detail?id=" + jsrNum ;
            cache.put(jsrNum, resp);
        }else{
            resp = "Don't worry. I have this cached! \n" + cache.get(jsrNum);
        }
        
        return Response.ok(resp).build(); 

    }
    
    private void validate(String jsrNum){
        List<String> validJSRs = Arrays.asList("366", "367", "371", "373",
                                                "375", "372", "370", "369",
                                                 "368", "365", "371", "373");
        
        
        if(!validJSRs.contains(jsrNum)){
            throw new RuntimeException("Invalid JSR specified. Here is the valid list - " + validJSRs);
        }
    }
}
