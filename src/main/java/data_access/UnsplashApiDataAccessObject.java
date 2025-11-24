package data_access;

import use_case.Sprites.Import.UnsplashImageDataAccessInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of UnsplashImageDataAccessInterface that communicates with the Unsplash API.
 * This class handles HTTP requests to fetch and download images from Unsplash.
 */
public class UnsplashApiDataAccessObject implements UnsplashImageDataAccessInterface {

    private static final String BASE_URL = "https://api.unsplash.com";
    private static final String SEARCH_ENDPOINT = "/search/photos";
    private static final String PHOTO_ENDPOINT = "/photos";

    private final String accessKey;

    /**
     * Constructor that creates an UnsplashApiDataAccessObject.
     *
     * @param accessKey the Unsplash API access key
     */
    public UnsplashApiDataAccessObject(String accessKey) {
        if (accessKey == null || accessKey.isEmpty()) {
            throw new IllegalArgumentException("Please link an Unsplash API access key");
        }
        this.accessKey = accessKey;
    }

    @Override
    public List<UnsplashImageInfo> searchImages(String query, int perPage) throws IOException {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be null or empty");
        }

        if (perPage < 1 || perPage > 30) {
            throw new IllegalArgumentException("perPage must be between 1 and 30");
        }

        // Build the search URL
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String urlString = BASE_URL + SEARCH_ENDPOINT + "?query=" + encodedQuery + "&per_page=" + perPage;

        // Make the API request
        String jsonResponse = makeApiRequest(urlString);

        // Parse the JSON response
        return parseSearchResults(jsonResponse);
    }

    @Override
    public UnsplashImageInfo getImageById(String imageId) throws IOException {
        if (imageId == null || imageId.isEmpty()) {
            throw new IllegalArgumentException("Image ID cannot be null or empty");
        }

        // Build the photo URL
        String urlString = BASE_URL + PHOTO_ENDPOINT + "/" + imageId;

        // Make the API request
        String jsonResponse = makeApiRequest(urlString);

        // Parse the JSON response
        JSONObject json = new JSONObject(jsonResponse);
        return parseImageInfo(json);
    }

    @Override
    public InputStream downloadImage(String downloadUrl) throws IOException {
        if (downloadUrl == null || downloadUrl.isEmpty()) {
            throw new IllegalArgumentException("Download URL cannot be null or empty");
        }

        // Trigger download endpoint (need for attribution tracking)
        if (downloadUrl.contains("?")) {
            downloadUrl += "&client_id=" + accessKey;
        }

        URL url = new URL(downloadUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept-Version", "v1");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to download image. HTTP response code: " + responseCode);
        }

        return connection.getInputStream();
    }

    /**
     * Makes an API request to Unsplash and returns the response as a string.
     */
    private String makeApiRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set request headers
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Client-ID " + accessKey);
        connection.setRequestProperty("Accept-Version", "v1");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            String errorMessage = readErrorStream(connection);
            throw new IOException("API request failed with code " + responseCode + ": " + errorMessage);
        }

        // Read the response
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }

    /**
     * Reads error stream from a failed HTTP connection.
     */
    private String readErrorStream(HttpURLConnection connection) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
            StringBuilder error = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                error.append(line);
            }
            return error.toString();
        } catch (Exception e) {
            return "Unable to read error message";
        }
    }

    /**
     * Parses the search results JSON response.
     */
    private List<UnsplashImageInfo> parseSearchResults(String jsonResponse) {
        List<UnsplashImageInfo> results = new ArrayList<>();

        JSONObject json = new JSONObject(jsonResponse);
        JSONArray resultsArray = json.getJSONArray("results");

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject photoJson = resultsArray.getJSONObject(i);
            results.add(parseImageInfo(photoJson));
        }

        return results;
    }

    /**
     * Parses a single photo JSON object into an UnsplashImageInfo.
     */
    private UnsplashImageInfo parseImageInfo(JSONObject photoJson) {
        String id = photoJson.getString("id");
        String description = photoJson.optString("description", photoJson.optString("alt_description", ""));

        JSONObject urls = photoJson.getJSONObject("urls");
        String regularUrl = urls.getString("regular");
        String downloadUrl = urls.getString("raw"); // use raw for installation

        JSONObject user = photoJson.getJSONObject("user");
        String photographer = user.getString("name");

        int width = photoJson.getInt("width");
        int height = photoJson.getInt("height");

        return new UnsplashImageInfo(id, description, downloadUrl, regularUrl, photographer, width, height);
    }
}

