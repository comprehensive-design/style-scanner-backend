package com.example.stylescanner.instagram.util;

import com.example.stylescanner.instagram.dto.CelebProfileResponseDto;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class InstagramGraphApiUtil {
    private String API_URL = "https://graph.facebook.com/v19.0/";

    @Value("${instagram-graph-api.access-token}")
    private String ACCESS_TOKEN;

    @Value("${instagram-graph-api.ig-user-id}")
    private String IG_USER_ID ;

    /**
     *  셀럽 검색 : 존재하면 프로필 정보 리턴, 없으면 오류 메시지 리턴
     */
    public CelebProfileResponseDto SearchCeleb(String celeb_id) throws IOException, JSONException {
        String url_format = String.format("?fields=business_discovery.username(%s){username,followers_count,profile_picture_url}&access_token=%s", celeb_id, ACCESS_TOKEN);
        URL url = new URL(API_URL +IG_USER_ID + url_format);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb=new StringBuilder();
        String line = null;

        while((line = rd.readLine()) != null) {
            sb.append(line);
        }

        JSONObject obj = new JSONObject(sb.toString());
        JSONObject businessDiscovery = obj.getJSONObject("business_discovery");

        CelebProfileResponseDto celebProfileResponseDto = new CelebProfileResponseDto();
        celebProfileResponseDto.setProfileName(businessDiscovery.getString("username"));
        celebProfileResponseDto.setProfilePictureUrl(businessDiscovery.getString("profile_picture_url"));
        celebProfileResponseDto.setProfileFollowerCount(businessDiscovery.getInt("followers_count"));
        return celebProfileResponseDto;
    }
}
