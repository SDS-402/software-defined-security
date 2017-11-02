(ns com.core)  
(import '(com.sds.securitycontroller.flow.manager FlowMonitor))
(import '(com.sds.securitycontroller.flow FlowAvgCount))
;relative threshold
(def rpth 3)
(def rbth 3)
;absolute threshold
(def apth 1000)
(def abth 100000)

(defn proccessFlowmapping[flowMapping fmi]
	;suspicious flows to be reported
	(def suspiciousFlows (.getSuspiciousFlows fmi))

  (def flowAvgCount (.getFlowAvgCount fmi))
 
	(doseq [i (iterator-seq (.iterator (.values flowMapping)))] 
   (println i)
    (def flowId (.getId i))
		(def pc (.getPacketCount i))
		(def bc (.getByteCount i))
  (println pc bc)
  ;flow exceed the absolute threshold, put it into badflows 
  (if (or (> pc apth) (> bc abth))
  (do
    (.put suspiciousFlows flowId i)
    (println "_____flow exceed the absolute threshold, put it into badflows ")
  )
  ;below absolute threshold , has a record   
  (if (.containsKey flowAvgCount flowId ) 
    (do
      (def pcavg (.getAvgPktCount (.get flowAvgCount flowId)))                                       
      (def bcavg (.getAvgByteCount (.get flowAvgCount flowId)))
      ;flow size > history average * 2 , put into badflows
      (if (or ( > pc (* rpth pcavg)) ( > bc (* rbth bcavg)))
          (do
            (.put suspiciousFlows flowId i)
            (println "flow size > history average * 2 , put into badflows")
          )
          ;normal flows, update record
          (do
            (.updateAvgPktCount  (.get flowAvgCount flowId) (.getPacketCount i))
            (.updateAvgByteCount  (.get flowAvgCount flowId) (.getByteCount i))
            (.increasePktLearnedCount  (.get flowAvgCount flowId))
            (println "normal flows, update record")
          )
      )
     )
  ;below abslolute threshold, no record. create it.  
      (do
        (def fac (new FlowAvgCount i))
        (.put flowAvgCount flowId fac)
       )
  )))
flowAvgCount ;abc
)
