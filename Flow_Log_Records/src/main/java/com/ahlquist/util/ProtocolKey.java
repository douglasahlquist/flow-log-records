package com.ahlquist.util;

import java.util.Objects;

/**
 * Encapsulate the attributes that are used to map the port and protocols
 * This gives options for both TcpIp and UDP protocols
 * The Value of name of the port/protocol mapping will be assigned elsewhere.
 */
public class ProtocolKey {
	
	private Integer port;
	private String protocol;
	public Integer getPort() {
		return port;
	}
	public ProtocolKey setPort(Integer port) {
		this.port = port;
		return this;
	}
	@Override
	public String toString() {
		return "ProtocolKey [port=" + port + ", protocol=" + protocol + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(port, protocol);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProtocolKey other = (ProtocolKey) obj;
		return Objects.equals(port, other.port) && Objects.equals(protocol, other.protocol);
	}
	public ProtocolKey(Integer port, String protocol) {
		super();
		this.port = port;
		this.protocol = protocol;
	}
	public String getProtocol() {
		return protocol;
	}
	public ProtocolKey setProtocol(String protocol) {
		this.protocol = protocol;
		return this;
	}
}
