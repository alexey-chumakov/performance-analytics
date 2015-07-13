package com.ghx.hackaton.analytcis.agent.sender;

import com.ghx.hackaton.analytcis.agent.logger.base.ExecutionInfo;

import java.util.Map;

/**
 * @author Alexey Sheukun <asheukun@exadel.com> on 01.07.2015.
 */
public interface Sender {
    void send(String serverId, Map<String, ExecutionInfo> info);
}
