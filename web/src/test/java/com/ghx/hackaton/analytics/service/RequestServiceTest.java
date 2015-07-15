package com.ghx.hackaton.analytics.service;

import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.RequestDetails;
import com.ghx.hackaton.analytics.spring.TestConfig;
import com.ghx.hackaton.analytics.util.DateUtil;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class RequestServiceTest  {

    private static String TEST_DATE_FORMAT = "yyyy-MM-dd HH:mm";
    private static String TEST_APP_NAME = "test_app_name";
    private static String TEST_SERVER_ID = "test_server_id";
    private static String TEST_URL = "test_url";
    private static long TEST_COUNT = 10L;
    private static long TEST_DURATION = 1000L;
    private static Date TEST_DATE;

    @Autowired
    private RequestService requestService;

    @Autowired
    private RequestDetailsService requestDetailsService;

    @BeforeClass
    public static void init() throws ParseException {
        TEST_DATE = new SimpleDateFormat(TEST_DATE_FORMAT).parse("1990-01-01 13:03");
    }

    @Before
    public void beforeClearTestData() {
        requestService.delete(TEST_DATE, TEST_DATE);
    }

    @Test
    public void testSaveOrUpdate() {
        List<Request> testRequests1 = new ArrayList<Request>();
        Request req1 = createRequest(TEST_DATE, TEST_APP_NAME, TEST_SERVER_ID, TEST_URL, TEST_COUNT, TEST_DURATION);
        req1.getDetails().add(createRequestDetails("mongo", TEST_COUNT / 2, TEST_DURATION / 2, req1));
        req1.getDetails().add(createRequestDetails("mysql", TEST_COUNT / 2, TEST_DURATION / 2, req1));
        testRequests1.add(req1);

        requestService.saveOrUpdate(testRequests1);

        List<Request> testRequests2 = new ArrayList<Request>();
        Request req2 = createRequest(TEST_DATE, TEST_APP_NAME, TEST_SERVER_ID, TEST_URL, TEST_COUNT, TEST_DURATION);
        req2.getDetails().add(createRequestDetails("mongo", TEST_COUNT / 2, TEST_DURATION / 2, req2));
        req2.getDetails().add(createRequestDetails("mysql", TEST_COUNT / 2, TEST_DURATION / 2, req2));
        testRequests2.add(req2);

        requestService.saveOrUpdate(testRequests2);

        List<Request> requests2 = requestService.find(TEST_DATE, TEST_DATE);
        Assert.assertTrue("Request should have been saved and then updated", requests2.size() == 1);

        Request tested = requests2.get(0);

        Assert.assertTrue("Wrong total count of a request", TEST_COUNT * 2 == tested.getCount());
        Assert.assertTrue("Wrong total duration of a request", TEST_DURATION * 2 == tested.getDuration());

        List<RequestDetails> requestDetails2 = requestDetailsService.findByRequest(TEST_DATE, TEST_DATE, tested);
        Assert.assertTrue("Request should have 2 test details", requestDetails2.size() == 2);
        for (RequestDetails requestDetail : requestDetails2) {
            Assert.assertTrue("Wrong total count of a request detail", TEST_COUNT == requestDetail.getCount());
            Assert.assertTrue("Wrong total duration of a request detail", TEST_DURATION == requestDetail.getDuration());
        }
    }

    @After
    public void afterClearTestData() {
        requestService.delete(TEST_DATE, TEST_DATE);
    }

    private Request createRequest(Date date, String appName, String serverId, String url, long count, long duration) {
        Request request = new Request();
        request.setTimestamp(date.getTime());
        request.setYear(DateUtil.year(date));
        request.setMonth(DateUtil.month(date));
        request.setDay(DateUtil.dayOfMonth(date));
        request.setHour(DateUtil.hourOfDay(date));
        request.setMinute(DateUtil.minute(date));
        request.setAppName(appName);
        request.setServerId(serverId);
        request.setUrl(url);
        request.setCount(count);
        request.setFailedCount(0L);
        request.setDuration(duration);
        request.setDetails(new ArrayList<RequestDetails>());
        return request;
    }

    private RequestDetails createRequestDetails(String systemName, long count, long duration, Request request) {
        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setSystemName(systemName);
        requestDetails.setCount(count);
        requestDetails.setDuration(duration);
        requestDetails.setRequest(request);
        return requestDetails;
    }
}
