package com.sds.orchestration.handler;

/**
 * if (App A) then (App B)
 * @param appOutput :  A.output
 * @author Sherry
 * @return B.input
 * function: B.in = processFunction(A.out)
 */
public interface JobHandler {
	public String processFunction(String appOutput);
}
