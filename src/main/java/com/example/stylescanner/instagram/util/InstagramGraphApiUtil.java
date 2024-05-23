package com.example.stylescanner.instagram.util;

import com.example.stylescanner.instagram.dto.CelebProfileResponseDto;
import com.example.stylescanner.instagram.dto.FeedDto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InstagramGraphApiUtil {
    private String API_URL = "https://graph.facebook.com/v19.0/";

    @Value("${instagram-graph-api.access-token}")
    private String ACCESS_TOKEN;

    @Value("${instagram-graph-api.ig-user-id}")
    private String IG_USER_ID ;

    /**
     * INSTAGRAM GRAPH API 호출
     */

    public JSONObject GetAPIData(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);

        int responseCode = conn.getResponseCode();

        if(responseCode == 200) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb=new StringBuilder();
            String line = null;

            while((line = rd.readLine()) != null) {
                sb.append(line);
            }

            JSONObject obj = new JSONObject(sb.toString());

            return obj;
        }else{
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (response.toString().contains("Invalid user id")) {
                return null;
            }
        }

        return null;
    }

    /**
     *  셀럽 검색 : 존재하면 프로필 정보 리턴, 없으면 오류 메시지 리턴
     */
    public CelebProfileResponseDto SearchCeleb(String celeb_id) throws IOException, JSONException {
        String url_format = String.format("?fields=business_discovery.username(%s){name,biography,follows_count,followers_count,profile_picture_url}&access_token=%s", celeb_id, ACCESS_TOKEN);
        URL url = new URL(API_URL + IG_USER_ID + url_format);

        JSONObject obj = GetAPIData(url);

        if(obj == null){
            return null;
        }

        JSONObject businessDiscovery = obj.getJSONObject("business_discovery");
        CelebProfileResponseDto celebProfileResponseDto = new CelebProfileResponseDto();
        celebProfileResponseDto.setProfileName(celeb_id);
        celebProfileResponseDto.setProfilePictureUrl(businessDiscovery.getString("profile_picture_url"));
        celebProfileResponseDto.setProfileFollowerCount(businessDiscovery.getInt("followers_count"));
        celebProfileResponseDto.setProfileFollowingCount(businessDiscovery.getInt("follows_count"));
        if(businessDiscovery.has("biography")) {
            celebProfileResponseDto.setProfileBio(businessDiscovery.getString("biography"));
        }
        return celebProfileResponseDto;
    }

    /**
     * 셀럽의 모든 피드 반환
     * @param username
     * @return
     * @throws IOException
     */
    public List<FeedDto> GetALlCelebFeed(String username) throws IOException {
        String url_format = String.format( "?fields=business_discovery.username(%s){media{media_type,media_url,id,timestamp}}&access_token=%s"
                ,username,ACCESS_TOKEN);
        URL url = new URL(API_URL +IG_USER_ID + url_format);

        JSONObject obj = GetAPIData(url);
        JSONObject media = obj.getJSONObject("business_discovery").getJSONObject("media");
        JSONArray data_list = media.getJSONArray("data");

        List<FeedDto> feedDtoList = new ArrayList<>();
        String cursors_after = null;
        if(media.has("paging")){
            if(media.getJSONObject("paging").has("cursors")){
                if(media.getJSONObject("paging").getJSONObject("cursors").has("after")){
                    cursors_after = media.getJSONObject("paging").getJSONObject("cursors").getString("after");
                }
            }
        }

        feedDtoList.addAll(GetMediaData(data_list));
        while(cursors_after != null){
            url_format = String.format( "?fields=business_discovery.username(%s){media.after(%s){media_type,media_url,id,timestamp}}&access_token=%s"
                    ,username,cursors_after,ACCESS_TOKEN);
            url = new URL(API_URL +IG_USER_ID + url_format);

            obj = GetAPIData(url);
            media = obj.getJSONObject("business_discovery").getJSONObject("media");
            data_list = media.getJSONArray("data");

            cursors_after = null;
            if(media.has("paging")){
                if(media.getJSONObject("paging").has("cursors")){
                    if(media.getJSONObject("paging").getJSONObject("cursors").has("after")){
                        cursors_after = media.getJSONObject("paging").getJSONObject("cursors").getString("after");
                    }
                }
            }

            feedDtoList.addAll(GetMediaData(data_list));
        }

        return feedDtoList;
    }


    public List<FeedDto> GetMediaData(JSONArray data_list){
        List<FeedDto> feedDtoList = new ArrayList<>();
        for(int i=0; i<data_list.length(); i++) {
            JSONObject data = (JSONObject) data_list.get(i);
            String media_type = data.getString("media_type");

            if(media_type.equals("IMAGE")||media_type.equals("CAROUSEL_ALBUM")) {
                //피드 사진
                List<String> media_url = new ArrayList<>();
                media_url.add(data.getString("media_url"));

                if(data.has("children")){
                    JSONArray children = data.getJSONObject("children").getJSONArray("data");
                    System.out.println(children);
                    for(int j=1; j<children.length(); j++) {
                        media_url.add(children.getJSONObject(j).getString("media_url"));
                    }
                }

                // 피드 시간
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                LocalDateTime timestamp = LocalDateTime.parse(data.getString("timestamp"),formatter);

                //피드 미디어 ID
                String id = data.getString("id");
                FeedDto feedDto = new FeedDto();
                feedDto.setMedia_url_list(media_url);
                feedDto.setTimestamp(timestamp);
                feedDto.setMedia_id(id);
                feedDtoList.add(feedDto);
            }
        }
        return feedDtoList;
    }

    /**
     * 한 셀럽의 가장 최근 피드 반환 메소드
     * @param username
     * @return 피드 리스트
     * @throws IOException
     */
    public List<FeedDto> GetRecentCelebFeed(String username) throws IOException {
        String url_format = String.format( "?fields=business_discovery.username(%s){media{media_type,media_url,children{media_url},timestamp}}&access_token=%s"
                            ,username,ACCESS_TOKEN);
        URL url = new URL(API_URL +IG_USER_ID + url_format);

        JSONObject obj = GetAPIData(url);
        JSONArray data_list = obj.getJSONObject("business_discovery").getJSONObject("media").getJSONArray("data");

        String profile_url = SearchCeleb(username).getProfilePictureUrl();

        List<FeedDto> feedDtoList = new ArrayList<>();

        for(int i=0; i<data_list.length(); i++) {
            JSONObject data = (JSONObject) data_list.get(i);
            String media_type = data.getString("media_type");

            if(media_type.equals("IMAGE")||media_type.equals("CAROUSEL_ALBUM")) {
                //피드 사진
                List<String> media_url = new ArrayList<>();
                media_url.add(data.getString("media_url"));

                if(data.has("children")){
                    JSONArray children = data.getJSONObject("children").getJSONArray("data");
                    for(int j=1; j<children.length(); j++) {
                        media_url.add(children.getJSONObject(j).getString("media_url"));
                    }
                }

                // 피드 시간
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                LocalDateTime timestamp = LocalDateTime.parse(data.getString("timestamp"),formatter);

                //피드 미디어 ID
                String id = data.getString("id");

                FeedDto feedDto = new FeedDto();
                feedDto.setUsername(username);
                feedDto.setProfile_url(profile_url);
                feedDto.setMedia_url_list(media_url);
                feedDto.setTimestamp(timestamp);
                feedDto.setMedia_id(id);
                feedDtoList.add(feedDto);
            }
        }

        return  feedDtoList.stream()
                .sorted(Comparator.comparing(FeedDto::getTimestamp).reversed())
                .collect(Collectors.toList());
    }
}
