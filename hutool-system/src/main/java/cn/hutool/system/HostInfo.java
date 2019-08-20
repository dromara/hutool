package cn.hutool.system;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 代表当前主机的信息。
 */
public class HostInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String HOST_NAME;
    private final String HOST_ADDRESS;
    private final String HOST_NETWORK_CARD_ADDRESS;

    public HostInfo() {
        String hostName;
        String hostAddress;
        String hostNetworkCardAddress;

        try {
            InetAddress localhost = InetAddress.getLocalHost();

            hostName = localhost.getHostName();
            hostAddress = localhost.getHostAddress();
            hostNetworkCardAddress = getInternetIp();
        } catch (UnknownHostException e) {
            hostName = "localhost";
            hostAddress = "127.0.0.1";
            hostNetworkCardAddress = "127.0.0.1";
        }

        HOST_NAME = hostName;
        HOST_ADDRESS = hostAddress;
        HOST_NETWORK_CARD_ADDRESS = hostNetworkCardAddress;
    }

    /**
     * 取得当前主机的名称。
     *
     * <p>
     * 例如：<code>"webserver1"</code>
     * </p>
     *
     * @return 主机名
     */
    public final String getName() {
        return HOST_NAME;
    }

    /**
     * 取得当前主机的地址。
     *
     * <p>
     * 例如：<code>"192.168.0.1"</code>
     * </p>
     *
     * @return 主机地址
     */
    public final String getAddress() {
        return HOST_ADDRESS;
    }

    /**
     * 取得当前主机的网卡IP地址。
     * <p>
     * 例如：<code>"192.168.0.1"</code>
     * </p>
     *
     * @return 网卡IP地址
     */
    public final String getNetworkCardAddress() {
        return HOST_NETWORK_CARD_ADDRESS;
    }

    /**
     * 获得网卡IP
     *
     * @return 网卡IP
     */
    public static String getInternetIp() {
        String INTRANET_IP = null;
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            Enumeration<InetAddress> addrs;
            while (networks.hasMoreElements()) {
                addrs = networks.nextElement().getInetAddresses();
                while (addrs.hasMoreElements()) {
                    ip = addrs.nextElement();
                    if (ip != null && ip instanceof Inet4Address && ip.isSiteLocalAddress() && !ip.getHostAddress().equals(INTRANET_IP)) {
                        return ip.getHostAddress();
                    }
                }
            }
            // 如果没有网卡IP，就返回/etc/hosts IP
            return INTRANET_IP;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将当前主机的信息转换成字符串。
     *
     * @return 主机信息的字符串表示
     */
    @Override
    public final String toString() {
        StringBuilder builder = new StringBuilder();

        SystemUtil.append(builder, "Host Name:    ", getName());
        SystemUtil.append(builder, "Host Address: ", getAddress());
        SystemUtil.append(builder, "Host NETWORK CARD ADDRESS: ", getNetworkCardAddress());

        return builder.toString();
    }

}
