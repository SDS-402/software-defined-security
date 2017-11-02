(ns com.core)  

(import '(com.sds.securitycontroller.flow FlowInfo))
(import '(com.sds.securitycontroller.event ISubscriptionResult))
(import '(com.sds.securitycontroller.storage IDBObject))
 
 
(defn checkFlow[objFlow refFlow]
	(and 
	(< (.getPacketCount objFlow) 300) 
	(= (.getnetworkDestination objFlow) (.getnetworkDestination refFlow))
	))
