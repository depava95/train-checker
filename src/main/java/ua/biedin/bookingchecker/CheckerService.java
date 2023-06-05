package ua.biedin.bookingchecker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class CheckerService {


    @PostConstruct
    public void check() throws JsonProcessingException {
        try {
            String url = "https://booking.uz.gov.ua/train_search/";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "*/*");
            headers.add("Accept-Language", "en-US,en;q=0.9,ru;q=0.8,pt;q=0.7");
            headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            headers.add("Cookie", "__uzma=d2e03893-e789-42fd-8a2e-51b1c2dc2a00; __uzmb=1685996256; __uzme=3231; _gv_lang=uk; _gv_sessid=o4vii3j10pqapnvrdvdlstr1t5; HTTPSERVERID=server3; cookiesession1=678B286E21264F1CADDAF4079A82E01B; _ga=GA1.3.343525472.1685996257; _gid=GA1.3.1795705805.1685996257; __uzmd=1685996273; __uzmc=509702532258");
            headers.add("Origin", "https://booking.uz.gov.ua");
            headers.add("Referer", "https://booking.uz.gov.ua/?from=2210700&to=2218070&date=2023-06-19&time=00%3A00&url=train-list");
            headers.add("Sec-Fetch-Dest", "empty");
            headers.add("Sec-Fetch-Mode", "cors");
            headers.add("Sec-Fetch-Site", "same-origin");
            headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");
            headers.add("X-Requested-With", "XMLHttpRequest");
            headers.add("cache-version", "761");
            headers.add("sec-ch-ua", "\"Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"115\", \"Chromium\";v=\"115\"");
            headers.add("sec-ch-ua-mobile", "?0");
            headers.add("sec-ch-ua-platform", "\"macOS\"");

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("from", "2210700");
            body.add("to", "2218070");
            body.add("date", "2023-06-19");
            body.add("time", "00:00");

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), String.class);
            String responseBody = response.getBody();


            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readValue(responseBody, JsonNode.class);

            jsonNode.get("data").get("list").get(0).get("types").forEach(type -> {
                log.info(type.toPrettyString());
            });
        } catch (Exception ex) {
            log.error("Failed");
        }
    }
}
