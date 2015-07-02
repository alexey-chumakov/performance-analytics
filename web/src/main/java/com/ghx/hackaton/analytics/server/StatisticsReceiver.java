package com.ghx.hackaton.analytics.server;

import com.ghx.hackaton.analytcis.ExecutionInfoBean;
import com.ghx.hackaton.analytcis.Statistics;
import org.apache.avro.AvroRemoteException;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Alexey Sheukun <asheukun@exadel.com> on 01.07.2015.
 */
@Service
public class StatisticsReceiver implements Statistics {
    public CharSequence send(Map<CharSequence, ExecutionInfoBean> info) throws AvroRemoteException {
        //TODO convert from ExecutionInfoBean to hibernate beans and save to DB
        return null;
    }
}
