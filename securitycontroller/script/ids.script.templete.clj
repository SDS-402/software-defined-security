(ns sds.expr.securitydevice)

;(def rule ["10.210.106.21" "10.102.26.104" 6 "any" 8087 ])
(def rule [">>>put your rule here<<<"])
(def ip (new org.jnetpcap.protocol.network.Ip4))

( defn proto[](
		let [num (nth rule 2)]
		(if (= num 1)
			(new org.jnetpcap.protocol.network.Icmp)
			(if (= num 11)
				(new org.jnetpcap.protocol.tcpip.Udp)
				(new org.jnetpcap.protocol.tcpip.Tcp)
			)
		)
	)
)

(defn content[tp](
	let [data (new String (.getPayload tp))]
	(let [subdata (.substring data (nth rule 5))]
		;(.startsWith subdata (nth rule 6))
		(.find (.matcher (java.util.regex.Pattern/compile (nth rule 6)) subdata))
	)
)
)

(
    defn matchPacket[pkt](
		let [tp (proto)]
		(
			and
			(.hasHeader pkt ip) 
			(or (= (nth rule 0) "any") (= (. com.sds.securitydevice.ids.IDSService getSrcIp ip) (nth rule 0))) ;match nwSrc
			(or (= (nth rule 1) "any") (= (. com.sds.securitydevice.ids.IDSService getDstIp ip) (nth rule 1))) ;match nwDst
			(.hasHeader pkt tp)		;nw_proto
			(or (= (nth rule 3) "any") (= (.source tp) (nth rule 3))) ;match tpSrc
			(or (= (nth rule 4) "any") (= (.destination tp) (nth rule 4))) ;match tpDst
			(or (= (get rule 5 "any") "any") (content tp)) ; match content with offset
		)
    )
)
