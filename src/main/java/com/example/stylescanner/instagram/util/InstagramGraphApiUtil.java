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
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InstagramGraphApiUtil {
    private String API_URL = "https://graph.facebook.com/v19.0/";


    // 공식 API
    @Value("${instagram-graph-api.access-token}")
    private String ACCESS_TOKEN;

    @Value("${instagram-graph-api.ig-user-id}")
    private String IG_USER_ID;


    //서드 파트 API
    @Value("${rapid-instagram-api.host}")
    private String HOST;

    @Value(("${rapid-instagram-api.key}"))
    private String KEY;

    /**
     * INSTAGRAM GRAPH API 호출
     */
    public JSONObject GetAPIData(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);

        int responseCode = conn.getResponseCode();

        if (responseCode == 200) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            JSONObject obj = new JSONObject(sb.toString());

            return obj;
        } else {
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
     *
     * @param url
     * @return
     * @throws IOException
     */
    public JSONObject GetAPIData_Rapid(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // RapidAPI 요청에 필요한 헤더 추가
        conn.setRequestProperty("x-rapidapi-host", HOST);
        conn.setRequestProperty("x-rapidapi-key", KEY);

        int responseCode = conn.getResponseCode();

        if (responseCode == 200) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            return new JSONObject(sb.toString());
        }else {
            // 오류 응답이 있을 경우 로그로 출력
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            System.err.println("Error response: " + sb.toString());
        }
        return null;
    }


    /**
     * 셀럽 검색 : 존재하면 프로필 정보 리턴, 없으면 null 리턴
     */
    public CelebProfileResponseDto SearchCeleb(String celeb_id) throws IOException, JSONException {
        String url_format = String.format("?fields=business_discovery.username(%s){name,biography,follows_count,followers_count,profile_picture_url}&access_token=%s", celeb_id, ACCESS_TOKEN);
        URL url = new URL(API_URL + IG_USER_ID + url_format);

        JSONObject obj = GetAPIData(url);
        if (obj == null) {
            System.out.println("Rapid API로 찾습니다 ");
            CelebProfileResponseDto celebProfileResponseDto = SearchCeleb_Rapid(celeb_id);
            return celebProfileResponseDto;
        }

        JSONObject businessDiscovery = obj.getJSONObject("business_discovery");
        CelebProfileResponseDto celebProfileResponseDto = new CelebProfileResponseDto();
        celebProfileResponseDto.setProfileName(celeb_id);
        celebProfileResponseDto.setProfilePictureUrl(businessDiscovery.getString("profile_picture_url"));
        celebProfileResponseDto.setProfileFollowerCount(businessDiscovery.getInt("followers_count"));
        celebProfileResponseDto.setProfileFollowingCount(businessDiscovery.getInt("follows_count"));
        if (businessDiscovery.has("biography")) {
            celebProfileResponseDto.setProfileBio(businessDiscovery.getString("biography"));
        }
        return celebProfileResponseDto;
    }

    public CelebProfileResponseDto SearchCeleb_Rapid(String celeb_id) throws IOException {
        String url_format = String.format("https://instagram-scraper-api2.p.rapidapi.com/v1/info?username_or_id_or_url=%s",celeb_id);

        URL url = new URL(url_format);
        JSONObject obj = GetAPIData_Rapid(url);

        if(obj == null) return null;

        JSONObject data = obj.getJSONObject("data");

        CelebProfileResponseDto celebProfileResponseDto = new CelebProfileResponseDto();
        celebProfileResponseDto.setProfileName(celeb_id);
        celebProfileResponseDto.setProfilePictureUrl(data.getString("profile_pic_url"));
        celebProfileResponseDto.setProfileFollowerCount(data.getInt("follower_count"));
        celebProfileResponseDto.setProfileFollowingCount(data.getInt("following_count"));
        celebProfileResponseDto.setProfileBio(data.getString("biography"));
        celebProfileResponseDto.setMediaCount(data.getInt("media_count"));

        return celebProfileResponseDto;
    }

    /**
     * 셀럽의 모든 피드 반환
     * (24.08.29 수정 : 모든 피드 -> 상위 12 피드만) => GetCelebInsta 사용
     *
     * @param username
     * @return
     * @throws IOException
     */
    public List<FeedDto> GetALlCelebFeed(String username) throws IOException {
        String url_format = String.format("?fields=business_discovery.username(%s){media{media_type,media_url,id,timestamp}}&access_token=%s"
                , username, ACCESS_TOKEN);
        URL url = new URL(API_URL + IG_USER_ID + url_format);

        JSONObject obj = GetAPIData(url);
        JSONObject media = obj.getJSONObject("business_discovery").getJSONObject("media");
        JSONArray data_list = media.getJSONArray("data");

        List<FeedDto> feedDtoList = new ArrayList<>();
        String cursors_after = null;
        String cursors_before = null;
        if (media.has("paging")) {
            if (media.getJSONObject("paging").has("cursors")) {
                if (media.getJSONObject("paging").getJSONObject("cursors").has("after")) {
                    cursors_after = media.getJSONObject("paging").getJSONObject("cursors").getString("after");
                }
                if (media.getJSONObject("paging").getJSONObject("cursors").has("before")) {
                    cursors_before = media.getJSONObject("paging").getJSONObject("cursors").getString("before");
                }
            }
        }

        feedDtoList.addAll(GetMediaData(data_list, cursors_before));
        while (cursors_after != null) {
            url_format = String.format("?fields=business_discovery.username(%s){media.after(%s){media_type,media_url,id,timestamp}}&access_token=%s"
                    , username, cursors_after, ACCESS_TOKEN);
            url = new URL(API_URL + IG_USER_ID + url_format);

            obj = GetAPIData(url);
            media = obj.getJSONObject("business_discovery").getJSONObject("media");
            data_list = media.getJSONArray("data");

            cursors_after = null;
            if (media.has("paging")) {
                if (media.getJSONObject("paging").has("cursors")) {
                    if (media.getJSONObject("paging").getJSONObject("cursors").has("after")) {
                        cursors_after = media.getJSONObject("paging").getJSONObject("cursors").getString("after");
                    }
                    if (media.getJSONObject("paging").getJSONObject("cursors").has("before")) {
                        cursors_before = media.getJSONObject("paging").getJSONObject("cursors").getString("before");
                    }
                }
            }

            feedDtoList.addAll(GetMediaData(data_list, cursors_before));
        }

        return feedDtoList;
    }


    public List<FeedDto> GetCelebInsta(String username) throws IOException {
        String url_format = String.format("?fields=business_discovery.username(%s){media.limit(%d){media_type,thumbnail_url, media_url,id,timestamp}}&access_token=%s"
                , username, 12, ACCESS_TOKEN);
        URL url = new URL(API_URL + IG_USER_ID + url_format);

        JSONObject obj = GetAPIData(url);

        if(obj == null){
            return GetCelebInsta_Rapid(username);
        }else{
            JSONObject media = obj.getJSONObject("business_discovery").getJSONObject("media");
            JSONArray data_list = media.getJSONArray("data");

            List<FeedDto> feedDtoList = new ArrayList<>();

            for(int i=0; i<data_list.length(); i++) {
                JSONObject data = data_list.getJSONObject(i);
                String media_type = data.getString("media_type");
                List<String> media_url = new ArrayList<>();

                if(media_type.equals("VIDEO")){
                    media_url.add(data_list.getJSONObject(i).getString("thumbnail_url"));
                }else{
                    media_url.add(data_list.getJSONObject(i).getString("media_url"));
                }

                // 피드 시간
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                LocalDateTime timestamp = LocalDateTime.parse(data.getString("timestamp"), formatter);

                //피드 미디어 ID
                String id = data.getString("id");
                FeedDto feedDto = new FeedDto();
                feedDto.setMedia_url_list(media_url);
                feedDto.setTimestamp(timestamp);
                feedDto.setMedia_id(id);
                feedDto.setFeed_index(i + 1);
                feedDto.setBefore_cursor("");
                feedDtoList.add(feedDto);
            }

            return feedDtoList;
        }

    }

    public List<FeedDto> GetCelebInsta_Rapid(String username) throws IOException {
        String url_format = String.format("https://instagram-scraper-api2.p.rapidapi.com/v1.2/posts?username_or_id_or_url=%s",username);

        URL url = new URL(url_format);
        JSONObject obj = GetAPIData_Rapid(url);

        if(obj == null) return null;

        JSONObject data = obj.getJSONObject("data");
        JSONArray items = data.getJSONArray("items");


        List<FeedDto> feedDtoList = new ArrayList<>();

        for(int i = 0 ; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);

            // 피드 시간
            long timestamp = item.getInt("taken_at");
            Instant instant = Instant.ofEpochSecond(timestamp);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

            List<String> media_url = new ArrayList<>();
            String id = item.getString("id");

            media_url.add(item.getString("thumbnail_url"));

            //피드 미디어 ID
            FeedDto feedDto = new FeedDto();
            feedDto.setUsername(username);
            feedDto.setMedia_url_list(media_url);
            feedDto.setTimestamp(localDateTime);
            feedDto.setMedia_id(id);
            feedDto.setFeed_index(i + 1);
            feedDto.setBefore_cursor("");
            feedDtoList.add(feedDto);
        }

        return feedDtoList;
    }


    public List<FeedDto> GetMediaData(JSONArray data_list, String before_cursor) {
        List<FeedDto> feedDtoList = new ArrayList<>();
        for (int i = 0; i < data_list.length(); i++) {
            JSONObject data = (JSONObject) data_list.get(i);
            String media_type = data.getString("media_type");

            if (media_type.equals("IMAGE") || media_type.equals("CAROUSEL_ALBUM")) {
                //피드 사진
                List<String> media_url = new ArrayList<>();
                media_url.add(data.getString("media_url"));

                if (data.has("children")) {
                    JSONArray children = data.getJSONObject("children").getJSONArray("data");
                    System.out.println(children);
                    for (int j = 1; j < children.length(); j++) {
                        media_url.add(children.getJSONObject(j).getString("media_url"));
                    }
                }

                // 피드 시간
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                LocalDateTime timestamp = LocalDateTime.parse(data.getString("timestamp"), formatter);

                //피드 미디어 ID
                String id = data.getString("id");
                FeedDto feedDto = new FeedDto();
                feedDto.setMedia_url_list(media_url);
                feedDto.setTimestamp(timestamp);
                feedDto.setMedia_id(id);
                feedDto.setFeed_index(i + 1);
                feedDto.setBefore_cursor(before_cursor);
                feedDtoList.add(feedDto);
            }
        }
        return feedDtoList;
    }

    /**
     * 한 셀럽의 가장 최근 피드 반환 메소드
     *
     * @param username
     * @return 피드 리스트
     * @throws IOException
     */
    public List<FeedDto> GetRecentCelebFeed(String username) throws IOException {
        String url_format = String.format("?fields=business_discovery.username(%s){media.limit(1){media_type,media_url,children{media_url,media_type},timestamp}}&access_token=%s"
                , username, ACCESS_TOKEN);
        URL url = new URL(API_URL + IG_USER_ID + url_format);

        JSONObject obj = GetAPIData(url);
        if(obj == null){
            return GetRecentCelebFeed_Rapid(username);

        }else{
            JSONArray data_list = obj.getJSONObject("business_discovery").getJSONObject("media").getJSONArray("data");

            String profile_url = SearchCeleb(username).getProfilePictureUrl();

            List<FeedDto> feedDtoList = new ArrayList<>();

            for (int i = 0; i < data_list.length(); i++) {
                JSONObject data = (JSONObject) data_list.get(i);
                String media_type = data.getString("media_type");

                if (media_type.equals("IMAGE") || media_type.equals("CAROUSEL_ALBUM")) {
                    //피드 사진
                    List<String> media_url = new ArrayList<>();
                    if (media_type.equals("IMAGE")) {
                        media_url.add(data.getString("media_url"));
                    } else {
                        if (data.has("children")) {
                            JSONArray children = data.getJSONObject("children").getJSONArray("data");
                            for (int j = 0; j < children.length(); j++) {
                                if (children.getJSONObject(j).getString("media_type").equals("IMAGE")) {
                                    media_url.add(children.getJSONObject(j).getString("media_url"));
                                }
                            }
                        }
                    }

                    // 피드 시간
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                    LocalDateTime timestamp = LocalDateTime.parse(data.getString("timestamp"), formatter);

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

            return feedDtoList.stream()
                    .sorted(Comparator.comparing(FeedDto::getTimestamp).reversed())
                    .collect(Collectors.toList());
        }


    }

    /**
     * 서드파티 API를 활용한
     * 한 셀럽의 가장 최근 피드 반환 메소드
     * @param username
     * @return
     * @throws IOException
     */
    public List<FeedDto> GetRecentCelebFeed_Rapid(String username) throws IOException {
        String url_format = String.format("https://instagram-scraper-api2.p.rapidapi.com/v1.2/posts?username_or_id_or_url=%s",username);

        URL url = new URL(url_format);
        JSONObject obj = GetAPIData_Rapid(url);

        if(obj == null) return null;

        JSONObject data = obj.getJSONObject("data");
        JSONArray items = data.getJSONArray("items");

        String profile_url = data.getJSONObject("user").getString("profile_pic_url");

        List<FeedDto> feedDtoList = new ArrayList<>();

        for(int i = 0 ; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);


            if(!item.getBoolean("is_video") && !item.getBoolean("is_pinned")){

                // 피드 시간
                long timestamp = item.getInt("taken_at");
                Instant instant = Instant.ofEpochSecond(timestamp);
                LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());


                JSONArray media_list = item.getJSONArray("carousel_media");


                List<String> media_url = new ArrayList<>();
                String id = item.getString("id");

                for(int j = 0 ; j < media_list.length(); j++) {
                    if(media_list.getJSONObject(j).getBoolean("is_video")){
                        continue;
                    }

                    media_url.add(media_list.getJSONObject(j).getString("thumbnail_url"));
                }

                //피드 미디어 ID
                FeedDto feedDto = new FeedDto();
                feedDto.setUsername(username);
                feedDto.setProfile_url(profile_url);
                feedDto.setMedia_url_list(media_url);
                feedDto.setTimestamp(localDateTime);
                feedDto.setMedia_id(id);
                feedDtoList.add(feedDto);

                break;
            }
        }

        return feedDtoList.stream()
                .sorted(Comparator.comparing(FeedDto::getTimestamp).reversed())
                .collect(Collectors.toList());
    }


    /**
     * 한 셀럽의 before_cursors 이후의 특정 피드 반환
     *
     * @param username
     * @param media_id
     * @param before_cursors
     * @param feed_index
     * @return
     */
    public FeedDto GetMedia(String username, String media_id, String before_cursors, int feed_index) {
        String url_format = "";
        FeedDto feedDto = new FeedDto();

        if (before_cursors == null) {
            url_format = String.format("?fields=business_discovery.username(%s){media.limit(%d){media_type,media_url,children{media_url,media_type}}}&access_token=%s"
                    , username, feed_index, ACCESS_TOKEN);
        } else {
            url_format = String.format("?fields=business_discovery.username(%s){media.limit(%d).after(%s){media_type,media_url,children{media_url,media_type}}}&access_token=%s"
                    , username, feed_index, before_cursors, ACCESS_TOKEN);
        }

        try {
            URL url = new URL(API_URL + IG_USER_ID + url_format);

            JSONObject obj = GetAPIData(url);
            JSONArray data_list = obj.getJSONObject("business_discovery").getJSONObject("media").getJSONArray("data");

            for (int i = 0; i < data_list.length(); i++) {
                JSONObject data = (JSONObject) data_list.get(i);
                String media_type = data.getString("media_type");
                String id = data.getString("id");

                if (id.equals(media_id)) {
                    if (media_type.equals("IMAGE") || media_type.equals("CAROUSEL_ALBUM")) {
                        //피드 사진
                        List<String> media_url = new ArrayList<>();
                        if (media_type.equals("IMAGE")) {
                            media_url.add(data.getString("media_url"));
                        } else {
                            if (data.has("children")) {
                                JSONArray children = data.getJSONObject("children").getJSONArray("data");
                                for (int j = 0; j < children.length(); j++) {
                                    if (children.getJSONObject(j).getString("media_type").equals("IMAGE")) {
                                        media_url.add(children.getJSONObject(j).getString("media_url"));
                                    }
                                }
                            }
                        }
                        //피드 미디어 ID
                        feedDto.setUsername(username);
                        feedDto.setMedia_url_list(media_url);
                        feedDto.setMedia_id(id);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return feedDto;
    }


    /**
     * 한 셀럽의 가장 최근 N개의 피드 섬네일 반환 메소드
     * @param username
     * @param feed_cnt
     * @return
     * @throws IOException
     */
    public List<String> GetRecentCelebFeed(String username, int feed_cnt) throws IOException {
        String url_format = String.format("?fields=business_discovery.username(%s){media.limit(%d){media_type,media_url,thumbnail_url,timestamp}}&access_token=%s"
                , username, feed_cnt, ACCESS_TOKEN);
        URL url = new URL(API_URL + IG_USER_ID + url_format);

        JSONObject obj = GetAPIData(url);

        if(obj == null){

            return GetRecentCelebFeed_Rapid(username, feed_cnt);
        }else{

            JSONArray data_list = obj.getJSONObject("business_discovery").getJSONObject("media").getJSONArray("data");

            List<String> mediaList = new ArrayList<>();

            for (int i = 0; i < data_list.length(); i++) {
                String media_url;
                if(data_list.getJSONObject(i).getString("media_type").equals("VIDEO")){
                    media_url = data_list.getJSONObject(i).getString("thumbnail_url");
                }else{
                    media_url = data_list.getJSONObject(i).getString("media_url");
                }
                System.out.println(media_url);

                mediaList.add(media_url);
            }

            return mediaList;
        }

    }


    public List<String> GetRecentCelebFeed_Rapid(String username, int feed_cnt) throws IOException {

        String url_format = String.format("https://instagram-scraper-api2.p.rapidapi.com/v1.2/posts?username_or_id_or_url=%s",username);

        URL url = new URL(url_format);
        JSONObject obj = GetAPIData_Rapid(url);

        if(obj == null) return null;

        JSONObject data = obj.getJSONObject("data");
        JSONArray items = data.getJSONArray("items");


        List<String> mediaList = new ArrayList<>();

        for(int i = 0 ; i < feed_cnt; i++) {
            JSONObject item = items.getJSONObject(i);
            mediaList.add(item.getString("thumbnail_url"));
        }

        return mediaList;
    }

}
