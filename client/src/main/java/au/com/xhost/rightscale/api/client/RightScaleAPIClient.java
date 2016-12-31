/**
 * RightScale HTTP API client
 */
package au.com.xhost.rightscale.api.client;

/**
 * @author Chris Fordham aka flaccid
 *
 */

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class RightScaleAPIClient {
	String userName;
	String userPassword;
	String accountID;
	String apiVersion;
	String endpoint;

	public static DefaultClientConfig config;
	public static Client client;
	public static WebResource resource;
	public static ClientResponse response;
	public static WebResource.Builder builder;

	private String loginHref;
	private String loginUri;
	private List<NewCookie> cookies;
	private MultivaluedMap<String, String> baseQueryParams = new MultivaluedMapImpl();

	public RightScaleAPIClient(String userName, String userPassword, String accountID, String apiVersion, String endpoint) {
		this.userName = userName;
		this.userPassword = userPassword;
		this.accountID = accountID;
		this.apiVersion = apiVersion;
		this.endpoint = endpoint;

		config = new DefaultClientConfig();
		client = Client.create(config);
	}
	
	/**
	 * Logins to the RightScale API
	 *
	 */
	public void login() {
		//System.out.print("API VERSION: " + apiVersion + "\r\n");

		if (apiVersion.equals("1.0")) {
			baseQueryParams.add("api_version", apiVersion);

			loginHref = "/api/acct/"+accountID+"/login";
			loginUri = "https://"+endpoint+loginHref;

			HTTPBasicAuthFilter basicAuth = new HTTPBasicAuthFilter(userName, userPassword);
			client.addFilter(basicAuth);

			WebResource webResource = client.resource(loginUri);
			ClientResponse response = webResource.queryParams(baseQueryParams).get(ClientResponse.class);
			
			// basic auth is not supported on subsequent calls
			client.removeFilter(basicAuth);

			// these may be used at some point
			// int status = response.getStatus();
			// String textEntity = response.getEntity(String.class);

			cookies = response.getCookies();
		} else if (apiVersion.equals("1.5")) {
			MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
			formData.add("account_href", "/api/accounts/"+accountID);
			formData.add("email", userName);
			formData.add("password", userPassword);
			loginHref = "/api/session";
			loginUri = "https://"+endpoint+loginHref;
			WebResource webResource = client.resource(loginUri);
			ClientResponse response = webResource.type("application/x-www-form-urlencoded")
					.header("X-API-VERSION", "1.5")
					.post(ClientResponse.class, formData);
			cookies = response.getCookies();
			// System.out.print("COOKIES NOM NOM: " + cookies + "\r\n");
		} else {
			throw new IllegalArgumentException("No API version specified.");
		}
	}

	/**
	 * Enables system debug output for the client
	 *
	 */
	public void enableDebug() {
		client.addFilter(new LoggingFilter(System.out));		
	}

	/**
	 * Generic GET request to the API
	 *
	 */
	public String getRequest(String href) {
		// keep in mind API 1.0 is now deprecated!
		String resourceUrl;
		if (apiVersion.equals("1.0")) {
			resourceUrl = "https://"+endpoint+"/api/acct/"+accountID+href;
		} else {
			resourceUrl = "https://"+endpoint+"/api"+href;
		}

		WebResource webResource = client.resource(resourceUrl);
		ClientResponse response = webResource
				// we need both cookies from the session
				.cookie(cookies.get(0))
				.cookie(cookies.get(1))
				.header("X-API-VERSION", apiVersion)
				.get(ClientResponse.class);
		return response.getEntity(String.class);
	}

	/**
	 * Return all RightScale servers from the API
	 *
	 */
	public String getServers() {
		return getRequest("/servers");
	}
    
	/**
	 * Destroys the HTTP connection object
	 *
	 */
	public void destroy() {
		try {        
			client.destroy();                     
		}
		catch (ClientHandlerException e)         
		{
			System.out.print("Failed to destroy Jersey client: " + e.getMessage());            
		}
	}

	/**
	 * Returns the cookies
	 *
	 */
	public List<NewCookie> getCookies() {
		return cookies;
	}

	/**
	 * Returns the username
	 *
	 */
	public String getUserName() {
		return userName;
	}
    
	/**
	 * Returns the password
	 *
	 */
	public String getUserPassword() {
		return userPassword;
	}
	
	/**
	 * Returns the API version
	 *
	 */
	public String getAPIVersion() {
		return apiVersion;
	}
	
	/**
	 * Returns the API endpoint
	 *
	 */
	public String getEndpoint() {
		return endpoint;
	}
}
