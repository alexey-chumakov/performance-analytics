package com.ghx.hackaton.analytics.server;

import com.ghx.hackaton.analytcis.ExecutionInfoBean;
import com.ghx.hackaton.analytcis.Statistics;
import com.ghx.hackaton.analytics.model.Request;
import com.ghx.hackaton.analytics.model.RequestDetails;
import com.ghx.hackaton.analytics.service.RequestService;
import com.ghx.hackaton.analytics.util.DateUtil;
import org.apache.avro.AvroRemoteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Alexey Sheukun <asheukun@exadel.com> on 01.07.2015.
 */
public class StatisticsReceiver implements Statistics {

    private AnnotationConfigWebApplicationContext context;

    private RequestService requestService;

    public StatisticsReceiver(AnnotationConfigWebApplicationContext context) {
        this.context = context;
    }

    @Override
    public CharSequence send(Map<CharSequence, ExecutionInfoBean> info, CharSequence appName, CharSequence serverId) throws AvroRemoteException {
        if (requestService == null) {
            requestService = (RequestService)context.getBean("requestServiceImpl");
        }
        requestService.saveOrUpdate(requestsToModel(info, appName.toString(), serverId.toString()));
        return "ok";
    }

    public static List<Request> requestsToModel(Map<CharSequence, ExecutionInfoBean> info, String appName, String serverId) {
        List<Request> model = new ArrayList<Request>();

        for (CharSequence  key : info.keySet()) {
            ExecutionInfoBean bean = info.get(key);

            Request request = new Request();
            request.setYear(DateUtil.year(bean.getSendTime()));
            request.setMonth(DateUtil.month(bean.getSendTime()));
            request.setDay(DateUtil.dayOfMonth(bean.getSendTime()));
            request.setHour(DateUtil.hourOfDay(bean.getSendTime()));
            request.setMinute(DateUtil.minute(bean.getSendTime()));
            request.setAppName(appName);
            request.setServerId(serverId);
            request.setUrl(key.toString());
            request.setCount((long) bean.getExecutionCount());
            request.setDuration(bean.getExecutionTimeTotal());
            request.setDetails(requestDetailsToModel(bean.getDetails(), request));
            model.add(request);
        }

        return model;
    }

    public static List<RequestDetails> requestDetailsToModel(Map<CharSequence, ExecutionInfoBean> info, Request request) {
        List<RequestDetails> model = new ArrayList<RequestDetails>();

        for (CharSequence  key : info.keySet()) {
            ExecutionInfoBean bean = info.get(key);

            RequestDetails requestDetails = new RequestDetails();
            requestDetails.setSystemName(key.toString());
            requestDetails.setCount(bean.getExecutionCount().longValue());
            requestDetails.setDuration(bean.getExecutionTimeTotal());
            requestDetails.setRequest(request);
            model.add(requestDetails);
        }

        return model;
    }
}
