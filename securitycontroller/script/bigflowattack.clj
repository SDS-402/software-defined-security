(ns com.core)  
(import '(com.sds.securitycontroller.flow.manager FlowMonitor))

(defn proccessFlowmapping[flowMapping objHost]

	(def flowAvgCount (. FlowMonitor getFlowAvgCount))

	(doseq [i (iterator-seq (.iterator (.values flowMapping)))] 

		(def pc (.getPacketCount (.getMatch i)))
		(def bc (.getByteCount (.getMatch i)))

		(if (and 
			(= (.getnetworkDestination i) objHost)
			(not (contains? portset dport))
			)

				(def portset (conj portset dport))

		)
	)
	(if (> (count portset) 200) (count portset) 0)

)
