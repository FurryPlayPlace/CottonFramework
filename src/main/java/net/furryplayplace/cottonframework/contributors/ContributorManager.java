/*
---------------------------------------------------------------------------------
File Name : ContributorManager

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.contributors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public class ContributorManager {
    private static final Gson gson = new Gson();

    public static Optional<List<Contributor>> getContributors() {
        try {
            String REPO_URL = "https://api.github.com/repos/FurryPlayPlace/CottonFramework/contributors?per_page=100&page=1";
            final URL repoUrl = new URL(REPO_URL);

            HttpsURLConnection connection = (HttpsURLConnection) repoUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                Type listType = new TypeToken<List<Contributor>>() {}.getType();
                List<Contributor> contributors = gson.fromJson(response.toString(), listType);

                return Optional.of(contributors);
            }
        } catch (Exception ignored) {}

        return Optional.empty();
    }


    @Getter
    @AllArgsConstructor
    public static class Contributor {
        private final String login;
        private final int contributions;
    }
}