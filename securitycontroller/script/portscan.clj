(ns com.core)  

(import '(com.sds.securitycontroller.flow FlowInfo))
(import '(com.sds.securitycontroller.event ISubscriptionResult))
(import '(com.sds.securitycontroller.storage IDBObject))
 
 
(defn checkFlow[objFlow refFlow]
	(and 
	(> (.getPacketCount objFlow) 300) 
	(= (.getnetworkDestination objFlow) (.getnetworkDestination refFlow))
	))


;;(defn proccessFlowmapping[flowMapping flowMonitor]
;;	(.getThreshold flowMonitor (str 'traffic'))
;;)

(defn proccessFlowmapping[flowMapping fmi]
	(def portset #{})
	(def flowcount 0)
	(def nostateflowcount 0)
	(def activeport_thres 300)
	(def nostateratio_thres 0.3)
	(doseq [i (iterator-seq (.iterator (.values flowMapping)))] 
		(def dport (.gettransportDestination (.getMatch i)))
		(when (= (.getnetworkDestination i) "100.0.0.14")
			(def flowcount (+ flowcount 1))
			(if (not (contains? portset dport))
				(def portset (conj portset dport))
			)
			(if (< (.getPacketCount i) 3)
				(def nostateflowcount (+ nostateflowcount 1))
			)
		)
	)
	;;(prn (count portset) (/ nostateflowcount flowcount))
	;;(if (> (count portset) 1000) (count portset) 1000) 0) 
	[(count portset) (.size flowMapping) nostateflowcount flowcount]
	;;[(count portset) (if (= flowcount 0) 0 (/ nostateflowcount flowcount) )]
)
