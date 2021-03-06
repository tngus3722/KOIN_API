package koreatech.in.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlackNotiSender {

    @Value("${slack.notify_koin_url}")
    private String notify_koin_url;
    @Value("${slack.notify_crash_url}")
    private String notify_crash_url;
    @Value("${slack.notify_question_url}")
    private String notify_question_url;
    @Value("${project.env}")
    private String env;

    //TODO:: 추후 메세지 큐로 후킹 처리하기

    private RestTemplate restTemplate = new RestTemplate();

    public void noticeError(Object data)
    {
        if (isProduction() || isStage()) {
            List<Object> attachment = new ArrayList<Object>();
            attachment.add(data);
            Map<String, Object> params = new HashMap<String, Object>() {{
                put("channel", "#코인_오류");
                put("username", "Error Notification");
                put("text", String.format("`%s` 서버에서 에러가 발생했습니다.", StringUtils.capitalize(env)));
                put("attachments", attachment);
            }};

            try {
                restTemplate.postForObject(notify_crash_url,params,String.class);
            } catch (RestClientException e) {
                e.printStackTrace();
            }
        }
    }
    public void noticeError() { noticeError("test"); }

    public void noticePost(Object data)
    {
        if (isProduction()) {
            List<Object> attachment = new ArrayList<Object>();
            attachment.add(data);
            Map<String, Object> params = new HashMap<String, Object>() {{
                put("channel", "#코인_이벤트알림");
                put("username", "커뮤니티 서비스");
                put("text", "새로운 포스트가 등록되었습니다.");
                put("attachments", attachment);
            }};

            try {
                restTemplate.postForObject(notify_koin_url,params,String.class);
            } catch (RestClientException e) {
                e.printStackTrace();
            }
        }
    }
    public void noticePost()
    {
        noticePost("test");
    }

    public void noticeComment(Object data)
    {
        if (isProduction()) {
            List<Object> attachment = new ArrayList<Object>();
            attachment.add(data);
            Map<String, Object> params = new HashMap<String, Object>() {{
                put("channel", "#코인_이벤트알림");
                put("username", "공통 댓글 서비스");
                put("text", "새로운 댓글이 등록되었습니다.");
                put("attachments", attachment);
            }};

            try {
                restTemplate.postForObject(notify_koin_url,params,String.class);
            } catch (RestClientException e) {
                e.printStackTrace();
            }
        }
    }
    public void noticeComment()
    {
        noticeComment("test");
    }

    public void noticeWithdraw(Object data)
    {
        if (isProduction()) {
            List<Object> attachment = new ArrayList<Object>();
            attachment.add(data);
            Map<String, Object> params = new HashMap<String, Object>() {{
                put("channel", "#코인_이벤트알림");
                put("username", "회원 플랫폼");
                put("attachments", attachment);
            }};

            try {
                restTemplate.postForObject(notify_koin_url,params,String.class);
            } catch (RestClientException e) {
                e.printStackTrace();
            }
        }
    }
    public void noticeWithdraw()
    {
        noticeWithdraw("test");
    }
    public void noticeRegister(Object data)
    {
        noticeWithdraw(data);
    }
    public void noticeRegister()
    {
        noticeRegister("test");
    }

    public void noticeItem(Object data)
    {
        if (isProduction()) {
            List<Object> attachment = new ArrayList<Object>();
            attachment.add(data);
            Map<String, Object> params = new HashMap<String, Object>() {{
                put("channel", "#코인_이벤트알림");
                put("username", "중고장터 서비스");
                put("text", "새로운 판매글이 등록되었습니다.");
                put("attachments", attachment);
            }};

            try {
                restTemplate.postForObject(notify_koin_url,params,String.class);
            } catch (RestClientException e) {
                e.printStackTrace();
            }
        }
    }
    public void noticeItem()
    {
        noticeItem("test");
    }
    public void noticeLostItem(Object data)
    {
        if (isProduction()) {
            List<Object> attachment = new ArrayList<Object>();
            attachment.add(data);
            Map<String, Object> params = new HashMap<String, Object>() {{
                put("channel", "#코인_이벤트알림");
                put("username", "분실물 서비스");
                put("text", "새로운 분실물이 등록되었습니다.");
                put("attachments", attachment);
            }};

            try {
                restTemplate.postForObject(notify_koin_url,params,String.class);
            } catch (RestClientException e) {
                e.printStackTrace();
            }
        }
    }
    public void noticeLostItem()
    {
        noticeLostItem("test");
    }

    public String noticeQuestion(String message)
    {
        String result = "질문을 등록할 수 없습니다.";
        if (isStage()) {
            Map<String, Object> params = new HashMap<String, Object>() {{
                put("channel", "#질의응답");
                put("text", message);
            }};

            try {
                String response = restTemplate.postForObject(notify_question_url,params,String.class);
                result = response.equals("ok") ? "질문이 등록되었습니다. #질의응답 채널에서 확인하세요." : "질문을 등록하지 못했습니다.";
            } catch (RestClientException e) {
                result = "질문을 등록하지 못했습니다.";
            }
        }
        return result;
    }

    public void request(Map<String, Object> params, String url) {
        Map<String, Object> res = new HashMap<String, Object>() {{
            put("form_params", put("payload", params));
        }};
    }

    private Boolean isProduction() {
        return env.equals("production");
    }

    private Boolean isStage() {
        return env.equals("stage");
    }

    private Boolean isDev() {
        return env.equals("dev");
    }
}
