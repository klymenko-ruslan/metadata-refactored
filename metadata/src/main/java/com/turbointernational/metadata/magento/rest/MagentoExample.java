//package com.turbointernational.metadata.magento.rest;
//
//import java.util.Scanner;
//
//import org.scribe.builder.ServiceBuilder;
//import org.scribe.model.OAuthRequest;
//import org.scribe.model.Response;
//import org.scribe.model.Token;
//import org.scribe.model.Verb;
//import org.scribe.model.Verifier;
//import org.scribe.oauth.OAuthService;
//
///**
// * Example test driver for the Magento17Api class - Magento Community 1.7.x and
// * Enterprise 1.12 support REST APIs going forward
// *
// * @see
// * http://www.magentocommerce.com/blog/the-magento-rest-api-a-better-way-to-integrate-business-applications/
// * @author Mike Salera
// */
//public class MagentoExample {
//    
//    static final String MAGENTO_API_KEY = "4wy124nq41hquo0z3hm8mm3uzaqrmte2";
//    static final String MAGENTO_API_SECRET = "m41dv0jtygqrl3cne1731pe5pglh5pel";
//    static final String MAGENTO_TOKEN = "8zp75lkc8zrm04jscylo0s3nbojae057";
//    static final String MAGENTO_TOKEN_SECRET = "4y791olryopwe8cfebxn6jij8apoerck";
//    static final String MAGENTO_REST_API_URL = "http://ec2-184-73-132-150.compute-1.amazonaws.com/api/rest/";
//
//    public static void main(String[] args) {
//        OAuthService service = new ServiceBuilder()
//                .provider(new Magento17Api("http://ec2-184-73-132-150.compute-1.amazonaws.com/api.php/"))
//                .apiKey(MAGENTO_API_KEY)
//                .apiSecret(MAGENTO_API_SECRET)
//                .build();
////        Scanner in = new Scanner(System.in);
////
////        System.out.println("=== Mage v1.7.0.2 OAuth Workflow ===");
////        System.out.println();
////
////        // Obtain the Request Token
////        System.out.println("Fetching the Request Token...");
////        Token requestToken = service.getRequestToken();
////        System.out.println("Got the Request Token!");
////        System.out.println();
////
////        System.out.println("Now go and authorize Scribe here:");
////        System.out.println(service.getAuthorizationUrl(requestToken));
////        System.out.println("And paste the verifier here");
////        System.out.print(">>");
////        Verifier verifier = new Verifier(in.nextLine());
////        System.out.println();
////
////        // Trade the Request Token and Verfier for the Access Token
////        System.out.println("Trading the Request Token for an Access Token...");
////        Token accessToken = service.getAccessToken(requestToken, verifier);
////        System.out.println("Got the Access Token!");
////        System.out.println("(if your curious it looks like this: " + accessToken + " )");
////        System.out.println();
////
////        // Now let's go and ask for a protected resource!
////        System.out.println("Now we're going to access a protected resource...");
//        
//        Token accessToken = new Token(MAGENTO_TOKEN, MAGENTO_TOKEN_SECRET);
//        OAuthRequest request = new OAuthRequest(Verb.GET, MAGENTO_REST_API_URL + "products?type=rest");
//        service.signRequest(accessToken, request);
//        Response response = request.send();
//        System.out.println(response.getBody());
//
//        System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
//    }
//}