package au.com.xhost.rightscale.api.client;


/**
 * Basic test of RightScaleAPIClient
 *
 */
public class RightScaleAPIClientTest
{
    public static void main( String[] args )
    {
        RightScaleAPIClient rightscale;
        rightscale = new RightScaleAPIClient("rick@james.com", "charliemurphy", "1456", "1.0", "us-4.rightscale.com");
        rightscale.enableDebug();
        rightscale.login();
        System.out.print("Logged in "+rightscale.getUserName());
        System.out.print(rightscale.getServers());
        System.out.print(rightscale.getRequest("/deployments"));
    }
}
