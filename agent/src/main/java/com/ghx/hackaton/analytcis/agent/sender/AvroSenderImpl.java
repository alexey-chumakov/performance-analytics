package com.ghx.hackaton.analytcis.agent.sender;

import com.ghx.hackaton.analytcis.ExecutionInfoBean;
import com.ghx.hackaton.analytcis.Statistics;
import com.ghx.hackaton.analytcis.agent.logger.base.ExecutionInfo;
import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexey Sheukun <asheukun@exadel.com> on 01.07.2015.
 */
public class AvroSenderImpl implements Sender {

    //FIXME move to properties
    private static final String SERVER_URL = "http://localhost:8082/avro";
    private static final int SERVER_PORT = 65111;

    private Statistics client;

    public AvroSenderImpl() throws IOException {
        HttpTransceiver transceiver = new HttpTransceiver(new URL(SERVER_URL));
        client = SpecificRequestor.getClient(Statistics.class, transceiver);
    }

    public void send(String appName, String serverId, Map<String, ExecutionInfo> info) {
        Map<CharSequence, ExecutionInfoBean> result = new HashMap<CharSequence, ExecutionInfoBean>();
        for (String key : info.keySet()) {
            ExecutionInfo executionInfo = info.get(key);

            Map<CharSequence, ExecutionInfoBean> details = new HashMap<CharSequence, ExecutionInfoBean>();
            for (String k : executionInfo.getDetails().keySet()) {
                ExecutionInfo v = executionInfo.getDetails().get(k);
                details.put(k, new ExecutionInfoBean(v.getExecutionCount(), v.getExecutionTimeTotal(), System.currentTimeMillis(), new HashMap<CharSequence, ExecutionInfoBean>()));
            }

            ExecutionInfoBean bean = new ExecutionInfoBean(executionInfo.getExecutionCount(), executionInfo.getExecutionTimeTotal(), System.currentTimeMillis(), details);
            result.put(key, bean);
        }
        try {
            if (result.size() > 0) {
                client.send(result, appName, serverId);
            }
        } catch (AvroRemoteException e) {
            System.err.println("Failed to send execution statistics. " + e.toString());
        }
    }
}
